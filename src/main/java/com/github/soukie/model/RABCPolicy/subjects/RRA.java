package com.github.soukie.model.RABCPolicy.subjects;

/**
 * Created by qiyiy on 2016/1/19.
 */
public class RRA {
    public String fatherRoleName;
    public String childrenRoleName;

    public RRA(String fatherRoleName, String childrenRoleName) {
        this.fatherRoleName = fatherRoleName;
        this.childrenRoleName = childrenRoleName;
    }

    public String getFatherRoleName() {
        return fatherRoleName;
    }

    public String getChildrenRoleName() {
        return childrenRoleName;
    }
}
