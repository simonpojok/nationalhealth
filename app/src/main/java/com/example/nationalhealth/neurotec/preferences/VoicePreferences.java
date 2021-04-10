package com.example.nationalhealth.neurotec.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import androidx.core.app.NavUtils;

import com.example.nationalhealth.R;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.samples.view.BasePreferenceFragment;

public final class VoicePreferences extends PreferenceActivity {

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final String TEXT_DEPENDENT_MODE = "Text dependent";
	
	public static final String UNIQUE_PHRASES_ONLY = "voice_unique_phrases_only";
	
	public static final String EXTRACTION_MODE = "voice_extraction_mode";

	public static final String VOICE_ENROLLMENT_CHECK_FOR_DUPLICATES = "voice_enrollment_check_for_duplicates";

	public static final String SET_DEFAULT_PREFERENCES = "voice_set_default_preferences";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static void updateClient(NBiometricClient client, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		client.setVoicesUniquePhrasesOnly(preferences.getBoolean(UNIQUE_PHRASES_ONLY, false));
		String extractionMode = preferences.getString(EXTRACTION_MODE, TEXT_DEPENDENT_MODE);
		client.setVoicesExtractTextDependentFeatures(extractionMode.equals(TEXT_DEPENDENT_MODE));
		client.setVoicesExtractTextIndependentFeatures(true);
	}

	public static boolean isCheckForDuplicates(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(VOICE_ENROLLMENT_CHECK_FOR_DUPLICATES, true);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new VoicePreferencesFragment()).commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static class VoicePreferencesFragment extends BasePreferenceFragment {

		// ===========================================================
		// Public methods
		// ===========================================================

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.voice_preferences);
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			if (preference.getKey().equals(SET_DEFAULT_PREFERENCES)) {
				preferenceScreen.getEditor().clear().commit();
				getFragmentManager().beginTransaction().replace(android.R.id.content, new VoicePreferencesFragment()).commit();
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
	}

}
