package com.sumaira.superiormanagementsystem;

public class Leave {
    String teacher_id;
    String reason;
    String status;
    String date;

    public Leave(String reason, String date, String status, String teacher_id) {
        this.teacher_id = teacher_id;
        this.reason = reason;
        this.status = status;
        this.date = date;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
