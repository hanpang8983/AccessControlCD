package com.github.soukie.model.DACPolicy.objects;

import com.github.soukie.model.DACPolicy.CapabilityList;
import com.github.soukie.model.ModelValues;

import java.util.Date;

/**
 * The capability class that Subjects owned from Objects.
 * Created by qiyiy on 2016/1/5.
 */
public class Capability {
    private final int capabilityId;
    private final int objectId;
    private String objectName;
    private final int grantedSubjectId;
    private String grantedSubjectName;
    private final int subjectId;
    private String subjectName;
    private final long createdTime;
    private long lastUpdateTime;
    private String capabilityString;
    private CapabilityList capabilityList;
    private String capabilityInfo;

    public static final String CAPABILITY_TYPE_CENTRALIZED_ACL = ModelValues.CENTRALIZED_ACL;
    public static final String CAPABILITY_TYPE_DISTRIBUTED_ACL = ModelValues.DISTRIBUTED_ACL;
    public static final String CAPABILITY_TYPE_LIMITED_DISTRIBUTED_ACL = ModelValues.LIMITED_DISTRIBUTED_ACL;

    public Capability(int capabilityId,
                      int objectId,
                      String objectName,
                      int grantedSubjectId,
                      String grantedSubjectName,
                      int subjectId,
                      String subjectName,
                      long createdTime,
                      String capabilityString) {
        this.capabilityId = capabilityId;
        this.objectId = objectId;
        this.objectName = objectName;
        this.grantedSubjectId = grantedSubjectId;
        this.grantedSubjectName = grantedSubjectName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
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


    public void setCapabilityString(String capabilityString) {
        this.capabilityString = capabilityString;
        updateInfo();
    }

    public void setCapabilityList(CapabilityList capabilityList) {
        this.capabilityList = capabilityList;
        updateInfo();
    }

    private void updateInfo() {
        this.capabilityInfo = grantedSubjectName +
                " granted " + subjectName + " with " + objectName + ":" + capabilityString;
    }

    public static Capability makeCapability(ACLSubject grantedSubject,
                                            ACLSubject subject,
                                            ACLObject object,
                                            String capabilityString) {
        return new Capability(grantedSubject.getId() * 1000000 + subject.getId() * 1000 +
                object.getId() + CapabilityList.capabilityStringToIntValue(capabilityString),

                object.getId(),
                object.getName(),
                subject.getId(),
                subject.getName(),
                subject.getId(),
                subject.getName(),
                new Date().getTime(),
                capabilityString);
    }
}
