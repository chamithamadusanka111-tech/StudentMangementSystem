package com.studentmanagement.service;

import com.studentmanagement.dao.StudentDAO;
import com.studentmanagement.model.Student;
import com.studentmanagement.util.DatabaseConnection;
import com.studentmanagement.util.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Business logic service for Student operations
 */
public class StudentService {
    private StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    /**
     * Create a new student with validation
     * @param firstName Student's first name
     * @param lastName Student's last name
     * @param email Student's email
     * @param phone Student's phone
     * @param dateOfBirth Student's date of birth
     * @param major Student's major
     * @param gpa Student's GPA
     * @return Validation result and student ID if successful
     */
    public ServiceResult<Integer> createStudent(String firstName, String lastName, String email,
                                                String phone, LocalDate dateOfBirth, String major, double gpa) {
        // Validate input
        ValidationResult validation = validateStudentData(firstName, lastName, email, phone, dateOfBirth, major, gpa);
        if (!validation.isValid()) {
            return ServiceResult.failure(validation.getErrorMessage());
        }

        // Check if email already exists
        if (isEmailExists(email)) {
            return ServiceResult.failure("Email already exists in the database");
        }

        try {
            Student student = new Student(firstName, lastName, email, phone, dateOfBirth, major, gpa);
            boolean success = studentDAO.createStudent(student);

            if (success) {
                return ServiceResult.success(student.getStudentId());
            } else {
                return ServiceResult.failure("Failed to create student record");
            }
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }

    /**
     * Update student information with validation
     * @param studentId Student ID to update
     * @param firstName Updated first name
     * @param lastName Updated last name
     * @param email Updated email
     * @param phone Updated phone
     * @param dateOfBirth Updated date of birth
     * @param major Updated major
     * @param gpa Updated GPA
     * @return Service result indicating success or failure
     */
    public ServiceResult<Boolean> updateStudent(int studentId, String firstName, String lastName,
                                                String email, String phone, LocalDate dateOfBirth,
                                                String major, double gpa) {
        // Check if student exists
        Student existingStudent = studentDAO.getStudentById(studentId);
        if (existingStudent == null) {
            return ServiceResult.failure("Student with ID " + studentId + " not found");
        }

        // Validate input
        ValidationResult validation = validateStudentData(firstName, lastName, email, phone, dateOfBirth, major, gpa);
        if (!validation.isValid()) {
            return ServiceResult.failure(validation.getErrorMessage());
        }

        // Check if email already exists for different student
        if (!existingStudent.getEmail().equals(email) && isEmailExists(email)) {
            return ServiceResult.failure("Email already exists for another student");
        }

        try {
            Student updatedStudent = new Student(studentId, firstName, lastName, email, phone, dateOfBirth, major, gpa);
            boolean success = studentDAO.updateStudent(updatedStudent);

            if (success) {
                return ServiceResult.success(true);
            } else {
                return ServiceResult.failure("Failed to update student record");
            }
        } catch (Exception e) {
            return ServiceResult.failure("Database error: " + e.getMessage());
        }
    }

    /**
     * Delete student with transaction handling
     * @param studentId Student ID to delete
     * @return Service result indicating success or failure
     */
    public ServiceResult<Boolean> deleteStudent(int studentId) {
        // Check if student exists
        Student existingStudent = studentDAO.getStudentById(studentId);
        if (existingStudent == null) {
            return ServiceResult.failure("Student with ID " + studentId + " not found");
        }

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            boolean success = studentDAO.deleteStudent(studentId);

            if (success) {
                conn.commit(); // Commit transaction
                return ServiceResult.success(true);
            } else {
                conn.rollback(); // Rollback transaction
                return ServiceResult.failure("Failed to delete student record");
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
            } catch (SQLException rollbackEx) {
                // Log rollback error
            }
            return ServiceResult.failure("Database error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                }
            } catch (SQLException e) {
                // Log error
            }
        }
    }

    /**
     * Get all students
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    /**
     * Get student by ID
     * @param studentId Student ID to search for
     * @return Student if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    /**
     * Search students by name
     * @param name Name to search for
     * @return List of matching students
     */
    public List<Student> searchStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return List.of(); // Return empty list for invalid input
        }
        return studentDAO.searchStudentsByName(name.trim());
    }

    /**
     * Get students by major
     * @param major Major to filter by
     * @return List of students with the specified major
     */
    public List<Student> getStudentsByMajor(String major) {
        if (major == null || major.trim().isEmpty()) {
            return List.of();
        }
        return studentDAO.getStudentsByMajor(major.trim());
    }

    /**
     * Get students with high GPA
     * @param minGpa Minimum GPA threshold
     * @return List of students with GPA above threshold
     */
    public List<Student> getStudentsWithHighGPA(double minGpa) {
        if (!InputValidator.isValidGPA(minGpa)) {
            return List.of();
        }
        return studentDAO.getStudentsWithHighGPA(minGpa);
    }

    /**
     * Validate student data
     * @return ValidationResult with validation status and error messages
     */
    private ValidationResult validateStudentData(String firstName, String lastName, String email,
                                                 String phone, LocalDate dateOfBirth, String major, double gpa) {
        if (!InputValidator.isValidName(firstName)) {
            return ValidationResult.invalid("Invalid first name. Must contain only letters and be at least 2 characters long.");
        }
        if (!InputValidator.isValidName(lastName)) {
            return ValidationResult.invalid("Invalid last name. Must contain only letters and be at least 2 characters long.");
        }
        if (!InputValidator.isValidEmail(email)) {
            return ValidationResult.invalid("Invalid email format.");
        }
        if (!InputValidator.isValidPhone(phone)) {
            return ValidationResult.invalid("Invalid phone number format.");
        }
        if (dateOfBirth == null || dateOfBirth.isAfter(LocalDate.now()) || dateOfBirth.isBefore(LocalDate.of(1900, 1, 1))) {
            return ValidationResult.invalid("Invalid date of birth. Must be between 1900-01-01 and today.");
        }
        if (!InputValidator.isValidMajor(major)) {
            return ValidationResult.invalid("Invalid major. Must be at least 2 characters long.");
        }
        if (!InputValidator.isValidGPA(gpa)) {
            return ValidationResult.invalid("Invalid GPA. Must be between 0.0 and 4.0.");
        }
        return ValidationResult.valid();
    }

    /**
     * Check if email already exists
     * @param email Email to check
     * @return true if email exists
     */
    private boolean isEmailExists(String email) {
        List<Student> allStudents = studentDAO.getAllStudents();
        return allStudents.stream().anyMatch(s -> s.getEmail().equalsIgnoreCase(email));
    }

    /**
     * Validation result class
     */
    public static class ValidationResult {
        private boolean valid;
        private String errorMessage;

        private ValidationResult(boolean valid, String errorMessage) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult valid() {
            return new ValidationResult(true, null);
        }

        public static ValidationResult invalid(String errorMessage) {
            return new ValidationResult(false, errorMessage);
        }

        public boolean isValid() { return valid; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * Service result class
     */
    public static class ServiceResult<T> {
        private boolean success;
        private T data;
        private String errorMessage;

        private ServiceResult(boolean success, T data, String errorMessage) {
            this.success = success;
            this.data = data;
            this.errorMessage = errorMessage;
        }

        public static <T> ServiceResult<T> success(T data) {
            return new ServiceResult<>(true, data, null);
        }

        public static <T> ServiceResult<T> failure(String errorMessage) {
            return new ServiceResult<>(false, null, errorMessage);
        }

        public boolean isSuccess() { return success; }
        public T getData() { return data; }
        public String getErrorMessage() { return errorMessage; }
    }
}