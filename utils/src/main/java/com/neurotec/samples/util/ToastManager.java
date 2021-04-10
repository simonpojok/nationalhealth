package com.neurotec.samples.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public final class ToastManager {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final int DURATION = 4000;
	private static final Map<Object, Long> sLastShown = new HashMap<Object, Long>();

	// ===========================================================
	// private constructor
	// ===========================================================

	private ToastManager() {
	}

	// ===========================================================
	// Private static methods
	// ===========================================================

	private static boolean isRecent(Object obj) {
		Long last = sLastShown.get(obj);
		if (last == null) {
			return false;
		}
		long now = System.currentTimeMillis();
		if (last + DURATION < now) {
			return false;
		}
		return true;
	}

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static synchronized void show(Context context, int resId) {
		if (context == null) throw new NullPointerException("context");
		show(context, context.getString(resId));
	}

	public static synchronized void show(Context context, String msg) {
		if (context == null) throw new NullPointerException("context");
		if (isRecent(msg)) {
			return;
		}
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_VERTICAL, 10, 10);
		toast.show();
		sLastShown.put(msg, System.currentTimeMillis());
	}

}
