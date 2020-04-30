package com.sumaira.superiorcms;

class User {
    private String name ;
    private String email;
    private String password;
    private int userType ;

    public User(){
        name = "" ;
        email = "" ;
        password = "" ;
        userType = 1 ;
    }

    public User(String name, String email, String password, int userType) {

        this.name = name ;
        this.email = email ;
        this.password = password ;
        this.userType = userType ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
