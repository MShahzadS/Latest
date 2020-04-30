package com.idc.wanharcarriage.classes;

public class PioneerOrder {
    private String orderDate;
    private String builtyNumber;
    private String invoiceNumber;
    private String vehicle;
    private String driver;
    private int weight;
    private String  finalDestination;
    private String  orderType;
    private int  commission;
    private int  paidAmount;
    private Boolean billStatus;

    public PioneerOrder(String orderDate, String builtyNumber, String invoiceNumber, String vehicle,
                        String driver, int weight, String finalDestination, String orderType,
                        int commission, int paidAmount, Boolean billStatus) {
        this.orderDate = orderDate;
        this.builtyNumber = builtyNumber;
        this.invoiceNumber = invoiceNumber;
        this.vehicle = vehicle;
        this.driver = driver;
        this.weight = weight;
        this.finalDestination = finalDestination;
        this.orderType = orderType;
        this.commission = commission;
        this.paidAmount = paidAmount;
        this.billStatus = billStatus;
    }

    public PioneerOrder(PioneerOrder P) {
        this.orderDate = P.orderDate;
        this.builtyNumber = P.builtyNumber;
        this.invoiceNumber = P.invoiceNumber;
        this.vehicle = P.vehicle;
        this.driver = P.driver;
        this.weight = P.weight;
        this.finalDestination = P.finalDestination;
        this.orderType = P.orderType;
        this.commission = P.commission;
        this.paidAmount = P.paidAmount;
        this.billStatus = P.billStatus;
    }

    public PioneerOrder() {
        this.orderDate = "";
        this.builtyNumber = "";
        this.invoiceNumber = "";
        this.vehicle = "";
        this.driver = "";
        this.weight = 0;
        this.finalDestination = "";
        this.orderType = "";
        this.commission = 0;
        this.paidAmount = 0;
        this.billStatus = false;
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

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
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

    public String getorderType() {
        return orderType;
    }

    public void setorderType(String orderType) {
        this.orderType = orderType;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(int paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Boolean getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Boolean billStatus) {
        this.billStatus = billStatus;
    }
}
