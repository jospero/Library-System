package userinterface.worker;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import userinterface.TitleView;
import userinterface.worker.WorkerInformationView;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class ModifyWorkerView extends WorkerInformationView {

    public ModifyWorkerView(IModel model) {
        super(model, true, "ModifyWorkerView");
    }

    @Override
    protected HBox getHeading() {
        return TitleView.createTitle("Modify Worker");
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();

        Button submit = new Button("Submit");
        Button cancel = new Button("Back to Search Results");
//        Button cancel = new Button("Back to Search");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                modifyWorker();
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewWorkerCancelled", null);
            }
        });
        return buttonBox;
    }

    private void modifyWorker() {
        Properties book = validateWorker();
        if(book.size() > 0 ){
            myModel.stateChangeRequest("ProcessModifyWorker", book);
        }
    }

    @Override
    protected void confirmDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Worker Confirmation");
        alert.setHeaderText("Worker Successfully Added");
        alert.setContentText("Would you like to add a new worker?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == noButton) {
            myModel.stateChangeRequest("ViewWorkerCancelled", null);
        }
    }

    @Override
    protected void errorDialog(String value) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Worker Error");
        alert.setHeaderText("Worker failed to be added");
        alert.setContentText("An error occurred while adding worker to database. " + value );

        ButtonType okButton = new ButtonType("Ok");

        alert.getButtonTypes().setAll(okButton);
    }

    @Override
    public void updateState(String key, Object value) {
        super.updateState(key, value);
        System.out.println("updateModify");
    }



}
