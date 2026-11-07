package com.ems.dao;

import com.ems.exception.DataAccessException;
import com.ems.model.Employee;
import com.ems.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** JDBC implementation of EmployeeDAO. Uses PreparedStatement everywhere to prevent SQL injection. */
public class EmployeeDAOImpl implements EmployeeDAO {

    private static final String INSERT =
            "INSERT INTO employees (name, email, department, designation, salary, join_date) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_ID = "SELECT * FROM employees WHERE id = ?";
    private static final String SELECT_ALL = "SELECT * FROM employees ORDER BY id";
    private static final String SEARCH_BY_NAME = "SELECT * FROM employees WHERE name LIKE ? ORDER BY id";
    private static final String SELECT_BY_DEPT = "SELECT * FROM employees WHERE department = ? ORDER BY id";
    private static final String UPDATE =
            "UPDATE employees SET name = ?, email = ?, department = ?, designation = ?, salary = ?, join_date = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM employees WHERE id = ?";
    private static final String EMAIL_EXISTS = "SELECT COUNT(*) FROM employees WHERE email = ? AND id <> ?";

    @Override
    public int add(Employee e) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getDepartment());
            ps.setString(4, e.getDesignation());
            ps.setDouble(5, e.getSalary());
            ps.setDate(6, Date.valueOf(e.getJoinDate()));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to add employee: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Employee> findById(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to fetch employee: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Employee> findAll() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            return mapRows(rs);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to fetch employees: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Employee> searchByName(String keyword) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SEARCH_BY_NAME)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return mapRows(rs);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Search failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Employee> findByDepartment(String department) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_DEPT)) {
            ps.setString(1, department);
            try (ResultSet rs = ps.executeQuery()) {
                return mapRows(rs);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Department search failed: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean update(Employee e) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE)) {
            ps.setString(1, e.getName());
            ps.setString(2, e.getEmail());
            ps.setString(3, e.getDepartment());
            ps.setString(4, e.getDesignation());
            ps.setDouble(5, e.getSalary());
            ps.setDate(6, Date.valueOf(e.getJoinDate()));
            ps.setInt(7, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update employee: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete employee: " + ex.getMessage(), ex);
        }
    }

    @Override
    public boolean emailExists(String email, int excludeId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(EMAIL_EXISTS)) {
            ps.setString(1, email);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Email check failed: " + ex.getMessage(), ex);
        }
    }

    private List<Employee> mapRows(ResultSet rs) throws SQLException {
        List<Employee> list = new ArrayList<>();
        while (rs.next()) {
            list.add(mapRow(rs));
        }
        return list;
    }

    private Employee mapRow(ResultSet rs) throws SQLException {
        return new Employee(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("department"),
                rs.getString("designation"),
                rs.getDouble("salary"),
                rs.getDate("join_date").toLocalDate());
    }
}
