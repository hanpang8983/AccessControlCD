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
    public Parent splash_login_window;
    public Parent main_window;

    private SystemAdminUser systemAdminUser;
    private String systemAdminUserPropertiesFilePath = "res/properties/system_admin_user.ini";

    private SplashLoginController splashLoginLayoutLoaderController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initSystemLoginAdminUserStatus();
        initSplashLoginWindow();
        initMainWindow();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(splash_login_window, 500, 400));
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.getIcons().add(new Image("file:res/image/Soukie_l.png"));
        primaryStage.show();


    }

    private void initSystemLoginAdminUserStatus() {
        systemAdminUser = SystemUserManagement.getDefaultSystemAdminUser(systemAdminUserPropertiesFilePath);
    }

    private void initSplashLoginWindow()  {

        try {
            FXMLLoader splashLoginLayoutLoader = new FXMLLoader();
            splashLoginLayoutLoader.setLocation(MainAPP.class.getResource("view/splash_login_window.fxml"));
            this.splash_login_window = splashLoginLayoutLoader.load();
            this.splashLoginLayoutLoaderController = (SplashLoginController) splashLoginLayoutLoader.getController();
            splashLoginLayoutLoaderController.setMainAPP(this);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initMainWindow() {
        FXMLLoader mainWindowLayoutLoader = new FXMLLoader(getClass().getResource("view/main_window.fxml"));
        try {
            main_window = mainWindowLayoutLoader.load();
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
