# Student Management System

A console-based Java application for managing student information using JDBC and MySQL database.

## Features

### Core CRUD Operations
-  **Create**: Add new student records with comprehensive validation
-  **Read**: View all students or search by specific criteria  
-  **Update**: Modify existing student information
-  **Delete**: Remove student records with confirmation

### Advanced Features
-  **Search by Name**: Find students by first name or last name
-  **Filter by Major**: View students enrolled in specific majors
-  **GPA Filtering**: Find students with GPA above a threshold
-  **Input Validation**: Comprehensive validation for all user inputs
-  **Transaction Handling**: Safe database operations with rollback support
-  **Email Uniqueness**: Prevents duplicate email addresses
-  **User-Friendly Interface**: Well-formatted console output with visual elements

## Technology Stack

- **Java Version**: 17.0.11
- **Database**: MySQL 8.0+ (via XAMPP)
- **JDBC Driver**: MySQL Connector/J
- **Development Environment**: IntelliJ IDEA
- **Database Management**: phpMyAdmin (XAMPP)

## Prerequisites

1. **Java 17.0.11** installed and configured
2. **IntelliJ IDEA** (Community or Ultimate Edition)
3. **XAMPP** with MySQL and Apache running
4. **MySQL Connector/J** JDBC driver

## Setup Instructions

### Step 1: Install and Configure XAMPP

1. Download and install [XAMPP](https://www.apachefriends.org/download.html)
2. Start XAMPP Control Panel
3. Start **Apache** and **MySQL** services
4. Verify MySQL is running on port 3306

### Step 2: Create Database

1. Open your web browser and go to `http://localhost/phpmyadmin`
2. Click **"SQL"** tab
3. Copy and paste the contents of `database_schema.sql`
4. Click **"Go"** to execute the script
5. Verify that the `student_management` database and `students` table are created

### Step 3: Download MySQL JDBC Driver

1. Download MySQL Connector/J from [MySQL official website](https://dev.mysql.com/downloads/connector/j/)
2. Extract the downloaded ZIP file
3. Locate the `mysql-connector-java-x.x.x.jar` file

### Step 4: Setup IntelliJ IDEA Project

1. Open IntelliJ IDEA
2. Create a new Java project or open existing one
3. Set Project SDK to Java 17
4. Create the following directory structure:
   ```
   src/
   ├── com/
   │   └── studentmanagement/
   │       ├── StudentManagementApp.java
   │       ├── dao/
   │       │   └── StudentDAO.java
   │       ├── model/
   │       │   └── Student.java
   │       ├── service/
   │       │   └── StudentService.java
   │       ├── ui/
   │       │   └── ConsoleUI.java
   │       └── util/
   │           ├── DatabaseConnection.java
   │           └── InputValidator.java
   ```

### Step 5: Add JDBC Driver to Project

1. In IntelliJ IDEA, right-click on your project
2. Select **"Open Module Settings"** or press `F4`
3. Go to **"Libraries"** tab
4. Click **"+"** → **"Java"**
5. Navigate to and select the MySQL Connector JAR file
6. Click **"OK"** to add it to your project

### Step 6: Copy Source Code

1. Copy all the provided Java source files to their respective packages
2. Ensure all files are in the correct directory structure
3. Verify that package declarations match the directory structure

### Step 7: Configure Database Connection

Open `DatabaseConnection.java` and verify the connection parameters:
```java
private static final String URL = "jdbc:mysql://localhost:3306/student_management";
private static final String USERNAME = "root";
private static final String PASSWORD = ""; // Default XAMPP password is empty
```

### Step 8: Run the Application

1. Open `StudentManagementApp.java`
2. Right-click and select **"Run 'StudentManagementApp'"**
3. The console application should start and display the main menu

## Database Configuration

### Default Connection Settings
- **Host**: localhost
- **Port**: 3306
- **Database**: student_management
- **Username**: root
- **Password**: (empty - default XAMPP setting)

### Database Schema
The `students` table contains:
- `student_id` (Primary Key, Auto Increment)
- `first_name` (VARCHAR(50), NOT NULL)
- `last_name` (VARCHAR(50), NOT NULL) 
- `email` (VARCHAR(100), UNIQUE, NOT NULL)
- `phone` (VARCHAR(20), NOT NULL)
- `date_of_birth` (DATE, NOT NULL)
- `major` (VARCHAR(100), NOT NULL)
- `gpa` (DECIMAL(3,2), NOT NULL, 0.0-4.0)
- `created_at` (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)
- `updated_at` (TIMESTAMP, ON UPDATE CURRENT_TIMESTAMP)

## Usage Guide

### Main Menu Options

1. **Add New Student**: Create a new student record with validation
2. **View All Students**: Display all students in tabular format
3. **View Student by ID**: Search and display specific student
4. **Update Student**: Modify existing student information
5. **Delete Student**: Remove student record with confirmation
6. **Search Students by Name**: Find students by name (partial matching)
7. **Filter Students by Major**: View students in specific major
8. **Filter Students by GPA**: Find students with GPA above threshold
9. **Exit**: Close the application safely

### Input Validation Rules

- **Names**: Must contain only letters and spaces, minimum 2 characters
- **Email**: Must follow standard email format (user@domain.com)
- **Phone**: Must be 10-15 digits, can include +, -, (), and spaces
- **Date of Birth**: Format yyyy-MM-dd, must be between 1900-01-01 and today
- **Major**: Must be 2-50 characters long
- **GPA**: Must be between 0.0 and 4.0

## Troubleshooting

### Common Issues and Solutions

**1. "Database connection failed"**
- Ensure XAMPP MySQL service is running
- Check if port 3306 is not blocked by firewall
- Verify database name and credentials in `DatabaseConnection.java`

**2. "MySQL JDBC Driver not found"**
- Ensure MySQL Connector JAR is added to project libraries
- Check that the JAR file is not corrupted
- Verify Java build path includes the JDBC driver

**3. "Table doesn't exist"**
- Run the `database_schema.sql` script in phpMyAdmin
- Ensure you're using the correct database name
- Check if the SQL script executed without errors

**4. "Access denied for user 'root'"**
- Default XAMPP MySQL has no password for root user
- If you've set a password, update `DatabaseConnection.java`
- Try resetting MySQL password in XAMPP

**5. "Port 3306 already in use"**
- Stop other MySQL services running on your system
- Change MySQL port in XAMPP configuration
- Update connection URL in `DatabaseConnection.java` if port is changed

## Project Structure

```
StudentManagementSystem/
├── src/
│   └── com/
│       └── studentmanagement/
│           ├── StudentManagementApp.java      # Main application entry point
│           ├── dao/
│           │   └── StudentDAO.java            # Data Access Object
│           ├── model/
│           │   └── Student.java               # Student entity model
│           ├── service/
│           │   └── StudentService.java        # Business logic layer
│           ├── ui/
│           │   └── ConsoleUI.java             # Console user interface
│           └── util/
│               ├── DatabaseConnection.java     # Database connectivity
│               └── InputValidator.java         # Input validation utility
├── database_schema.sql                        # Database setup script
└── README.md                                  # This file
```

## Assumptions and Notes

### Assumptions Made
1. **XAMPP Default Configuration**: Using default XAMPP MySQL settings (root user, no password)
2. **Single User Environment**: Application designed for single-user console access
3. **Local Database**: Database runs locally on the same machine
4. **UTF-8 Encoding**: All text data uses UTF-8 character encoding
5. **GPA Scale**: Using 4.0 GPA scale (common in US education system)

### Important Notes
- The application uses PreparedStatements to prevent SQL injection
- Database connections are properly managed with try-with-resources
- Input validation is performed at multiple layers (UI, Service, and Database)
- Transaction handling ensures data consistency during operations
- Email addresses must be unique across all student records
- All database operations include proper exception handling
- The console interface provides user-friendly formatting and feedback

### Security Considerations
- Input validation prevents malicious data entry
- PreparedStatements protect against SQL injection attacks  
- Database credentials should be externalized in production environments
- Consider adding user authentication for production use

## Support

For issues or questions:
1. Check the troubleshooting section above
2. Verify all setup steps are completed correctly
3. Ensure all prerequisites are properly installed
4. Check database connectivity and permissions
