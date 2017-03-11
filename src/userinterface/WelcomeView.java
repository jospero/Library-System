package userinterface;

import impresario.IModel;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/6/17.
 */
public class WelcomeView extends View {

    public WelcomeView(IModel model) {
        super(model, "WelcomeView");

        VBox box = new VBox();
        box.setStyle("-fx-background-color: #93ffa8");
        Label label = new Label("Welcome " + myModel.getState("FirstName") + " " + myModel.getState("LastName"));
        box.getChildren().add(label);
        getChildren().add(box);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
