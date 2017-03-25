package userinterface.book;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import model.Book;
import userinterface.InformationView;
import userinterface.InformationView.*;
import userinterface.View;

import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class BookInformationView extends InformationView<BookInformationView.FieldsEnum> {

    enum FieldsEnum{
        Barcode, Title, Authors, Discipline, Publisher, YearOfPublication, ISBN, Condition, SuggestedPrice, Notes, Status
    }



    public BookInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);
    }

    @Override
    protected void setupFields() {
        fieldsStr.put(FieldsEnum.Barcode, "Barcode");
        fieldsStr.put(FieldsEnum.Title, "Title");
        fieldsStr.put(FieldsEnum.Authors, "Author(s)");
        fieldsStr.put(FieldsEnum.Discipline, "Discipline");
        fieldsStr.put(FieldsEnum.Publisher, "Publisher");
        fieldsStr.put(FieldsEnum.YearOfPublication, "Year of Publication");
        fieldsStr.put(FieldsEnum.ISBN, "ISBN");
        fieldsStr.put(FieldsEnum.Condition, "Condition");
        fieldsStr.put(FieldsEnum.SuggestedPrice, "Suggested Price");
        fieldsStr.put(FieldsEnum.Notes, "Notes");
        fieldsStr.put(FieldsEnum.Status, "Status");
    }

    @Override
    protected GridPane getInformation(){
        GridPane bookInfo =  super.getInformation();
        int row = 0;
        Vector<String> book = ((Book) myModel).getEntryListView();
        for(FieldsEnum fEnum : FieldsEnum.values()){

            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == FieldsEnum.Condition){
                    field.field = getConditionNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == FieldsEnum.Status){
                    field.field = getStatusNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == FieldsEnum.Notes) {
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
                row++;
            }
        }
        return bookInfo;
    }

    private void error(Node n){
        if(!n.getStyleClass().contains("error")){
            n.getStyleClass().add("error");
        }
    }

    final public Properties validateBook(){
        Properties book = new Properties();
        boolean errorFound = false;
        for(FieldsEnum fieldsEnum: fieldsList.keySet()){
             if(fieldsList.get(fieldsEnum).field instanceof TextField || fieldsList.get(fieldsEnum).field instanceof TextArea) {
                 String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                 if (str.isEmpty() && fieldsEnum != FieldsEnum.Notes) {
                     error(fieldsList.get(fieldsEnum).field);
                     if (!errorFound) {
                         errorFound = true;
                         book = new Properties();
                     }
                 } else if (fieldsEnum == FieldsEnum.Barcode || fieldsEnum == FieldsEnum.YearOfPublication ||
                         fieldsEnum == FieldsEnum.ISBN) {
                     try {
                         int i = Integer.parseInt(str);
                         if (!errorFound) {
                             fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                             book.setProperty(fieldsEnum.name(), str);
                         }
                     } catch (NumberFormatException ex) {
                         error(fieldsList.get(fieldsEnum).field);
                         if (!errorFound) {
                             errorFound = true;
                             book = new Properties();
                         }

                     }
                 } else if (fieldsEnum == FieldsEnum.SuggestedPrice){
                     try {
                         double i = Double.parseDouble(str);
                         if (!errorFound) {
                             fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                             book.setProperty(fieldsEnum.name(), str);
                         }
                     } catch (NumberFormatException ex) {
                         error(fieldsList.get(fieldsEnum).field);
                         if (!errorFound) {
                             errorFound = true;
                             book = new Properties();
                         }
                     }
                }
                 else {
                    if(!errorFound){
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        book.setProperty(fieldsEnum.name(), str);
                    }
                 }
             } else {
                 if(!errorFound) {
                     String str = ((ComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem().toString();
                     book.setProperty(fieldsEnum.name(), str);
                 }
             }
        }
        System.out.println(book.toString());
        return book;
    }


    private String Databasify(String field){
        return field.replaceAll("[^a-zA-Z0-9]", "");
    }
    private ComboBox getConditionNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Good", "Damaged");
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Active", "Inactive");
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

//    abstract void clearFields();





}
