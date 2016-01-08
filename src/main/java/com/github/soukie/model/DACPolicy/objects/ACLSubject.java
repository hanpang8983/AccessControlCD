package com.github.soukie.model.DACPolicy.objects;

import java.util.Date;

/**
 * The ACL Subject contains a subject's properties.
 * Created by qiyiy on 2016/1/5.
 */
public class ACLSubject {
    private int id;
    private String name;
    private String password;
    private String info;
    private long createdTime;
    private long lastUpdateTime;

    public ACLSubject() {

    }


    public ACLSubject(int id, String name, long createdTime, String password ) {
        this.createdTime = createdTime;
        this.password = password;
        this.name = name;
        this.id = id;
        updateInfo();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getInfo() {
        return info;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setName(String name, long lastUpdateTime) {
        this.name = name;
        this.lastUpdateTime = lastUpdateTime;
        updateInfo();
    }

    public void setPassword(String password, long lastUpdateTime) {
        this.password = password;
        this.lastUpdateTime = lastUpdateTime;
        updateInfo();
    }

    public void setInfo(String info) {
        this.info = info;
    }

    private void updateInfo() {
        this.info = "Subject id:" + id + " Subject name:" + name + " created at:" + new Date(createdTime);
    }


}
