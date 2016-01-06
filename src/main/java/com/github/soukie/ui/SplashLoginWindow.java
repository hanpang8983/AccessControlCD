package com.github.soukie.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Created by qiyiy on 2016/1/5.
 */
public class SplashLoginWindow extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("layout/splash_login_window.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.initStyle(StageStyle.UNIFIED);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);

    }
}
