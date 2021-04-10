package com.neurotec.samples.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public final class ManifestData {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = ManifestData.class.getSimpleName();

	// ===========================================================
	// Private static methods
	// ===========================================================

	private static ApplicationInfo getApplicationInfo(Context context) {
		try {
			return context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			Log.w(TAG, "NameNotFoundException", e);
		}
		return null;
	}

	private static PackageInfo getPackageInfo(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			Log.w(TAG, "NameNotFoundException", e);
		}
		return null;
	}

	private static Object readKey(Context context, String keyName) {
		ApplicationInfo ai = getApplicationInfo(context);
		return ai == null || ai.metaData == null ? null : ai.metaData.get(keyName);
	}

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static String getString(Context context, String keyName) {
		return (String) readKey(context, keyName);
	}

	public static int getInt(Context context, String keyName) {
		return (Integer) readKey(context, keyName);
	}

	public static Boolean getBoolean(Context context, String keyName) {
		return (Boolean) readKey(context, keyName);
	}

	public static Object get(Context context, String keyName) {
		return readKey(context, keyName);
	}

	public static String getApplicationName(Context context) {
		ApplicationInfo ai = getApplicationInfo(context);
		return (String) (ai != null ? context.getPackageManager().getApplicationLabel(ai) : "(unknown)");
	}

	public static String getApplicationVersion(Context context) {
		PackageInfo pi = getPackageInfo(context);
		return pi == null ? null : pi.versionName;
	}

	// ===========================================================
	// Private constructor
	// ===========================================================

	private ManifestData() {
	}
}
