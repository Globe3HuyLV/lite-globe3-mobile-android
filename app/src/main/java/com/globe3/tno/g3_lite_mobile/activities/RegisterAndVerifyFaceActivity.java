package com.globe3.tno.g3_lite_mobile.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.fragments.FaceRegistrationFragment;
import com.globe3.tno.g3_lite_mobile.fragments.FaceVerificationFragment;
import com.globe3.tno.g3_lite_mobile.globals.Globals;

import java.util.List;

/**
 * Created by huylv on 27/02/2017.
 */

public class RegisterAndVerifyFaceActivity extends BaseActivity implements View.OnClickListener {

    private Dialog dialogCameraOption;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_and_verify_face);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String fragmentStr = intent.getStringExtra(Globals.INTENT_FACE_ACTIVITIES);
        Fragment fragment;
        if (fragmentStr.equals(FaceRegistrationFragment.class.getSimpleName())) {
            fragment = new FaceRegistrationFragment();
        } else {
            fragment = new FaceVerificationFragment();
        }
        changeFragment(fragment, null);
    }

    @Override
    public void onActivityLoading() {

    }

    @Override
    public void onActivityReady() {

    }

    public void changeFragment(Fragment fragment, String nameAddBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(nameAddBackStack);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList != null && fragmentList.size() > 0) {
            for (Fragment fragment : fragmentList) {
                if (fragment instanceof OnBackPressedListener) {
                    ((OnBackPressedListener) fragment).onBackPressedListener();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioMagicalCamera:
                Globals.IS_MAGICAL_CAMERA = true;
                if (dialogCameraOption != null) {
                    dialogCameraOption.dismiss();
                }
                break;
            case R.id.radioStandardCamera:
                Globals.IS_MAGICAL_CAMERA = false;
                if (dialogCameraOption != null) {
                    dialogCameraOption.dismiss();
                }
                openDialogFaceSetting();
                break;
        }

    }

    public void setDialogCameraOption(Dialog dialogCameraOption) {
        this.dialogCameraOption = dialogCameraOption;
    }

    public interface OnBackPressedListener {
        void onBackPressedListener();
    }

    public Dialog openDialogCameraOption() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_camera_options);
        RadioButton radioMagicalCamera = (RadioButton) dialog.findViewById(R.id.radioMagicalCamera);
        RadioButton radioStandardCamera = (RadioButton) dialog.findViewById(R.id.radioStandardCamera);
        if (Globals.IS_MAGICAL_CAMERA) {
            radioMagicalCamera.setChecked(true);
        } else {
            radioStandardCamera.setChecked(true);
        }
        radioMagicalCamera.setOnClickListener(this);
        radioStandardCamera.setOnClickListener(this);
        dialog.show();
        return dialog;
    }

    public void openDialogFaceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_face_recognition_settings, null);
        final RadioGroup radioCameraType = (RadioGroup) view.findViewById(R.id.radioCameraType);
        final RadioGroup radioFaceRecognition = (RadioGroup) view.findViewById(R.id.radioFaceRecognition);
        RadioButton radioFront = (RadioButton) view.findViewById(R.id.radioFront);
        RadioButton radioBack = (RadioButton) view.findViewById(R.id.radioBack);
        RadioButton radioManual = (RadioButton) view.findViewById(R.id.radioManual);
        RadioButton radioStream = (RadioButton) view.findViewById(R.id.radioStream);
        radioBack.setChecked(Globals.CAMERA_TYPE == 0);
        radioFront.setChecked(Globals.CAMERA_TYPE != 0);
        radioManual.setChecked(Globals.CAPTURE_METHOD == 0);
        radioStream.setChecked(Globals.CAPTURE_METHOD != 0);
        builder.setCancelable(false).setView(view)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectType = radioCameraType.getCheckedRadioButtonId();
                        int selectMethod = radioFaceRecognition.getCheckedRadioButtonId();
                        Globals.CAMERA_TYPE = selectType == R.id.radioFront ? 1 : 0;
                        Globals.CAPTURE_METHOD = (selectMethod == R.id.radioManual ? 0 : 1);
//                        bt_capture.setVisibility(selectMethod == R.id.radioManual ? View.VISIBLE : View.GONE);

                    }
                })
                .setNegativeButton(getString(R.string.label_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    public void showProgressBarActivity(boolean isShow) {
        layout_base_loader.setVisibility(isShow ? View.VISIBLE : View.GONE);
        layout_main.setVisibility(isShow ? View.GONE : View.VISIBLE);
        if (!isShow) {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.animate_fade_in);
            layout_main.setAnimation(animation);
        }
    }

    public void showMessages(final String messages) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(base_activity, messages, Toast.LENGTH_LONG).show();
            }
        });
    }

    public Dialog showMessagesDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.show();
        return alertDialog;
    }
}
