package com.globe3.tno.g3_lite_mobile.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.controllers.ProjectController;
import com.globe3.tno.g3_lite_mobile.controllers.WorkerController;
import com.globe3.tno.g3_lite_mobile.globals.Globals;
import com.globe3.tno.g3_lite_mobile.security.G3Encryption;
import com.globe3.tno.g3_lite_mobile.utils.BiometricUtility;
import com.globe3.tno.g3_lite_mobile.utils.FileUtility;
import com.neurotec.lang.NCore;
import com.neurotec.plugins.NDataFileManager;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText et_company;
    private EditText et_userid;
    private EditText et_password;
    private Button btn_login;
    private LinearLayout ll_login_remember;
    private CheckBox cb_login_remember;
    private LinearLayout ll_login_loader;
    private final String[] PERMISSIONS = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.CAMERA, Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO, Manifest.permission.WAKE_LOCK, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_SETTINGS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE};

    private static final String KEY_REMEMBER_ME = "REMEMBER_ME";
    private static final String KEY_USERNAME = "USERNAME";
    private static final String KEY_COMPANY = "COMPANY";
    private static final String KEY_PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        et_company = (EditText) findViewById(R.id.et_company);
        et_userid = (EditText) findViewById(R.id.et_userid);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        ll_login_remember = (LinearLayout) findViewById(R.id.ll_login_remember);
        cb_login_remember = (CheckBox) findViewById(R.id.cb_login_remember);
        ll_login_loader = (LinearLayout) findViewById(R.id.ll_login_loader);
        btn_login.setOnClickListener(this);
        ll_login_remember.setOnClickListener(this);
        et_company.requestFocus();
    }

    @Override
    public void onActivityLoading() {
        NCore.setContext(this);
        if (!hasPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, Globals.REQUEST_MULTIPLE);
        } else {
            new LoadTask().execute();
        }
        Log.d(TAG, "onActivityLoading()");
    }

    private boolean hasPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Globals.REQUEST_MULTIPLE:
                boolean permission_granted = true;
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permission_granted = false;
                        break;
                    }
                }
                if (grantResults.length != PERMISSIONS.length || permission_granted) {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false)
                            .setMessage(getString(R.string.msg_failed_to_grant_app_permission))
                            .setPositiveButton(getString(R.string.label_exit), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton(getString(R.string.label_retry), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(LoginActivity.this, PERMISSIONS, Globals.REQUEST_MULTIPLE);
                                }
                            }).setIcon(android.R.drawable.ic_dialog_alert);
                    AlertDialog dialog = builder.create();
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.show();
                } else {
                    new LoadTask().execute();
                }
        }
        Log.d(TAG, "onRequestPermissionsResult()");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        ProjectController.getInstance(this).closeDB();
        WorkerController.getInstance(this).closeDB();
        super.onDestroy();
    }

    @Override
    public void onActivityReady() {
        Log.d(TAG, "onActivityReady()");
        if (Globals.REMEMBER_ME_CHECK) {
            et_company.setText(Globals.REMEMBER_ME_COMPANY);
            et_userid.setText(Globals.REMEMBER_ME_USERID);
            et_password.setText(Globals.REMEMBER_ME_PASSWORD);
            cb_login_remember.setChecked(Globals.REMEMBER_ME_CHECK);
            enableLogin(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Globals.REMEMBER_ME_CHECK) {
            restoreRememberLogin();
        } else {
            enableLogin(true);
        }
    }

    public void enableLogin(boolean enable) {
        et_company.setEnabled(enable);
        et_userid.setEnabled(enable);
        et_password.setEnabled(enable);
        btn_login.setEnabled(enable);
        ll_login_remember.setEnabled(enable);
        cb_login_remember.setEnabled(enable);
        btn_login.setVisibility(enable ? View.VISIBLE : View.GONE);
        ll_login_loader.setVisibility(enable ? View.GONE : View.VISIBLE);
    }


    private void restoreRememberLogin() {
        enableLogin(false);
        SharedPreferences sharedPreferences = getSharedPreferences(KEY_REMEMBER_ME, MODE_PRIVATE);
        if (sharedPreferences.contains(KEY_COMPANY)) {
            Globals.REMEMBER_ME_CHECK = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false);
            Globals.REMEMBER_ME_COMPANY = sharedPreferences.getString(KEY_COMPANY, "");
            Globals.REMEMBER_ME_USERID = sharedPreferences.getString(KEY_USERNAME, "");
            G3Encryption g3encryption = G3Encryption.getDefault(G3Encryption.ENC_KEY, G3Encryption.ENC_SALT, new byte[16]);
            try {
                Globals.REMEMBER_ME_PASSWORD = g3encryption.decrypt(sharedPreferences.getString(KEY_PASSWORD, ""));
                et_company.setText(Globals.REMEMBER_ME_COMPANY);
                et_userid.setText(Globals.REMEMBER_ME_USERID);
                et_password.setText(Globals.REMEMBER_ME_PASSWORD);
                cb_login_remember.setChecked(Globals.REMEMBER_ME_CHECK);
                enableLogin(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            et_company.setText(Globals.REMEMBER_ME_COMPANY);
            et_userid.setText(Globals.REMEMBER_ME_USERID);
            et_password.setText(Globals.REMEMBER_ME_PASSWORD);
            cb_login_remember.setChecked(Globals.REMEMBER_ME_CHECK);
            et_company.requestFocus();
            enableLogin(true);
        }
    }

    private void rememberPassword(final boolean isChecked) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(KEY_REMEMBER_ME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String company = et_company.getText().toString().trim();
                String username = et_userid.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (isChecked && (!company.equals(Globals.REMEMBER_ME_COMPANY) ||
                        !username.equals(Globals.REMEMBER_ME_USERID) ||
                        !password.equals(Globals.REMEMBER_ME_PASSWORD))) {
                    editor.clear();
                    editor.putBoolean(KEY_REMEMBER_ME, true);
                    editor.putString(KEY_COMPANY, company);
                    editor.putString(KEY_USERNAME, username);
                    G3Encryption g3encryption = G3Encryption.getDefault(G3Encryption.ENC_KEY, G3Encryption.ENC_SALT, new byte[16]);
                    try {
                        String encrypt_password = g3encryption.encrypt(password);
                        editor.putString(KEY_PASSWORD, encrypt_password);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    saveRememberLogin(true, company, username, password);
                } else if (!isChecked) {
                    editor.clear();
                    saveRememberLogin(false, "", "", "");
                }
                editor.apply();
            }
        }).run();
    }

    private void saveRememberLogin(boolean isChecked, String company, String username, String password) {
        Globals.REMEMBER_ME_CHECK = isChecked;
        Globals.REMEMBER_ME_COMPANY = company;
        Globals.REMEMBER_ME_USERID = username;
        Globals.REMEMBER_ME_PASSWORD = password;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String company = et_company.getText().toString().trim();
                String userid = et_userid.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(company) || TextUtils.isEmpty(userid) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, "Let fill full all fields to login!", Toast.LENGTH_SHORT).show();
                } else {
                    rememberPassword(cb_login_remember.isChecked());
                    enableLogin(false);
                    startActivity(new Intent(this, DashBoardActivity.class));
                }
                break;
            case R.id.ll_login_remember:
                cb_login_remember.setChecked(!cb_login_remember.isChecked());
                String toast;
                if (cb_login_remember.isChecked()) {
                    toast = "Remember logging in information!";
                } else {
                    toast = "Remember nothing!";
                }
                Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            layout_base_loader.setVisibility(View.VISIBLE);
            layout_main.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Globals.ALL_LICENSE_OBTAINED = BiometricUtility.obtainLicense();
            NDataFileManager.getInstance().addFromDirectory("data", false);
            FileUtility.createAppFolders(LoginActivity.this);
            ProjectController.getInstance(LoginActivity.this).openDB();
            WorkerController.getInstance(LoginActivity.this).openDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            layout_base_loader.setVisibility(View.GONE);
            layout_main.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.animate_fade_in);
            layout_main.setAnimation(animation);
            super.onPostExecute(aVoid);
        }
    }
}
