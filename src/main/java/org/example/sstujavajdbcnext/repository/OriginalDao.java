package org.example.sstujavajdbcnext.repository;

import org.example.sstujavajdbcnext.annotation.Column;
import org.example.sstujavajdbcnext.annotation.Table;
import org.example.sstujavajdbcnext.converter.Converter;
import org.example.sstujavajdbcnext.converter.ConverterContext;
import org.example.sstujavajdbcnext.exception.DatabaseAccessException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OriginalDao {

    private final Connection conn;

    private final ConverterContext converterContext;

    public OriginalDao(Connection conn, ConverterContext converterContext) {
        this.conn = conn;
        this.converterContext = converterContext;
    }

    public <T> List<T> findAll(Class<T> clazz) {
        List<T> result = new ArrayList<T>();

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

        try (PreparedStatement statement = conn.prepareStatement(String.format("SELECT %s FROM %s", fieldsClause, tableName))) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                T obj = clazz.getConstructor().newInstance();
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
}