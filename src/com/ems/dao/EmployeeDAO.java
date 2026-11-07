package com.ems.dao;

import com.ems.model.Employee;

import java.util.List;
import java.util.Optional;

/** Abstraction over employee persistence. */
public interface EmployeeDAO {
    int add(Employee employee);
    Optional<Employee> findById(int id);
    List<Employee> findAll();
    List<Employee> searchByName(String keyword);
    List<Employee> findByDepartment(String department);
    boolean update(Employee employee);
    boolean delete(int id);
    boolean emailExists(String email, int excludeId);
}
