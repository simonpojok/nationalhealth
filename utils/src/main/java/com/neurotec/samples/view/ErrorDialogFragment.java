package com.neurotec.samples.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.neurotec.samples.R;

public class ErrorDialogFragment extends BaseDialogFragment {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_MESSAGE = "message";
	private static final String EXTRA_TITLE = "title";
	private static final String EXTRA_CLOSE = "close";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static ErrorDialogFragment newInstance(String message, boolean close) {
		return newInstance(null, message, close);
	}

	public static ErrorDialogFragment newInstance(String title, String message, boolean close) {
		ErrorDialogFragment frag = new ErrorDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);
		args.putString(EXTRA_TITLE, title);
		args.putBoolean(EXTRA_CLOSE, close);
		frag.setArguments(args);
		return frag;
	}

	// ===========================================================
	// Private constructor
	// ===========================================================

	public ErrorDialogFragment() {
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = getArguments().getString(EXTRA_MESSAGE);
		String title = getArguments().getString(EXTRA_TITLE);
		final boolean close = getArguments().getBoolean(EXTRA_CLOSE);
		return new AlertDialog.Builder(getActivity())
			.setMessage(message)
			.setTitle(title)
			.setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if (close) {
						getActivity().finish();
					} else {
						dialog.cancel();
					}
				}
		}).create();
	}

}