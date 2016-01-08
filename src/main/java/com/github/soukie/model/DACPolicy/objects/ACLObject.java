package com.github.soukie.model.DACPolicy.objects;

import java.util.Date;

/**
 * The ACL Object class contains all of a object' properties.
 * Created by qiyiy on 2016/1/5.
 */
public class ACLObject {
    private int id;
    private String name;
    private String info;
    private int createdSubjectId;
    private String createdSubjectName;
    private long createTime;
    private long lastUpdateTime;
    private boolean executable;

    public ACLObject() {

    }

    public ACLObject(boolean executable,
                     int id, String name,
                     int createdSubjectId,
                     String createdSubjectName,
                     long createTime) {
        this.executable = executable;
        this.id = id;
        this.name = name;
        this.createdSubjectId = createdSubjectId;
        this.createdSubjectName = createdSubjectName;
        this.createTime = createTime;
        this.lastUpdateTime = createTime;
        updateInfo();;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public int getCreatedSubjectId() {
        return createdSubjectId;
    }

    public String getCreatedSubjectName() {
        return createdSubjectName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setName(String name, long lastUpdateTime) {
        this.name = name;
        this.lastUpdateTime = lastUpdateTime;
        updateInfo();
    }


    public void setExecutable(boolean executable, long lastUpdateTime) {
        this.executable = executable;
        this.lastUpdateTime = lastUpdateTime;
        updateInfo();
    }

    private void updateInfo() {
        this.info = "Object id:" + id + " Object name:" + name + " created by:" + createdSubjectId +
                "(" + createdSubjectName + ")" + " at " + new Date(createTime);
    }
}
