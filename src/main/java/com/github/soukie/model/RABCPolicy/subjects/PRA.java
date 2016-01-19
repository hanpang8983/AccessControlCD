package com.github.soukie.model.RABCPolicy.subjects;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class PRA {
    public String roleName;
    public String permissionName;

    public PRA(String roleName, String permissionName) {
        this.roleName = roleName;
        this.permissionName = permissionName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
