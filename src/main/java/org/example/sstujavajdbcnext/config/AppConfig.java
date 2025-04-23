package org.example.sstujavajdbcnext.config;

import org.example.sstujavajdbcnext.converter.ConverterContext;
import org.example.sstujavajdbcnext.operator.OperatorContext;
import org.example.sstujavajdbcnext.proxy.DaoProxy;
import org.example.sstujavajdbcnext.repository.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class AppConfig {

    private OperatorContext operatorContext;

    private ConverterContext converterContext;

    public AppConfig(OperatorContext operatorContext, ConverterContext converterContext) {
        this.operatorContext = operatorContext;
        this.converterContext = converterContext;
    }

    @Bean
    public Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5435/postgres", "test", "test");
    }

    @Bean
    public Repository daoIfaceTest() throws SQLException {
        return DaoProxy.create(Repository.class, connection(), operatorContext, converterContext);
    }
}
