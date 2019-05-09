package com.example.calorietracker.Database;

import java.util.Date;

public class Report {
    private Integer repid;
    private Users userid;
    private Date repdate;
    private Integer totalcalconsumed;
    private Integer totalcalburned;
    private Integer totalsteps;
    private Integer calgoal;

    public Report(Integer repid, Users userid, Date repdate, Integer totalcalconsumed,
                  Integer totalcalburned, Integer totalsteps, Integer calgoal) {
        this.repid = repid;
        this.userid = userid;
        this.repdate = repdate;
        this.totalcalconsumed = totalcalconsumed;
        this.totalcalburned = totalcalburned;
        this.totalsteps = totalsteps;
        this.calgoal = calgoal;
    }

    public Integer getRepid() {
        return repid;
    }

    public void setRepid(Integer repid) {
        this.repid = repid;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public Date getRepdate() {
        return repdate;
    }

    public void setRepdate(Date repdate) {
        this.repdate = repdate;
    }

    public Integer getTotalcalconsumed() {
        return totalcalconsumed;
    }

    public void setTotalcalconsumed(Integer totalcalconsumed) {
        this.totalcalconsumed = totalcalconsumed;
    }

    public Integer getTotalcalburned() {
        return totalcalburned;
    }

    public void setTotalcalburned(Integer totalcalburned) {
        this.totalcalburned = totalcalburned;
    }

    public Integer getTotalsteps() {
        return totalsteps;
    }

    public void setTotalsteps(Integer totalsteps) {
        this.totalsteps = totalsteps;
    }

    public Integer getCalgoal() {
        return calgoal;
    }

    public void setCalgoal(Integer calgoal) {
        this.calgoal = calgoal;
    }
}
