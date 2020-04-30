package com.idc.wanharcarriage.classes;

public class MappleOrder {

    private String orderDate;
    private String builtyNumber;
    private String vehicle;
    private String driver;
    private String weight;
    private String finalDestination;
    private String vehicleStatus;
    private String commission;

    public MappleOrder() {
        this.orderDate = "";
        this.builtyNumber = "";
        this.vehicle = "";
        this.driver = "";
        this.weight = "";
        this.finalDestination = "";
        this.vehicleStatus = "";
        this.commission = "" ;
    }

    public MappleOrder(String orderDate, String builtyNumber,
                       String vehicle, String driver, String weight,
                       String finalDestination, String vehicleStatus) {
        this.orderDate = orderDate;
        this.builtyNumber = builtyNumber;
        this.vehicle = vehicle;
        this.driver = driver;
        this.weight = weight;
        this.finalDestination = finalDestination;
        this.vehicleStatus = vehicleStatus;
    }

    public MappleOrder(MappleOrder m) {
        this.orderDate = m.orderDate;
        this.builtyNumber = m.builtyNumber;
        this.vehicle = m.vehicle;
        this.driver = m.driver;
        this.weight = m.weight;
        this.finalDestination = m.finalDestination;
        this.vehicleStatus = m.vehicleStatus;
        this.commission = m.commission;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        this.vehicleStatus = vehicleStatus;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }
}
