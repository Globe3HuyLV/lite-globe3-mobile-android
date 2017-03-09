package com.globe3.tno.g3_lite_mobile.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject;
import com.frosquivel.magicalcamera.Utilities.ConvertSimpleImage;
import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.adapters.ProjectAdapter;
import com.globe3.tno.g3_lite_mobile.adapters.WorkerAdapter;
import com.globe3.tno.g3_lite_mobile.controllers.ProjectController;
import com.globe3.tno.g3_lite_mobile.controllers.WorkerController;
import com.globe3.tno.g3_lite_mobile.globals.Globals;
import com.globe3.tno.g3_lite_mobile.models.Project;
import com.globe3.tno.g3_lite_mobile.models.Worker;
import com.globe3.tno.g3_lite_mobile.utils.DateUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class LogTimeActivity extends BaseActivity implements View.OnClickListener, ProjectAdapter.ProjectItemListener, WorkerAdapter.WorkerItemListener {

    private static final String TAG = LogTimeActivity.class.getSimpleName();
    private TextView tv_real_time;
    private TextView tv_time_kind;
    private TextView tv_project_name;
    private TextView tv_status_log;
    private TextView tv_worker_information;
    private TextView tv_date_log;
    private TextView tv_time_log;

    private ImageView iv_worker_image;

    private Button bt_action;
    private Button bt_cancel;
    private LinearLayout ll_log_information;
    private LinearLayout ll_project_information;
    private LinearLayout ll_worker_information;
    private RecyclerView recycler_view_project;

    private EditText et_search_project;
    private EditText et_search_worker;

    private List<Project> projectList;

    private List<Worker> workerList;

    private ProjectAdapter projectAdapter;

    private WorkerAdapter workerAdapter;

    private String timeKindActivity;
    private String stringDate;
    private String stringHour;
    private LogTimeActivity logTimeActivity;
    MagicalCamera magicalCamera;

    private BroadcastReceiver updateTimeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().compareTo(Intent.ACTION_TIME_TICK) == 0) {
                setRealTime();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_log_time);
        super.onCreate(savedInstanceState);
        tv_real_time = (TextView) findViewById(R.id.tv_real_time);
        tv_time_kind = (TextView) findViewById(R.id.tv_time_kind);
        tv_project_name = (TextView) findViewById(R.id.tv_project_name);
        tv_status_log = (TextView) findViewById(R.id.tv_status_log);
        tv_worker_information = (TextView) findViewById(R.id.tv_worker_information);
        tv_date_log = (TextView) findViewById(R.id.tv_date_log);
        tv_time_log = (TextView) findViewById(R.id.tv_time_log);
        iv_worker_image = (ImageView) findViewById(R.id.iv_worker_image);
        bt_action = (Button) findViewById(R.id.bt_action);
        bt_cancel = (Button) findViewById(R.id.bt_cancel);
        ll_log_information = (LinearLayout) findViewById(R.id.ll_log_information);
        ll_project_information = (LinearLayout) findViewById(R.id.ll_project_information);
        ll_worker_information = (LinearLayout) findViewById(R.id.ll_worker_information);
        recycler_view_project = (RecyclerView) findViewById(R.id.recycler_view_project);
        et_search_project = (EditText) findViewById(R.id.et_search_project);
        et_search_worker = (EditText) findViewById(R.id.et_search_worker);
        bt_action.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        logTimeActivity = this;
        et_search_project.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String text = textView.getText().toString().trim();
                    if (text.length() < 3) {
                        Toast.makeText(logTimeActivity, "Please input minimum 3 characters to search!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(logTimeActivity, "Searching: " + text,
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityLoading() {
        checkMagicalCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(updateTimeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy()");
        unregisterReceiver(updateTimeReceiver);
        super.onDestroy();
    }

    @Override
    public void onActivityReady() {
        Intent intent = getIntent();
        timeKindActivity = intent.getStringExtra(LogTimeActivity.class.getSimpleName());
        onUpdateUI(timeKindActivity);
        // update data

    }

    private void onUpdateUI(String timeKindActivity) {
        ll_log_information.setVisibility(View.GONE);
        bt_action.setVisibility(View.GONE);
        setRealTime();
        ll_project_information.setVisibility(View.VISIBLE);
        switch (timeKindActivity) {
            case Globals.TIME_IN_ACTIVITY:
                initializeTimeInUI();
                break;
            case Globals.TIME_OUT_ACTIVITY:
                initializeTimeOutUI();
                break;
        }
    }

    private void initializeTimeInUI() {
        tv_time_kind.setText(getString(R.string.label_time_in));
        et_search_project.setHint(getString(R.string.hint_select_project));
        initRecyclerView(recycler_view_project);
        dummyData();
        recycler_view_project.setAdapter(projectAdapter);
        projectAdapter.setProjectItemListener(this);
        workerAdapter.setWorkerItemListener(this);
    }

    private void initializeTimeOutUI() {
        dummyData();
        initRecyclerView(recycler_view_project);
        tv_time_kind.setText(getString(R.string.label_time_out));
        et_search_project.setHint(getString(R.string.hint_select_worker));
        workerAdapter.setDataList((ArrayList<Worker>) WorkerController.getInstance(this).getAllWorker());
        recycler_view_project.setAdapter(workerAdapter);
        workerAdapter.setWorkerItemListener(this);
    }

    private void dummyData() {
        if (ProjectController.getInstance(this).getAllProject().size() == 0) {
            ProjectController.getInstance(this).makeDummyData();
            WorkerController.getInstance(this).makeDummyData();
        }
        projectList = ProjectController.getInstance(this).getAllProject();
        projectAdapter = new ProjectAdapter(this, (ArrayList<Project>) projectList);
        workerAdapter = new WorkerAdapter(this, new ArrayList<Worker>());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_action:
                String actionKind = bt_action.getText().toString();
                if (actionKind.equals(getString(R.string.label_take_photo))) {
                    Toast.makeText(this, "Open Camera", Toast.LENGTH_SHORT).show();
                    magicalCamera.takePhoto();
                } else if (actionKind.equals(getString(R.string.label_confirm_time_in))
                        || actionKind.equals(getString(R.string.label_confirm_time_out))) {
                    Toast.makeText(this, "Worker Confirmed!", Toast.LENGTH_SHORT).show();
                    bt_action.setText(getString(R.string.label_next_worker));
                    tv_status_log.setVisibility(timeKindActivity.equals(Globals.TIME_OUT_ACTIVITY)
                            ? View.VISIBLE : View.GONE);
                } else if (actionKind.equals(getString(R.string.label_next_worker))) {
                    ll_project_information.setVisibility(View.VISIBLE);
                    ll_log_information.setVisibility(View.GONE);
                    iv_worker_image.setVisibility(View.GONE);
                    iv_worker_image.setImageBitmap(null);
                    tv_status_log.setVisibility(View.GONE);
                    bt_action.setVisibility(View.GONE);
                    recycler_view_project.setAdapter(null);
                    if (timeKindActivity.equals(Globals.TIME_IN_ACTIVITY)) {
                        recycler_view_project.setAdapter(projectAdapter);
                        et_search_project.setHint(getString(R.string.hint_select_project));
                    } else {
                        recycler_view_project.setAdapter(workerAdapter);
                        et_search_project.setHint(getString(R.string.hint_select_worker));
                    }
                    showViewAnimation(ll_project_information);
                }
                break;
            case R.id.bt_cancel:
                Toast.makeText(this, "Cancelled!", Toast.LENGTH_SHORT).show();
                logTimeActivity.finish();
                break;
        }

    }

    @Override
    public void onProjectItemListener(int position, Project project) {
        Toast.makeText(this, "Position: " + position + " - " + "Project name: " + project.getName(),
                Toast.LENGTH_SHORT).show();
        hideKeyboard(et_search_project);
        tv_project_name.setText(project.getName());
        et_search_project.setHint(getString(R.string.hint_select_worker));
        workerList = WorkerController.getInstance(this).getWorkerListByProjectId(project.getId());
        workerAdapter.setDataList((ArrayList<Worker>) workerList);
        recycler_view_project.setAdapter(null);
        recycler_view_project.setAdapter(workerAdapter);
        showViewAnimation(ll_project_information);
        Log.d(TAG, "workerAdapter: " + workerAdapter.toString() + "/t" + recycler_view_project.getAdapter().getItemCount());
    }

    @Override
    public void onWorkerItemListener(int position, Worker worker) {
        Toast.makeText(this, "Position: " + position + " - " + "Worker name: " + worker.getName(),
                Toast.LENGTH_SHORT).show();
        hideKeyboard(et_search_project);
        showViewAnimation(ll_log_information);
        ll_project_information.setVisibility(View.GONE);
        tv_worker_information.setText(worker.getName());
        tv_date_log.setText(stringDate);
        iv_worker_image.setVisibility(View.VISIBLE);
        Bitmap bitmap = ConvertSimpleImage.bytesToBitmap(worker.getFace());
        iv_worker_image.setImageBitmap(bitmap);
        if (timeKindActivity.equals(Globals.TIME_IN_ACTIVITY)) {
            tv_time_log.setText(getString(R.string.label_time_in) + " " + stringHour);
        } else {
            tv_time_log.setText(getString(R.string.label_time_out) + " " + stringHour);
        }
        ll_log_information.setVisibility(View.VISIBLE);
        bt_action.setText(getString(R.string.label_take_photo));
        bt_action.setVisibility(View.VISIBLE);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MagicalCameraObject.TAKE_PHOTO) {
            Log.d(TAG, "Photo is taken!");
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            if (magicalCamera.getPhoto() != null) {
                iv_worker_image.setVisibility(View.VISIBLE);
                if (timeKindActivity.equals(Globals.TIME_IN_ACTIVITY)) {
                    bt_action.setText(getString(R.string.label_confirm_time_in));
                } else {
                    bt_action.setText(getString(R.string.label_confirm_time_out));
                }
                iv_worker_image.setImageBitmap(magicalCamera.getPhoto());
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (magicalCamera != null) {
            magicalCamera.permissionGrant(requestCode, permissions, grantResults);
        }
    }

    private void checkMagicalCamera() {
        PermissionGranted permissionGranted = new PermissionGranted(this);
        permissionGranted.checkCameraPermission();
        permissionGranted.checkReadExternalPermission();
        permissionGranted.checkWriteExternalPermission();
        if (magicalCamera == null) {
            magicalCamera = new MagicalCamera(this, 80, permissionGranted);
        }

    }

    private void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    private void showViewAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animate_slide_left);
        view.setAnimation(animation);
    }

    private void setRealTime() {
        stringDate = DateUtility.getDateString(Calendar.getInstance().getTime(),
                DateUtility.DATE_FORMAT_ENQUIRY);
        stringHour = DateUtility.getDateString(Calendar.getInstance().getTime(),
                DateUtility.DATE_FORMAT_HOUR_MINUTES);
        tv_real_time.setText(stringDate + "\t" + stringHour);
        Log.d(TAG, stringDate + "\t" + stringHour);
    }
}
