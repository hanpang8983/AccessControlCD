package com.github.soukie.DACPolicy.objects;

/**
 * The ACL Object class contains all of a object' properties.
 * Created by qiyiy on 2016/1/5.
 */
public class ACLObject {
    private int oId;
    private String oName;
    private String objectInfo;
    private String createSubjectId;
    private boolean executable;

    public ACLObject() {

    }


    public ACLObject(int oId, String oName, String objectInfo, String createSubjectId, boolean executable) {
        this.oId = oId;
        this.oName = oName;
        this.objectInfo = objectInfo;
        this.createSubjectId = createSubjectId;
        this.executable = executable;
    }

    public int getoId() {
        return oId;
    }

    public String getoName() {
        return oName;
    }

    public String getObjectInfo() {
        return objectInfo;
    }

    public String getCreateSubjectId() {
        return createSubjectId;
    }

    public boolean isExecutable() {
        return executable;
    }

    public void setCreateSubjectId(String createSubjectId) {
        this.createSubjectId = createSubjectId;
    }

    public void setoId(int oId) {
        this.oId = oId;
    }

    public void setObjectInfo(String objectInfo) {
        this.objectInfo = objectInfo;
    }

    public void setoName(String oName) {
        this.oName = oName;
    }

    public void setExecutable(boolean executable) {
        this.executable = executable;
    }

    public void changeObjectName(String newName) {
        this.oName = newName;
    }

    public void changeObjectInfo(String newObjectInfo) {
        this.objectInfo = newObjectInfo;
    }

    public void changeObjectExecuteMod(Boolean newExecutableMod) {
        this.executable = newExecutableMod;
    }
}
