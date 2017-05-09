package userinterface.studentborrower;

import Utilities.Utilities;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import model.StudentBorrower;
import model.Worker;
import userinterface.InformationView;
import validation.EmailValidator;
import validation.NumberValidator;
import validation.RequiredFieldValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static model.StudentBorrower.getFields;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class StudentBorrowerInformationView extends InformationView<StudentBorrower.DATABASE> {


    public StudentBorrowerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);

    }



    @Override
    protected void setupFields() {
        fieldsStr = getFields();
    }

    public final GridPane getInformation(){
        GridPane studentBorrowerInfo = super.getInformation();
        int row = 0;
        Vector<String> studentBorrower = ((StudentBorrower) myModel).getEntryListView();
        for(StudentBorrower.DATABASE fEnum : StudentBorrower.DATABASE.values()){
            RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
            requiredFieldValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_not_empty"));
            if(fEnum != StudentBorrower.DATABASE.Status && fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == StudentBorrower.DATABASE.BorrowerStatus){
                    field.field = getBorrowerStatusNode();
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        ((JFXComboBox<BorrowerStatus>) field.field).getSelectionModel().select(BorrowerStatus.getConditionIndex(studentBorrower.get(row)));
                } else if(fEnum == StudentBorrower.DATABASE.DateOfLastBorrowerStatus || fEnum == StudentBorrower.DATABASE.DateOfRegistration) {
                    if(modify) {
                        LocalDate localDate;
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.getStringNorm("dateFormat"));

                        DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        if (studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty()) {
                            localDate = LocalDate.parse(studentBorrower.get(row), dbformatter);
                        } else {
                            localDate = LocalDate.now();
                        }
                        JFXTextField fTF = new JFXTextField(localDate.format(formatter));
                        fTF.setEditable(false);
                        field.field = fTF;
                    }
                } else if(fEnum == StudentBorrower.DATABASE.Notes) {
                    JFXTextArea ta = new JFXTextArea();
                    field.field = ta;
                } else if(fEnum == StudentBorrower.DATABASE.Phone ){
                    HBox box = new HBox();
                    box.setSpacing(10);

                    JFXTextField area = new JFXTextField();
                    area.setPromptText(Utilities.getStringLang("cCode"));
                    Utilities.addTextLimiter(area, 3);
                    JFXTextField phone = new JFXTextField();
                    phone.setPromptText(str);
                    phone.getValidators().add(requiredFieldValidator);
                    Utilities.addTextLimiter(phone, 10);
                    area.setPrefWidth(60);
                    box.getChildren().addAll(area, phone);
                    field.field = box;
                    NumberValidator numberValidator = new NumberValidator();
                    phone.getValidators().add(numberValidator);
                    phone.setMaxWidth(Double.MAX_VALUE);
                    HBox.setHgrow(phone, Priority.ALWAYS);
                    area.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(!newValue)
                                area.validate();
                        }
                    });
                    phone.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(!newValue)
                                phone.validate();
                        }
                    });
                    if (studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty()) {
                        String[] splitPhone = studentBorrower.get(row).split("-");
                        area.setText(splitPhone[0]);
                        phone.setText(splitPhone[1]);
                    }
                }  else {
                    JFXTextField fTF = new JFXTextField();
                    fTF.setEditable(enableFields);
                    fTF.setPromptText(str);

                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        fTF.setText(studentBorrower.get(row));
                     if(fEnum == StudentBorrower.DATABASE.BannerId){
                         fTF.setEditable(!modify);
                         NumberValidator numberValidator = new NumberValidator();
                         fTF.getValidators().add(numberValidator);
                         numberValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_be_num"));
                     } else if (fEnum == StudentBorrower.DATABASE.Email){
                         EmailValidator emailValidator = new EmailValidator();
                         emailValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_in_email"));
                         fTF.getValidators().add(emailValidator);
                     }

                    field.field = fTF;

                    fTF.getValidators().add(requiredFieldValidator);
                    fTF.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(!newValue)
                                fTF.validate();
                        }
                    });
                }
                fieldsList.put(fEnum, field);
                field.label.setWrapText(true);

                if(field.label != null && field.field != null) {
                    studentBorrowerInfo.add(field.label, 0, row);
                    studentBorrowerInfo.add(field.field, 1, row);
                }
                row++;
            }
        }


        return studentBorrowerInfo;


    }

//    private void error(Node n){
//        if(!n.getStyleClass().contains("error")){
//            n.getStyleClass().add("error");
//        }
//    }

    final public Properties validateStudentBorrower(){
        boolean valid = true;
        Properties book = new Properties();
        for(StudentBorrower.DATABASE fieldsEnum: fieldsList.keySet()){
            if(fieldsList.get(fieldsEnum).field instanceof JFXTextField){
                String str = ((JFXTextField) fieldsList.get(fieldsEnum).field).getText();
                valid = ((JFXTextField) fieldsList.get(fieldsEnum).field).validate() && valid;
                book.setProperty(fieldsEnum.name(), str);
            } else if(fieldsList.get(fieldsEnum).field instanceof JFXComboBox){
                BorrowerStatus cond = (BorrowerStatus) ((JFXComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem();
                String str = Utilities.getStringInEng(cond.key);
                book.setProperty(fieldsEnum.name(), str);
            } else{
                if(fieldsEnum != StudentBorrower.DATABASE.DateOfRegistration && fieldsEnum != StudentBorrower.DATABASE.DateOfLastBorrowerStatus) {
                    String str = "";
                    if(fieldsEnum != StudentBorrower.DATABASE.Phone){
                        str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                    } else{
                        JFXTextField node1 = (JFXTextField) ((HBox) fieldsList.get(fieldsEnum).field).getChildren().get(0);
                        JFXTextField node2 = (JFXTextField) ((HBox) fieldsList.get(fieldsEnum).field).getChildren().get(1);
                        valid = node1.validate() && node2.validate() && valid;

                        str = node1.getText().trim() + "-" + node2.getText();
                    }
                    book.setProperty(fieldsEnum.name(), str);
                }
            }
        }
        if(!valid){
            book.clear();
        } else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            book.setProperty(StudentBorrower.DATABASE.DateOfLastBorrowerStatus.name(), LocalDate.now().format(formatter));
            if(!modify){
                book.setProperty(StudentBorrower.DATABASE.DateOfRegistration.name(), LocalDate.now().format(formatter));
            }
        }
        System.out.println(book);
        return book;
    }



    private JFXComboBox getBorrowerStatusNode(){
        JFXComboBox<BorrowerStatus> comboBox = new JFXComboBox<>();
        Map<String, String> cond = BorrowerStatus.getConditions();
        for(Map.Entry<String, String> entry: cond.entrySet()){
            BorrowerStatus temp = new BorrowerStatus(Utilities.getStringLang(entry.getValue()), entry.getValue());
            comboBox.getItems().add(temp);
        }
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

    private static class BorrowerStatus{
        private static final Map<String, String> credList;
        static {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("good standing","borrowstat");
            map.put("delinquent","borrowstatdel");
            credList = Collections.unmodifiableMap(map);
        }

        String value;
        String key;

        public BorrowerStatus(String value, String key) {
            this.value = value;
            this.key = key;
        }

        @Override
        public String toString() {
            return value;
        }

        public static Map<String, String> getConditions(){
            return credList;
        }
        public static int getConditionIndex(String condition){

            return new ArrayList<String>(credList.keySet()).indexOf(condition.toLowerCase());
        }

    }

}
