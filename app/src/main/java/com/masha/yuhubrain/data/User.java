package com.masha.yuhubrain.data;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String email;
    private String fullName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public User(){}
    public User(String username, String email, String fullName){
        this.email = email;
        this.username = username;
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
