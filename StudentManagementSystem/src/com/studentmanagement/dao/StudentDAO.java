package com.studentmanagement.dao;

import com.studentmanagement.model.Student;
import com.studentmanagement.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {

    /**
     * Create a new student record
     * @param student Student object to insert
     * @return true if insertion is successful
     */
    public boolean createStudent(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, email, phone, date_of_birth, major, gpa) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setDate(5, Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(6, student.getMajor());
            pstmt.setDouble(7, student.getGpa());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    student.setStudentId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating student: " + e.getMessage());
        }
        return false;
    }

    /**
     * Retrieve all students
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY student_id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving students: " + e.getMessage());
        }
        return students;
    }

    /**
     * Retrieve student by ID
     * @param studentId Student ID to search for
     * @return Student object if found, null otherwise
     */
    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Search students by name (first or last name)
     * @param name Name to search for
     * @return List of matching students
     */
    public List<Student> searchStudentsByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE first_name LIKE ? OR last_name LIKE ? ORDER BY first_name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + name + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error searching students by name: " + e.getMessage());
        }
        return students;
    }

    /**
     * Update student information
     * @param student Student object with updated information
     * @return true if update is successful
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, phone = ?, " +
                "date_of_birth = ?, major = ?, gpa = ? WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getPhone());
            pstmt.setDate(5, Date.valueOf(student.getDateOfBirth()));
            pstmt.setString(6, student.getMajor());
            pstmt.setDouble(7, student.getGpa());
            pstmt.setInt(8, student.getStudentId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating student: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete student by ID
     * @param studentId Student ID to delete
     * @return true if deletion is successful
     */
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting student: " + e.getMessage());
        }
        return false;
    }

    /**
     * Get students by major
     * @param major Major to filter by
     * @return List of students with the specified major
     */
    public List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE major LIKE ? ORDER BY gpa DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + major + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving students by major: " + e.getMessage());
        }
        return students;
    }

    /**
     * Get students with GPA above threshold
     * @param minGpa Minimum GPA threshold
     * @return List of students with GPA above threshold
     */
    public List<Student> getStudentsWithHighGPA(double minGpa) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE gpa >= ? ORDER BY gpa DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, minGpa);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving students with high GPA: " + e.getMessage());
        }
        return students;
    }

    /**
     * Extract Student object from ResultSet
     * @param rs ResultSet containing student data
     * @return Student object
     * @throws SQLException if data extraction fails
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("student_id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("phone"),
                rs.getDate("date_of_birth").toLocalDate(),
                rs.getString("major"),
                rs.getDouble("gpa")
        );
    }
}