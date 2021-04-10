package com.example.nationalhealth.examination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.nationalhealth.R;

public class MedicalHistoryActivity extends AppCompatActivity {
    Button physicalExamination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_history);

        physicalExamination = findViewById(R.id.go_history_id);
        physicalExamination.setOnClickListener(physical->{
            startActivity(new Intent(getApplicationContext(),PhysicalExaminationActivity.class));
        });
    }
}