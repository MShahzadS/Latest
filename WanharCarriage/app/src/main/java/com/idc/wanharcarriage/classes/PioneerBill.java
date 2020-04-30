package com.idc.wanharcarriage.classes;

import java.util.ArrayList;

public class PioneerBill {

    private String billDate;
    ArrayList<String> orderInvoices;
    private int totalBill;
    Boolean billStatus;

    public PioneerBill() {
        this.billDate = "";
        this.orderInvoices = new ArrayList<>();
        this.totalBill = 0;
        this.billStatus = false;
    }

    public PioneerBill(String billDate, ArrayList<String> orderInvoices, int totalBill) {
        this.billDate = billDate;
        this.orderInvoices = orderInvoices;
        this.totalBill = totalBill;
        this.billStatus = false;
    }

    public PioneerBill(PioneerBill B) {
        this.billDate = B.billDate;
        this.orderInvoices = B.orderInvoices;
        this.totalBill = B.totalBill;
        this.billStatus = B.billStatus;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public ArrayList<String> getOrderInvoices() {
        return orderInvoices;
    }

    public void setOrderInvoices(ArrayList<String> orderInvoices) {
        this.orderInvoices = orderInvoices;
    }

    public int getTotalBill() {
        return totalBill;
    }

    public void setTotalBill(int totalBill) {
        this.totalBill = totalBill;
    }

    public Boolean getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Boolean billStatus) {
        this.billStatus = billStatus;
    }
}
