package userinterface.worker;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Worker;
import userinterface.TitleView;
import Utilities.Utilities;

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
        for(Worker.DATABASE fEnum : Worker.DATABASE.values()){
            clearFields(fieldsList.get(fEnum).field);
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
        return TitleView.createTitle(Utilities.getStringLang("add_worker"));
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(30, 0,30, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        Button submit = new Button(Utilities.getStringLang("sub_btn"));
        Button cancel = new Button(Utilities.getStringLang("cancel_btn"));

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
        alert.setTitle(Utilities.getStringLang("worker_com"));
        alert.setHeaderText(Utilities.getStringLang("worker_added"));
        alert.setContentText(Utilities.getStringLang("add_worker?"));

        ButtonType yesButton = new ButtonType(Utilities.getStringLang("yes"));
        ButtonType noButton = new ButtonType(Utilities.getStringLang("no"));

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
        alert.setTitle(Utilities.getStringLang("worker_err"));
        alert.setHeaderText(Utilities.getStringLang("worker_add_fail"));
        alert.setContentText(Utilities.getStringLang("worker_err_occ") + " "  + msg );

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == okButton){
//            clearFields();
//        }
    }
}
