package userinterface;

import impresario.IModel;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddWorkerView extends View {
    public AddWorkerView(IModel model) {
        super(model, "AddWorkerView");

        VBox box = new VBox();
        box.setStyle("-fx-background-color: #ffad16");

        box.getChildren().add(TitleView.createTitle("Add New Worker"));

        getChildren().add(box);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
