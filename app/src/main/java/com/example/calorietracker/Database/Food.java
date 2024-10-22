package com.example.calorietracker.Database;

public class Food {
    private Integer foodid;
    private String foodname;
    private String category;
    private Double calorie;
    private String unit;
    private Double amount;
    private Double fat;

    public Food() {
    }

    public Food(Integer foodid, String foodname, String category, Double calorie, String unit,
                Double amount, Double fat) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.category = category;
        this.calorie = calorie;
        this.unit = unit;
        this.amount = amount;
        this.fat = fat;
    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getCalorie() {
        return calorie;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }
}
