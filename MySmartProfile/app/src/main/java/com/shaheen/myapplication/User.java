package com.shaheen.myapplication;

public class User {

    private String userid;
    private String name ;
    private String email;
    private String password ;

    public User() {
        this.userid = "" ;
        this.name = "";
        this.email = "";
        this.password = "";
    }

    public User(String userid, String name, String email, String password) {
        this.userid = userid;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
