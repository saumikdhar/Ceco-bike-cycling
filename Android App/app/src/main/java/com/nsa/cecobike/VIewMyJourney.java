package com.nsa.cecobike;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class VIewMyJourney extends Fragment implements AdapterView.OnItemClickListener {
    //    View Creation
    private View v;
    private List<Journey> listOfJourneys;
    private List<Goal> listOfGoals;
    private int numberOfJourneys;
    private JourneyDatabase db;
    private GoalDatabase gdb;
    private Double goalMax;
    private Double journeyDistance;
    private static final int COLUMN_COUNT = 1;
    private RecyclerView recyclerView;
    private CustomRecyclerViewAdapter customAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_view_journey_recycler, container, false);
        setHasOptionsMenu(false);

        listOfJourneys = new ArrayList<Journey>();
        db = Room.databaseBuilder(v.getContext(), JourneyDatabase.class, "MyJourneyDatabase").build();
        gdb = Room.databaseBuilder(v.getContext(), GoalDatabase.class, "MyGoalDatabase").build();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<Journey> journeys = db.journeyDao().getAllJourneys();
                listOfGoals = gdb.goalDao().getAllGoals();
                checkGoalInstance();
                listOfGoals = gdb.goalDao().getAllGoals();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        numberOfJourneys = journeys.size();
                        listOfJourneys = journeys;

                        Log.d(listOfJourneys.toString(), "All journeys");

                        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
                        CustomRecyclerViewAdapter recyclerViewAdapter = new CustomRecyclerViewAdapter(getContext(), listOfJourneys);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(recyclerViewAdapter);

                    }
                });
            }
        });
        return v;
    }

    public void checkGoalInstance() {
        Date currentDate = new Date();
        for (int i = 0; i < listOfGoals.size(); i++) {
            if (!listOfGoals.get(i).getMilestone_date().equals(android.text.format.DateFormat.format("MM/yyyy", currentDate))) {
                break;
            }
        }

        if (listOfGoals.size() == 0) {
            Goal newGoal = new Goal(20.0, (String) android.text.format.DateFormat.format("MM/yyyy",currentDate));
            gdb.goalDao().insertGoals(newGoal);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public VIewMyJourney() {
        // Required empty public constructor

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getContext(), String.format("Journey clicked on = %d", i), Toast.LENGTH_SHORT).show();
    }


    private class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.CustomViewHolder> {

        Context mContext;
        List<Journey>mData;

        public CustomRecyclerViewAdapter(Context mContext, List<Journey> mData) {
            this.mContext = mContext;
            this.mData = mData;
            Log.d("TEST", String.valueOf(mData.size()));
        }

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View row = LayoutInflater.from(mContext).inflate(R.layout.fragment_view_my_journey, parent, false);
            CustomViewHolder cHolder = new CustomViewHolder(row);
            return cHolder;
        }

        public int CalculateProgress(int position){
            int journeyProgress = 0;
            Double totalDistance = 0.0;
            for (Goal goal: listOfGoals) {
                goalMax = goal.getGoal_miles() * 1.609;
                for (Journey journey : listOfJourneys) {
                    totalDistance = totalDistance + journey.getDistance();
                    Log.d("Goal Distance =", String.valueOf(goalMax));
                    Log.d("Journey Distance =", String.valueOf(journey.getDistance()));
                    if (listOfJourneys.get(position).getJid() == journey.getJid()){
                       break;
                    }
                }
            }
                journeyDistance = totalDistance;
                journeyProgress = (int) Math.round(((journeyDistance * 271 * 1.609) / (goalMax * 271 * 1.609)) * 100);
                return journeyProgress;
            }

        @Override
        public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, int position) {
            int progress = CalculateProgress(position);
            customViewHolder.journeyText.setText(mData.get(position).getJourneyname());
            customViewHolder.dateAndTimeText.setText(android.text.format.DateFormat.format("dd-MM-yyyy  HH:mm:ss a" , (mData.get(position).getDate())));
            customViewHolder.milestoneText.setText(progress + "%");
            customViewHolder.progressBar.setProgress(progress);


        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        protected class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private AppCompatTextView journeyText;
            private AppCompatTextView dateAndTimeText;
            private AppCompatTextView milestoneText;
            private AppCompatTextView emSavedText;
            private AppCompatTextView emNeededText;
            private ProgressBar progressBar;

            CustomViewHolder(View itemView) {
                super(itemView);
                journeyText = (AppCompatTextView) itemView.findViewById(R.id.journey_text);
                dateAndTimeText = (AppCompatTextView) itemView.findViewById(R.id.dateAndTime_text);
                progressBar = (ProgressBar) itemView.findViewById(R.id.determ_circular_progress);
                milestoneText = (AppCompatTextView) itemView.findViewById(R.id.milestone);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                int i = this.getAdapterPosition();
                Bundle bundle = new Bundle();
                Log.d(String.valueOf(listOfJourneys.get(0).getJid()), " Parse id ");
                bundle.putInt("Journey id", i);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                ViewAJourney viewAJourney = new ViewAJourney();
                viewAJourney.setArguments(bundle);

                transaction.replace(R.id.start_fragment, viewAJourney);
                transaction.addToBackStack(null).commit();
            }
        }
    }

}
