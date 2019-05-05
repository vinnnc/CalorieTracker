package com.example.calorietracker.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Step {
    @PrimaryKey(autoGenerate = true)
    public int sid;

    @ColumnInfo(name = "time")
    public String time;

    @ColumnInfo(name = "step")
    public int step;

    public Step(String time, int step) {
        this.time = time;
        this.step = step;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}