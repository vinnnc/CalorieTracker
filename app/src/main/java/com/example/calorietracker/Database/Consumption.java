package com.example.calorietracker.Database;

import java.util.Calendar;
import java.util.Date;

public class Consumption {
    private Integer conId;
    private Integer userId;
    private Date conDate;
    private Integer foodId;
    private Integer quantity;

    public Consumption(Integer conId, Integer userId, Integer foodId, Integer quantity) {
        this.conId = conId;
        this.userId = userId;
        this.conDate = Calendar.getInstance().getTime();
        this.foodId = foodId;
        this.quantity = quantity;
    }

    public Integer getConId() {
        return conId;
    }

    public void setConId(Integer conId) {
        this.conId = conId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getConDate() {
        return conDate;
    }

    public void setConDate(Date conDate) {
        this.conDate = conDate;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
