package com.example.nationalhealth.barcode;

import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;

/**
 * Created by zone2 on 11/12/17.
 */

public class CustomBarCodeDetector extends Detector<Barcode> {

    public SparseArray<Barcode> detect(Frame frame) {
        return null;
    }

    public boolean isOperational() {
        return super.isOperational();
    }
}
