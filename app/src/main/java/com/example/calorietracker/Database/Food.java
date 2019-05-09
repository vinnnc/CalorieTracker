package com.example.calorietracker.Database;

public class Food {
    private Integer foodid;
    private String name;
    private String category;
    private Integer calorie;
    private String unit;
    private Integer amount;
    private Integer fat;

    public Food(Integer foodId, String name, String category, Integer calorie, String unit,
                Integer amount, Integer fat) {
        this.foodid = foodId;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }
}
