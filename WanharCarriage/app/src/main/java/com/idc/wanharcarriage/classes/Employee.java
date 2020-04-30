package com.idc.wanharcarriage.classes;

public class Employee {

    private String name ;
    private String cnic;
    private String password ;
    private String contact;
    private String address;
    private String usertype;

    public Employee() {
        this.name = "";
        this.cnic = "";
        this.password = "";
        this.contact = "";
        this.address = "";
        this.usertype = "";
    }

    public Employee(String name, String cnic, String password, String contact, String address, String usertype) {
        this.name = name;
        this.cnic = cnic;
        this.password = password;
        this.contact = contact;
        this.address = address;
        this.usertype = usertype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
