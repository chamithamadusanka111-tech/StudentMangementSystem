package com.studentmanagement;

import com.studentmanagement.ui.ConsoleUI;
import com.studentmanagement.util.DatabaseConnection;

/**
 * Main application class for Student Management System
 */
public class StudentManagementApp {

    public static void main(String[] args) {
        System.out.println("Starting Student Management System...");

        // Test database connection
        if (!DatabaseConnection.testConnection()) {
            System.err.println("❌ Failed to connect to database. Please check your database setup.");
            System.err.println("Make sure XAMPP is running and the database 'student_management' exists.");
            return;
        }

        try {
            // Start the console UI
            ConsoleUI consoleUI = new ConsoleUI();
            consoleUI.start();
        } catch (Exception e) {
            System.err.println("❌ Application error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close database connection
            DatabaseConnection.closeConnection();
            System.out.println("Application terminated.");
        }
    }
}