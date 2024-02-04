package com.example.stage;

import java.time.LocalDate;
import java.util.UUID;

public class Fact {
    String text;
    String date;

    String factID;
    String userID;

    public Fact() {
    }

    public Fact(String text, String userID) {
        this.text = text;
        this.date = LocalDate.now().toString();
        this.factID = UUID.randomUUID().toString();
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFactID() {
        return factID;
    }

    public void setFactID(String factID) {
        this.factID = factID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
