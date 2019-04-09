package com.nsa.cecobike;

import android.util.Log;

import java.text.DecimalFormat;

public class Distance {
    private Double latitude;
    private Double longitude;
    private Double lat1;
    private Double lat2;
    private Double distance;

    public Distance(Double latitude, Double longitude, Double lat1, Double lat2) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.lat1 = lat1;
        this.lat2 = lat2;
    }

    public Double getDistance(){
        double valueResult;
        int Radius = 6371;
        double a = Math.sin(latitude / 2) * Math.sin(latitude / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(longitude / 2)
                * Math.sin(longitude / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        Log.d("Radius" , String.valueOf(Radius * c));
        distance = valueResult;
        return distance;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLat1() {
        return lat1;
    }

    public Double getLat2() {
        return lat2;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", lat1=" + lat1 +
                ", lat2=" + lat2 +
                '}';
    }
}
