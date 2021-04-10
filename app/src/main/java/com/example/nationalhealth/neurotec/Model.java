package com.example.nationalhealth.neurotec;

import com.example.nationalhealth.neurotec.preferences.FacePreferences;
import com.example.nationalhealth.neurotec.preferences.FingerPreferences;
import com.example.nationalhealth.neurotec.preferences.IrisPreferences;
import com.example.nationalhealth.neurotec.preferences.VoicePreferences;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.lang.NCore;
import com.neurotec.samples.util.IOUtils;

public final class Model {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static Model sInstance;

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static Model getInstance() {
		synchronized (Model.class) {
			if (sInstance == null) {
				sInstance = new Model();
			}
			return sInstance;
		}
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private NBiometricClient mClient;
	private NSubject mSubject;

	private NSubject[] mSubjects;

	// ===========================================================
	// Private constructor
	// ===========================================================

	private Model() {
		mClient = new NBiometricClient();
		mClient.setDatabaseConnectionToSQLite(IOUtils.combinePath(NCore.getContext().getFilesDir().getAbsolutePath(), "BiometricsV50.db"));
		mClient.setUseDeviceManager(true);
		mClient.setMatchingWithDetails(true);
		mClient.setProperty("Faces.IcaoUnnaturalSkinToneThreshold", 10);
		mClient.setProperty("Faces.IcaoSkinReflectionThreshold", 10);
		mClient.initialize();
		mSubjects = new NSubject[]{};
		mSubject = new NSubject();
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public NBiometricClient getClient() {
		return mClient;
	}

	public NSubject getSubject() {
		return mSubject;
	}

	/**
	 * Subjects contain copy of subject list from biometric client
	 * so that list could be accessible while continuous tasks are being
	 * performed on biometric client like capturing from camera
	 */
	public NSubject[] getSubjects() {
		return mSubjects;
	}

	/**
	 * Subjects contain copy of subject list from biometric client
	 * so that list could be accessible while continuous tasks are being
	 * performed on biometric client like capturing from camera
	 */
	public void setSubjects(NSubject[] subjects) {
		this.mSubjects = subjects;
	}

	public void update() {
		FingerPreferences.updateClient(mClient, NCore.getContext());
		FacePreferences.updateClient(mClient, NCore.getContext());
		VoicePreferences.updateClient(mClient, NCore.getContext());
		IrisPreferences.updateClient(mClient, NCore.getContext());
	}
}
