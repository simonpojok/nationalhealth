package com.example.nationalhealth.barcode;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.nationalhealth.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BarcodeRoomResultsActivity extends AppCompatActivity {
    BarcodeResultsModel barcodeResultsModel;
    RecyclerView bacodeRecyclerView;
    private Boolean created=true;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_room_results);
        getSupportActionBar().hide();

        bacodeRecyclerView = findViewById(R.id.display_visitors_recycler);
        bacodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        bacodeRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
//        visitorsDisplayAdapter = new VisitorsDisplayAdapter();

//        receivedVisitors();

//        final BarcodeResultsListAdapter barcodeResultsListAdapter = new BarcodeResultsListAdapter();
//        bacodeRecyclerView.setAdapter(barcodeResultsListAdapter);



    }
//
//    public void receivedVisitors(){
//        AdminRolesService services = ServiceGenerator.createService(AdminRolesService.class);
//        Call<List<ResponseDto>> call = services.fetchAllVisitors();
//
//        call.enqueue(new Callback<List<ResponseDto>>() {
//            @Override
//            public void onResponse(Call<List<ResponseDto>> call, Response<List<ResponseDto>> response) {
//                List<ResponseDto> responseDto = response.body();
//                    visitorsDisplayAdapter.setVisitorsData(responseDto);
//                    bacodeRecyclerView.setAdapter(visitorsDisplayAdapter);
//                Log.d("" , ""+responseDto);
//
//            }
//
//            @Override
//            public void onFailure(Call<List<ResponseDto>> call, Throwable t) {
//
//            }
//        });
//        call.enqueue(new Callback<ResponseDto>() {
//            @Override
//            public void onResponse(Call<ResponseDto> call, Response<ResponseDto> response) {
//                ResponseDto responseDto = response.body();
//                Log.d("" , ""+responseDto.getData());
//            }
//
//            @Override
//            public void onFailure(Call<ResponseDto> call, Throwable t) {
//                t.printStackTrace();
//                Log.d("", "onFailure: "+t.getMessage());
//            }
//        });
    }

//    public void receivedVisitors(){
//        AdminRolesService services = ServiceGenerator.createService(AdminRolesService.class);
//        Call<List<VisitorDisplayResponse>>callResponse= services.fetchAllVisitors();
//        callResponse.enqueue(new Callback<List<VisitorDisplayResponse>>() {
//            @Override
//            public void onResponse(Call<List<VisitorDisplayResponse>> call, Response<List<VisitorDisplayResponse>> response) {
//
//                if (response.isSuccessful()){
//                    List<VisitorDisplayResponse>visitorDisplayResponses=response.body();
//                    visitorsDisplayAdapter.setVisitorsData(visitorDisplayResponses);
//                    bacodeRecyclerView.setAdapter(visitorsDisplayAdapter);
//                    Toast.makeText(BarcodeRoomResultsActivity.this, "VISITORS"+response.body(), Toast.LENGTH_SHORT).show();
//                }
//
//                else {
//                    Toast.makeText(BarcodeRoomResultsActivity.this, "UNSUCCESSFUL RESPONSE"+response.body(), Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<VisitorDisplayResponse>> call, Throwable t) {
//                Toast.makeText(BarcodeRoomResultsActivity.this, "Failed: "+t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("VIEW VISITORS",""+t.getMessage());
//
//
//            }
//        });
