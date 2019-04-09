package com.nsa.cecobike;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;
@Dao
public interface GoalDao {
        @Query("SELECT * FROM Goal")
        List<Goal> getAllGoals();

        @Insert
        void insertGoals(Goal... goals);

        @Query("DELETE FROM Goal")
        void clearGoals();


}
