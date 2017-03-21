package userinterface.studentborrower;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import userinterface.TitleView;

import java.util.Optional;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddStudentBorrowerView extends StudentBorrowerInformationView {

    public AddStudentBorrowerView(IModel model) {
        super(model, true, "AddStudentBorrowerView");

        VBox box = new VBox();
        box.setStyle("-fx-background-color: #530016");

        box.getChildren().add(TitleView.createTitle("Add New Student Borrower"));

        box.getChildren().add(getStudentBorrowerInformation());

        HBox buttonBox = new HBox();

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddStudentBorrowerCancelled", null);
            }
        });

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog with Custom Actions");
                alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
                alert.setContentText("Choose your option.");

                ButtonType buttonTypeOne = new ButtonType("One");
                ButtonType buttonTypeTwo = new ButtonType("Two");
                ButtonType buttonTypeThree = new ButtonType("Three");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne){
                    // ... user chose "One"
                } else if (result.get() == buttonTypeTwo) {
                    // ... user chose "Two"
                } else if (result.get() == buttonTypeThree) {
                    // ... user chose "Three"
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        });

        box.getChildren().add(buttonBox);

        getChildren().add(box);

    }


    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Destroyed");
    }
}
