package com.sumaira.superiorcms;

public class TeacherLeave {

    String teacherid ;
    String leavedate ;
    String leavereason;
    String leavedays ;

    public TeacherLeave() {
        this.teacherid = "";
        this.leavedate = "";
        this.leavereason = "";
        this.leavedays = "";
    }

    public TeacherLeave(String teacherid, String leavedate, String leavereason, String leavedays) {
        this.teacherid = teacherid;
        this.leavedate = leavedate;
        this.leavereason = leavereason;
        this.leavedays = leavedays;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public String getLeavedate() {
        return leavedate;
    }

    public void setLeavedate(String leavedate) {
        this.leavedate = leavedate;
    }

    public String getLeavereason() {
        return leavereason;
    }

    public void setLeavereason(String leavereason) {
        this.leavereason = leavereason;
    }

    public String getLeavedays() {
        return leavedays;
    }

    public void setLeavedays(String leavedays) {
        this.leavedays = leavedays;
    }
}
