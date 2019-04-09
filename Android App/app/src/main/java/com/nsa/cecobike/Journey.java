package com.nsa.cecobike;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;

@Entity
public class Journey {

    @PrimaryKey(autoGenerate = true)
    private int jid;

    @ColumnInfo(name = "journeyName")
    private String journeyname;

    @ColumnInfo(name = "distanceKm")
    private Double distance;

    @ColumnInfo(name = "duration")
    private Double duration;

    @ColumnInfo(name = "date")
    private Date date;

    @ColumnInfo(name = "coordinates")
    private ArrayList<Point> coordinates;

    @ColumnInfo(name = "coordinatesString")
    private List<String> points;


    @Ignore
    public Journey() {
    }


    public Journey(String journeyname, Double distance, Double duration, Date date, ArrayList<Point> coordinates) {
        this.journeyname = journeyname;
        this.distance = distance;
        this.duration = duration;
        this.date = date;
        this.coordinates = coordinates;
    }

    public void setJid(int jid) {
        this.jid = jid;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setCoordinates(ArrayList<Point> coordinates) {
        this.coordinates = coordinates;
    }


    public int getJid() {
        return jid;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }

    public String getJourneyname () {
        return journeyname;
    }

    public ArrayList<Point> getCoordinates () {
        return coordinates;
    }

    @Override
    public String toString () {
        return "Journey{" +
                "jid=" + jid +
                ", journeyname='" + journeyname + '\'' +
                ", distance=" + distance +
                ", duration=" + duration +
                ", date=" + date +
                ", coordinates=" + (coordinates == null ? null : Arrays.asList(coordinates)) +
                '}';
    }
}