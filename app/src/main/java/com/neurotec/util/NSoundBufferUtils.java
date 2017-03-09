package com.neurotec.util;

import android.content.Context;
import android.net.Uri;

import com.neurotec.sound.NSoundBuffer;

import java.io.IOException;

public class NSoundBufferUtils {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String ANDROID_ASSET_DESCRIPTOR = "file:///android_asset/";

	// ===========================================================
	// Private constructor
	// ===========================================================

	private NSoundBufferUtils() {
	}

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static NSoundBuffer fromUri(Context context, Uri uri) throws IOException {
		if (context == null) throw new NullPointerException("context");
		if (uri == null) throw new NullPointerException("uri");

		String path;
		if (uri.toString().contains(ANDROID_ASSET_DESCRIPTOR)) {
			path = uri.toString().replace(ANDROID_ASSET_DESCRIPTOR, "");
		} else {
			path = uri.getPath();
		}

		return NSoundBuffer.fromFile(path);
	}
}
