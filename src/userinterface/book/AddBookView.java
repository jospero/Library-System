package userinterface.book;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Book;
import userinterface.TitleView;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddBookView extends BookInformationView {

    public AddBookView(IModel model) {
        super(model, true, "AddBookView");

        myModel.subscribe("UpdateStatusMessage", this);

    }

    private void processBook() {
        Properties book = validateBook();
        if(book.size() > 0 ){
            myModel.stateChangeRequest("ProcessNewBook", book);
        }
    }

    void clearFields(){
        for(Book.DATABASE fEnum : Book.DATABASE.values()){
            if(fieldsList.get(fEnum).field instanceof TextField || fieldsList.get(fEnum).field instanceof TextArea) {
                ((TextInputControl) fieldsList.get(fEnum).field).setText("");
            } else{
                ((ComboBox) fieldsList.get(fEnum).field).getSelectionModel().select(0);
            }
        }
    }


//    protected GridPane getInformation(){
//        return super.getInformation();
//    }

    @Override
    protected HBox getHeading() {
        return TitleView.createTitle("Add New Book");
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();
        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddBookCancelled", null);
            }
        });

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processBook();
            }
        });
        return buttonBox;
    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Destroyed");
    }

    protected void confirmDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Confirmation");
        alert.setHeaderText("Book Successfully Added");
        alert.setContentText("Would you like to add a new book?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton){
            clearFields();
        } else if (result.get() == noButton) {
            myModel.stateChangeRequest("AddBookCancelled", null);
        }
    }

    protected void errorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Book Error");
        alert.setHeaderText("Book failed to be added");
        alert.setContentText("An error occurred while adding book to database. " + msg );

        ButtonType okButton = new ButtonType("Ok");

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == okButton){
//            clearFields();
//        }
    }
}
