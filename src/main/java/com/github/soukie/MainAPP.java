package com.github.soukie;

import com.github.soukie.model.ModelValues;
import com.github.soukie.model.SystemUser.SystemAdminUser;
import com.github.soukie.model.SystemUser.SystemUserManagement;
import com.github.soukie.view.MainWindowController;
import com.github.soukie.view.ModifyAdminInfoDialogController;
import com.github.soukie.view.SplashLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by qiyiy on 2016/1/5.
 */
public class MainAPP extends Application {

    private Stage primaryStage;
    public Parent splashLoginWindow;
    public Parent mainWindow;
    public Parent modifyAdminInfoDialog;

    private SystemAdminUser systemAdminUser;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initSystemLoginAdminUserStatus();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public SystemAdminUser getSystemAdminUser() {
        return systemAdminUser;
    }
    
    public void showModifyAdminInfoDialog() {
        try {
            FXMLLoader modifyAdminInfoWindowLayoutLoader = new FXMLLoader();
            modifyAdminInfoWindowLayoutLoader.setLocation(MainAPP.class.getResource("view/modify_admin_info_dialog.fxml"));
            modifyAdminInfoDialog = modifyAdminInfoWindowLayoutLoader.load();
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

    public static void main(String[] args) {
        launch(args);
    }
}
