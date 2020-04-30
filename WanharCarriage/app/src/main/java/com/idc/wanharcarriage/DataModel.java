package com.idc.wanharcarriage;

public class DataModel {

    private String itemmain ;
    private String itemsecondary ;

    public DataModel() {
        this.itemmain = "00-00-0000";
        this.itemsecondary = "0";
    }

    public DataModel(String main, String secondary) {
        this.itemmain = main;
        this.itemsecondary = secondary;
    }

    public String getItemmain() {
        return itemmain;
    }

    public void setItemmain(String itemmain) {
        this.itemmain = itemmain;
    }

    public String getItemsecondary() {
        return itemsecondary;
    }

    public void setItemsecondary(String itemsecondary) {
        this.itemsecondary = itemsecondary;
    }
}
