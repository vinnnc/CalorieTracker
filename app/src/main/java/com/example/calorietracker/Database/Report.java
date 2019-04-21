package com.example.calorietracker.Database;

import java.util.Calendar;
import java.util.Date;

public class Report {
    private Integer repId;
    private Integer userId;
    private Date repDate;
    private Integer totalCalConsumed;
    private Integer totalCalBurned;
    private Integer totalSteps;
    private Integer calGoal;

    public Report(Integer repId, Integer userId, Integer totalCalConsumed, Integer totalCalBurned,
                  Integer totalSteps, Integer calGoal) {
        this.repId = repId;
        this.userId = userId;
        this.repDate = Calendar.getInstance().getTime();
        this.totalCalConsumed = totalCalConsumed;
        this.totalCalBurned = totalCalBurned;
        this.totalSteps = totalSteps;
        this.calGoal = calGoal;
    }

    public Integer getRepId() {
        return repId;
    }

    public void setRepId(Integer repId) {
        this.repId = repId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getRepDate() {
        return repDate;
    }

    public void setRepDate(Date repDate) {
        this.repDate = repDate;
    }

    public Integer getTotalCalConsumed() {
        return totalCalConsumed;
    }

    public void setTotalCalConsumed(Integer totalCalConsumed) {
        this.totalCalConsumed = totalCalConsumed;
    }

    public Integer getTotalCalBurned() {
        return totalCalBurned;
    }

    public void setTotalCalBurned(Integer totalCalBurned) {
        this.totalCalBurned = totalCalBurned;
    }

    public Integer getTotalSteps() {
        return totalSteps;
    }

    public void setTotalSteps(Integer totalSteps) {
        this.totalSteps = totalSteps;
    }

    public Integer getCalGoal() {
        return calGoal;
    }

    public void setCalGoal(Integer calGoal) {
        this.calGoal = calGoal;
    }
}
