package com.github.soukie.model.DACPolicy.objects;

import java.util.HashMap;

/**
 * The ACL Subject contains a subject's properties.
 * Created by qiyiy on 2016/1/5.
 */
public class ACLSubject {
    private int sId;
    private String sName;
    private String sPassword;
    private String subjectInfo;
    //Capabilities HashMap of Subject granted by another Subject.
    private HashMap<String, CapabilityOfSubject> capabilitiesMap;
    public ACLSubject() {

    }

    public ACLSubject(int sId, String sName, String sPassword, String subjectInfo) {
        this.sId = sId;
        this.sName = sName;
        this.sPassword = sPassword;
        this.subjectInfo = subjectInfo;
    }

    public ACLSubject(int sId, String sName, String sPassword, String subjectInfo, HashMap<String, CapabilityOfSubject> capabilitiesMap) {
        this.sId = sId;
        this.sName = sName;
        this.sPassword = sPassword;
        this.subjectInfo = subjectInfo;
        this.capabilitiesMap = capabilitiesMap;
    }

    public int getsId() {
        return sId;
    }

    public String getsName() {
        return sName;
    }

    public String getsPassword() {
        return sPassword;
    }

    public String getSubjectInfo() {
        return subjectInfo;
    }

    public HashMap<String, CapabilityOfSubject> getCapablitiesMap() {
        return capabilitiesMap;
    }

    public void setsId(int sId) {
        this.sId = sId;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public void setSubjectInfo(String subjectInfo) {
        this.subjectInfo = subjectInfo;
    }

    public void setCapablitiesMap(HashMap<String, CapabilityOfSubject> capabilitiesMap) {
        this.capabilitiesMap = capabilitiesMap;
    }

    /**
     * Method to change subject's name.
     * @param newName: subject' new name.
     */
    public void changeName(String newName) {
        this.sName = newName;
    }

    /**
     * Method to change subject's information.
     * @param newInfo: subject's new information.
     */
    public void changeInfo(String newInfo) {
        this.subjectInfo = newInfo;
    }

    /**
     * Method to change subject's password.
     * @param newPassword: subject's new password.
     */
    public void changePassword(String newPassword) {
        this.sPassword = newPassword;
    }

    /**
     * Method to add  a new capability to subject.
     * @param subjectId: new capability's owner subject's id as integer string.
     * @param addCapabilityItem: new capability.
     */
    public void addCapability(String subjectId, CapabilityOfSubject addCapabilityItem) {
        this.capabilitiesMap.put(subjectId, addCapabilityItem);
    }

    /**
     * Method to add some new capabilities to subject.
     * @param addCapabilityItems: new capabilities HashMap.
     */
    public void addCapabilities(HashMap<String, CapabilityOfSubject> addCapabilityItems) {
        this.capabilitiesMap.putAll(addCapabilityItems);
    }

    /**
     * Method to modify capability of subject.
     * @param subjectId: capability's owner subject's id as integer string.
     * @param newCapabilityItem: modified capability.
     */
    public void modifyCapability(String subjectId, CapabilityOfSubject newCapabilityItem) {
        this.capabilitiesMap.put(subjectId, newCapabilityItem);
    }

    /**
     * Method to delete capability of subject.
     * @param subjectId: delete capability's owner subject's id as integer string.
     * @param removeCapabilityItem: capability will be removed.
     */
    public void removeCapability(String subjectId, CapabilityOfSubject removeCapabilityItem) {
        this.capabilitiesMap.remove(subjectId);
    }

}
