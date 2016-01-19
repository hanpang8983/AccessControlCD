package com.github.soukie.model.RABCPolicy.subjects;

import java.util.Date;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class Permission {
    public final String permissionName;
    public String permissionInfo;
    public final long createdTime;

    public Permission(String permissionName, long createdTime) {
        this.permissionName = permissionName;
        this.createdTime = createdTime;
        this.permissionInfo = permissionName + " created at " + new Date(createdTime);
    }



}
