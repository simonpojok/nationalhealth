package com.example.nationalhealth.neurotec.multimodal;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.Model;
import com.example.nationalhealth.neurotec.view.EnrollmentDialogFragment;
import com.example.nationalhealth.neurotec.view.SubjectListFragment;
import com.neurotec.biometrics.NBiometric;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NMatchingResult;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.lang.NCore;
import com.neurotec.licensing.gui.ActivationActivity;
import com.neurotec.samples.app.BaseActivity;
import com.neurotec.samples.app.DirectoryViewer;
import com.neurotec.samples.app.InfoActivity;
import com.neurotec.samples.licensing.LicensingManager.LicensingStateCallback;
import com.neurotec.samples.licensing.LicensingState;
import com.neurotec.util.concurrent.CompletionHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public abstract class BiometricActivity extends BaseActivity implements EnrollmentDialogFragment.EnrollmentDialogListener, SubjectListFragment.SubjectSelectionListener, LicensingStateCallback {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final int REQUEST_CODE_GET_FILE = 1;

	private static final String EXTRA_REQUEST_CODE = "request_code";
	private static final int VERIFICATION_REQUEST_CODE = 1;
	private static final int DATABASE_REQUEST_CODE = 2;

	protected static final String RECORD_REQUEST_FACE = "face";
	protected static final String RECORD_REQUEST_FINGER = "finger";
	protected static final String RECORD_REQUEST_IRIS = "iris";
	protected static final String RECORD_REQUEST_VOICE = "voice";

	private static final String TAG = BiometricActivity.class.getSimpleName();

	// ===========================================================
	// Private fields
	// ===========================================================

	private CompletionHandler<NSubject[], ? super NBiometricOperation> subjectListHandler = new CompletionHandler<NSubject[], NBiometricOperation>() {

		@Override
		public void completed(NSubject[] result, NBiometricOperation attachment) {
			Model.getInstance().setSubjects(result);
		}

		@Override
		public void failed(Throwable exc, NBiometricOperation attachment) {
			Log.e(TAG, exc.toString(), exc);
		}

	};

	private CompletionHandler<NBiometricTask, NBiometricOperation> completionHandler = new CompletionHandler<NBiometricTask, NBiometricOperation>() {
		@Override
		public void completed(NBiometricTask task, NBiometricOperation operation) {
			String message = null;
			NBiometricStatus status = task.getStatus();
			Log.i(TAG, String.format("Operation: %s, Status: %s", operation, status));

			onOperationCompleted(operation, task);
			if (status == NBiometricStatus.CANCELED) return;

			if (task.getError() != null) {
				showError(task.getError());
			} else {
				subject = task.getSubjects().get(0);
				switch (operation) {
				case CAPTURE:
				case CREATE_TEMPLATE: {
					if (status == NBiometricStatus.OK) {
						message = getString(R.string.msg_extraction_succeeded);
					} else if (task.getSubjects().size() > 0 && task.getSubjects().get(0).getFaces().size() > 0 && task.getStatus() == NBiometricStatus.TIMEOUT) {
						message = getString(R.string.msg_extraction_failed, getString(R.string.msg_liveness_check_failed));
					} else {
						message = getString(R.string.msg_extraction_failed, status.toString());
					}
				} break;
				case ENROLL:
				case ENROLL_WITH_DUPLICATE_CHECK: {
					if (status == NBiometricStatus.OK) {
						message = getString(R.string.msg_enrollment_succeeded);
					} else {
						message = getString(R.string.msg_enrollment_failed, status.toString());
					}
					client.list(NBiometricOperation.LIST, subjectListHandler);
				} break;
				case VERIFY: {
					if (status == NBiometricStatus.OK) {
						message = getString(R.string.msg_verification_succeeded);
					} else {
						message = getString(R.string.msg_verification_failed, status.toString());
					}
				} break;
				case IDENTIFY: {
					if (status == NBiometricStatus.OK) {
						StringBuilder sb = new StringBuilder();
						NSubject subject = task.getSubjects().get(0);
						for (NMatchingResult result : subject.getMatchingResults()) {
							sb.append(getString(R.string.msg_identification_results, result.getId())).append('\n');
						}
						message = sb.toString();
					} else {
						message = getString(R.string.msg_no_matches);
					}
				} break;
				default: {
					throw new AssertionError("Invalid NBiometricOperation");
				}
				}
				showInfo(message);
			}
		}

		@Override
		public void failed(Throwable th, NBiometricOperation operation) {
			onOperationCompleted(operation, null);
			showError(th);
		}
	};

	private LinearLayout captureControls;
	private LinearLayout stopControls;
	private LinearLayout successControls;

	protected boolean mAppClosing = false;
	protected boolean mAppIsGoingToBackground = false;

	// ===========================================================
	// Protected fields
	// ===========================================================

	protected NBiometricClient client = null;
	protected NSubject subject = null;
	protected final PropertyChangeListener biometricPropertyChanged = new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("Status".equals(evt.getPropertyName())) {
				onStatusChanged(((NBiometric) evt.getSource()).getStatus());
			}
		}
	};

	// ===========================================================
	// Protected abstract methods
	// ===========================================================

	protected abstract Class<?> getPreferences();
	protected abstract void updatePreferences(NBiometricClient client);
	protected abstract boolean isCheckForDuplicates();
	protected abstract List<String> getAdditionalComponents();
	protected abstract List<String> getMandatoryComponents();
	protected abstract String getModalityAssetDirectory();

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NCore.setContext(this);
		try {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			setContentView(R.layout.multimodal_main_biometric);
			captureControls = (LinearLayout) findViewById(R.id.multimodal_capture_controls);
			successControls = (LinearLayout) findViewById(R.id.multimodal_success_controls);
			stopControls = (LinearLayout) findViewById(R.id.multimodal_stop_controls);
			Button mCaptureButton = (Button) findViewById(R.id.multimodal_button_capture);
			mCaptureButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onStartCapturing();
				}
			});
			Button mStopButton = (Button) findViewById(R.id.multimodal_button_stop);
			mStopButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						onStopCapturing();
					} catch (Exception e) {
						showError(e);
					}
				}
			});
			Button mRetryButton = (Button) findViewById(R.id.multimodal_button_retry);
			mRetryButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onBack();
				}
			});
			Button mAddButton = (Button) findViewById(R.id.multimodal_button_add);
			mAddButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					onEnroll();
				}
			});
			Button mDiscardButton = (Button) findViewById(R.id.multimodal_button_discard);
			mDiscardButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					setResult(RESULT_OK);
					finish();
				}
			});
			Button mUnboundButton = (Button) findViewById(R.id.multimodal_button_unbound);
			mUnboundButton.setVisibility(View.INVISIBLE);
			new InitializationTask().execute(savedInstanceState == null);
		} catch (Exception e) {
			showError(e);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_GET_FILE) {
			if (resultCode == RESULT_OK) {
				try {
					onFileSelected(data.getData());
				} catch (Throwable th) {
					showError(th);
				}
			}
		}
	}

	protected void onStartCapturing() { }

	protected void onStopCapturing() {
		cancel();
	}

	protected void onOperationStarted(NBiometricOperation operation) {
		if (operation == NBiometricOperation.CAPTURE) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (isStopSupported()) {
						captureControls.setVisibility(View.GONE);
						stopControls.setVisibility(View.VISIBLE);
						successControls.setVisibility(View.GONE);
					}
				}
			});
		} else {
			if (isActive()) {
				showProgress(R.string.msg_processing);
			}
		}
	}

	protected void onOperationCompleted(final NBiometricOperation operation, final NBiometricTask task) {
		hideProgress();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (task != null && (task.getStatus() == NBiometricStatus.OK
						|| task.getOperations().contains(NBiometricOperation.IDENTIFY)
						|| task.getOperations().contains(NBiometricOperation.VERIFY)
						|| task.getOperations().contains(NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK)
						|| task.getOperations().contains(NBiometricOperation.ENROLL))) {
					captureControls.setVisibility(View.GONE);
					stopControls.setVisibility(View.GONE);
					successControls.setVisibility(View.VISIBLE);
				} else {
					stopControls.setVisibility(View.GONE);
					successControls.setVisibility(View.GONE);
					captureControls.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	protected void onLicensesObtained() { }
	protected void onFileSelected(Uri uri) throws Exception { };

	protected final boolean isActive() {
		return client.getCurrentBiometric() != null || client.getCurrentSubject() != null;
	}

	protected boolean isStopSupported() {
		return true;
	}

	protected void stop() {
		client.force();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mAppIsGoingToBackground = false;
	}

	protected void cancel() {
		if (client != null) {
			client.cancel();
		}
	}

	protected void onLoad(){
		cancel();
		hideProgress();
		Intent intent = new Intent(this, DirectoryViewer.class);
		intent.putExtra(DirectoryViewer.ASSET_DIRECTORY_LOCATION, getModalityAssetDirectory());
		startActivityForResult(intent, REQUEST_CODE_GET_FILE);
	}

	protected void onBack() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				captureControls.setVisibility(View.VISIBLE);
				stopControls.setVisibility(View.GONE);
				successControls.setVisibility(View.GONE);
			}
		});
	}

	protected void onEnroll() {
		new EnrollmentDialogFragment().show(getFragmentManager(), "enrollment");
	}

	protected void onIdentify() {
		if (subject == null) throw new NullPointerException("subject");
		NBiometricTask task = client.createTask(EnumSet.of(NBiometricOperation.IDENTIFY), subject);
		client.performTask(task, NBiometricOperation.IDENTIFY, completionHandler);
		onOperationStarted(NBiometricOperation.IDENTIFY);
	}

	protected void onVerify() {
		Bundle bundle = new Bundle();
		bundle.putInt(EXTRA_REQUEST_CODE, VERIFICATION_REQUEST_CODE);
		SubjectListFragment.newInstance(Model.getInstance().getSubjects(), true, bundle).show(getFragmentManager(), "verification");
	}

	protected void onStatusChanged(final NBiometricStatus status) { }

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		mAppClosing = true;
	}

	@Override
	protected void onStop() {
		mAppIsGoingToBackground = true;
		cancel();
		if (mAppClosing) {
		}
		super.onStop();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_preferences: {
				startActivity(new Intent(this, getPreferences()));
				break;
			}
			case R.id.action_database: {
				Bundle bundle = new Bundle();
				bundle.putInt(EXTRA_REQUEST_CODE, DATABASE_REQUEST_CODE);
				SubjectListFragment.newInstance(Model.getInstance().getSubjects(), false, bundle).show(getFragmentManager(), "database");
				break;
			}
			case R.id.action_activation: {
				Intent activation = new  Intent(this, ActivationActivity.class);
				Bundle params = new Bundle();
				params.putStringArrayList(ActivationActivity.LICENSES, new ArrayList<String>(MultiModalActivity.getAllComponentsInternal()));
				activation.putExtras(params);
				startActivity(activation);
				break;
			}
			case R.id.action_about: {
				startActivity(new Intent(this, InfoActivity.class));
				break;
			}
		}
		return true;
	}

	@Override
	public void onEnrollmentIDProvided(String id) {
		subject.setId(id);
		updatePreferences(client);
		NBiometricOperation operation = isCheckForDuplicates() ? NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK : NBiometricOperation.ENROLL;
		NBiometricTask task = client.createTask(EnumSet.of(operation), subject);
		client.performTask(task, NBiometricOperation.ENROLL, completionHandler);
		onOperationStarted(NBiometricOperation.ENROLL);
	}

	@Override
	public void onSubjectSelected(NSubject otherSubject, Bundle bundle) {
		if (bundle.getInt(EXTRA_REQUEST_CODE) == VERIFICATION_REQUEST_CODE) {
			subject.setId(otherSubject.getId());
			updatePreferences(client);
			NBiometricTask task = client.createTask(EnumSet.of(NBiometricOperation.VERIFY), subject);
			client.performTask(task, NBiometricOperation.VERIFY, completionHandler);
			onOperationStarted(NBiometricOperation.VERIFY);
		}
	}

	@Override
	public void onLicensingStateChanged(LicensingState state) {
		switch (state) {
		case OBTAINING:
			showProgress(R.string.msg_obtaining_licenses);
			break;
		case OBTAINED:
			hideProgress();
			showToast(R.string.msg_licenses_obtained);
			break;
		case NOT_OBTAINED:
			hideProgress();
			showToast(R.string.msg_licenses_not_obtained);
			break;
		}
	}

	public void capture(NSubject subject, EnumSet<NBiometricOperation> additionalOperations) {
		if (subject == null) throw new NullPointerException("subject");
		this.subject = subject;
		updatePreferences(client);
		EnumSet<NBiometricOperation> operations = EnumSet.of(NBiometricOperation.CREATE_TEMPLATE);
		if (additionalOperations != null) {
			operations.addAll(additionalOperations);
		}
		NBiometricTask task = client.createTask(operations, subject);
		client.performTask(task, NBiometricOperation.CREATE_TEMPLATE, completionHandler);
		onOperationStarted(NBiometricOperation.CAPTURE);
	}

	public void extract(NBiometric biometric) {
		if (biometric == null) throw new NullPointerException("biometric");
		subject.clear();
		updatePreferences(client);
		NBiometricTask task = client.createTask(EnumSet.of(NBiometricOperation.CREATE_TEMPLATE), subject);
		task.setBiometric(biometric);
		client.performTask(task, NBiometricOperation.CREATE_TEMPLATE, completionHandler);
//		client.createTemplate(subject, NBiometricOperation.CREATE_TEMPLATE, completionHandler);
		onOperationStarted(NBiometricOperation.CREATE_TEMPLATE);
	}

	public void extract(NSubject subject) {
		if (subject == null) throw new NullPointerException("subject");
		this.subject = subject;
		updatePreferences(client);
		NBiometricTask task = client.createTask(EnumSet.of(NBiometricOperation.CREATE_TEMPLATE), subject);
		client.performTask(task, NBiometricOperation.CREATE_TEMPLATE, completionHandler);
		onOperationStarted(NBiometricOperation.CREATE_TEMPLATE);
	}

	final class InitializationTask extends AsyncTask<Object, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(R.string.msg_initializing);
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			if (params.length < 1) {
				throw new IllegalArgumentException("Missing parameter if to obtain license");
			}
			showProgress(R.string.msg_initializing_client);

			try {
				client = Model.getInstance().getClient();
				subject = Model.getInstance().getSubject();
				mAppClosing = false;
				client.list(NBiometricOperation.LIST, subjectListHandler);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			hideProgress();
			onLicensesObtained();
		}
	}
}
