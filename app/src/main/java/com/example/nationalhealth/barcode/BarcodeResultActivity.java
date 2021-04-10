package com.example.nationalhealth.barcode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nationalhealth.R;
import com.example.nationalhealth.activities.FillFormActivity;
import com.example.nationalhealth.adapters.BarcodeCapturedResultsAdapter;
import com.example.nationalhealth.models.BarcodeCapturedResultsModel;

import java.util.ArrayList;
import java.util.List;


public class BarcodeResultActivity extends AppCompatActivity implements View.OnClickListener{
    final String TAG = BarcodeResultActivity.class.getName();

    Button submitResults;
    BarcodeResultsRepository barcodeResultsRepository;
    private RecyclerView recyclerView;
    private List<BarcodeCapturedResultsModel> list;
    BarcodeResults barcodeResults;
    BarcodeResultsModel barcodeResultsModel;
    private BarcodeCapturedResultsAdapter barcodeAdapter;
    BarcodeResultsDAO barcodeResultsDAO;
    String surname;
    String givenName;
    String dateOfBirth;
    String sex;
    String nin;
    String cardNumber;
    String expirationDate;
    String nationality;

    Context context;
    Boolean created=true;

    private TextView capturedPhone;
    public static final String Phone = "phoneKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_result);

        getSupportActionBar().hide();
        // saveVisitorDetails(surname,givenName,sex,dateOfBirth,nin,cardNumber,expirationDate,phoneNumber);
        list = new ArrayList<>();
        recyclerView = findViewById(R.id.barcodeResultsId);

        Intent receivedintent = getIntent();
        barcodeResultsRepository = new BarcodeResultsRepository(getApplication());
        BarcodeCapturedResultsModel receivedModel = (BarcodeCapturedResultsModel) receivedintent.getSerializableExtra("SentModel");
        surname = receivedModel.getSurname();
        givenName = receivedModel.getGivenName();
        dateOfBirth = receivedModel.getDateOfBirth();
        sex = receivedModel.getSex();
        nin = receivedModel.getNin();
        cardNumber = receivedModel.getDocumentNumber();
        expirationDate = receivedModel.getExpirationDate();
        nationality = receivedModel.getPhoneNumber();
        list.add(receivedModel);
        barcodeResults = new BarcodeResults(surname, givenName, dateOfBirth, sex, nin, cardNumber, expirationDate, nationality);
        barcodeAdapter = new BarcodeCapturedResultsAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(barcodeAdapter);
        submitResults = findViewById(R.id.submitscannedUser_id);
        submitResults.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), FillFormActivity.class));
//            Visitor visitor = new Visitor();
//            visitor.setSurName(surname);
//            visitor.setGivenName(givenName);
//            visitor.setDob(dateOfBirth);
//            visitor.setExpDate(expirationDate);
//            visitor.setNationalId(nin);
//            visitor.setCardNumber(cardNumber);
//            visitor.setPhoneNumber(phoneNumber);

            Log.d("visitor details:"+cardNumber,givenName);
            //on endpoint
//            savingVisitorDetails(visitor);
            //saving into room db
            addVisitorDetailsBarcode();
    }

//    private void savingVisitorDetails(Visitor visitor) {
//        ServiceGenerator.createService(AuthenticationServices.class).saveVisitorDetails(visitor)
//                .enqueue(new Callback<ResponseDto>() {
//            @Override
//            public void onResponse(Call<ResponseDto> call, Response<ResponseDto> response) {
//                if (response.isSuccessful()) {
//                    Log.d(TAG,""+response.body());
//                    ResponseDto responseDto = response.body();
//                    Toast.makeText(getApplicationContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(BarcodeResultActivity.this, MYImageActivity.class);
//                    startActivity(i);
//                }
////                else if (response.body().getMessage().matches(cardNumber)){
////                    Toast.makeText(getApplicationContext(), "User Exists", Toast.LENGTH_SHORT).show();
////
////                }
//                else {
//                    Toast.makeText(BarcodeResultActivity.this, "Not successful..." + response.body(), Toast.LENGTH_SHORT).show();
//                    Log.d(TAG, "Not Successsful...Unsuccessful" + response.body());
//                    Intent i = new Intent(BarcodeResultActivity.this, MYImageActivity.class);
//                    startActivity(i);
//                    Log.d(TAG,""+ visitor);
//                } }
//
//            @Override
//            public void onFailure(Call<ResponseDto> call, Throwable t) {
//             t.printStackTrace();
//                Toast.makeText(BarcodeResultActivity.this, "Failed Visitor Details"+t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Failed connection: "+t.getMessage());
//            }});


    public void addVisitorDetailsBarcode(){
        barcodeResultsRepository.insert(barcodeResults);
        created = true;
        if (created){
            Toast.makeText(getApplicationContext(), "Added Sqlite Now", Toast.LENGTH_SHORT).show();
        }
    }
}