package userinterface.book;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import userinterface.TitleView;

import java.util.Optional;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class AddBookView extends BookInformationView {

    public AddBookView(IModel model) {
        super(model, true, "AddBookView");

        VBox box = new VBox();
        box.setPadding(new Insets(10,40,10,40));
        box.getChildren().add(TitleView.createTitle(messages.getString("new_book_title")));

        box.getChildren().add(getBookInformation());

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(30, 0,30, 0));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(20);

        Button submit = new Button(messages.getString("sub_btn"));
        Button cancel = new Button(messages.getString("cancel_btn"));

        buttonBox.getChildren().add(submit);
        buttonBox.getChildren().add(cancel);

        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("AddBookCancelled", null);
            }
        });

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processBook();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog with Custom Actions");
                alert.setHeaderText("Look, a Confirmation Dialog with Custom Actions");
                alert.setContentText("Choose your option.");

                ButtonType buttonTypeOne = new ButtonType("One");
                ButtonType buttonTypeTwo = new ButtonType("Two");
                ButtonType buttonTypeThree = new ButtonType("Three");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne){
                    // ... user chose "One"
                } else if (result.get() == buttonTypeTwo) {
                    // ... user chose "Two"
                } else if (result.get() == buttonTypeThree) {
                    // ... user chose "Three"
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        });

        box.getChildren().add(buttonBox);

        getChildren().add(box);

    }

    private void processBook() {
        Properties book = validateBook();
        if(book.size() > 0 ){
            myModel.stateChangeRequest("ProcessNewBook", book);
        }
    }

    private void clearFields(){
        for(FieldsEnum fEnum : FieldsEnum.values()){


        }
    }




    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("Destroyed");
    }
}
