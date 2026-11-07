package com.ems.model;

import java.time.LocalDate;

/** Model class representing one row of the employees table. */
public class Employee {
    private int id;
    private String name;
    private String email;
    private String department;
    private String designation;
    private double salary;
    private LocalDate joinDate;

    public Employee() { }

    public Employee(int id, String name, String email, String department,
                    String designation, double salary, LocalDate joinDate) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
        this.designation = designation;
        this.salary = salary;
        this.joinDate = joinDate;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public LocalDate getJoinDate() { return joinDate; }
    public void setJoinDate(LocalDate joinDate) { this.joinDate = joinDate; }

    @Override
    public String toString() {
        return String.format("%-5d %-20s %-28s %-14s %-20s %-10.2f %s",
                id, name, email, department, designation, salary, joinDate);
    }
}
