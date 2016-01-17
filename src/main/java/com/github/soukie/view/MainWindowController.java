package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.DACPolicy.objects.CapabilityProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/7.
 */
public class MainWindowController {

    private MainAPP mainAPP;
    /**
     * VBox is the whole main window pane.
     */
    @FXML
    private VBox mainWindowVBox;

    /**
     * Main window MenuBar and Menus and MenuItems.
     */
    @FXML
    private MenuBar mainWindowMenuBar;
    @FXML
    private Menu fileMenu;
    @FXML
    private MenuItem fileMenuClose;

    @FXML
    private Menu editMenu;
    @FXML
    private MenuItem editMenuClearLog;
    @FXML
    private MenuItem editMenuModifyAdminInfo;
    @FXML
    private MenuItem editMenuReflashDatabase;

    @FXML
    private MenuItem dacAddSubject;
    @FXML
    private MenuItem dacDeleteSubject;
    @FXML
    private MenuItem dacModifySubject;

    @FXML
    private MenuItem dacAddObject;
    @FXML
    private MenuItem dacDeleteObject;
    @FXML
    private MenuItem dacModifyObject;

    @FXML
    private MenuItem dacAddCapability;
    @FXML
    private MenuItem dacDeleteCapability;
    @FXML
    private MenuItem dacModifyCapability;

    @FXML
    private MenuItem dacAddBlackToken;
    @FXML
    private MenuItem dacDeleteBlackToken;
    @FXML
    private MenuItem dacModifyBlackToken;

    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem helpMenuAbout;
    @FXML
    private MenuItem helpMenuContact;
    @FXML
    private MenuItem helpMenuHelp;


    @FXML
    private TitledPane dacAllSubjectsTitledPane;
    @FXML
    private AnchorPane dacAllSubjectsAnchorPane;
    @FXML
    private ListView<String> dacAllSubjectsListView;
    @FXML
    private TitledPane dacAllObjectsTitledPane;
    @FXML
    private AnchorPane dacAllObjectsAnchorPane;
    @FXML
    private ListView<String> dacAllObjectsListView;
    @FXML
    private TitledPane dacALLCapabilityTitledPane;
    @FXML
    private AnchorPane dacAllCapabilitiesAnchorPane;
    @FXML
    private ListView<Integer> dacAllCapabilitiesListView;
    @FXML
    private TitledPane dacAllBlackTokensTitledPane;
    @FXML
    private AnchorPane dacAllBlackTokensAnchorPane;
    @FXML
    private ListView<Integer> dacAllBlackTokensListView;

    @FXML
    private TableView<CapabilityProperty> dacAllCapabilitiesTableView;

    @FXML
    private ScrollPane dacCapabilityDetailsScrollPane;


    /**
     * The RBAC layouts and controls.
     */


    @FXML
    private TitledPane rbacAllRolesTitledPane;
    @FXML
    private TitledPane rbacAllUsersTitledPane;
    @FXML
    private TitledPane rbacAllAuthoritiesTitledPane;


    /**
     * Running log window layouts and controls.
     */

    @FXML
    private ScrollPane runningLogScrollPane;
    @FXML
    private TextArea runningLogTextArea;


    /**
     * Empty constructor.
     */
    public MainWindowController() {

    }

    @FXML
    public void initialize() {

    }

    public void initInterface() {

        initMenuBar();
        initDACSubjects();
        initDACObjects();
        initDACCapabilities();
        initDACBlackTokens();

    }


    private void initMenuBar() {
        fileMenuClose.setOnAction(event -> {
            mainAPP.dacDatabaseOperation.closeConnection();
            System.exit(0);
        });

        editMenuClearLog.setOnAction(event -> {

        });

        editMenuModifyAdminInfo.setOnAction(event -> mainAPP.showModifyAdminInfoDialog());

        editMenuReflashDatabase.setOnAction(event -> mainAPP.refreshDatabase());

        dacAddSubject.setOnAction(event -> mainAPP.showAddSubjectDialog());

        dacDeleteSubject.setOnAction(event -> mainAPP.showDeleteSubjectDialog());

        dacModifySubject.setOnAction(event -> mainAPP.showModifySubjectDialog());

        dacAddObject.setOnAction(event -> {
            Dialog<ACLObject> dialog = new Dialog<>();
            dialog.setTitle("Add Object");
            dialog.setHeaderText("Add One Object");
            dialog.setResizable(true);

            Label objectIdLabel = new Label("Object ID: ");
            Label objectNameLabel = new Label("Object Name: ");
            Label grantedSubjectIdLabel = new Label("Granted Subject ID: ");
            Label grantedSubjectNameLabel = new Label("Granted Subject Name: ");
            Label executableLabel = new Label("Executable(y/n): ");
            TextField objectIdTextField = new TextField();
            TextField objectNameTextField = new TextField();
            TextField grantedSubjectIdTextField = new TextField();
            TextField grantedSubjectNameTextField = new TextField();
            TextField executableTextField = new TextField();

            GridPane gridPane = new GridPane();
            gridPane.add(objectIdLabel, 1, 1);
            gridPane.add(objectIdTextField, 2, 1);
            gridPane.add(objectNameLabel, 1, 2);
            gridPane.add(objectNameTextField, 2, 2);
            gridPane.add(grantedSubjectIdLabel, 1, 3);
            gridPane.add(grantedSubjectIdTextField, 2, 3);
            gridPane.add(grantedSubjectNameLabel, 1, 4);
            gridPane.add(grantedSubjectNameTextField, 2, 4);
            gridPane.add(executableLabel, 1, 5);
            gridPane.add(executableTextField, 2, 5);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOK = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOK);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOK) {
                    return new ACLObject(Integer.valueOf(objectIdTextField.getText()),
                            objectNameTextField.getText(),
                            Integer.valueOf(grantedSubjectIdTextField.getText()),
                            grantedSubjectNameTextField.getText(),
                            new Date().getTime(),
                            executableTextField.getText().equalsIgnoreCase("y"));
                }
                return null;
            });

            Optional<ACLObject> resultObject = dialog.showAndWait();
            if (resultObject.isPresent()) {
                ACLSubject grantedSubject = mainAPP.dacDatabaseOperation.queryOneSubject(Integer.valueOf(objectIdTextField.getText()));
                ACLObject object = resultObject.get();
                int createResult = mainAPP.dacManagement.createObject(grantedSubject, object);
                if (createResult == 2) {
                    mainAPP.dacAllObjectsObservableList.add(objectNameTextField.getText());
                    mainAPP.dacAllCapabilitiesObservableList.addAll(grantedSubject.getId() * 1000000 +
                            grantedSubject.getId() * 1000 +
                            object.getId() + 31);
                    mainAPP.dacAllCapabilitiesPropertyObservableList.add(
                            CapabilityProperty.capabilityToCapabilityProperty(
                                    mainAPP.dacDatabaseOperation.queryOneCapability(
                                            grantedSubject.getId() * 1000000 +
                                                    grantedSubject.getId() * 1000 +
                                                    object.getId() + 31)));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Object Succeed");
                    alert.setContentText("A new object had written into database.");
                    alert.showAndWait();
                } else if (createResult == 1) {
                    mainAPP.dacAllObjectsObservableList.add(objectNameTextField.getText());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Object Wrong");
                    alert.setContentText("A new object had written into database but subject self capability granted failed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Object Error");
                    alert.setContentText("Error TextField Input");
                    alert.showAndWait();
                }
            }

        });

        dacDeleteObject.setOnAction(event -> {
            Dialog<Boolean> dialog = new Dialog<>();
            dialog.setResizable(true);
            dialog.setTitle("Delete Object");
            dialog.setHeaderText("Delete One Object");

            Label chooseObjectLabel = new Label("Choose Object: ");
            ChoiceBox<String> allObjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllObjectsObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(chooseObjectLabel, 1, 1);
            gridPane.add(allObjectChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.dacManagement.deleteObject(mainAPP.dacDatabaseOperation.queryObjectIdByName(allObjectChoiceBox.getValue())) == 1;
                }
                return null;
            });

            Optional<Boolean> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get()) {
                    ArrayList<Capability> newCapabilities = mainAPP.dacDatabaseOperation.queryAllCapabilities();
                    mainAPP.dacAllCapabilitiesObservableList.clear();
                    mainAPP.dacAllCapabilitiesPropertyObservableList.clear();
                    if (newCapabilities != null) {
                        mainAPP.dacAllCapabilitiesObservableList.addAll(newCapabilities.stream().map(Capability::getCapabilityId).collect(Collectors.toList()));

                        mainAPP.dacAllCapabilitiesPropertyObservableList.addAll(CapabilityProperty.capabilitiesToCapabilitiesProperty(newCapabilities));
                    }
                    mainAPP.dacAllObjectsObservableList.removeAll(allObjectChoiceBox.getValue());

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Delete Succeed");
                    alert.setContentText("Delete Succeed");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Failed");
                    alert.setContentText("Delete Failed");
                    alert.showAndWait();
                }
            }
        });

        dacModifyObject.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setTitle("Modify Object");
            dialog.setHeaderText("Modify One Object");
            dialog.setResizable(true);

            Label chooseObjectLabel = new Label("Choose Object: ");
            Label objectNameLabel = new Label("Object New Name: ");
            Label objectExecutableLabel = new Label("Executeable(y/n): ");
            ChoiceBox<String> allObjectsChoiceBox = new ChoiceBox<>(mainAPP.dacAllObjectsObservableList);
            TextField objectNameTextField = new TextField();
            TextField objectExecuteableTextField = new TextField();

            GridPane gridPane = new GridPane();
            gridPane.add(chooseObjectLabel, 1, 1);
            gridPane.add(allObjectsChoiceBox, 2, 1);
            gridPane.add(objectNameLabel, 1, 3);
            gridPane.add(objectNameTextField, 2, 3);
            gridPane.add(objectExecutableLabel, 1, 4);
            gridPane.add(objectExecuteableTextField, 2, 4);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.dacManagement.modifyObject(mainAPP.dacDatabaseOperation.queryObjectIdByName(allObjectsChoiceBox.getValue()),
                            objectNameTextField.getText(),
                            "",
                            new Date().getTime(),
                            objectExecuteableTextField.getText().equalsIgnoreCase("y"));
                } else return null;
            });

            Optional<Integer> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.dacAllObjectsObservableList.remove(allObjectsChoiceBox.getValue());
                    mainAPP.dacAllObjectsObservableList.add(objectNameTextField.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Modify Succeed");
                    alert.setContentText("Modified succeed object into database.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Modify Failed");
                    alert.setContentText("Modified failed object into database.");
                    alert.showAndWait();
                }
            }

        });

        dacAddCapability.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<>();
            dialog.setResizable(true);
            dialog.setTitle("Grant Capability");
            dialog.setHeaderText("Grant One Capability");

            Label grantedSubjectChoiceLabel = new Label("Choose Grant Subject: ");
            ChoiceBox<String> grantedSubjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllSubjectsObservableList);
            Label subjectChoiceLabel = new Label("Choose Subject: ");
            ChoiceBox<String> subjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllSubjectsObservableList);
            Label objectChoiceLabel = new Label("Choose object: ");
            ChoiceBox<String> objectChoiceBox = new ChoiceBox<>(mainAPP.dacAllObjectsObservableList);
            CheckBox ownCheckBox = new CheckBox("Own");
            CheckBox readCheckBox = new CheckBox("Read");
            CheckBox writeCheckBox = new CheckBox("Write");
            CheckBox controlCheckBox = new CheckBox("Control");
            CheckBox deleteCheckBox = new CheckBox("Delete");

            GridPane gridPane = new GridPane();
            gridPane.add(grantedSubjectChoiceLabel, 1, 2);
            gridPane.add(grantedSubjectChoiceBox, 2, 2);
            gridPane.add(subjectChoiceLabel, 1, 3);
            gridPane.add(subjectChoiceBox, 2, 3);
            gridPane.add(objectChoiceLabel, 1, 4);
            gridPane.add(objectChoiceBox, 2, 4);
            gridPane.add(ownCheckBox, 3, 1);
            gridPane.add(readCheckBox, 3, 2);
            gridPane.add(writeCheckBox, 3, 3);
            gridPane.add(controlCheckBox, 3, 4);
            gridPane.add(deleteCheckBox, 3, 5);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            final Capability[] addedCapability = new Capability[1];
            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    ACLSubject grantedSubject = mainAPP.dacDatabaseOperation.queryOneSubjectByName(grantedSubjectChoiceBox.getValue());
                    ACLSubject subject = mainAPP.dacDatabaseOperation.queryOneSubjectByName(subjectChoiceBox.getValue());
                    ACLObject object = mainAPP.dacDatabaseOperation.queryOneObjectByName(objectChoiceBox.getValue());
                    String capabilityString = (ownCheckBox.isSelected() ? "o" : "-") +
                            (readCheckBox.isSelected() ? "r" : "-") +
                            (writeCheckBox.isSelected() ? "w" : "-") +
                            (controlCheckBox.isSelected() ? "c" : "-") +
                            (deleteCheckBox.isSelected() ? "d" : "-");
                    addedCapability[0] = Capability.makeCapability(grantedSubject, subject, object, capabilityString);
                    return mainAPP.dacManagement.createCapability(grantedSubject, subject, object, capabilityString);
                }

                return null;
            });

            Optional<Integer> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.dacAllCapabilitiesPropertyObservableList.addAll(CapabilityProperty.capabilityToCapabilityProperty(addedCapability[0]));
                    mainAPP.dacAllCapabilitiesObservableList.addAll(addedCapability[0].getCapabilityId());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Granted Succeed");
                    alert.setContentText("Granted Capability Succeed");
                    alert.showAndWait();
                } else if (result.get() == 2) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("There is a black token denied the capability.");
                    alert.showAndWait();
                } else if (result.get() == 3) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("Granted subject has not control of object.");
                    alert.showAndWait();
                } else if (result.get() == 4) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("There is cyclical capability.");
                    alert.showAndWait();
                } else if (result.get() == 5) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("Granted Subject = Subject.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("Add capability record into database failed.");
                    alert.showAndWait();
                }
            }
        });

        helpMenuAbout.setOnAction(event -> {
            Dialog aboutDialog = new Dialog();
            aboutDialog.setTitle("About");
            aboutDialog.setContentText("Access Control Demo.\nVersion: Beta v1.0.0 ");
            aboutDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            aboutDialog.showAndWait();
        });

        helpMenuContact.setOnAction(event -> {
            Dialog contactDialog = new Dialog();
            contactDialog.setTitle("Contact");
            contactDialog.setContentText("Author: Soukie Zhang\nEmail: qiyiyiqiqi@gmail.com");
            contactDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            contactDialog.showAndWait();
        });

        helpMenuHelp.setOnAction(event -> {
            try {
                Process process = Runtime.getRuntime().exec("notepad " + new File("res/help.txt").getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                Dialog helpDialog = new Dialog();
                helpDialog.setTitle("Get Help Error");
                helpDialog.setContentText("Can't find help documents, please contact to admin.");
                helpDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                helpDialog.showAndWait();
            }
        });

    }


    private void initDACSubjects() {

        dacAllSubjectsListView.setItems(mainAPP.dacAllSubjectsObservableList);

    }

    private void initDACObjects() {

        dacAllObjectsListView.setItems(mainAPP.dacAllObjectsObservableList);

    }

    private void initDACCapabilities() {

        dacAllCapabilitiesListView.setItems(mainAPP.dacAllCapabilitiesObservableList);

        dacAllCapabilitiesTableView.setItems(mainAPP.dacAllCapabilitiesPropertyObservableList);
        dacAllCapabilitiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CapabilityProperty, Integer> capabilityIdCol = new TableColumn<>("Capability ID");
        capabilityIdCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, Integer>("capabilityId"));
        capabilityIdCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, Integer> objectIdCol = new TableColumn<>("Object ID");
        objectIdCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, Integer>("objectId"));
        objectIdCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, String> objectNameCol = new TableColumn<>("Object Name");
        objectNameCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, String>("objectName"));
        objectNameCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, Integer> grantedSubjectIdCol = new TableColumn<>("Granted Subject ID");
        grantedSubjectIdCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, Integer>("grantedSubjectId"));
        grantedSubjectIdCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, String> grantedSubjectNameCol = new TableColumn<>("Granted Subject Name");
        grantedSubjectNameCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, String>("grantedSubjectName"));
        grantedSubjectNameCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, Integer> subjectIdCol = new TableColumn<>("Subject ID");
        subjectIdCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, Integer>("subjectId"));
        subjectIdCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, String> subjectNameCol = new TableColumn<>("Subject Name");
        subjectNameCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, String>("subjectName"));
        subjectNameCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, String> capabilityStringCol = new TableColumn<>("Capability String");
        capabilityStringCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, String>("capabilityString"));
        capabilityStringCol.setPrefWidth(200);

        TableColumn<CapabilityProperty, String> capabilityInfoCol = new TableColumn<>("Capability Info");
        capabilityInfoCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, String>("capabilityInfo"));
        capabilityInfoCol.setPrefWidth(200);

        dacAllCapabilitiesTableView.getColumns().setAll(capabilityIdCol,
                objectIdCol,
                objectNameCol,
                grantedSubjectIdCol,
                grantedSubjectNameCol,
                subjectIdCol,
                subjectNameCol,
                capabilityStringCol,
                capabilityInfoCol);
    }

    private void initDACBlackTokens() {

        dacAllBlackTokensListView.setItems(mainAPP.dacAllBlackTokensObservableList);
    }


    private void initRunningLogPane() {


    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }

}
