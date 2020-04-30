package com.shaheen.myapplication;

import java.util.ArrayList;

public class Profile {

    private String profileid;
    private String profilename ;
    private String userid ;
    private String profilepicture;


    public Profile() {
        this.profileid = "" ;
        this.profilename = "";
        this.userid = "";
        this.profilepicture = Integer.toString(R.id.school);
    }

    public Profile(String profileid, String profilename, String userid, String profilepicture) {
        this.profileid = profileid ;
        this.profilename = profilename;
        this.userid = userid;
        this.profilepicture = profilepicture;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }
}
