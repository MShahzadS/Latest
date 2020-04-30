package com.idc.wanharcarriage.classes;

public class FlyingOrder {

    private String orderDate;
    private String builtyNumber;
    private String vehicle;
    private String driver;
    private int weight;
    private String  finalDestination;
    private int  commission;

    public FlyingOrder() {
        this.orderDate = "";
        this.builtyNumber = "";
        this.vehicle = "";
        this.driver = "";
        this.weight = 0;
        this.finalDestination = "";
        this.commission = 0;
    }

    public FlyingOrder(String orderDate, String builtyNumber, String vehicle, String driver,
                       int weight, String finalDestination, int commission) {
        this.orderDate = orderDate;
        this.builtyNumber = builtyNumber;
        this.vehicle = vehicle;
        this.driver = driver;
        this.weight = weight;
        this.finalDestination = finalDestination;
        this.commission = commission;
    }

    public FlyingOrder(FlyingOrder F) {
        this.orderDate = F.orderDate;
        this.builtyNumber = F.builtyNumber;
        this.vehicle = F.vehicle;
        this.driver = F.driver;
        this.weight = F.weight;
        this.finalDestination = F.finalDestination;
        this.commission = F.commission;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getBuiltyNumber() {
        return builtyNumber;
    }

    public void setBuiltyNumber(String builtyNumber) {
        this.builtyNumber = builtyNumber;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }
}
