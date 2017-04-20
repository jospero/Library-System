package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import impresario.ModelRegistry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Rental;
import model.StudentBorrower;
import userinterface.View;

import java.io.File;
import java.util.ArrayList;
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

    public CheckOutBookView(IModel model) {
        super(model, "CheckOutBookView");
        File file = new File("resources/css/common.css");
        this.getStylesheets().add(file.toURI().toString());
        VBox box = new VBox();
        box.setSpacing(40);
        box.setPadding(new Insets(10,40,10,40));
//        box.setStyle("-fx-background-color: #00533e");

        box.setAlignment(Pos.CENTER);
        String labelString = Utilities.getStringLang("check_out_book");
        Label label = new Label(labelString.toUpperCase());
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label.setId("CheckOut");

        VBox fieldBox = new VBox();
        fieldBox.setSpacing(20);

        barcode = new JFXTextField();
        barcode.setPromptText("Barcode");
        barcode.setLabelFloat(true);
        studentBorrowerId = new JFXTextField();
        studentBorrowerId.setPromptText("Student Borrower BannerId");
        studentBorrowerId.setLabelFloat(true);

        fieldBox.getChildren().addAll(barcode, studentBorrowerId);

        HBox buttons = getButtonBox();

        box.getChildren().addAll(label, fieldBox, buttons);
        getChildren().add(box);
   //     getButtonBox();
    }

    protected HBox getButtonBox() {

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);
        JFXButton submit = new JFXButton(Utilities.getStringLang("sub_btn"));
        JFXButton cancel = new JFXButton(Utilities.getStringLang("cancel_btn"));
        submit.getStyleClass().add("button-raised");
        submit.setId("accept");
        cancel.getStyleClass().add("button-raised");
        cancel.setId("cancel");
        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Properties temp = new Properties();
                temp.setProperty(Rental.DATABASE.Barcode.name(), barcode.getText().trim());
                temp.setProperty(Rental.DATABASE.BorrowerId.name(), studentBorrowerId.getText().trim());
                myModel.stateChangeRequest("ProcessCheckOut", temp);
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
