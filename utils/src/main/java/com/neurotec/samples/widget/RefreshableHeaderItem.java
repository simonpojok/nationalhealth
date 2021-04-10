package com.neurotec.samples.widget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.neurotec.samples.R;

public class RefreshableHeaderItem extends ListItem {

	// ===========================================================
	// Private fields
	// ===========================================================

	private OnClickListener listener;

	// ===========================================================
	// ViewHolder declaration
	// ===========================================================

	static class ViewHolder {
		public TextView header;
		public ImageView refresh;
	}

	// ===========================================================
	// Public constructors
	// ===========================================================

	public RefreshableHeaderItem(String title, String subtitle, OnClickListener listener, boolean enabled) {
		super(title, subtitle, enabled);
		this.listener = listener;
	}

	public RefreshableHeaderItem(String title, String subtitle, OnClickListener listener) {
		super(title, subtitle);
		this.listener = listener;
	}

	public RefreshableHeaderItem(String title, OnClickListener listener) {
		super(title);
		this.listener = listener;
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.header = (TextView) rowView.findViewById(R.id.list_header_title);
		viewHolder.refresh = (ImageView) rowView.findViewById(R.id.list_header_title_refresh);
		return viewHolder;
	}

	private void setEnabledView(ViewHolder viewHolder, boolean enabled) {
		viewHolder.header.setEnabled(enabled);
		viewHolder.refresh.setEnabled(enabled);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public View inflate(View convertView, LayoutInflater inflater) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.refreshable_list_header, null);
			ViewHolder viewHolder = createViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder;
		try {
			holder = (ViewHolder) rowView.getTag();
		} catch (ClassCastException e) {
			rowView = inflater.inflate(R.layout.refreshable_list_header, null);
			holder = createViewHolder(rowView);
			rowView.setTag(holder);
		}
		if (this.listener != null) {
			holder.refresh.setOnClickListener((OnClickListener) this.listener);
		}
		holder.header.setText(getTitle());
		setEnabledView(holder, isEnabled());
		return rowView;
	}

}
