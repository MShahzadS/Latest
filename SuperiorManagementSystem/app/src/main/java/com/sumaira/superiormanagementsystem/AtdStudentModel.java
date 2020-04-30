package com.sumaira.superiormanagementsystem;

public class AtdStudentModel {

    String studentID ;
    String status ;

    public AtdStudentModel() {
        this.studentID = "";
        this.status = "";
    }

    public AtdStudentModel(String studentID, String status) {
        this.studentID = studentID;
        this.status = status;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
