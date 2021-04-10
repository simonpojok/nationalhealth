package com.neurotec.samples.widget;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neurotec.samples.R;

/*
 * Core list item class containing title, subtitle and is capable of
 * generating simple view used by CustomItemAdapter. If more specific
 * view is needed this class should be extended and
 * inflate(View convertView, LayoutInflater inflater) overridden customizing
 * returned view.
 */
public class ListItem implements Parcelable {

	// ===========================================================
	// ViewHolder declaration
	// ===========================================================

	static class ViewHolder {
		public TextView title;
		public TextView subtitle;
		public ImageView image;
		public LinearLayout layout;
	}

	// ===========================================================
	// Private fields
	// ===========================================================

	private boolean mEnabled;
	private String mTitle;
	private String mSubtitle;
	private Bitmap mImage;

	// ===========================================================
	// Public constructors
	// ===========================================================

	public ListItem(String title) {
		this(title, null);
	}

	public ListItem(String title, String subtitle) {
		this(title, subtitle, true);
	}

	public ListItem(String title, String subtitle, boolean enabled) {
		this(title, subtitle, null, enabled);
	}

	public ListItem(String title, String subtitle, Bitmap image, boolean enabled) {
		this.mTitle = title;
		this.mSubtitle = subtitle;
		this.mImage = image;
		this.mEnabled = enabled;
	}

	// ===========================================================
	// Private methods
	// ===========================================================

	private void setEnabledView(ViewHolder holder, boolean enabled) {
		holder.layout.setEnabled(enabled);
		holder.title.setEnabled(enabled);
		holder.subtitle.setEnabled(enabled);
		holder.image.setEnabled(enabled);
	}

	private ViewHolder createViewHolder(View rowView) {
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.layout = (LinearLayout) rowView.findViewById(R.id.list_item_layout);
		viewHolder.title = (TextView) rowView.findViewById(R.id.list_item_title);
		viewHolder.subtitle = (TextView) rowView.findViewById(R.id.list_item_subtitle);
		viewHolder.image = (ImageView) rowView.findViewById(R.id.list_item_image);
		return viewHolder;
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	/*
	 * Method generating view for this item. View reusing and ViewHolder
	 * pattern implemented for efficiency.
	 */
	public View inflate(View convertView, LayoutInflater inflater) {
		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_item, null);
			ViewHolder viewHolder = createViewHolder(rowView);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder;
		try {
			holder = (ViewHolder) rowView.getTag();
		} catch (ClassCastException e) {
			rowView = inflater.inflate(R.layout.list_item, null);
			holder = createViewHolder(rowView);
			rowView.setTag(holder);
		}
		if (getTitle() != null) {
			holder.title.setText(getTitle());
		}
		if (getSubtitle() != null) {
			holder.subtitle.setText(getSubtitle());
		}
		if (getImage() != null) {
			holder.image.setImageBitmap(getImage());
			holder.image.setVisibility(View.VISIBLE);
		} else {
			holder.image.setVisibility(View.GONE);
		}
		setEnabledView(holder, isEnabled());
		return rowView;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setSubtitle(String subtitle) {
		this.mSubtitle = subtitle;
	}

	public String getSubtitle() {
		return mSubtitle;
	}

	public Bitmap getImage() {
		return mImage;
	}

	public void setImage(Bitmap image) {
		this.mImage = image;
	}

	public void setEnabled(boolean enabled) {
		this.mEnabled = enabled;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public static final Parcelable.Creator<ListItem> CREATOR = new Parcelable.Creator<ListItem>() {
		public ListItem createFromParcel(Parcel in) {
			return new ListItem(in);
		}

		public ListItem[] newArray(int size) {
			return new ListItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) ((mEnabled) ? 1 : 0));
		dest.writeString(mTitle);
		dest.writeString(mSubtitle);
		dest.writeParcelable(mImage, flags);
	}

	public void readFromParcel(Parcel in) {
		mEnabled = in.readByte() == 1;
		mTitle = in.readString();
		mSubtitle = in.readString();
		mImage = in.readParcelable(null);
	}

	protected ListItem(Parcel in) {
		readFromParcel(in);
	}
}
