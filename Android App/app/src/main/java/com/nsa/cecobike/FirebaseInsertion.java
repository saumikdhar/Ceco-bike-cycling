package com.nsa.cecobike;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class FirebaseInsertion extends AppCompatActivity {
    DatabaseReference reff;
    Journey ajourney;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        final TextView timeAndDateText = findViewById(R.id.time_and_date_text);
        final TextView distAndDurationText = findViewById(R.id.distance_and_duration_text);
        final TextView emissionsText = findViewById(R.id.emissions_text);
        Button fin = findViewById(R.id.finish_journey_button);
//        ajourney = new Journey();
        reff = FirebaseDatabase.getInstance().getReference().child("Journey");
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double disatnce = Double.parseDouble(distAndDurationText.getText().toString().trim());
                Double duration = Double.parseDouble(distAndDurationText.getText().toString().trim());
//                Date timeanddate = Date.parse(timeAndDateText.getText().toString().trim());
                Float emissions = Float.parseFloat(emissionsText.getText().toString().trim());

//                ajourney.setDate(timeanddate);
                ajourney.setDuration(duration);
                ajourney.setDistance(disatnce);
                reff.push().setValue(ajourney);
            }
        });
    }
}
