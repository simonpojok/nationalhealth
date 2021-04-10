package com.example.nationalhealth.neurotec.multimodal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.preferences.FingerPreferences;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NFPosition;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.view.NFingerView;
import com.neurotec.biometrics.view.NFingerViewBase.ShownImage;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceType;
import com.neurotec.devices.NFScanner;
import com.neurotec.images.NImage;
import com.neurotec.samples.licensing.LicensingManager;
import com.neurotec.samples.util.IOUtils;
import com.neurotec.samples.util.NImageUtils;
import com.neurotec.samples.util.ResourceUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class FingerActivity extends BiometricActivity {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String TAG = FingerActivity.class.getSimpleName();
	private static final String BUNDLE_KEY_STATUS = "status";
	private static final String MODALITY_ASSET_DIRECTORY = "fingers";

	// ===========================================================
	// Private fields
	// ===========================================================

	private NFingerView mFingerView;
	private Bitmap mDefaultBitmap;
	private TextView mStatus;
	private Map<String, NFPosition> mFingerPositions;

	// ===========================================================
	// Private methods
	// ===========================================================

	private void setFingerPosition() {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(FingerActivity.this);
		builderSingle.setTitle("Select finger possition");
		builderSingle.setCancelable(false);

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FingerActivity.this, android.R.layout.select_dialog_singlechoice);

		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.UNKNOWN.name()));

		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.LEFT_LITTLE_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.LEFT_RING_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.LEFT_MIDDLE_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.LEFT_INDEX_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.LEFT_THUMB.name()));

		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.RIGHT_LITTLE_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.RIGHT_RING_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.RIGHT_MIDDLE_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.RIGHT_INDEX_FINGER.name()));
		arrayAdapter.add(MultiModalActivity.toLowerCase(NFPosition.RIGHT_THUMB.name()));

		builderSingle.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				String strName = arrayAdapter.getItem(item);
				final String element = strName;
				subject.getTemplate().getFingers().getRecords().get(0).setPosition(mFingerPositions.get(element));
			}
		});
		builderSingle.show();
	}

	private NFScanner getScanner() {
		//TODO Read last used scanner from preferences
//		for (NPlugin plugin : NDeviceManager.getPluginManager().getPlugins()) {
//			Log.i("Model", String.format("Plugin name => %s, Error => %s", plugin.getModule().getName(), plugin.getError()));
		NDevice fingerDevice = null;
		for (NDevice device : client.getDeviceManager().getDevices()) {
//			Log.i("Device", String.format("Device name => %s", device.getDisplayName()));
			if (device.getDeviceType().contains(NDeviceType.FSCANNER)) {
				if (device.getId().equals(PreferenceManager.getDefaultSharedPreferences(this).getString(FingerPreferences.FINGER_CAPTURING_DEVICE, "None"))) {
					return (NFScanner) device;
				} else if (fingerDevice == null){
					fingerDevice = device;
				}
			}
		}
		return (NFScanner) fingerDevice;
	}

	//TODO: Try to load as image
	private NSubject createSubjectFromImage(Uri uri) {
		NSubject subject = null;
		try {
			NImage image = NImageUtils.fromUri(this, uri);
			subject = new NSubject();
			NFinger finger = new NFinger();
			finger.setImage(image);
			subject.getFingers().add(finger);
		} catch (Exception e){
			Log.i(TAG, "Failed to load file as NImage");
		}
		return subject;
	}

	private NSubject createSubjectFromFile(Uri uri) {
		NSubject subject = null;
		try {
			subject = NSubject.fromMemory(IOUtils.toByteBuffer(this, uri));
		} catch (Exception e) {
			Log.i(TAG, "Failed to load finger from file");
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
			PreferenceManager.setDefaultValues(this, R.xml.finger_preferences, false);
			LinearLayout layout = ((LinearLayout) findViewById(R.id.multimodal_biometric_layout));

			mFingerPositions = new HashMap<String, NFPosition>();
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.UNKNOWN.name()), NFPosition.UNKNOWN);

			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.LEFT_LITTLE_FINGER.name()), NFPosition.LEFT_LITTLE_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.LEFT_RING_FINGER.name()), NFPosition.LEFT_RING_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.LEFT_MIDDLE_FINGER.name()), NFPosition.LEFT_MIDDLE_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.LEFT_INDEX_FINGER.name()), NFPosition.LEFT_INDEX_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.LEFT_THUMB.name()), NFPosition.LEFT_THUMB);

			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.RIGHT_LITTLE_FINGER.name()), NFPosition.RIGHT_LITTLE_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.RIGHT_RING_FINGER.name()), NFPosition.RIGHT_RING_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.RIGHT_MIDDLE_FINGER.name()), NFPosition.RIGHT_MIDDLE_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.RIGHT_INDEX_FINGER.name()), NFPosition.RIGHT_INDEX_FINGER);
			mFingerPositions.put(MultiModalActivity.toLowerCase(NFPosition.RIGHT_THUMB.name()), NFPosition.RIGHT_THUMB);

			mFingerView = new NFingerView(this);
			layout.addView(mFingerView);

			mStatus = new TextView(this);
			mStatus.setText("Status");
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			mStatus.setLayoutParams(params);
			layout.addView(mStatus);

			mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_finger);
			if (savedInstanceState == null) {
				NFinger finger = new NFinger();
				finger.setImage(NImage.fromBitmap(mDefaultBitmap));
				mFingerView.setFinger(finger);
			}
			Button add = (Button) findViewById(R.id.multimodal_button_add);
			add.setText("Add");
			add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					Bundle b = new Bundle();
					byte[] nFTemplate = subject.getTemplate().getFingers().save().toByteArray();
					b.putByteArray(RECORD_REQUEST_FINGER, Arrays.copyOf(nFTemplate, nFTemplate.length));
					intent.putExtras(b);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
			Button setPosition = (Button) findViewById(R.id.multimodal_button_unbound);
			setPosition.setText("Set position");
			setPosition.setVisibility(View.VISIBLE);
			setPosition.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					setFingerPosition();
				}
			});

		} catch (Exception e) {
			showError(e);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(BUNDLE_KEY_STATUS, TextUtils.isEmpty(mStatus.getText()) ? "" : mStatus.getText().toString());
	}

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
		return FingerPreferences.class;
	}

	@Override
	protected void updatePreferences(NBiometricClient client) {
		FingerPreferences.updateClient(client, this);
	}

	@Override
	protected boolean isCheckForDuplicates() {
		return FingerPreferences.isCheckForDuplicates(this);
	}

	@Override
	protected String getModalityAssetDirectory() {
		return MODALITY_ASSET_DIRECTORY;
	}

	@Override
	protected void onFileSelected(Uri uri) throws Exception {
		NSubject subject = null;
		mFingerView.setShownImage(FingerPreferences.isReturnBinarizedImage(this) ? ShownImage.RESULT : ShownImage.ORIGINAL);
		subject = createSubjectFromImage(uri);

		if (subject == null) {
			subject = createSubjectFromFile(uri);
		}

		if (subject != null) {
			if (subject.getFingers() != null && subject.getFingers().get(0) != null) {
				mFingerView.setFinger(subject.getFingers().get(0));
			}
			extract(subject);
		} else {
			showInfo(R.string.msg_failed_to_load_image_or_standard);
		}
	}

	@Override
	protected void onStartCapturing() {
		NFScanner scanner = getScanner();
		if (scanner == null) {
			showError(R.string.msg_capturing_device_is_unavailable);
		} else {
			client.setFingerScanner(scanner);
			NSubject subject = new NSubject();
			NFinger finger = new NFinger();
			finger.addPropertyChangeListener(biometricPropertyChanged);
			mFingerView.setShownImage(FingerPreferences.isReturnBinarizedImage(this) ? ShownImage.RESULT : ShownImage.ORIGINAL);
			mFingerView.setFinger(finger);
			subject.getFingers().add(finger);
			capture(subject, null);
		}
	}

	@Override
	protected void onStatusChanged(final NBiometricStatus value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mStatus.setText(value == null ? "" : ResourceUtils.getEnum(FingerActivity.this, value));
			}
		});
	}

	public static List<String> mandatoryComponents() {
		return Arrays.asList(LicensingManager.LICENSE_FINGER_DETECTION,
				LicensingManager.LICENSE_FINGER_EXTRACTION,
				LicensingManager.LICENSE_FINGER_MATCHING,
				LicensingManager.LICENSE_FINGER_DEVICES_SCANNERS);
	}

	public static List<String> additionalComponents() {
		return Arrays.asList(LicensingManager.LICENSE_FINGER_WSQ,
				LicensingManager.LICENSE_FINGER_STANDARDS_FINGER_TEMPLATES,
				LicensingManager.LICENSE_FINGER_STANDARDS_FINGERS);
//			LicensingManager.LICENSE_FINGER_QUALITY_ASSESSMENT,
//			LicensingManager.LICENSE_FINGER_SEGMENTS_DETECTION);
	}
}
