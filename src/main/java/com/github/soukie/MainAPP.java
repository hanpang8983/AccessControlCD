package com.github.soukie;

import com.github.soukie.model.SystemUser.SystemAdminUser;
import com.github.soukie.model.SystemUser.SystemUserManagement;
import com.github.soukie.view.SplashLoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
        String systemAdminUserPropertiesFilePath = "res/properties/system_admin_user.ini";
        systemAdminUser = SystemUserManagement.getDefaultSystemAdminUser(systemAdminUserPropertiesFilePath);
    }

    private void initSplashLoginWindow()  {

        try {
            FXMLLoader splashLoginLayoutLoader = new FXMLLoader();
            splashLoginLayoutLoader.setLocation(getClass().getResource("view/splash_login_window.fxml"));
            this.splashLoginWindow = splashLoginLayoutLoader.load();
            SplashLoginController splashLoginLayoutLoaderController = splashLoginLayoutLoader.getController();
            splashLoginLayoutLoaderController.setMainAPP(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initMainWindow() {
        try {
            FXMLLoader mainWindowLayoutLoader = new FXMLLoader();
            mainWindowLayoutLoader.setLocation(getClass().getResource("view/main_window.fml"));
            mainWindow = mainWindowLayoutLoader.load();
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

    public static void main(String[] args) {
        launch(args);
    }
}
