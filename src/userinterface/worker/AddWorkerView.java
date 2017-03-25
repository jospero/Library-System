package userinterface.worker;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import userinterface.TitleView;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddWorkerView extends WorkerInformationView {
    public AddWorkerView(IModel model) {
        super(model, true,"AddWorkerView");

        myModel.subscribe("UpdateStatusMessage", this);
    }

    private void processWorker() {
        Properties worker = validateWorker();
        if(worker.size() > 0 ){
            myModel.stateChangeRequest("ProcessNewWorker", worker);
        }
    }

    void clearFields(){
        for(FieldsEnum fEnum : FieldsEnum.values()){
            if(fieldsList.get(fEnum).field instanceof TextField || fieldsList.get(fEnum).field instanceof TextArea) {
                ((TextInputControl) fieldsList.get(fEnum).field).setText("");
            } else if (fieldsList.get(fEnum).field instanceof ComboBox){
                ((ComboBox) fieldsList.get(fEnum).field).getSelectionModel().select(0);
            } else {
                ((DatePicker) fieldsList.get(fEnum).field).setValue(LocalDate.now());
            }
        }
    }


    @Override
    public void updateState(String key, Object value) {
        super.updateState(key, value);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Destroyed");
    }

    @Override
    protected HBox getHeading() {
        return TitleView.createTitle("Add New Worker");
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(30, 0,30, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processWorker();
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddWorkerCancelled", null);
            }
        });
        return buttonBox;
    }

    protected void confirmDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Worker Confirmation");
        alert.setHeaderText("Worker Successfully Added");
        alert.setContentText("Would you like to add a new book?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton){
            clearFields();
        } else if (result.get() == noButton) {
            myModel.stateChangeRequest("AddWorkerCancelled", null);
        }
    }

    protected void errorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Worker Error");
        alert.setHeaderText("Worker failed to be added");
        alert.setContentText("An error occurred while adding book to database. " + msg );

        ButtonType okButton = new ButtonType("Ok");

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == okButton){
//            clearFields();
//        }
    }
}
