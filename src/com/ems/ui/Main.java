package com.ems.ui;

import com.ems.dao.EmployeeDAOImpl;
import com.ems.exception.DataAccessException;
import com.ems.exception.ValidationException;
import com.ems.model.Employee;
import com.ems.service.EmployeeService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/** Console-based user interface for the Employee Management System. */
public class Main {

    private static final Scanner SC = new Scanner(System.in);
    private static final EmployeeService SERVICE = new EmployeeService(new EmployeeDAOImpl());

    public static void main(String[] args) {
        System.out.println("=== Employee Management System ===");
        boolean running = true;
        while (running) {
            printMenu();
            try {
                int choice = readInt("Enter your choice: ");
                switch (choice) {
                    case 1 -> addEmployee();
                    case 2 -> viewAll();
                    case 3 -> viewById();
                    case 4 -> updateEmployee();
                    case 5 -> deleteEmployee();
                    case 6 -> searchByName();
                    case 7 -> filterByDepartment();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (ValidationException e) {
                System.out.println("Validation error: " + e.getMessage());
            } catch (DataAccessException e) {
                System.out.println("Database error: " + e.getMessage());
            }
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. View Employee by ID");
        System.out.println("4. Update Employee");
        System.out.println("5. Delete Employee");
        System.out.println("6. Search by Name");
        System.out.println("7. Filter by Department");
        System.out.println("0. Exit");
    }

    private static void addEmployee() {
        Employee e = readEmployeeDetails(null);
        int id = SERVICE.addEmployee(e);
        System.out.println("Employee added successfully with ID " + id);
    }

    private static void viewAll() {
        printTable(SERVICE.getAllEmployees());
    }

    private static void viewById() {
        int id = readInt("Enter employee ID: ");
        Employee e = SERVICE.getEmployee(id);
        printTable(List.of(e));
    }

    private static void updateEmployee() {
        int id = readInt("Enter ID of employee to update: ");
        Employee existing = SERVICE.getEmployee(id);
        System.out.println("Leave a field blank to keep the current value.");
        Employee updated = readEmployeeDetails(existing);
        updated.setId(id);
        SERVICE.updateEmployee(updated);
        System.out.println("Employee updated successfully.");
    }

    private static void deleteEmployee() {
        int id = readInt("Enter ID of employee to delete: ");
        Employee e = SERVICE.getEmployee(id);
        System.out.println("About to delete: " + e.getName());
        String confirm = readLine("Are you sure? (y/n): ");
        if (confirm.equalsIgnoreCase("y")) {
            SERVICE.deleteEmployee(id);
            System.out.println("Employee deleted.");
        } else {
            System.out.println("Cancelled.");
        }
    }

    private static void searchByName() {
        String keyword = readLine("Enter name to search: ");
        printTable(SERVICE.searchByName(keyword));
    }

    private static void filterByDepartment() {
        String dept = readLine("Enter department: ");
        printTable(SERVICE.getByDepartment(dept));
    }

    /** Reads employee fields. If existing is non-null, blank input keeps the old value. */
    private static Employee readEmployeeDetails(Employee existing) {
        boolean edit = existing != null;
        Employee e = new Employee();

        e.setName(readOrKeep("Name", edit ? existing.getName() : null));
        e.setEmail(readOrKeep("Email", edit ? existing.getEmail() : null));
        e.setDepartment(readOrKeep("Department", edit ? existing.getDepartment() : null));
        e.setDesignation(readOrKeep("Designation", edit ? existing.getDesignation() : null));

        String salaryInput = readOrKeep("Salary", edit ? String.valueOf(existing.getSalary()) : null);
        try {
            e.setSalary(Double.parseDouble(salaryInput));
        } catch (NumberFormatException ex) {
            throw new ValidationException("Salary must be a valid number.");
        }

        String dateInput = readOrKeep("Join date (yyyy-MM-dd)", edit ? existing.getJoinDate().toString() : null);
        try {
            e.setJoinDate(LocalDate.parse(dateInput));
        } catch (DateTimeParseException ex) {
            throw new ValidationException("Date must be in yyyy-MM-dd format.");
        }
        return e;
    }

    private static String readOrKeep(String label, String current) {
        String prompt = current == null ? label + ": " : label + " [" + current + "]: ";
        String input = readLine(prompt);
        return (input.isBlank() && current != null) ? current : input;
    }

    private static void printTable(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No records found.");
            return;
        }
        System.out.println(String.format("%-5s %-20s %-28s %-14s %-20s %-10s %s",
                "ID", "Name", "Email", "Department", "Designation", "Salary", "Joined"));
        System.out.println("-".repeat(115));
        employees.forEach(System.out::println);
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return SC.nextLine().trim();
    }

    private static int readInt(String prompt) {
        String input = readLine(prompt);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new ValidationException("Please enter a valid number.");
        }
    }
}
