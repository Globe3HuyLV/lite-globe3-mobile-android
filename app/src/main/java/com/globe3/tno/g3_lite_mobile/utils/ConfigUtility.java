package com.globe3.tno.g3_lite_mobile.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.Window;

import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.security.G3Encryption;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static com.globe3.tno.g3_lite_mobile.globals.Globals.CFSQLFILENAME;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.GLOBE3_DIR_NAME;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.GLOBE3_SERVER_EXT;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.GLOBE3_SERVER_INT;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.GLOBE3_WEBSERVICE_ADDR;
import static com.globe3.tno.g3_lite_mobile.globals.Globals.MASTERFN;

public class ConfigUtility {

    public static boolean loadConfig(final Activity activity) {
        try {
            File fileConf = new File(Environment.getExternalStorageDirectory(), GLOBE3_DIR_NAME + "/globe3_conf.json");

            if (!fileConf.exists()) {
                promptFailed(activity);
                return true;
            }

            StringBuilder textConf = new StringBuilder();

            BufferedReader br = new BufferedReader(new FileReader(fileConf));
            String line;

            while ((line = br.readLine()) != null) {
                textConf.append(line);
                textConf.append('\n');
            }

            br.close();

            G3Encryption g3encryption = G3Encryption.getDefault(G3Encryption.ENC_KEY, G3Encryption.ENC_SALT, new byte[16]);
            String confDecrypted = g3encryption.decryptOrNull(textConf.toString());

            JSONObject jsonConf = new JSONObject(confDecrypted);

            MASTERFN = jsonConf.getString("masterfn");
            CFSQLFILENAME = jsonConf.getString("datasource");
            GLOBE3_SERVER_EXT = jsonConf.getString("external");
            GLOBE3_SERVER_INT = jsonConf.getString("internal");

            GLOBE3_WEBSERVICE_ADDR = GLOBE3_SERVER_INT + GLOBE3_WEBSERVICE_ADDR;

            return true;
        } catch (Exception e) {
            promptFailed(activity);
            e.printStackTrace();
            return false;
        }
    }

    private static void promptFailed(final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                android.app.AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity)
                        .setCancelable(false)
                        .setMessage(activity.getString(R.string.msg_failed_to_load_configuration))
                        .setPositiveButton(activity.getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                activity.finish();
                            }
                        });

                android.app.AlertDialog configAlert = alertBuilder.create();
                configAlert.requestWindowFeature(Window.FEATURE_NO_TITLE);

                configAlert.show();
            }
        });
    }
}
