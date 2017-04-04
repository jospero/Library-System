package userinterface.book;

import Utilities.Utilities;
import impresario.IModel;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import model.Book;
import userinterface.InformationView;

import java.util.Properties;
import java.util.Vector;

import static model.Book.getFields;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class BookInformationView extends InformationView<Book.DATABASE> {


    public BookInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);
    }

    @Override
    protected void setupFields() {
        fieldsStr = getFields();
    }



    @Override
    protected GridPane getInformation(){
        GridPane bookInfo =  super.getInformation();
        int row = 0;
        Vector<String> book = ((Book) myModel).getEntryListView();
        for(Book.DATABASE fEnum : Book.DATABASE.values()){

            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == Book.DATABASE.Condition){
                    field.field = getConditionNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == Book.DATABASE.Status){
                    field.field = getStatusNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == Book.DATABASE.Notes) {
                    TextArea ta = new TextArea();
                    field.field = ta;
                } else {
                    TextField fTF = new TextField();
                    fTF.setPromptText(str);
                    fTF.setEditable(enableFields);
                    if(fEnum == Book.DATABASE.Barcode){
                        fTF.setEditable(!modify);
                    }
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
        System.out.println("Checking");
        for(Book.DATABASE fieldsEnum: fieldsList.keySet()){
             if(fieldsList.get(fieldsEnum).field instanceof TextField || fieldsList.get(fieldsEnum).field instanceof TextArea) {
                 String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                 if (str.isEmpty() && fieldsEnum != Book.DATABASE.Notes) {
                     error(fieldsList.get(fieldsEnum).field);
                     if (!errorFound) {
                         errorFound = true;
                         book = new Properties();
                     }
                 } else if (fieldsEnum == Book.DATABASE.Barcode || fieldsEnum == Book.DATABASE.YearOfPublication ||
                         fieldsEnum == Book.DATABASE.ISBN) {
                     if (str.matches("[0-9]+")) {
                         fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                         if (!errorFound) {
                             book.setProperty(fieldsEnum.name(), str);
                         }
                     } else {
                         error(fieldsList.get(fieldsEnum).field);
                         if (!errorFound) {
                             errorFound = true;
                             book = new Properties();
                         }
                     }
                 } else if (fieldsEnum == Book.DATABASE.SuggestedPrice){
                     try {
                         double i = Double.parseDouble(str);
                         fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                         if (!errorFound) {
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
                     fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                     if(!errorFound){
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
        comboBox.getItems().addAll(Utilities.getStringLang("condgood"), Utilities.getStringLang("conddmg"));
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(Utilities.getStringLang("statusact"), Utilities.getStringLang("statusinact"));
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

//    abstract void clearFields();





}
