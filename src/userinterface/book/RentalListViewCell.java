package userinterface.book;

import Utilities.Utilities;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.rmi.CORBA.Util;

/**
 * Created by Sammytech on 4/6/17.
 */
public class RentalListViewCell extends ListCell<RentalTableModel> {

    private ImageView bookView;
    private Label titleLabel;
    private Label barcodeLabel;
    private Label dateCheckOutLabel;
    private Label dueDateLabel;
    private Label bannerIdBorrowerLabel;
    private Label fullBorrowerLabel;
    private Label bannerIdWorkerLabel;
    private Label fullWorkerLabel;

    private HBox layout;
    private static double HEIGHT = 300;
    private static double VPAD = 30;

    private void configureBookView() {
        bookView = new ImageView();
        Image img = new Image(getClass().getClassLoader().getResourceAsStream("resources/images/nocover2.gif"));
        bookView.setImage(img);
        bookView.setFitHeight(HEIGHT/2.5);
        bookView.setFitWidth(70);
    }

    private void addControlsToLayout() {
        layout.getChildren().add(bookView);
        VBox infoBox = new VBox();
        infoBox.getChildren().add(titleLabel);
        infoBox.getChildren().add(barcodeLabel);
        infoBox.getChildren().add(dateCheckOutLabel);
        infoBox.getChildren().add(dueDateLabel);
        infoBox.getChildren().add(bannerIdBorrowerLabel);
        infoBox.getChildren().add(fullBorrowerLabel);
        infoBox.getChildren().add(bannerIdWorkerLabel);
        infoBox.getChildren().add(fullWorkerLabel);
        layout.getChildren().add(infoBox);
    }

    private void configureTitle() {
        titleLabel = new Label();
        titleLabel.setId("title");
        titleLabel.getStyleClass().add("title");
    }

    private void configureBarcode() {
        barcodeLabel = new Label();
        barcodeLabel.getStyleClass().add("year");
    }

    private void configureDateCheckOut() {
        dateCheckOutLabel = new Label();
        dateCheckOutLabel.getStyleClass().add("year");
    }

    private void configureDueDate() {
        dueDateLabel = new Label();
        dueDateLabel.getStyleClass().add("year");
    }

    private void configureBannerIdBorrower() {
        bannerIdBorrowerLabel = new Label();
        bannerIdBorrowerLabel.getStyleClass().add("author");
    }

    private void configureFullBorrower() {
        fullBorrowerLabel = new Label();
        fullBorrowerLabel.getStyleClass().add("author");


    }
    private void configureBannerIdWorker() {
        bannerIdWorkerLabel = new Label();
        bannerIdWorkerLabel.getStyleClass().add("author");

    }
    private void configureFullWorker() {
        fullWorkerLabel = new Label();
        fullWorkerLabel.getStyleClass().add("author");

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
    protected void updateItem(RentalTableModel item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            clearContent();
        } else {
            if(item != null) {
                addContent(item);
            }
        }
    }

    private void addContent(RentalTableModel item) {
        setText(null);
        configureLayout();
        configureTitle();
        configureBookView();
        configureBannerIdBorrower();
        configureBannerIdWorker();
        configureBarcode();
        configureDateCheckOut();
        configureDueDate();
        configureFullBorrower();
        configureFullWorker();
        addControlsToLayout();

//        System.out.println(item.getTitle());
//        titleLabel.setText(Utilities.convertToTitleCase(item.getTitle()));
        titleLabel.setText(Utilities.getStringLang("title")+ ": "+item.getTitle().toUpperCase());
        barcodeLabel.setText(Utilities.getStringLang("barcode")+ ": "+item.getBarcode());
        dateCheckOutLabel.setText(Utilities.getStringLang("date_checkout")+ ": " + item.getDateCheckOut());
        dueDateLabel.setText(Utilities.getStringLang("duedate")+ ": " + item.getDateDue());
        bannerIdBorrowerLabel.setText(Utilities.getStringLang("sb_id")+ ": " + item.getBannerIdBorrower());
        fullBorrowerLabel.setText(Utilities.getStringLang("sb")+ ": " + item.getFirstNameBorrower() + " " + item.getLastNameBorrower());
        bannerIdWorkerLabel.setText(Utilities.getStringLang("worker_id") +": "+ item.getBannerIdWorker());
        fullWorkerLabel.setText(Utilities.getStringLang("worker")+": "+ item.getFirstNameWorker() + " "+ item.getLastNameWorker());
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
