package com.example.nationalhealth.barcode;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BarcodeResultsModel extends AndroidViewModel {
    private final BarcodeResultsRepository barRepository;
    private final LiveData<List<BarcodeResults>> allBarcodeResults;

    public BarcodeResultsModel(@NonNull Application application) {
        super(application);
        this.barRepository = new BarcodeResultsRepository(application);
        this.allBarcodeResults = barRepository.getAllBarcodeResults();
    }

    public LiveData<List<BarcodeResults>> getAllBarcodeResults() {

        return allBarcodeResults;
    }

    public void insert(BarcodeResults barcodeResults) {
        barRepository.insert(barcodeResults);
    }

    void deleteBarcodeResult(BarcodeResults barcodeResults){
        barRepository.deleteBarcodeResult(barcodeResults);

    }

}
