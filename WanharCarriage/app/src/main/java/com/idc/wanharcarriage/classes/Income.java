package com.idc.wanharcarriage.classes;

public class Income {

    private String incomeDate;
    private String incomeAbout;
    private String company;
    private String orderid;
    private int amount;

    public Income() {
        this.incomeDate = "";
        this.incomeAbout = "";
        this.company = "";
        this.orderid = "";
        this.amount = 0 ;
    }

    public Income(String incomeDate, String incomeAbout, String company, String orderid) {
        this.incomeDate = incomeDate;
        this.incomeAbout = incomeAbout;
        this.company = company;
        this.orderid = orderid;
    }

    public String getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(String incomeDate) {
        this.incomeDate = incomeDate;
    }

    public String getIncomeAbout() {
        return incomeAbout;
    }

    public void setIncomeAbout(String incomeAbout) {
        this.incomeAbout = incomeAbout;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
