
package com.naman.sms;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

@RestController
public class WebController {

    @GetMapping("/api/students")
    public ResponseEntity<?> getStudents() {
        List<Map<String, Object>> list = new ArrayList<>();

        String sql = "SELECT roll_no, name, email, course, marks FROM students ORDER BY roll_no";

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> student = new LinkedHashMap<>();
                student.put("roll_no", rs.getInt("roll_no"));
                student.put("name", rs.getString("name"));
                student.put("email", rs.getString("email"));
                student.put("course", rs.getString("course"));
                student.put("marks", rs.getBigDecimal("marks"));
                list.add(student);
            }

            return ResponseEntity.ok(list);

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Database error: " + e.getMessage()));
        }
    }

    @PostMapping("/api/students")
    public ResponseEntity<Map<String, String>> addStudent(
            @RequestBody Map<String, String> data) {

        try {
            int rollNo = Integer.parseInt(data.get("roll_no"));
            String name = data.get("name");
            String email = data.get("email");
            String course = data.get("course");
            BigDecimal marks = new BigDecimal(data.get("marks"));

            if (rollNo <= 0 || name == null || name.isBlank()
                    || email == null || email.isBlank()
                    || course == null || course.isBlank()
                    || marks.compareTo(BigDecimal.ZERO) < 0
                    || marks.compareTo(new BigDecimal("100")) > 0) {

                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Please enter valid student details"));
            }

            String sql = "INSERT INTO students (roll_no, name, email, course, marks) VALUES (?, ?, ?, ?, ?)";

            try (Connection con = Database.getConnection();
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setInt(1, rollNo);
                ps.setString(2, name.trim());
                ps.setString(3, email.trim());
                ps.setString(4, course.trim());
                ps.setBigDecimal(5, marks);
                ps.executeUpdate();
            }

            return ResponseEntity.ok(
                    Map.of("message", "Student added successfully"));

        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid or missing student details"));

        } catch (SQLException e) {
            e.printStackTrace();

            String message = e.getErrorCode() == 1062
                    ? "Roll number or email already exists"
                    : "Database error: " + e.getMessage();

            return ResponseEntity.status(500)
                    .body(Map.of("message", message));
        }
    }

    @DeleteMapping("/api/students/{rollNo}")
    public ResponseEntity<Map<String, String>> deleteStudent(
            @PathVariable("rollNo") int rollNo) {

        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM students WHERE roll_no = ?")) {

            ps.setInt(1, rollNo);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                return ResponseEntity.status(404)
                        .body(Map.of("message", "Student not found"));
            }

            return ResponseEntity.ok(
                    Map.of("message", "Student deleted successfully"));

        } catch (SQLException e) {
            System.err.println("Student delete failed: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(500)
                    .body(Map.of("message",
                            "Database error: " + e.getMessage()));
        }
    }
}

