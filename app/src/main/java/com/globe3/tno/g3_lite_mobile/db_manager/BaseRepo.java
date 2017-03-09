package com.globe3.tno.g3_lite_mobile.db_manager;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.globe3.tno.g3_lite_mobile.globals.Globals;

import java.io.File;

/**
 * Created by huylv on 22/02/2017.
 */

public class BaseRepo {
    public SQLiteDatabase database;
    public DBManager dbManager;

    public BaseRepo(Context context) {
        dbManager = new DBManager(context);
    }

    public BaseRepo(SQLiteDatabase database, DBManager dbManager) {
        this.database = database;
        this.dbManager = dbManager;
    }

    public void open() throws SQLException {
        if (database == null || !database.isOpen()) {
            database = dbManager.getWritableDatabase();
        }
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
