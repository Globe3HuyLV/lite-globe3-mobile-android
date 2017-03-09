package com.globe3.tno.g3_lite_mobile.utils;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.images.NImage;
import com.neurotec.licensing.LicensingManager;
import com.neurotec.licensing.NLicense;
import com.neurotec.util.NImageUtils;

import java.io.IOException;

import static com.globe3.tno.g3_lite_mobile.globals.Globals.APP_NAME;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.BIOMETRIC_DATA;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.DETECTION_LICENSE_OBTAINED;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.EXTRACT_LICENSE_OBTAINED;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.MATCHING_FAST_LICENSE_OBTAINED;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.MATCHING_LICENSE_OBTAINED;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.CAMERAS_LICENSE_OBTAINED;


public class BiometricUtility {
    private static final String ADDRESS = "/local";
    private static final int PORT = 5000;

    public static boolean obtainLicense() {
        boolean licenses_obtained;
        try {
            EXTRACT_LICENSE_OBTAINED = NLicense.obtainComponents(ADDRESS, PORT, LicensingManager.LICENSE_FACE_EXTRACTION);
            MATCHING_LICENSE_OBTAINED = NLicense.obtainComponents(ADDRESS, PORT, LicensingManager.LICENSE_FACE_MATCHING);
            DETECTION_LICENSE_OBTAINED = NLicense.obtainComponents(ADDRESS, PORT, LicensingManager.LICENSE_FACE_DETECTION);
            MATCHING_FAST_LICENSE_OBTAINED = NLicense.obtainComponents(ADDRESS, PORT, LicensingManager.LICENSE_FACE_MATCHING_FAST);
            CAMERAS_LICENSE_OBTAINED = NLicense.obtainComponents(ADDRESS, PORT, LicensingManager.LICENSE_DEVICES_CAMERAS);

            licenses_obtained = EXTRACT_LICENSE_OBTAINED && MATCHING_LICENSE_OBTAINED && DETECTION_LICENSE_OBTAINED
                    && MATCHING_FAST_LICENSE_OBTAINED && CAMERAS_LICENSE_OBTAINED;
        } catch (Exception e) {
            e.printStackTrace();
            licenses_obtained = false;
        }

        Log.i(APP_NAME, "EXTRACT_LICENSE_OBTAINED:" + String.valueOf(EXTRACT_LICENSE_OBTAINED));
        Log.i(APP_NAME, "MATCHING_LICENSE_OBTAINED:" + String.valueOf(MATCHING_LICENSE_OBTAINED));
        Log.i(APP_NAME, "DETECTION_LICENSE_OBTAINED:" + String.valueOf(DETECTION_LICENSE_OBTAINED));
        Log.i(APP_NAME, "MATCHING_FAST_LICENSE_OBTAINED:" + String.valueOf(MATCHING_FAST_LICENSE_OBTAINED));
        Log.i(APP_NAME, "CAMERAS_LICENSE_OBTAINED:" + String.valueOf(CAMERAS_LICENSE_OBTAINED));

        return licenses_obtained;
    }

    public static NSubject createSubject(Activity activity, Uri uri) throws IOException {
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();
        finger.setImage(NImageUtils.fromUri(activity, uri));
        subject.getFingers().add(finger);
        subject.setId(uri.getPath());
        return subject;
    }

    public static NSubject createSubject(byte[] fingerData, String subjectId) throws IOException {
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();

        NImage image = NImageUtils.fromJPEG(fingerData);

        finger.setImage(image);
        subject.getFingers().add(finger);
        subject.setId(subjectId);
        return subject;
    }

    public static boolean enrollFinger(byte[] finger_data, String finger_data_id) {
        if (finger_data != null) {
            NSubject candidateSubject = null;
            try {
                candidateSubject = createSubject(finger_data, finger_data_id);
                BIOMETRIC_DATA.enroll(candidateSubject);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static boolean updateFinger(byte[] finger_data, String finger_data_id) {
        if (finger_data != null) {
            NSubject candidateSubject = null;
            try {
                candidateSubject = createSubject(finger_data, finger_data_id);
                BIOMETRIC_DATA.update(candidateSubject).toString();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static boolean enrollFinger(NBiometricClient nBiometricClient, byte[] finger_data, String finger_data_id) {
        if (finger_data != null) {
            NSubject candidateSubject = null;
            try {
                candidateSubject = createSubject(finger_data, finger_data_id);
                nBiometricClient.enroll(candidateSubject);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }

    public static boolean deleteFinger(String fingerId) {
        try {
            BIOMETRIC_DATA.delete(fingerId);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean licenseObtained() {
        return (EXTRACT_LICENSE_OBTAINED && MATCHING_LICENSE_OBTAINED && DETECTION_LICENSE_OBTAINED);
    }
}
