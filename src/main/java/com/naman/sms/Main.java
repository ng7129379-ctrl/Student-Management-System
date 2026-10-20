package com.naman.sms;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SC = new Scanner(System.in);
    private static final StudentDao DAO = new StudentDao();

    public static void main(String[] args) {
        System.out.println("================================");
        System.out.println("   STUDENT MANAGEMENT SYSTEM");
        System.out.println("================================");

        try {
            // Quick connection check so setup errors are shown clearly.
            try (var ignored = Database.getConnection()) {
                System.out.println("Database connection successful.");
            }
        } catch (SQLException e) {
            System.out.println("Could not connect to MySQL.");
            System.out.println("Check that MySQL is running, schema.sql was executed, and DB settings are correct.");
            System.out.println("Details: " + e.getMessage());
            return;
        }

        while (true) {
            printMenu();
            int choice = readInt("Choose an option: ");
            try {
                switch (choice) {
                    case 1 -> addStudent();
                    case 2 -> viewAllStudents();
                    case 3 -> searchStudent();
                    case 4 -> updateStudent();
                    case 5 -> deleteStudent();
                    case 6 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Please choose a number from 1 to 6.");
                }
            } catch (SQLException e) {
                System.out.println("Database operation failed: " + e.getMessage());
                if (e.getErrorCode() == 1062) {
                    System.out.println("That roll number or email already exists.");
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Add student");
        System.out.println("2. View all students");
        System.out.println("3. Search by roll number");
        System.out.println("4. Update student");
        System.out.println("5. Delete student");
        System.out.println("6. Exit");
    }

    private static void addStudent() throws SQLException {
        int roll = readPositiveInt("Roll number: ");
        String name = readRequiredText("Name: ");
        String email = readEmail("Email: ");
        String course = readRequiredText("Course: ");
        BigDecimal marks = readMarks("Marks (0-100): ");

        boolean added = DAO.add(new Student(roll, name, email, course, marks));
        System.out.println(added ? "Student added successfully." : "Student was not added.");
    }

    private static void viewAllStudents() throws SQLException {
        List<Student> students = DAO.findAll();
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        System.out.println("\n--- STUDENT RECORDS ---");
        students.forEach(System.out::println);
        System.out.println("Total students: " + students.size());
    }

    private static void searchStudent() throws SQLException {
        int roll = readPositiveInt("Enter roll number to search: ");
        Student student = DAO.findByRollNo(roll);
        System.out.println(student == null ? "Student not found." : student);
    }

    private static void updateStudent() throws SQLException {
        int roll = readPositiveInt("Roll number to update: ");
        Student existing = DAO.findByRollNo(roll);
        if (existing == null) {
            System.out.println("Student not found.");
            return;
        }

        System.out.println("Enter the updated details:");
        String name = readRequiredText("Name: ");
        String email = readEmail("Email: ");
        String course = readRequiredText("Course: ");
        BigDecimal marks = readMarks("Marks (0-100): ");

        boolean updated = DAO.update(new Student(roll, name, email, course, marks));
        System.out.println(updated ? "Student updated successfully." : "No changes made.");
    }

    private static void deleteStudent() throws SQLException {
        int roll = readPositiveInt("Roll number to delete: ");
        String confirm = readRequiredText("Type YES to confirm deletion: ");
        if (!confirm.equals("YES")) {
            System.out.println("Deletion cancelled.");
            return;
        }
        System.out.println(DAO.delete(roll) ? "Student deleted." : "Student not found.");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SC.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            int value = readInt(prompt);
            if (value > 0) return value;
            System.out.println("Value must be greater than zero.");
        }
    }

    private static String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = SC.nextLine().trim();
            if (!value.isEmpty()) return value;
            System.out.println("This field cannot be empty.");
        }
    }

    private static String readEmail(String prompt) {
        while (true) {
            String email = readRequiredText(prompt);
            if (email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) return email;
            System.out.println("Enter a valid email address.");
        }
    }

    private static BigDecimal readMarks(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                BigDecimal marks = new BigDecimal(SC.nextLine().trim());
                if (marks.compareTo(BigDecimal.ZERO) >= 0 &&
                    marks.compareTo(new BigDecimal("100")) <= 0) {
                    return marks;
                }
            } catch (NumberFormatException ignored) {
                // Show the same validation message below.
            }
            System.out.println("Marks must be a number from 0 to 100.");
        }
    }
}
