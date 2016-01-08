package com.github.soukie.view;

import com.github.soukie.MainAPP;
import com.github.soukie.model.database.DACDatabaseOperation;
import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.ModelValues;
import com.sun.org.apache.xpath.internal.operations.String;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

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
    private MenuItem editMenuDelete;
    @FXML
    private MenuItem editMenuModifyAdminInfo;

    @FXML
    private Menu helpMenu;
    @FXML
    private MenuItem helpMenuAbout;
    @FXML
    private MenuItem helpMenuContact;
    @FXML
    private MenuItem helpMenuHelp;


    @FXML
    private TitledPane dacAllSubjetsTitledPane;
    @FXML
    private TitledPane dacAllObjectsTitledPane;
    @FXML
    private TitledPane dacALLCapabilityTitledPane;

    @FXML
    private TreeTableView dacCapabilitiesTableView;

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
     * All subjects as an observable list of Subjects
     */
    private ObservableList<String> dacAllSubjectsNameObservableList = FXCollections.observableArrayList();

    /**
     * All objects as an observable list of Objects
     */
    private ObservableList<String> dacAllObjectsNameObservableList = FXCollections.observableArrayList();




    /**
     * DAC Database Handle.
     */
    private DACDatabaseOperation dacDatabaseOperation;


    /**
     * DAC all subjects and objects ArrayList
     */
    private ArrayList<ACLSubject> dacAllSubjects;
    private ArrayList<ACLObject> dacAllObjects;

    /**
     * Empty constructor.
     */
    public MainWindowController() {

    }

    @FXML
    public void initialize() {
        //Init MenuBar.
        initMenuBar();

        initDatabaseOperation();

        initDACSubjects();
        initDACObjects();

    }

    private void initMenuBar() {
        fileMenuClose.setOnAction(event -> {
            dacDatabaseOperation.closeConnection();
            System.exit(0);
        });

        editMenuClearLog.setOnAction(event -> {

        });

        editMenuDelete.setOnAction(event -> {

        });

        editMenuModifyAdminInfo.setOnAction(event -> {
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
    private void initDatabaseOperation() {
        dacDatabaseOperation = new DACDatabaseOperation(new Date().getTime());
        try {
            dacDatabaseOperation.initDatabaseConnection(ModelValues.DATABASE_MYSQL_PROPERTIES_FILE_PATH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initDACSubjects() {
        dacAllSubjects = dacDatabaseOperation.queryAllSubjects();
        if (dacAllSubjects != null) {
            //dacAllSubjectsNameObservableList.addAll(dacAllSubjects.stream().map(ACLSubject::getsName).collect(Collectors.toList()));
        } else {
            //dacAllSubjectsNameObservableList.addAll();
        }
        dacAllObjectsTitledPane.setContent(new ImageView(new Image("file:res/Soukie_l.png")));


    }

    private void initDACObjects() {
        dacAllObjects = dacDatabaseOperation.queryAllObjects();
        if (dacAllObjects != null) {
            //dacAllObjectsNameObservableList.addAll(dacAllObjects.stream().map(ACLObject::getoName).collect(Collectors.toList()));
        } else {

            //dacAllObjectsNameObservableList.addAll("Empty", "Empty", "Empty");
        }
        dacAllObjectsTitledPane.setContent(new ImageView(new Image("file:res/Soukie_l.png")));

    }



    private void initRunningLogPane() {


    }

    public void setMainAPP(MainAPP mainAPP) {
        this.mainAPP = mainAPP;
    }

}
