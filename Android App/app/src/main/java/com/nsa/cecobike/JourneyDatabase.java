package com.nsa.cecobike;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Journey.class}, version = 1, exportSchema = false)
@TypeConverters({DataTypeConverter.class})
public abstract class JourneyDatabase extends RoomDatabase {
    public abstract JourneyDao journeyDao();
}
