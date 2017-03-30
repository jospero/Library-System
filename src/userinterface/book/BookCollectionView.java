package userinterface.book;

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
import model.Book;
import model.BookCollection;
import userinterface.MessageView;
import userinterface.TitleView;
import userinterface.View;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import static model.Book.getFields;

/**
 * Created by Sammytech on 3/12/17.
 */
public class BookCollectionView extends View {

    protected TableView<BookTableModel> tableOfBooks;
    protected Button cancelButton;
    protected Button viewButton;
    protected TableColumn authorColumn;
    protected MessageView statusLog;

    public BookCollectionView(IModel model) {
        super(model, "BookCollectionView");

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

        ObservableList<BookTableModel> tableData = FXCollections.observableArrayList();
        try
        {
            BookCollection bookCollection = (BookCollection)myModel.getState("BookList");

            Vector entryList = (Vector)bookCollection.getState("Books");
            Enumeration entries = entryList.elements();

            while (entries.hasMoreElements())
            {
                Book nextAccount = (Book)entries.nextElement();
                Vector<String> view = nextAccount.getEntryListView();

                // add this list entry to the list
                BookTableModel nextTableRowData = new BookTableModel(view);
                tableData.add(nextTableRowData);

            }

            tableOfBooks.setItems(tableData);
            tableOfBooks.getSortOrder().add(authorColumn);
            authorColumn.setSortable(false);
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

        Text prompt = new Text(Utilities.getStringLang("list_book"));
        prompt.setId("collection_head");
        prompt.setWrappingWidth(350);
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setFill(Color.BLACK);
        grid.add(prompt, 0, 0, 2, 1);

        HashMap<Book.DATABASE, String> fields = getFields();
        tableOfBooks = new TableView<BookTableModel>();
        tableOfBooks.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        TableColumn barcodeColumn = new TableColumn(fields.get(Book.DATABASE.Barcode)) ;
        barcodeColumn.setMinWidth(100);
        barcodeColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("barcode"));
        barcodeColumn.setSortable(false);

        TableColumn titleColumn = new TableColumn(fields.get(Book.DATABASE.Title)) ;
        titleColumn.setMinWidth(100);
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("title"));
        titleColumn.setSortable(false);


        TableColumn disciplineColumn = new TableColumn(fields.get(Book.DATABASE.Discipline)) ;
        disciplineColumn.setMinWidth(100);
        disciplineColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("discipline"));
        disciplineColumn.setSortable(false);


        authorColumn = new TableColumn(fields.get(Book.DATABASE.Authors)) ;
        authorColumn.setMinWidth(100);
        authorColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("authors"));
//        authorColumn.setSortType(TableColumn.SortType.ASCENDING);


        TableColumn pubColumn = new TableColumn(fields.get(Book.DATABASE.Publisher)) ;
        pubColumn.setMinWidth(120);
        pubColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("publisher"));
        pubColumn.setSortable(false);

        TableColumn pubYearColumn = new TableColumn(fields.get(Book.DATABASE.YearOfPublication)) ;
        pubYearColumn.setMinWidth(120);
        pubYearColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("yearOfPublication"));
        pubYearColumn.setSortable(false);


        TableColumn isbnColumn = new TableColumn(fields.get(Book.DATABASE.ISBN)) ;
        isbnColumn.setMinWidth(120);
        isbnColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("isbn"));
        isbnColumn.setSortable(false);


        TableColumn sugPriceColumn = new TableColumn(fields.get(Book.DATABASE.SuggestedPrice)) ;
        sugPriceColumn.setMinWidth(120);
        sugPriceColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("suggestedPrice"));
        sugPriceColumn.setSortable(false);


        TableColumn notesColumn = new TableColumn(fields.get(Book.DATABASE.Notes)) ;
        notesColumn.setMinWidth(120);
        notesColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("notes"));
        notesColumn.setSortable(false);

        TableColumn statusColumn = new TableColumn(fields.get(Book.DATABASE.Status));
        statusColumn.setMinWidth(100);
        statusColumn.setCellValueFactory(
                new PropertyValueFactory<BookTableModel, String>("status"));
        statusColumn.setSortable(false);
        tableOfBooks.getColumns().addAll(barcodeColumn, titleColumn, disciplineColumn, authorColumn, pubColumn,
                pubYearColumn, isbnColumn, sugPriceColumn, notesColumn, statusColumn);

        tableOfBooks.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<BookTableModel>() {
            @Override
            public void changed(ObservableValue<? extends BookTableModel> observable, BookTableModel oldValue, BookTableModel newValue) {
                if(tableOfBooks.getSelectionModel().getSelectedItem() != null){
                    viewButton.setDisable(false);
                }
            }
        });

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(200, 150);
        scrollPane.setContent(tableOfBooks);


        viewButton = new Button("View Book Information");
        viewButton.setDisable(true);

        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewBook", tableOfBooks.getSelectionModel().getSelectedIndex());
            }
        });

        cancelButton = new Button("Back to Search");

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
                myModel.stateChangeRequest("CancelBookList", null);
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

