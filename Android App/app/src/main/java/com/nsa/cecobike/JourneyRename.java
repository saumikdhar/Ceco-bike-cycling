package com.nsa.cecobike;

import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.widget.EditText;

import java.util.List;

public class JourneyRename extends AppCompatDialogFragment {

    private List<Journey> listOfJourneys;
    private JourneyDatabase db;
    int text;
    private EditText input;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        input = new EditText(getContext());
        builder.setView(input);
        builder.setTitle("Rename Journey") //change the dialog box header here
                .setCancelable(true)
                .setMessage("Journey Name")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Rename", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        renameAJourney();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.start_fragment, new VIewMyJourney());
                        transaction.commit();
                    }
                });
        return builder.create();

    }

    private void renameAJourney() {
        Bundle bundle = getArguments();
        text = bundle.getInt("Journey id");
        db = Room.databaseBuilder(getContext(), JourneyDatabase.class, "MyJourneyDatabase").build();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final List<Journey> journeys = db.journeyDao().getAllJourneys();
                listOfJourneys = journeys;
                db.journeyDao().updateAJourneyById(listOfJourneys.get(text).getJid(), input.getText().toString());
            }
        });
    }
}