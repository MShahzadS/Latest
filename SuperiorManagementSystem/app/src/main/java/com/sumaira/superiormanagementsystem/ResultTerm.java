package com.sumaira.superiormanagementsystem;

public class ResultTerm {

    private String termName ;
    private String termMonth ;

    public ResultTerm() {
        this.termName = "";
        this.termMonth = "";
    }

    public ResultTerm(String termName, String termMonth) {
        this.termName = termName;
        this.termMonth = termMonth;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getTermMonth() {
        return termMonth;
    }

    public void setTermMonth(String termMonth) {
        this.termMonth = termMonth;
    }
}
