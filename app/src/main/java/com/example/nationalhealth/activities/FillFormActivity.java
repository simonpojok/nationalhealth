package com.example.nationalhealth.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.nationalhealth.R;
import com.example.nationalhealth.examination.VitalSignsActivity;

public class FillFormActivity extends AppCompatActivity {
    Button vitalSignsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_form);

        vitalSignsBtn = findViewById(R.id.goVital_id);
        vitalSignsBtn.setOnClickListener(vital->{
            startActivity(new Intent(getApplicationContext(), VitalSignsActivity.class));
        });
    }
}