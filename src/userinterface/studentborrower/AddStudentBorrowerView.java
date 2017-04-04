package userinterface.studentborrower;

import Utilities.Utilities;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.StudentBorrower;
import model.Worker;
import userinterface.TitleView;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddStudentBorrowerView extends StudentBorrowerInformationView {

    public AddStudentBorrowerView(IModel model) {
        super(model, true, "AddStudentBorrowerView");
    }


    @Override
    protected HBox getHeading() {
        return TitleView.createTitle(Utilities.getStringLang("add_sb"));
    }

    @Override
    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();

        Button submit = new Button(Utilities.getStringLang("sub_btn"));
        Button cancel = new Button(Utilities.getStringLang("cancel_btn"));

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
                processStudentBorrower();
            }
        });

        return buttonBox;
    }

    private void processStudentBorrower() {
        Properties studentBorrower = validateStudentBorrower();
        if(studentBorrower.size() > 0 ){
            myModel.stateChangeRequest("ProcessNewStudentBorrower", studentBorrower);
        }
    }

    @Override
    protected void confirmDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Utilities.getStringLang("sb_com"));
        alert.setHeaderText(Utilities.getStringLang("sb_added"));
        alert.setContentText(Utilities.getStringLang("add_sb?"));
        ButtonType yesButton = new ButtonType(Utilities.getStringLang("yes"));
        ButtonType noButton = new ButtonType(Utilities.getStringLang("no"));

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton){
            clearFields();
        } else if (result.get() == noButton) {
            myModel.stateChangeRequest("AddStudentBorrowerCancelled", null);
        }
    }

    void clearFields(){
        for(StudentBorrower.DATABASE fEnum : StudentBorrower.DATABASE.values()){
            clearFields(fieldsList.get(fEnum).field);
        }
    }

    @Override
    protected void errorDialog(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Utilities.getStringLang("sb_err"));
        alert.setHeaderText(Utilities.getStringLang("sb_add_fail"));
        alert.setContentText(Utilities.getStringLang("sb_err_occ") + " "  + msg );

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
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
}
