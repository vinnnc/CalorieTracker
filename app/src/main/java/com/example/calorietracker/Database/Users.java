package com.example.calorietracker.Database;

import java.util.Date;

public class Users {
    private Integer userId;
    private String firstName;
    private String surname;
    private String email;
    private Date dob;
    private Integer height;
    private Integer weight;
    private String gender;
    private String address;
    private Integer postcode;
    private Integer activityLv;
    private Integer stepPerMile;

    public Users(Integer userId, String firstName, String surname, String email, Date dob,
                 Integer height, Integer weight, String gender, String address, Integer postcode,
                 Integer activityLv, Integer stepPerMile) {
        this.userId = userId;
        this.firstName = firstName;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.activityLv = activityLv;
        this.stepPerMile = stepPerMile;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPostcode() {
        return postcode;
    }

    public void setPostcode(Integer postcode) {
        this.postcode = postcode;
    }

    public Integer getActivityLv() {
        return activityLv;
    }

    public void setActivityLv(Integer activityLv) {
        this.activityLv = activityLv;
    }

    public Integer getStepPerMile() {
        return stepPerMile;
    }

    public void setStepPerMile(Integer stepPerMile) {
        this.stepPerMile = stepPerMile;
    }
}
