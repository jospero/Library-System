package userinterface.worker;

import Utilities.Utilities;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;
import model.Book;
import model.Worker;
import userinterface.InformationView;
import validation.EmailValidator;
import validation.ExactValidator;
import validation.NumberValidator;
import validation.RequiredFieldValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static model.Worker.getFields;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class WorkerInformationView extends InformationView<Worker.DATABASE> {

//    boolean enableFields;
    private static final  DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public WorkerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);
    }

    @Override
    protected void setupFields() {
        fieldsStr = getFields();
    }



    public GridPane getInformation(){
        GridPane workerInfo = super.getInformation();
        int row = 0;
        Vector<String> worker = ((Worker) myModel).getEntryListView();
        for(Worker.DATABASE fEnum : Worker.DATABASE.values()){
            if(fEnum != Worker.DATABASE.Status && fieldsStr.containsKey(fEnum) ){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                RequiredFieldValidator requiredFieldValidator = new RequiredFieldValidator();
                requiredFieldValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_not_empty"));
                if(fEnum == Worker.DATABASE.Credentials){
                    field.field = getCredentialsNode();
                    if(worker.get(row) != null && !worker.get(row).isEmpty()) {
                        ((JFXComboBox<Credentials>) field.field).getSelectionModel().select(Credentials.getConditionIndex(worker.get(row)));
                    }
                }
                else if(fEnum == Worker.DATABASE.DateOfHire || fEnum == Worker.DATABASE.DateOfLatestCredentialStatus) {
                    if(modify) {
                        LocalDate localDate;
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.getStringNorm("dateFormat"));

                        if (worker.get(row) != null && !worker.get(row).isEmpty()) {
                            localDate = LocalDate.parse(worker.get(row), dbformatter);
                        } else {
                            localDate = LocalDate.now();
                        }
                        JFXTextField fTF = new JFXTextField(localDate.format(formatter));
                        fTF.setEditable(false);
                        field.field = fTF;
                    }
                } else if(fEnum == Worker.DATABASE.Phone ){

                    HBox box = new HBox();
                    box.setSpacing(10);
                    JFXTextField area = new JFXTextField();
                    area.setPromptText("Area");
                    area.setPromptText(Utilities.getStringLang("cCode"));
                    Utilities.addTextLimiter(area, 3);
                    JFXTextField phone = new JFXTextField();
                    phone.setPromptText("Phone Number");
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
                    if (worker.get(row) != null && !worker.get(row).isEmpty()) {
                        String[] splitPhone = worker.get(row).split("-");
                        area.setText(splitPhone[0]);
                        phone.setText(splitPhone[1]);
                    }
                }else if(fEnum == Worker.DATABASE.Password){
                    JFXPasswordField fPF = new JFXPasswordField();
                    fPF.setPromptText(str);
                    field.field = fPF;
                    fPF.setEditable(enableFields);
                    fPF.getValidators().add(requiredFieldValidator);
                    fPF.focusedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                            if(!newValue)
                                fPF.validate();
                        }
                    });
                }
                else {
                    JFXTextField fTF = new JFXTextField();
                    fTF.setPromptText(str);
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        fTF.setText(worker.get(row));
                    if(fEnum == Worker.DATABASE.BannerId){
                        fTF.setEditable(!modify);
                        Utilities.addTextLimiter(fTF, 9);
                        NumberValidator numberValidator = new NumberValidator();
                        fTF.getValidators().add(numberValidator);
                        numberValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_be_num"));
                        ExactValidator exactValidator = new ExactValidator(9);
                        exactValidator.setMessage(fieldsStr.get(fEnum)+" : 9 length");
                        fTF.getValidators().add(exactValidator);
                    } else if (fEnum == Worker.DATABASE.Email){
                        EmailValidator emailValidator = new EmailValidator();
                        emailValidator.setMessage(fieldsStr.get(fEnum)+" " + Utilities.getStringLang("must_in_email"));
                        fTF.getValidators().add(emailValidator);
                    }

                    field.field = fTF;
                    fTF.setEditable(enableFields);
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
//                field.label.setPrefHeight(400);
                field.label.setWrapText(true);
                if(field.label != null && field.field != null) {
                    workerInfo.add(field.label, 0, row);
                    workerInfo.add(field.field, 1, row);
                }
//                workerInfo.add(field.label, 0, row);
                row++;
            }
        }


        return workerInfo;


    }

//    private void error(Node n){
//        if(!n.getStyleClass().contains("error")){
//            n.getStyleClass().add("error");
//        }
//    }

    final public Properties validateWorker(){
        boolean valid = true;
        Properties book = new Properties();
        for(Worker.DATABASE fieldsEnum: fieldsList.keySet()){
            if(fieldsList.get(fieldsEnum).field instanceof JFXTextField){
                String str = ((JFXTextField) fieldsList.get(fieldsEnum).field).getText();
                valid = ((JFXTextField) fieldsList.get(fieldsEnum).field).validate() && valid;
                book.setProperty(fieldsEnum.name(), str);
            } else if(fieldsList.get(fieldsEnum).field instanceof JFXComboBox){
                Credentials cond = (Credentials) ((JFXComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem();
                String str = Utilities.getStringInEng(cond.key);
                book.setProperty(fieldsEnum.name(), str);
            } else{
                if(fieldsEnum != Worker.DATABASE.DateOfHire && fieldsEnum != Worker.DATABASE.DateOfLatestCredentialStatus) {
                    String str = "";
                    if(fieldsEnum != Worker.DATABASE.Phone){
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
            book.setProperty(Worker.DATABASE.DateOfLatestCredentialStatus.name(), LocalDate.now().format(formatter));
            if(!modify){
                book.setProperty(Worker.DATABASE.DateOfHire.name(), LocalDate.now().format(formatter));
            }
        }
        System.out.println(book);
        return book;
    }


    private JFXComboBox getCredentialsNode(){
        JFXComboBox<Credentials> comboBox = new JFXComboBox<>();
        Map<String, String> cond = Credentials.getConditions();
        for(Map.Entry<String, String> entry: cond.entrySet()){
            Credentials temp = new Credentials(Utilities.getStringLang(entry.getValue()), entry.getValue());
            comboBox.getItems().add(temp);
        }
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

//    private ComboBox getStatusNode(){
//        ComboBox comboBox = new ComboBox();
//        comboBox.getItems().addAll(Utilities.getStringLang("statusact"), Utilities.getStringLang("statusinact"));
//        comboBox.getSelectionModel().select(0);
//        return comboBox;
//    }

    private static class Credentials{
        private static final Map<String, String> credList;
        static {
            Map<String, String> map = new LinkedHashMap<>();
            map.put("ordinary","credsord");
            map.put("administrator","credsadmin");
            credList = Collections.unmodifiableMap(map);
        }

        String value;
        String key;

        public Credentials(String value, String key) {
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
