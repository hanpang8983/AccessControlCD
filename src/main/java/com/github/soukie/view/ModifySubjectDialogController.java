package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.security.SecurityEncode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Created by qiyiy on 2016/1/17.
 */
public class ModifySubjectDialogController {
    private MainAPP mainAPP;
    private Stage dialogStage;

    @FXML
    private Label subjectIdLabel;
    @FXML
    private TextField subjectNameTextField;
    @FXML
    private TextField subjectPasswordTextField;
    @FXML
    private ChoiceBox<String> allSubjectsChoiceBox;
    @FXML
    private Button sureButton;
    @FXML
    private Button cancelButton;

    public ModifySubjectDialogController() {

    }

    @FXML
    public void initialize() {
        sureButton.setOnAction(event -> {
            if (subjectIdLabel.getText()!=null && subjectNameTextField.getText() != null && subjectPasswordTextField != null) {
                try {
                    mainAPP.dacManagement.modifySubject(mainAPP.databaseOperation.querySubjectIdByName(subjectNameTextField.getText()),
                            subjectNameTextField.getText(),
                            SecurityEncode.encoderByMd5(SecurityEncode.encoderByMd5(subjectPasswordTextField.getText())),
                            "",
                            new Date().getTime());
                } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mainAPP.dacAllSubjectsObservableList.remove(allSubjectsChoiceBox.getValue());
                mainAPP.dacAllSubjectsObservableList.add(subjectNameTextField.getText());
            }
            dialogStage.close();

        });
        cancelButton.setOnAction(event -> dialogStage.close());
    }

    public void intiDialogValues() {
        allSubjectsChoiceBox.setItems(mainAPP.dacAllSubjectsObservableList);


    }
    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}
