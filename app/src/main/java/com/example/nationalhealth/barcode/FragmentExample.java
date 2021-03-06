package com.example.nationalhealth.barcode;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.nationalhealth.R;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

import static com.example.nationalhealth.R.id.barcode_fragment_id;

public class FragmentExample extends Fragment implements BarcodeRetriever {


    CheckBox fromXMl;
    SwitchCompat drawRect, autoFocus, supportMultiple, touchBack, drawText, flash, frontCam;

    BarcodeCapture barcodeCapture;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_main, container, false);


        barcodeCapture = (BarcodeCapture) getChildFragmentManager().findFragmentById(barcode_fragment_id);
        barcodeCapture.setRetrieval(this);
//
//        fromXMl = (CheckBox) rootView.findViewById(R.id.from_xml);
//        drawRect = (SwitchCompat) rootView.findViewById(R.id.draw_rect);
//        autoFocus = (SwitchCompat) rootView.findViewById(R.id.focus);
//        supportMultiple = (SwitchCompat) rootView.findViewById(R.id.support_multiple);
//        touchBack = (SwitchCompat) rootView.findViewById(R.id.touch_callback);
//        drawText = (SwitchCompat) rootView.findViewById(R.id.draw_text);
//        flash = (SwitchCompat) rootView.findViewById(R.id.on_flash);
//        frontCam = (SwitchCompat) rootView.findViewById(R.id.front_cam);

        rootView.findViewById(R.id.stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                barcodeCapture.stopScanning();
            }
        });

//        rootView.findViewById(R.id.refresh);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromXMl.isChecked()) {

                } else {
                    barcodeCapture.setShowDrawRect(drawRect.isChecked())
                            .setSupportMultipleScan(supportMultiple.isChecked())
                            .setTouchAsCallback(touchBack.isChecked())
                            .shouldAutoFocus(autoFocus.isChecked())
                            .setShowFlash(flash.isChecked())
                            .setBarcodeFormat(Barcode.ALL_FORMATS)
                            .setCameraFacing(frontCam.isChecked() ? CameraSource.CAMERA_FACING_FRONT : CameraSource.CAMERA_FACING_BACK)
                            .setShouldShowText(drawText.isChecked());
                    barcodeCapture.refresh();
                }
            }
        });
        return rootView;


    }

    @Override
    public void onRetrieved(Barcode barcode) {

    }

    @Override
    public void onRetrievedMultiple(Barcode closetToClick, List<BarcodeGraphic> barcode) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onRetrievedFailed(String reason) {

    }

    @Override
    public void onPermissionRequestDenied() {

    }
}
