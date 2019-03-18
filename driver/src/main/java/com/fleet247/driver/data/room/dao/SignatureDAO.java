package com.fleet247.driver.data.room.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fleet247.driver.data.models.Signature;

import java.util.List;

@Dao
public interface SignatureDAO {

    /**
     * Save signature in database.
     * if save data appears replace it.
     * @param signature
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addSignature(Signature signature);

    @Query("SELECT * FROM SIGNATURE WHERE type='start' ORDER BY dateTimeStamp LIMIT 1")
    Signature getStartSignature();

    @Query("SELECT * FROM SIGNATURE WHERE type='end' ORDER BY dateTimeStamp LIMIT 1")
    Signature getEndSignature();

    /**
     *
     * @return Signature object ordered by datetime
     */
    @Query("SELECT * FROM SIGNATURE ORDER BY dateTimeStamp LIMIT 1")
    Signature getSignature();

    /**
     * Returns list of all signatures saved in database ordered by datetime.
     * @return
     */
    @Query("SELECT * FROM SIGNATURE ORDER BY dateTimeStamp ")
    List<Signature> getAllSignature();

    /**
     *
     * @param bookingId bookingId of Signature to be deleted.
     * @param type type of signature to be deleted.
     */
    @Query("DELETE FROM SIGNATURE WHERE bookingId=:bookingId AND type=:type")
    void deleteSignature(String bookingId,String type);

    @Delete()
    void deleteSignature(Signature signature);
}
