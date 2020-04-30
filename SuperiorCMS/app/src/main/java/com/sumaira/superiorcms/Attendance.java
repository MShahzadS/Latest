package com.sumaira.superiorcms;

public class Attendance {

    private String aDate;
    private  String classid;
    private String stdid ;
    private String status ;

    public Attendance() {
        this.aDate = "";
        this.classid = "";
        this.stdid = "";
        this.status = "";
    }

    public Attendance(String aDate, String classid, String stdid, String status) {
        this.aDate = aDate;
        this.classid = classid;
        this.stdid = stdid;
        this.status = status;
    }

    public String getaDate() {
        return aDate;
    }

    public void setaDate(String aDate) {
        this.aDate = aDate;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getStdid() {
        return stdid;
    }

    public void setStdid(String stdid) {
        this.stdid = stdid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
