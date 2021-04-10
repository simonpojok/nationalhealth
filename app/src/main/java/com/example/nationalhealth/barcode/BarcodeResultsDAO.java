package com.example.nationalhealth.barcode;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BarcodeResultsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(BarcodeResults barcodeResults);

    @Query("DELETE from misho_barcode_userDetails")
    public void deleteall();

    @Query("SELECT * from misho_barcode_userDetails ORDER BY surname asc")
    LiveData<List<BarcodeResults>> getAllBarcodeResults();

    @Delete
    public void deleteBarcodeResult(BarcodeResults barcodeResults);

}
