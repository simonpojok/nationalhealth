package com.neurotec.samples.net;

import android.content.Context;
import android.net.ConnectivityManager;

import com.neurotec.samples.R;

public final class ConnectivityHelper {

	// ===========================================================
	// Private static methods
	// ===========================================================

	private static ConnectivityManager getConnectivityManager(final Context context) {
		if (context == null) throw new NullPointerException("context");
		return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
	}

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static boolean isConnected(final Context context) {
		ConnectivityManager cm = getConnectivityManager(context);
		return cm.getActiveNetworkInfo() == null ? false : cm.getActiveNetworkInfo().isConnected();
	}

	public static boolean isWifi(final Context context) {
		ConnectivityManager cm = getConnectivityManager(context);
		return cm.getActiveNetworkInfo() == null ? false : cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	public static String getType(final Context context) {
		ConnectivityManager cm = getConnectivityManager(context);
		return cm.getActiveNetworkInfo() == null ? null : cm.getActiveNetworkInfo().getTypeName();
	}

	public static String getStatus(final Context context) {
		return isConnected(context) ?
			context.getString(R.string.msg_connected) :
			context.getString(R.string.msg_not_connected);
	}


	// ===========================================================
	// Private constructor
	// ===========================================================

	private ConnectivityHelper() {}
}
