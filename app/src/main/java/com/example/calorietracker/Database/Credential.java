package com.example.calorietracker.Database;

import java.util.Calendar;
import java.util.Date;

public class Credential {
    private String username;
    private Users userid;
    private String passwordhash;
    private Date signupdate;

    public Credential(String username, Users userid, String passwordhash) {
        this.username = username;
        this.userid = userid;
        this.passwordhash = passwordhash;
        this.signupdate = Calendar.getInstance().getTime();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public String getPasswordhash() {
        return passwordhash;
    }

    public void setPasswordhash(String passwordhash) {
        this.passwordhash = passwordhash;
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }
}
