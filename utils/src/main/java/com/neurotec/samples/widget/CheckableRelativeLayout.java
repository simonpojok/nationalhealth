package com.neurotec.samples.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class CheckableRelativeLayout extends RelativeLayout implements Checkable {

	// ===========================================================
	// Private fields
	// ===========================================================

	private boolean mIsChecked;
	private List<Checkable> mCheckableViews;

	// ===========================================================
	// Public constructors
	// ===========================================================

	public CheckableRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initFromAttributes(attrs);
	}

	public CheckableRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initFromAttributes(attrs);
	}

	public CheckableRelativeLayout(Context context, int checkableId) {
		super(context);
		initFromAttributes(null);
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private void initFromAttributes(AttributeSet attrs) {
		this.mIsChecked = false;
		this.mCheckableViews = new ArrayList<Checkable>(5);
	}

	private void findCheckableChildren(View v) {
		if (v instanceof Checkable) {
			this.mCheckableViews.add((Checkable) v);
		}

		if (v instanceof ViewGroup) {
			final ViewGroup vg = (ViewGroup) v;
			final int childCount = vg.getChildCount();
			for (int i = 0; i < childCount; ++i) {
				findCheckableChildren(vg.getChildAt(i));
			}
		}
	}

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		final int childCount = this.getChildCount();
		for (int i = 0; i < childCount; ++i) {
			findCheckableChildren(this.getChildAt(i));
		}
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public boolean isChecked() {
		return mIsChecked;
	}

	@Override
	public void setChecked(boolean isChecked) {
		this.mIsChecked = isChecked;
		for (Checkable c : mCheckableViews) {
			c.setChecked(isChecked);
		}
	}

	@Override
	public void toggle() {
		this.mIsChecked = !this.mIsChecked;
		for (Checkable c : mCheckableViews) {
			c.toggle();
		}
	}
}