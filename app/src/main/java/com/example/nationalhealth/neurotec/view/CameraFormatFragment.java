package com.example.nationalhealth.neurotec.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.nationalhealth.R;
import com.example.nationalhealth.neurotec.Model;
import com.neurotec.media.NMediaFormat;
import com.neurotec.samples.view.BaseDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CameraFormatFragment extends BaseDialogFragment {

	// ===========================================================
	// Public types
	// ===========================================================

	public interface CameraFormatSelectionListener {
		void onCameraFormatSelected(NMediaFormat format);
	}

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_FORMATS = "formats";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static DialogFragment newInstance() {
		ArrayList<String> records = new ArrayList<String>();
		for (NMediaFormat format : Model.getInstance().getClient().getFaceCaptureDevice().getFormats()) {
			records.add(format.toString());
		}

		CameraFormatFragment fragment = new CameraFormatFragment();
		Bundle bundle = new Bundle();
		bundle.putStringArrayList(EXTRA_FORMATS, records);
		fragment.setArguments(bundle);
		return fragment;
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private ListView mListView;
	private CameraFormatSelectionListener mListener;
	private List<String> mItems;

	// ===========================================================
	// Private constructor
	// ===========================================================

	public CameraFormatFragment() {
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setCancelable(true);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (CameraFormatSelectionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement CameraFormatSelectionListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_list, null);
		mItems = getArguments().getStringArrayList(EXTRA_FORMATS);
		mListView = (ListView) view.findViewById(R.id.list);
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mItems));
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for (NMediaFormat format : Model.getInstance().getClient().getFaceCaptureDevice().getFormats()) {
					if (format.toString().equals(mItems.get(position))) {
						mListener.onCameraFormatSelected(format);
						break;
					}
				}
				dismiss();
			}
		});

		builder.setView(view);
		builder.setTitle(R.string.msg_camera_formats);
		return builder.create();
	}
}