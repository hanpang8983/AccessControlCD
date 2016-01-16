package com.github.soukie.model.DACPolicy.objects;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/16.
 */
public class CapabilityProperty {
    private IntegerProperty capabilityId;
    private IntegerProperty objectId;
    private StringProperty objectName;
    private IntegerProperty grantedSubjectId;
    private StringProperty grantedSubjectName;
    private IntegerProperty subjectId;
    private StringProperty subjectName;
    private LongProperty createdTime;
    private LongProperty lastUpdateTime;
    private StringProperty capabilityString;
    private StringProperty capabilityInfo;

    public CapabilityProperty(int capabilityId,
                              int objectId,
                              String objectName,
                              int grantedSubjectId,
                              String grantedSubjectName,
                              int subjectId,
                              String subjectName,
                              long createdTime,
                              long lastUpdateTime,
                              String capabilityString,
                              String capabilityInfo) {
        this.capabilityId = new SimpleIntegerProperty(capabilityId);
        this.objectId = new SimpleIntegerProperty(objectId);
        this.objectName = new SimpleStringProperty(objectName);
        this.grantedSubjectId = new SimpleIntegerProperty(grantedSubjectId);
        this.grantedSubjectName = new SimpleStringProperty(grantedSubjectName);
        this.subjectId = new SimpleIntegerProperty(subjectId);
        this.subjectName = new SimpleStringProperty(subjectName);
        this.createdTime = new SimpleLongProperty(createdTime);
        this.lastUpdateTime = new SimpleLongProperty(lastUpdateTime);
        this.capabilityString = new SimpleStringProperty(capabilityString);
        this.capabilityInfo = new SimpleStringProperty(capabilityInfo);
    }

    public int getCapabilityId() {
        return capabilityIdProperty().get();
    }

    public void setCapabilityId(int capabilityId) {
        capabilityIdProperty().setValue(capabilityId);
    }

    public IntegerProperty capabilityIdProperty() {
        if (capabilityId == null) {
            capabilityId = new SimpleIntegerProperty(this, "capabilityId");
        }
        return capabilityId;
    }

    public int getObjectId() {
        return objectIdProperty().get();
    }

    public void setObjectId(int objectId) {
        objectIdProperty().setValue(objectId);
    }

    public IntegerProperty objectIdProperty() {
        if (objectId == null) {
            objectId = new SimpleIntegerProperty(this, "objectId");
        }
        return objectId;
    }

    public String getObjectName() {
        return objectNameProperty().get();
    }

    public void setObjectName(String objectName) {
        objectNameProperty().setValue(objectName);
    }

    public StringProperty objectNameProperty() {
        if (objectName == null) {
            objectName = new SimpleStringProperty(this, "objectName");
        }
        return objectName;
    }

    public int getGrantedSubjectId() {
        return grantedSubjectIdProperty().get();
    }

    public void setGrantedSubjectId(int grantedSubjectId) {
        grantedSubjectIdProperty().set(grantedSubjectId);
    }

    public IntegerProperty grantedSubjectIdProperty() {
        if (grantedSubjectId == null) {
            grantedSubjectId = new SimpleIntegerProperty(this, "grantedSubjectId");
        }
        return grantedSubjectId;
    }

    public String getGrantedSubjectName() {
        return grantedSubjectNameProperty().get();
    }

    public void setGrantedSubjectName(String grantedSubjectName) {
        grantedSubjectNameProperty().set(grantedSubjectName);
    }

    public StringProperty grantedSubjectNameProperty() {
        if (grantedSubjectName == null) {
            grantedSubjectName = new SimpleStringProperty(this, "grantedSubjectName");
        }
        return grantedSubjectName;
    }

    public int getSubjectId() {
        return subjectIdProperty().get();
    }

    public void setSubjectId(int subjectId) {
        subjectIdProperty().set(subjectId);
    }

    public IntegerProperty subjectIdProperty() {
        if (subjectId == null) {
            subjectId = new SimpleIntegerProperty(this, "subjectId");
        }
        return subjectId;
    }

    public String getSubjectName() {
        return subjectNameProperty().get();
    }

    public void setSubjectName(String subjectName) {
        subjectNameProperty().set(subjectName);
    }

    public StringProperty subjectNameProperty() {
        if (subjectName == null) {
            subjectName = new SimpleStringProperty(this, "subjectName");
        }
        return subjectName;
    }


    public LongProperty createdTimeProperty() {
        if (createdTime == null) {
            createdTime = new SimpleLongProperty(this, "createdTime");
        }
        return createdTime;
    }

    public long getCreatedTime() {
        return createdTimeProperty().get();
    }

    public void setCreatedTime(long createdTime) {
        createdTimeProperty().set(createdTime);
    }

    public LongProperty lastUpdateTimeProperty() {
        if (lastUpdateTime == null) {
            lastUpdateTime = new SimpleLongProperty(this, "lastUpdateTime");
        }
        return lastUpdateTime;
    }

    public long getLastUpdateTime() {
        return lastUpdateTimeProperty().get();
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        lastUpdateTimeProperty().set(lastUpdateTime);
    }

    public StringProperty capabilityStringProperty() {
        if (capabilityString == null) {
            capabilityString = new SimpleStringProperty(this, "capabilityString");
        }
        return capabilityString;
    }

    public void setCapabilityString(String capabilityString) {
        capabilityStringProperty().set(capabilityString);
    }
    public String getCapabilityString() {
        return capabilityStringProperty().get();
    }

    public StringProperty capabilityInfoProperty() {
        if (capabilityInfo == null) {
            capabilityInfo = new SimpleStringProperty(this, "capabilityInfo");
        }
        return capabilityInfo;
    }

    public void setCapabilityInfo(String capabilityInfo) {
        capabilityInfoProperty().set(capabilityInfo);
    }
    public String getCapabilityInfo() {
        return capabilityInfoProperty().get();
    }


    public static CapabilityProperty capabilityToCapabilityProperty(Capability capability) {
        return new CapabilityProperty(capability.getCapabilityId(),
                capability.getObjectId(),
                capability.getObjectName(),
                capability.getGrantedSubjectId(),
                capability.getGrantedSubjectName(),
                capability.getSubjectId(),
                capability.getSubjectName(),
                capability.getCreatedTime(),
                capability.getLastUpdateTime(),
                capability.getCapabilityString(),
                capability.getCapabilityInfo());
    }

    public static ArrayList<CapabilityProperty> capabilitiesToCapabilitiesProperty(ArrayList<Capability> capabilityArrayList) {
        return capabilityArrayList.stream().map(CapabilityProperty::capabilityToCapabilityProperty).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Capability> capabilitiesPropertyToCapabilities(ArrayList<CapabilityProperty> capabilitiesPropertyArrayList) {
        return capabilitiesPropertyArrayList.stream().map(CapabilityProperty::capabilityPropertyToCapability).collect(Collectors.toCollection(ArrayList<Capability>::new));
    }

    public static Capability capabilityPropertyToCapability(CapabilityProperty capabilityProperty) {
        return new Capability(capabilityProperty.getCapabilityId(),
                capabilityProperty.getObjectId(),
                capabilityProperty.getObjectName(),
                capabilityProperty.getGrantedSubjectId(),
                capabilityProperty.getGrantedSubjectName(),
                capabilityProperty.getSubjectId(),
                capabilityProperty.getSubjectName(),
                capabilityProperty.getCreatedTime(),
                capabilityProperty.getCapabilityString());
    }


}
