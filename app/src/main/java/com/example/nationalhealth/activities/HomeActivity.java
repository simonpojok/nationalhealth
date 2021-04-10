package com.example.nationalhealth.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nationalhealth.R;
import com.example.nationalhealth.barcode.BarcodeScanningActivity;

public class HomeActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 256;
    ImageButton next;
    TextView callTxt, dataPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        next = findViewById(R.id.go);
        callTxt = findViewById(R.id.call_text_field);
        dataPolicy = findViewById(R.id.data_policy);

        next.setOnClickListener(goNext ->{
            startActivity(new Intent(getApplicationContext(), BarcodeScanningActivity.class));
        });

        dataPolicy.setOnClickListener(policy -> {
            startActivity(new Intent(getApplicationContext(),DataPolicyActivity.class));
        });

    }
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.health_home_menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.helplines:
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Sure, Call Support Team ??");
                    builder.setMessage("MISHO");
                    builder.setIcon(R.mipmap.ic_launcher_misho_foreground);
                    builder.setPositiveButton("Confirm",
                            (dialog, which) -> {
                                String number=callTxt.getText().toString();
                                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+number));
                                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CALL_PHONE)
                                        != PackageManager.PERMISSION_GRANTED) {

                                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                                } else {
                                    //You already have permission
                                    try {
                                        startActivity(callIntent);
                                    } catch(SecurityException e) {
                                        e.printStackTrace();
                                    } }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return true;

                case R.id.about_misho:
                    startActivity(new Intent(getApplicationContext(),AboutMishoActivity.class));
                    return true;

                case R.id.data_policy:
                    startActivity(new Intent(getApplicationContext(),DataPolicyActivity.class));
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }


    }
}