package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.DACPolicy.objects.CapabilityProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/17.
 */
public class DeleteSubjectDialogController {

    private MainAPP mainAPP;
    private Stage dialogStage;

    @FXML
    private ChoiceBox<String> subjectChoiceBox;
    @FXML
    private Button sureButton;
    @FXML
    private Button cancelButton;

    public DeleteSubjectDialogController() {

    }

    @FXML
    private void initialize() {
        sureButton.setOnAction(event -> {
            mainAPP.dacManagement.deleteSubject(mainAPP.dacDatabaseOperation.querySubjectIdByName(subjectChoiceBox.getValue()));
            ArrayList<Capability> newCapabilities = mainAPP.dacDatabaseOperation.queryAllCapabilities();
            mainAPP.dacAllCapabilitiesObservableList.clear();
            mainAPP.dacAllCapabilitiesPropertyObservableList.clear();
            if (newCapabilities != null) {

                mainAPP.dacAllCapabilitiesObservableList.addAll(newCapabilities.stream().map(Capability::getCapabilityId).collect(Collectors.toList()));

                mainAPP.dacAllCapabilitiesPropertyObservableList.addAll(CapabilityProperty.capabilitiesToCapabilitiesProperty(newCapabilities));
            }
            mainAPP.dacAllSubjectsObservableList.removeAll(subjectChoiceBox.getValue());
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
        subjectChoiceBox.setItems(mainAPP.dacAllSubjectsObservableList);
    }


}
