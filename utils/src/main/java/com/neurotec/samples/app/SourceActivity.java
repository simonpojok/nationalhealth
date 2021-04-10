package com.neurotec.samples.app;

import android.os.AsyncTask;
import android.util.Log;

import com.neurotec.samples.R;
import com.neurotec.samples.util.EnvironmentUtils;
import com.neurotec.samples.util.ResourceUtils;

import java.io.IOException;

public abstract class SourceActivity extends BaseListActivity {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = SourceActivity.class.getSimpleName();

	// ===========================================================
	// Private fields
	// ===========================================================

	private BackgroundTask mTask;

	// ===========================================================
	// Protected methods
	// ===========================================================

	protected void loadResources(String source, String destination) {
		mTask = new BackgroundTask();
		mTask.execute(source, destination);
	}

	protected void onResourcesLoaded() {
		hideProgress();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTask != null) {
			mTask.cancel(true);
			mTask = null;
		}
	}

	// ===========================================================
	// Inner class
	// ===========================================================

	final class BackgroundTask extends AsyncTask<String, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(R.string.msg_loading_resources);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {
				if (EnvironmentUtils.isSdPresent()) {
					ResourceUtils.copyAssets(SourceActivity.this, params[0], params[1]);
				} else {
					Log.d(TAG, "Cannot access external storage. Skipping sample data copying from assets.");
				}
			} catch (IOException e) {
				Log.e(TAG, "IOException", e);
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			onResourcesLoaded();
		}


	}
}
