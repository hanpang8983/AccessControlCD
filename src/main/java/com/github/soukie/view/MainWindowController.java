package com.github.soukie.view;

import com.github.soukie.model.DACPolicy.database.DACDatabaseOperation;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.ModelValues;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by qiyiy on 2016/1/7.
 */
public class MainWindowController {

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
    @FXML MenuItem helpMenuAbout;
    @FXML
    private MenuItem helpMenuContact;
    @FXML
    private MenuItem helpMenuHelp;

    /**
     * DAC policy panes.
     */
    @FXML
    private MenuBar DACSubjectsMenuBar;
    @FXML
    private Menu DACSubjectMenuDACPolicy;

    @FXML
    private TitledPane DACSubjectsTitledPane;
    @FXML
    private AnchorPane DACSubjectsAnchorPane;
    @FXML
    private ListView<String> DACSubjectsListView;

    /**
     * All subjects as an observable list of Subjects
     */
    private ObservableList<String> DACAllSubjectsData = FXCollections.observableArrayList();

    @FXML
    private TreeTableView DACCapabilitiesTableView;

    @FXML
    private ScrollPane DACCapabilityDetailsScrollPane;


    /**
     * DAC Database Handle.
     */
    private DACDatabaseOperation DACDACDatabaseOperation;

    /**
     * DAC all subjects ArrayList
     */
    private ArrayList<ACLSubject> DACAllSubjects;

    /**
     * Empty constructor.
     */
    public MainWindowController() {

    }

    @FXML
    public void initialize() {

        DACDACDatabaseOperation = new DACDatabaseOperation(new Date().getTime());
        try {
            DACDACDatabaseOperation.initDatabaseConnection(ModelValues.DATABASE_MYSQL_PROPERTIES_FILE_PATH);
            DACAllSubjects = DACDACDatabaseOperation.queryAllSubjects();
            DACAllSubjectsData.addAll(DACAllSubjects.stream().map(DACAllSubject -> (DACAllSubject).getsName()).collect(Collectors.toList()));
            DACSubjectsListView.setItems(DACAllSubjectsData);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        fileMenuClose.setOnAction(event -> {

        });
    }
}
