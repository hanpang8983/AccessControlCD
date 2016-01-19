package com.github.soukie.model.RABCPolicy.subjects;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class URA {
    public String userName;
    public String roleName;

    public URA(String userName, String roleName) {
        this.roleName = roleName;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getRoleName() {
        return roleName;
    }
}
