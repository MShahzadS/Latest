package com.sumaira.superiorcms;

public class Exam {

    private String examid ;
    private  String classid ;
    private String examStart;
    private  String examEnd ;

    public Exam() {
        this.examid = "";
        this.classid = "";
        this.examStart = "";
        this.examEnd = "";
    }

    public Exam(String examid, String classid, String examStart, String examEnd) {
        this.examid = examid;
        this.classid = classid;
        this.examStart = examStart;
        this.examEnd = examEnd;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getExamStart() {
        return examStart;
    }

    public void setExamStart(String examStart) {
        this.examStart = examStart;
    }

    public String getExamEnd() {
        return examEnd;
    }

    public void setExamEnd(String examEnd) {
        this.examEnd = examEnd;
    }
}
