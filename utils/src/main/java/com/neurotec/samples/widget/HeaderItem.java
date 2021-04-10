package com.neurotec.samples.widget;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.neurotec.samples.R;

/*
 * Simple list item extension used for item grouping or item separation.
 */
public class HeaderItem extends ListItem {

	// ===========================================================
	// ViewHolder declaration
	// ===========================================================

	static class ViewHolder {
		public TextView header;
	}

	// ===========================================================
	// Public constructors
	// ===========================================================

	public HeaderItem(String title) {
		super(title, null, false);
	}

	public HeaderItem(String title, boolean enabled) {
		super(title, null, enabled);
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.header = (TextView) rowView.findViewById(R.id.list_header_title);
		return viewHolder;
	}

	private void setEnabledView(ViewHolder viewHolder, boolean enabled) {
		viewHolder.header.setEnabled(enabled);
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public View inflate(View convertView, LayoutInflater inflater) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_header, null);
			ViewHolder viewHolder = createViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder;
		try {
			holder = (ViewHolder) rowView.getTag();
		} catch (ClassCastException e) {
			rowView = inflater.inflate(R.layout.list_header, null);
			holder = createViewHolder(rowView);
			rowView.setTag(holder);
		}
		holder.header.setText(getTitle());
		setEnabledView(holder, isEnabled());
		return rowView;
	}

	public static final Parcelable.Creator<HeaderItem> CREATOR = new Parcelable.Creator<HeaderItem>() {
		public HeaderItem createFromParcel(Parcel in) {
			return new HeaderItem(in);
		}

		public HeaderItem[] newArray(int size) {
			return new HeaderItem[size];
		}
	};

	protected HeaderItem(Parcel in) {
		super(in);
	}
}
