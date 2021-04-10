package com.neurotec.samples.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.neurotec.samples.R;
import com.neurotec.samples.app.BaseActivity;

public class InfoDialogFragment extends BaseDialogFragment {
	private BaseActivity.IdentificationAction identificationAction;
	private String employeeNumber;
	private Context context;

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_MESSAGE = "message";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static InfoDialogFragment newInstance(String message) {
		InfoDialogFragment frag = new InfoDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);
		frag.setArguments(args);
		return frag;
	}

	public static InfoDialogFragment newInstance(String message, BaseActivity.IdentificationAction identificationAction, String employeeNumber, Context context) {
		InfoDialogFragment frag = new InfoDialogFragment(identificationAction, employeeNumber, context);
		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);
		frag.setArguments(args);

		return frag;
	}

	// ===========================================================
	// Private constructor
	// ===========================================================

	public InfoDialogFragment() {
	}

	@SuppressLint("ValidFragment")
	private InfoDialogFragment(BaseActivity.IdentificationAction identificationAction, String employeeNumber, Context context) {
		this.identificationAction = identificationAction;
		this.employeeNumber = employeeNumber;
		this.context = context;
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = getArguments().getString(EXTRA_MESSAGE);
		return new AlertDialog.Builder(getActivity())
			.setMessage(message)
			.setPositiveButton(R.string.msg_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.cancel();
					if(context != null && context instanceof BaseActivity.OnCloseBaseActivity) {
						BaseActivity.OnCloseBaseActivity activity =  (BaseActivity.OnCloseBaseActivity) context;
						activity.closeBaseActivity(identificationAction, employeeNumber);

					}
				}
		}).create();
	}

}