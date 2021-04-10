package com.neurotec.samples.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/*
 * Adapter extension that lets it's mItems to define how they should be
 * represented. Useful for lists with custom mItems or various mItems in a
 * single list.
 */
public final class CustomItemAdapter extends ArrayAdapter<ListItem> {

	// ===========================================================
	// Private fields
	// ===========================================================

	private List<ListItem> mItems;
	private LayoutInflater mLayoutInflater;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public CustomItemAdapter(Context context, List<ListItem> items) {
		super(context, 0, items);
		this.mItems = items;
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mItems.get(position).inflate(convertView, mLayoutInflater);
	}

	@Override
	public boolean isEnabled(int position) {
		return mItems.get(position).isEnabled();
	}
}