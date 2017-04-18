package userinterface;

import Utilities.Utilities;
import impresario.IModel;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by KingMiguel on 4/18/17.
 */
public class CheckInBookView extends View {

    public CheckInBookView(IModel model) {
        super(model, "CheckInBookView");
        VBox root = new VBox();

        GridPane grid = new GridPane();
        grid.setHgap(30);
        grid.setVgap(30);

        HBox buttonBox = getButtonBox();
        buttonBox.setPadding(new Insets(30, 0,30, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        root.getChildren().addAll(grid, buttonBox);
        getChildren().add(root);
    }


    protected HBox getHeading() {
        return TitleView.createTitle(Utilities.getStringLang("mod_book"));
    }


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
                CheckInBook();
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

    private void CheckInBook() {

    }


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
    }

}
