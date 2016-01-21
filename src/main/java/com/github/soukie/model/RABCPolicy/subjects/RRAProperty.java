package com.github.soukie.model.RABCPolicy.subjects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/20.
 */
public class RRAProperty {
    private StringProperty fatherRoleName;
    private StringProperty childrenRoleName;

    public RRAProperty(String fatherRoleName, String childrenRoleName) {
        this.fatherRoleName = new SimpleStringProperty(fatherRoleName);
        this.childrenRoleName = new SimpleStringProperty(childrenRoleName);
    }

    public StringProperty fatherRoleNameProperty() {
        if (fatherRoleName == null) {
            fatherRoleName = new SimpleStringProperty(this, "fatherRoleName");
        }
        return fatherRoleName;
    }

    public String getFatherRoleName() {
        return fatherRoleNameProperty().get();
    }

    public void setFatherRoleName(String fatherRoleName) {
        fatherRoleNameProperty().setValue(fatherRoleName);
    }

    public StringProperty childrenRoleNameProperty() {
        if (childrenRoleName == null) {
            childrenRoleName = new SimpleStringProperty(this, "childrenRoleName");
        }
        return childrenRoleName;
    }

    public String getChildrenRoleName() {
        return childrenRoleNameProperty().get();
    }

    public void setChildrenRoleName(String childrenRoleName) {
        childrenRoleNameProperty().setValue(childrenRoleName);
    }

    public static RRAProperty rraToRRAProperty(RRA rra) {
        return new RRAProperty(rra.getFatherRoleName(), rra.getChildrenRoleName());
    }

    public static ArrayList<RRAProperty> rrasToRRAProperties(ArrayList<RRA> rras) {
        return rras.stream().map(RRAProperty::rraToRRAProperty).collect(Collectors.toCollection(ArrayList::new));
    }

    public static RRA rraPropertyToRRA(RRAProperty rraProperty) {
        return new RRA(rraProperty.getFatherRoleName(), rraProperty.getFatherRoleName());
    }

    public static ArrayList<RRA> rraPropertiesToRRAs(ArrayList<RRAProperty> rraProperties) {
        return rraProperties.stream().map(RRAProperty::rraPropertyToRRA).collect(Collectors.toCollection(ArrayList::new));
    }
}
