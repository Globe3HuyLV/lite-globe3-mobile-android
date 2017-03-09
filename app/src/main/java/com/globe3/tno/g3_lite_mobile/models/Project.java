package com.globe3.tno.g3_lite_mobile.models;

/**
 * Created by HuyLV on 10-Feb-17.
 */

public class Project {
    private int id;
    private String name;

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


    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
