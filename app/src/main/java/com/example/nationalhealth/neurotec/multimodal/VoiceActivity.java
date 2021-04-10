package com.example.nationalhealth.neurotec.multimodal;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.preferences.VoicePreferences;
import com.example.nationalhealth.neurotec.view.MicrophoneView;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NVoice;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.view.NVoiceView;
import com.neurotec.samples.licensing.LicensingManager;
import com.neurotec.sound.NSoundBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public final class VoiceActivity extends BiometricActivity {

	// ===========================================================
	// Private static field
	// ===========================================================

	private static final String TAG = VoiceActivity.class.getSimpleName();
	private static final String MODALITY_ASSET_DIRECTORY = "voices";

	// ===========================================================
	// Private field
	// ===========================================================

	private NVoiceView mVoiceView;
	private MicrophoneView mMicrophoneView;

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			PreferenceManager.setDefaultValues(this, R.xml.voice_preferences, false);
			LinearLayout layout = (LinearLayout) findViewById(R.id.multimodal_biometric_layout);
			mMicrophoneView = new MicrophoneView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
			mMicrophoneView.setLayoutParams(params);
//			controlsView.setVisibility(View.GONE);
			layout.addView(mMicrophoneView);
			mVoiceView = new NVoiceView(this);
			layout.addView(mVoiceView);
			Button add = (Button) findViewById(R.id.multimodal_button_add);
			add.setText("Add");
			add.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (subject != null && subject.getTemplate() != null && subject.getTemplate().getVoices() != null) {
						Intent intent = new Intent();
						Bundle b = new Bundle();
						byte[] nSTemplate = subject.getTemplate().getVoices().save().toByteArray();
						b.putByteArray(RECORD_REQUEST_VOICE, Arrays.copyOf(nSTemplate, nSTemplate.length));
						intent.putExtras(b);
						setResult(RESULT_OK, intent);
						finish();
					}
				}
			});
		} catch (Exception e) {
			showError(e);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
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
		return VoicePreferences.class;
	}

	@Override
	protected void updatePreferences(NBiometricClient client) {
		VoicePreferences.updateClient(client, this);
	}

	@Override
	protected boolean isCheckForDuplicates() {
		return VoicePreferences.isCheckForDuplicates(this);
	}

	@Override
	protected String getModalityAssetDirectory() {
		return MODALITY_ASSET_DIRECTORY;
	}

	@Override
	protected void onFileSelected(Uri uri) throws Exception {
		InputStream is = null;
		try {
			NSoundBuffer soundBuffer = NSoundBuffer.fromFile(uri.getPath());
			NSubject subject = new NSubject();
			NVoice voice = new NVoice();
			voice.setSoundBuffer(soundBuffer);
			subject.getVoices().add(voice);
			extract(subject);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					Log.e(TAG, e.toString(), e);
				}
			}
		}
	}

	@Override
	protected void onOperationStarted(final NBiometricOperation operation) {
		super.onOperationStarted(operation);
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (operation == NBiometricOperation.CAPTURE) {
					mMicrophoneView.start();
				}
			}
		});
	}

	@Override
	protected void onOperationCompleted(final NBiometricOperation operation, NBiometricTask task) {
		super.onOperationCompleted(operation, task);
		if (operation == NBiometricOperation.CAPTURE || operation == NBiometricOperation.CREATE_TEMPLATE) {
			stop();
		}
	}

	@Override
	protected void onStartCapturing() {
		showToast(R.string.msg_say_your_phrase);
		NSubject subject = new NSubject();
		NVoice voice = new NVoice();
		mVoiceView.setVoice(voice);
		subject.getVoices().add(voice);
		capture(subject, null);
	}

	@Override
	protected void onStopCapturing() {
		stop();
	}

	@Override
	protected void stop() {
		super.stop();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mMicrophoneView.stop();
			}
		});
	}

	public static List<String> mandatoryComponents() {
		return Arrays.asList(LicensingManager.LICENSE_VOICE_DETECTION,
				LicensingManager.LICENSE_VOICE_EXTRACTION,
				LicensingManager.LICENSE_VOICE_MATCHING);
	}

	public static List<String> additionalComponents() {
		return Arrays.asList(LicensingManager.LICENSE_VOICE_MATCHING_FAST);
	}

}
