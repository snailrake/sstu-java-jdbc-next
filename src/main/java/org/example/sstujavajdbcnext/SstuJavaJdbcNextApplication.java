package org.example.sstujavajdbcnext;

import org.example.sstujavajdbcnext.model.Employee;
import org.example.sstujavajdbcnext.repository.Repository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SstuJavaJdbcNextApplication implements CommandLineRunner {

    private Repository dao;

    public SstuJavaJdbcNextApplication(Repository dao) {
        this.dao = dao;
    }

    public static void main(String[] args) {
        SpringApplication.run(SstuJavaJdbcNextApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Employee> employees = dao.findByIdEqualsDepidGreater(Employee.class, 1, 9);
        System.out.println(employees);
    }
}
