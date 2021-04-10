package com.neurotec.samples.app;

import android.os.Bundle;

import com.neurotec.samples.R;
import com.neurotec.samples.licensing.app.ActivationInfoFragment;

@SuppressWarnings( "deprecation" )
public final class InfoActivity extends BaseActivity {

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.app.ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);
		android.app.ActionBar.Tab tab1 = actionBar.newTab().setText(R.string.msg_about);
		android.app.ActionBar.Tab tab2 = actionBar.newTab().setText(R.string.msg_activation);
		android.app.ActionBar.Tab tab3 = actionBar.newTab().setText(R.string.msg_connection);

		tab1.setTabListener(new TabListener<AboutFragment>(this, AboutFragment.class));
		tab2.setTabListener(new TabListener<ActivationInfoFragment>(this, ActivationInfoFragment.class));
		tab3.setTabListener(new TabListener<ConnectionFragment>(this, ConnectionFragment.class));

		actionBar.addTab(tab1, true);
		actionBar.addTab(tab2);
		actionBar.addTab(tab3);
	}
}
