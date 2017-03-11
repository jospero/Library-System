package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class BookInformationView extends View {

    enum FieldsEnum{
        BARCODE, TITLE, AUTHOR, DISCIPLINE, PUBLISHER, YEAROFPUB, ISBN, SUGPRICE, NOTES, STATUS
    }

    private class Fields{
        Label label = new Label();
        TextField textField = new TextField();
    }
    HashMap<FieldsEnum, Fields> fieldsList = new HashMap<>();
    HashMap<FieldsEnum, String> fieldsStr = new HashMap<>();
    boolean enableFields;
    public BookInformationView(IModel model, boolean enableFields, String classname) {
        super(model, classname);
        this.enableFields = enableFields;

        fieldsStr.put(FieldsEnum.BARCODE, "BARCODE");
        fieldsStr.put(FieldsEnum.TITLE, "TITLE");
        fieldsStr.put(FieldsEnum.AUTHOR, "AUTHOR");
        fieldsStr.put(FieldsEnum.DISCIPLINE, "DISCIPLINE");
        fieldsStr.put(FieldsEnum.PUBLISHER, "PUBLISHER");
        fieldsStr.put(FieldsEnum.YEAROFPUB, "YEAR OF PUBLICATION");
        fieldsStr.put(FieldsEnum.ISBN, "ISBN");
        fieldsStr.put(FieldsEnum.SUGPRICE, "SUGGESTED PRICE");
        fieldsStr.put(FieldsEnum.NOTES, "NOTES");
        fieldsStr.put(FieldsEnum.STATUS, "STATUS");

    }

    public GridPane getBookInformation(){
        GridPane bookInfo = new GridPane();
        bookInfo.setHgap(10);
        bookInfo.setVgap(10);
        bookInfo.setPadding(new Insets(0, 10, 0, 10));

        int row = 0;
        for(FieldsEnum fEnum : FieldsEnum.values()){
            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                field.textField.setPromptText(str);
                field.textField.setEditable(enableFields);
                fieldsList.put(fEnum, field);
                bookInfo.add(field.label, 0, row);
                bookInfo.add(field.textField, 1, row);
//                bookInfo.add(field.label, 0, row);
                row++;
            }
        }


        return bookInfo;


    }

}
