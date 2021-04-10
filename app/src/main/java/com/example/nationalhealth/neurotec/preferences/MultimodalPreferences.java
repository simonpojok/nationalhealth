package com.example.nationalhealth.neurotec.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.nationalhealth.R;
import com.neurotec.lang.NCore;
import com.neurotec.samples.preferences.BasePreferenceActivity;
import com.neurotec.samples.view.BasePreferenceFragment;

public class MultimodalPreferences extends BasePreferenceActivity {

	public static final String DUPLICATE_CHECK = "duplicate_check";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MultimodalPreferencesFragment()).commit();
	}

	public static boolean isCheckForDuplicates() {
		if (NCore.getContext() == null) throw new NullPointerException("NCore.setContext() should be set");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NCore.getContext());
		return preferences.getBoolean(DUPLICATE_CHECK, true);
	}

	public static class MultimodalPreferencesFragment extends BasePreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.multimodal_preferences);
		}
	}
}
