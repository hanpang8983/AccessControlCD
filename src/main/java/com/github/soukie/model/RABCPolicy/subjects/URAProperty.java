package com.github.soukie.model.RABCPolicy.subjects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/20.
 */
public class URAProperty {
    private StringProperty userName;
    private StringProperty roleName;

    public URAProperty(String userName, String roleName) {
        this.userName = new SimpleStringProperty(userName);
        this.roleName = new SimpleStringProperty(roleName);
    }

    public StringProperty userNameProperty() {
        if (userName == null) {
            userName = new SimpleStringProperty(this, "userName");
        }
        return userName;
    }
    public String getUserName() {
        return userNameProperty().get();
    }

    public void setUserName(String userName) {
        userNameProperty().setValue(userName);
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

    public static URAProperty uraToURAProperty(URA ura) {
        return new URAProperty(ura.getUserName(), ura.getRoleName());
    }

    public static ArrayList<URAProperty> urasToURAProperties(ArrayList<URA> uras) {
        return uras.stream().map(URAProperty::uraToURAProperty).collect(Collectors.toCollection(ArrayList::new));
    }

    public static URA uraPropertyToURA(URAProperty uraProperty) {
        return new URA(uraProperty.getUserName(), uraProperty.getRoleName());
    }

    public static ArrayList<URA> uraPropertiesToURAs(ArrayList<URAProperty> uraProperties) {
        return uraProperties.stream().map(URAProperty::uraPropertyToURA).collect(Collectors.toCollection(ArrayList::new));
    }

}
