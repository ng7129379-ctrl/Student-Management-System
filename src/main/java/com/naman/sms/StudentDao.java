package com.naman.sms;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDao {
    public boolean add(Student student) throws SQLException {
        String sql = "INSERT INTO students (roll_no, name, email, course, marks) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, student.getRollNo());
            ps.setString(2, student.getName());
            ps.setString(3, student.getEmail());
            ps.setString(4, student.getCourse());
            ps.setBigDecimal(5, student.getMarks());
            return ps.executeUpdate() == 1;
        }
    }

    public List<Student> findAll() throws SQLException {
        String sql = "SELECT roll_no, name, email, course, marks FROM students ORDER BY roll_no";
        List<Student> students = new ArrayList<>();
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                students.add(mapRow(rs));
            }
        }
        return students;
    }

    public Student findByRollNo(int rollNo) throws SQLException {
        String sql = "SELECT roll_no, name, email, course, marks FROM students WHERE roll_no = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rollNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean update(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, email = ?, course = ?, marks = ? WHERE roll_no = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getEmail());
            ps.setString(3, student.getCourse());
            ps.setBigDecimal(4, student.getMarks());
            ps.setInt(5, student.getRollNo());
            return ps.executeUpdate() == 1;
        }
    }

    public boolean delete(int rollNo) throws SQLException {
        String sql = "DELETE FROM students WHERE roll_no = ?";
        try (Connection con = Database.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, rollNo);
            return ps.executeUpdate() == 1;
        }
    }

    private Student mapRow(ResultSet rs) throws SQLException {
        return new Student(
                rs.getInt("roll_no"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("course"),
                rs.getBigDecimal("marks")
        );
    }
}
