package userinterface.studentborrower;

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
import userinterface.View;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchStudentBorrowerView extends View {

    TextField firstName = new TextField();
    TextField lastName = new TextField();
    TextField phone = new TextField();
    TextField email = new TextField();

    public SearchStudentBorrowerView(IModel model) {
        super(model, "SearchStudentBorrowerView");

        VBox root = new VBox();
//        root.setFillWidth(true);
        root.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(0,60,0,60));
        grid.setHgap(30);
        grid.setVgap(10);
//        grid.setStyle("-fx-background-color: #580e7a");

//        HBox title = TitleView.createTitle("Enter StudentBorrower Information");
//        title.setStyle("-fx-background-color:#0c7a79");
//        GridPane.setHgrow(title, Priority.ALWAYS);
        Label title = new Label("Enter StudentBorrower Information");
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
        String firstnameStr = "First Name";
        Label firsnameLabel = new Label(firstnameStr);
        firstName.setPromptText(firstnameStr);
        grid.add(firsnameLabel, 0, row);
        grid.add(firstName, 1, row);

        row++;
        String lastnameStr = "Last Name";
        Label lastnameLabel = new Label(lastnameStr);
        lastName.setPromptText(lastnameStr);
        grid.add(lastnameLabel, 0, row);
        grid.add(lastName, 1, row);

        row++;
        String phoneStr = "Phone Number";
        Label phoneLabal = new Label(phoneStr);
        phone.setPromptText(phoneStr);
        grid.add(phoneLabal, 0, row);
        grid.add(phone, 1, row);

        row++;
        String emailStr = "Email Address";
        Label emailLabel = new Label(emailStr);
        email.setPromptText(emailStr);
        grid.add(emailLabel, 0, row);
        grid.add(email, 1, row);


        row++;
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        Button searchButton = new Button("Submit");
        Button cancelButton = new Button("Cancel");

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                MainStageContainer.getInstance().close();
//                MainStageContainer.setStage(new Stage(), "Here");
//                MainStageContainer.getInstance().show();
                myModel.stateChangeRequest("SearchStudentBorrowerCancelled", null);
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

    private void processSearch() {

        myModel.stateChangeRequest("ProcessSearch", null);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
