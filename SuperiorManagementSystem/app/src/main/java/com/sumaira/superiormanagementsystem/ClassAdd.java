package com.sumaira.superiormanagementsystem;

public class ClassAdd {
    String class_id;
    String section;
    String namae;


    public ClassAdd(String class_id, String section, String namae) {
        this.class_id = class_id;
        this.section = section;
        this.namae = namae;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getNamae() {
        return namae;
    }

    public void setNamae(String namae) {
        this.namae = namae;
    }
}
