package com.github.soukie.model.RABCPolicy.subjects;

import com.github.soukie.model.ModelValues;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class Role {

    public final String roleName;
    public String roleInfo;
    public final long createdTime;

    private String fatherRoleName = ModelValues.ADMIN_ROLE_NAME;
    private ArrayList<String> childrenRoleNames;
    private ArrayList<String> permissionNames;

    public Role(String roleName, long createdTime) {
        this.roleName = roleName;
        this.createdTime = createdTime;

        this.roleInfo = roleName + " created at " + new Date(createdTime);
    }

    public void setRoleInfo(String roleInfo) {
        this.roleInfo = roleInfo;
    }

    public void addChildrenRoleName(String childrenRoleName) {
        childrenRoleNames.add(childrenRoleName);
    }

    public void addChildrenRoleNames(ArrayList<String> childrenRoleNames) {
        this.childrenRoleNames.addAll(childrenRoleNames);
    }

    public boolean removeChildrenRoleName(String  childrenRoleName) {
        return childrenRoleNames.remove(childrenRoleName);
    }

    public boolean removeChildrenRoleNames(ArrayList<String> childrenRoleNames) {
        return this.childrenRoleNames.removeAll(childrenRoleNames);
    }

    public ArrayList<String> getChildrenRoleNames() {
        return childrenRoleNames;
    }

    public void addPermissionName(String  permissionName) {
        permissionNames.add(permissionName);
    }

    public void addPermissionNames(ArrayList<String> permissionNames) {
        this.permissionNames.addAll(permissionNames);
    }

    public boolean removePermissionName(String permissionName) {
        return permissionNames.remove(permissionName);
    }

    public boolean removePermissionNames(ArrayList<String> permissionNames) {
        return this.permissionNames.removeAll(permissionNames);
    }
    public ArrayList<String> getPermissionNames() {
        return permissionNames;
    }

    public void setFatherRoleName(String fatherRoleName) {
        this.fatherRoleName = fatherRoleName;
    }

}
