package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.security.SecurityEncode;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qiyiy on 2016/1/5.
 */
public class SplashLoginController {
    @FXML
    private Button loginButton;
    @FXML
    private Button forgetPassButton;
    @FXML
    private ImageView userImageView;
    @FXML
    private ImageView passImageView;
    @FXML
    private TextField userNameTestFiled;
    @FXML
    private PasswordField userPasswordFiled;

    private MainAPP mainAPP;


    public SplashLoginController() {

    }

    @FXML
    private void initialize() {

        loginButton.setOnMouseClicked(event -> {
            try {
                boolean userNameCheck = userNameTestFiled.getText().equalsIgnoreCase(mainAPP.getSystemAdminUser().adminName);
                boolean userPassCheck = SecurityEncode.encoderByMd5(SecurityEncode.encoderByMd5(userPasswordFiled.getText())).
                        equalsIgnoreCase(mainAPP.getSystemAdminUser().getAdminPass());
                if (true) {
                    mainAPP.getPrimaryStage().setTitle("Access Control Demo");
                    mainAPP.getPrimaryStage().setScene(new Scene(mainAPP.mainWindow, 1200, 800));
                    mainAPP.getPrimaryStage().getIcons().add(new Image("file:res/image/Soukie_l.png"));
                    mainAPP.getPrimaryStage().show();
                } else {
                    Dialog dialog = new Dialog();
                    dialog.setTitle("InputError");
                    dialog.setContentText("Wrong user name or pass, please check an try again.");
                    dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                    dialog.showAndWait();
                }
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        forgetPassButton.setOnMouseClicked(event -> {
            Dialog dialog = new Dialog();
            dialog.setTitle("Help");
            dialog.setContentText("Forgot password? You can contact qiyiyiqiqi@gmail for help from administrator.");
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.showAndWait();
        });

    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }
}
