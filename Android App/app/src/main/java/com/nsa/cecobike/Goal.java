package com.nsa.cecobike;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Goal {

    @PrimaryKey(autoGenerate = true)
    private int gid;

    @ColumnInfo(name = "goal_Miles")
    private double goal_miles;

    @ColumnInfo(name = "date")
    private String milestone_date;


    public Goal(double goal_miles, String milestone_date) {
        this.goal_miles = goal_miles;
        this.milestone_date = milestone_date;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public int getGid() {
        return gid;
    }

    public Double getGoal_miles() {
        return goal_miles;
    }

    public void setGoal_miles(double goal_miles) {
        this.goal_miles = goal_miles;
    }

    public void setMilestone_date(String milestone_date) {
        this.milestone_date = milestone_date;
    }

    public String getMilestone_date() {
        return milestone_date;
    }

    @Override
    public String toString() {
        return "Goal{" +
                "gid=" + gid +
                ", Goal_Miles=" + goal_miles +
                ", milestone_date='" + milestone_date + '\'' +
                '}';
    }
}
