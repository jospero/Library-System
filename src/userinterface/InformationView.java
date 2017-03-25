package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.HashMap;

/**
 * Created by Sammytech on 3/24/17.
 */
public abstract class InformationView<T> extends View {

    public class Fields{
        public Label label = new Label();
        public Node field;
    }

    protected HashMap<T, Fields> fieldsList = new HashMap<>();
    protected HashMap<T, String> fieldsStr = new HashMap<>();
    protected boolean enableFields;

    public InformationView(IModel model, boolean enableFields, String classname) {
        super(model, classname);
        this.enableFields = enableFields;
        setupFields();
        VBox box = new VBox();
        box.setPadding(new Insets(10,40,10,40));
        box.getChildren().add(getHeading());

        box.getChildren().add(getInformation());

        HBox buttonBox = getButtonBox();
        buttonBox.setPadding(new Insets(30, 0,30, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        box.getChildren().add(buttonBox);

        getChildren().add(box);
    }

    protected GridPane getInformation(){
        GridPane info = new GridPane();
        info.setHgap(10);
        info.setVgap(10);
        info.setPadding(new Insets(0, 10, 0, 10));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(35);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(100-col1.getPercentWidth());
        info.getColumnConstraints().addAll(col1, col2);
        return info;
    }

    protected abstract HBox getHeading();
    protected abstract HBox getButtonBox();
    protected abstract void setupFields();
//    protected abstract GridPane getInformation();
    protected abstract void confirmDialog();
    protected abstract void errorDialog(String value);

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("UpdateStatusMessage")){
            boolean success = (boolean) myModel.getState("SuccessFlag");
            if(success){
                confirmDialog();
            } else {
                errorDialog((String) value);
            }
        }
    }
}
