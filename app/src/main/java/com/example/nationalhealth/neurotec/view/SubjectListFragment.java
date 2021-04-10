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
import com.neurotec.biometrics.NSubject;
import com.neurotec.samples.view.BaseDialogFragment;
import java.util.ArrayList;
import java.util.List;

public class SubjectListFragment extends BaseDialogFragment {

	// ===========================================================
	// Public types
	// ===========================================================

	public interface SubjectSelectionListener {
		void onSubjectSelected(NSubject subject, Bundle bundle);
	}

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_ENABLED = "enabled";

	private static NSubject[] mSubjects;

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static DialogFragment newInstance(NSubject[] subjects, boolean enabled, Bundle bundle) {
		mSubjects = subjects;
		bundle.putBoolean(SubjectListFragment.EXTRA_ENABLED, true);
		SubjectListFragment fragment = new SubjectListFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private ListView mSubjectListView;
	private SubjectSelectionListener mListener;
	private List<String> mItems;

	// ===========================================================
	// Private constructor
	// ===========================================================

	public SubjectListFragment() {
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
			mListener = (SubjectSelectionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement SubjectSelectionListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_list, null);
		mItems = new ArrayList<String>();
		for (NSubject subject : mSubjects) {
			mItems.add(subject.getId());
		}
		if (mItems.isEmpty()) {
			mItems.add(getResources().getString(R.string.msg_no_records_in_database));
		}
		mSubjectListView = (ListView) view.findViewById(R.id.list);
		mSubjectListView.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mItems));
		mSubjectListView.setEnabled(getArguments().getBoolean(EXTRA_ENABLED));
		mSubjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for (NSubject subject : mSubjects) {
					if (subject.getId().equals(mItems.get(position))) {
						mListener.onSubjectSelected(subject, getArguments());
						break;
					}
				}
				dismiss();
			}
		});

		builder.setView(view);
		builder.setTitle(R.string.msg_database);
		return builder.create();
	}
}