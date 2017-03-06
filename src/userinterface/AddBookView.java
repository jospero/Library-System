package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddBookView extends View {
    public AddBookView(IModel model) {
        super(model, "AddBookView");

        VBox box = new VBox();
        box.setStyle("-fx-background-color: #93ffa8");

        box.getChildren().add(TitleView.createTitle("Add New Book"));

        Button cancel = new Button("Cancel");
        box.getChildren().add(cancel);
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddBookCancelled", null);
            }
        });
        getChildren().add(box);

    }



    @Override
    public void updateState(String key, Object value) {

    }
}
