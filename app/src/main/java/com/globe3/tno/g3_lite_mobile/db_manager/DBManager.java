package com.globe3.tno.g3_lite_mobile.db_manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.globe3.tno.g3_lite_mobile.globals.Globals;

/**
 * Created by huylv on 22/02/2017.
 */

public class DBManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "globe3TMS.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_WORKER = "worker";
    public static final String TABLE_PROJECT = "project";

    public static final String COLUMN_WORKER_ID = "worker_id";
    public static final String COLUMN_WORKER_NAME = "worker_name";
    public static final String COLUMN_WORKER_FACE_ID = "worker_face_id";
    public static final String COLUMN_PROJECT_ID = "project_id";
    public static final String COLUMN_PROJECT_NAME = "project_name";
    public static final String COLUMN_WORKER_IS_NSUBJECT = "is_nsubject";

    private static final String CREATE_TABLE_PROJECT = "CREATE TABLE IF NOT EXISTS `project` (" +
            "`project_id`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`project_name`TEXT);";

    private static final String CREATE_TABLE_WORKER = "CREATE TABLE IF NOT EXISTS `worker` (" +
            "`worker_id`INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "`worker_name`TEXT," +
            "`project_id` INTEGER," +
            "`worker_face_id`BLOB," +
            "'is_nsubject' INTEGER," +
            "FOREIGN KEY(project_id) REFERENCES project(project_id));";

    public DBManager(Context context) {
        super(context, Globals.GLOBE3_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PROJECT);
        db.execSQL(CREATE_TABLE_WORKER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_PROJECT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_WORKER);
    }

}
