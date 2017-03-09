package com.globe3.tno.g3_lite_mobile.controllers;

import android.content.Context;

import com.globe3.tno.g3_lite_mobile.db_manager.WorkerRepo;
import com.globe3.tno.g3_lite_mobile.models.Worker;
import com.globe3.tno.g3_lite_mobile.utils.FaceVerificationUtility;
import com.neurotec.face.verification.NFaceVerification;
import com.neurotec.face.verification.NFaceVerificationStatus;
import com.neurotec.lang.NCore;

import java.util.List;

import static com.globe3.tno.g3_lite_mobile.globals.Globals.FACE_RECOGNITION_TIMEOUT;

/**
 * Created by huylv on 22/02/2017.
 */

public class WorkerController {

    private boolean isFaced;
    private String workerId;
    private static WorkerController instance;
    private NFaceVerification nFaceVerification;
    private static WorkerRepo workerRepo;

    public NFaceVerification getnFaceVerification() {
        return nFaceVerification;
    }

    private WorkerController() {
    }

    public static WorkerController getInstance(Context context) {
        if (instance == null) {
            instance = new WorkerController();
        }
        if (workerRepo == null) {
            workerRepo = new WorkerRepo(context);
        }
        return instance;
    }

    public synchronized NFaceVerification getNFaceVerification(Context context) {
        if (nFaceVerification == null) {
            try {
                NCore.setContext(context);
                nFaceVerification = new NFaceVerification(FaceVerificationUtility.combinePath(
                        FaceVerificationUtility.NEUROTECHNOLOGY_DIRECTORY, "face_database.db"), "database_password");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return nFaceVerification;
    }

    public void registerFaceId(Worker worker) {
        workerRepo.updateWorker(worker);
    }

    public void deleteWorkerById(Worker worker) {
        workerRepo.deleteWorker(worker);
    }

    public NFaceVerificationStatus verifyFaceId(String faceId) {
        if (nFaceVerification != null) {
            nFaceVerification.cancel();
            NFaceVerificationStatus status = nFaceVerification.verify(faceId, FACE_RECOGNITION_TIMEOUT);
            isFaced = status == NFaceVerificationStatus.SUCCESS;
            return status;
        }
        return null;
    }

    public NFaceVerification.UserCollection getNFaceVErificationUsers() {
        return nFaceVerification.getUsers();
    }

    public boolean isFaceId() {
        return isFaced;
    }

    public List<Worker> getAllWorker() {
        return workerRepo.getAllWorker();
    }

    public Worker getWorkerById(int id) {
        Worker worker = workerRepo.getWorkerById(id);
        return worker;
    }

    public List<Worker> getWorkerListByProjectId(int id) {
        return workerRepo.getWorkerByProjectId(id);
    }

    public Worker getWorkerByFaceId(String faceId) {
        return workerRepo.getWorkerByFaceId(faceId);
    }

    public void makeDummyData() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 5; j++) {
                workerRepo.insertWorker(new Worker((j + 1), "Worker name " + (i + 1) + "-" + (j + 1), (i + 1), null, false));
            }
        }
    }

    public void openDB() {
        workerRepo.open();
    }

    public void closeDB() {
        workerRepo.close();
    }

}
