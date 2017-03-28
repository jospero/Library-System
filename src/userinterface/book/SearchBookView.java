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
import userinterface.View;

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

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,60,0,60));
        grid.setHgap(30);
        grid.setVgap(10);
//        grid.setStyle("-fx-background-color: #580e7a");

//        HBox title = TitleView.createTitle("Enter Book Information");
//        title.setStyle("-fx-background-color:#0c7a79");
//        GridPane.setHgrow(title, Priority.ALWAYS);
        Label title = new Label(messages.getString("search_book"));
        title.setPrefWidth(Double.MAX_VALUE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setPadding(new Insets(0,0,20,0));
        title.setAlignment(Pos.CENTER);
        grid.add(title,0,0,2,1);

        String barcodeStr = messages.getString("barcode");
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
        String titleStr = messages.getString("title");
        Label titleLabel = new Label(titleStr);
        bookTitle.setPromptText(titleStr);
        grid.add(titleLabel, 0, row);
        grid.add(bookTitle, 1, row);

        row++;
        String authorStr = messages.getString("auth");
        Label authorLabel = new Label(authorStr);
        author.setPromptText(authorStr);
        grid.add(authorLabel, 0, row);
        grid.add(author, 1, row);

        row++;
        String publisherStr = messages.getString("pub");
        Label pubLabel = new Label(publisherStr);
        publisher.setPromptText(publisherStr);
        grid.add(pubLabel, 0, row);
        grid.add(publisher, 1, row);

        row++;
        String pubYearStr = messages.getString("year_pub");
        Label pubYearLabel = new Label(pubYearStr);
        pubYear.setPromptText(pubYearStr);
        grid.add(pubYearLabel, 0, row);
        grid.add(pubYear, 1, row);

        row++;
        String isbnStr = messages.getString("isbn");
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
        String sugPriceStr = messages.getString("sug_price");
        Label sugPriceLabel = new Label(sugPriceStr);
        sugPrice.setPromptText(sugPriceStr);
        grid.add(sugPriceLabel, 0, row);
        grid.add(sugPrice, 1, row);

        row++;
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        Button searchButton = new Button(messages.getString("sub_btn"));
        Button cancelButton = new Button(messages.getString("cancel_btn"));

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
        buttonPane.getChildren().addAll(searchButton, cancelButton);

        grid.add(buttonPane, 0, row, 2, 1);


        root.getChildren().add(grid);
        getChildren().add(root);

    }

    private void processSearch() {

        myModel.stateChangeRequest("ProcessSearch", null);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
