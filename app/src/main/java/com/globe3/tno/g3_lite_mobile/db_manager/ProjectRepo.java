package com.globe3.tno.g3_lite_mobile.db_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.globe3.tno.g3_lite_mobile.models.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huylv on 22/02/2017.
 */

public class ProjectRepo extends BaseRepo {

    private String[] allColumns = {
            DBManager.COLUMN_PROJECT_ID,
            DBManager.COLUMN_PROJECT_NAME
    };

    public ProjectRepo(Context context) {
        super(context);
    }

    public ProjectRepo(BaseRepo baseRepo) {
        super(baseRepo.database, baseRepo.dbManager);
    }

    public List<Project> getAllProject() {
        List<Project> projectList = new ArrayList<>();
        if (database != null && database.isOpen()) {
            String query = "SELECT * FROM " + DBManager.TABLE_PROJECT;
            Cursor cursor = database.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    projectList.add(cursorToObject(cursor));
                } while (cursor.moveToNext());
            }
        }
        return projectList;
    }

    public Project getProjectById(int id) {
        if (database != null && database.isOpen()) {
            Cursor cursor = database.query(DBManager.TABLE_PROJECT, allColumns, DBManager.COLUMN_PROJECT_ID + " = " + id, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                return cursorToObject(cursor);
            }
        }
        return null;
    }

    public void insertProject(Project project) {
        ContentValues values = new ContentValues();
        putValues(values, project);
        database.insert(DBManager.TABLE_PROJECT, null, values);
    }

    public void updateProject(Project project) {
        ContentValues values = new ContentValues();
        putValues(values, project);
        database.update(DBManager.TABLE_PROJECT, values, DBManager.COLUMN_PROJECT_ID + " = " + project.getId(), null);
    }

    public void deleteProject(Project project) {
        database.delete(DBManager.TABLE_PROJECT, DBManager.COLUMN_PROJECT_ID + " = " + project.getId(), null);
    }

    private Project cursorToObject(Cursor cursor) {
        int id = Integer.parseInt(cursor.getString(0));
        String name = cursor.getString(1);
        return new Project(id, name);
    }

    private void putValues(ContentValues values, Project project) {
        values.put(DBManager.COLUMN_PROJECT_NAME, project.getName());
    }
}
