package com.example.nationalhealth.neurotec.multimodal;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.Model;
import com.example.nationalhealth.neurotec.preferences.MultimodalPreferences;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NEMatchingDetails;
import com.neurotec.biometrics.NERecord;
import com.neurotec.biometrics.NETemplate;
import com.neurotec.biometrics.NFMatchingDetails;
import com.neurotec.biometrics.NFRecord;
import com.neurotec.biometrics.NFTemplate;
import com.neurotec.biometrics.NLMatchingDetails;
import com.neurotec.biometrics.NLRecord;
import com.neurotec.biometrics.NLTemplate;
import com.neurotec.biometrics.NMatchingResult;
import com.neurotec.biometrics.NSMatchingDetails;
import com.neurotec.biometrics.NSRecord;
import com.neurotec.biometrics.NSTemplate;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.NTemplate;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.lang.NCore;
import com.neurotec.licensing.gui.ActivationActivity;
import com.neurotec.samples.app.BaseActivity;
import com.neurotec.samples.licensing.LicensingManager;
import com.neurotec.util.concurrent.CompletionHandler;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public final class MultiModalActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

	private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
	private static final String WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS = "Do you wish to proceed without granting all permissions?";
	private static final String WARNING_NOT_ALL_GRANTED = "Some permissions are not granted.";
	private static final String MESSAGE_ALL_PERMISSIONS_GRANTED = "All permissions granted";

	private static String TAG = MultiModalActivity.class.getSimpleName();

	private final int MODALITY_CODE_FACE = 1;
	private final int MODALITY_CODE_FINGER = 2;
	private final int MODALITY_CODE_IRIS = 3;
	private final int MODALITY_CODE_VOICE = 4;

	private List<NLRecord> mFaces;
	private List<NFRecord> mFingers;
	private List<NERecord> mIris;
	private List<NSRecord> mVoice;

	private EditText mSubjectId;

	private TextView mFaceCounter;
	private TextView mFingerCounter;
	private TextView mIrisCounter;
	private TextView mVoiceCounter;

	private static List<String> getMandatoryComponentsInternal() {
		List<String> components = new ArrayList<String>();
		for (String component : FaceActivity.mandatoryComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : FingerActivity.mandatoryComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : IrisActivity.mandatoryComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : VoiceActivity.mandatoryComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		return components;
	}

	private static List<String> getAdditionalComponentsInternal() {
		List<String> components = new ArrayList<String>();
		for (String component : FaceActivity.additionalComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : FingerActivity.additionalComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : IrisActivity.additionalComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		for (String component : VoiceActivity.additionalComponents()) {
			if (!components.contains(component)) {
				components.add(component);
			}
		}
		return components;
	}

	private static List<String> getRequiredPermissions() {
		List<String> permissions = new ArrayList<String>();
		permissions.add(Manifest.permission.INTERNET);
		permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
		permissions.add(Manifest.permission.CAMERA);
		permissions.add(Manifest.permission.RECORD_AUDIO);
		permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

		return permissions;
	}

	public static List<String> getAllComponentsInternal() {
		List<String> combinedComponents = getMandatoryComponentsInternal();
		combinedComponents.addAll(getAdditionalComponentsInternal());
		return combinedComponents;
	}

	private void updateRecordCount(List<NLRecord> faces, List<NFRecord> fingers, List<NERecord> iris, List<NSRecord> voice) {
		if (faces != null) {
			mFaceCounter.setText(String.valueOf(faces.size()));
		}
		if (fingers != null) {
			mFingerCounter.setText(String.valueOf(fingers.size()));
		}
		if (iris != null) {
			mIrisCounter.setText(String.valueOf(iris.size()));
		}
		if (voice != null) {
			mVoiceCounter.setText(String.valueOf(voice.size()));
		}
	}

	private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(this)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", okListener)
				.create()
				.show();
	}

	private String[] getNotGrantedPermissions() {
		List<String> neededPermissions = new ArrayList<String>();

		for (String permission : getRequiredPermissions()) {
			if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
				neededPermissions.add(permission);
			}
		}
		return neededPermissions.toArray(new String[neededPermissions.size()]);
	}

	private void requestPermissions(String[] permissions) {
		ActivityCompat.requestPermissions(this, permissions,REQUEST_ID_MULTIPLE_PERMISSIONS);
	}

	private void verify() {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(MultiModalActivity.this);
		builderSingle.setTitle("Database elements (" + Model.getInstance().getClient().listIds().length + ")");

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MultiModalActivity.this, android.R.layout.select_dialog_singlechoice);
		if (Model.getInstance().getClient().listIds().length > 0) {
			arrayAdapter.addAll(Model.getInstance().getClient().listIds());
			builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					String strName = arrayAdapter.getItem(item);
					NSubject subject = createSubjectFromRecords(mFaces, mFingers, mIris, mVoice);
					subject.setId(strName);
					if (subject != null) {
						NBiometricTask task = Model.getInstance().getClient().createTask(EnumSet.of(NBiometricOperation.VERIFY), subject);
						Model.getInstance().getClient().performTask(task, NBiometricOperation.VERIFY, completionHandler);
					} else {
						showError("Empty subject");
					}
				}
			});
		} else {
			arrayAdapter.add("Database is empty");
			builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialogInterface, int i) {

				}
			});
		}

		builderSingle.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.show();
	}

	private void showDatabase() {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(MultiModalActivity.this);
		builderSingle.setTitle("Database elements (" + Model.getInstance().getClient().listIds().length + ")");

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MultiModalActivity.this, android.R.layout.select_dialog_singlechoice);
		if (Model.getInstance().getClient().listIds().length > 0) {
			arrayAdapter.addAll(Model.getInstance().getClient().listIds());

			builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					String strName = arrayAdapter.getItem(item);
					final String element = strName;
					AlertDialog.Builder builderInner = new AlertDialog.Builder(MultiModalActivity.this);

					NSubject subject = new NSubject();
					subject.setId(strName);
					NBiometricStatus status = Model.getInstance().getClient().get(subject);

					StringBuilder sb = new StringBuilder();
					sb.append("Subject ID: ");
					sb.append(strName);
					if (status == NBiometricStatus.OK) {
						sb.append("\n");
						if (subject.getTemplate() != null) {
							if (subject.getTemplate().getFaces() != null) {
								for (NLRecord record :subject.getTemplate().getFaces().getRecords()) {
									sb.append("\tFace record, quality: ");
									sb.append(record.getQuality());
									sb.append("\n");
								}
							}
							if (subject.getTemplate().getFingers() != null) {
								for (NFRecord record :subject.getTemplate().getFingers().getRecords()) {
									sb.append("\tFinger, quality: ");
									sb.append(record.getQuality());
									sb.append(", position: ");
									sb.append(toLowerCase(record.getPosition().name()));
									sb.append("\n");
								}
							}
							if (subject.getTemplate().getIrises() != null) {
								for (NERecord record :subject.getTemplate().getIrises().getRecords()) {
									sb.append("\tIris record, quality: ");
									sb.append(record.getQuality());
									sb.append("\n");
								}
							}
							if (subject.getTemplate().getVoices() != null) {
								for (NSRecord record :subject.getTemplate().getVoices().getRecords()) {
									sb.append("\tVoice record, quality: ");
									sb.append(record.getQuality());
									sb.append("\n");
								}
							}
						}
					}

					builderInner.setMessage(sb.toString());
					builderInner.setTitle("Do you wish to delete item?");
					builderInner.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,int which) {
							Model.getInstance().getClient().delete(element);
						}
					});
					builderInner.setNegativeButton("Close", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							dialogInterface.dismiss();
						}
					});
					builderInner.show();
				}
			});

		} else {
			arrayAdapter.add("Database is empty");
			builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialogInterface, int i) {

				}
			});
		}

		builderSingle.setNegativeButton("Close", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builderSingle.show();
	}

	private NSubject createSubjectFromRecords(List<NLRecord> faces, List<NFRecord>  fingers, List<NERecord> irises, List<NSRecord> voices) {
		if (!faces.isEmpty() || !fingers.isEmpty() || !irises.isEmpty() || !voices.isEmpty()) {
			NSubject subject = new NSubject();
			NTemplate template = new NTemplate();
			NLTemplate faceTemplate = new NLTemplate();
			for (NLRecord record : faces) {
				faceTemplate.getRecords().add(record);
			}
			template.setFaces(faceTemplate);

			NFTemplate fingerTemplate = new NFTemplate();
			for (NFRecord record : fingers) {
				fingerTemplate.getRecords().add(record);
			}
			template.setFingers(fingerTemplate);

			NETemplate irisTemplate = new NETemplate();
			for (NERecord record : irises) {
				irisTemplate.getRecords().add(record);
			}
			template.setIrises(irisTemplate);

			NSTemplate voiceTemplate = new NSTemplate();
			for (NSRecord record : voices) {
				voiceTemplate.getRecords().add(record);
			}
			template.setVoices(voiceTemplate);

			subject.setTemplate(template);
			return subject;
		} else {
			return null;
		}
	}

	private CompletionHandler<NBiometricTask, NBiometricOperation> completionHandler = new CompletionHandler<NBiometricTask, NBiometricOperation>() {
		@Override
		public void completed(NBiometricTask task, NBiometricOperation operation) {
			try {
				String message = null;
				NBiometricStatus status = task.getStatus();
				Log.i(TAG, String.format("Operation: %s, Status: %s", operation, status));

				if (status == NBiometricStatus.CANCELED) return;

				if (task.getError() != null) {
					showError(task.getError());
				} else {
					switch (operation) {
						case ENROLL:
						case ENROLL_WITH_DUPLICATE_CHECK: {
							if (status == NBiometricStatus.OK) {
								message = getString(R.string.msg_enrollment_succeeded);
							} else {
								message = getString(R.string.msg_enrollment_failed, status.toString());
							}
						} break;
						case IDENTIFY: {
							if (status == NBiometricStatus.OK) {
								StringBuilder sb = new StringBuilder();
								NSubject subject = task.getSubjects().get(0);
								for (NMatchingResult result : subject.getMatchingResults()) {
									sb.append("MATCHED WITH: " + getString(R.string.msg_identification_results, result.getId())).append('\n');
									sb.append("\tMatching score: " + result.getScore() + "\n");
									if(result.getMatchingDetails() != null && !result.getMatchingDetails().getFaces().isEmpty()) {
										int index = 0;
										sb.append("\tFaces fused score: " + result.getMatchingDetails().getFacesScore() + "\n");
										for (NLMatchingDetails score : result.getMatchingDetails().getFaces()) {
											sb.append("\t\tNL " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n");
											index++;
										}
									}
									if(result.getMatchingDetails() != null && !result.getMatchingDetails().getFingers().isEmpty()) {
										int index = 0;
										sb.append("\tFingers fused score: " + result.getMatchingDetails().getFingersScore() + "\n");
										for (NFMatchingDetails score : result.getMatchingDetails().getFingers()) {
											sb.append("\t\tNF " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n");
											index++;
										}
									}
									if(result.getMatchingDetails() != null && !result.getMatchingDetails().getIrises().isEmpty()) {
										int index = 0;
										sb.append("\tIrises fused score: " + result.getMatchingDetails().getIrisesScore() + "\n");
										for (NEMatchingDetails score : result.getMatchingDetails().getIrises()) {
											sb.append("\t\tNI " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n");
											index++;
										}
									}
									if(result.getMatchingDetails() != null && !result.getMatchingDetails().getVoices().isEmpty()) {
										int index = 0;
										sb.append("\tVoices fused score: " + result.getMatchingDetails().getVoicesScore() + "\n");
										for (NSMatchingDetails score : result.getMatchingDetails().getVoices()) {
											sb.append("\t\tNS " + index + " with: " + score.getMatchedIndex() + " Score: " + score.getScore() + "\n");
											index++;
										}
									}
									sb.append("\n");
								}
								message = sb.toString();
							} else {
								message = getString(R.string.msg_no_matches);
							}
						} break;
						case VERIFY: {
							if (status == NBiometricStatus.OK) {
								StringBuilder sb = new StringBuilder();
								message = getString(R.string.msg_verification_succeeded);
							} else {
								message = getString(R.string.msg_verification_failed, status.toString());
							}
						} break;
						default: {
							throw new AssertionError("Invalid NBiometricOperation");
						}
					}
				}
				showInfo(message);
			} catch (Exception e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		@Override
		public void failed(Throwable throwable, NBiometricOperation nBiometricOperation) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NCore.setContext(this);
		setContentView(R.layout.multi_modal_main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mFaces = new ArrayList<NLRecord>();
		mFingers = new ArrayList<NFRecord>();
		mIris = new ArrayList<NERecord>();
		mVoice = new ArrayList<NSRecord>();

		mFaceCounter = (TextView) findViewById(R.id.face_counter);
		mFingerCounter = (TextView) findViewById(R.id.finger_counter);
		mIrisCounter = (TextView) findViewById(R.id.iris_counter);
		mVoiceCounter = (TextView) findViewById(R.id.voice_counter);

		mSubjectId = (EditText) findViewById(R.id.subject_id);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		ImageView imageFace = (ImageView) findViewById(R.id.face);
		imageFace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent faceActivity = new Intent(MultiModalActivity.this, FaceActivity.class);
				startActivityForResult(faceActivity, MODALITY_CODE_FACE);
			}
		});

		ImageView imageFinger = (ImageView) findViewById(R.id.finger);
		imageFinger.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent fingerActivity = new Intent(MultiModalActivity.this, FingerActivity.class);
				startActivityForResult(fingerActivity, MODALITY_CODE_FINGER);
			}
		});

		ImageView imageIris = (ImageView) findViewById(R.id.iris);
		imageIris.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent irisActivity = new Intent(MultiModalActivity.this, IrisActivity.class);
				startActivityForResult(irisActivity, MODALITY_CODE_IRIS);
			}
		});

		ImageView imageVoice = (ImageView) findViewById(R.id.voice);
		imageVoice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent voiceActivity = new Intent(MultiModalActivity.this, VoiceActivity.class);
				startActivityForResult(voiceActivity, MODALITY_CODE_VOICE);
			}
		});

		Button enrollSubject = (Button) findViewById(R.id.multimodal_button_enroll);
		enrollSubject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mSubjectId.getText().toString() == "" || mSubjectId.getText().toString().isEmpty()) {
					showError("Missing subject ID");
				} else {
					NSubject subject = createSubjectFromRecords(mFaces, mFingers, mIris, mVoice);
					if (subject != null) {
						subject.setId(mSubjectId.getText().toString());
						NBiometricOperation operation = MultimodalPreferences.isCheckForDuplicates() ? NBiometricOperation.ENROLL_WITH_DUPLICATE_CHECK : NBiometricOperation.ENROLL;
						NBiometricTask task = Model.getInstance().getClient().createTask(EnumSet.of(operation), subject);
						Model.getInstance().getClient().performTask(task, operation, completionHandler);
					} else {
						showError("Empty subject");
					}
				}
			}
		});
		Button identifySubject = (Button) findViewById(R.id.multimodal_button_identify);
		identifySubject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NSubject subject = createSubjectFromRecords(mFaces, mFingers, mIris, mVoice);
				if (subject != null) {
					NBiometricTask task = Model.getInstance().getClient().createTask(EnumSet.of(NBiometricOperation.IDENTIFY), subject);
					Model.getInstance().getClient().performTask(task, NBiometricOperation.IDENTIFY, completionHandler);
				} else {
					showError("Empty subject");
				}

			}
		});
		Button clearSubject = (Button) findViewById(R.id.multimodal_button_new);
		clearSubject.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mFaces.clear();
				mFingers.clear();
				mIris.clear();
				mVoice.clear();
				mSubjectId.setText("");
				updateRecordCount(mFaces, mFingers, mIris, mVoice);
			}
		});
		Button mVerify = (Button) findViewById(R.id.multimodal_button_verify);
		mVerify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				verify();
			}
		});

		updateRecordCount(mFaces, mFingers, mIris, mVoice);

		String[] neededPermissions = getNotGrantedPermissions();
		if(neededPermissions.length == 0) {
			new InitializationTask().execute();
		} else {
			requestPermissions(neededPermissions);
		}
	}

	public static String toLowerCase(String string) {
		StringBuilder sb = new StringBuilder();
		sb.append(string.substring(0,1).toUpperCase());
		sb.append(string.substring(1).toLowerCase());
		return sb.toString().replaceAll("_", " ");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.multimodal_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_clear_db: {
				new AlertDialog.Builder(this)
				.setTitle("Clear database")
				.setMessage("Are you sure you want to clear database?")
				.setPositiveButton("Clear", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Model.getInstance().getClient().clear();
					}
				}).setNegativeButton("Cancel", null).show();
			} break;
			case R.id.action_view_db: {
				showDatabase();
			} break;
			case R.id.action_activation: {
				Intent activation = new  Intent(this, ActivationActivity.class);
				Bundle params = new Bundle();
				params.putStringArrayList(ActivationActivity.LICENSES, new ArrayList<String>(getAllComponentsInternal()));
				activation.putExtras(params);
				startActivity(activation);
			} break;
			case R.id.action_preferences: {
				startActivity(new Intent(this, MultimodalPreferences.class));
			} break;
		}
		return true;
	}

	private boolean ifAllPermissionsGranted(int[] results) {
		boolean finalResult = true;
		for (int permissionResult : results) {
			finalResult &= (permissionResult == PackageManager.PERMISSION_GRANTED);
			if (!finalResult) break;
		}
		return finalResult;
	}

	public void onRequestPermissionsResult(int requestCode, final String permissions[], final int[] grantResults) {
		switch (requestCode) {
			case REQUEST_ID_MULTIPLE_PERMISSIONS: {
				if (grantResults.length > 0) {

					// Check if all permissions granted
					if (!ifAllPermissionsGranted(grantResults)) {
						showDialogOK(WARNING_PROCEED_WITH_NOT_GRANTED_PERMISSIONS,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										switch (which) {
											case DialogInterface.BUTTON_POSITIVE:
												Log.w(TAG, WARNING_NOT_ALL_GRANTED);
												for(int i = 0; i < permissions.length;i++) {
													if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
														Log.w(TAG, permissions[i] + ": PERMISSION_DENIED");
													}
												}
												new InitializationTask().execute();
												break;
											case DialogInterface.BUTTON_NEGATIVE:
												requestPermissions(permissions);
												break;
											default:
												throw new AssertionError("Unrecognised permission dialog parameter value");
										}
									}
								});
					} else {
						Log.i(TAG, MESSAGE_ALL_PERMISSIONS_GRANTED);
						new InitializationTask().execute();
					}
				}
			}
		}
	}

	final class InitializationTask extends AsyncTask<Object, Boolean, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showProgress(R.string.msg_initializing);
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			showProgress(R.string.msg_obtaining_licenses);
			try {
				LicensingManager.getInstance().obtain(MultiModalActivity.this, getAdditionalComponentsInternal());
				if (LicensingManager.getInstance().obtain(MultiModalActivity.this, getMandatoryComponentsInternal())) {
					showToast(R.string.msg_licenses_obtained);
				} else {
					showToast(R.string.msg_licenses_partially_obtained);
				}
			} catch (Exception e) {
				showError(e.getMessage(), false);
			}
			showProgress(R.string.msg_initializing_client);

			try {
				NBiometricClient client = Model.getInstance().getClient();
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
		}
	}
}
