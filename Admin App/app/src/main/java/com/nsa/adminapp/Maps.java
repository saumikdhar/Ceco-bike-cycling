package com.nsa.adminapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static android.support.constraint.Constraints.TAG;

public class Maps extends Fragment implements OnMapReadyCallback {
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    private GoogleMap mMap;
    private List<LatLng> list = null;
    private Points p;
    String data = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p = new Points("");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, null, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference reff = database.getDatabase().getReference().child("Journey");
        Query positionQuery = reff;
        positionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = "";
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    data += singleSnapshot.child("points").getValue().toString() + ";";
//                    Log.d("dataFire", singleSnapshot.child("points").getValue().toString());
                }
                p.setLocations(data);
                Log.d("lTag", "" +p.getLocations());
                //Render locations instead of heatmap
//                for (LatLng latLng : p.getLocations()) {
//                    mMap.addMarker(new MarkerOptions().position(latLng));
//                }
                //Render heatmap
                Collection<LatLng> collection = new ArrayList<>(p.getLocations());
                mProvider = new HeatmapTileProvider.Builder().data(collection).build();
                mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });



        mMap = googleMap;
        LatLng cardiff = new LatLng(51.495624, -3.176227);
        mMap.addMarker(new MarkerOptions().position(cardiff).title("Marker in Cardiff"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cardiff));



    }

    protected GoogleMap getMap() {

        return mMap;
    }


}
