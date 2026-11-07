CREATE DATABASE IF NOT EXISTS employee_db;
USE employee_db;

CREATE TABLE IF NOT EXISTS employees (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,
    email       VARCHAR(100)  NOT NULL UNIQUE,
    department  VARCHAR(50)   NOT NULL,
    designation VARCHAR(50)   NOT NULL,
    salary      DECIMAL(10,2) NOT NULL CHECK (salary >= 0),
    join_date   DATE          NOT NULL
);

-- Optional sample data
INSERT INTO employees (name, email, department, designation, salary, join_date) VALUES
('Amit Sharma',  'amit.sharma@example.com',  'Engineering', 'Software Engineer', 60000.00, '2023-06-15'),
('Priya Patil',  'priya.patil@example.com',  'HR',          'HR Executive',      45000.00, '2022-09-01'),
('Rahul Verma',  'rahul.verma@example.com',  'Finance',     'Accountant',        50000.00, '2021-03-20');
