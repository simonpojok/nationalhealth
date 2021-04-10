package com.example.nationalhealth.neurotec.multimodal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.preferences.IrisPreferences;
import com.neurotec.biometrics.NEPosition;
import com.neurotec.biometrics.NIris;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.view.NIrisView;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceType;
import com.neurotec.devices.NIrisScanner;
import com.neurotec.images.NImage;
import com.neurotec.samples.licensing.LicensingManager;
import com.neurotec.samples.util.NImageUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IrisActivity extends BiometricActivity {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String MODALITY_ASSET_DIRECTORY = "irises";
	private static final String TAG = "IrisActivity";

	// ===========================================================
	// Private fields
	// ===========================================================

	private NIrisView mIrisView;
	private Bitmap mDefaultBitmap;
	private Map<String, NEPosition> mIrisPositions;

	// ===========================================================
	// Private methods
	// ===========================================================

	private void setIrisPosition(NEPosition[] supportedPositions) {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(IrisActivity.this);
		builderSingle.setTitle("Select iris position");
		builderSingle.setCancelable(false);

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(IrisActivity.this, android.R.layout.select_dialog_singlechoice);

		for (NEPosition supportedPosition: supportedPositions) {
			arrayAdapter.add(MultiModalActivity.toLowerCase(supportedPosition.name()));
		}

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
				capture(mIrisPositions.get(element));
			}
		});
		builderSingle.show();

	}

	private NIrisScanner getScanner() {
		for (NDevice device : client.getDeviceManager().getDevices()) {
			if (device.getDeviceType().contains(NDeviceType.IRIS_SCANNER)) {
				return (NIrisScanner) device;
			}
		}
		return null;
	}

	private NSubject createSubjectFromImage(Uri uri) {
		NSubject subject = null;
		try {
			NImage image = NImageUtils.fromUri(this, uri);
			subject = new NSubject();
			NIris iris = new NIris();
			iris.setImage(image);
			subject.getIrises().add(iris);
		} catch (Exception e){
			Log.i(TAG, "Failed to load file as NImage");
		}
		return subject;
	}

	private NSubject createSubjectFromFile(Uri uri) {
		NSubject subject = null;
		try {
			subject = NSubject.fromFile(uri.toString());
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
			PreferenceManager.setDefaultValues(this, R.xml.iris_preferences, false);
			LinearLayout layout = ((LinearLayout) findViewById(R.id.multimodal_biometric_layout));

			mIrisPositions = new HashMap<String, NEPosition>();
			mIrisPositions.put(MultiModalActivity.toLowerCase(NEPosition.UNKNOWN.name()), NEPosition.UNKNOWN);
			mIrisPositions.put(MultiModalActivity.toLowerCase(NEPosition.RIGHT.name()), NEPosition.RIGHT);
			mIrisPositions.put(MultiModalActivity.toLowerCase(NEPosition.LEFT.name()), NEPosition.LEFT);
			mIrisPositions.put(MultiModalActivity.toLowerCase(NEPosition.BOTH.name()), NEPosition.BOTH);

			mIrisView = new NIrisView(this);
			layout.addView(mIrisView);
			mDefaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_eye);
			if (savedInstanceState == null) {
				NIris iris = new NIris();
				iris.setImage(NImage.fromBitmap(mDefaultBitmap));
				mIrisView.setIris(iris);
			}
			Button add = (Button) findViewById(R.id.multimodal_button_add);
			add.setText("Add");
			add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					Bundle b = new Bundle();
					byte[] nETemplate = subject.getTemplate().getIrises().save().toByteArray();
					b.putByteArray(RECORD_REQUEST_IRIS, Arrays.copyOf(nETemplate, nETemplate.length));
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
	protected List<String> getAdditionalComponents() {
		return additionalComponents();
	}

	@Override
	protected List<String> getMandatoryComponents() {
		return mandatoryComponents();
	}

	@Override
	protected Class<?> getPreferences() {
		return IrisPreferences.class;
	}

	@Override
	protected void updatePreferences(NBiometricClient client) {
		IrisPreferences.updateClient(client, this);
	}

	@Override
	protected boolean isCheckForDuplicates() {
		return IrisPreferences.isCheckForDuplicates(this);
	}

	@Override
	protected String getModalityAssetDirectory() {
		return MODALITY_ASSET_DIRECTORY;
	}

	@Override
	protected void onFileSelected(Uri uri) throws Exception {
		NSubject subject = null;

		subject = createSubjectFromImage(uri);

		if (subject == null) {
			subject = createSubjectFromFile(uri);
		}

		if (subject != null) {
			if (!subject.getIrises().isEmpty()) {
				mIrisView.setIris(subject.getIrises().get(0));
			}
			extract(subject);
		} else {
			showInfo("File did not contain valid information for subject");
		}
	}

	@Override
	protected void onStartCapturing() {
		NIrisScanner scanner = getScanner();
		if (scanner == null) {
			showError(R.string.msg_capturing_device_is_unavailable);
		} else {
			client.setIrisScanner(scanner);
			setIrisPosition(scanner.getSupportedPositions());
		}
	}

	protected void capture(NEPosition position) {
		NSubject subject = new NSubject();
		NIris iris = new NIris();
		iris.addPropertyChangeListener(biometricPropertyChanged);
		iris.setPosition(position);
		mIrisView.setIris(iris);
		subject.getIrises().add(iris);
		capture(subject, null);
	}

	public static List<String> mandatoryComponents() {
		return Arrays.asList(LicensingManager.LICENSE_IRIS_DETECTION,
				LicensingManager.LICENSE_IRIS_EXTRACTION,
				LicensingManager.LICENSE_IRIS_MATCHING);
	}

	public static List<String> additionalComponents() {
		return Arrays.asList(LicensingManager.LICENSE_IRIS_MATCHING_FAST,
				LicensingManager.LICENSE_IRIS_STANDARDS);
	}
}
