package com.github.soukie.model.RABCPolicy.subjects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class User {
    public final  String userName;
    public String password;
    public String userInfo;
    public final long createdTime;

    private ArrayList<String> roleNames;

    public User(String userName, String password, long createdTime) {
        this.userName = userName;
        this.password = password;
        this.createdTime = createdTime;
        this.userInfo = userName + "created at " + new Date(createdTime);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public void addRoleName(String roleName) {
        roleNames.add(roleName);
    }

    public void addRoleNames(ArrayList<String> roleNames) {
        this.roleNames.addAll(roleNames);
    }

    public boolean removeRoleName(String roleName) {
        return roleNames.remove(roleName);
    }

    public boolean removeRoleName(ArrayList<String> roleNames) {
        return this.roleNames.removeAll(roleNames);
    }
    public ArrayList<String> getRolenames() {
        return roleNames;
    }
}
