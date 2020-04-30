package com.idc.wanharcarriage.classes;

public class DealerOrder {
    private String orderid ;
    private String customerid ;
    private String totalweight;
    private String destination;
    private String freightperTon;
    private String company;

    public DealerOrder() {
        this.orderid = "";
        this.customerid = "";
        this.totalweight = "";
        this.destination = "" ;
        this.freightperTon = "";
        this.company = "";
    }

    public DealerOrder(String customerid, String totalweight, String destination, String freightperTon,String company) {
        this.orderid = "";
        this.customerid = customerid;
        this.totalweight = totalweight;
        this.destination = destination;
        this.freightperTon = freightperTon;
        this.company =  company;
    }
    public DealerOrder(String orderid, String customerid, String totalweight, String destination, String freightperTon,String company) {
        this.orderid = orderid;
        this.customerid = customerid;
        this.totalweight = totalweight;
        this.destination = destination;
        this.freightperTon = freightperTon;
        this.company =  company;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getTotalweight() {
        return totalweight;
    }

    public void setTotalweight(String totalweight) {
        this.totalweight = totalweight;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getFreightperTon() {
        return freightperTon;
    }

    public void setFreightperTon(String freightperTon) {
        this.freightperTon = freightperTon;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
