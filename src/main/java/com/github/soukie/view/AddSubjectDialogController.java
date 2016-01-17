package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Date;

/**
 * Created by qiyiy on 2016/1/17.
 */
public class AddSubjectDialogController {

    private MainAPP mainAPP;

    @FXML
    private TextField subjectIdTextField;
    @FXML
    private TextField subjectNameTextField;
    @FXML
    private TextField subjectPassTextFiled;
    @FXML
    private Button sureButton;
    @FXML
    private Button cancelButton;

    private Stage dialogStage;

    public AddSubjectDialogController() {

    }

    @FXML
    public void initialize() {
        sureButton.setOnAction(event -> {
            mainAPP.dacManagement.createSubject(new ACLSubject(Integer.valueOf(subjectIdTextField.getText()),
                    subjectNameTextField.getText(),
                    subjectPassTextFiled.getText(),
                    new Date().getTime()));
            mainAPP.dacAllSubjectsObservableList.add(subjectNameTextField.getText());
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

}
