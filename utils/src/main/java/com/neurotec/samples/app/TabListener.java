package com.neurotec.samples.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

@SuppressWarnings( "deprecation" )
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
	private Fragment mFragment;
	private final Activity mActivity;
	private final Class<T> mClass;
	
	public TabListener(Activity activity, Class<T> clz, Fragment fragment) {
		mFragment = fragment;
		mActivity = activity;
		mClass = clz;
	}

	public TabListener(Activity activity, Class<T> clz) {
		this(activity, clz, null);
	}

	public TabListener(Fragment fragment) {
		this(null, null, fragment);
	}

	public void onTabSelected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
		if (mFragment == null) {
			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			ft.replace(android.R.id.content, mFragment, mClass.getName());
		} else {
			ft.replace(android.R.id.content, mFragment);
		}
	}

	public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
			ft.remove(mFragment);
		}
	}

	public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
	}
}