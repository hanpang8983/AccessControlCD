package com.github.soukie.model.RABCPolicy.subjects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/20.
 */
public class PRAProperty {
    private StringProperty roleName;
    private StringProperty permissionName;

    public PRAProperty(String roleName, String permissionName) {
        this.roleName = new SimpleStringProperty(roleName);
        this.permissionName = new SimpleStringProperty(permissionName);
    }

    public StringProperty roleNameProperty() {
        if (roleName == null) {
            roleName = new SimpleStringProperty(this, "roleName");
        }
        return roleName;
    }

    public String getRoleName() {
        return roleNameProperty().get();
    }

    public void setRoleName(String roleName) {
        roleNameProperty().setValue(roleName);
    }

    public StringProperty permissionNameProperty() {
        if (permissionName == null) {
            permissionName = new SimpleStringProperty(this, "permissionName");
        }
        return permissionName;
    }

    public String getPermissionName() {
        return permissionNameProperty().get();
    }

    public void setPermissionName(String permissionName) {
        permissionNameProperty().setValue(permissionName);
    }


    public static PRAProperty praToPRAProperty(PRA pra) {
        return new PRAProperty(pra.getRoleName(), pra.getPermissionName());
    }

    public static ArrayList<PRAProperty> prasToPRAProperties(ArrayList<PRA> pras) {
        return pras.stream().map(PRAProperty::praToPRAProperty).collect(Collectors.toCollection(ArrayList::new));
    }

    public static PRA praPropertyToPRA(PRAProperty praProperty) {
        return new PRA(praProperty.getRoleName(), praProperty.getPermissionName());
    }

    public ArrayList<PRA> praPropertiesToPRAs(ArrayList<PRAProperty> praProperties) {
        return praProperties.stream().map(PRAProperty::praPropertyToPRA).collect(Collectors.toCollection(ArrayList::new));
    }
}
