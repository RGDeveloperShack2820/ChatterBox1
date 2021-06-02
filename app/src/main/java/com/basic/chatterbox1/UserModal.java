package com.basic.chatterbox1;


public class UserModal {
    String name;
    String phno;
    String userId;

    public UserModal(String name, String phno, String userId) {
        this.userId= userId;
        this.name = name;
        this.phno = phno;
    }

    public String getName() {
        return name;
    }

    public String getphno() {
        return phno;
    }

    public String getUserId() {return userId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public void setUserId(String userId) { this.userId = userId; }
}

