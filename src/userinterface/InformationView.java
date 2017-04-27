package userinterface;

import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import userinterface.book.BookInformationView;

import java.time.LocalDate;
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
    protected boolean modify = false;

    public InformationView(IModel model, boolean enableFields, String classname) {
        super(model, classname);
        this.enableFields = enableFields;
        if(classname.toLowerCase().contains("modify")){
            modify = true;
        }
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

        myModel.subscribe("UpdateStatusMessage", this);
    }

    protected GridPane getInformation(){
        GridPane info = new GridPane();
        info.setHgap(20);
        info.setVgap(30);
        info.setPadding(new Insets(0, 10, 0, 10));
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(100 - col1.getPercentWidth());
        info.getColumnConstraints().addAll(col1, col2);
        return info;
    }

    protected abstract HBox getHeading();
    protected abstract HBox getButtonBox();
    protected abstract void setupFields();
//    protected abstract GridPane getInformation();
    protected abstract void confirmDialog();
    protected abstract void errorDialog(String value);

    public void clearFields(Node n){
        if(n instanceof TextField || n instanceof TextArea) {
            ((TextInputControl) n).setText("");
        } else if (n instanceof ComboBox){
            ((ComboBox) n).getSelectionModel().select(0);
        } else if (n instanceof  HBox){
            JFXTextField node1 = (JFXTextField) ((HBox) n).getChildren().get(0);
            JFXTextField node2 = (JFXTextField) ((HBox) n).getChildren().get(1);
            node1.setText("");
            node2.setText("");
        } else {
            ((DatePicker) n).setValue(LocalDate.now());
        }
    }
    @Override
    public void updateState(String key, Object value) {
        System.out.println(key);
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
