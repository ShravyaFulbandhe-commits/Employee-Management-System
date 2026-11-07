package com.ems.service;

import com.ems.dao.EmployeeDAO;
import com.ems.exception.ValidationException;
import com.ems.model.Employee;

import java.util.List;
import java.util.regex.Pattern;

/** Business logic and validation layer sitting between the UI and the DAO. */
public class EmployeeService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final EmployeeDAO dao;

    public EmployeeService(EmployeeDAO dao) {
        this.dao = dao;
    }

    public int addEmployee(Employee e) {
        validate(e);
        if (dao.emailExists(e.getEmail(), 0)) {
            throw new ValidationException("An employee with this email already exists.");
        }
        return dao.add(e);
    }

    public Employee getEmployee(int id) {
        return dao.findById(id)
                .orElseThrow(() -> new ValidationException("No employee found with ID " + id));
    }

    public List<Employee> getAllEmployees() {
        return dao.findAll();
    }

    public List<Employee> searchByName(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            throw new ValidationException("Search keyword cannot be empty.");
        }
        return dao.searchByName(keyword.trim());
    }

    public List<Employee> getByDepartment(String department) {
        if (department == null || department.isBlank()) {
            throw new ValidationException("Department cannot be empty.");
        }
        return dao.findByDepartment(department.trim());
    }

    public void updateEmployee(Employee e) {
        validate(e);
        if (dao.emailExists(e.getEmail(), e.getId())) {
            throw new ValidationException("Another employee already uses this email.");
        }
        if (!dao.update(e)) {
            throw new ValidationException("No employee found with ID " + e.getId());
        }
    }

    public void deleteEmployee(int id) {
        if (!dao.delete(id)) {
            throw new ValidationException("No employee found with ID " + id);
        }
    }

    private void validate(Employee e) {
        if (e.getName() == null || e.getName().isBlank()) {
            throw new ValidationException("Name is required.");
        }
        if (e.getEmail() == null || !EMAIL_PATTERN.matcher(e.getEmail()).matches()) {
            throw new ValidationException("Please enter a valid email address.");
        }
        if (e.getDepartment() == null || e.getDepartment().isBlank()) {
            throw new ValidationException("Department is required.");
        }
        if (e.getDesignation() == null || e.getDesignation().isBlank()) {
            throw new ValidationException("Designation is required.");
        }
        if (e.getSalary() < 0) {
            throw new ValidationException("Salary cannot be negative.");
        }
        if (e.getJoinDate() == null) {
            throw new ValidationException("Join date is required.");
        }
    }
}
