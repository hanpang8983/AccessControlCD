package com.github.soukie;

import com.github.soukie.model.DACPolicy.DACManagement;
import com.github.soukie.model.DACPolicy.objects.*;
import com.github.soukie.model.ModelValues;
import com.github.soukie.model.SystemUser.SystemAdminUser;
import com.github.soukie.model.SystemUser.SystemUserManagement;
import com.github.soukie.model.database.DatabaseOperation;
import com.github.soukie.view.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

/**
 *
 * Created by qiyiy on 2016/1/5.
 */
public class MainAPP extends Application {

    private Stage primaryStage;
    public Parent splashLoginWindow;
    public Parent mainWindow;

    private SystemAdminUser systemAdminUser;
    /**
     * DAC Database Handle.
     */
    public DatabaseOperation databaseOperation;
    /**
     * DAC Operation Handle.
     */
    public DACManagement dacManagement;

    public ObservableList<String> dacAllSubjectsObservableList = FXCollections.observableArrayList();
    public ObservableList<String> dacAllObjectsObservableList = FXCollections.observableArrayList();
    public ObservableList<Integer> dacAllCapabilitiesObservableList = FXCollections.observableArrayList();
    public ObservableList<CapabilityProperty> dacAllCapabilitiesPropertyObservableList = FXCollections.observableArrayList();
    public ObservableList<Integer> dacAllBlackTokensObservableList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initSystemLoginAdminUserStatus();

        initDatabaseConnectionAndManagement();

        initSplashLoginWindow();

        initMainWindow();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(splashLoginWindow, 500, 400));
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.getIcons().add(new Image("file:res/image/Soukie_l.png"));
        primaryStage.show();


    }

    private void initSystemLoginAdminUserStatus() {
        String systemAdminUserPropertiesFilePath = ModelValues.SYSTEM_ADMIN_USER_PROPERTIES_FILE_PATH;
        systemAdminUser = SystemUserManagement.getDefaultSystemAdminUser(systemAdminUserPropertiesFilePath);
    }

    private void initDatabaseConnectionAndManagement() {
        //init DatabaseOperation and DACManagement.
        databaseOperation = new DatabaseOperation(new Date().getTime());
        try {
            databaseOperation.initDatabaseConnection(ModelValues.DATABASE_MYSQL_PROPERTIES_FILE_PATH);
            dacManagement = new DACManagement(databaseOperation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        refreshDatabase();

    }

    private void initSplashLoginWindow()  {

        try {
            FXMLLoader splashLoginLayoutLoader = new FXMLLoader();
            splashLoginLayoutLoader.setLocation(MainAPP.class.getResource("view/splash_login_window.fxml"));
            splashLoginWindow = splashLoginLayoutLoader.load();
            SplashLoginController splashLoginLayoutLoaderController = splashLoginLayoutLoader.getController();
            splashLoginLayoutLoaderController.setMainAPP(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initMainWindow() {
        try {
            FXMLLoader mainWindowLayoutLoader = new FXMLLoader();
            mainWindowLayoutLoader.setLocation(MainAPP.class.getResource("view/main_window.fxml"));
            mainWindow = mainWindowLayoutLoader.load();
            MainWindowController mainWindowController = mainWindowLayoutLoader.getController();
            mainWindowController.setMainAPP(this);
            mainWindowController.initInterface();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showModifyAdminInfoDialog() {
        try {
            FXMLLoader modifyAdminInfoWindowLayoutLoader = new FXMLLoader();
            modifyAdminInfoWindowLayoutLoader.setLocation(MainAPP.class.getResource("view/modify_admin_info_dialog.fxml"));
            Parent modifyAdminInfoDialog = modifyAdminInfoWindowLayoutLoader.load();
            ModifyAdminInfoDialogController modifyAdminInfoDialogController = modifyAdminInfoWindowLayoutLoader.getController();
            modifyAdminInfoDialogController.setMainAPP(this);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modify Admin");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(modifyAdminInfoDialog));
            modifyAdminInfoDialogController.setDialogStage(dialogStage);
            modifyAdminInfoDialogController.initDialogValues();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showAddSubjectDialog() {
        try {
            FXMLLoader addSubjectDialogLoader = new FXMLLoader();
            addSubjectDialogLoader.setLocation(MainAPP.class.getResource("view/add_subject_dialog.fxml"));
            Parent addSubjectDialog = addSubjectDialogLoader.load();
            AddSubjectDialogController addSubjectDialogController = addSubjectDialogLoader.getController();
            addSubjectDialogController.setMainAPP(this);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Subject");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(addSubjectDialog));
            addSubjectDialogController.setDialogStage(dialogStage);
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showDeleteSubjectDialog() {
        try {
            FXMLLoader deleteSubjectDialogLoader = new FXMLLoader();
            deleteSubjectDialogLoader.setLocation(MainAPP.class.getResource("view/delete_subject_dialog.fxml"));
            Parent deleteSubjectDialog = deleteSubjectDialogLoader.load();
            DeleteSubjectDialogController deleteSubjectDialogController = deleteSubjectDialogLoader.getController();
            deleteSubjectDialogController.setMainAPP(this);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Delete Subject");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(deleteSubjectDialog));
            deleteSubjectDialogController.setDialogStage(dialogStage);
            deleteSubjectDialogController.initDialogValues();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showModifySubjectDialog() {
        try {
            FXMLLoader modifySubjectDialogLoader = new FXMLLoader();
            modifySubjectDialogLoader.setLocation(MainAPP.class.getResource("view/modify_subject_dialog.fxml"));
            Parent modifySubjectDialog = modifySubjectDialogLoader.load();
            ModifySubjectDialogController modifySubjectDialogController = modifySubjectDialogLoader.getController();
            modifySubjectDialogController.setMainAPP(this);
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Modify Subject");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene(new Scene(modifySubjectDialog));
            modifySubjectDialogController.setDialogStage(dialogStage);
            modifySubjectDialogController.intiDialogValues();
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshDatabase() {
        //init dacAllSubjectsObservableList
        ArrayList<ACLSubject> dacAllSubjects = databaseOperation.queryAllSubjects();
        dacAllSubjectsObservableList.clear();
        if (dacAllSubjects != null) {
            dacAllSubjectsObservableList.addAll(dacAllSubjects.stream().map(ACLSubject::getName).collect(Collectors.toList()));
        } else {
            dacAllSubjectsObservableList.addAll("SubjectDemo");
        }

        //init dacAllObjectsObservableList
        ArrayList<ACLObject> dacAllObjects = databaseOperation.queryAllObjects();
        dacAllObjectsObservableList.clear();
        if (dacAllObjects != null) {
            dacAllObjectsObservableList.addAll(dacAllObjects.stream().map(ACLObject::getName).collect(Collectors.toList()));
        } else {
            dacAllObjectsObservableList.addAll("ObjectDemo");
        }

        //init dacAllCapabilitiesObservableList and dacAllCapabilitiesPropertyObservableList
        ArrayList<Capability> dacAllCapabilities = databaseOperation.queryAllCapabilities();
        ArrayList<CapabilityProperty> dacAllCapabilitiesProperty;
        dacAllCapabilitiesObservableList.clear();
        dacAllCapabilitiesPropertyObservableList.clear();
        if (dacAllCapabilities != null) {
            dacAllCapabilitiesObservableList.addAll(dacAllCapabilities.stream().map(Capability::getCapabilityId).collect(Collectors.toList()));
            dacAllCapabilitiesProperty = CapabilityProperty.capabilitiesToCapabilitiesProperty(dacAllCapabilities);
            dacAllCapabilitiesPropertyObservableList.addAll(dacAllCapabilitiesProperty);
        } else {
            dacAllCapabilitiesObservableList.addAll();
            dacAllCapabilitiesPropertyObservableList.addAll();
        }

        //init dacAllBlackTokensObservableList
        ArrayList<BlackToken> dacAllBlackTokens = databaseOperation.queryAllBlackTokens();
        dacAllBlackTokensObservableList.clear();
        if (dacAllBlackTokens != null) {
            dacAllBlackTokensObservableList.addAll(dacAllBlackTokens.stream().map(BlackToken::getBlackTokenId).collect(Collectors.toList()));
        } else {
            dacAllBlackTokensObservableList.addAll(1);
        }
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public SystemAdminUser getSystemAdminUser() {
        return systemAdminUser;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
