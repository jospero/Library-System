package userinterface.book;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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

    public BookListViewCell() {
        configureLayout();
        configureBookView();
        configureTitle();
        configureAuthor();
        configureYearOfPub();
        addControlsToLayout();

    }

    private void configureBookView() {
        bookView = new ImageView();
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
    }

    private void configureAuthor() {
        authorLabel = new Label();
    }

    private void configureTitle() {
        titleLabel = new Label();

    }

    private void configureLayout() {
        layout = new HBox();
    }

    @Override
    protected void updateItem(BookTableModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            clearContent();
        } else {
            addContent(item);
        }
    }

    private void addContent(BookTableModel item) {
        setText(null);
        System.out.println(item.getTitle());
        titleLabel.setText(item.getTitle());
        authorLabel.setText(item.getAuthors());
        yearOfPubLabel.setText(item.getYearOfPublication());
        setGraphic(layout);
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }
}
