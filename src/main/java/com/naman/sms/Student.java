package com.naman.sms;

import java.math.BigDecimal;

public class Student {
    private final int rollNo;
    private final String name;
    private final String email;
    private final String course;
    private final BigDecimal marks;

    public Student(int rollNo, String name, String email, String course, BigDecimal marks) {
        this.rollNo = rollNo;
        this.name = name;
        this.email = email;
        this.course = course;
        this.marks = marks;
    }

    public int getRollNo() { return rollNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public BigDecimal getMarks() { return marks; }

    @Override
    public String toString() {
        return String.format("Roll: %d | Name: %s | Email: %s | Course: %s | Marks: %s",
                rollNo, name, email, course, marks.toPlainString());
    }
}
