package com.sumaira.superiormanagementsystem;

public class StudentS extends User {
    String registrationnumber;
    String classname;
    String section;



    public StudentS(String registrationnumber, String classname, String section){
        super(registrationnumber, classname, section,0);
        this.registrationnumber=registrationnumber;
        this.classname=classname;
        this.section=section;
    }

    public String getRegistrationnumber() {
        return registrationnumber;
    }

    public void setRegistrationnumber(String registrationnumber) {
        this.registrationnumber = registrationnumber;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }
}
