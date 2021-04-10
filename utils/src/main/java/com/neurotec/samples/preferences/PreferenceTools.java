package com.neurotec.samples.preferences;

import android.content.SharedPreferences;

public final class PreferenceTools {

	// ===========================================================
	// Public static fields
	// ===========================================================

	public static final String SAMPLE_PREFERENCES = "com.neurotec.samples.preferences";

	// ===========================================================
	// Private constructor
	// ===========================================================

	private PreferenceTools() { }

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static double validateDouble(double value, double min, double max) {
		if (value < min) {
			return min;
		} else if (value > max) {
			return max;
		} else {
			return value;
		}
	}

	public static int getInt(SharedPreferences sharedPreferences, String parameter, int defaultValue) {
		return sharedPreferences.getInt(parameter, defaultValue);
	}

	public static short getShort(SharedPreferences sharedPreferences, String parameter, short defaultValue) {
		return (short) sharedPreferences.getInt(parameter, defaultValue);
	}

	public static double getDouble(SharedPreferences sharedPreferences, String parameter, double defaultValue) {
		Integer result = sharedPreferences.getInt(parameter, ((Double)defaultValue).intValue());
		return result.doubleValue();
	}

	public static boolean getBoolean (SharedPreferences sharedPreferences, String parameter, boolean defaultValue) {
		return sharedPreferences.getBoolean(parameter, defaultValue);
	}

	public static boolean getBooleanFromString (SharedPreferences sharedPreferences, String parameter, boolean defaultValue) {
		return Boolean.parseBoolean(sharedPreferences.getString(parameter, String.valueOf(defaultValue)));
	}

	public static int getInt(SharedPreferences sharedPreferences, String parameter, String defaultValue) {
		return Integer.valueOf(sharedPreferences.getString(parameter, defaultValue));
	}

	public static int getIntFromString(SharedPreferences sharedPreferences, String parameter, int defaultValue) {
		return Integer.valueOf(sharedPreferences.getString(parameter, String.valueOf(defaultValue)));
	}

	public static double getDoubleFromFloat(SharedPreferences sharedPreferences, String parameter, double defaultValue) {
		float result = sharedPreferences.getFloat(parameter, ((Double) defaultValue).floatValue());
		return ((Float)result).doubleValue();
	}

	public static double getDoubleFromString(SharedPreferences sharedPreferences, String parameter, double defaultValue) {
		double result;
		try {
			result = Double.parseDouble(sharedPreferences.getString(parameter, String.valueOf(defaultValue)));
		} catch (NumberFormatException e){
			result = defaultValue;
		}
		return result;
	}

	//Loads parameter and performs validation. Returns validated value and writes it to xml.
	public static double getDoubleFromString(SharedPreferences sharedPreferences, String parameter, double defaultValue, double min, double max) {
		double result = getDoubleFromString(sharedPreferences, parameter, defaultValue);
		if (result < min) {
			sharedPreferences.edit().putString(parameter, String.valueOf(min)).commit();
			return result;
		} else if (result > max) {
			sharedPreferences.edit().putString(parameter, String.valueOf(max)).commit();
			return max;
		} else {
			return result;
		}
	}
}
