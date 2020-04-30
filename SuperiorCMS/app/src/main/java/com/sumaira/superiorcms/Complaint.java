package com.sumaira.superiorcms;

public class Complaint {

    private String studentid ;
    private String complaintDate ;
    private String complaintAgainst;
    private String complaintDetails;

    public Complaint() {
        this.studentid = "";
        this.complaintDate = "";
        this.complaintAgainst = "";
        this.complaintDetails = "";
    }

    public Complaint(String studentid, String complaintDate, String complaintAgainst, String complaintDetails) {
        this.studentid = studentid;
        this.complaintDate = complaintDate;
        this.complaintAgainst = complaintAgainst;
        this.complaintDetails = complaintDetails;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getComplaintAgainst() {
        return complaintAgainst;
    }

    public void setComplaintAgainst(String complaintAgainst) {
        this.complaintAgainst = complaintAgainst;
    }

    public String getComplaintDetails() {
        return complaintDetails;
    }

    public void setComplaintDetails(String complaintDetails) {
        this.complaintDetails = complaintDetails;
    }
}
