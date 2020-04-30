package com.idc.wanharcarriage.classes;

public class Payment {

    private String paymentDate;
    private String paidBy;
    private String paidTo;
    private String paymentReason;
    private int amount;
    private Boolean paymentStatus;

    public Payment() {
        this.paymentDate = "";
        this.paidBy = "";
        this.paidTo = "";
        this.paymentReason = "";
        this.amount = 0;
        this.paymentStatus = false;
    }

    public Payment(String paymentDate, String paidBy, String paidTo, String paymentReason, int amount) {
        this.paymentDate = paymentDate;
        this.paidBy = paidBy;
        this.paidTo = paidTo;
        this.paymentReason = paymentReason;
        this.amount = amount;
        this.paymentStatus = false;
    }

    public Payment(Payment P) {
        this.paymentDate = P.paymentDate;
        this.paidBy = P.paidBy;
        this.paidTo = P.paidTo;
        this.paymentReason = P.paymentReason;
        this.amount = P.amount;
        this.paymentStatus = P.paymentStatus;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaidBy() {
        return paidBy;
    }

    public void setPaidBy(String paidBy) {
        this.paidBy = paidBy;
    }

    public String getPaidTo() {
        return paidTo;
    }

    public void setPaidTo(String paidTo) {
        this.paidTo = paidTo;
    }

    public String getPaymentReason() {
        return paymentReason;
    }

    public void setPaymentReason(String paymentReason) {
        this.paymentReason = paymentReason;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Boolean getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
