package com.globe3.tno.g3_lite_mobile.controllers;

import android.content.Context;

import com.globe3.tno.g3_lite_mobile.db_manager.ProjectRepo;
import com.globe3.tno.g3_lite_mobile.models.Project;

import java.util.List;

/**
 * Created by huylv on 23/02/2017.
 */

public class ProjectController {

    private static ProjectController instance;
    private static ProjectRepo projectRepo;

    private ProjectController() {

    }

    public static ProjectController getInstance(Context context) {
        if (instance == null) {
            instance = new ProjectController();
        }
        if (projectRepo == null) {
            projectRepo = new ProjectRepo(context);
        }
        return instance;
    }

    public void makeDummyData() {
        for (int i = 0; i < 10; i++) {
            projectRepo.insertProject(new Project((i + 1), "Project name - " + (i + 1)));
        }
    }

    public List<Project> getAllProject() {
        return projectRepo.getAllProject();
    }

    public void updateProject(Project project) {
        projectRepo.updateProject(project);
    }

    public Project getProjectById(int id) {
        return projectRepo.getProjectById(id);
    }

    public void openDB() {
        projectRepo.open();
    }

    public void closeDB() {
        projectRepo.close();
    }

}
