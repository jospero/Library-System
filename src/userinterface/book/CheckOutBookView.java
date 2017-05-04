package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import impresario.ModelRegistry;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import model.Rental;
import userinterface.View;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class CheckOutBookView extends View {
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
    private JFXTextField barcode;
    private JFXTextField studentBorrowerId;
    private JFXButton backButton;
    private JFXButton submit;
    private JFXButton cancel;
    private boolean barcodePage = true;
private StackPane fieldBox;
    public CheckOutBookView(IModel model) {
        super(model, "CheckOutBookView");

        VBox box = new VBox();
        box.setSpacing(40);
        box.setPadding(new Insets(10,40,10,40));
//        box.setStyle("-fx-background-color: #00533e");

        box.setAlignment(Pos.CENTER);
        String labelString = Utilities.getStringLang("check_out_book");
        Label label = new Label(labelString.toUpperCase());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setId("CheckOut");

        fieldBox = new StackPane();

        barcode = new JFXTextField();
        Utilities.addTextLimiter(barcode, 5);
        barcode.setPromptText("Barcode");
        barcode.setLabelFloat(true);

        studentBorrowerId = new JFXTextField();
        studentBorrowerId.setPromptText("Student Borrower BannerId");
        studentBorrowerId.setLabelFloat(true);


        fieldBox.getChildren().addAll(barcode, studentBorrowerId);

        fieldBox.setPrefHeight(fieldBox.getHeight()+80);
        Rectangle clipRect = new Rectangle(fieldBox.getWidth(), fieldBox.getHeight());

// bind properties so height and width of rect
// changes according pane's width and height
        clipRect.heightProperty().bind(fieldBox.heightProperty());
        clipRect.widthProperty().bind(fieldBox.widthProperty());

// set rect as clip rect
        fieldBox.setClip(clipRect);

        HBox buttons = getButtonBox();

        box.getChildren().addAll(label, fieldBox, buttons);
        getChildren().add(box);


        studentBorrowerId.translateXProperty().set(1000);
        myModel.subscribe("UpdateStatusMessage", this);
        myModel.subscribe("BarcodeSuccessFlag", this);
   //     getButtonBox();
    }

    protected HBox getButtonBox() {

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
         submit = new JFXButton(Utilities.getStringLang("sub_btn"));
         cancel = new JFXButton(Utilities.getStringLang("cancel_btn"));
         //TODO: BACK TEXT
         backButton = new JFXButton("Back");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                slideBack();
            }
        });
        submit.getStyleClass().add("button-raised");
        submit.setId("accept");

        backButton.getStyleClass().add("button-raised");
        backButton.setVisible(false);

        cancel.getStyleClass().add("button-raised");
        cancel.setId("cancel");
        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);
        buttonBox.getChildren().add(backButton);
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Properties temp = new Properties();
                temp.setProperty(Rental.DATABASE.Barcode.name(), barcode.getText().trim());
                temp.setProperty(Rental.DATABASE.BorrowerId.name(), studentBorrowerId.getText().trim());
                if(barcodePage)
                    myModel.stateChangeRequest("CheckBarcode", temp);
                else
                    myModel.stateChangeRequest("ProcessCheckOut", temp);


//                slideOut();
            }
        });

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("SCR ");
                myModel.stateChangeRequest("ViewCancelled", null);
            }
        });
        return buttonBox;
    }

    @Override
    public void updateState(String key, Object value) {
        System.out.println(key + "    " +"the");
        if(key.equals("UpdateStatusMessage")){
            boolean success = (boolean) myModel.getState("SuccessFlag");
            if(success){
                confirmDialog();
            } else {
                errorDialog((String) value);
            }
        } else if(key.equals("BarcodeSuccessFlag")){
            boolean success = (boolean) value;
            if(success){
                slideOut();
            } else {
                myModel.stateChangeRequest("SnackBarErrorMessage", "Barcode is Invalid");
            }
        }
    }

    private void slideOut(){
        studentBorrowerId.requestFocus();
        submit.setDisable(true);
        cancel.setDisable(true);
        backButton.setDisable(true);
        Timeline acceptedBarcode = new Timeline();
        acceptedBarcode.setCycleCount(1);
        acceptedBarcode.setAutoReverse(true);
        final KeyValue kvUp1 = new KeyValue(barcode.translateXProperty(), -fieldBox.getWidth());
        final KeyValue kvUp2 = new KeyValue(studentBorrowerId.translateXProperty(), 0);

        EventHandler onFinishedBarcode = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                backButton.setVisible(true);
                submit.setDisable(false);
                cancel.setDisable(false);
                backButton.setDisable(false);
                barcodePage = false;
            }
        };

        final KeyFrame kfUp = new KeyFrame(Duration.millis(300), onFinishedBarcode, kvUp1, kvUp2);//, kvUp2);//, kvUp3);
        acceptedBarcode.getKeyFrames().add(kfUp);
        EventHandler onFinishedBack= new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                backButton.setVisible(false);
            }
        };
        acceptedBarcode.play();

    }

    private void slideBack(){
        submit.setDisable(true);
        cancel.setDisable(true);
        backButton.setDisable(true);
        Timeline acceptedBarcode = new Timeline();
        acceptedBarcode.setCycleCount(1);
        acceptedBarcode.setAutoReverse(true);
        final KeyValue kvUp1 = new KeyValue(barcode.translateXProperty(), 0);
        final KeyValue kvUp2 = new KeyValue(studentBorrowerId.translateXProperty(), fieldBox.getWidth());


        EventHandler onFinishedBack= new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                backButton.setVisible(false);
                submit.setDisable(false);
                cancel.setDisable(false);
                backButton.setDisable(false);
                barcodePage = true;
            }
        };
        final KeyFrame kfUp = new KeyFrame(Duration.millis(300), onFinishedBack, kvUp1, kvUp2);//, kvUp2);//, kvUp3);
        acceptedBarcode.getKeyFrames().add(kfUp);
        acceptedBarcode.play();

    }


    protected void confirmDialog(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(Utilities.getStringLang("check_out_book"));
        alert.setHeaderText(Utilities.getStringLang("check_out_book"));
        alert.setContentText(Utilities.getStringLang("checkout_confirm"));

        ButtonType yesButton = new ButtonType(Utilities.getStringLang("ok_btn"));
//        ButtonType noButton = new ButtonType(Utilities.getStringLang("no"));

        alert.getButtonTypes().setAll(yesButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == yesButton){

        }
//        else if (result.get() == noButton) {
//            myModel.stateChangeRequest("ViewCancelled", null);
//        }
    }

    protected void errorDialog(String msg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(Utilities.getStringLang("check_out_book"));
        alert.setHeaderText("Error Occurred");
        alert.setContentText("There was an error: " + msg );

        ButtonType okButton = new ButtonType(Utilities.getStringLang("ok_btn"));

        alert.getButtonTypes().setAll(okButton);

        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == okButton){
//            clearFields();
//        }
    }

//    protected void setDependencies()
//    {
//        dependencies = new Properties();
//        dependencies.setProperty("ProcessSearch", "UpdateSearch");
//        dependencies.setProperty("ViewBookCancelled", "ParentView");
//        dependencies.setProperty("SearchCancelled", "ViewCancelled");
//        dependencies.setProperty("View", "SubViewChange");
//        myRegistry.setDependencies(dependencies);
//    }

//    public void stateChangeRequest(String key, Object value) {
 //       System.out.println("SCR "+ key);
 //        if(key.equals("ViewBookCancelled")){
 //            System.out.println("SCR "+ key);
 //            nextView.remove(nextView.size()-1);
 //       }

 //   }

}
