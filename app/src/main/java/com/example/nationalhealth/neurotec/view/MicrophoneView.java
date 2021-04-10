package com.example.nationalhealth.neurotec.view;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.LinearLayout;

import com.example.nationalhealth.R;


public class MicrophoneView extends LinearLayout {

	// ===========================================================
	// Private fields
	// ===========================================================

	private Chronometer mChronometer;
	private View mRecIndicator;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public MicrophoneView(Context context) {
		super(context);
		LayoutInflater  mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.microphone_view, this, true);
		mChronometer = (Chronometer) view.findViewById(R.id.chronometer_view);
		mRecIndicator = view.findViewById(R.id.rec_indicator);
		stop();
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public void start(){
		Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.rec_blink);
		mRecIndicator.setVisibility(View.VISIBLE);
		mRecIndicator.startAnimation(anim);
		mChronometer.setBase(SystemClock.elapsedRealtime());
		mChronometer.start();
	}

	public void stop(){
		mRecIndicator.setVisibility(View.INVISIBLE);
		mChronometer.stop();
		mRecIndicator.clearAnimation();
	}

}