package com.nsa.cecobike;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Goal.class}, version = 1, exportSchema = false)
public abstract class GoalDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
}