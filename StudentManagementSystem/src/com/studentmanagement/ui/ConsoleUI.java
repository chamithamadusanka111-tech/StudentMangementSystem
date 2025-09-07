package com.studentmanagement.ui;

import com.studentmanagement.model.Student;
import com.studentmanagement.service.StudentService;
import com.studentmanagement.util.InputValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Console-based User Interface for Student Management System
 */
public class ConsoleUI {
    private Scanner scanner;
    private StudentService studentService;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.studentService = new StudentService();
    }

    /**
     * Start the console application
     */
    public void start() {
        printWelcomeMessage();

        while (true) {
            printMainMenu();
            int choice = getMenuChoice();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> viewStudentById();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> searchStudentsByName();
                case 7 -> filterStudentsByMajor();
                case 8 -> filterStudentsByGPA();
                case 9 -> {
                    System.out.println("\nThank you for using Student Management System!");
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }

            // Pause before showing menu again
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Print welcome message
     */
    private void printWelcomeMessage() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              STUDENT MANAGEMENT SYSTEM                      ║");
        System.out.println("║                     Welcome!                                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Print main menu
     */
    private void printMainMenu() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    MAIN MENU");
        System.out.println("=".repeat(60));
        System.out.println("1. Add New Student");
        System.out.println("2. View All Students");
        System.out.println("3. View Student by ID");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Search Students by Name");
        System.out.println("7. Filter Students by Major");
        System.out.println("8. Filter Students by GPA");
        System.out.println("9. Exit");
        System.out.println("=".repeat(60));
    }

    /**
     * Get menu choice from user
     */
    private int getMenuChoice() {
        System.out.print("Enter your choice (1-9): ");
        String input = scanner.nextLine().trim();
        Integer choice = InputValidator.parseInteger(input);
        return (choice != null && choice >= 1 && choice <= 9) ? choice : 0;
    }

    /**
     * Add new student
     */
    private void addStudent() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("              ADD NEW STUDENT");
        System.out.println("=".repeat(50));

        try {
            System.out.print("Enter First Name: ");
            String firstName = scanner.nextLine().trim();

            System.out.print("Enter Last Name: ");
            String lastName = scanner.nextLine().trim();

            System.out.print("Enter Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Enter Phone: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
            String dobString = scanner.nextLine().trim();
            LocalDate dateOfBirth = InputValidator.parseDate(dobString);

            System.out.print("Enter Major: ");
            String major = scanner.nextLine().trim();

            System.out.print("Enter GPA (0.0-4.0): ");
            String gpaString = scanner.nextLine().trim();
            Double gpa = InputValidator.parseDouble(gpaString);

            if (dateOfBirth == null) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
                return;
            }

            if (gpa == null) {
                System.out.println("Invalid GPA format. Please enter a valid number.");
                return;
            }

            StudentService.ServiceResult<Integer> result = studentService.createStudent(
                    firstName, lastName, email, phone, dateOfBirth, major, gpa);

            if (result.isSuccess()) {
                System.out.println("Student added successfully! Student ID: " + result.getData());
            } else {
                System.out.println("Failed to add student: " + result.getErrorMessage());
            }

        } catch (Exception e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    /**
     * View all students
     */
    private void viewAllStudents() {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("                                        ALL STUDENTS");
        System.out.println("=".repeat(120));

        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found in the database.");
            return;
        }

        System.out.printf("%-5s %-15s %-15s %-30s %-15s %-12s %-20s %-6s%n",
                "ID", "First Name", "Last Name", "Email", "Phone", "DOB", "Major", "GPA");
        System.out.println("-".repeat(120));

        for (Student student : students) {
            System.out.printf("%-5d %-15s %-15s %-30s %-15s %-12s %-20s %.2f%n",
                    student.getStudentId(),
                    truncateString(student.getFirstName(), 15),
                    truncateString(student.getLastName(), 15),
                    truncateString(student.getEmail(), 30),
                    truncateString(student.getPhone(), 15),
                    student.getDateOfBirth(),
                    truncateString(student.getMajor(), 20),
                    student.getGpa());
        }

        System.out.println("-".repeat(120));
        System.out.println("Total students: " + students.size());
    }

    /**
     * View student by ID
     */
    private void viewStudentById() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            VIEW STUDENT BY ID");
        System.out.println("=".repeat(50));

        System.out.print("Enter Student ID: ");
        String input = scanner.nextLine().trim();
        Integer studentId = InputValidator.parseInteger(input);

        if (studentId == null) {
            System.out.println("Invalid Student ID format.");
            return;
        }

        Student student = studentService.getStudentById(studentId);

        if (student != null) {
            System.out.println("\n Student found:");
            printStudentDetails(student);
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }
    }

    /**
     * Update student information
     */
    private void updateStudent() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            UPDATE STUDENT");
        System.out.println("=".repeat(50));

        System.out.print("Enter Student ID to update: ");
        String input = scanner.nextLine().trim();
        Integer studentId = InputValidator.parseInteger(input);

        if (studentId == null) {
            System.out.println("Invalid Student ID format.");
            return;
        }

        Student existingStudent = studentService.getStudentById(studentId);
        if (existingStudent == null) {
            System.out.println("Student with ID " + studentId + " not found.");
            return;
        }

        System.out.println("\n Current student information:");
        printStudentDetails(existingStudent);

        System.out.println("\n Enter new information (press Enter to keep current value):");

        System.out.print("First Name [" + existingStudent.getFirstName() + "]: ");
        String firstName = getInputOrDefault(scanner.nextLine().trim(), existingStudent.getFirstName());

        System.out.print("Last Name [" + existingStudent.getLastName() + "]: ");
        String lastName = getInputOrDefault(scanner.nextLine().trim(), existingStudent.getLastName());

        System.out.print("Email [" + existingStudent.getEmail() + "]: ");
        String email = getInputOrDefault(scanner.nextLine().trim(), existingStudent.getEmail());

        System.out.print("Phone [" + existingStudent.getPhone() + "]: ");
        String phone = getInputOrDefault(scanner.nextLine().trim(), existingStudent.getPhone());

        System.out.print("Date of Birth [" + existingStudent.getDateOfBirth() + "] (yyyy-MM-dd): ");
        String dobInput = scanner.nextLine().trim();
        LocalDate dateOfBirth = dobInput.isEmpty() ? existingStudent.getDateOfBirth() : InputValidator.parseDate(dobInput);

        System.out.print("Major [" + existingStudent.getMajor() + "]: ");
        String major = getInputOrDefault(scanner.nextLine().trim(), existingStudent.getMajor());

        System.out.print("GPA [" + existingStudent.getGpa() + "]: ");
        String gpaInput = scanner.nextLine().trim();
        Double gpa = gpaInput.isEmpty() ? existingStudent.getGpa() : InputValidator.parseDouble(gpaInput);

        if (dateOfBirth == null) {
            System.out.println(" Invalid date format. Update cancelled.");
            return;
        }

        if (gpa == null) {
            System.out.println(" Invalid GPA format. Update cancelled.");
            return;
        }

        StudentService.ServiceResult<Boolean> result = studentService.updateStudent(
                studentId, firstName, lastName, email, phone, dateOfBirth, major, gpa);

        if (result.isSuccess()) {
            System.out.println(" Student updated successfully!");

            // Show updated student details
            Student updatedStudent = studentService.getStudentById(studentId);
            if (updatedStudent != null) {
                System.out.println("\n📋 Updated student information:");
                printStudentDetails(updatedStudent);
            }
        } else {
            System.out.println(" Failed to update student: " + result.getErrorMessage());
        }
    }

    /**
     * Delete student
     */
    private void deleteStudent() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("            DELETE STUDENT");
        System.out.println("=".repeat(50));

        System.out.print("Enter Student ID to delete: ");
        String input = scanner.nextLine().trim();
        Integer studentId = InputValidator.parseInteger(input);

        if (studentId == null) {
            System.out.println(" Invalid Student ID format.");
            return;
        }

        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println(" Student with ID " + studentId + " not found.");
            return;
        }

        System.out.println("\n Student to be deleted:");
        printStudentDetails(student);

        System.out.print("\n Are you sure you want to delete this student? (yes/no): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("yes") || confirmation.equals("y")) {
            StudentService.ServiceResult<Boolean> result = studentService.deleteStudent(studentId);

            if (result.isSuccess()) {
                System.out.println(" Student deleted successfully!");
            } else {
                System.out.println(" Failed to delete student: " + result.getErrorMessage());
            }
        } else {
            System.out.println(" Delete operation cancelled.");
        }
    }

    /**
     * Search students by name
     */
    private void searchStudentsByName() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          SEARCH STUDENTS BY NAME");
        System.out.println("=".repeat(50));

        System.out.print("Enter name to search: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("❌ Please enter a name to search.");
            return;
        }

        List<Student> students = studentService.searchStudentsByName(name);

        if (students.isEmpty()) {
            System.out.println("❌ No students found with name containing: \"" + name + "\"");
            return;
        }

        System.out.println("\n✅ Found " + students.size() + " student(s) with name containing \"" + name + "\":");
        printStudentList(students);
    }

    /**
     * Filter students by major
     */
    private void filterStudentsByMajor() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("         FILTER STUDENTS BY MAJOR");
        System.out.println("=".repeat(50));

        System.out.print("Enter major to filter: ");
        String major = scanner.nextLine().trim();

        if (major.isEmpty()) {
            System.out.println("❌ Please enter a major to filter.");
            return;
        }

        List<Student> students = studentService.getStudentsByMajor(major);

        if (students.isEmpty()) {
            System.out.println("❌ No students found with major containing: \"" + major + "\"");
            return;
        }

        System.out.println("\n✅ Found " + students.size() + " student(s) with major containing \"" + major + "\":");
        printStudentList(students);
    }

    /**
     * Filter students by GPA
     */
    private void filterStudentsByGPA() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          FILTER STUDENTS BY GPA");
        System.out.println("=".repeat(50));

        System.out.print("Enter minimum GPA (0.0-4.0): ");
        String input = scanner.nextLine().trim();
        Double minGpa = InputValidator.parseDouble(input);

        if (minGpa == null || !InputValidator.isValidGPA(minGpa)) {
            System.out.println("❌ Invalid GPA. Please enter a value between 0.0 and 4.0.");
            return;
        }

        List<Student> students = studentService.getStudentsWithHighGPA(minGpa);

        if (students.isEmpty()) {
            System.out.println("❌ No students found with GPA >= " + minGpa);
            return;
        }

        System.out.println("\n✅ Found " + students.size() + " student(s) with GPA >= " + minGpa + ":");
        printStudentList(students);
    }

    /**
     * Print detailed student information
     */
    private void printStudentDetails(Student student) {
        System.out.println("┌─────────────────────────────────────────────────────────────┐");
        System.out.printf("│ Student ID    : %-43d │%n", student.getStudentId());
        System.out.printf("│ Name          : %-43s │%n", student.getFirstName() + " " + student.getLastName());
        System.out.printf("│ Email         : %-43s │%n", student.getEmail());
        System.out.printf("│ Phone         : %-43s │%n", student.getPhone());
        System.out.printf("│ Date of Birth : %-43s │%n", student.getDateOfBirth());
        System.out.printf("│ Major         : %-43s │%n", student.getMajor());
        System.out.printf("│ GPA           : %-43.2f │%n", student.getGpa());
        System.out.println("└─────────────────────────────────────────────────────────────┘");
    }

    /**
     * Print list of students in table format
     */
    private void printStudentList(List<Student> students) {
        System.out.println("-".repeat(120));
        System.out.printf("%-5s %-15s %-15s %-30s %-15s %-12s %-20s %-6s%n",
                "ID", "First Name", "Last Name", "Email", "Phone", "DOB", "Major", "GPA");
        System.out.println("-".repeat(120));

        for (Student student : students) {
            System.out.printf("%-5d %-15s %-15s %-30s %-15s %-12s %-20s %.2f%n",
                    student.getStudentId(),
                    truncateString(student.getFirstName(), 15),
                    truncateString(student.getLastName(), 15),
                    truncateString(student.getEmail(), 30),
                    truncateString(student.getPhone(), 15),
                    student.getDateOfBirth(),
                    truncateString(student.getMajor(), 20),
                    student.getGpa());
        }
        System.out.println("-".repeat(120));
    }

    /**
     * Get input or return default value
     */
    private String getInputOrDefault(String input, String defaultValue) {
        return input.isEmpty() ? defaultValue : input;
    }

    /**
     * Truncate string to fit column width
     */
    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }
}