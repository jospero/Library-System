package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Book;
import model.Rental;
import userinterface.TitleView;
import userinterface.View;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by KingMiguel on 4/18/17.
 */
public class CheckInBookView extends View {

    private JFXTextField barcode;
    public CheckInBookView(IModel model) {
        super(model, "CheckInBookView");

        VBox box = new VBox();
        box.setPadding(new Insets(10,40,10,40));
        box.setSpacing(40);

        box.setAlignment(Pos.CENTER);
        barcode = new JFXTextField();
        Utilities.addTextLimiter(barcode, 5);
        barcode.setPromptText(Utilities.getStringLang("barcode"));
        barcode.setLabelFloat(true);
//        file = new File("resources/images/SUNY_Brockport_Logo.png");
//        final ImageView imv = new ImageView();
//        imv.setPreserveRatio(true);
//        imv.setFitHeight(300);
//        final Image image2 = new Image(file.toURI().toString());
//        imv.setImage(image2);
        HBox res = getButtonBox();
        HBox title = getHeading();
        box.getChildren().addAll(title, barcode, res);
        getChildren().add(box);
        myModel.subscribe("UpdateStatusMessage", this);
        //     getButtonBox();
    }


    protected HBox getHeading() {
        return TitleView.createTitle(Utilities.getStringLang("check_in_book"));
    }


    protected HBox getButtonBox() {
        HBox buttonBox = new HBox();

        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);
        JFXButton submit = new JFXButton(Utilities.getStringLang("sub_btn"));
        submit.getStyleClass().add("button-raised");
        JFXButton cancel = new JFXButton(Utilities.getStringLang("cancel_btn"));
        cancel.getStyleClass().add("button-raised");
        cancel.setId("cancel");

//      Button cancel = new Button("Back to Search");

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String temp = barcode.getText();
                //temp.setProperty(Rental.DATABASE.Barcode.name(), barcode.getText().trim());
                myModel.stateChangeRequest("ProcessCheckIn", temp);
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("ViewCancelled", null);
            }
        });
        return buttonBox;
    }


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

    protected void confirmDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Utilities.getStringLang("check_in_book"));
        alert.setHeaderText(Utilities.getStringLang("check_in_book"));
        alert.setContentText(Utilities.getStringLang("checkin_confirm"));

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == okButton){

        } //else if (result.get() == noButton) {
           // myModel.stateChangeRequest("ViewCancelled", null);
       // }
    }

    protected void errorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Utilities.getStringLang("check_in_book"));
        alert.setHeaderText(Utilities.getStringLang("err_occ"));
        alert.setContentText(msg);

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == okButton){
//            clearFields();
//        }
    }

}
