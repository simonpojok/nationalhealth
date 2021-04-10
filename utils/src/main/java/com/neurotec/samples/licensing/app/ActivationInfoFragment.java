package com.neurotec.samples.licensing.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.neurotec.lang.NModule;
import com.neurotec.samples.R;
import com.neurotec.samples.app.BaseListFragment;
import com.neurotec.samples.widget.CustomItemAdapter;
import com.neurotec.samples.widget.HeaderItem;
import com.neurotec.samples.widget.ListItem;

public final class ActivationInfoFragment extends BaseListFragment {

	// ===========================================================
	// Private fields
	// ===========================================================

	View.OnClickListener mDetailsButtonClickListener;

	// ===========================================================
	// Private methods
	// ===========================================================

	private List<String> getComponents(boolean activated) {
		List<String> components = new ArrayList<String>();
		NModule[] modules = NModule.getLoadedModules();

		if (modules != null) {
			for (NModule module : modules) {
				if (module != null) {
					String activatedStr = module.getActivated();
					if (activatedStr != null && !activatedStr.equals("")) {
						for (String activatedPart : activatedStr.split(",")) {
							String[] apParts = activatedPart.split(":");
							if (apParts.length > 1) {
								if (activated && "yes".equals(apParts[apParts.length - 1].trim().toLowerCase())) {
									components.add(apParts[0].trim());
								} else if (!activated && "no".equals(apParts[apParts.length - 1].trim().toLowerCase())) {
									components.add(apParts[0].trim());
								}
							}
						}
					}
				}
			}
		}
		return components;
	}

	// ===========================================================
	// Public methods
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
		mDetailsButtonClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), LicensingServiceReportActivity.class));
			}
		};
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		List<ListItem> items = new ArrayList<ListItem>();

		items.add(new HeaderItem(getString(R.string.msg_activated_components)));
		if (getComponents(true).size() > 0) {
			for (String component : getComponents(true)) {
				items.add(new ListItem(component, ""));
			}
		} else {
			items.add(new ListItem(getString(R.string.msg_no_activated_components)));
		}

		items.add(new HeaderItem(getString(R.string.msg_not_activated_components)));
		if (getComponents(false).size() > 0) {
			for (String component : getComponents(false)) {
				items.add(new ListItem(component, ""));
			}
		} else {
			items.add(new ListItem(getString(R.string.msg_all_components_activated)));
		}
		setListAdapter(new CustomItemAdapter(getActivity(), items));
	}

}