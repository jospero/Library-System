package userinterface.book;

import Utilities.Utilities;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 4/6/17.
 */
public class BookListViewCell extends ListCell<BookTableModel> {

    private ImageView bookView;
    private Label titleLabel;
    private Label authorLabel;
    private Label yearOfPubLabel;
    private HBox layout;
    private static double HEIGHT = 160;
    private static double VPAD = 30;

    private void configureBookView() {
        bookView = new ImageView();
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("resources/images/nocover2.gif"));
        bookView.setImage(img);
        bookView.setFitHeight(HEIGHT-VPAD);
        bookView.setFitWidth(70);
    }

    private void addControlsToLayout() {
        layout.getChildren().add(bookView);
        VBox infoBox = new VBox();
        infoBox.getChildren().add(titleLabel);
        infoBox.getChildren().add(authorLabel);
        infoBox.getChildren().add(yearOfPubLabel);
        layout.getChildren().add(infoBox);
    }

    private void configureYearOfPub() {
        yearOfPubLabel = new Label();
        yearOfPubLabel.getStyleClass().add("year");
    }

    private void configureAuthor() {
        authorLabel = new Label();
        authorLabel.getStyleClass().add("author");
    }

    private void configureTitle() {
        titleLabel = new Label();
        titleLabel.setId("title");
        titleLabel.getStyleClass().add("title");

    }

    private void configureLayout() {
        layout = new HBox();
        layout.setSpacing(20);
        layout.setPadding(new Insets(VPAD/2));
        layout.setPrefHeight(HEIGHT);
        layout.setMaxHeight(HEIGHT);
        layout.setMinHeight(HEIGHT);
        layout.setId("outerBox");
    }

    @Override
    protected void updateItem(BookTableModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            clearContent();
        } else {
            if(item != null) {
                addContent(item);
            }
        }
    }

    private void addContent(BookTableModel item) {
        setText(null);
        configureLayout();
        configureBookView();
        configureTitle();
        configureAuthor();
        configureYearOfPub();
        addControlsToLayout();

//        System.out.println(item.getTitle());
//        titleLabel.setText(Utilities.convertToTitleCase(item.getTitle()));
        titleLabel.setText(item.getTitle().toUpperCase());
        authorLabel.setText("By: " + item.getAuthors().toUpperCase());
        yearOfPubLabel.setText(item.getYearOfPublication());
        setGraphic(layout);
    }

    public static double getCustomHeight(){
        return HEIGHT;
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
}
