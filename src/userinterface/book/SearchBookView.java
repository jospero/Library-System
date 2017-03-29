package userinterface.book;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Book;
import userinterface.View;

import java.util.HashMap;
import java.util.Properties;

import static model.Book.getFields;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchBookView extends View {

    TextField barcode = new TextField();
    TextField bookTitle = new TextField();
    TextField author = new TextField();
    TextField publisher = new TextField();
    TextField pubYear = new TextField();
    TextField isbn = new TextField();
    ComboBox condition;
    TextField sugPrice = new TextField();

    public SearchBookView(IModel model) {
        super(model, "SearchBookView");

        VBox root = new VBox();
//        root.setFillWidth(true);
        root.setAlignment(Pos.CENTER);

        HashMap<Book.DATABASE, String> fields = getFields();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,60,0,60));
        grid.setHgap(30);
        grid.setVgap(10);
//        grid.setStyle("-fx-background-color: #580e7a");

//        HBox title = TitleView.createTitle("Enter Book Information");
//        title.setStyle("-fx-background-color:#0c7a79");
//        GridPane.setHgrow(title, Priority.ALWAYS);
        Label title = new Label("Enter Book Information");
        title.setPrefWidth(Double.MAX_VALUE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setPadding(new Insets(0,0,20,0));
        title.setAlignment(Pos.CENTER);
        grid.add(title,0,0,2,1);

        String barcodeStr = fields.get(Book.DATABASE.Barcode);
        Label barcodeLabel = new Label(barcodeStr);
        barcode.setPromptText(barcodeStr);
        grid.add(barcodeLabel, 0, 1);
        grid.add(barcode, 1, 1);

        VBox oneFieldHead = new VBox();
        Label oneFieldOr = new Label("OR");
        Label oneFieldText = new Label("(ENTER AT LEAST ONE FIELD)");

        oneFieldOr.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        oneFieldText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        oneFieldHead.setPadding(new Insets(20,0,20,0));
        oneFieldHead.setAlignment(Pos.CENTER);

        oneFieldHead.getChildren().add(oneFieldOr);
        oneFieldHead.getChildren().add(oneFieldText);

        grid.add(oneFieldHead,0,2,2,1);


        int row = 3;
        String titleStr = fields.get(Book.DATABASE.Title);
        Label titleLabel = new Label(titleStr);
        bookTitle.setPromptText(titleStr);
        grid.add(titleLabel, 0, row);
        grid.add(bookTitle, 1, row);

        row++;
        String authorStr = fields.get(Book.DATABASE.Authors);
        Label authorLabel = new Label(authorStr);
        author.setPromptText(authorStr);
        grid.add(authorLabel, 0, row);
        grid.add(author, 1, row);

        row++;
        String publisherStr = fields.get(Book.DATABASE.Publisher);
        Label pubLabel = new Label(publisherStr);
        publisher.setPromptText(publisherStr);
        grid.add(pubLabel, 0, row);
        grid.add(publisher, 1, row);

        row++;
        String pubYearStr = fields.get(Book.DATABASE.YearOfPublication);
        Label pubYearLabel = new Label(pubYearStr);
        pubYear.setPromptText(pubYearStr);
        grid.add(pubYearLabel, 0, row);
        grid.add(pubYear, 1, row);

        row++;
        String isbnStr = fields.get(Book.DATABASE.ISBN);
        Label isbnLabel = new Label(isbnStr);
        isbn.setPromptText(isbnStr);
        grid.add(isbnLabel, 0, row);
        grid.add(isbn, 1, row);

//        row++;
//        String conditionStr = "Condition";
//        Label conditionLabel = new Label(conditionStr);
//        condition.getItems().addAll("Good","Damaged");
//        grid.add(conditionLabel, 0, row);
//        grid.add(condition, 1, row);

        row++;
        String sugPriceStr = fields.get(Book.DATABASE.SuggestedPrice);
        Label sugPriceLabel = new Label(sugPriceStr);
        sugPrice.setPromptText(sugPriceStr);
        grid.add(sugPriceLabel, 0, row);
        grid.add(sugPrice, 1, row);

        row++;
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        Button searchButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                MainStageContainer.getInstance().close();
//                MainStageContainer.setStage(new Stage(), "Here");
//                MainStageContainer.getInstance().show();
                myModel.stateChangeRequest("SearchBookCancelled", null);
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processSearch();

            }
        });
        buttonPane.setSpacing(20);
        buttonPane.setPadding(new Insets(20));
        buttonPane.getChildren().addAll(searchButton, cancelButton);

        grid.add(buttonPane, 0, row, 2, 1);


        root.getChildren().add(grid);
        getChildren().add(root);

    }

    private void processSearch() {
        Properties prop = validateSearch();
        if(prop.size() > 0){
            myModel.stateChangeRequest("ProcessSearch", prop);
        }

    }

    private Properties validateSearch(){
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
    public void updateState(String key, Object value) {

    }
}
