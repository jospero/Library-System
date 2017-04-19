package userinterface.book;

import Utilities.Utilities;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.text.Font;
import model.Book;
import userinterface.TitleView;

import java.io.File;
import java.util.Optional;
import userinterface.View;
import java.util.Properties;

/**
 * Created by KingMiguel on 4/18/17.
 */
public class CheckInBookView extends View {

    private JFXTextField barcode;
    public CheckInBookView(IModel model) {
        super(model, "CheckInBookView");
        File file = new File("resources/css/common.css");
        this.getStylesheets().add(file.toURI().toString());
        VBox box = new VBox();
        box.setSpacing(40);

        box.setAlignment(Pos.CENTER);
        barcode = new JFXTextField();
        Label label = new Label(Utilities.getStringLang("barcode"));
        label.setFont(Font.font(40));
        label.setId("CheckIn");
//        file = new File("resources/images/SUNY_Brockport_Logo.png");
//        final ImageView imv = new ImageView();
//        imv.setPreserveRatio(true);
//        imv.setFitHeight(300);
//        final Image image2 = new Image(file.toURI().toString());
//        imv.setImage(image2);
        HBox res = getButtonBox();
        HBox title = getHeading();
        box.getChildren().addAll(title,label, barcode, res);
        getChildren().add(box);
        //     getButtonBox();
    }


    protected HBox getHeading() {
        return TitleView.createTitle(Utilities.getStringLang("check_in_book"));
    }


    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();

        Button submit = new Button(Utilities.getStringLang("sub_btn"));
        Button cancel = new Button(Utilities.getStringLang("cancel_btn"));
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
                myModel.stateChangeRequest("ViewCancelled", null);
            }
        });
        return buttonBox;
    }

    private void CheckInBook() {
        Properties props = new Properties();
        props.setProperty(Book.DATABASE.Barcode.name(), barcode.getText().trim());
        myModel.stateChangeRequest("ProcessCheckIn", props);
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
