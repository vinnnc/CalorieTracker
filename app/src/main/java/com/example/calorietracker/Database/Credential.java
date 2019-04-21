package com.example.calorietracker.Database;

import java.util.Calendar;
import java.util.Date;

public class Credential {
    private String username;
    private Integer userId;
    private String passwordHash;
    private Date signUpDate;

    public Credential(String username, Integer userId, String passwordHash) {
        this.username = username;
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.signUpDate = Calendar.getInstance().getTime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getSignUpDate() {
        return signUpDate;
    }

    public void setSignUpDate(Date signUpDate) {
        this.signUpDate = signUpDate;
    }
}
