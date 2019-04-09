package com.nsa.cecobike;

import java.util.ArrayList;
import java.util.List;

//Holds Lat and Long as Decimal Point to be converted to string
public class Point {
    private Double pLat;
    private Double pLon;
    private ArrayList<Double> Coords;


    public Point(Double pLat, Double pLon) {
        this.pLat = pLat;
        this.pLon = pLon;
    }

    public Double getpLat() {
        return pLat;
    }

    public Double getpLon() {
        return pLon;
    }


    public Double[] getCoords(){
        Double coord[] = new Double[2];
        coord[0] = pLat;
        coord[1] = pLon;
        return  coord;

    }


}
