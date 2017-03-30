package userinterface.worker;

import Utilities.Utilities;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Worker;
import userinterface.View;

import javax.rmi.CORBA.Util;
import java.util.HashMap;
import java.util.Properties;

import static model.Worker.getFields;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchWorkerView extends View {

    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField phone = new TextField();
    TextField email = new TextField();

    public SearchWorkerView(IModel model) {
        super(model, "SearchWorkerView");

        VBox root = new VBox();
//        root.setFillWidth(true);
        root.setAlignment(Pos.CENTER);

        HashMap<Worker.DATABASE, String> fields = getFields();

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,60,0,60));
        grid.setHgap(30);
        grid.setVgap(10);
//        grid.setStyle("-fx-background-color: #580e7a");

//        HBox title = TitleView.createTitle("Enter Worker Information");
//        title.setStyle("-fx-background-color:#0c7a79");
//        GridPane.setHgrow(title, Priority.ALWAYS);
        Label title = new Label(Utilities.getStringLang("search_worker").toUpperCase());
        title.setPrefWidth(Double.MAX_VALUE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setPadding(new Insets(0,0,20,0));
        title.setAlignment(Pos.CENTER);
        grid.add(title,0,0,2,1);

//        String barcodeStr = "First Name";
//        Label barcodeLabel = new Label(barcodeStr);
//        barcode.setPromptText(barcodeStr);
//        grid.add(barcodeLabel, 0, 1);
//        grid.add(barcode, 1, 1);

//        VBox oneFieldHead = new VBox();
//        Label oneFieldOr = new Label("OR");
//        Label oneFieldText = new Label("(ENTER AT LEAST ONE FIELD)");
//
//        oneFieldOr.setFont(Font.font("Arial", FontWeight.BOLD, 20));
//        oneFieldText.setFont(Font.font("Arial", FontWeight.BOLD, 15));
//        oneFieldHead.setPadding(new Insets(20,0,20,0));
//        oneFieldHead.setAlignment(Pos.CENTER);
//
//        oneFieldHead.getChildren().add(oneFieldOr);
//        oneFieldHead.getChildren().add(oneFieldText);
//
//        grid.add(oneFieldHead,0,2,2,1);

        int row = 1;
        String firstnameStr = fields.get(Worker.DATABASE.FirstName);
        Label firsnameLabel = new Label(firstnameStr);
        firstName.setPromptText(firstnameStr);
        grid.add(firsnameLabel, 0, row);
        grid.add(firstName, 1, row);

        row++;
        String lastnameStr = fields.get(Worker.DATABASE.LastName);
        Label lastnameLabel = new Label(lastnameStr);
        lastName.setPromptText(lastnameStr);
        grid.add(lastnameLabel, 0, row);
        grid.add(lastName, 1, row);

        row++;
        String phoneStr = fields.get(Worker.DATABASE.Phone);
        Label phoneLabal = new Label(phoneStr);
        phone.setPromptText(phoneStr);
        grid.add(phoneLabal, 0, row);
        grid.add(phone, 1, row);

        row++;
        String emailStr = fields.get(Worker.DATABASE.Email);
        Label emailLabel = new Label(emailStr);
        email.setPromptText(emailStr);
        grid.add(emailLabel, 0, row);
        grid.add(email, 1, row);


        row++;
        HBox buttonPane = new HBox();
        buttonPane.setPadding(new Insets(22));
        buttonPane.setSpacing(30);
        buttonPane.setAlignment(Pos.CENTER);
        Button searchButton = new Button(Utilities.getStringLang("sub_btn"));
        Button cancelButton = new Button(Utilities.getStringLang("cancel_btn"));

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                MainStageContainer.getInstance().close();
//                MainStageContainer.setStage(new Stage(), "Here");
//                MainStageContainer.getInstance().show();
                myModel.stateChangeRequest("SearchWorkerCancelled", null);
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processSearch();

            }
        });
        buttonPane.getChildren().addAll(searchButton, cancelButton);

        grid.add(buttonPane, 0, row, 2, 1);


        root.getChildren().add(grid);
        getChildren().add(root);

    }

    private Properties validateSearch(){
        Properties search = new Properties();
        String firstNameStr = firstName.getText();
        String lastNameStr = lastName.getText();
        String phoneStr = phone.getText();
        String emailStr = email.getText();
       if(firstNameStr.trim().isEmpty() && lastNameStr.trim().isEmpty() && phoneStr.trim().isEmpty() && emailStr.trim().isEmpty()){
//Error
       } else{
           if(!firstNameStr.trim().isEmpty()){
               search.setProperty(Worker.DATABASE.FirstName.name(), firstNameStr);
           }
           if(!lastNameStr.trim().isEmpty()){
               search.setProperty(Worker.DATABASE.LastName.name(), lastNameStr);
           }
           if(!phoneStr.trim().isEmpty() && Utilities.validatePhoneNumber(phoneStr)){
               search.setProperty(Worker.DATABASE.Phone.name(), phoneStr);
           }
           if(!emailStr.trim().isEmpty()){
               search.setProperty(Worker.DATABASE.Email.name(), emailStr);
           }
       }
        return search;
    }

    private void processSearch() {
        Properties prop = validateSearch();
        if(prop.size() > 0) {
            myModel.stateChangeRequest("ProcessSearch", prop);
        }
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
