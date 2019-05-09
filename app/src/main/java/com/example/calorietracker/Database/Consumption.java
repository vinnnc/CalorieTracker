package com.example.calorietracker.Database;

import java.util.Date;

public class Consumption {
    private Integer conid;
    private Users userid;
    private Date condate;
    private Integer foodid;
    private Integer quantity;

    public Consumption(Integer conid, Users userid, Date condate, Integer foodid,
                       Integer quantity) {
        this.conid = conid;
        this.userid = userid;
        this.condate = condate;
        this.foodid = foodid;
        this.quantity = quantity;
    }

    public Integer getConid() {
        return conid;
    }

    public void setConid(Integer conid) {
        this.conid = conid;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public Date getCondate() {
        return condate;
    }

    public void setCondate(Date condate) {
        this.condate = condate;
    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
