package com.idc.wanharcarriage.classes;

public class Expense {

    private String expenseDate;
    private String expenseEmployee;
    private String expenseType;
    private String expenseAmount;

    public Expense() {
        this.expenseDate = expenseDate;
        this.expenseEmployee = expenseEmployee;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
    }

    public Expense(String expenseDate, String expenseEmployee, String expenseType, String expenseAmount) {
        this.expenseDate = expenseDate;
        this.expenseEmployee = expenseEmployee;
        this.expenseType = expenseType;
        this.expenseAmount = expenseAmount;
    }


    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getExpenseEmployee() {
        return expenseEmployee;
    }

    public void setExpenseEmployee(String expenseEmployee) {
        this.expenseEmployee = expenseEmployee;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }
}
