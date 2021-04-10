package com.neurotec.samples.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.util.Log;

import com.neurotec.samples.util.ExceptionUtils;
import com.neurotec.samples.util.ToastManager;
import com.neurotec.samples.view.ErrorDialogFragment;
import com.neurotec.samples.view.InfoDialogFragment;

public abstract class BaseFragment extends Fragment {

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
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mProgressDialog = ProgressDialog.show(getActivity(), "", message);
			}
		});
	}

	protected void hideProgress() {
		getActivity().runOnUiThread(new Runnable() {
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
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ToastManager.show(getActivity(), message);
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

	protected void showInfo(int messageId) {
		showInfo(getString(messageId));
	}

	protected void showError(Throwable th) {
		Log.e(getClass().getSimpleName(), "Exception", th);
		showError(ExceptionUtils.getMessage(th), false);
	}

	protected void showInfo(String message) {
		InfoDialogFragment.newInstance(message).show(getFragmentManager(), "info");
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onStop() {
		super.onStop();
		hideProgress();
	}
}
