package com.neurotec.samples.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.neurotec.samples.R;

/*
 * Simple list item extension used for list items that should have
 * check option.
 */
public final class CheckableItem extends ListItem {

	// ===========================================================
	// ViewHolder declaration
	// ===========================================================

	static class ViewHolder {
		public CheckedTextView title;
		public TextView subtitle;
		public CheckableLinearLayout layout;
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private CheckableLinearLayout mLayout = null;
	private boolean mChecked;

	// ===========================================================
	// Public constructors
	// ===========================================================

	public CheckableItem(String title, String subtitle) {
		this(title, subtitle, false);
	}

	public CheckableItem(String title, String subtitle, boolean checked) {
		this(title, subtitle, checked, true);
	}

	public CheckableItem(String title, String subtitle, boolean checked, boolean enabled) {
		super(title, subtitle, enabled);
		this.mChecked = checked;
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder = new ViewHolder();
		mLayout = viewHolder.layout = (CheckableLinearLayout) rowView.findViewById(R.id.item_list_layout);
		viewHolder.title = (CheckedTextView) rowView.findViewById(R.id.list_item_title);
		viewHolder.subtitle = (TextView) rowView.findViewById(R.id.list_item_subtitle);
		return viewHolder;
	}

	private void setEnabledView(ViewHolder viewHolder, boolean enabled) {
		viewHolder.layout.setEnabled(enabled);
		viewHolder.title.setEnabled(enabled);
		viewHolder.subtitle.setEnabled(enabled);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public View inflate(View convertView, LayoutInflater inflater) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.checkable_list_item, null);
			ViewHolder viewHolder = createViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder;
		try {
			holder = (ViewHolder) rowView.getTag();
		} catch (ClassCastException e) {
			rowView = inflater.inflate(R.layout.checkable_list_item, null);
			holder = createViewHolder(rowView);
			rowView.setTag(holder);
		}

		if (getTitle() != null) {
			holder.title.setText(getTitle());
		}

		if (getSubtitle() != null) {
			holder.subtitle.setText(getSubtitle());
		}

		holder.layout.setChecked(mChecked);

		setEnabledView(holder, isEnabled());

		return rowView;
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean checked) {
		mChecked = checked;
		mLayout.setChecked(checked);
	}
}
