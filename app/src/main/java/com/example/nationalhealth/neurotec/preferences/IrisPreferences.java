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
import com.neurotec.biometrics.NMatchingSpeed;
import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.samples.view.BasePreferenceFragment;

public final class IrisPreferences extends PreferenceActivity {

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final String MATCHING_SPEED = "iris_matching_speed";
	public static final String MAXIMAL_ROTATION = "iris_maximal_rotation";

	public static final String TEMPLATE_SIZE = "iris_template_size";
	public static final String QUALITY_THRESHOLD = "iris_quality_threshold";
	public static final String FAST_EXTRACTION = "iris_fast_extraction";

	public static final String IRIS_ENROLLMENT_CHECK_FOR_DUPLICATES = "iris_enrollment_check_for_duplicates";

	public static final String SET_DEFAULT_PREFERENCES = "iris_set_default_preferences";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static void updateClient(NBiometricClient client, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		client.setIrisesMatchingSpeed(NMatchingSpeed.get(Integer.valueOf(preferences.getString(MATCHING_SPEED, String.valueOf(NMatchingSpeed.LOW.getValue())))));
		client.setIrisesMaximalRotation(preferences.getInt(MAXIMAL_ROTATION, 15));

		client.setIrisesTemplateSize(NTemplateSize.get(Integer.valueOf(preferences.getString(TEMPLATE_SIZE, String.valueOf(NTemplateSize.SMALL.getValue())))));
		client.setIrisesQualityThreshold((byte) preferences.getInt(QUALITY_THRESHOLD, 5));
		client.setIrisesFastExtraction(preferences.getBoolean(FAST_EXTRACTION, false));
	}

	public static boolean isCheckForDuplicates(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(IRIS_ENROLLMENT_CHECK_FOR_DUPLICATES, true);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new IrisPreferencesFragment()).commit();
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

	public static class IrisPreferencesFragment extends BasePreferenceFragment {

		// ===========================================================
		// Public methods
		// ===========================================================

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.iris_preferences);
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			if (preference.getKey().equals(SET_DEFAULT_PREFERENCES)) {
				preferenceScreen.getEditor().clear().commit();
				getFragmentManager().beginTransaction().replace(android.R.id.content, new IrisPreferencesFragment()).commit();
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}
	}
}
