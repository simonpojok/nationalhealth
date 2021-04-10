package com.neurotec.samples.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.neurotec.samples.R;

public final class ExtendedEntryItem extends ListItem {

	// ===========================================================
	// ViewHolder declaration
	// ===========================================================

	static class ViewHolder {
		public TextView title;
		public TextView subtitle;
		public Button button;
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private String mButtonText;
	private View.OnClickListener mOnClickListener;

	// ===========================================================
	// Public constructor
	// ===========================================================

	public ExtendedEntryItem(String title, String subtitle, String buttonText, View.OnClickListener onClickListener) {
		super(title, subtitle);
		mButtonText = buttonText;
		mOnClickListener = onClickListener;
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.title = (TextView) rowView.findViewById(R.id.list_item_entry_title);
		viewHolder.subtitle = (TextView) rowView.findViewById(R.id.list_item_entry_summary);
		viewHolder.button = (Button) rowView.findViewById(R.id.list_item_entry_button);
		return viewHolder;
	}

	private void setEnabledView(ViewHolder viewHolder, boolean enabled) {
//		viewHolder.title.setEnabled(enabled);
//		viewHolder.subtitle.setEnabled(enabled);
		viewHolder.button.setEnabled(enabled);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public View.OnClickListener getOnClickListener() {
		return mOnClickListener;
	}

	public String getButtonText() {
		return mButtonText;
	}

	@Override
	public View inflate(View convertView, LayoutInflater inflater) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_item_extended_entry, null);
			ViewHolder viewHolder = createViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder;
		try {
			holder = (ViewHolder) rowView.getTag();
		} catch (ClassCastException e) {
			rowView = inflater.inflate(R.layout.list_item_extended_entry, null);
			holder = createViewHolder(rowView);
			rowView.setTag(holder);
		}

		if (getTitle() != null) {
			holder.title.setText(getTitle());
		}

		if (getSubtitle() != null) {
			holder.subtitle.setText(getSubtitle());
		}

		if (mButtonText != null) {
			holder.button.setText(mButtonText);
		}

		if (mOnClickListener != null) {
			holder.button.setOnClickListener(mOnClickListener);
		}

		setEnabledView(holder, isEnabled());

		return rowView;
	}
}
