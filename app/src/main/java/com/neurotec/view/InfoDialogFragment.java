package com.neurotec.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.globe3.tno.g3_lite_mobile.R;

public class InfoDialogFragment extends BaseDialogFragment {

	// ===========================================================
	// Private static fields
	// ===========================================================

	private static final String EXTRA_MESSAGE = "message";

	// ===========================================================
	// Public static methods
	// ===========================================================

	public static InfoDialogFragment newInstance(String message) {
		InfoDialogFragment frag = new InfoDialogFragment();
		Bundle args = new Bundle();
		args.putString(EXTRA_MESSAGE, message);
		frag.setArguments(args);
		return frag;
	}

	// ===========================================================
	// Private constructor
	// ===========================================================

	public InfoDialogFragment() {
	}

	// ===========================================================
	// Public methods
	// ===========================================================

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String message = getArguments().getString(EXTRA_MESSAGE);
		return new AlertDialog.Builder(getActivity())
			.setMessage(message)
			.setPositiveButton(R.string.neurotec_msg_ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
				}
		}).create();
	}

}