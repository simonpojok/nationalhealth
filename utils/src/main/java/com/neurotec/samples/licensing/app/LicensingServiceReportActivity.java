package com.neurotec.samples.licensing.app;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

import com.neurotec.licensing.NLicensing;
import com.neurotec.samples.R;
import com.neurotec.samples.app.BaseActivity;
import com.neurotec.samples.util.EnvironmentUtils;
import com.neurotec.samples.util.IOUtils;

public final class LicensingServiceReportActivity extends BaseActivity {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = LicensingServiceReportActivity.class.getSimpleName();

	// ===========================================================
	// Private fields
	// ===========================================================

	private TextView mTrial = null;
	private TextView mLog = null;
	private TextView mConf = null;

	// ===========================================================
	// Private fields
	// ===========================================================

	private void updateView() {
		String trial = null;

		try {
			trial = String.valueOf(NLicensing.nativeModuleOf().getTitle().contains("(Trial)"));
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
		}

		if (EnvironmentUtils.isSdPresent()) {
//			mLog.setText(getString(R.string.msg_format_log, LicensingServiceManager.getInstance().getLog()));
//			mConf.setText(getString(R.string.msg_format_configuration, LicensingServiceManager.getInstance().getConf()));
		}

		mTrial.setText(getString(R.string.msg_format_trial, trial));
	}


	private void generateReport() {
		try {
			String fileName = String.format("licensing_service_report_%s.txt", DateFormat.format(EnvironmentUtils.DATE_FORMAT, new Date()));
			String filePath = IOUtils.combinePath(EnvironmentUtils.REPORTS_DIRECTORY_PATH, fileName);
//			LicensingServiceManager.generateReport(this, filePath);
			showInfo(getString(R.string.msg_format_licensing_service_report_was_saved, filePath));
		} catch (Exception e) {
			showError(e);
		}
	}

	// ===========================================================
	// Activity events
	// ===========================================================

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.licensing_service_report);

			mTrial = (TextView) findViewById(R.id.text_view_licensing_service_trial);
			mLog = (TextView) findViewById(R.id.text_view_licensing_service_log);
			mConf = (TextView) findViewById(R.id.text_view_licensing_service_conf);

			Button buttonGenerateReport = (Button) findViewById(R.id.button_generate_report);
			buttonGenerateReport.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					generateReport();
				}
			});
		} catch (Exception e) {
			showError(e);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		try {
			updateView();
		} catch (Exception e) {
			showError(e);
		}
	}
}
