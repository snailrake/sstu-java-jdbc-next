//package org.example.sstujavajdbcnext.postprocessors;
//
//import org.example.sstujavajdbcnext.converter.ConverterContext;
//import org.example.sstujavajdbcnext.operation.OperatorContext;
//import org.example.sstujavajdbcnext.proxy.DaoProxy;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//
//@Component
//public class DaoBeanPostProcessor implements BeanPostProcessor {
//
//    private Connection connection;
//
//    private OperatorContext operatorContext;
//
//    private ConverterContext converterContext;
//
//    public DaoBeanPostProcessor(Connection connection, OperatorContext operatorContext, ConverterContext converterContext) {
//        this.connection = connection;
//        this.operatorContext = operatorContext;
//        this.converterContext = converterContext;
//    }
//
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (isImplementsDao(bean.getClass())) {
//            return DaoProxy.create(bean.getClass(), connection, operatorContext, converterContext);
//        }
//        return bean;
//    }
//
//    private boolean isImplementsDao(Class<?> clazz) {
//        for (Class<?> iface : clazz.getInterfaces()) {
//            if (iface.equals(Dao.class)) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
