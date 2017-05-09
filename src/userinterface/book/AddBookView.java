package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
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
import java.util.logging.Logger;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddBookView extends BookInformationView {
    private static final Logger LOGGER = Logger.getLogger( AddBookView.class.getName() );
    public AddBookView(IModel model) {
        super(model, true, "AddBookView");
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
        return TitleView.createTitle(Utilities.getStringLang("add_book"));
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();
        JFXButton submit = new JFXButton(Utilities.getStringLang("sub_btn"));
        JFXButton cancel = new JFXButton(Utilities.getStringLang("cancel_btn"));
        submit.getStyleClass().add("button-raised");
        submit.setId("accept");
        cancel.getStyleClass().add("button-raised");
        cancel.setId("cancel");
        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                myModel.stateChangeRequest("Error", "Everything is wrong");
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
        super.updateState(key, value);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LOGGER.info("Destroyed");
    }

    protected void confirmDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Utilities.getStringLang("book_com"));
        alert.setHeaderText(Utilities.getStringLang("book_added"));
        alert.setContentText(Utilities.getStringLang("add_book?"));

        ButtonType yesButton = new ButtonType(Utilities.getStringLang("yes"));
        ButtonType noButton = new ButtonType(Utilities.getStringLang("no"));

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
        alert.setTitle(Utilities.getStringLang("book_err"));
        alert.setHeaderText(Utilities.getStringLang("book_add_fail"));
        alert.setContentText(Utilities.getStringLang("book_err_occ") + " " + msg );

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == okButton){
//            clearFields();
//        }
    }
}
