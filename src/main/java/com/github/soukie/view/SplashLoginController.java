package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.security.SecurityEncode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by qiyiy on 2016/1/5.
 */
public class SplashLoginController {
    @FXML
    private Button login_button;
    @FXML
    private Button forget_pass_button;
    @FXML
    private ImageView user_imageView;
    @FXML
    private ImageView pass_imageView;
    @FXML
    private TextField user_name_testFiled;
    @FXML
    private PasswordField user_passwordFiled;

    private MainAPP mainAPP;

    /**
     * Constructor
     */
    public SplashLoginController() {

    }

    @FXML
    private void initialize() {

        login_button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    boolean userNameCheck = user_name_testFiled.getText().equalsIgnoreCase(mainAPP.getSystemAdminUser().adminName);
                    boolean userPassCheck = SecurityEncode.encoderByMd5(SecurityEncode.encoderByMd5(user_passwordFiled.getText())).
                            equalsIgnoreCase(mainAPP.getSystemAdminUser().getAdminPass());
                    if (userNameCheck && userPassCheck) {
                        mainAPP.getPrimaryStage().setTitle("Access Control Demo");
                        mainAPP.getPrimaryStage().setScene(new Scene(mainAPP.main_window, 900, 600));
                        mainAPP.getPrimaryStage().getIcons().add(new Image("file:res/image/Soukie_l.png"));
                        mainAPP.getPrimaryStage().show();
                    } else {
                        Dialog dialog = new Dialog();
                        dialog.setTitle("Prompt");
                        dialog.setContentText("wrong user name or pass");
                        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                        //dialog.setOnCloseRequest(event1 -> dialog.close());
                        dialog.showAndWait();
                    }
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }
}
