package com.example.nationalhealth.barcode;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.nationalhealth.MishoRoomDB;

import java.util.List;

public class BarcodeResultsRepository {
    private BarcodeResultsDAO barcodeResultsDAO;
    private LiveData<List<BarcodeResults>> allBarcodeResults;

    public BarcodeResultsRepository(Application application) {
        MishoRoomDB db = MishoRoomDB.getInstance(application);
        barcodeResultsDAO = db.barcodeResultsDAO();
        allBarcodeResults = barcodeResultsDAO.getAllBarcodeResults();
    }
    public LiveData<List<BarcodeResults>> getAllBarcodeResults() {

        return allBarcodeResults;
    }

    public void insert(BarcodeResults barcodeResults) {
        new InsertBarcodeResultsAsyncTask(barcodeResultsDAO).execute(barcodeResults);
    }

    public void deleteBarcodeResult(BarcodeResults barcodeResults) {
        new DeleteBarcodeResultsAsyncTask(barcodeResultsDAO).execute(barcodeResults);
    }

    private static class InsertBarcodeResultsAsyncTask extends AsyncTask<BarcodeResults, Void, Void> {
        private BarcodeResultsDAO taskBarcodeResultsDao;

        public InsertBarcodeResultsAsyncTask(BarcodeResultsDAO barcodeResultsDAO) {
            taskBarcodeResultsDao = barcodeResultsDAO;
        }

        @Override
        protected Void doInBackground(final BarcodeResults... barcodeResults) {
            taskBarcodeResultsDao.insert(barcodeResults[0]);
            return null;
        }
    }
    private static class DeleteBarcodeResultsAsyncTask extends AsyncTask<BarcodeResults, Void, Void> {
        private BarcodeResultsDAO taskBarcodeResultsDao;

        public DeleteBarcodeResultsAsyncTask(BarcodeResultsDAO barcodeResultsDAO) {
            this.taskBarcodeResultsDao = barcodeResultsDAO;
        }

        @Override
        protected Void doInBackground(final BarcodeResults... barcodeResults) {
            taskBarcodeResultsDao.deleteBarcodeResult(barcodeResults[0]);
            return null;
        }
    }
}
