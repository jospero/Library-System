package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Book;
import userinterface.SearchView;
import userinterface.View;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import static model.Book.getFields;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchBookView extends SearchView<Book.DATABASE> {

    private JFXTextField barcode;
    private JFXTextField bookTitle;
    private JFXTextField author;
    private JFXTextField publisher;
    private JFXTextField pubYear;
    private JFXTextField isbn;
    private ComboBox condition;
    private JFXTextField sugPrice;
    private static final int MAX_COLUMN = 2;

    public SearchBookView(IModel model) {
        super(model, "SearchBookView");

    }

    protected GridPane createSearch(){
        barcode = new JFXTextField();
        bookTitle = new JFXTextField();
        author = new JFXTextField();
        publisher = new JFXTextField();
        pubYear = new JFXTextField();
        isbn = new JFXTextField();
        sugPrice = new JFXTextField();


        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100/MAX_COLUMN);
        for(int i = 0; i < MAX_COLUMN; i++){
            grid.getColumnConstraints().add(col1);
        }

        int line = 0;
        int row = line / MAX_COLUMN;
        int col = line % MAX_COLUMN;
        String barcodeStr = fields.get(Book.DATABASE.Barcode);
        setupField(barcode, barcodeStr);
        grid.add(barcode, col, row,1,1);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String titleStr = fields.get(Book.DATABASE.Title);
        setupField(bookTitle, titleStr);
        grid.add(bookTitle, col, row,1,1);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String authorStr = fields.get(Book.DATABASE.Authors);
        setupField(author, authorStr);
        grid.add(author, col, row);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String publisherStr = fields.get(Book.DATABASE.Publisher);
        setupField(publisher, publisherStr);
        grid.add(publisher, col, row);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String pubYearStr = fields.get(Book.DATABASE.YearOfPublication);
        setupField(pubYear, pubYearStr);
        grid.add(pubYear, col, row);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String isbnStr = fields.get(Book.DATABASE.ISBN);
        setupField(isbn, isbnStr);
        grid.add(isbn, col, row);

        line++;
        row = line / MAX_COLUMN;
        col = line % MAX_COLUMN;
        String sugPriceStr = fields.get(Book.DATABASE.SuggestedPrice);
        setupField(sugPrice, sugPriceStr);
        grid.add(sugPrice, col, row);

        return grid;
    }

    private void setupField(JFXTextField node, String prompt){
        node.setPadding(new Insets(30, 0, 30, 0));
        node.setLabelFloat(true);
        node.setPromptText(prompt);
    }



    protected Properties validateSearch(){
        Properties search = new Properties();
        String bookTitleStr = bookTitle.getText();
        String authorStr = author.getText();
        String publisherStr = publisher.getText();
        String pubYearStr = pubYear.getText();
        String isbnStr = isbn.getText();
        String sugPriceStr = sugPrice.getText();
        if(barcode.getText().trim().isEmpty()){

            if(bookTitleStr.trim().isEmpty() && authorStr.trim().isEmpty() && publisherStr.trim().isEmpty() && pubYearStr.trim().isEmpty() &&
                    isbnStr.trim().isEmpty() && sugPriceStr.trim().isEmpty()){
//                Error
            } else{
                if(!bookTitleStr.trim().isEmpty()){
                    search.setProperty(Book.DATABASE.Title.name(), bookTitleStr);
                }
                if(!authorStr.trim().isEmpty()){
                    search.setProperty(Book.DATABASE.Authors.name(), authorStr);
                }
                if(!publisherStr.trim().isEmpty()){
                    search.setProperty(Book.DATABASE.Publisher.name(), publisherStr);
                }
                if(!pubYearStr.trim().isEmpty() && pubYearStr.matches("[0-9]+")){
                    search.setProperty(Book.DATABASE.YearOfPublication.name(), pubYearStr);
                }
                if(!isbnStr.trim().isEmpty() && isbnStr.matches("[0-9]+")){
                    search.setProperty(Book.DATABASE.ISBN.name(), isbnStr);
                }
                if(!sugPriceStr.trim().isEmpty()){
                    try {
                        double d = Double.parseDouble(sugPriceStr);
                        search.setProperty(Book.DATABASE.SuggestedPrice.name(), sugPriceStr);
                    } catch (NumberFormatException ex){

                    }
                }
            }
        } else{
            String str = barcode.getText();
            if(bookTitleStr.trim().isEmpty() && authorStr.trim().isEmpty() && publisherStr.trim().isEmpty() && pubYearStr.trim().isEmpty() &&
                    isbnStr.trim().isEmpty() && sugPriceStr.trim().isEmpty()){
                if (str.matches("[0-9]+")) {
                    search.setProperty(Book.DATABASE.Barcode.name(), str);
                } else {
                    //Error
                }
            } else {

            }
        }
        return search;
    }

    @Override
    protected HashMap<Book.DATABASE, String> getFields() {
        return Book.getFields();
    }

    @Override
    protected ListView getSearchResult() {
        ListView<BookTableModel> listView = new ListView<>();
        listView.setCellFactory(new Callback<ListView<BookTableModel>, ListCell<BookTableModel>>() {
            @Override
            public ListCell<BookTableModel> call(ListView<BookTableModel> param) {
                return new BookListViewCell();
            }
        });
        return listView;
    }

    @Override
    protected void UpdateSearchResult(Object value) {
        ObservableList<BookTableModel> tableData = FXCollections.observableArrayList();
        Vector entryList = (Vector)value;
        Enumeration entries = entryList.elements();

        while (entries.hasMoreElements())
        {
            Book nextBook = (Book)entries.nextElement();
            Vector<String> view = nextBook.getEntryListView();

            // add this list entry to the list
            BookTableModel nextRowData = new BookTableModel(view);
            tableData.add(nextRowData);

        }
        searchResult.setItems(tableData);
        searchResult.setVisible(true);
    }

}
