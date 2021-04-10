package com.neurotec.samples.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaScannerConnection;
import android.util.Log;

public final class ResourceUtils {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = ResourceUtils.class.getSimpleName();
	private static final String ASSET_COPYING_FLAG_SUFFIX = "AssetCopyingDone";

	// ===========================================================
	// Private static methods
	// ===========================================================

	private static void copyAssets(AssetManager manager, String assetDir, String destDirPath, List<String> paths, boolean isRoot) throws IOException {
		String[] filePaths = manager.list(assetDir);
		for (String filePath : filePaths) {
			String src = isRoot ? filePath : IOUtils.combinePath(assetDir, filePath).substring(1);
			File destDir = isRoot ? new File(destDirPath) : new File(destDirPath, assetDir);
			if (!destDir.exists()) {
				if (!destDir.mkdirs()) {
					throw new IOException("Cannot create file: " + destDir.getAbsolutePath());
				}
			}
			String dest = IOUtils.combinePath(destDir.getAbsolutePath(), filePath).substring(1);
			InputStream in = null;
			OutputStream out = null;
			try {
				in = manager.open(src);
				out = new FileOutputStream(dest);
				Log.i(TAG, "Copying asset " + src + " to " + dest);
				IOUtils.copy(in, out);
				paths.add(dest);
			} catch (FileNotFoundException e) {
				// Must be a directory - ignore and continue.
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
			copyAssets(manager, src, destDirPath, paths, false);
		}
	}

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static void copyAssets(Context context, String applicationName, String destDirPath) throws IOException {
		SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
		String key = ResourceUtils.class.getName() + "." + applicationName + ASSET_COPYING_FLAG_SUFFIX;
		boolean done = prefs.getBoolean(key, false);
		if (!done) {
			List<String> paths = new ArrayList<String>();
			ResourceUtils.copyAssets(context.getAssets(), "", destDirPath, paths, true);
			MediaScannerConnection.scanFile(context, paths.toArray(new String[paths.size()]), null, null);
			prefs.edit().putBoolean(key, true).commit();
		}
	}

	public static String getEnum(Context context, Enum<?> value) {
		if (context == null)
			throw new NullPointerException("context");
		if (value == null)
			throw new NullPointerException("value");

		int resId = context.getResources().getIdentifier(String.format("msg_%s", value.toString().toLowerCase()), "string", context.getPackageName());
		return resId == 0 ? null : context.getString(resId);
	}

	// ===========================================================
	// Private constructor
	// ===========================================================

	private ResourceUtils() {
	}

}
