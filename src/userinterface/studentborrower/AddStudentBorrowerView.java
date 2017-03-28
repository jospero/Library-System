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
        return TitleView.createTitle("Add New Student Borrower");
    }

    @Override
    protected HBox getButtonBox() {
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
        System.out.println("Confirm");
    }

    @Override
    protected void errorDialog(String value) {
        System.out.println("error");
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
