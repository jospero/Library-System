package userinterface;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Book;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class BookInformationView extends View {

    enum FieldsEnum{
        BARCODE, TITLE, AUTHOR, DISCIPLINE, PUBLISHER, YEAROFPUB, ISBN, CONDITION, SUGPRICE, NOTES, STATUS
    }

    private class Fields{
        Label label = new Label();
        Node field;
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
        fieldsStr.put(FieldsEnum.CONDITION, "CONDITION");
        fieldsStr.put(FieldsEnum.SUGPRICE, "SUGGESTED PRICE");
        fieldsStr.put(FieldsEnum.NOTES, "NOTES");
        fieldsStr.put(FieldsEnum.STATUS, "STATUS");

//        getFieldsString();
    }

    public final GridPane getBookInformation(){
        GridPane bookInfo = new GridPane();
        bookInfo.setHgap(10);
        bookInfo.setVgap(10);
        bookInfo.setPadding(new Insets(0, 10, 0, 10));

        int row = 0;
        Vector<String> book = ((Book) myModel).getEntryListView();
        for(FieldsEnum fEnum : FieldsEnum.values()){
            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == FieldsEnum.CONDITION){
                    field.field = getConditionNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == FieldsEnum.STATUS){
                    field.field = getStatusNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == FieldsEnum.NOTES) {
                    TextArea ta = new TextArea();
                    field.field = ta;
                } else {
                    TextField fTF = new TextField();
                    fTF.setPromptText(str);
                    fTF.setEditable(enableFields);
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        fTF.setText(book.get(row));
                    field.field = fTF;
                }
                fieldsList.put(fEnum, field);
                bookInfo.add(field.label, 0, row);
                bookInfo.add(field.field, 1, row);
//                bookInfo.add(field.label, 0, row);
                row++;
            }
        }


        return bookInfo;


    }

    private ComboBox getConditionNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Good", "Damaged");
        comboBox.setValue("Good");
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Active", "Inactive");
        comboBox.setValue("Active");
        return comboBox;
    }

}
