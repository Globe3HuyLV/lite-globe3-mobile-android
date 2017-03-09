package com.neurotec.app;

import android.app.ListActivity;
import android.app.ProgressDialog;

import com.neurotec.util.ToastManager;
import com.neurotec.view.ErrorDialogFragment;
import com.neurotec.view.InfoDialogFragment;

public abstract class BaseListActivity extends ListActivity {

	// ===========================================================
	// Private fields
	// ===========================================================

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

	protected void showInfo(int messageId) {
		showInfo(getString(messageId));
	}

	protected void showInfo(String message) {
		InfoDialogFragment.newInstance(message).show(getFragmentManager(), "info");
	}

	@Override
	protected void onStop() {
		super.onStop();
		hideProgress();
	}
}
