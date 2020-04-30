package com.sumaira.superiorcollege;

class User {
    private String name ;
    private String email;
    private String password;
    private String cnic;
    private String DOB;
    private String address;
    private int userType ;

    public User(){
        name = "" ;
        email = "" ;
        password = "" ;
        cnic = "" ;
        DOB = "" ;
        address = "" ;
        userType = 0 ;
        userType = 1 ;
    }

    public User(String name, String email, String password, int userType) {

        this.name = name ;
        this.email = email ;
        this.password = password ;
        this.userType = userType ;
        cnic = "" ;
        DOB = "" ;
        address = "" ;
    }

    public User(String name, String email, String password, String cnic, String DOB, String address, int userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.cnic = cnic;
        this.DOB = DOB;
        this.address = address;
        this.userType = userType ;
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

    public int getType() {
        return userType;
    }

    public void setType(int userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
