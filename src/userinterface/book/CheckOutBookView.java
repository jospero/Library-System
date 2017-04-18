package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import userinterface.View;

import java.io.File;

/**
 * Created by Sammytech on 3/5/17.
 */
public class CheckOutBookView extends View {

    public CheckOutBookView(IModel model) {
        super(model, "CheckOutBookView");
        File file = new File("resources/css/common.css");
        this.getStylesheets().add(file.toURI().toString());
        VBox box = new VBox();
        box.setSpacing(40);
        box.setStyle("-fx-background-color: #00533e");

        box.setAlignment(Pos.CENTER);
        String labelString = Utilities.getStringLang("welcome_string") +" "+ myModel.getState("FirstName") + " " + myModel.getState("LastName");
        Label label = new Label(labelString.toUpperCase());
        label.setFont(Font.font(40));
        label.setId("CheckOut");
//        file = new File("resources/images/SUNY_Brockport_Logo.png");
//        final ImageView imv = new ImageView();
//        imv.setPreserveRatio(true);
//        imv.setFitHeight(300);
//        final Image image2 = new Image(file.toURI().toString());
//        imv.setImage(image2);
        box.getChildren().addAll(label, getButtonBox());
        getChildren().add(box);
   //     getButtonBox();
    }

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
                myModel.stateChangeRequest("ViewBookCancelled", null);
            }
        });
        return buttonBox;
    }

    @Override
    public void updateState(String key, Object value) {

    }

}
