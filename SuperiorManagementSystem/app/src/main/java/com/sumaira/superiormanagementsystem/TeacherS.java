package com.sumaira.superiormanagementsystem;

public class TeacherS extends User {

    String qualification;
    String experience;
    String cnic;
    public TeacherS(String cnic, String qualification, String experience) {
        super(experience, qualification, cnic,1);
        this.qualification = qualification;
        this.experience = experience;
        this.cnic = cnic;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getcnic() {
        return cnic;
    }

    public void setcnic(String cnic) {
        this.cnic = cnic;
    }
}


