package com.idc.wanharcarriage.classes;

public class Vehicle {

    private String registrationNumber;
    private String cityname ;
    private String model;

    public Vehicle() {
        this.registrationNumber = "";
        this.cityname = "";
        this.model = "";
    }

    public Vehicle(String registrationNumber, String cityname, String model) {
        this.registrationNumber = registrationNumber;
        this.cityname = cityname;
        this.model = model;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
