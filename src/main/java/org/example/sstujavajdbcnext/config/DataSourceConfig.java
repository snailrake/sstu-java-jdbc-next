package org.example.sstujavajdbcnext.config;

import org.example.sstujavajdbcnext.converter.ConverterContext;
import org.example.sstujavajdbcnext.operation.OperatorContext;
import org.example.sstujavajdbcnext.proxy.DaoProxy;
import org.example.sstujavajdbcnext.repository.DaoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DataSourceConfig {

    private OperatorContext operatorContext;

    private ConverterContext converterContext;

    public DataSourceConfig(OperatorContext operatorContext, ConverterContext converterContext) {
        this.operatorContext = operatorContext;
        this.converterContext = converterContext;
    }

    @Bean
    public Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:postgresql://localhost:5435/postgres", "test", "test");
    }

    @Bean
    public DaoTest daoTest() throws SQLException {
        return DaoProxy.create(DaoTest.class, connection(), operatorContext, converterContext);
    }
}
