package com.example.calorietracker.Database;

import java.util.Date;

public class Users {

    private Integer userid;
    private String firstname;
    private String surname;
    private String email;
    private Date dob;
    private Integer height;
    private Integer weight;
    private String gender;
    private String address;
    private Integer postcode;
    private Integer activitylv;
    private Integer steppermile;

    public Users() {
    }

    public Users(Integer userid, String firstname, String surname, String email, Date dob,
                 Integer height, Integer weight, String gender, String address, Integer postcode,
                 Integer activitylv, Integer steppermile) {
        this.userid = userid;
        this.firstname = firstname;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.address = address;
        this.postcode = postcode;
        this.activitylv = activitylv;
        this.steppermile = steppermile;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
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

    public Integer getActivitylv() {
        return activitylv;
    }

    public void setActivitylv(Integer activitylv) {
        this.activitylv = activitylv;
    }

    public Integer getSteppermile() {
        return steppermile;
    }

    public void setSteppermile(Integer steppermile) {
        this.steppermile = steppermile;
    }

    @Override
    public String toString() {
        return "Users{" +
                "userid=" + userid +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", height=" + height +
                ", weight=" + weight +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", postcode=" + postcode +
                ", activitylv=" + activitylv +
                ", steppermile=" + steppermile +
                '}';
    }
}
