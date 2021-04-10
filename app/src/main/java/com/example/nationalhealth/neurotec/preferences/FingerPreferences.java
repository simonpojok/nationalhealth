package com.example.nationalhealth.neurotec.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;

import androidx.core.app.NavUtils;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.Model;
import com.neurotec.biometrics.NMatchingSpeed;
import com.neurotec.biometrics.NTemplateSize;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceType;
import com.neurotec.samples.view.BasePreferenceFragment;

import java.util.ArrayList;
import java.util.List;


public final class FingerPreferences extends PreferenceActivity {

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final String FINGER_CAPTURING_DEVICE = "finger_capturing_device";

	public static final String MATCHING_SPEED = "finger_matching_speed";
	public static final String MAXIMAL_ROTATION = "finger_maximal_rotation";

	public static final String TEMPLATE_SIZE = "finger_template_size";
	public static final String QUALITY_THRESHOLD = "finger_quality_threshold";
	public static final String FAST_EXTRACTION = "finger_fast_extraction";
	public static final String RETURN_BINARIZED_IMAGE = "finger_return_binarized_image";

	public static final String FINGER_ENROLLMENT_CHECK_FOR_DUPLICATES = "finger_enrollment_check_for_duplicates";

	public static final String SET_DEFAULT_PREFERENCES = "finger_set_default_preferences";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static void updateClient(NBiometricClient client, Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		client.setFingersMatchingSpeed(NMatchingSpeed.get(Integer.valueOf(preferences.getString(MATCHING_SPEED, String.valueOf(NMatchingSpeed.LOW.getValue())))));
		client.setFingersMaximalRotation(Float.valueOf(preferences.getInt(MAXIMAL_ROTATION, 180)));

		client.setFingersTemplateSize(NTemplateSize.get(Integer.valueOf(preferences.getString(TEMPLATE_SIZE, String.valueOf(NTemplateSize.SMALL.getValue())))));
		client.setFingersQualityThreshold((byte) preferences.getInt(QUALITY_THRESHOLD, (byte) 39));
		client.setFingersFastExtraction(preferences.getBoolean(FAST_EXTRACTION, false));
		client.setFingersReturnBinarizedImage(preferences.getBoolean(RETURN_BINARIZED_IMAGE, false));
	}

	public static boolean isCheckForDuplicates(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(FINGER_ENROLLMENT_CHECK_FOR_DUPLICATES, true);
	}

	public static boolean isReturnBinarizedImage(Context context) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getBoolean(RETURN_BINARIZED_IMAGE, false);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new FingerPreferencesFragment()).commit();
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

	public static class FingerPreferencesFragment extends BasePreferenceFragment {

		// ===========================================================
		// Public methods
		// ===========================================================

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.finger_preferences);
			setDeviceListPreferenceData((ListPreference) findPreference(FINGER_CAPTURING_DEVICE));
		}

		@Override
		public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
			if (preference.getKey().equals(SET_DEFAULT_PREFERENCES)) {
				preferenceScreen.getEditor().clear().commit();
				getFragmentManager().beginTransaction().replace(android.R.id.content, new FingerPreferencesFragment()).commit();
			}
			return super.onPreferenceTreeClick(preferenceScreen, preference);
		}

		protected void setDeviceListPreferenceData(ListPreference listPreference) {
			List<String> deviceList = new ArrayList<String>();
			for (NDevice device : Model.getInstance().getClient().getDeviceManager().getDevices()) {
				if (device.getDeviceType().contains(NDeviceType.FSCANNER)) {
					deviceList.add(device.getId());
				}
			}
			if (deviceList.size() == 0) {
				deviceList.add("None");
			}

			CharSequence[] cs = deviceList.toArray(new CharSequence[deviceList.size()]);
			listPreference.setEntries(cs);
			listPreference.setEntryValues(cs);
			String preferedDevice = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getString(FINGER_CAPTURING_DEVICE, "None");

			if (deviceList.contains(preferedDevice)) {
				listPreference.setValue(preferedDevice);
			} else {
				listPreference.setValueIndex(0);
			}
		}
	}
}
