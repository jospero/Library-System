package userinterface.book;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import userinterface.TitleView;

/**
 * Created by Sammytech on 3/5/17.
 */
public class ModifyBookView extends BookInformationView {

    public ModifyBookView(IModel model) {
        super(model, false, "ModifyBookView");

        VBox box = new VBox();
        box.setStyle("-fx-background-color: #93ffa8");

        box.getChildren().add(TitleView.createTitle("Modify Book"));

        box.getChildren().add(getBookInformation());

        HBox buttonBox = new HBox();

        Button submit = new Button("Submit");
        Button cancel = new Button("Back to Search Results");
//        Button cancel = new Button("Back to Search");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Book has been modified");
                alert.setHeaderText(null);
                alert.setContentText("I have a great message for you!");

                alert.showAndWait();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewBookCancelled", null);
            }
        });

        box.getChildren().add(buttonBox);

        getChildren().add(box);
    }

    @Override
    public void updateState(String key, Object value) {

    }



}
