package com.nsa.cecobike;

import android.Manifest;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

public class Map extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final int STORAGE_PERMISSION_CODE = 1;
    LatLng previousLocation;
    Button start_journey, finish_journey;
    private Chronometer Timer;
    private boolean running;
    LocationManager locationManager;
    boolean permissionIsGranted = false;
    double valueResult;
    ArrayList<String> test = new ArrayList<>();
    DatabaseReference reff;
    Journey ajourney;
    Location location = null;
    //List of Points for Database:
    ArrayList<Point> coordinates = new ArrayList<>();
    Double TotalDistance = 0.0;
    double totalDistanceKmRounded;

    public Map() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStoragePermission();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        AppCompatButton dialog = v.findViewById(R.id.finish_journey_button);

        dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.finish_journey_button, new Dialogbox());
                fr.commit();
            }
        });
        setHasOptionsMenu(false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map));
        FragmentManager fm = getChildFragmentManager();
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        //button to start the journey

        start_journey = view.findViewById(R.id.start_journey_button);
        finish_journey = view.findViewById(R.id.finish_journey_button);
        final JourneyDatabase db = Room.databaseBuilder(getContext(), JourneyDatabase.class, "MyJourneyDatabase").build();

        start_journey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start journey actions start here
                boolean getCurrentLocationFailed = false;
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                requestStoragePermission();
                final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );
                if (permissionIsGranted && manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    try {
                        getCurrentLocation();
                        getCurrentLocationFailed = false;
                    }catch (Exception e){
                        Log.d("Location error" , "Couldn't get location");
                        getCurrentLocationFailed = true;
                    }
                    if (!getCurrentLocationFailed) {
                        start_journey.setVisibility(View.GONE);
                        finish_journey.setVisibility(View.VISIBLE);
                        Timer = view.findViewById(R.id.timer);
                        startTimer(Timer);
                    }
                } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    Toast.makeText(getContext(), "Couldn't get location, Please ensure you enable GPS and aeroplane mode is off", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                            .setCancelable(false)
                            .setPositiveButton("Goto Settings Page To Enable GPS",
                                    new DialogInterface.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int id){
                                            Intent callGPSSettingIntent = new Intent(
                                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                            startActivity(callGPSSettingIntent);
                                        }
                                    });
                    alertDialogBuilder.setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int id){
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();
                }
            }
        });
        finish_journey.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                //finish journey actions start here

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestStoragePermission();
                    return;
                }
                //Calculates Distance
                if (coordinates.size() > 1) {
                    getlatlon();
                    Log.d("Distance", TotalDistance.toString());
                    totalDistanceKmRounded = round(TotalDistance, 2);
                    Log.d("Distance", String.valueOf(totalDistanceKmRounded));
                    final Double seconds = ((double) calculateElapsedTime(Timer) / 1000);
                    final Date currentDate = new Date();
                    Log.d(currentDate.toString(), "Date");
                    Log.d("Timer", String.valueOf(seconds));
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
//                        db.journeyDao().clearJourneys();
                            final List<Journey> journeys = db.journeyDao().getAllJourneys();
                            db.journeyDao().insertJourneys(
                                    new Journey( "Journey " + (String.valueOf(journeys.size() + 1)),totalDistanceKmRounded, seconds, currentDate, coordinates)

                            );
                            final ArrayList<Double> coords = new ArrayList<Double>() {};
                            final ArrayList<String> points = new ArrayList<String>() {};
                            final List<Journey> journeys2 = db.journeyDao().getAllJourneys();
                            Log.d("Journey_TEST", String.format("Number of Journeys: %d", journeys.size()));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(String.format("Number of Journeys: %d", journeys2.size()),"Total Journeys");
                                    for (Point pts: journeys2.get(journeys2.size() - 1).getCoordinates()) {
                                        Double lat = Double.valueOf(pts.getCoords()[0].toString());
                                        Double lon = Double.valueOf(pts.getCoords()[1].toString());

                                        coords.addAll(Arrays.asList(lat, lon));
                                        points.add(coords.toString());
                                        coords.clear();

                                        Log.d("Points", pts.getCoords()[0].toString()+":"+pts.getCoords()[1].toString());
                                    }

                                    ajourney = new Journey();
                                    reff = FirebaseDatabase.getInstance().getReference("Journey");;
                                    ajourney.setPoints(points);
                                    ajourney.setDate(currentDate);
                                    reff.push().setValue(ajourney);
                                }
                            });
                            db.close();
                        }
                    });
                    Dialogboxaction dialog = new Dialogboxaction();
                    dialog.show(getActivity().getSupportFragmentManager(), "anything");
                } else {
                    JourneyNotSavedDialogue dialog = new JourneyNotSavedDialogue();
                    dialog.show(getActivity().getSupportFragmentManager(), "Null");
                }
                locationManager.removeUpdates(locationListenerGPS);
                mMap.setMyLocationEnabled(false);
                stopTimer(Timer);
                final Double seconds = ((double) calculateElapsedTime(Timer) /1000);
                final Date currentDate = new Date();
                Log.d(currentDate.toString(), "Date");
                Log.d("Timer", String.valueOf(seconds));
            }
        });
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private long calculateElapsedTime(Chronometer mChronometer) {

        long stoppedMilliseconds = 0;

        String caronText = mChronometer.getText().toString();
        String array[] = caronText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }

        return stoppedMilliseconds;

    }

    public void startTimer (View v){
        if (!running) {
            Timer.setBase(SystemClock.elapsedRealtime());
            Timer.start();
            running = true;
        }
    }

    public void stopTimer (View v){
        if(running){
            Timer.stop();
            running=false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this.getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
            return;
        }
        mMap.setMyLocationEnabled(true);
        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        try {
            getCameraUpdates(location);
            previousLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }catch (Exception e){
            Log.d("Last Location" , "Couldn't get last location,  ...applying another method");
        }
//        previousLocation = new LatLng(location.getLatitude(), location.getLongitude());
        location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000,
                5, locationListenerGPS);

    }


    private void getCameraUpdates(Location location)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                .zoom(17)// Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this.getActivity(), "Access is now granted", Toast.LENGTH_SHORT).show();
                    permissionIsGranted = true;
                    // permission was granted, yay!
                } else {
//                    Toast.makeText(this.getActivity(), "Access has been declined by user", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this.getActivity(), "Permission must be accepted to start", Toast.LENGTH_SHORT).show();
                    permissionIsGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
        }
    }

    public void addPolyLinesToMap(final Location location) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                if (previousLocation != null) {
                    PolylineOptions polyline = new PolylineOptions().add(previousLocation)
                            .add(new LatLng(location.getLatitude(), location.getLongitude())).width(20).color(Color.BLUE).geodesic(true);
                    coordinates.add(new Point(location.getLatitude(), location.getLongitude()));
                    mMap.addPolyline(polyline);
                }
                previousLocation = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
    }

    public void getlatlon(){
        //Calculating the distance in meters
        double latitude = 0.0;
        double longitude = 0.0;

        for (int i = 0; i+1 < coordinates.size(); i++){
            double lat1 = coordinates.get(i).getpLat();
            double lat2 = coordinates.get(i+1).getpLat();
            double lon1 = coordinates.get(i).getpLon();
            double lon2 = coordinates.get(i+1).getpLon();
            latitude = Math.toRadians(lat2 - lat1);
            longitude = Math.toRadians(lon2 - lon1);
            Distance d = new Distance(latitude, longitude, lat1, lat2);
            TotalDistance = TotalDistance + d.getDistance();
//            getcaldistance(latitude, longitude, lat1, lat2);
        }
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            Toast.makeText(getActivity(), "Location update", Toast.LENGTH_SHORT).show();
            getCameraUpdates(location);
            addPolyLinesToMap(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
            ;

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


}
