package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import impresario.ModelRegistry;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    private static final int MAX_COLUMN = 2;

    public CheckOutBookView(IModel model) {
        super(model, "CheckOutBookView");
        File file = new File("resources/css/common.css");
        this.getStylesheets().add(file.toURI().toString());
        VBox box = new VBox();
        box.setSpacing(40);
//        box.setStyle("-fx-background-color: #00533e");

        box.setAlignment(Pos.CENTER);
        String labelString = Utilities.getStringLang("search");
        Label label = new Label(labelString.toUpperCase());
        label.setFont(Font.font(40));
        label.setId("CheckOut");
        barcode = new JFXTextField();
        GridPane grid = new GridPane();

        int line = 0;
        int row = line / MAX_COLUMN;
        int col = line % MAX_COLUMN;
//        fieldsStr.put(Book.DATABASE.Barcode, getStringLang("barcode"));
//        setupField();
//        String barcodeStr = fields.get(Book.DATABASE.Barcode);
//        setupField(barcode);
        grid.add(barcode, col, row,1,1);
        line++;
//        file = new File("resources/images/SUNY_Brockport_Logo.png");
//        final ImageView imv = new ImageView();
//        imv.setPreserveRatio(true);
//        imv.setFitHeight(300);
//        final Image image2 = new Image(file.toURI().toString());
//        imv.setImage(image2);
        box.getChildren().addAll(label, grid, getButtonBox());
        getChildren().add(box);
   //     getButtonBox();
    }

    protected HBox getButtonBox() {

        HBox buttonBox = new HBox();
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
                System.out.println("Ok");
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
