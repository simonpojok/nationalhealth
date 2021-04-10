package com.example.nationalhealth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nationalhealth.R;


public class AboutMishoActivity extends AppCompatActivity {
    Button abt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_misho);

        abt = findViewById(R.id.btn_abt);

        abt.setOnClickListener(about->{
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        });
    }
}