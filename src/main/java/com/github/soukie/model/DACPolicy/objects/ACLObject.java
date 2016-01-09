package com.github.soukie.model.DACPolicy.objects;

import java.util.Date;

/**
 * The ACL Object class contains all of a object' properties.
 * Created by qiyiy on 2016/1/5.
 */
public class ACLObject {
    private final int id;
    private String name;
    private String info;
    private final int createdSubjectId;
    private String createdSubjectName;
    private final long createdTime;
    private long lastUpdateTime;
    private boolean executable;


    public ACLObject(int id,
                     String name,
                     int createdSubjectId,
                     String createdSubjectName,
                     long createTime,
                     boolean executable) {
        this.executable = executable;
        this.id = id;
        this.name = name;
        this.createdSubjectId = createdSubjectId;
        this.createdSubjectName = createdSubjectName;
        this.createdTime = createTime;
        this.lastUpdateTime = createTime;
        updateInfo();
        ;
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
        return createdTime;
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
                "(" + createdSubjectName + ")" + " at " + new Date(createdTime);
    }
}
