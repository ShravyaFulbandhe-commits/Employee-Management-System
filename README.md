# Employee Management System

A console-based Employee Management application built with **Java, JDBC and MySQL**.

## Features
- Add, view, update and delete employee records (CRUD)
- Search employees by name, filter by department
- Input validation (email format, duplicate email, salary, date)
- Safe database access with `PreparedStatement` (prevents SQL injection)
- Layered architecture: Model -> DAO -> Service -> UI

## Project Structure
```
sql/schema.sql                      Database + table + sample data
src/com/ems/model/Employee.java     Entity class
src/com/ems/util/DBConnection.java  JDBC connection helper
src/com/ems/dao/                    DAO interface + JDBC implementation
src/com/ems/service/                Business logic and validation
src/com/ems/exception/              Custom exceptions
src/com/ems/ui/Main.java            Console menu (entry point)
```

## Prerequisites
- JDK 17 or later
- MySQL Server 8.x
- MySQL Connector/J jar (download from https://dev.mysql.com/downloads/connector/j/)

## Setup
1. Create the database:
   ```
   mysql -u root -p < sql/schema.sql
   ```
2. Set your MySQL password. Either edit `DBConnection.java`, or set environment variables:
   ```
   export EMS_DB_USER=root
   export EMS_DB_PASSWORD=your_password
   ```
   (On Windows CMD use `set EMS_DB_PASSWORD=your_password`)
3. Compile (put the connector jar in a `lib/` folder):
   ```
   mkdir out
   javac -d out -cp "lib/*" src/com/ems/*/*.java
   ```
4. Run:
   - Linux/macOS: `java -cp "out:lib/*" com.ems.ui.Main`
   - Windows:     `java -cp "out;lib/*" com.ems.ui.Main`

## Running in an IDE
Open the folder in IntelliJ IDEA / Eclipse / VS Code, add the Connector/J jar to the
project libraries, mark `src` as the source root, and run `com.ems.ui.Main`.
