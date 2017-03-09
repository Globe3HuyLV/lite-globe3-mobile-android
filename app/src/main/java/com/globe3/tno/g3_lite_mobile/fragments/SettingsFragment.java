package com.globe3.tno.g3_lite_mobile.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceFragment;

import com.globe3.tno.g3_lite_mobile.controllers.WorkerController;
import com.neurotec.face.verification.NFaceVerificationLivenessMode;

/**
 * Created by huylv on 01/03/2017.
 */

public class SettingsFragment extends PreferenceFragment {

    public static final String KEY_PREF_LIVENESS_TH = "key_pref_liveness_th";
    public static final String KEY_PREF_QUALITY_TH = "key_pref_quality_th";
    public static final String KEY_PREF_MATCHING_TH = "key_pref_matching_th";
    public static final String KEY_PREF_LIVENESS_MODE = "key_pref_liveness_mode";

    public static final int PREF_LIVENESS_TH_DEFAULT_VALUE = 50;
    public static final int PREF_QUALITY_TH_DEFAULT_VALUE = 50;
    public static final int PREF_MATCHING_TH_DEFAULT_VALUE = 48;
    public static final String PREF_LIVENESS_MODE_DEFAULT_VALUE = "NONE";

    private SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(KEY_PREF_LIVENESS_MODE)) {
                String value = sharedPreferences.getString(key, PREF_LIVENESS_MODE_DEFAULT_VALUE);
                NFaceVerificationLivenessMode livenessMode = NFaceVerificationLivenessMode.valueOf(value);
                WorkerController.getInstance(getActivity()).getNFaceVerification(getActivity()).setLivenessMode(livenessMode);
            } else if (key.equals(KEY_PREF_LIVENESS_TH)) {
                byte th = (byte) sharedPreferences.getInt(key, PREF_LIVENESS_TH_DEFAULT_VALUE);
                WorkerController.getInstance(getActivity()).getNFaceVerification(getActivity()).setLivenessThreshold(th);
            } else if (key.equals(KEY_PREF_QUALITY_TH)) {
                byte th = (byte) sharedPreferences.getInt(key, PREF_QUALITY_TH_DEFAULT_VALUE);
                WorkerController.getInstance(getActivity()).getNFaceVerification(getActivity()).setQualityThreshold(th);
            } else if (key.equals(KEY_PREF_MATCHING_TH)) {
                int th = sharedPreferences.getInt(key, PREF_MATCHING_TH_DEFAULT_VALUE);
                WorkerController.getInstance(getActivity()).getNFaceVerification(getActivity()).setMatchingThreshold(th);
            }
        }
    };
}
