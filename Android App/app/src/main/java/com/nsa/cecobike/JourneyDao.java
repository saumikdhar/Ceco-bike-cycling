package com.nsa.cecobike;

import android.app.Activity;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.Update;
import java.util.List;
//

@Dao
public interface JourneyDao { ;
    @Query("SELECT * FROM Journey")
    List<Journey> getAllJourneys();

    @Insert
    void insertJourneys(Journey... journey);

    @Query("DELETE FROM Journey")
    void clearJourneys();

    @Query("DELETE FROM Journey WHERE jid = :journeyId")
    void deleteAJourneyById(long journeyId);

    @Query("UPDATE journey SET journeyName = :jName WHERE jid = :journeyID")
    void updateAJourneyById(long journeyID, String jName);

}
