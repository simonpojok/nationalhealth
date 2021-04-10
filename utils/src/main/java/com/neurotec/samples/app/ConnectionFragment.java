package com.neurotec.samples.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.neurotec.samples.R;
import com.neurotec.samples.net.ConnectivityHelper;
import com.neurotec.samples.widget.CustomItemAdapter;
import com.neurotec.samples.widget.HeaderItem;
import com.neurotec.samples.widget.ListItem;

import java.util.ArrayList;
import java.util.List;

public final class ConnectionFragment extends BaseListFragment {

	// ===========================================================
	// Protected methods
	// ===========================================================

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		view.setBackground(getActivity().getWindow().getDecorView().getBackground());
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<ListItem> items = new ArrayList<ListItem>();
		items.add(new HeaderItem(getString(R.string.msg_internet_connection)));
		items.add(new ListItem(getString(R.string.msg_internet_connection_status), ConnectivityHelper.getStatus(getActivity())));
		if (ConnectivityHelper.isConnected(getActivity())) {
			items.add(new ListItem(getString(R.string.msg_internet_connection_type), ConnectivityHelper.getType(getActivity())));
		}
		setListAdapter(new CustomItemAdapter(getActivity(), items));
	}
}