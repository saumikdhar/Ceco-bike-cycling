package com.nsa.cecobike;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;


public class JourneySummary extends Fragment {

    private List<Journey> listOfJourneys;
    private JourneyDatabase db;
    Button closebutton;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_jouney_summary, container, false);
        // Inflate the layout for this fragment
        getJourneyInfo(v);
        closebutton = (Button) v.findViewById(R.id.close_button);
        closebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.start_fragment, new Map());
                transaction.commit();
            }
        });
        setHasOptionsMenu(false);
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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        listOfJourneys = journeys;
                        Log.d("test journey id", String.valueOf(listOfJourneys.get(0).getJid()));
                        TextView JourneyText = (TextView) v.findViewById(R.id.journey_summary_text);
                        JourneyText.setText("Date: " + android.text.format.DateFormat.format("yyyy-MM-dd", (listOfJourneys.get(listOfJourneys.size() - 1).getDate()))
                                + System.lineSeparator() + System.lineSeparator() + "Time: " + android.text.format.DateFormat.format("HH:mm:ss a" ,listOfJourneys.get(listOfJourneys.size() - 1).getDate())
                                + System.lineSeparator() + System.lineSeparator() + "Distance: " + listOfJourneys.get(listOfJourneys.size() - 1).getDistance() + " Km"
                                + System.lineSeparator() + System.lineSeparator() + "Duration: " + listOfJourneys.get(listOfJourneys.size() - 1).getDuration()
                                + System.lineSeparator() + System.lineSeparator() + "Car emissions saved " + listOfJourneys.get(listOfJourneys.size() - 1).getDistance() * 271+ "g");
//                        JourneyText.setText("Date and Time: " + listOfJourneys.get(journeys.size()).getDateAndTime() );

                    }
                });
            }
        });
        return v;
    }

}
