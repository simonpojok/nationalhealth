package com.example.nationalhealth.examination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.nationalhealth.R;

public class DifferentialDiagnosisActivity extends AppCompatActivity {
    Button labBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_differential_diagnosis);

        labBtn=findViewById(R.id.go_diferential_id);
        labBtn.setOnClickListener(lab->{
            startActivity(new Intent(getApplicationContext(),LabAndImagingActivity.class));
        });
    }
}