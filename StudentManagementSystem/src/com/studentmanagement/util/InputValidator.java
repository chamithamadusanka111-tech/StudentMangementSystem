package com.studentmanagement.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Input validation utility class
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^[+]?[0-9\\s\\-()]{10,15}$");

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Validate email format
     * @param email Email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validate phone number format
     * @param phone Phone number to validate
     * @return true if phone number is valid
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validate and parse date string
     * @param dateString Date string in yyyy-MM-dd format
     * @return LocalDate if valid, null otherwise
     */
    public static LocalDate parseDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, DATE_FORMATTER);
            // Check if date is not in the future and reasonable for birth date
            if (date.isAfter(LocalDate.now()) || date.isBefore(LocalDate.of(1900, 1, 1))) {
                return null;
            }
            return date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Validate GPA range
     * @param gpa GPA to validate
     * @return true if GPA is within valid range (0.0 - 4.0)
     */
    public static boolean isValidGPA(double gpa) {
        return gpa >= 0.0 && gpa <= 4.0;
    }

    /**
     * Validate name (not empty, contains only letters and spaces)
     * @param name Name to validate
     * @return true if name is valid
     */
    public static boolean isValidName(String name) {
        return name != null &&
                !name.trim().isEmpty() &&
                name.trim().matches("^[a-zA-Z\\s]+$") &&
                name.trim().length() >= 2;
    }

    /**
     * Validate major (not empty, reasonable length)
     * @param major Major to validate
     * @return true if major is valid
     */
    public static boolean isValidMajor(String major) {
        return major != null &&
                !major.trim().isEmpty() &&
                major.trim().length() >= 2 &&
                major.trim().length() <= 50;
    }

    /**
     * Parse double from string with validation
     * @param value String value to parse
     * @return Double value if valid, null otherwise
     */
    public static Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parse integer from string with validation
     * @param value String value to parse
     * @return Integer value if valid, null otherwise
     */
    public static Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}