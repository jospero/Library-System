package userinterface.book;

import Utilities.Utilities;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.GridPane;
import model.Book;
import userinterface.InformationView;
import validation.DoubleValidator;
import validation.ExactValidator;
import validation.NumberValidator;
import validation.RequiredFieldValidator;

import java.util.*;
import java.util.logging.Logger;

import static model.Book.getFields;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class BookInformationView extends InformationView<Book.DATABASE> {

    private static final Logger LOGGER = Logger.getLogger( BookInformationView.class.getName() );

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
        LOGGER.info(String.valueOf(book));
        for(Book.DATABASE fEnum : Book.DATABASE.values()){
            if(fEnum != Book.DATABASE.Status && fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                field.label.setAlignment(Pos.CENTER_RIGHT);
                if(fEnum == Book.DATABASE.Condition){
                    field.field = getConditionNode();
                    if(book.get(row) != null && !book.get(row).isEmpty()) {
                        ((JFXComboBox<Condition>) field.field).getSelectionModel().select(Condition.getConditionIndex(book.get(row)));
                    }
                } else if(fEnum == Book.DATABASE.Notes) {
                    JFXTextArea ta = new JFXTextArea();
                    field.field = ta;
                } else {
                    JFXTextField fTF = new JFXTextField();

                    RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
                    fTF.getValidators().add(requiredFieldValidator);
                    if(fEnum == Book.DATABASE.Barcode || fEnum == Book.DATABASE.ISBN || fEnum == Book.DATABASE.YearOfPublication){
                        NumberValidator numberValidator = new NumberValidator();
                        fTF.getValidators().add(numberValidator);
                        numberValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_be_num"));

                        if(fEnum == Book.DATABASE.YearOfPublication){
                            Utilities.addTextLimiter(fTF, 4);
                            ExactValidator exactValidator = new ExactValidator(4);
                            exactValidator.setMessage(fieldsStr.get(fEnum)+" : 4 length");
                            fTF.getValidators().add(exactValidator);
                        }
                        if(fEnum == Book.DATABASE.Barcode){
                            Utilities.addTextLimiter(fTF, 5);
                            ExactValidator exactValidator = new ExactValidator(5);
                            exactValidator.setMessage(fieldsStr.get(fEnum)+" : 5 length");
                            fTF.getValidators().add(exactValidator);
                        }
                        if(fEnum == Book.DATABASE.ISBN){
                            Utilities.addTextLimiter(fTF, 13);
                            ExactValidator exactValidator = new ExactValidator(13);
                            exactValidator.setMessage(fieldsStr.get(fEnum)+" : 13 length");
                            fTF.getValidators().add(exactValidator);
                        }

                    }
                    if(fEnum == Book.DATABASE.SuggestedPrice){
                        DoubleValidator doubleValidator = new DoubleValidator();
                        fTF.getValidators().add(doubleValidator);
                        doubleValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_be_decimal"));
                    }

                    requiredFieldValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_not_empty"));
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

    public static boolean validate(JFXTextField... textFields){
        boolean valid = true;
        for(JFXTextField jtf : textFields){
            valid = valid && jtf.validate();
        }
        return valid;
    }

    final public Properties validateBook(){
        boolean valid = true;
        Properties book = new Properties();
        for(Book.DATABASE fieldsEnum: fieldsList.keySet()){
            if(fieldsList.get(fieldsEnum).field instanceof JFXTextField){
                LOGGER.info(fieldsEnum.name());
                valid = ((JFXTextField) fieldsList.get(fieldsEnum).field).validate() && valid;
                String str = ((JFXTextField) fieldsList.get(fieldsEnum).field).getText();
                book.setProperty(fieldsEnum.name(), str);
            } else if(fieldsList.get(fieldsEnum).field instanceof JFXComboBox){
                Condition cond = (Condition) ((JFXComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem();
                String str = Utilities.getStringInEng(cond.key);
                book.setProperty(fieldsEnum.name(), str);
            } else{
                String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                book.setProperty(fieldsEnum.name(), str);
            }
        }
        if(!valid){
            book.clear();
        }
        LOGGER.info(String.valueOf(book));
        return book;
    }

    private String Databasify(String field){
        return field.replaceAll("[^a-zA-Z0-9]", "");
    }

    private JFXComboBox<Condition> getConditionNode(){
        JFXComboBox<Condition> comboBox = new JFXComboBox<>();
        Map<String, String> cond = Condition.getConditions();
        for(Map.Entry<String, String> entry: cond.entrySet()){
            Condition temp = new Condition(Utilities.getStringLang(entry.getValue()), entry.getValue());
            comboBox.getItems().add(temp);
        }
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

    private static class Condition{
        private static final Map<String, String> conditionList;
        static {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("good","condgood");
            map.put("damaged","conddmg");
            conditionList = Collections.unmodifiableMap(map);
        }

        String value;
        String key;

        public Condition(String value, String key) {
            this.value = value;
            this.key = key;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Map<String, String> getConditions(){
            return conditionList;
        }
        public static int getConditionIndex(String condition){

            return new ArrayList<String>(conditionList.keySet()).indexOf(condition.toLowerCase());
        }

    }


//    abstract void clearFields();





}
