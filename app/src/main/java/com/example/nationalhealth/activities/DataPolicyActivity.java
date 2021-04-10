package com.example.nationalhealth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nationalhealth.R;


public class DataPolicyActivity extends AppCompatActivity {
    Button policyBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_policy);

        getSupportActionBar().hide();

        policyBtn = findViewById(R.id.btn_data_policy);
        policyBtn.setOnClickListener(exitBtn->{
            startActivity(new Intent(this, HomeActivity.class));
        });
    }
}