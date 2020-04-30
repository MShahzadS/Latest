package com.sumaira.superiorcms;

import androidx.annotation.NonNull;

public class Student {

    private String regNum ;
    private String name ;
    private String classid;
    private String password;


    public Student() {
        this.regNum = "";
        this.name = "";
        this.password = "" ;
        this.classid = "";
    }

    public Student(String regNum, String name, String password,  String classid) {
        this.regNum = regNum;
        this.name = name;
        this.password = password;
        this.classid = classid;
    }

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
