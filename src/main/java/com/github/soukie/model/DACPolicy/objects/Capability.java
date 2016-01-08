package com.github.soukie.model.DACPolicy.objects;

import com.github.soukie.model.DACPolicy.values.CapabilityList;
import com.github.soukie.model.ModelValues;

/**
 * The capability class that Subjects owned from Objects.
 * Created by qiyiy on 2016/1/5.
 */
public class Capability {
    private int capabilityId;
    private int objectId;
    private String objectName;
    private int grantedSubjectId;
    private String grantedSubjectName;
    private int subjectId;
    private String subjectName;
    private String capabilityType;
    private long createdTime;
    private long lastUpdateTime;
    private String capabilityString;
    private CapabilityList capabilityList;
    private String capabilityInfo;

    public static final String CAPABILITY_TYPE_CENTRALIZED_ACL = ModelValues.CENTRALIZED_ACL;
    public static final String CAPABILITY_TYPE_DISTRIBUTED_ACL = ModelValues.DISTRIBUTED_ACL;
    public static final String CAPABILITY_TYPE_LIMITED_DISTRIBUTED_ACL = ModelValues.LIMITED_DISTRIBUTED_ACL;

    public Capability() {

    }


    public Capability(int capabilityId,
                      int objectId,
                      String objectName,
                      int grantedSubjectId,
                      String grantedSubjectName,
                      int subjectId,
                      String subjectName,
                      String capabilityType,
                      long createdTime,
                      String capabilityString) {
        this.capabilityId = capabilityId;
        this.objectId = objectId;
        this.objectName = objectName;
        this.grantedSubjectId = grantedSubjectId;
        this.grantedSubjectName = grantedSubjectName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.capabilityType = capabilityType;
        this.createdTime = createdTime;
        this.lastUpdateTime = createdTime;
        this.capabilityString = capabilityString;
        this.capabilityList = CapabilityList.capabilityStringToList(capabilityString);
        updateInfo();
    }

    public int getCapabilityId() {
        return capabilityId;
    }

    public int getObjectId() {
        return objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public int getGrantedSubjectId() {
        return grantedSubjectId;
    }

    public String getGrantedSubjectName() {
        return grantedSubjectName;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getCapabilityType() {
        return capabilityType;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public String getCapabilityString() {
        return capabilityString;
    }

    public CapabilityList getCapabilityList() {
        return capabilityList;
    }

    public String getCapabilityInfo() {
        return capabilityInfo;
    }

    public void setCapabilityType(String capabilityType) {
        this.capabilityType = capabilityType;
        updateInfo();
    }

    public void setCapabilityString(String capabilityString) {
        this.capabilityString = capabilityString;
        updateInfo();
    }

    public void setCapabilityList(CapabilityList capabilityList) {
        this.capabilityList = capabilityList;
        updateInfo();
    }

    private void updateInfo() {
        this.capabilityInfo = capabilityType + ": " + grantedSubjectName +
                " granted " + subjectName + " with " + objectName + ":" + capabilityString;
    }
}