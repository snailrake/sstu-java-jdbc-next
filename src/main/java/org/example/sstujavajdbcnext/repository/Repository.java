package org.example.sstujavajdbcnext.repository;

import org.example.sstujavajdbcnext.model.Employee;

import java.util.List;

public interface Repository extends Dao {

    List<Employee> findByIdEqualsDepidGreater(Class<?> clazz, Integer id, Integer depid);
}
