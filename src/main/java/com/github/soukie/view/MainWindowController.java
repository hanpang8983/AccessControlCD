package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.DACPolicy.CapabilityList;
import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.DACPolicy.objects.CapabilityProperty;
import com.github.soukie.model.ModelValues;
import com.github.soukie.model.RABCPolicy.subjects.*;
import com.github.soukie.model.security.SecurityEncode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
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
    private MenuItem dacAddBlackToken;
    @FXML
    private MenuItem dacDeleteBlackToken;


    @FXML
    private MenuItem rbacAddRole;
    @FXML
    private MenuItem rbacDeleteRole;
    @FXML
    private MenuItem rbacAddUser;
    @FXML
    private MenuItem rbacDeleteUser;
    @FXML
    private MenuItem rbacAddPermission;
    @FXML
    private MenuItem rbacDeletePermission;

    @FXML
    private MenuItem rbacAssginRoleToUser;
    @FXML
    private MenuItem rbacRevokeRoleFromUser;
    @FXML
    private MenuItem rbacAssginPermissionToRole;
    @FXML
    private MenuItem rbacRevokePermissionFromRole;
    @FXML
    private MenuItem rbacAssginCroleToFrole;
    @FXML
    private MenuItem rbacRevokeCroleFromFrole;


    @FXML
    private MenuItem rbacQueryRolesOfUser;
    @FXML
    private MenuItem rbacQueryPermissionsOfRole;
    @FXML
    private MenuItem rbacQueryPermissionsOfUser;


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


    /**
     * The RBAC layouts and controls.
     */


    @FXML
    private TitledPane rbacAllRolesTitledPane;
    @FXML
    private ListView<String> rbacAllUserListView;
    @FXML
    private TitledPane rbacAllUsersTitledPane;
    @FXML
    private ListView<String> rbacAllRoleListView;
    @FXML
    private TitledPane rbacAllAuthoritiesTitledPane;
    @FXML
    private ListView<String> rbacAllPermissionListView;

    @FXML
    private TableView<URAProperty> rbacAllURATableView;
    @FXML
    private TableView<PRAProperty> rbacAllPRATableView;
    @FXML
    private TableView<RRAProperty> rbacAllRRATableView;


    /**
     * Running log window layouts and controls.
     */

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
        initRBACRoles();
        initRBACUsers();
        initRBACPermissions();
        initRBACURAs();
        initRBACPRAs();
        initRBACRRAs();
        //initRunningLogPane();

    }


    private void initMenuBar() {
        fileMenuClose.setOnAction(event -> {
            mainAPP.databaseOperation.closeConnection();
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
                ACLSubject grantedSubject = mainAPP.databaseOperation.queryOneSubject(Integer.valueOf(objectIdTextField.getText()));
                ACLObject object = resultObject.get();
                int createResult = mainAPP.dacManagement.createObject(grantedSubject, object);
                if (createResult == 2) {
                    mainAPP.dacAllObjectsObservableList.add(objectNameTextField.getText());
                    mainAPP.dacAllCapabilitiesObservableList.addAll(grantedSubject.getId() * 1000000 +
                            grantedSubject.getId() * 1000 +
                            object.getId() + 31);
                    mainAPP.dacAllCapabilitiesPropertyObservableList.add(
                            CapabilityProperty.capabilityToCapabilityProperty(
                                    mainAPP.databaseOperation.queryOneCapability(
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
                    return mainAPP.dacManagement.deleteObject(mainAPP.databaseOperation.queryObjectIdByName(allObjectChoiceBox.getValue())) == 1;
                }
                return null;
            });

            Optional<Boolean> result = dialog.showAndWait();
            if (result.isPresent()) {
                if (result.get()) {
                    ArrayList<Capability> newCapabilities = mainAPP.databaseOperation.queryAllCapabilities();
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
                    return mainAPP.dacManagement.modifyObject(mainAPP.databaseOperation.queryObjectIdByName(allObjectsChoiceBox.getValue()),
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
                    ACLSubject grantedSubject = mainAPP.databaseOperation.queryOneSubjectByName(grantedSubjectChoiceBox.getValue());
                    ACLSubject subject = mainAPP.databaseOperation.queryOneSubjectByName(subjectChoiceBox.getValue());
                    ACLObject object = mainAPP.databaseOperation.queryOneObjectByName(objectChoiceBox.getValue());
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
                } else if (result.get() == 6) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("The granted subject hasn't the capabilities of object.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Granted Failed");
                    alert.setContentText("Add capability record into database failed.");
                    alert.showAndWait();
                }
            }
        });

        dacDeleteCapability.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<>();
            ObservableList<String> subjectsObservableList = FXCollections.observableArrayList();
            ObservableList<String> objectsObservableList = FXCollections.observableArrayList();
            ObservableList<String> capabilitiesObservableList = FXCollections.observableArrayList();
            dialog.setResizable(true);
            dialog.setTitle("Delete Capability");
            dialog.setHeaderText("Delete A capability And Clear Capability Chain.");

            Label choiceGrantedSubjectLabel = new Label("GrantedSubject: ");
            ChoiceBox<String> grantedSubjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllSubjectsObservableList);
            Label subjectLabel = new Label("Subject: ");

            ChoiceBox<String> subjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllSubjectsObservableList);
            /*subjectsObservableList.addAll(mainAPP.databaseOperation.queryCapabilitiesByGrantedSubjectName(
                    grantedSubjectChoiceBox.getValue()).stream().map(Capability::getSubjectName).collect(Collectors.toList()));

            subjectChoiceBox.setItems(subjectsObservableList);*/

            Label objectLabel = new Label("Object: ");
            ChoiceBox<String> objectChoiceBox = new ChoiceBox<>(mainAPP.dacAllObjectsObservableList);
            /*objectsObservableList.addAll(mainAPP.databaseOperation.queryCapabilitiesByGrantedSubjectNameAndSubjectName(
                    grantedSubjectChoiceBox.getValue(),
                    subjectChoiceBox.getValue()).stream().map(Capability::getObjectName).collect(Collectors.toList()));
            objectChoiceBox.setItems(objectsObservableList);*/

            Label capabilitiesLabel = new Label("Capabilities");

            CheckBox ownCheckBox = new CheckBox("Own");
            CheckBox readCheckBox = new CheckBox("Read");
            CheckBox writeCheckBox = new CheckBox("Write");
            CheckBox controlCheckBox = new CheckBox("Control");
            CheckBox deleteCheckBox = new CheckBox("Delete");
            /*ChoiceBox<String> capabilitiesChoiceBox = new ChoiceBox<>(mainAPP.dacAllCapabilitiesObservableList);
            capabilitiesObservableList.addAll(mainAPP.databaseOperation.queryCapabilitiesByGrantedSubjectNameAndSubjectNameAndObjectName(
                    grantedSubjectChoiceBox.getValue(),
                    subjectChoiceBox.getValue(),
                    objectChoiceBox.getValue()).stream().map(Capability::getCapabilityString).collect(Collectors.toList()));
            capabilitiesChoiceBox.setItems(capabilitiesObservableList);*/

            GridPane gridPane = new GridPane();
            gridPane.add(choiceGrantedSubjectLabel, 1, 1);
            gridPane.add(grantedSubjectChoiceBox, 2, 1);
            gridPane.add(subjectLabel, 1, 2);
            gridPane.add(subjectChoiceBox, 2, 2);
            gridPane.add(objectLabel, 1, 3);
            gridPane.add(objectChoiceBox, 2, 3);
            gridPane.add(capabilitiesLabel, 3, 2);
            gridPane.add(ownCheckBox, 4, 2);
            gridPane.add(readCheckBox, 4, 3);
            gridPane.add(writeCheckBox, 4, 4);
            gridPane.add(controlCheckBox, 4, 5);
            gridPane.add(deleteCheckBox, 4, 6);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                String capabilityString = (ownCheckBox.isSelected() ? "o" : "-") +
                        (readCheckBox.isSelected() ? "r" : "-") +
                        (writeCheckBox.isSelected() ? "w" : "-") +
                        (controlCheckBox.isSelected() ? "c" : "-") +
                        (deleteCheckBox.isSelected() ? "d" : "-");
                ACLSubject grantedSubject = mainAPP.databaseOperation.queryOneSubjectByName(grantedSubjectChoiceBox.getValue());
                ACLSubject subject = mainAPP.databaseOperation.queryOneSubjectByName(subjectChoiceBox.getValue());
                ACLObject object = mainAPP.databaseOperation.queryOneObjectByName(objectChoiceBox.getValue());
                if (param == buttonTypeOk) {
                    int deleteResult = mainAPP.dacManagement.deleteCapability(grantedSubject,
                            subject,
                            object,
                            capabilityString);
                    if (deleteResult == 1) {
                        int deleteCapabilityId = grantedSubject.getId() * 1000000 +
                                subject.getId() * 1000 +
                                object.getId() +
                                CapabilityList.capabilityStringToIntValue(capabilityString);
                        mainAPP.dacAllCapabilitiesObservableList.removeAll(deleteCapabilityId);

                        CapabilityProperty deleteCapabilityProperty = null;
                        for (CapabilityProperty capabilityProperty : mainAPP.dacAllCapabilitiesPropertyObservableList) {
                            if (capabilityProperty.getCapabilityId() == deleteCapabilityId) {
                                deleteCapabilityProperty = capabilityProperty;
                            }
                        }
                        if (deleteCapabilityProperty != null) {
                            mainAPP.dacAllCapabilitiesPropertyObservableList.removeAll(deleteCapabilityProperty);
                        }

                    }
                    return deleteResult;
                } else {
                    return null;
                }
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Deleted Failed");
                    alert.setContentText("The capability deleted failed.");
                    alert.showAndWait();
                } else if (result.get() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deleted Succeed");
                    alert.setContentText("The capability deleted succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Deleted Failed");
                    alert.setContentText("the capability record is not existed.");
                    alert.showAndWait();
                }
            }


        });

        dacAddBlackToken.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Add BlockToken");
            dialog.setResizable(true);
            dialog.setHeaderText("Add One Block Token");

            Label grantedSubjectLabel = new Label("Granted Subject: ");
            ChoiceBox<String> grantedSubjectChoiceBox = new ChoiceBox<String>(mainAPP.dacAllSubjectsObservableList);
            Label subjectLabel = new Label("Subject: ");
            ChoiceBox<String> subjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllSubjectsObservableList);
            Label objectLabel = new Label("Object: ");
            ChoiceBox<String> objectChoiceBox = new ChoiceBox<String>(mainAPP.dacAllObjectsObservableList);

            Label choiceCapabilityLabel = new Label("Capability: ");
            CheckBox ownCheckBox = new CheckBox("Own");
            CheckBox readCheckBox = new CheckBox("Read");
            CheckBox writeCheckBox = new CheckBox("Write");
            CheckBox controlCheckBox = new CheckBox("Control");
            CheckBox deleteCheckBox = new CheckBox("Delete");
            CheckBox blackToken = new CheckBox("BlackToken");

            GridPane gridPane = new GridPane();
            gridPane.add(grantedSubjectLabel, 1, 1);
            gridPane.add(grantedSubjectChoiceBox, 2, 1);
            gridPane.add(subjectLabel, 1, 2);
            gridPane.add(subjectChoiceBox, 2, 2);
            gridPane.add(objectLabel, 1, 3);
            gridPane.add(objectChoiceBox, 2, 3);
            gridPane.add(choiceCapabilityLabel, 3, 1);
            gridPane.add(ownCheckBox, 4, 1);
            gridPane.add(readCheckBox, 4, 2);
            gridPane.add(writeCheckBox, 4, 3);
            gridPane.add(controlCheckBox, 4, 4);
            gridPane.add(deleteCheckBox, 4, 5);
            gridPane.add(blackToken, 1, 4);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    if (blackToken.isSelected()) {
                        ACLSubject grantedSubject = mainAPP.databaseOperation.queryOneSubjectByName(grantedSubjectChoiceBox.getValue());
                        ACLSubject subject = mainAPP.databaseOperation.queryOneSubjectByName(subjectChoiceBox.getValue());
                        ACLObject object = mainAPP.databaseOperation.queryOneObjectByName(objectChoiceBox.getValue());
                        String capabilityString = (ownCheckBox.isSelected() ? "o" : "-") +
                                (readCheckBox.isSelected() ? "r" : "-") +
                                (writeCheckBox.isSelected() ? "w" : "-") +
                                (controlCheckBox.isSelected() ? "c" : "-") +
                                (deleteCheckBox.isSelected() ? "d" : "-");
                        int deleteResult = mainAPP.dacManagement.createBlackToken(grantedSubject,
                                subject,
                                object,
                                capabilityString,
                                blackToken.isSelected());
                        if (deleteResult == 1) {
                            mainAPP.dacAllBlackTokensObservableList.add(grantedSubject.getId() * 1000000 +
                                    subject.getId() * 1000 + object.getId() + CapabilityList.capabilityStringToIntValue(capabilityString));
                        }
                        return deleteResult;

                    }

                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Added Failed");
                    alert.setContentText("Added black token failed.");
                    alert.showAndWait();
                } else if (result.get() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Added Succeed");
                    alert.setContentText("Added black token succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Added Failed");
                    alert.setContentText("the black token record is already existed.");
                    alert.showAndWait();
                }
            }


        });

        dacDeleteBlackToken.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Delete BlockToken");
            dialog.setResizable(true);
            dialog.setHeaderText("Delete One Block Token");

            Label grantedSubjectLabel = new Label("Granted Subject: ");
            ChoiceBox<String> grantedSubjectChoiceBox = new ChoiceBox<String>(mainAPP.dacAllSubjectsObservableList);
            Label subjectLabel = new Label("Subject: ");
            ChoiceBox<String> subjectChoiceBox = new ChoiceBox<>(mainAPP.dacAllSubjectsObservableList);
            Label objectLabel = new Label("Object: ");
            ChoiceBox<String> objectChoiceBox = new ChoiceBox<String>(mainAPP.dacAllObjectsObservableList);

            Label choiceCapabilityLabel = new Label("Capability: ");
            CheckBox ownCheckBox = new CheckBox("Own");
            CheckBox readCheckBox = new CheckBox("Read");
            CheckBox writeCheckBox = new CheckBox("Write");
            CheckBox controlCheckBox = new CheckBox("Control");
            CheckBox deleteCheckBox = new CheckBox("Delete");
            GridPane gridPane = new GridPane();
            gridPane.add(grantedSubjectLabel, 1, 1);
            gridPane.add(grantedSubjectChoiceBox, 2, 1);
            gridPane.add(subjectLabel, 1, 2);
            gridPane.add(subjectChoiceBox, 2, 2);
            gridPane.add(objectLabel, 1, 3);
            gridPane.add(objectChoiceBox, 2, 3);
            gridPane.add(choiceCapabilityLabel, 3, 1);
            gridPane.add(ownCheckBox, 4, 1);
            gridPane.add(readCheckBox, 4, 2);
            gridPane.add(writeCheckBox, 4, 3);
            gridPane.add(controlCheckBox, 4, 4);
            gridPane.add(deleteCheckBox, 4, 5);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {

                    ACLSubject grantedSubject = mainAPP.databaseOperation.queryOneSubjectByName(grantedSubjectChoiceBox.getValue());
                    ACLSubject subject = mainAPP.databaseOperation.queryOneSubjectByName(subjectChoiceBox.getValue());
                    ACLObject object = mainAPP.databaseOperation.queryOneObjectByName(objectChoiceBox.getValue());
                    String capabilityString = (ownCheckBox.isSelected() ? "o" : "-") +
                            (readCheckBox.isSelected() ? "r" : "-") +
                            (writeCheckBox.isSelected() ? "w" : "-") +
                            (controlCheckBox.isSelected() ? "c" : "-") +
                            (deleteCheckBox.isSelected() ? "d" : "-");
                    int deleteResult = mainAPP.dacManagement.deleteBlackToken(grantedSubject,
                            subject,
                            object,
                            capabilityString);
                    if (deleteResult == 1) {
                        mainAPP.dacAllBlackTokensObservableList.removeAll(grantedSubject.getId() * 1000000 +
                                subject.getId() * 1000 + object.getId() + CapabilityList.capabilityStringToIntValue(capabilityString));
                    }
                    return deleteResult;
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Failed");
                    alert.setContentText("Delete black token failed.");
                    alert.showAndWait();
                } else if (result.get() == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Added Succeed");
                    alert.setContentText("Added black token succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Added Failed");
                    alert.setContentText("the black token record not existed.");
                    alert.showAndWait();
                }
            }

        });


        rbacAddRole.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Add User");
            dialog.setHeaderText("Add One User");
            dialog.setResizable(true);

            Label roleNameLabel = new Label("New Role Name: ");
            TextField roleNameTextFiled = new TextField();
            roleNameTextFiled.setPromptText("input unique role name");

            GridPane gridPane = new GridPane();
            gridPane.add(roleNameLabel, 1, 1);
            gridPane.add(roleNameTextFiled, 1, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);
            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.addRole(new Role(roleNameTextFiled.getText(), new Date().getTime()));
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Role Name repeated");
                    alert.setContentText("Add user's name repeated.");
                    alert.showAndWait();
                } else if (result.get() == 1) {
                    mainAPP.rbacAllRoleNameObservableList.addAll(roleNameTextFiled.getText());
                    mainAPP.rbacAllRoleObservableList.addAll(new Role(roleNameTextFiled.getText(), new Date().getTime()));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Add Succeed");
                    alert.setContentText("Add user record into database succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Add Failed");
                    alert.setContentText("Add user record into database failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacDeleteRole.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Delete User");
            dialog.setHeaderText("delete user record from database.");
            dialog.setResizable(true);

            Label roleChoiceLabel = new Label("Choose Role: ");
            ChoiceBox<String> roleChoiceBox = new ChoiceBox<>(mainAPP.rbacAllRoleNameObservableList);
            GridPane gridPane = new GridPane();
            gridPane.add(roleChoiceLabel, 1, 1);
            gridPane.add(roleChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.deleteRole(roleChoiceBox.getValue());
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    Role deleteRole = null;
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(roleChoiceBox.getValue())) {
                            deleteRole = role;
                            break;
                        }
                    }
                    if (deleteRole != null) {
                        mainAPP.rbacAllRoleObservableList.removeAll(deleteRole);
                    }
                    mainAPP.rbacAllRoleNameObservableList.removeAll(roleChoiceBox.getValue());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deleted Succeed");
                    alert.setContentText("deleted role database record succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Deleted Failed");
                    alert.setContentText("deleted role database record failed.");
                    alert.showAndWait();
                }
            }

        });

        rbacAddUser.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Add User");
            dialog.setHeaderText("Add a user");
            dialog.setResizable(true);

            Label userNameLabel = new Label("User Name: ");
            TextField userNameTextField = new TextField();
            userNameTextField.setPromptText("user unique name");
            Label userPasswordLabel = new Label("User Pass");
            TextField userPasswordTextField = new TextField();
            userPasswordTextField.setPromptText("user pass");

            GridPane gridPane = new GridPane();
            gridPane.add(userNameLabel, 1, 1);
            gridPane.add(userNameTextField, 2, 1);
            gridPane.add(userPasswordLabel, 1, 2);
            gridPane.add(userPasswordTextField, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    try {
                        return mainAPP.rbacManagement.addUser(new User(userNameTextField.getText(),
                                SecurityEncode.encoderByMd5(SecurityEncode.encoderByMd5(userPasswordTextField.getText())),
                                new Date().getTime()));
                    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllUserObservableList.addAll(new User(userNameTextField.getText(),
                            userPasswordTextField.getText(),
                            new Date().getTime()));
                    mainAPP.rbacAllUserNameObservableList.addAll(userNameTextField.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Added Succeed");
                    alert.setContentText("added user database record succeed.");
                    alert.showAndWait();
                } else if (result.get() == 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Added Warning");
                    alert.setContentText("User had already existed int database.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Added Error");
                    alert.setContentText("added user database record failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacDeleteUser.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Delete User");
            dialog.setHeaderText("Delete user database record");
            dialog.setResizable(true);

            Label choiceUserLabel = new Label("Choose User: ");
            ChoiceBox<String> userChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllUserNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(choiceUserLabel, 1, 1);
            gridPane.add(userChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.deleteUser(userChoiceBox.getValue());
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    User deleteUser = null;
                    for (User user : mainAPP.rbacAllUserObservableList) {
                        if (user.userName.equalsIgnoreCase(userChoiceBox.getValue())) {
                            deleteUser = user;
                            break;
                        }
                    }
                    if (deleteUser != null) {
                        mainAPP.rbacAllUserObservableList.removeAll(deleteUser);
                    }
                    mainAPP.rbacAllUserNameObservableList.removeAll(userChoiceBox.getValue());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deleted Succeed");
                    alert.setContentText("deleted user database record succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Deleted Failed");
                    alert.setContentText("deleted user database record failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacAddPermission.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Add Permission");
            dialog.setHeaderText("add permission record into database.");
            dialog.setResizable(true);

            Label permissionNameLabel = new Label("Permission Name: ");
            TextField permissionNameTextField = new TextField();
            permissionNameTextField.setPromptText("permission unique name");

            GridPane gridPane = new GridPane();
            gridPane.add(permissionNameLabel, 1, 1);
            gridPane.add(permissionNameTextField, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.addPermission(new Permission(permissionNameTextField.getText(),
                            new Date().getTime()));
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllPermissionObservableList.addAll(new Permission(permissionNameTextField.getText(),
                            new Date().getTime()));
                    mainAPP.rbacAllPermissionNameObservableList.addAll(permissionNameTextField.getText());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Added Succeed");
                    alert.setContentText("added permission database record succeed.");
                    alert.showAndWait();
                } else if (result.get() == 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Added Failed");
                    alert.setContentText("the permission is already existed in database.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Added Failed");
                    alert.setContentText("the permission database record failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacDeletePermission.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("Delete Permission");
            dialog.setHeaderText("delete permission record from database.");
            dialog.setResizable(true);

            Label choicePermissionLabel = new Label("Choose Permission: ");
            ChoiceBox<String> permissionNameChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllPermissionNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(choicePermissionLabel, 1, 1);
            gridPane.add(permissionNameChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.deletePermission(permissionNameChoiceBox.getValue());
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    Permission deletePermission = null;
                    for (Permission permission :
                            mainAPP.rbacAllPermissionObservableList) {
                        if (permission.permissionName.equalsIgnoreCase(permissionNameChoiceBox.getValue())) {
                            deletePermission = permission;
                            break;
                        }
                    }
                    if (deletePermission != null) {
                        mainAPP.rbacAllPermissionObservableList.removeAll(deletePermission);
                    }
                    mainAPP.rbacAllPermissionNameObservableList.removeAll(permissionNameChoiceBox.getValue());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Deleted Succeed");
                    alert.setContentText("deleted permission database record succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Deleted Failed");
                    alert.setContentText("deleted permission database record failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacAssginRoleToUser.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("URA Assign");
            dialog.setHeaderText("assign role to user");
            dialog.setResizable(true);

            Label chooseUserLabel = new Label("Choose User: ");
            ChoiceBox<String> choiceBoxUser = new ChoiceBox<String>(mainAPP.rbacAllUserNameObservableList);
            Label chooseRoleLabel = new Label("Choose Role: ");
            ChoiceBox<String> choiceBoxRole = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(chooseUserLabel, 1, 1);
            gridPane.add(choiceBoxUser, 2, 1);
            gridPane.add(chooseRoleLabel, 1, 2);
            gridPane.add(choiceBoxRole, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            User[] newUser = new User[1];
            Role[] newRole = new Role[1];
            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {

                    for (User user : mainAPP.rbacAllUserObservableList) {
                        if (user.userName.equalsIgnoreCase(choiceBoxUser.getValue())) {
                            newUser[0] = user;
                            break;
                        }
                    }
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(choiceBoxRole.getValue())) {
                            newRole[0] = role;
                            break;
                        }
                    }
                    return mainAPP.rbacManagement.createURA(newUser[0], newRole[0]);
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllUserObservableList.removeAll(newUser[0]);
                    newUser[0].addRoleName(newRole[0].roleName);
                    mainAPP.rbacAllUserObservableList.addAll(newUser[0]);

                    mainAPP.rbacAllURAPropertiesObservableList.addAll(new URAProperty(newUser[0].userName, newRole[0].roleName));

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Assigned Succeed");
                    alert.setContentText("assigned role to user succeed.");
                    alert.showAndWait();
                } else if (result.get() == 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Assigned Warning");
                    alert.setContentText("the URA record is already existed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Assigned Error");
                    alert.setContentText("assigned role to user failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacRevokeRoleFromUser.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("URA Revoke");
            dialog.setHeaderText("revoke role from user");
            dialog.setResizable(true);

            Label chooseUserLabel = new Label("Choose User: ");
            ChoiceBox<String> choiceBoxUser = new ChoiceBox<String>(mainAPP.rbacAllUserNameObservableList);
            Label chooseRoleLabel = new Label("Choose Role: ");
            ChoiceBox<String> choiceBoxRole = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(chooseUserLabel, 1, 1);
            gridPane.add(choiceBoxUser, 2, 1);
            gridPane.add(chooseRoleLabel, 1, 2);
            gridPane.add(choiceBoxRole, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            User[] newUser = new User[1];
            Role[] newRole = new Role[1];
            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {

                    for (User user : mainAPP.rbacAllUserObservableList) {
                        if (user.userName.equalsIgnoreCase(choiceBoxUser.getValue())) {
                            newUser[0] = user;
                            break;
                        }
                    }
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(choiceBoxRole.getValue())) {
                            newRole[0] = role;
                            break;
                        }
                    }
                    return mainAPP.rbacManagement.deleteURA(newUser[0], newRole[0]);
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllUserObservableList.removeAll(newUser[0]);
                    newUser[0].removeRoleName(newRole[0].roleName);
                    mainAPP.rbacAllUserObservableList.addAll(newUser[0]);

                    URAProperty deleteURAUraProperty = null;
                    for (URAProperty property : mainAPP.rbacAllURAPropertiesObservableList) {
                        if (property.getRoleName().equalsIgnoreCase(newRole[0].roleName) &&
                                property.getUserName().equalsIgnoreCase(newUser[0].userName)) {
                            deleteURAUraProperty = property;
                            break;
                        }
                    }
                    if (deleteURAUraProperty != null) {
                        mainAPP.rbacAllURAPropertiesObservableList.removeAll(deleteURAUraProperty);
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Revoked Succeed");
                    alert.setContentText("revoked role from user succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Revoked Error");
                    alert.setContentText("assigned role from user failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacAssginPermissionToRole.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("PRA Assign");
            dialog.setHeaderText("assign permission to role");
            dialog.setResizable(true);

            Label roleLabel = new Label("Choose Role: ");
            ChoiceBox<String> roleChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);
            Label permissionLabel = new Label("Choose Permission: ");
            ChoiceBox<String> permissionChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllPermissionNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(roleLabel, 1, 1);
            gridPane.add(roleChoiceBox, 2, 1);
            gridPane.add(permissionLabel, 1, 2);
            gridPane.add(permissionChoiceBox, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            Role[] newRole = new Role[1];
            Permission[] newPermission = new Permission[1];

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(roleChoiceBox.getValue())) {
                            newRole[0] = role;
                            break;
                        }
                    }
                    for (Permission permission : mainAPP.rbacAllPermissionObservableList) {
                        if (permission.permissionName.equalsIgnoreCase(permissionChoiceBox.getValue())) {
                            newPermission[0] = permission;
                            break;
                        }
                    }

                    return mainAPP.rbacManagement.createPRA(newRole[0], newPermission[0]);
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllRoleObservableList.removeAll(newRole[0]);
                    newRole[0].addPermissionName(newPermission[0].permissionName);
                    mainAPP.rbacAllRoleObservableList.addAll(newRole[0]);

                    mainAPP.rbacAllPRAPropertiesObservableList.add(new PRAProperty(newRole[0].roleName, newPermission[0].permissionName));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Assigned Succeed");
                    alert.setContentText("assigned permission to role succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Assigned Failed");
                    alert.setContentText("assigned permission to role failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacRevokePermissionFromRole.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("PRA Revoke");
            dialog.setHeaderText("revoke permission from role");
            dialog.setResizable(true);

            Label roleLabel = new Label("Choose Role: ");
            ChoiceBox<String> roleChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);
            Label permissionLabel = new Label("Choose Permission: ");
            ChoiceBox<String> permissionChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllPermissionNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(roleLabel, 1, 1);
            gridPane.add(roleChoiceBox, 2, 1);
            gridPane.add(permissionLabel, 1, 2);
            gridPane.add(permissionChoiceBox, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            Role[] newRole = new Role[1];
            Permission[] newPermission = new Permission[1];

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(roleChoiceBox.getValue())) {
                            newRole[0] = role;
                            break;
                        }
                    }
                    for (Permission permission : mainAPP.rbacAllPermissionObservableList) {
                        if (permission.permissionName.equalsIgnoreCase(permissionChoiceBox.getValue())) {
                            newPermission[0] = permission;
                            break;
                        }
                    }

                    return mainAPP.rbacManagement.deletePRA(newRole[0], newPermission[0]);
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllRoleObservableList.removeAll(newRole[0]);
                    newRole[0].removePermissionName(newPermission[0].permissionName);
                    mainAPP.rbacAllRoleObservableList.addAll(newRole[0]);

                    PRAProperty praProperty = null;
                    for (PRAProperty property : mainAPP.rbacAllPRAPropertiesObservableList) {
                        if (property.getRoleName().equalsIgnoreCase(newRole[0].roleName) &&
                                property.getPermissionName().equalsIgnoreCase(newPermission[0].permissionName)) {
                            praProperty = property;
                            break;
                        }
                    }
                    if (praProperty != null) {
                        mainAPP.rbacAllPRAPropertiesObservableList.removeAll(praProperty);
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Revoked Succeed");
                    alert.setContentText("revoked permission from role succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Revoked Failed");
                    alert.setContentText("revoked permission from role failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacAssginCroleToFrole.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setHeaderText("Assign children role to father role.");
            dialog.setTitle("RRA Assign");
            dialog.setResizable(true);

            Label fRoleLabel = new Label("Father Role: ");
            ChoiceBox<String> fatherChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);
            Label cRoleLabel = new Label("Children Role: ");
            ChoiceBox<String> childrenChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(fRoleLabel, 1, 1);
            gridPane.add(fatherChoiceBox, 2, 1);
            gridPane.add(cRoleLabel, 1, 2);
            gridPane.add(childrenChoiceBox, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            Role[] newRole = new Role[2];

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(fatherChoiceBox.getValue())) {
                            newRole[0] = role;
                        }
                        if (role.roleName.equalsIgnoreCase(childrenChoiceBox.getValue())) {
                            newRole[1] = role;
                        }
                    }

                    return mainAPP.rbacManagement.createRRA(newRole[0], newRole[1]);
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllRoleObservableList.removeAll(newRole);
                    newRole[0].addChildrenRoleName(newRole[1].roleName);
                    newRole[1].setFatherRoleName(newRole[0].roleName);
                    mainAPP.rbacAllRoleObservableList.addAll(newRole);

                    mainAPP.rbacAllRRAPropertiesObservableList.add(new RRAProperty(newRole[0].roleName, newRole[1].roleName));
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Assigned Succeed");
                    alert.setContentText("assigned children role to father role succeed.");
                    alert.showAndWait();
                } else if (result.get() == 2) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Assigned Failed");
                    alert.setContentText("the RRA record is already existed.");
                    alert.showAndWait();
                } else if (result.get() == 3) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Assigned Failed");
                    alert.setContentText("role can't assign RRA to itself.");
                    alert.showAndWait();
                } else if (result.get() == 4) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Assigned Failed");
                    alert.setContentText("there is a cyclical RRA.");
                    alert.showAndWait();
                } else if (result.get() == 5) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Assigned Failed");
                    alert.setContentText("father role is already children role's father.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Assigned Failed");
                    alert.setContentText("assigned children role to father role failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacRevokeCroleFromFrole.setOnAction(event -> {
            Dialog<Integer> dialog = new Dialog<Integer>();
            dialog.setTitle("RRA Revoke");
            dialog.setHeaderText("revoke children role from father role");
            dialog.setResizable(true);

            Label fRoleLabel = new Label("Father Role: ");
            ChoiceBox<String> fatherChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);
            Label cRoleLabel = new Label("Children Role: ");
            ChoiceBox<String> childrenChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(fRoleLabel, 1, 1);
            gridPane.add(fatherChoiceBox, 2, 1);
            gridPane.add(cRoleLabel, 1, 2);
            gridPane.add(childrenChoiceBox, 2, 2);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            Role[] newRole = new Role[2];

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    for (Role role : mainAPP.rbacAllRoleObservableList) {
                        if (role.roleName.equalsIgnoreCase(fatherChoiceBox.getValue())) {
                            newRole[0] = role;
                        }
                        if (role.roleName.equalsIgnoreCase(childrenChoiceBox.getValue())) {
                            newRole[1] = role;
                        }
                    }

                    return mainAPP.rbacManagement.deleteRRA(newRole[0], newRole[1]);
                }
                return null;
            });

            Optional<Integer> result = dialog.showAndWait();

            if (result.isPresent()) {
                if (result.get() == 1) {
                    mainAPP.rbacAllRoleObservableList.removeAll(newRole);
                    newRole[0].removeChildrenRoleName(newRole[1].roleName);
                    newRole[1].setFatherRoleName(ModelValues.ADMIN_ROLE_NAME);
                    mainAPP.rbacAllRoleObservableList.addAll(newRole);

                    RRAProperty deleteProperty = null;
                    for (RRAProperty property : mainAPP.rbacAllRRAPropertiesObservableList) {
                        if (property.getFatherRoleName().equalsIgnoreCase(newRole[0].roleName) &&
                                property.getChildrenRoleName().equalsIgnoreCase(newRole[1].roleName)) {
                            deleteProperty = property;
                            break;
                        }
                    }
                    if (deleteProperty != null) {
                        mainAPP.rbacAllRRAPropertiesObservableList.removeAll(deleteProperty);
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Revoked Succeed");
                    alert.setContentText("revoked children role from father role succeed.");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Revoked Failed");
                    alert.setContentText("revoked children role from father role failed.");
                    alert.showAndWait();
                }
            }
        });

        rbacQueryRolesOfUser.setOnAction(event -> {
            Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
            dialog.setTitle("Query Roles");
            dialog.setHeaderText("query all roles of user");
            dialog.setResizable(true);

            Label userLabel = new Label("Choose User");
            ChoiceBox<String> userChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllUserNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(userLabel, 1, 1);
            gridPane.add(userChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.queryRoleNamesOfUser(userChoiceBox.getValue());
                }
                return null;
            });

            Optional<ArrayList<String>> result = dialog.showAndWait();


            if (result.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("All Roles Of User");
                ObservableList<String> observableList = FXCollections.observableArrayList();
                observableList.addAll(result.get());
                alert.getDialogPane().setContent(new ListView<>(observableList));
                alert.showAndWait();
            }
        });

        rbacQueryPermissionsOfRole.setOnAction(event -> {
            Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
            dialog.setTitle("Query Permissions of Role");
            dialog.setHeaderText("query permissions of role.");
            dialog.setResizable(true);

            Label roleLabel = new Label("Choose Role: ");
            ChoiceBox<String> roleChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllRoleNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(roleLabel, 1, 1);
            gridPane.add(roleChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return mainAPP.rbacManagement.queryPermissionNamesOfRoles(roleChoiceBox.getValue());
                }
                return null;
            });

            Optional<ArrayList<String>> result = dialog.showAndWait();

            if (result.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("All Permissions Of Role");
                ObservableList<String> observableList = FXCollections.observableArrayList();
                observableList.addAll(result.get());
                alert.getDialogPane().setContent(new ListView<>(observableList));
                alert.showAndWait();
            }

        });

        rbacQueryPermissionsOfUser.setOnAction(event -> {
            Dialog<ArrayList<String>> dialog = new Dialog<ArrayList<String>>();
            dialog.setTitle("Query Permissions of User");
            dialog.setHeaderText("query all permissions of user");
            dialog.setResizable(true);

            Label userLabel = new Label("Choose user: ");
            ChoiceBox<String> userChoiceBox = new ChoiceBox<String>(mainAPP.rbacAllUserNameObservableList);

            GridPane gridPane = new GridPane();
            gridPane.add(userLabel, 1, 1);
            gridPane.add(userChoiceBox, 2, 1);

            dialog.getDialogPane().setContent(gridPane);

            ButtonType buttonTypeOk = new ButtonType("Sure", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(buttonTypeCancel, buttonTypeOk);

            dialog.setResultConverter(param -> {
                if (param == buttonTypeOk) {
                    return new ArrayList<>(new LinkedHashSet<>(mainAPP.rbacManagement.queryPermissionNamesOfUser(userChoiceBox.getValue())));
                }
                return null;
            });

            Optional<ArrayList<String>> result = dialog.showAndWait();

            if (result.isPresent()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("All Permissions Of User");
                ObservableList<String> observableList = FXCollections.observableArrayList();
                observableList.addAll(result.get());
                alert.getDialogPane().setContent(new ListView<>(observableList));
                alert.showAndWait();
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

    private void initRBACUsers() {
        rbacAllUserListView.setItems(mainAPP.rbacAllUserNameObservableList);
    }

    private void initRBACRoles() {
        rbacAllRoleListView.setItems(mainAPP.rbacAllRoleNameObservableList);
    }

    private void initRBACPermissions() {
        rbacAllPermissionListView.setItems(mainAPP.rbacAllPermissionNameObservableList);
    }

    private void initRBACURAs() {
        rbacAllURATableView.setItems(mainAPP.rbacAllURAPropertiesObservableList);
        rbacAllURATableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<URAProperty, String> userNameCol = new TableColumn<>("User Name");
        userNameCol.setCellValueFactory(new PropertyValueFactory<URAProperty, String>("userName"));
        userNameCol.setPrefWidth(150);

        TableColumn<URAProperty, String> roleNameCol = new TableColumn<>("Role Name");
        roleNameCol.setCellValueFactory(new PropertyValueFactory<URAProperty, String>("roleName"));
        roleNameCol.setPrefWidth(150);

        rbacAllURATableView.getColumns().setAll(userNameCol, roleNameCol);
    }

    private void initRBACPRAs() {
        rbacAllPRATableView.setItems(mainAPP.rbacAllPRAPropertiesObservableList);
        rbacAllPRATableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<PRAProperty, String> roleNameCol = new TableColumn<>("Role Name");
        roleNameCol.setCellValueFactory(new PropertyValueFactory<PRAProperty, String>("roleName"));
        roleNameCol.setPrefWidth(150);

        TableColumn<PRAProperty, String> permissionNameCol = new TableColumn<>("Permission Name");
        permissionNameCol.setCellValueFactory(new PropertyValueFactory<PRAProperty, String>("permissionName"));
        permissionNameCol.setPrefWidth(150);

        rbacAllPRATableView.getColumns().setAll(roleNameCol, permissionNameCol);
    }

    private void initRBACRRAs() {
        rbacAllRRATableView.setItems(mainAPP.rbacAllRRAPropertiesObservableList);
        rbacAllRRATableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<RRAProperty, String> fatherRoleNameCol = new TableColumn<>("Father Role Name");
        fatherRoleNameCol.setCellValueFactory(new PropertyValueFactory<RRAProperty, String>("fatherRoleName"));
        fatherRoleNameCol.setPrefWidth(200);

        TableColumn<RRAProperty, String> childrenRoleNameCol = new TableColumn<>("Children Role Name");
        childrenRoleNameCol.setCellValueFactory(new PropertyValueFactory<RRAProperty, String>("childrenRoleName"));
        childrenRoleNameCol.setPrefWidth(200);

        rbacAllRRATableView.getColumns().setAll(fatherRoleNameCol, childrenRoleNameCol);
    }


    private void initRunningLogPane() {

        redirectSystemStreams();
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateTextArea(String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                runningLogTextArea.appendText(text);
            }
        }).start();
    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }

}
