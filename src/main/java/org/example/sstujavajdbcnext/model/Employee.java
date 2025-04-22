package org.example.sstujavajdbcnext.model;

import org.example.sstujavajdbcnext.annotation.Column;
import org.example.sstujavajdbcnext.annotation.Table;

@Table("employee")
public class Employee {

    @Column("id")
    Integer id;

    @Column("name")
    String name;

    @Column("dep_id")
    Integer depId;

    public Employee() {

    }

    public Employee(Integer id, String name, Integer depId) {
        this.id = id;
        this.name = name;
        this.depId = depId;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", depId=" + depId +
                '}';
    }
}