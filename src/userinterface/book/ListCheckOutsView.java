package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import userinterface.View;

/**
 * Created by jackdist on 5/5/17.
 */
public class ListCheckOutsView extends View {

    public ListCheckOutsView(IModel model) {
        super(model, "ListCheckOutsView");

        VBox box = new VBox();
        box.setSpacing(40);
        box.setPadding(new Insets(10,40,10,40));
//        box.setStyle("-fx-background-color: #00533e");

        box.setAlignment(Pos.CENTER);
        String labelString = Utilities.getStringLang("checked_out_books");
        Label label = new Label(labelString.toUpperCase());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setId("CheckOutList");

        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("BorrowerSuccessFlag", this);
        //     getButtonBox();
    }

    public void updateState(String key, Object value) {}
}
