package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.DoubleValidator;
import com.jfoenix.validation.NumberValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import model.Book;
import userinterface.InformationView;

import java.time.Year;
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
            if(fEnum != Book.DATABASE.Status && fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == Book.DATABASE.Condition){
                    field.field = getConditionNode();
                    if(book.get(row) != null && !book.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(book.get(row));
                } else if(fEnum == Book.DATABASE.Notes) {
                    JFXTextArea ta = new JFXTextArea();
                    field.field = ta;
                } else {
                    JFXTextField fTF = new JFXTextField();
                    RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
                    fTF.getValidators().add(requiredFieldValidator);
//                    requiredFieldValidator.setErrorStyleClass("field-error");
                    if(fEnum == Book.DATABASE.Barcode || fEnum == Book.DATABASE.ISBN || fEnum == Book.DATABASE.YearOfPublication){
                        NumberValidator numberValidator = new NumberValidator();
                        fTF.getValidators().add(numberValidator);
                        numberValidator.setMessage(fieldsStr.get(fEnum)+" must be a number");
                        if(fEnum == Book.DATABASE.YearOfPublication){
                            Utilities.addTextLimiter(fTF, 4);
                        }
                        if(fEnum == Book.DATABASE.Barcode){
                            Utilities.addTextLimiter(fTF, 5);
                        }
                        if(fEnum == Book.DATABASE.ISBN){
                            Utilities.addTextLimiter(fTF, 13);
                        }

                    }
                    if(fEnum == Book.DATABASE.SuggestedPrice){
                        DoubleValidator doubleValidator = new DoubleValidator();
                        fTF.getValidators().add(doubleValidator);
                        doubleValidator.setMessage(fieldsStr.get(fEnum)+" must be a decimal");
                    }

                    requiredFieldValidator.setMessage(fieldsStr.get(fEnum)+" must not be empty");
                    fTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(!newValue)
                                fTF.validate();
                        }
                    });

                    fTF.setPromptText(str);
//                    fTF.setLabelFloat(true);
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

    final public Properties validateBook(){
        Properties book = new Properties();
        boolean errorFound = false;
        System.out.println("Checking");
        for(Book.DATABASE fieldsEnum: fieldsList.keySet()){
             if(fieldsList.get(fieldsEnum).field instanceof JFXTextField || fieldsList.get(fieldsEnum).field instanceof JFXTextArea) {
                 String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                 if (str.isEmpty() && fieldsEnum != Book.DATABASE.Notes) {

                     if (!errorFound) {
                         errorFound = true;
                         book = new Properties();
                     }
                 } else if (fieldsEnum == Book.DATABASE.Barcode || fieldsEnum == Book.DATABASE.YearOfPublication ||
                         fieldsEnum == Book.DATABASE.ISBN) {


                     if (str.matches("[0-9]+")) {
                         if (!errorFound) {
                             book.setProperty(fieldsEnum.name(), str);
                         }
                     } else {
                         if (!errorFound) {
                             errorFound = true;
                             book = new Properties();
                         }
                     }
                 } else if (fieldsEnum == Book.DATABASE.SuggestedPrice){
                     try {
                         double i = Double.parseDouble(str);
                         if (!errorFound) {
                             book.setProperty(fieldsEnum.name(), str);
                         }
                     } catch (NumberFormatException ex) {
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
//                    int index =  ((ComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedIndex();
                     String str = ((JFXComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem().toString();
//                    String str =
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

    private JFXComboBox getConditionNode(){
        JFXComboBox comboBox = new JFXComboBox();
        comboBox.getItems().addAll(Utilities.getStringLang("condgood"), Utilities.getStringLang("conddmg"));
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

//    abstract void clearFields();





}
