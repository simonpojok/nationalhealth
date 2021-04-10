package com.example.nationalhealth;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nationalhealth.barcode.BarcodeResults;
import com.example.nationalhealth.barcode.BarcodeResultsDAO;


@Database(entities = {BarcodeResults.class},version = 13)
public abstract class MishoRoomDB extends RoomDatabase {
    private static MishoRoomDB INSTANCE;

    public static synchronized MishoRoomDB getInstance(Context context){
        if (INSTANCE == null){
            synchronized (MishoRoomDB.class){
                if (INSTANCE==null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MishoRoomDB.class,
                            "misho_DB")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract BarcodeResultsDAO barcodeResultsDAO();

}
