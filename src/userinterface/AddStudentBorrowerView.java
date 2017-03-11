package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddStudentBorrowerView extends View {
    public AddStudentBorrowerView(IModel model) {
        super(model, "AddStudentBorrowerView");
        VBox box = new VBox();
        box.setStyle("-fx-background-color: #ff102b");

        box.getChildren().add(TitleView.createTitle("Add New Student Borrower"));

        Button cancel = new Button("Cancel");
        box.getChildren().add(cancel);
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddStudentBorrowerCancelled", null);
            }
        });

        getChildren().add(box);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
