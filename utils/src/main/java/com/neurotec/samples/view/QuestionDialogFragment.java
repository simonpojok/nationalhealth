package com.neurotec.samples.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.neurotec.samples.R;

public class QuestionDialogFragment extends BaseDialogFragment {

	// ===========================================================
	// Public types
	// ===========================================================

	public interface QuestionDialogListener {
		void onQuestionAnswered(boolean accepted);
	}

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_MESSAGE = "message";
	private static final String EXTRA_TITLE = "title";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static QuestionDialogFragment newInstance(String message) {
		return newInstance(null, message);
	}

	public static QuestionDialogFragment newInstance(String title, String message) {
		QuestionDialogFragment frag = new QuestionDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);
		if (title != null) {
			args.putString(EXTRA_TITLE, title);
		}
		frag.setArguments(args);
		return frag;
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private QuestionDialogListener mListener;

	// ===========================================================
	// Private constructor
	// ===========================================================

	public QuestionDialogFragment() {
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (QuestionDialogListener) getTargetFragment();
			if (mListener == null) {
				mListener = (QuestionDialogListener) activity;
			}
		} catch (ClassCastException e) {
			throw new ClassCastException("Calling fragment must implement QuestionDialogListener interface or set target fragment");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = getArguments().getString(EXTRA_MESSAGE);
		String title = getArguments().getString(EXTRA_TITLE);
		return new AlertDialog.Builder(getActivity())
			.setMessage(message)
			.setPositiveButton(R.string.msg_yes, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					mListener.onQuestionAnswered(true);
					dialog.cancel();
				}
			})
			.setNegativeButton(R.string.msg_no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					mListener.onQuestionAnswered(false);
					dialog.cancel();
				}
			}).setTitle(title).create();
	}

}