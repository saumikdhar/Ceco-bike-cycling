package com.nsa.adminapp;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class Points {

    private String locations;

    public Points(String locations) {
        this.locations = locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public List<LatLng> getLocations() {
        String[] l = locations.split(";");
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < l.length; i++) {
            String s = l[i];
            s = s.substring(1);
            s = s.replaceAll(".$", "");
            String[] split = s.split("], ");
            for (String s1 : split) {
                s1 = s1.replace("[", "");
                s1 = s1.replace("]", "");
                temp.add(s1);
            }
            l[i] = s;
        }
        List<LatLng> w = new ArrayList<>();
        for (String s : temp) {
            String[] f = s.split(", ");
            LatLng p = new LatLng(Double.parseDouble(f[0]),Double.parseDouble(f[1]));
            w.add(p);
        }
        return w;

    }
}
