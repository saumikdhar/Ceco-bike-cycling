package com.nsa.cecobike;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static android.support.constraint.Constraints.TAG;

public class SetGoal extends Fragment {
    SeekBar seekBar;
    TextView textView;
    Button  setGoalButton;
    Button  removeGoalButton;
    private Double goal_Miles = 0.0;
    public SetGoal() {
        // Required empty public constructor
    }

    //Seekbar set goal in Miles
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_set_goal, container, false);

        //Getting IDs
        seekBar = v.findViewById(R.id.seek_bar);
        textView = v.findViewById(R.id.goal_miles);
        setGoalButton = (Button) v.findViewById(R.id.set_goal);
        removeGoalButton = (Button) v.findViewById(R.id.remove_goal);

        //Set Default Miles
        final String i = "0";
        textView.setText(i);
        goal_Miles = Double.valueOf(textView.getText().toString());


        //Distance Slider
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                goal_Miles = (double) progress;
                textView.setText(progress + "");
                Log.d(TAG, "onProgressChanged: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Logs the set value, Goal should match this
                Log.d(TAG, "onStopTrackingTouch: " + goal_Miles);

            }
        });
//        Insert Button Task
        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                final GoalDatabase db = Room.databaseBuilder(v.getContext(), GoalDatabase.class, "MyGoalDatabase").build();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Goal> goals = db.goalDao().getAllGoals();
                        //Init DTF formatter for date
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/yyyy");
                        DateTimeFormatter mtf = DateTimeFormatter.ofPattern("MM");
                        DateTimeFormatter ytf = DateTimeFormatter.ofPattern("yyyy");
                        LocalDateTime now = LocalDateTime.now();
                        String dateNow = String.valueOf(dtf.format(now));
                        String monthNow = String.valueOf(mtf.format(now));
                        String yearNow = String.valueOf(ytf.format(now));
                        
                        Log.d("Goals: ",String.valueOf(goals.size()));

                        if (goals.size() == 0) {
                            Goal newGoal = new Goal(goal_Miles, String.valueOf(dateNow));
                            db.goalDao().insertGoals(newGoal);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Goal Set", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        Log.d("Test", "Im here now " + dateNow);
                        for (int j = 0; j < goals.size(); j++) {
                            String currentMonth = goals.get(j).getMilestone_date();
                            List<String> milestone_dates = Arrays.asList(currentMonth.split("/"));
                            Log.d("date", milestone_dates.get(0));
                            Log.d("date", currentMonth);
                            Log.d("Milestone Dates", String.valueOf(milestone_dates.toString()));

                            if (!milestone_dates.get(0).equals(monthNow) && (milestone_dates.get(1).equals(yearNow)) ||
                                    milestone_dates.get(0).equals(monthNow) && (!milestone_dates.get(1).equals(yearNow))) {
                                db.goalDao().insertGoals(
                                        new Goal(goal_Miles, String.valueOf(dateNow))
                                );
                                Log.i("New GOAL", goals.get(j).getGoal_miles().toString() + " - " + goals.get(j).getMilestone_date());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Goal Set", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "You have already set a goal", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
                db.close();
            }
        });
//      End Insert Task

        removeGoalButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                final GoalDatabase db = Room.databaseBuilder(v.getContext(), GoalDatabase.class, "MyGoalDatabase").build();
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Goal> goals = db.goalDao().getAllGoals();
                        Log.d("Goals to be removed", String.valueOf(goals.size()));
                        db.goalDao().clearGoals();
                    }
                });
                db.close();
                Toast.makeText(getContext(), "Removed Goal", Toast.LENGTH_SHORT).show();
            }
        });
        setHasOptionsMenu(false);
        return v;
    }

    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
