package com.github.soukie.model.DACPolicy.objects;


import java.util.Date;

/**
 * Created by qiyiy on 2016/1/10.
 */
public class BlackToken {
    private int blackTokenId;
    private int objectId;
    private int grantedSubjectId;
    private int subjectId;
    private long createdTime;
    private long lastUpdateTime;
    private String capabilityString;
    private boolean blackToken;

    public BlackToken(int blackTokenId,
                      int objectId,
                      int grantedSubjectId,
                      int subjectId,
                      long createdTime,
                      String capabilityString,
                      boolean blackToken) {
        this.blackTokenId = blackTokenId;
        this.objectId = objectId;
        this.grantedSubjectId = grantedSubjectId;
        this.subjectId = subjectId;
        this.createdTime = createdTime;
        this.lastUpdateTime = createdTime;
        this.capabilityString = capabilityString;
        this.blackToken = blackToken;
    }

    public int getBlackTokenId() {
        return blackTokenId;
    }

    public int getObjectId() {
        return objectId;
    }

    public int getGrantedSubjectId() {
        return grantedSubjectId;
    }

    public int getSubjectId() {
        return subjectId;
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

    public boolean isBlackToken() {
        return blackToken;
    }

    public void setBlackToken(boolean blackToken) {
        this.blackToken = blackToken;
        this.lastUpdateTime = new Date().getTime();
    }

}
