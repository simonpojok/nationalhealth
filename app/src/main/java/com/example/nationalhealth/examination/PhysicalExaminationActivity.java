package com.example.nationalhealth.examination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.nationalhealth.R;

public class PhysicalExaminationActivity extends AppCompatActivity {
    Button differentialDiagnosisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physical_examination);

        differentialDiagnosisBtn= findViewById(R.id.go_physical_id);
        differentialDiagnosisBtn.setOnClickListener(diagnosis->{
            startActivity(new Intent(getApplicationContext(),DifferentialDiagnosisActivity.class));
        });
    }
}