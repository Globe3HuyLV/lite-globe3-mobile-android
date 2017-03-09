package com.globe3.tno.g3_lite_mobile.db_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.globe3.tno.g3_lite_mobile.models.Worker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huylv on 22/02/2017.
 */

public class WorkerRepo extends BaseRepo {

    private String[] allColumns = {
            DBManager.COLUMN_WORKER_ID,
            DBManager.COLUMN_WORKER_NAME,
            DBManager.COLUMN_PROJECT_ID,
            DBManager.COLUMN_WORKER_FACE_ID,
            DBManager.COLUMN_WORKER_IS_NSUBJECT
    };

    public WorkerRepo(Context context) {
        super(context);
    }

    public WorkerRepo(BaseRepo baseRepo) {
        super(baseRepo.database, baseRepo.dbManager);
    }

    public List<Worker> getAllWorker() {
        List<Worker> workerList = new ArrayList<Worker>();
        if (database != null && database.isOpen()) {
            String query = "SELECT * FROM " + DBManager.TABLE_WORKER;
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    workerList.add(cursorToObject(cursor));
                } while (cursor.moveToNext());
            }
        }
        return workerList;
    }

    public Worker getWorkerById(int id) {
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(DBManager.TABLE_WORKER, allColumns, DBManager.COLUMN_WORKER_ID + " = " + id, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursorToObject(cursor);
            }
        }
        return null;
    }

    public Worker getWorkerByFaceId(String faceId) {
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(DBManager.TABLE_WORKER, allColumns, DBManager.COLUMN_WORKER_FACE_ID + " = " + faceId, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursorToObject(cursor);
            }
        }
        return null;
    }

    public List<Worker> getWorkerByProjectId(int projectId) {
        List<Worker> workerList = new ArrayList<>();
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(DBManager.TABLE_WORKER, allColumns, DBManager.COLUMN_PROJECT_ID + " = " + projectId, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    workerList.add(cursorToObject(cursor));
                } while (cursor.moveToNext());
            }
        }
        for (Worker worker : workerList) {
            Log.d("HuyLV", worker.getId() + " - " + worker.getName() + " - " + worker.getProjectId());
        }
        return workerList;
    }

    public void insertWorker(Worker worker) {
        ContentValues values = new ContentValues();
        putValues(values, worker);
        database.insert(DBManager.TABLE_WORKER, null, values);
    }

    public void updateWorker(Worker worker) {
        ContentValues values = new ContentValues();
        putValues(values, worker);
        database.update(DBManager.TABLE_WORKER, values, DBManager.COLUMN_WORKER_ID + " = " + worker.getId(), null);
    }

    public void deleteWorker(Worker worker) {
        database.delete(DBManager.TABLE_WORKER, DBManager.COLUMN_WORKER_ID + " = " + worker.getId(), null);
    }

    private Worker cursorToObject(Cursor cursor) {
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        int projectId = Integer.parseInt(cursor.getString(2));
        byte[] faceId = cursor.getBlob(3);
        int nSubject = cursor.getInt(4);
        return new Worker(id, name, projectId, faceId, nSubject != 0);
    }

    private void putValues(ContentValues values, Worker worker) {
        values.put(DBManager.COLUMN_WORKER_NAME, worker.getName());
        values.put(DBManager.COLUMN_PROJECT_ID, worker.getProjectId());
        values.put(DBManager.COLUMN_WORKER_FACE_ID, worker.getFace());
        values.put(DBManager.COLUMN_WORKER_IS_NSUBJECT, worker.isNSubject() ? 1 : 0);
    }
}
