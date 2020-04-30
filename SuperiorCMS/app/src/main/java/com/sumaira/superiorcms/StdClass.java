package com.sumaira.superiorcms;

public class StdClass {

    private String classid ;
    private  String classname;
    private String classsection ;

    public StdClass() {
        this.classid = "";
        this.classname = "";
        this.classsection = "";
    }

    public StdClass(String classid, String classname, String classsection) {
        this.classid = classid;
        this.classname = classname;
        this.classsection = classsection;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasssection() {
        return classsection;
    }

    public void setClasssection(String classsection) {
        this.classsection = classsection;
    }
}
