package com.idc.wanharcarriage.classes;

public class Driver {

    private String name ;
    private String contact;
    private String address;

    public Driver() {
        this.name = "";
        this.contact = "";
        this.address = "";
    }

    public Driver(String name, String contact, String address) {
        this.name = name;
        this.contact = contact;
        this.address = address;
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
}
