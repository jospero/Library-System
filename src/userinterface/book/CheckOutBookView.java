package userinterface.book;

import Utilities.Utilities;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import userinterface.TitleView;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class CheckOutBookView extends BookInformationView {

    public CheckOutBookView(IModel model) {
        super(model, true, "CheckOutBookView");


    }

    @Override
    protected HBox getHeading() {
        return TitleView.createTitle(Utilities.getStringLang("mod_book"));
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();

        Button submit = new Button(Utilities.getStringLang("sub_btn"));
        Button cancel = new Button(Utilities.getStringLang("back_search_result"));
//      Button cancel = new Button("Back to Search");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CheckOutBook();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewBookCancelled", null);
            }
        });
        return buttonBox;
    }

    private void CheckOutBook() {
        Properties book = validateBook();
        if(book.size() > 0 ){
            myModel.stateChangeRequest("ProcessCheckOutBook", book);
        }
    }

    @Override
    protected void confirmDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Utilities.getStringLang("book_com"));
        alert.setHeaderText(Utilities.getStringLang("book_added"));
        alert.setContentText(Utilities.getStringLang("cont_mod_worker"));

        ButtonType yesButton = new ButtonType(Utilities.getStringLang("yes"));
        ButtonType noButton = new ButtonType(Utilities.getStringLang("no"));

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == noButton) {
            myModel.stateChangeRequest("ViewBookCancelled", null);
        }
    }

    @Override
    protected void errorDialog(String value) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Utilities.getStringLang("book_err"));
        alert.setHeaderText(Utilities.getStringLang("book_mod_fail"));
        alert.setContentText(Utilities.getStringLang("book_err_occ_mod") + " " + value );

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);
    }

    @Override
    public void updateState(String key, Object value) {
        super.updateState(key, value);
    }



}
