package userinterface.worker;

import Utilities.Utilities;
import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import model.Worker;
import model.WorkerCollection;
import userinterface.MessageView;
import userinterface.TitleView;
import userinterface.View;
import userinterface.worker.WorkerTableModel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import static model.Worker.getFields;

/**
 * Created by Sammytech on 3/12/17.
 */
public class WorkerCollectionView extends View {

    protected TableView<WorkerTableModel> tableOfWorkers;
    protected Button cancelButton;
    protected Button viewButton;
    protected TableColumn phoneColumn;
    protected MessageView statusLog;

    public WorkerCollectionView(IModel model) {
        super(model, "WorkerCollectionView");

        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(TitleView.createTitle("Brockport"));
        container.getChildren().add(createFormContent());

        // Error message area
        container.getChildren().add(createStatusLog("                                            "));

        getChildren().add(container);

        populateFields();
    }

    //--------------------------------------------------------------------------
    protected void populateFields()
    {
        getEntryTableModelValues();
    }

    //--------------------------------------------------------------------------
    protected void getEntryTableModelValues()
    {

        ObservableList<WorkerTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            WorkerCollection workerCollection = (WorkerCollection)myModel.getState("WorkerList");

            Vector entryList = (Vector)workerCollection.getState("Workers");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements())
            {
                Worker nextAccount = (Worker)entries.nextElement();
                Vector<String> view = nextAccount.getEntryListView();

                // add this list entry to the list
                WorkerTableModel nextTableRowData = new WorkerTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfWorkers.setItems(tableData);
            tableOfWorkers.getSortOrder().add(phoneColumn);
            phoneColumn.setSortable(false);
        }
        catch (Exception e) {//SQLException e) {
            // Need to handle this exception
            e.printStackTrace();
        }
    }

    private VBox createFormContent()
    {
        VBox vbox = new VBox(10);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text prompt = new Text(Utilities.getStringLang("list_workers"));
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        HashMap<Worker.DATABASE, String> fields = getFields();
        tableOfWorkers = new TableView<WorkerTableModel>();
        tableOfWorkers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn bannerIdColumn = new TableColumn(fields.get(Worker.DATABASE.BannerId)) ;
        bannerIdColumn.setMinWidth(100);
        bannerIdColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("bannerId"));
        bannerIdColumn.setSortable(false);

        TableColumn firstNameColumn = new TableColumn(fields.get(Worker.DATABASE.FirstName)) ;
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("firstName"));
        firstNameColumn.setSortable(false);


        TableColumn disciplineColumn = new TableColumn(fields.get(Worker.DATABASE.LastName)) ;
        disciplineColumn.setMinWidth(100);
        disciplineColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("lastName"));
        disciplineColumn.setSortable(false);


        phoneColumn = new TableColumn(fields.get(Worker.DATABASE.Phone)) ;
        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("phone"));
//        phoneColumn.setSortType(TableColumn.SortType.ASCENDING);


        TableColumn emailColumn = new TableColumn(fields.get(Worker.DATABASE.Email)) ;
        emailColumn.setMinWidth(120);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("email"));
        emailColumn.setSortable(false);

        TableColumn credentialsColumn = new TableColumn(fields.get(Worker.DATABASE.Credentials)) ;
        credentialsColumn.setMinWidth(120);
        credentialsColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("credentials"));
        credentialsColumn.setSortable(false);


        TableColumn dateOfLastestCredentialStatusColumn = new TableColumn(fields.get(Worker.DATABASE.DateOfLatestCredentialStatus)) ;
        dateOfLastestCredentialStatusColumn.setMinWidth(120);
        dateOfLastestCredentialStatusColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("dateOfLastestCredentialStatus"));
        dateOfLastestCredentialStatusColumn.setSortable(false);


        TableColumn dateOfHireColumn = new TableColumn(fields.get(Worker.DATABASE.DateOfHire)) ;
        dateOfHireColumn.setMinWidth(120);
        dateOfHireColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("dateOfHire"));
        dateOfHireColumn.setSortable(false);


        TableColumn statusColumn = new TableColumn(fields.get(Worker.DATABASE.Status)) ;
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<WorkerTableModel, String>("status"));
        statusColumn.setSortable(false);

        tableOfWorkers.getColumns().addAll(bannerIdColumn, firstNameColumn, disciplineColumn, phoneColumn, emailColumn,
                credentialsColumn, dateOfLastestCredentialStatusColumn, dateOfHireColumn, statusColumn);

        tableOfWorkers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WorkerTableModel>() {
            @Override
            public void changed(ObservableValue<? extends WorkerTableModel> observable, WorkerTableModel oldValue, WorkerTableModel newValue) {
                if(tableOfWorkers.getSelectionModel().getSelectedItem() != null){
                    viewButton.setDisable(false);
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(200, 150);
        scrollPane.setContent(tableOfWorkers);


        viewButton = new Button(Utilities.getStringLang("view_worker_info"));
        viewButton.setDisable(true);

        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewWorker", tableOfWorkers.getSelectionModel().getSelectedIndex());
            }
        });

        cancelButton = new Button(Utilities.getStringLang("back_to_search"));

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                /**
                 * Process the Cancel button.
                 * The ultimate result of this action is that the transaction will tell the teller to
                 * to switch to the transaction choice view. BUT THAT IS NOT THIS VIEW'S CONCERN.
                 * It simply tells its model (controller) that the transaction was canceled, and leaves it
                 * to the model to decide to tell the teller to do the switch back.
                 */
                //----------------------------------------------------------
                clearErrorMessage();
                myModel.stateChangeRequest("CancelWorkerList", null);
            }
        });

        HBox btnContainer = new HBox(100);
        btnContainer.setAlignment(Pos.CENTER);
        btnContainer.getChildren().add(viewButton);
        btnContainer.getChildren().add(cancelButton);

        vbox.getChildren().add(grid);
        vbox.getChildren().add(scrollPane);
        vbox.getChildren().add(btnContainer);

        return vbox;
    }

    //--------------------------------------------------------------------------
    protected MessageView createStatusLog(String initialMessage)
    {
        statusLog = new MessageView(initialMessage);

        return statusLog;
    }


    /**
     * Display info message
     */
    //----------------------------------------------------------
    public void displayMessage(String message)
    {
        statusLog.displayMessage(message);
    }

    /**
     * Clear error message
     */
    //----------------------------------------------------------
    public void clearErrorMessage()
    {
        statusLog.clearErrorMessage();
    }
    @Override
    public void updateState(String key, Object value) {

    }
}

