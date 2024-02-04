package com.example.stage;

import java.util.UUID;

public class User {
    String userID;
    String username;
    String mail;
    String password;

    public User() {
    }

    public User( String username, String mail, String password) {
        this.userID = UUID.randomUUID().toString();
        this.username = username;
        this.mail = mail;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
