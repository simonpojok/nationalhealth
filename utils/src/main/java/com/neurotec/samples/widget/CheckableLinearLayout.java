package com.neurotec.samples.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

/* This class is useful for using inside of ListView that needs to have
 * checkable items.
 */
public final class CheckableLinearLayout extends LinearLayout implements Checkable {

	// ===========================================================
	// Private fields
	// ===========================================================

	private CheckedTextView mCheckbox;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public CheckableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// find checked text view
		int childCount = getChildCount();
		for (int i = 0; i < childCount; ++i) {
			View v = getChildAt(i);
			if (v instanceof CheckedTextView) {
				mCheckbox = (CheckedTextView) v;
			}
		}
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public boolean isChecked() {
		return mCheckbox != null ? mCheckbox.isChecked() : false;
	}

	@Override
	public void setChecked(boolean checked) {
		if (mCheckbox != null) {
			mCheckbox.setChecked(checked);
		}
	}

	@Override
	public void toggle() {
		if (mCheckbox != null) {
			mCheckbox.toggle();
		}
	}
}