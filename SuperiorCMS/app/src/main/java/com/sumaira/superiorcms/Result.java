package com.sumaira.superiorcms;

public class Result {

    String teacherid ;
    String studentid ;
    String subjectid ;
    String examid ;
    String obtainedMarks;
    String totalMarks;

    public Result() {
        this.teacherid = "";
        this.studentid = "";
        this.subjectid = "";
        this.examid = "";
        this.obtainedMarks = "";
        this.totalMarks = "";
    }

    public Result(String teacherid, String studentid, String subjectid, String examid, String obtainedMarks, String totalMarks) {
        this.teacherid = teacherid;
        this.studentid = studentid;
        this.subjectid = subjectid;
        this.examid = examid;
        this.obtainedMarks = obtainedMarks;
        this.totalMarks = totalMarks;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getSubjectid() {
        return subjectid;
    }

    public void setSubjectid(String subjectid) {
        this.subjectid = subjectid;
    }

    public String getExamid() {
        return examid;
    }

    public void setExamid(String examid) {
        this.examid = examid;
    }

    public String getObtainedMarks() {
        return obtainedMarks;
    }

    public void setObtainedMarks(String obtainedMarks) {
        this.obtainedMarks = obtainedMarks;
    }

    public String getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(String totalMarks) {
        this.totalMarks = totalMarks;
    }
}
