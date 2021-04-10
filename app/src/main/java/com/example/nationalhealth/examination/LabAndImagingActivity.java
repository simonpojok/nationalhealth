package com.example.nationalhealth.examination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.nationalhealth.R;

public class LabAndImagingActivity extends AppCompatActivity {
    Button diagnosisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_and_imaging);

        diagnosisBtn =findViewById(R.id.go_lab_id);
        diagnosisBtn.setOnClickListener(diagnosis ->{
            startActivity(new Intent(getApplicationContext(),PatientDiagnosisActivity.class));
        });
    }
}