package com.neurotec.preferences;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.neurotec.util.ToastManager;
import com.neurotec.view.ErrorDialogFragment;
import com.neurotec.view.InfoDialogFragment;

public abstract class BasePreferenceActivity extends PreferenceActivity {

	private ProgressDialog mProgressDialog;

	// ===========================================================
	// Protected methods
	// ===========================================================

	protected void showProgress(int messageId) {
		showProgress(getString(messageId));
	}

	protected void showProgress(String message) {
		hideProgress();
		mProgressDialog = ProgressDialog.show(this, "", message);
	}

	protected void hideProgress() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	protected void showToast(int messageId) {
		ToastManager.show(this, messageId);
	}

	protected void showToast(String message) {
		ToastManager.show(this, message);
	}

	protected void showError(String message, boolean close) {
		ErrorDialogFragment.newInstance(message, close).show(getFragmentManager(), "error");
	}

	protected void showError(int messageId) {
		showError(getString(messageId));
	}

	protected void showError(String message) {
		showError(message, false);
	}

	protected void showInfo(String message) {
		InfoDialogFragment.newInstance(message).show(getFragmentManager(), "info");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
		hideProgress();
	}
}
