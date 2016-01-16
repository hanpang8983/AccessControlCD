package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.DACPolicy.DACManagement;
import com.github.soukie.model.DACPolicy.objects.*;
import com.github.soukie.model.ModelValues;
import com.github.soukie.model.database.DACDatabaseOperation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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



    private ArrayList<ACLSubject> dacAllSubjects;
    private ArrayList<ACLObject> dacAllObjects;
    private ArrayList<Capability> dacAllCapabilities;
    private ArrayList<CapabilityProperty> dacAllCapabilitiesProperty;
    private ArrayList<BlackToken> dacAllBlackTokens;

    private ObservableList<String> dacAllSubjectsObservableList = FXCollections.observableArrayList();
    private ObservableList<String> dacAllObjectsObservableList = FXCollections.observableArrayList();
    private ObservableList<Integer> dacAllCapabilitiesObservableList = FXCollections.observableArrayList();
    private ObservableList<CapabilityProperty> dacAllCapabilitiesPropertyObservableList = FXCollections.observableArrayList();
    private ObservableList<Integer> dacAllBlackTokensObservableList = FXCollections.observableArrayList();

    /**
     * DAC Database Handle.
     */
    private DACDatabaseOperation dacDatabaseOperation;
    /**
     * DAC Operation Handle.
     */
    private DACManagement dacManagement;

    /**
     * Empty constructor.
     */
    public MainWindowController() {

    }

    @FXML
    public void initialize() {
        //Init MenuBar.
        initMenuBar();
        //init databaseOperation to connect database and dacManagement to operate.
        initDatabaseOperation();

        initDACSubjects();
        initDACObjects();
        initDACCapabilities();
        initDACBlackTokens();

    }

    private void initMenuBar() {
        fileMenuClose.setOnAction(event -> {
            dacDatabaseOperation.closeConnection();
            System.exit(0);
        });

        editMenuClearLog.setOnAction(event -> {

        });

        editMenuModifyAdminInfo.setOnAction(event -> mainAPP.showModifyAdminInfoDialog());



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
    private void initDatabaseOperation() {
        dacDatabaseOperation = new DACDatabaseOperation(new Date().getTime());
        try {
            dacDatabaseOperation.initDatabaseConnection(ModelValues.DATABASE_MYSQL_PROPERTIES_FILE_PATH);
            //init the DACManagement.
            dacManagement = new DACManagement(dacDatabaseOperation);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initDACSubjects() {
        dacAllSubjects = dacDatabaseOperation.queryAllSubjects();
        if (dacAllSubjects != null) {
            dacAllSubjectsObservableList.addAll(dacAllSubjects.stream().map(ACLSubject::getName).collect(Collectors.toList()));
        } else {
            dacAllSubjectsObservableList.addAll("SubjectDemo");
        }
        dacAllSubjectsListView.setItems(dacAllSubjectsObservableList);

    }

    private void initDACObjects() {
        dacAllObjects = dacDatabaseOperation.queryAllObjects();
        if (dacAllObjects != null) {
            dacAllObjectsObservableList.addAll(dacAllObjects.stream().map(ACLObject::getName).collect(Collectors.toList()));
        } else {
            dacAllObjectsObservableList.addAll("ObjectDemo");
        }
        dacAllObjectsListView.setItems(dacAllObjectsObservableList);

    }

    private void initDACCapabilities() {
        dacAllCapabilities = dacDatabaseOperation.queryAllCapabilities();
        if (dacAllCapabilities != null) {
            dacAllCapabilitiesObservableList.addAll(dacAllCapabilities.stream().map(Capability::getCapabilityId).collect(Collectors.toList()));
            dacAllCapabilitiesProperty = CapabilityProperty.capabilitiesToCapabilitiesProperty(dacAllCapabilities);
            dacAllCapabilitiesPropertyObservableList.addAll(dacAllCapabilitiesProperty);
        } else {
            dacAllCapabilitiesObservableList.addAll(1);
            dacAllCapabilitiesPropertyObservableList.addAll();
        }
        dacAllCapabilitiesListView.setItems(dacAllCapabilitiesObservableList);

        dacAllCapabilitiesTableView.setItems(dacAllCapabilitiesPropertyObservableList);
        dacAllCapabilitiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<CapabilityProperty,Integer> capabilityIdCol = new TableColumn<>("Capability ID");
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
        capabilityStringCol.setPrefWidth(150);

        TableColumn<CapabilityProperty, String> capabilityInfoCol = new TableColumn<>("Capability Info");
        capabilityInfoCol.setCellValueFactory(new PropertyValueFactory<CapabilityProperty, String>("capabilityInfo"));
        capabilityInfoCol.setPrefWidth(150);

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
        dacAllBlackTokens = dacDatabaseOperation.queryAllBlackTokens();
        if (dacAllBlackTokens != null) {
            dacAllBlackTokensObservableList.addAll(dacAllBlackTokens.stream().map(BlackToken::getBlackTokenId).collect(Collectors.toList()));
        } else {
            dacAllBlackTokensObservableList.addAll(1);
        }
        dacAllBlackTokensListView.setItems(dacAllBlackTokensObservableList);
    }



    private void initRunningLogPane() {


    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }

}
