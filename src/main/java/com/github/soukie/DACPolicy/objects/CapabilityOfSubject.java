package com.github.soukie.DACPolicy.objects;

import com.github.soukie.DACPolicy.values.CapabilityList;
import com.github.soukie.DACPolicy.values.DACValues;

/**
 * The capability class that Subjects owned from Objects.
 * Created by qiyiy on 2016/1/5.
 */
public class CapabilityOfSubject {
    private int capabilityId;
    private int objectId;
    private String objectName;
    private int grantedSubjectId;
    private String grantedSubjectName;
    private int subjectId;
    private String subjectName;
    private String capabilityType;
    private String capabilityString;
    private CapabilityList capabilityList;
    private String capabilityInfo;

    public static final String CAPABILITY_TYPE_CENTRALIZED_ACL = DACValues.CENTRALIZED_ACL;
    public static final String CAPABILITY_TYPE_DISTRIBUTED_ACL = DACValues.DISTRIBUTED_ACL;
    public static final String CAPABILITY_TYPE_LIMITED_DISTRIBUTED_ACL = DACValues.LIMITED_DISTRIBUTED_ACL;


    public CapabilityOfSubject(int capabilityId,
                               int objectId,
                               String objectName,
                               int grantedSubjectId,
                               String grantedSubjectName,
                               int subjectId,
                               String subjectName,
                               String capabilityType,
                               CapabilityList capabilityList) {
        this.capabilityId = capabilityId;
        this.objectId = objectId;
        this.objectName = objectName;
        this.grantedSubjectId = grantedSubjectId;
        this.grantedSubjectName = grantedSubjectName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.capabilityType = capabilityType;
        this.capabilityString = CapabilityList.capabilityListToString(capabilityList);
        this.capabilityList = capabilityList;
        this.capabilityInfo = capabilityType + ": " + grantedSubjectName +
                " granted " + subjectName + " with " + objectName + ":"
                + CapabilityList.capabilityListToString(capabilityList);
    }

    public CapabilityOfSubject(int capabilityId,
                               int objectId,
                               String objectName,
                               int grantedSubjectId,
                               String grantedSubjectName,
                               int subjectId,
                               String subjectName,
                               String capabilityType,
                               String capabilityString) {
        this.capabilityId = capabilityId;
        this.objectId = objectId;
        this.objectName = objectName;
        this.grantedSubjectId = grantedSubjectId;
        this.grantedSubjectName = grantedSubjectName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.capabilityType = capabilityType;
        this.capabilityString = capabilityString;
        this.capabilityList = CapabilityList.capabilityStringToList(capabilityString);
        this.capabilityInfo = capabilityType + ": " + grantedSubjectName +
                " granted " + subjectName + " with " + objectName + ":" + capabilityString;
    }

    public int getCapabilityId() {
        return capabilityId;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getGrantedSubjectId() {
        return grantedSubjectId;
    }

    public String getGrantedSubjectName() {
        return grantedSubjectName;
    }

    public String getObjectName() {
        return objectName;
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

    public String getCapabilityString() {
        return capabilityString;
    }

    public String getCapabilityInfo() {
        return capabilityInfo;
    }

    public CapabilityList getCapabilityList() {
        return capabilityList;
    }

    public void modifyCapabilityList(CapabilityList newCapability) {
        this.capabilityList = newCapability;
        this.capabilityString = CapabilityList.capabilityListToString(newCapability);
        this.capabilityInfo = capabilityType + ": " + grantedSubjectName + " on " + objectName + " " + CapabilityList.capabilityListToString(newCapability);
    }

    public void modifyCapabilityString(String newCapabilityString) {
        this.capabilityString = newCapabilityString;
        this.capabilityList = CapabilityList.capabilityStringToList(newCapabilityString);
        this.capabilityInfo = capabilityType + ": " + grantedSubjectName + " 0n " + objectName + " " + newCapabilityString;
    }
}
