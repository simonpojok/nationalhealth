package com.example.nationalhealth.examination;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.nationalhealth.R;

public class VitalSignsActivity extends AppCompatActivity {
    Button medicalHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vital_signs);

        medicalHistoryBtn =findViewById(R.id.go_vital_id);

        medicalHistoryBtn.setOnClickListener(medicalHistory->{
          startActivity(new Intent(getApplicationContext(),PhysicalExaminationActivity.class));
        });
    }
}