package com.sumaira.superiormanagementsystem;

class AttendanceS {

    String student_id;
    String class_id;
    String status;
    public AttendanceS(String status, String class_id, String student_id) {

        this.student_id = student_id;
        this.class_id = class_id;
        this.status = status;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}





