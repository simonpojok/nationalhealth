package com.neurotec.samples.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.neurotec.samples.R;

public class SeekBarPreference extends Preference implements OnSeekBarChangeListener {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private final String TAG = getClass().getSimpleName();
	private static final String ANDROIDNS = "http://schemas.android.com/apk/res/android";
	private static final String SEEKBAR = "http://seekbar.com";
	private static final int DEFAULT_VALUE = 50;

	// ===========================================================
	// Private fields
	// ===========================================================

	private int mMaxValue = 100;
	private int mMinValue = 0;
	private int mInterval = 1;
	private int mCurrentValue;
	private boolean mShowMinMax = false;
	private SeekBar mSeekBar;
	private TextView mStatusText;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public SeekBarPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPreference(context, attrs);
	}

	public SeekBarPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPreference(context, attrs);
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private void initPreference(Context context, AttributeSet attrs) {
		setValuesFromXml(attrs);
		mSeekBar = new SeekBar(context, attrs);
		mSeekBar.setOnSeekBarChangeListener(this);
		mSeekBar.setMax(mMaxValue - mMinValue);
	}

	private void setValuesFromXml(AttributeSet attrs) {
		mMaxValue = attrs.getAttributeIntValue(ANDROIDNS, "max", 100);
		mMinValue = attrs.getAttributeIntValue(SEEKBAR, "min", 0);

		try {
			String newInterval = attrs.getAttributeValue(SEEKBAR, "interval");
			if (newInterval != null) mInterval = Integer.parseInt(newInterval);
		} catch (Exception e) {
			Log.e(TAG, "Invalid interval value", e);
		}
	}

	// ===========================================================
	// Protected methods
	// ===========================================================

	/**
	 * Update a SeekBarPreference view with our current state
	 * @param view
	 */
	protected void updateView(View view) {

		try {
			RelativeLayout layout = (RelativeLayout) view;

			mStatusText = (TextView) layout.findViewById(R.id.seekBarPrefValue);
			mStatusText.setText(String.valueOf(mCurrentValue));
			mStatusText.setMinimumWidth(30);

			mSeekBar.setProgress(mCurrentValue - mMinValue);

			if (this.mShowMinMax) {
				TextView unitsRight = (TextView) layout.findViewById(R.id.seekBarPrefUnitsRight);
				unitsRight.setText(String.valueOf(mMaxValue));

				TextView unitsLeft = (TextView) layout.findViewById(R.id.seekBarPrefUnitsLeft);
				unitsLeft.setText(String.valueOf(mMinValue));
			}

		} catch (Exception e) {
			Log.e(TAG, "Error updating seek bar preference", e);
		}

	}

	@Override
	protected View onCreateView(ViewGroup parent) {

		RelativeLayout layout = null;

		try {
			LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			layout = (RelativeLayout) mInflater.inflate(R.layout.seek_bar_preference, parent, false);
		} catch (Exception e) {
			Log.e(TAG, "Error creating seek bar preference", e);
		}

		return layout;
	}

	@Override
	protected Object onGetDefaultValue(TypedArray ta, int index) {

		int defaultValue = ta.getInt(index, DEFAULT_VALUE);
		return defaultValue;

	}

	@Override
	protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

		if (restoreValue) {
			mCurrentValue = getPersistedInt(mCurrentValue);
		} else {
			int temp = 0;
			try {
				temp = (Integer) defaultValue;
			} catch (Exception ex) {
				Log.e(TAG, "Invalid default value: " + defaultValue.toString());
			}

			persistInt(temp);
			mCurrentValue = temp;
		}

	}


	// ===========================================================
	// Public methods
	// ===========================================================

	public void setCurrentValue(int value) {
		mCurrentValue = value;
		mSeekBar.setProgress(mCurrentValue - mMinValue);
	}

	@Override
	public void onBindView(View view) {
		super.onBindView(view);

		try {
			// move our seekbar to the new view we've been given
			ViewParent oldContainer = mSeekBar.getParent();
			ViewGroup newContainer = (ViewGroup) view.findViewById(R.id.seekBarPrefBarContainer);

			if (oldContainer != newContainer) {
				// remove the seekbar from the old view
				if (oldContainer != null) {
					((ViewGroup) oldContainer).removeView(mSeekBar);
				}
				// remove the existing seekbar (there may not be one) and add ours
				newContainer.removeAllViews();
				newContainer.addView(mSeekBar, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			}
		} catch (Exception ex) {
			Log.e(TAG, "Error binding view: " + ex.toString());
		}

		updateView(view);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		int newValue = progress + mMinValue;

		if (newValue > mMaxValue)
			newValue = mMaxValue;
		else if (newValue < mMinValue)
			newValue = mMinValue;
		else if (mInterval != 1 && newValue % mInterval != 0) newValue = Math.round(((float) newValue) / mInterval) * mInterval;

		// change rejected, revert to the previous value
		if (!callChangeListener(newValue)) {
			seekBar.setProgress(mCurrentValue - mMinValue);
			return;
		}

		// change accepted, store it
		mCurrentValue = newValue;
		if (mStatusText != null) {
			mStatusText.setText(String.valueOf(newValue));
		}
		persistInt(newValue);

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		notifyChanged();
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		this.mSeekBar.setEnabled(enabled);
	}

	public void setShowMinMax(boolean show) {
		this.mShowMinMax = show;
	}

	public boolean getShowMinMax() {
		return mShowMinMax;
	}

	@Override
	public void onDependencyChanged(Preference dependency, boolean disableDependent) {
		super.onDependencyChanged(dependency, disableDependent);
		this.mSeekBar.setEnabled(!disableDependent);
	}
}