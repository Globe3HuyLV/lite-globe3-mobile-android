package com.globe3.tno.g3_lite_mobile.models;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class Worker implements Serializable {
    private int id;
    private String name;
    private int projectId;
    private byte[] face;
    private int nSubject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public byte[] getFace() {
        return face;
    }

    public void setFace(byte[] face) {
        Log.d("HuyLV", "setFace");
        this.face = face;
    }

    public boolean isNSubject() {
        return nSubject != 0;
    }

    public void setNSubject(boolean subject) {
        this.nSubject = subject ? 1 : 0;
    }

    public Worker(int id, String name, int projectId, byte[] face, boolean isNSubject) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.face = face;
        this.nSubject = isNSubject ? 1 : 0;
    }
}
