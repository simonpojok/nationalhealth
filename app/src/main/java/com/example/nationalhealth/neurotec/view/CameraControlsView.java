package com.example.nationalhealth.neurotec.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.nationalhealth.R;

public class CameraControlsView extends LinearLayout {

	// ===========================================================
	// Public types
	// ===========================================================

	public interface CameraControlsListener {
		void onSwitchCamera();
		void onChangeFormat();
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private CameraControlsListener mListener;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public CameraControlsView(Context context, CameraControlsListener listener) {
		super(context);
		if (listener == null) throw new NullPointerException("listener");
		mListener = listener;
		LayoutInflater  mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.camera_controls, this, true);
		ImageView switchCamera = (ImageView) view.findViewById(R.id.switch_camera);
		switchCamera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onSwitchCamera();
			}
		});

		ImageView changeFormat = (ImageView) view.findViewById(R.id.change_format);
		changeFormat.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onChangeFormat();
			}
		});
	}

}