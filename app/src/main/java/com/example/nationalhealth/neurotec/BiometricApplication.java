package com.example.nationalhealth.neurotec;

import android.app.Application;
import android.util.Log;

import com.neurotec.licensing.NLicenseManager;
import com.neurotec.licensing.gui.LicensingPreferencesFragment;
import com.neurotec.samples.util.EnvironmentUtils;

public final class BiometricApplication extends Application {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = BiometricApplication.class.getSimpleName();

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final String APP_NAME = "multibiometric";
	public static final String SAMPLE_DATA_DIR = EnvironmentUtils.getDataDirectoryPath(EnvironmentUtils.SAMPLE_DATA_DIR_NAME, APP_NAME);

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate() {
		super.onCreate();
		try {
			NLicenseManager.setTrialMode(LicensingPreferencesFragment.isUseTrial(this));
			System.setProperty("jna.nounpack", "true");
			System.setProperty("java.io.tmpdir", getCacheDir().getAbsolutePath());
		} catch (Exception e) {
			Log.e(TAG, "Exception", e);
		}
	}
}
