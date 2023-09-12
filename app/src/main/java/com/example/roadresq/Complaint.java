package com.example.roadresq;

public class Complaint {

    private String phoneNumber;  // Phone number of the user making the complaint
    private String date;         // Date when the complaint was submitted
    private String time;         // Time when the complaint was submitted
    private String category;     // Category of the complaint (e.g., accident, rules, road, other)
    private String subject;      // Subject of the complaint
    private String body;         // Body or description of the complaint
    private String status;       // Status of the complaint (e.g., "Under Review", "Resolved", etc.)

    public Complaint() {
        // Default constructor required for Firebase Realtime Database
    }

    public Complaint(String phoneNumber, String date, String time, String category, String subject, String body, String status) {
        this.phoneNumber = phoneNumber;
        this.date = date;
        this.time = time;
        this.category = category;
        this.subject = subject;
        this.body = body;
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
