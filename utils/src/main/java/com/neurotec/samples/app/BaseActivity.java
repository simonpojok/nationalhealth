package com.neurotec.samples.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.neurotec.samples.util.ExceptionUtils;
import com.neurotec.samples.util.ToastManager;
import com.neurotec.samples.view.ErrorDialogFragment;
import com.neurotec.samples.view.InfoDialogFragment;

public abstract class BaseActivity extends AppCompatActivity {

	// ===========================================================
	// Private fields
	// ===========================================================

	private ProgressDialog mProgressDialog;

	// ===========================================================
	// Protected methods
	// ===========================================================

	protected void showProgress(int messageId) {
		showProgress(getString(messageId));
	}

	protected void showProgress(final String message) {
		hideProgress();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProgressDialog = ProgressDialog.show(BaseActivity.this, "", message);
			}
		});
	}

	protected void hideProgress() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (mProgressDialog != null && mProgressDialog.isShowing()) {
					mProgressDialog.dismiss();
				}
			}
		});
	}

	protected void showToast(int messageId) {
		showToast(getString(messageId));
	}

	protected void showToast(final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastManager.show(BaseActivity.this, message);
			}
		});
	}

	protected void showError(String message, boolean close) {
		ErrorDialogFragment.newInstance(message, close).show(getFragmentManager(), "error");
	}

	protected void showError(int messageId) {
		showError(getString(messageId));
	}

	protected void showError(String message) {
		showError(message, false);
	}

	protected void showError(Throwable th) {
		Log.e(getClass().getSimpleName(), "Exception", th);
		showError(ExceptionUtils.getMessage(th), false);
	}

	protected void showInfo(int messageId) {
		showInfo(getString(messageId));
	}

	protected void showInfo(String message) {
		InfoDialogFragment.newInstance(message).show(getFragmentManager(), "info");
	}

	protected void showInfo(String message, IdentificationAction identificationAction, String employeeNumber, Context context) {
		InfoDialogFragment.newInstance(message, identificationAction, employeeNumber, context).show(getFragmentManager(), "info");
	}

	@Override
	protected void onStop() {
		super.onStop();
		hideProgress();
	}

	public static enum IdentificationAction {
		CLOCK_IN,
		CLOCK_OUT
	}

	public interface OnCloseBaseActivity {
		public void closeBaseActivity(IdentificationAction identificationAction, String employeeNumber);
	}
}
