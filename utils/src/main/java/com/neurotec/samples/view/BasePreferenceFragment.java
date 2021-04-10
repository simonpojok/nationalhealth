package com.neurotec.samples.view;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

public class BasePreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {

	// ===========================================================
	// Protected methods
	// ===========================================================

	protected void updatePrefsSummary(SharedPreferences sharedPreferences, Preference pref) {
		if (pref == null) {
			return;
		}
		if (pref instanceof ListPreference) {
			ListPreference listPref = (ListPreference) pref;
			listPref.setSummary(listPref.getEntry());
		} else if (pref instanceof EditTextPreference) {
			EditTextPreference editTextPref = (EditTextPreference) pref;
			editTextPref.setSummary(editTextPref.getText());
		}
	}

	protected void initSummary() {
		for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
			initPrefsSummary(getPreferenceManager().getSharedPreferences(), getPreferenceScreen().getPreference(i));
		}
	}

	protected void initPrefsSummary(SharedPreferences sharedPreferences, Preference p) {
		if (p instanceof PreferenceCategory) {
			PreferenceCategory pCat = (PreferenceCategory) p;
			for (int i = 0; i < pCat.getPreferenceCount(); i++) {
				initPrefsSummary(sharedPreferences, pCat.getPreference(i));
			}
		} else {
			updatePrefsSummary(sharedPreferences, p);
		}
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		initSummary();
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		updatePrefsSummary(sharedPreferences, findPreference(key));
	}
}
