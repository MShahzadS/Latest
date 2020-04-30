package com.idc.wanharcarriage.classes;

public class Customer {

    private String name ;
    private String contact;
    private String address;
    private String company;

    public Customer() {
        this.name = "";
        this.contact = "";
        this.address = "";
        this.company = "";
    }
    public Customer(String name, String contact, String address, String company) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
