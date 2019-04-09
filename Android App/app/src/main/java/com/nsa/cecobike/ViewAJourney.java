package com.nsa.cecobike;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class ViewAJourney extends Fragment implements OnMapReadyCallback {

    private List<Journey> listOfJourneys;
    private JourneyDatabase db;
    private GoogleMap mMap;
    int text;
    ArrayList<Point> coordinates = new ArrayList<>();

    public ViewAJourney() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_view_ajourney, container, false);
        // Inflate the layout for this fragment
        getJourneyInfo(v);
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.view_my_journey_on_map));
        FragmentManager fm = getChildFragmentManager();
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.view_my_journey_on_map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return v;
    }

    private View getJourneyInfo(final View v){
        db = Room.databaseBuilder(getContext(), JourneyDatabase.class, "MyJourneyDatabase").build();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<Journey> journeys = db.journeyDao().getAllJourneys();
                getActivity().runOnUiThread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        listOfJourneys = journeys;
                        Log.d("test journey id", String.valueOf(listOfJourneys.get(text).toString()));
                        Bundle bundle = getArguments();
                        text = bundle.getInt("Journey id");
                        Log.d("actual journey id", String.valueOf(text));
                        TextView timeAndDateText = v.findViewById(R.id.time_and_date_text);
                        TextView distAndDurationText = v.findViewById(R.id.distance_and_duration_text);
                        TextView emissionsText = v.findViewById(R.id.emissions_text);
//                        JourneyText.setText(String.format("Date: %s%s%sDistance: %s Miles%s%sDuration: %ss", listOfJourneys.get(text).getDate(), System.lineSeparator(), System.lineSeparator(), listOfJourneys.get(text).getDistance(), System.lineSeparator(), System.lineSeparator(), listOfJourneys.get(text).getDuration()));
                        timeAndDateText.setText("Date: " + android.text.format.DateFormat.format("dd-MM-yyyy", (listOfJourneys.get(text).getDate())) + "  Time: " + android.text.format.DateFormat.format("HH:mm:ss a" ,listOfJourneys.get(text).getDate()));
                        distAndDurationText.setText("Distance: " + listOfJourneys.get(text).getDistance() + " Km" + "   Duration: " + listOfJourneys.get(text).getDuration()+"s");
                        emissionsText.setText("Car emissions saved: " + (listOfJourneys.get(text).getDistance() * 271) + "g");
                    }
                });
            }
        });
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        coordinates = listOfJourneys.get(text).getCoordinates();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i+1 < coordinates.size(); i++){
            addPolyLinesToMap(coordinates.get(i).getpLat(), coordinates.get(i).getpLon(), coordinates.get((i+1)).getpLat(),coordinates.get((i+1)).getpLon());
            builder.include(new LatLng(coordinates.get(i).getpLat(), coordinates.get(i).getpLon()));
            builder.include(new LatLng(coordinates.get((i+1)).getpLat(),coordinates.get((i+1)).getpLon()));
        }
        LatLngBounds bounds = builder.build();
        int padding = 100;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        if (listOfJourneys.get(text).getCoordinates().size() != 0){
            mMap.addMarker(new MarkerOptions().position(new LatLng(listOfJourneys.get(text).getCoordinates().get(0).getpLat() , listOfJourneys.get(text).getCoordinates().get(0).getpLon())).title("Start location"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(listOfJourneys.get(text).getCoordinates().get(coordinates.size() - 1).getpLat() , listOfJourneys.get(text).getCoordinates().get(coordinates.size() - 1).getpLon())).title("End location"));
            Log.d("Number of journeys", listOfJourneys.toString());
        }
    }

    public void addPolyLinesToMap(final Double pLat1, final Double pLon1, final Double pLat2, final Double pLon2) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                PolylineOptions polyline = new PolylineOptions().add(new LatLng(pLat1, pLon1))
                        .add(new LatLng(pLat2, pLon2)).width(20).color(Color.BLUE).geodesic(true);
                mMap.addPolyline(polyline);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main3, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Bundle bundle = new Bundle();
        bundle.putInt("Journey id", text);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {

            ConfirmationDialogue dialog = new ConfirmationDialogue();
            dialog.setArguments(bundle);

            dialog.show(getActivity().getSupportFragmentManager(), "Null");
            return true;
        }else if (id == R.id.action_rename) {

            JourneyRename dialog = new JourneyRename();
            dialog.setArguments(bundle);

            dialog.show(getActivity().getSupportFragmentManager(), "Null");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
