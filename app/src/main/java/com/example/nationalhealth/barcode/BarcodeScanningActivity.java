package com.example.nationalhealth.barcode;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.nationalhealth.R;
import com.example.nationalhealth.models.BarcodeCapturedResultsModel;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.barcode.Barcode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static com.example.nationalhealth.R.id.barcode_fragment_id;

public class BarcodeScanningActivity extends AppCompatActivity implements BarcodeRetriever {
    private static final String TAG = "BarcodeMain";
    static final public int SUCCESS_BARCODE_RESULT = 0;
    public static final String BARCODEKEY = "BARCODEKEY";

    CheckBox fromXMl, pause;
    SwitchCompat drawRect, autoFocus, supportMultiple, touchBack, drawText, flash, frontCam;
    public final static int BARCODE_RESULT = 0002;
    BarcodeCapture barcodeCapture;
    private String surname, cardNumber;
    private String givenName, nin, dateofbirth, expirydate;
    String sex;
    String nationality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanning);

        getSupportActionBar().hide();

        barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(barcode_fragment_id);
        barcodeCapture.setRetrieval(this);

//        fromXMl = (CheckBox) findViewById(R.id.from_xml);
//        pause = (CheckBox) findViewById(R.id.pause);
//        drawRect = (SwitchCompat) findViewById(R.id.draw_rect);
//        autoFocus = (SwitchCompat) findViewById(R.id.focus);
//        supportMultiple = (SwitchCompat) findViewById(R.id.support_multiple);
//        touchBack = (SwitchCompat) findViewById(R.id.touch_callback);
//        drawText = (SwitchCompat) findViewById(R.id.draw_text);
//        flash = (SwitchCompat) findViewById(R.id.on_flash);
//        frontCam = (SwitchCompat) findViewById(R.id.front_cam);

//        findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                barcodeCapture.stopScanning();
//            }
//        });

//        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (fromXMl.isChecked()) {
//
//                } else {
//                    barcodeCapture.setShowDrawRect(drawRect.isChecked())
//                            .setSupportMultipleScan(supportMultiple.isChecked())
//                            .setTouchAsCallback(touchBack.isChecked())
//                            .shouldAutoFocus(autoFocus.isChecked())
//                            .setShowFlash(flash.isChecked())
//                            .setShowFlash(flash.isChecked())
//                            .setBarcodeFormat(Barcode.ALL_FORMATS)
//                            .setCameraFacing(frontCam.isChecked() ? CameraSource.CAMERA_FACING_FRONT : CameraSource.CAMERA_FACING_BACK)
//                            .setShouldShowText(drawText.isChecked());
//                }
//            }
//        });
    }

    @Override
    public void onRetrieved(final Barcode barcode) {
//        Log.d(TAG, "Barcode read: " + barcode.displayValue);
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanningActivity.this)
                        .setTitle("code retrieved")
                        .setMessage(barcode.displayValue);

                //splitting
                String results = barcode.displayValue;
                String[] splitted = results.split(";");
                for (String sp : splitted) {
//                    Log.d(TAG,sp);
                }
                //base64 decode
                Base64.Decoder decoder = Base64.getDecoder();
                surname = new String(decoder.decode(splitted[0]));
                givenName = new String(decoder.decode(splitted[1]));

                SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyy");
                Date dateOfBirth = null, expiryDate = null;
                try {
                    dateOfBirth = sdf.parse(splitted[3]);
                    expiryDate = sdf.parse(splitted[5]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                sdf = new SimpleDateFormat("yyy-MM-dd");
                String sexpiryDate = sdf.format(expiryDate);
                String sdateOfBirth = sdf.format(dateOfBirth);

                surname = new String(decoder.decode(splitted[0]));
                givenName = new String(decoder.decode(splitted[1]));
                cardNumber = (splitted[7]);
                nin = (splitted[6]);
                dateofbirth = (sdateOfBirth);
                expirydate = (sexpiryDate);
                String NIN = splitted[6];
                char sexletter = NIN.charAt(1);
                System.out.println(sexletter);
                sex = String.valueOf((sexletter));
                nationality ="UGANDA";

                Toast.makeText(BarcodeScanningActivity.this, "Captured Nation is: " + nationality, Toast.LENGTH_SHORT).show();

    BarcodeCapturedResultsModel myModel = new BarcodeCapturedResultsModel(surname, givenName, dateofbirth, sex, nin, cardNumber, expirydate, nationality);
    Intent data = new Intent(getApplicationContext(), BarcodeResultActivity.class);
    data.putExtra("SentModel", myModel);
    startActivity(data);

//                Intent personData = new Intent(getApplicationContext(), PersonsActivity.class);
//                data.putExtra("SentModel", myModel);
//                startActivity(personData);

    Log.d("letsTry", surname);
    Log.d("letsTry", givenName);
    Log.d("letsTry", dateofbirth);
    Log.d("letsTry", sex);
    Log.d("letsTry", nin);
    Log.d("letsTry", cardNumber);
    Log.d("letsTry", expirydate);
    Log.d("trying",barcode.displayValue);


                BarcodeScanningActivity barcodeScanned = new BarcodeScanningActivity();
                if (barcodeScanned.toString().equals(barcode.displayValue)) {
                    finish();
                }
            }
        });
        barcodeCapture.stopScanning();
    }

    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(BarcodeScanningActivity.this)
                        .setTitle("code retrieved")
                        .setMessage(message);

                builder.show();
            }
        });
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        for (int i = 0; i < sparseArray.size(); i++) {
            Barcode barcode = sparseArray.valueAt(i);
//            Log.e("value", barcode.displayValue);
        }
    }

    @Override
    public void onRetrievedFailed(String reason) {
    }

    @Override
    public void onPermissionRequestDenied() {

    }

}