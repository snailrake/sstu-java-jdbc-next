package org.example.sstujavajdbcnext.proxy;

import org.example.sstujavajdbcnext.Operation;
import org.example.sstujavajdbcnext.annotation.Column;
import org.example.sstujavajdbcnext.annotation.Table;
import org.example.sstujavajdbcnext.converter.Converter;
import org.example.sstujavajdbcnext.converter.ConverterContext;
import org.example.sstujavajdbcnext.exception.DatabaseAccessException;
import org.example.sstujavajdbcnext.operation.Operator;
import org.example.sstujavajdbcnext.operation.OperatorContext;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class DaoProxy {

    public static <T> T create(Class<T> clazz, Connection connection,
                               OperatorContext operatorContext, ConverterContext converterContext) {
        return (T) Enhancer.create(
                clazz,
                new DaoProxyHandler(connection, operatorContext, converterContext)
        );
    }


    private static class DaoProxyHandler implements InvocationHandler {

        private Connection connection;

        private OperatorContext operatorContext;

        private ConverterContext converterContext;

        private DaoProxyHandler(Connection connection, OperatorContext operatorContext, ConverterContext converterContext) {
            this.connection = connection;
            this.operatorContext = operatorContext;
            this.converterContext = converterContext;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Class<?> clazz = (Class<?>) args[0];
            String filters = method.getName().substring("findBy".length());
            List<Object> result = new ArrayList<>();

            String tableName = clazz.getSimpleName();
            if (clazz.isAnnotationPresent(Table.class)) {
                tableName = clazz.getAnnotation(Table.class).value();
            }

            String fieldsClause = Arrays.stream(clazz.getDeclaredFields())
                    .map(field -> {
                        if (field.isAnnotationPresent(Column.class)) {
                            return field.getAnnotation(Column.class).value();
                        }
                        return field.getName();
                    }).collect(Collectors.joining(", "));

            List<String> split = splitMethodToOperators(filters);
            List<Operation> operations = new ArrayList<>();
            StringBuilder whereCondition = new StringBuilder();
            List<Object> params = new ArrayList<>();

            for (int i = 0; i < split.size() - 1; i += 2) {
                operations.add(new Operation(split.get(i + 1), split.get(i)));
            }

            for (int i = 0; i < operations.size(); i++) {
                Operation operation = operations.get(i);
                Operator operator = operatorContext.getOperator(operation.getOperator());
                operator.apply(getColumnName(clazz, operation.getField()), args[i + 1], whereCondition, params);
            }

            String sql = String.format("SELECT %s FROM %s WHERE %s", fieldsClause, tableName, whereCondition);

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (int i = 1; i < params.size() + 1; i++) {
                    statement.setObject(i, params.get(i - 1));
                }
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    Object obj = clazz.getConstructor().newInstance();
                    for (Field field : clazz.getDeclaredFields()) {
                        String fieldName = field.getName();
                        String columnName = field.getName();
                        if (field.isAnnotationPresent(Column.class)) {
                            columnName = field.getAnnotation(Column.class).value();
                        }
                        String setterName = String.format("set%s%s", fieldName.substring(0, 1).toUpperCase(),
                                fieldName.substring(1));
                        Method setter = obj.getClass().getMethod(setterName, field.getType());
                        String fieldResult = resultSet.getString(columnName);
                        Converter<?> converter = converterContext.getConverter(field.getType());
                        setter.invoke(obj, converter.convert(fieldResult));
                    }
                    result.add(obj);
                }
                return result;
            } catch (SQLException e) {
                throw new DatabaseAccessException(String.format("Ошибка выполнения SQL запроса, отсутствует таблица %s " +
                        "в базе данных или база данных недоступна", tableName), e);
            } catch (Exception e) {
                throw new DatabaseAccessException(String.format("Не удалось создать и наполнить объект класса %s, " +
                        "отсутствует публичный конструктор или сеттеры полей", clazz.getSimpleName()), e);
            }
        }

        private List<String> splitMethodToOperators(String method) {
            List<String> result = new ArrayList<>();
            StringBuilder current = new StringBuilder();

            for (char c : method.toCharArray()) {
                if (Character.isUpperCase(c) && !current.isEmpty()) {
                    result.add(current.toString());
                    current = new StringBuilder();
                }
                current.append(c);
            }
            result.add(current.toString());

            return result;
        }

        private String getColumnName(Class<?> clazz, String fieldName) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase(fieldName)) {
                    if (field.isAnnotationPresent(Column.class)) {
                        return field.getAnnotation(Column.class).value();
                    }
                    return field.getName();
                }
            }
            return null;
        }
    }
}
