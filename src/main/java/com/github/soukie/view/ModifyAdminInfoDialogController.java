package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.ModelValues;
import com.github.soukie.model.SystemUser.SystemUserManagement;
import com.github.soukie.model.security.SecurityEncode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by qiyiy on 2016/1/16.
 */
public class ModifyAdminInfoDialogController {

    private MainAPP mainAPP;
    @FXML
    private Label adminIdLabel;
    @FXML
    private TextField adminNameTextField;
    @FXML
    private TextField adminPasswordTextField;
    @FXML
    private Label adminCreateTimeLabel;
    @FXML
    private TextField adminEmailTextField;
    @FXML
    private TextField adminProfileTextField;
    @FXML
    private Label adminLastUpdateTimeLabel;
    @FXML
    private TextField adminWebsiteUrlTextField;
    @FXML
    private Button modifyButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;

    public ModifyAdminInfoDialogController() {

    }

    @FXML
    public void initialize() {
        modifyButton.setOnAction(event -> {
            mainAPP.getSystemAdminUser().setAdminName(adminNameTextField.getText());
            try {
                mainAPP.getSystemAdminUser().setAdminPass(SecurityEncode.encoderByMd5(SecurityEncode.encoderByMd5(adminPasswordTextField.getText())));
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mainAPP.getSystemAdminUser().setAdminEmail(adminEmailTextField.getText());
            mainAPP.getSystemAdminUser().setAdminProfileUrl(adminProfileTextField.getText());
            mainAPP.getSystemAdminUser().setAdminPersonalWebsiteUrl(adminWebsiteUrlTextField.getText());
            SystemUserManagement.modifySystemAdminUser(ModelValues.SYSTEM_ADMIN_USER_PROPERTIES_FILE_PATH,mainAPP.getSystemAdminUser());
            dialogStage.close();
        });
        cancelButton.setOnAction(event -> dialogStage.close());
    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void initDialogValues() {
        adminIdLabel.setText(String.valueOf(mainAPP.getSystemAdminUser().adminId));
        adminNameTextField.setPromptText(mainAPP.getSystemAdminUser().adminName);
        adminPasswordTextField.setPromptText("******");
        adminCreateTimeLabel.setText(new Date(mainAPP.getSystemAdminUser().adminCreateTime).toString());
        adminEmailTextField.setPromptText(mainAPP.getSystemAdminUser().adminEmail);
        adminProfileTextField.setPromptText(mainAPP.getSystemAdminUser().adminProfileUrl);
        adminLastUpdateTimeLabel.setText(new Date(mainAPP.getSystemAdminUser().adminLastUpdateTime).toString());
        adminWebsiteUrlTextField.setPromptText(mainAPP.getSystemAdminUser().adminPersonalWebsiteUrl);
    }
}
