package com.neurotec.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.globe3.tno.g3_lite_mobile.R;

public class ErrorDialogFragment extends BaseDialogFragment {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_MESSAGE = "message";
	private static final String EXTRA_CLOSE = "close";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static ErrorDialogFragment newInstance(String message, boolean close) {
		ErrorDialogFragment frag = new ErrorDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);
		args.putBoolean(EXTRA_CLOSE, close);
		frag.setArguments(args);
		return frag;
	}

	// ===========================================================
	// Private constructor
	// ===========================================================

	public ErrorDialogFragment() {
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = getArguments().getString(EXTRA_MESSAGE);
		final boolean close = getArguments().getBoolean(EXTRA_CLOSE);
		return new AlertDialog.Builder(getActivity())
			.setMessage(message)
			.setPositiveButton(R.string.neurotec_msg_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					if (close) {
						getActivity().finish();
					} else {
						dialog.cancel();
					}
				}
		}).create();
	}

}