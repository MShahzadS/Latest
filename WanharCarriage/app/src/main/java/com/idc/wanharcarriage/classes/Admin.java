package com.idc.wanharcarriage.classes;

public class Admin {

    private String name;
    private String cnic;
    private String password;
    private String contact;
    private Boolean status;

    public Admin() {
        this.name = "";
        this.cnic = "";
        this.password = "";
        this.contact = "";
        this.status = false;
    }

    public Admin(String name, String cnic, String password, String contact, Boolean status) {
        this.name = name;
        this.cnic = cnic;
        this.password = password;
        this.contact = contact;
        this.status = status;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
