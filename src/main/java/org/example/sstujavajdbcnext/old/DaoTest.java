package org.example.sstujavajdbcnext.old;

import org.example.sstujavajdbcnext.model.Employee;
import org.example.sstujavajdbcnext.repository.Dao;

import java.util.List;

public abstract class DaoTest implements Dao {

    public abstract List<Employee> findByIdEqualsDepidGreater(Class<?> clazz, Integer id, Integer depid);
}
