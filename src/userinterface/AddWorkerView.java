package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddWorkerView extends WorkerInformationView {
    public AddWorkerView(IModel model) {
        super(model, true,"AddWorkerView");

        VBox box = new VBox();
        box.setStyle("-fx-background-color: #ffad16");

        box.getChildren().add(TitleView.createTitle("Add New Worker"));

        box.getChildren().add(getWorkerInformation());

        HBox buttonBox = new HBox();

        Button submit = new Button("Submit");
        Button cancel = new Button("Cancel");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddWorkerCancelled", null);
            }
        });

        box.getChildren().add(buttonBox);
        getChildren().add(box);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
