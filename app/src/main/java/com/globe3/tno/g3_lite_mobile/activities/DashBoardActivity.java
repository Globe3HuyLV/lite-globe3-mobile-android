package com.globe3.tno.g3_lite_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.fragments.FaceRegistrationFragment;
import com.globe3.tno.g3_lite_mobile.fragments.FaceVerificationFragment;
import com.globe3.tno.g3_lite_mobile.globals.Globals;

/**
 * Created by HuyLV on 10-Feb-17.
 */
public class DashBoardActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout ll_connection_status;
    private LinearLayout ll_sync_down_incremental;
    private LinearLayout ll_request_gps;
    private LinearLayout ll_pending_sync;
    private LinearLayout ll_time_in;
    private LinearLayout ll_time_out;
    private RelativeLayout rl_close_menu;

    private TextView tv_server_connect;
    private TextView tv_last_sync;
    private TextView tv_gps_connect;
    private TextView tv_pending_sync;

    private FloatingActionMenu float_menu;
    private FloatingActionButton face_registration;
    private FloatingActionButton face_verification;

    private Toolbar dashboardToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dashboard);
        super.onCreate(savedInstanceState);
        ll_connection_status = (LinearLayout) findViewById(R.id.ll_connection_status);
        ll_sync_down_incremental = (LinearLayout) findViewById(R.id.ll_sync_down_incremental);
        ll_request_gps = (LinearLayout) findViewById(R.id.ll_request_gps);
        ll_pending_sync = (LinearLayout) findViewById(R.id.ll_pending_sync);
        rl_close_menu = (RelativeLayout) findViewById(R.id.rl_close_menu);
        ll_time_in = (LinearLayout) findViewById(R.id.ll_time_in);
        ll_time_out = (LinearLayout) findViewById(R.id.ll_time_out);
        tv_server_connect = (TextView) findViewById(R.id.tv_server_connect);
        tv_last_sync = (TextView) findViewById(R.id.tv_last_sync);
        tv_gps_connect = (TextView) findViewById(R.id.tv_gps_connect);
        tv_pending_sync = (TextView) findViewById(R.id.tv_pending_sync);
        float_menu = (FloatingActionMenu) findViewById(R.id.float_menu);
        face_registration = (FloatingActionButton) findViewById(R.id.face_registration);
        face_verification = (FloatingActionButton) findViewById(R.id.face_verification);
        dashboardToolbar = (Toolbar) findViewById(R.id.dashboardToolbar);

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, LogTimeActivity.class);
        switch (view.getId()) {
            case R.id.ll_connection_status:
                Toast.makeText(this, "Connection Status!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_sync_down_incremental:
                Toast.makeText(this, "Sync Down Incremental!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_request_gps:
                Toast.makeText(this, "Request GPS!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_pending_sync:
                Toast.makeText(this, "Pending Sync!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_time_in:
                Toast.makeText(this, "Log Time-In!", Toast.LENGTH_SHORT).show();
                i.putExtra(LogTimeActivity.class.getSimpleName(), Globals.TIME_IN_ACTIVITY);
                startActivity(i);
                break;
            case R.id.ll_time_out:
                Toast.makeText(this, "Log Time-Out!", Toast.LENGTH_SHORT).show();
                i.putExtra(LogTimeActivity.class.getSimpleName(), Globals.TIME_OUT_ACTIVITY);
                startActivity(i);
                break;
            case R.id.rl_close_menu:
                float_menu.close(true);
                break;
            case R.id.face_registration:
                Intent RegisterIntent = new Intent(this, RegisterAndVerifyFaceActivity.class);
                RegisterIntent.putExtra(Globals.INTENT_FACE_ACTIVITIES, FaceRegistrationFragment.class.getSimpleName());
                startActivity(RegisterIntent);
                break;
            case R.id.face_verification:
                Intent verifyIntent = new Intent(this, RegisterAndVerifyFaceActivity.class);
                verifyIntent.putExtra(Globals.INTENT_FACE_ACTIVITIES, FaceVerificationFragment.class.getSimpleName());
                startActivity(verifyIntent);
                break;
        }
    }


    @Override
    public void onActivityLoading() {

    }

    @Override
    public void onActivityReady() {
        ll_connection_status.setOnClickListener(this);
        ll_sync_down_incremental.setOnClickListener(this);
        ll_request_gps.setOnClickListener(this);
        ll_pending_sync.setOnClickListener(this);
        ll_time_in.setOnClickListener(this);

        rl_close_menu.setOnClickListener(this);
        face_registration.setOnClickListener(this);
        face_verification.setOnClickListener(this);
        float_menu.setClosedOnTouchOutside(true);

        float_menu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {
                if (opened) {
                    rl_close_menu.setVisibility(View.VISIBLE);
                    rl_close_menu.setAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.animate_fade_in));
                } else {
                    rl_close_menu.setVisibility(View.GONE);
                    rl_close_menu.setAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.animate_fade_out));
                }
            }
        });
    }
}
