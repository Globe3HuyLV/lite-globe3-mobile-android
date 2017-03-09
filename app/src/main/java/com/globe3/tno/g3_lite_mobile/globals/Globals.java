package com.globe3.tno.g3_lite_mobile.globals;

import android.os.Environment;

import com.neurotec.biometrics.client.NBiometricClient;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class Globals {

    public static final String APP_NAME = "globe3 TMS";

    public static String GLOBE3_SERVER_EXT = "";
    public static String GLOBE3_SERVER_INT = "";

    public static String GLOBE3_WEBSERVICE_PATH = "rest/mobile/";
    public static String GLOBE3_WEBSERVICE_ADDR = GLOBE3_WEBSERVICE_PATH;
    public static final String GLOBE3_DIR_NAME = "globe3TMS";
    public static final String GLOBE3_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + GLOBE3_DIR_NAME + "/";
    public static final String GLOBE3_TIMELOG_DIR = GLOBE3_DIR + "timelog/";
    public static final String GLOBE3_DATA_DIR = GLOBE3_DIR + "data/";
    public static final String GLOBE3_IMAGE_DIR = GLOBE3_DIR + "images/";
    public static final String GLOBE3_DB = GLOBE3_DIR + "db/globe3.db";

    public static int HTTP_READ_TIMEOUT = 900000;
    public static int HTTP_CONNECT_TIMEOUT = 900000;

    public static final int FACE_RECOGNITION_TIMEOUT = 60000;

    public static final int REQUEST_MULTIPLE = 0;
    public static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public static final int REQUEST_GPS = 2;
    public static final int REQUEST_CAMERA = 3;
    public static final int REQUEST_READ_PHONE_STATE = 4;


    public static final int ACTIVITY_RESULT_SELECT_PHOTOS = 1;
    public static final int ACTIVITY_RESULT_TAKE_PHOTO = 2;

    public static final String WEB_SERVICE_PREFIX = "ws_";

    public static final int TIMELOG_SYNC_DOWNLOAD_HOURS = 72;

    public static final int TMS_LOGGING_PRE_INDIVIDUAL = 0;
    public static final int TMS_LOGGING_PRE_MULTIPLE = 1;
    public static final int TMS_LOGGING_POST_SELECT = 2;
    public static final int TMS_LOGGING_AUTO_SELECT = 3;

    public static final String TIME_IN_ACTIVITY = "TIME_IN_ACTIVITY";
    public static final String TIME_OUT_ACTIVITY = "TIME_OUT_ACTIVITY";
    public static String CFSQLFILENAME = "";
    public static String MASTERFN = "";
    public static String COMPANYFN = "";
    public static String COMPANY_NAME = "";
    public static String USERLOGINUNIQ = "";
    public static String USERLOGINID = "";
    public static String PASSWORD = "";

    public static String MAC = "";

    public static String DEVICE_ID = "";
    public static String DEVICE_MODEL = "";
    public static String DEVICE_NAME = "";
    public static String PHONE_NUMBER = "";

    public static final String INTENT_FACE_ACTIVITIES = "INTENT_FACE_ACTIVITIES";

    public static NBiometricClient BIOMETRIC_DATA;

    public static boolean EXTRACT_LICENSE_OBTAINED = false;
    public static boolean MATCHING_LICENSE_OBTAINED = false;
    public static boolean DETECTION_LICENSE_OBTAINED = false;
    public static boolean MATCHING_FAST_LICENSE_OBTAINED = false;
    public static boolean CAMERAS_LICENSE_OBTAINED = false;
    public static boolean ALL_LICENSE_OBTAINED = false;

    public static int CAMERA_TYPE = 1;
    public static int CAPTURE_METHOD = 0;
    public static boolean IS_MAGICAL_CAMERA = true;

    public static final int RESIZE_PHOTO_PIXELS_PERCENTAGE = 8;

    public static boolean REMEMBER_ME_CHECK = false;
    public static String REMEMBER_ME_COMPANY = "";
    public static String REMEMBER_ME_USERID = "";
    public static String REMEMBER_ME_PASSWORD = "";

}
