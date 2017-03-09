package com.globe3.tno.g3_lite_mobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;
import com.frosquivel.magicalcamera.Objects.MagicalCameraObject;
import com.frosquivel.magicalcamera.Utilities.ConvertSimpleImage;
import com.globe3.tno.g3_lite_mobile.R;
import com.globe3.tno.g3_lite_mobile.activities.RegisterAndVerifyFaceActivity;
import com.globe3.tno.g3_lite_mobile.adapters.ProjectAdapter;
import com.globe3.tno.g3_lite_mobile.adapters.WorkerAdapter;
import com.globe3.tno.g3_lite_mobile.controllers.ProjectController;
import com.globe3.tno.g3_lite_mobile.controllers.WorkerController;
import com.globe3.tno.g3_lite_mobile.globals.Globals;
import com.globe3.tno.g3_lite_mobile.models.Project;
import com.globe3.tno.g3_lite_mobile.models.Worker;
import com.globe3.tno.g3_lite_mobile.utils.BitmapUtility;
import com.neurotec.biometrics.NBiometric;
import com.neurotec.biometrics.NBiometricCaptureOption;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NFace;
import com.neurotec.biometrics.NMatchingSpeed;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.biometrics.view.NFaceView;
import com.neurotec.devices.NCamera;
import com.neurotec.devices.NDevice;
import com.neurotec.devices.NDeviceManager;
import com.neurotec.devices.NDeviceType;
import com.neurotec.licensing.LicensingManager;
import com.neurotec.licensing.LicensingStateResult;
import com.neurotec.util.NImageUtils;
import com.neurotec.util.concurrent.CompletionHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class FaceRegistrationFragment extends Fragment implements View.OnClickListener, ProjectAdapter.ProjectItemListener, WorkerAdapter.WorkerItemListener, RegisterAndVerifyFaceActivity.OnBackPressedListener {
    private static final String TAG = FaceRegistrationFragment.class.getSimpleName();
    private EditText et_search;
    private RecyclerView recycler_view;
    private ProjectAdapter projectAdapter;
    private WorkerAdapter workerAdapter;
    private List<Project> projectList;
    private List<Worker> workerList;
    private RegisterAndVerifyFaceActivity registerAndVerifyFaceActivity;
    private LinearLayout ll_main_fragment;
    private LinearLayout ll_total_content;
    private LinearLayout ll_register_face;
    private ImageButton bt_capture;
    private NFaceView nFaceView;
    private WorkerController workerController;
    private NBiometricClient nBiometricClient;
    private ImageButton bt_face_recognition_settings;
    private Worker selectedWorker;
    private MagicalCamera magicalCamera;

    private CompletionHandler<NBiometricStatus, NSubject> completionHandler = new CompletionHandler<NBiometricStatus, NSubject>() {
        @Override
        public void completed(NBiometricStatus nBiometricStatus, NSubject nSubject) {
            Log.d(TAG, "status: " + nBiometricStatus.name());
            if (nBiometricStatus == NBiometricStatus.OK) {
                NFace face = nSubject.getFaces().get(0);
                Bitmap bitmap = face.getImage().toBitmap();
                selectedWorker.setFace(BitmapUtility.encodeBitmapToByte(bitmap));
                selectedWorker.setNSubject(true);

//                NSubject subject = createNSubject(selectedWorker.getFace());
//                NFace faceSave = subject.getFaces().get(0);
//                File outputFile = new File(Globals.GLOBE3_IMAGE_DIR, "face_image_from_camera.JPEG");
//                File file2 = new File(Globals.GLOBE3_IMAGE_DIR, "original_image.JPEG");
//                try {
//                    faceSave.getImage().save(outputFile.getAbsolutePath());
//                    face.getImage().save(file2.getAbsolutePath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                Log.d(TAG, "face: " + selectedWorker.getFace().toString());
                workerController.registerFaceId(selectedWorker);
                visibleCapture(false);
                registerAndVerifyFaceActivity.showMessages("Face is Registered!");
                Log.d(TAG, "Worker: " + selectedWorker.getId() + " \t" + selectedWorker.getFace().toString() + " \t" + selectedWorker.getName());
            } else if (nBiometricStatus != NBiometricStatus.CANCELED) {
                registerAndVerifyFaceActivity.showMessages(nBiometricStatus.name() + ": Please Re-Register again!");
                startCapturing();
            }
        }

        @Override
        public void failed(Throwable throwable, NSubject nSubject) {
            throwable.printStackTrace();
            visibleCapture(false);
            registerAndVerifyFaceActivity.showMessages("Enroll failed, Please Re-Register again!");
            Log.d(TAG, "failed of enrolling");
        }
    };

    private static final String[] LICENSES = {
            LicensingManager.LICENSE_FACE_EXTRACTION,
            LicensingManager.LICENSE_FACE_MATCHING,
            LicensingManager.LICENSE_FACE_MATCHING_FAST
    };

    private LicensingManager.LicensingStateCallback callback = new LicensingManager.LicensingStateCallback() {
        @Override
        public void onLicensingStateChanged(LicensingStateResult stateResult) {
            Log.d(TAG, stateResult.getState().name());
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_face_registration, container, false);
        et_search = (EditText) view.findViewById(R.id.et_search);
        recycler_view = (RecyclerView) view.findViewById(R.id.recycler_view);
        ll_main_fragment = (LinearLayout) view.findViewById(R.id.ll_main_fragment);
        ll_total_content = (LinearLayout) view.findViewById(R.id.ll_total_content);
        ll_register_face = (LinearLayout) view.findViewById(R.id.ll_register_face);
        nFaceView = (NFaceView) view.findViewById(R.id.nFaceView);
        bt_capture = (ImageButton) view.findViewById(R.id.bt_capture);
        bt_face_recognition_settings = (ImageButton) view.findViewById(R.id.bt_face_recognition_settings);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterAndVerifyFaceActivity) {
            registerAndVerifyFaceActivity = (RegisterAndVerifyFaceActivity) context;
            Log.d(TAG, "activity: " + registerAndVerifyFaceActivity.getClass().getSimpleName());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (ProjectController.getInstance(getActivity()).getAllProject().size() == 0) {
            ProjectController.getInstance(getActivity()).makeDummyData();
            WorkerController.getInstance(getActivity()).makeDummyData();
        }
        projectList = ProjectController.getInstance(getActivity()).getAllProject();
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        projectAdapter = new ProjectAdapter(getActivity(), (ArrayList<Project>) projectList);
        workerAdapter = new WorkerAdapter(getActivity(), new ArrayList<Worker>());
        recycler_view.setAdapter(projectAdapter);
        projectAdapter.setProjectItemListener(this);
        workerAdapter.setWorkerItemListener(this);
        bt_face_recognition_settings.setOnClickListener(this);
        bt_capture.setOnClickListener(this);
        visibleCapture(false);
        workerController = WorkerController.getInstance(getActivity());
        PermissionGranted permissionGranted = new PermissionGranted(getActivity());
        permissionGranted.checkCameraPermission();
        permissionGranted.checkReadExternalPermission();
        permissionGranted.checkWriteExternalPermission();
        magicalCamera = new MagicalCamera(getActivity(), Globals.RESIZE_PHOTO_PIXELS_PERCENTAGE, permissionGranted);
        new LoadTask().execute();
    }

    @Override
    public void onProjectItemListener(final int position, final Project project) {
        workerList = WorkerController.getInstance(getActivity()).getWorkerListByProjectId(project.getId());
        workerAdapter.setDataList((ArrayList<Worker>) workerList);
        recycler_view.setAdapter(null);
        recycler_view.setAdapter(workerAdapter);
        et_search.setHint(getString(R.string.hint_select_worker));
    }

    @Override
    public void onWorkerItemListener(final int position, final Worker worker) {
        selectedWorker = worker;
        if (Globals.IS_MAGICAL_CAMERA) {
            if (magicalCamera.takeFragmentPhoto()) {
                startActivityForResult(magicalCamera.getIntentFragment(), MagicalCameraObject.TAKE_PHOTO);
            }
        } else {
            visibleCapture(true);
            new OpenCameraTask().execute();
        }
    }

    private void initBiometric() {
        nBiometricClient = new NBiometricClient();
        nBiometricClient.setMatchingThreshold(48);
        nBiometricClient.setFacesMatchingSpeed(NMatchingSpeed.LOW);
    }

    private void initFaceRecognition() {
        LicensingManager.getInstance().obtain(getActivity(), callback, Arrays.asList(LICENSES));
        if (nBiometricClient == null) {
            nBiometricClient = new NBiometricClient();
        } else {
            nBiometricClient.setUseDeviceManager(true);
            NDeviceManager deviceManager = nBiometricClient.getDeviceManager();
            deviceManager.setDeviceTypes(EnumSet.of(NDeviceType.CAMERA));
            nBiometricClient.initialize();
        }
//        startCapturing();
    }

    private NSubject createNSubject(byte[] arr) {
        Bitmap bitmap = ConvertSimpleImage.bytesToBitmap(arr, MagicalCamera.JPEG);
        NSubject subject = new NSubject();
        NFace face = new NFace();
        face.setImage(NImageUtils.fromBitMap(bitmap));
        subject.getFaces().add(face);
        return subject;
    }

    private void startCapturing() {
        try {
            if (!LicensingManager.isActivated(LicensingManager.LICENSE_FACE_DETECTION)) {
                //Toast
                registerAndVerifyFaceActivity.showMessages("Face Detection is not activated!");
            } else if (!LicensingManager.isActivated(LicensingManager.LICENSE_FACE_EXTRACTION)) {
                //Toast
                registerAndVerifyFaceActivity.showMessages("Face Extraction is not activated!");
            } else if (nBiometricClient.getCurrentSubject() != null) {
                // Toast
                Log.d(TAG, "startCapturing() - nBiometricClient.getCurrentSubject() != null");
            } else {
                NSubject subject = new NSubject();
                NFace face = new NFace();
                face.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        NBiometricStatus status = ((NBiometric) evt.getSource()).getStatus();
                        Log.d(TAG, status.toString());
                    }
                });
                face.setCaptureOptions(Globals.CAPTURE_METHOD == 0 ? EnumSet.of(NBiometricCaptureOption.MANUAL)
                        : EnumSet.of(NBiometricCaptureOption.STREAM));
                nFaceView.setFace(face);
                subject.getFaces().add(face);
                NCamera camera = (NCamera) connectDevice(nBiometricClient.getDeviceManager());
                nBiometricClient.setFaceCaptureDevice(camera);
                nBiometricClient.capture(subject, subject, completionHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private NDevice connectDevice(NDeviceManager deviceManager) {
        if (deviceManager.getDevices().size() > 1) {
            if (Globals.CAMERA_TYPE == 0) {
                return deviceManager.getDevices().get(0);
            } else {
                return deviceManager.getDevices().get(1);
            }
        } else {
            return deviceManager.getDevices().get(0);
        }
    }

    @Override
    public void onBackPressedListener() {
        if (ll_total_content.getVisibility() == View.GONE) {
            nBiometricClient.cancel();
            visibleCapture(false);
        } else {
            if (recycler_view.getAdapter() instanceof WorkerAdapter) {
                recycler_view.setAdapter(null);
                recycler_view.setAdapter(projectAdapter);
                et_search.setHint(getString(R.string.hint_select_project));
            } else
                registerAndVerifyFaceActivity.finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_face_recognition_settings:
                registerAndVerifyFaceActivity.setDialogCameraOption(registerAndVerifyFaceActivity.openDialogCameraOption());
                break;
            case R.id.bt_capture:
                if (nBiometricClient != null) {
                    nBiometricClient.force();
                }
                break;
        }
    }

//    private NSubject createNSubject(Bitmap bitmap) {
//        NSubject subject = new NSubject();
//        NFace face = new NFace();
//        face.setImage(NImageUtils.fromBitMap(bitmap));
//        subject.getFaces().add(face);
//        return subject;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (magicalCamera != null) {
            magicalCamera.resultPhoto(requestCode, resultCode, data);
            if (magicalCamera.getPhoto() != null) {
                final Bitmap bitmap = magicalCamera.getPhoto();
                selectedWorker.setFace(ConvertSimpleImage.bitmapToBytes(bitmap, MagicalCamera.JPEG));
                selectedWorker.setNSubject(false);
                workerController.registerFaceId(selectedWorker);
                registerAndVerifyFaceActivity.showMessages("Face is registered!");
                Log.d(TAG, "Worker: " + selectedWorker.getId() + " \t" + selectedWorker.getName() + "\t"
                        + selectedWorker.getFace().toString().length() + " \t" + selectedWorker.getFace().toString());
//                try {
//                    NSubject subject = createNSubject(bitmap);
//                    NBiometricTask enrollTask = nBiometricClient.createTask(EnumSet.of(NBiometricOperation.ENROLL), subject);
//                    nBiometricClient.performTask(enrollTask, NBiometricOperation.ENROLL, new CompletionHandler<NBiometricTask, NBiometricOperation>() {
//                        @Override
//                        public void completed(NBiometricTask result, NBiometricOperation attachment) {
//                            Log.d(TAG, "Enroll completed: " + result.getStatus().name());
//                            switch (attachment) {
//                                case ENROLL:
//                                    if (result.getStatus() == NBiometricStatus.OK) {
//                                        selectedWorker.setFace(ConvertSimpleImage.bitmapToBytes(bitmap, MagicalCamera.JPEG));
//                                        workerController.registerFaceId(selectedWorker);
//                                        registerAndVerifyFaceActivity.showMessages("Face is registered!");
//                                        Log.d(TAG, "Worker: " + selectedWorker.getId() + " \t" + selectedWorker.getName() + "\t"
//                                                + selectedWorker.getFace().toString().length() + " \t" + selectedWorker.getFace().toString());
//                                    } else {
//                                        registerAndVerifyFaceActivity.showMessages("Face is not registered, please try again!");
//                                    }
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void failed(Throwable throwable, NBiometricOperation attachment) {
//                            Log.d(TAG, "Enroll failed!");
//                            registerAndVerifyFaceActivity.showMessages(throwable.getMessage() != null ? throwable.getMessage() : throwable.toString());
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (magicalCamera != null) {
            magicalCamera.permissionGrant(requestCode, permissions, grantResults);
        }
    }


    private void visibleCapture(final boolean isVisible) {
        registerAndVerifyFaceActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isVisible) {
                    ll_total_content.setVisibility(View.GONE);
                    ll_register_face.setVisibility(View.VISIBLE);
                    bt_capture.setVisibility(Globals.CAPTURE_METHOD == 0 ? View.VISIBLE : View.GONE);
                    ll_register_face.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_fade_in));
                } else {
                    ll_register_face.setVisibility(View.GONE);
                    ll_total_content.setVisibility(View.VISIBLE);
                    ll_total_content.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.animate_fade_in));
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView()");
        if (nBiometricClient != null) {
            nBiometricClient.setUseDeviceManager(false);
            nBiometricClient.cancel();
            nBiometricClient.dispose();
            nBiometricClient = null;
        }
        super.onDestroyView();
    }

    class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            registerAndVerifyFaceActivity.showProgressBarActivity(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            initBiometric();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            registerAndVerifyFaceActivity.showProgressBarActivity(false);
            super.onPostExecute(aVoid);
        }
    }

    class OpenCameraTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            registerAndVerifyFaceActivity.showProgressBarActivity(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            initFaceRecognition();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            registerAndVerifyFaceActivity.showProgressBarActivity(false);
            startCapturing();
            super.onPostExecute(aVoid);
        }
    }
}
