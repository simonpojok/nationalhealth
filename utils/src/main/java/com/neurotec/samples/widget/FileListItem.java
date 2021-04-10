package com.neurotec.samples.widget;

public class FileListItem extends ListItem {

	// ===========================================================
	// Private fields
	// ===========================================================

	private String mURL;

	// ===========================================================
	// Public constructors
	// ===========================================================

	public FileListItem(String title, String url) {
		super(title);
		mURL = url;
	}

	public FileListItem(String title, String subtitle, String url) {
		super(title, subtitle);
		mURL = url;
	}

	public FileListItem(String title, String subtitle, String url, boolean enabled) {
		super(title, subtitle, enabled);
		mURL = url;
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	public String getUrl() {
		return mURL;
	}

}
