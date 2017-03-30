package userinterface.studentborrower;

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
import model.StudentBorrower;
import model.StudentBorrowerCollection;
import userinterface.MessageView;
import userinterface.TitleView;
import userinterface.View;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class StudentBorrowerCollectionView extends View {

    protected TableView<StudentBorrowerTableModel> tableOfStudentBorrowers;
    protected Button cancelButton;
    protected Button viewButton;
    protected TableColumn phoneColumn;
    protected MessageView statusLog;

    public StudentBorrowerCollectionView(IModel model) {
        super(model, "StudentBorrowerCollectionView");

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

        ObservableList<StudentBorrowerTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            StudentBorrowerCollection studentBorrowerCollection = (StudentBorrowerCollection)myModel.getState("StudentBorrowerList");

            Vector entryList = (Vector)studentBorrowerCollection.getState("StudentBorrowers");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements())
            {
                StudentBorrower nextAccount = (StudentBorrower)entries.nextElement();
                Vector<String> view = nextAccount.getEntryListView();

                // add this list entry to the list
                StudentBorrowerTableModel nextTableRowData = new StudentBorrowerTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfStudentBorrowers.setItems(tableData);
            tableOfStudentBorrowers.getSortOrder().add(phoneColumn);
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

        Text prompt = new Text(Utilities.getStringLang("list_sb"));
        prompt.setId("collection_head");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        tableOfStudentBorrowers = new TableView<StudentBorrowerTableModel>();
        tableOfStudentBorrowers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn bannerIdColumn = new TableColumn(Utilities.getStringLang("bid")) ;
        bannerIdColumn.setMinWidth(100);
        bannerIdColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("bannerId"));
        bannerIdColumn.setSortable(false);

        TableColumn firstNameColumn = new TableColumn(Utilities.getStringLang("fname")) ;
        firstNameColumn.setMinWidth(100);
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("firstName"));
        firstNameColumn.setSortable(false);


        TableColumn lastNameColumn = new TableColumn(Utilities.getStringLang("lname")) ;
        lastNameColumn.setMinWidth(100);
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("lastName"));
        lastNameColumn.setSortable(false);


        phoneColumn = new TableColumn(Utilities.getStringLang("phone_num")) ;
        phoneColumn.setMinWidth(100);
        phoneColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("phone"));
//        phoneColumn.setSortType(TableColumn.SortType.ASCENDING);


        TableColumn emailColumn = new TableColumn(Utilities.getStringLang("email")) ;
        emailColumn.setMinWidth(120);
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("email"));
        emailColumn.setSortable(false);

        TableColumn borrowerStatusColumn = new TableColumn(Utilities.getStringLang("sb_status")) ;
        borrowerStatusColumn.setMinWidth(120);
        borrowerStatusColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("borrowerStatus"));
        borrowerStatusColumn.setSortable(false);


        TableColumn dateOfLastBorrowerStatusColumn = new TableColumn(Utilities.getStringLang("date_latest_sb_status")) ;
        dateOfLastBorrowerStatusColumn.setMinWidth(120);
        dateOfLastBorrowerStatusColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("dateOfLastBorrowerStatus"));
        dateOfLastBorrowerStatusColumn.setSortable(false);


        TableColumn dateOfRegistrationColumn = new TableColumn(Utilities.getStringLang("date_reg")) ;
        dateOfRegistrationColumn.setMinWidth(120);
        dateOfRegistrationColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("dateOfRegistration"));
        dateOfRegistrationColumn.setSortable(false);

        TableColumn notesColumn = new TableColumn(Utilities.getStringLang("notes")) ;
        notesColumn.setMinWidth(120);
        notesColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("notes"));
        notesColumn.setSortable(false);

        TableColumn statusColumn = new TableColumn(Utilities.getStringLang("status")) ;
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<StudentBorrowerTableModel, String>("status"));
        statusColumn.setSortable(false);

        tableOfStudentBorrowers.getColumns().addAll(bannerIdColumn, firstNameColumn, lastNameColumn, phoneColumn, emailColumn,
                borrowerStatusColumn, dateOfLastBorrowerStatusColumn, dateOfRegistrationColumn, notesColumn, statusColumn);

        tableOfStudentBorrowers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<StudentBorrowerTableModel>() {
            @Override
            public void changed(ObservableValue<? extends StudentBorrowerTableModel> observable, StudentBorrowerTableModel oldValue, StudentBorrowerTableModel newValue) {
                if(tableOfStudentBorrowers.getSelectionModel().getSelectedItem() != null){
                    viewButton.setDisable(false);
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(200, 150);
        scrollPane.setContent(tableOfStudentBorrowers);


        viewButton = new Button(Utilities.getStringLang("view_sb_info"));
        viewButton.setDisable(true);

        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewStudentBorrower", tableOfStudentBorrowers.getSelectionModel().getSelectedIndex());
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
                myModel.stateChangeRequest("CancelStudentBorrowerList", null);
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

