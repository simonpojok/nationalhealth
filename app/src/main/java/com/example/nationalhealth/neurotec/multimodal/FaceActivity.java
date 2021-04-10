package com.example.nationalhealth.neurotec.multimodal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.Model;
import com.example.nationalhealth.neurotec.preferences.FacePreferences;
import com.example.nationalhealth.neurotec.view.CameraControlsView;
import com.example.nationalhealth.neurotec.view.CameraFormatFragment;
import com.neurotec.biometrics.NBiometricCaptureOption;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NFace;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.standards.BDIFStandard;
import com.neurotec.biometrics.standards.FCRFaceImage;
import com.neurotec.biometrics.standards.FCRecord;
import com.neurotec.biometrics.view.NFaceView;
import com.neurotec.devices.NCamera;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceType;
import com.neurotec.images.NImage;
import com.neurotec.media.NMediaFormat;
import com.neurotec.samples.licensing.LicensingManager;
import com.neurotec.samples.util.IOUtils;
import com.neurotec.samples.util.NImageUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;


public final class FaceActivity extends BiometricActivity implements CameraFormatFragment.CameraFormatSelectionListener {

	private enum Status {
		CAPTURING,
		OPENING_FILE,
		TEMPLATE_CREATED
	}

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = "FaceActivity";
//	private static final String LICENSING_STATE = "licensing_state";
	private static final String MODALITY_ASSET_DIRECTORY = "faces";

	// ===========================================================
	// Private fields
	// ===========================================================

	private NFaceView mFaceView;
	private CameraControlsView controlsView;

	private boolean mLicensesObtained = false;
	private Status mStatus = Status.CAPTURING;

	// ===========================================================
	// Private methods
	// ===========================================================

	private void startCapturing() {
		NSubject subject = new NSubject();
		NFace face = new NFace();
		face.addPropertyChangeListener(biometricPropertyChanged);
		EnumSet<NBiometricCaptureOption> options = EnumSet.of(NBiometricCaptureOption.MANUAL);
		if (FacePreferences.isShowIcaoWarnings(this) || FacePreferences.isShowIcaoTextWarnings(this)) {
			options.add(NBiometricCaptureOption.STREAM);
			mFaceView.setShowIcaoArrows(FacePreferences.isShowIcaoWarnings(this));
			mFaceView.setShowIcaoTextWarnings(FacePreferences.isShowIcaoTextWarnings(this));
		}
		if (FacePreferences.isUseLiveness(this)) {
			if (!options.contains(NBiometricCaptureOption.STREAM)) {
				options.add(NBiometricCaptureOption.STREAM);
			}
		}
		face.setCaptureOptions(options);
		mFaceView.setFace(face);
		subject.getFaces().add(face);
		capture(subject, (FacePreferences.isShowIcaoWarnings(this) || FacePreferences.isShowIcaoTextWarnings(this)) ? EnumSet.of(NBiometricOperation.ASSESS_QUALITY) : null);
	}


	private NSubject createSubjectFromImage(Uri uri) {
		NSubject subject = null;
		try {
			NImage image = NImageUtils.fromUri(this, uri);
			subject = new NSubject();
			NFace face = new NFace();
			face.setImage(image);
			subject.getFaces().add(face);
		} catch (Exception e){
			Log.i(TAG, "Failed to load file as NImage");
		}
		return subject;
	}

	private NSubject createSubjectFromFCRecord(Uri uri) {
		NSubject subject = null;
		try {
			FCRecord fcRecord = new FCRecord(IOUtils.toByteBuffer(this, uri), BDIFStandard.ISO);
			subject = new NSubject();
			for (FCRFaceImage img : fcRecord.getFaceImages()) {
				NFace face = new NFace();
				face.setImage(img.toNImage());
				subject.getFaces().add(face);
			}
		} catch (Throwable th) {
			Log.i(TAG, "Failed to load file as FCRecord");
		}
		return subject;
	}

	private NSubject createSubjectFromFile(Uri uri) {
		NSubject subject = null;
		try {
			subject = NSubject.fromMemory(IOUtils.toByteBuffer(this, uri));
		} catch (IOException e) {
			Log.i(TAG, "Failed to load from file");
		}
		return subject;
	}

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			PreferenceManager.setDefaultValues(this, R.xml.face_preferences, false);
			LinearLayout layout = (LinearLayout) findViewById(R.id.multimodal_biometric_layout);


			mFaceView = new NFaceView(this);
			mFaceView.setShowAge(true);
			mFaceView.setShowGender(true);
			mFaceView.setShowEyes(true);
			mFaceView.setShowMouth(true);
			mFaceView.setShowNose(true);
			mFaceView.setShowFaceQuality(true);
			layout.addView(mFaceView);

			Button backButton = (Button) findViewById(R.id.multimodal_button_retry);
			backButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startCapturing();
					onBack();
					mStatus = Status.CAPTURING;
				}
			});

			Button add = (Button) findViewById(R.id.multimodal_button_add);
			add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					Bundle b = new Bundle();
					byte[] nLTemplate = subject.getTemplate().getFaces().save().toByteArray();
					b.putByteArray(RECORD_REQUEST_FACE , Arrays.copyOf(nLTemplate, nLTemplate.length));
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		} catch (Exception e) {
			showError(e);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mLicensesObtained && mStatus == Status.CAPTURING) {
			startCapturing();
		}
	}

	//TODO: Licensing state retrieving when unbound from screen orientation
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		mLicensesObtained = savedInstanceState.getBoolean(LICENSING_STATE);
//	}
//
	//TODO: Licensing state saving when unbound from screen orientation
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putBoolean(LICENSING_STATE, mLicensesObtained);
//	}

	@Override
	protected List<String> getAdditionalComponents() {
		return additionalComponents();
	}

	@Override
	protected List<String> getMandatoryComponents() {
		return mandatoryComponents();
	}

	@Override
	protected Class<?> getPreferences() {
		return FacePreferences.class;
	}

	@Override
	protected void updatePreferences(NBiometricClient client) {
		FacePreferences.updateClient(client, this);
	}

	@Override
	protected boolean isCheckForDuplicates() {
		return FacePreferences.isCheckForDuplicates(this);
	}

	@Override
	protected String getModalityAssetDirectory() {
		return MODALITY_ASSET_DIRECTORY;
	}

	@Override
	protected void onLicensesObtained() {
		mLicensesObtained = true;
		startCapturing();
	}

	protected void onStartCapturing() {
		stop();
	}

	@Override
	protected void onFileSelected(Uri uri) throws Exception {
		mStatus = Status.OPENING_FILE;

		NSubject subject = createSubjectFromImage(uri);

		if (subject == null) {
			subject = createSubjectFromFCRecord(uri);
		}

		if (subject == null) {
			subject = createSubjectFromFile(uri);
		}

		if (subject != null) {
			if (!subject.getFaces().isEmpty()) {
				mFaceView.setFace(subject.getFaces().get(0));
			}
			extract(subject);
		} else {
			mStatus = Status.CAPTURING;
			showInfo("File did not contain valid information for subject");
		}
	}

	@Override
	protected void onOperationStarted(NBiometricOperation operation) {
		super.onOperationStarted(operation);
		if (operation == NBiometricOperation.CAPTURE) {
			mStatus = Status.CAPTURING;
		}
	}

	@Override
	protected void onOperationCompleted(NBiometricOperation operation, NBiometricTask task) {
		super.onOperationCompleted(operation, task);
		if (task != null && task.getStatus() == NBiometricStatus.OK && operation == NBiometricOperation.CREATE_TEMPLATE) {
			mStatus = Status.TEMPLATE_CREATED;
		}

		if (task == null || (operation == NBiometricOperation.CREATE_TEMPLATE
				&& task.getStatus() != NBiometricStatus.OK
				&& task.getStatus() != NBiometricStatus.CANCELED
				&& task.getStatus() != NBiometricStatus.OPERATION_NOT_ACTIVATED)) {
			if (!mAppIsGoingToBackground) {
				startCapturing();
			}
		}
	}

	@Override
	protected boolean isStopSupported() {
		return false;
	}

	// ===========================================================
	// 	Public methods
	// ===========================================================

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_FOCUS || keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//TODO call autofocus
			return true;
		}
		return false;
	}

	@Override
	public void onCameraFormatSelected(final NMediaFormat format) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				NCamera camera = Model.getInstance().getClient().getFaceCaptureDevice();
				if (camera != null) {
					camera.setCurrentFormat(format);
				}

			}
		}).start();
	}


	public static List<String> mandatoryComponents() {
		return Arrays.asList(LicensingManager.LICENSE_DEVICES_CAMERAS,
				LicensingManager.LICENSE_FACE_DETECTION,
				LicensingManager.LICENSE_FACE_EXTRACTION,
				LicensingManager.LICENSE_FACE_MATCHING);
	}

	public static List<String> additionalComponents() {
		return Arrays.asList(LicensingManager.LICENSE_FACE_STANDARDS,
				LicensingManager.LICENSE_FACE_MATCHING_FAST,
				LicensingManager.LICENSE_FACE_SEGMENTS_DETECTION);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.face_activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.action_change_camera) {
			NCamera currentCamera = client.getFaceCaptureDevice();
			for (NDevice device : client.getDeviceManager().getDevices()) {
				if (device.getDeviceType().contains(NDeviceType.CAMERA)) {
					if (!device.equals(currentCamera) && currentCamera.isCapturing()) {
						cancel();
						client.setFaceCaptureDevice((NCamera) device);
						startCapturing();
						break;
					}
				}
			}
		}
		return super.onOptionsItemSelected(item);
	}
}
