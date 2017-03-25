package userinterface.worker;

import Utilities.Utilities;
import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.Worker;
import userinterface.InformationView;
import userinterface.View;
import userinterface.book.BookInformationView;
import userinterface.studentborrower.StudentBorrowerInformationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class WorkerInformationView extends InformationView<WorkerInformationView.FieldsEnum> {

    enum FieldsEnum{
        BannerId, Password, FirstName, LastName, Phone, Email, Credentials, DateOfLatestCredentialStatus, DateOfHire,
        Status
    }
//    boolean enableFields;

    public WorkerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);
    }

    @Override
    protected void setupFields() {
        fieldsStr = getFields();
    }

    public static HashMap<FieldsEnum, String> getFields(){
        HashMap<FieldsEnum, String> fieldsStr = new HashMap<>();
        fieldsStr.put(FieldsEnum.BannerId, "Banner ID");
        fieldsStr.put(FieldsEnum.Password, "Password");
        fieldsStr.put(FieldsEnum.FirstName, "First Name");
        fieldsStr.put(FieldsEnum.LastName, "Last Name");
        fieldsStr.put(FieldsEnum.Phone, "Phone");
        fieldsStr.put(FieldsEnum.Email, "Email");
        fieldsStr.put(FieldsEnum.Credentials, "Credentials");
        fieldsStr.put(FieldsEnum.DateOfLatestCredentialStatus, "Date of Last Credentials Status");
        fieldsStr.put(FieldsEnum.DateOfHire, "Date of Hire");
        fieldsStr.put(FieldsEnum.Status, "Status");
        return fieldsStr;
    }

    public GridPane getInformation(){
        GridPane workerInfo = super.getInformation();
        int row = 0;
        Vector<String> worker = ((Worker) myModel).getEntryListView();
        for(FieldsEnum fEnum : FieldsEnum.values()){
            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == FieldsEnum.Credentials){
                    field.field = getCredentialsNode();
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(worker.get(row));
                } else if(fEnum == FieldsEnum.Status){
                    field.field = getStatusNode();
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(worker.get(row));
                }
                else if(fEnum == FieldsEnum.DateOfHire || fEnum == FieldsEnum.DateOfLatestCredentialStatus) {
                    LocalDate localDate;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    if(worker.get(row) != null && !worker.get(row).isEmpty()){
                        localDate = LocalDate.parse(worker.get(row), formatter);
                    } else {
                        localDate = LocalDate.now();
                    }
                    DatePicker datePicker = new DatePicker(localDate);
                    datePicker.setEditable(false);
                    Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
                        @Override
                        public DateCell call(DatePicker param) {
                            return new DateCell(){
                                @Override
                                public void updateItem(LocalDate item, boolean empty) {
                                    super.updateItem(item, empty);
                                    // Disable all future date cells
                                    if (item.isAfter(LocalDate.now()))
                                    {
                                        this.setDisable(true);
                                    }

                                }
                            };
                        }
                    };
                    datePicker.setDayCellFactory(dayCellFactory);
                    field.field = datePicker;
                }
                else {
                    TextField fTF = new TextField();
                    fTF.setPromptText(str);
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        fTF.setText(worker.get(row));
                    field.field = fTF;
                    fTF.setEditable(enableFields);
                }
                fieldsList.put(fEnum, field);
                workerInfo.add(field.label, 0, row);
                workerInfo.add(field.field, 1, row);
//                workerInfo.add(field.label, 0, row);
                row++;
            }
        }


        return workerInfo;


    }

    private void error(Node n){
        if(!n.getStyleClass().contains("error")){
            n.getStyleClass().add("error");
        }
    }

    final public Properties validateWorker(){
        Properties worker = new Properties();
        boolean errorFound = false;
        for(FieldsEnum fieldsEnum: fieldsList.keySet()){
            if(fieldsList.get(fieldsEnum).field instanceof TextField || fieldsList.get(fieldsEnum).field instanceof TextArea) {
                String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                if (str.isEmpty()) {
                    error(fieldsList.get(fieldsEnum).field);
                    if (!errorFound) {
                        errorFound = true;
                        worker = new Properties();
                    }
                } else if (fieldsEnum == FieldsEnum.BannerId) {
                    if (str.matches("[0-9]+")) {
                        if (!errorFound) {
                            fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }
                    }

                } else if (fieldsEnum == FieldsEnum.Phone) {
                    if (Utilities.validatePhoneNumber(str)) {
                        if (!errorFound) {
                            fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }
                    }
                }
                else if (fieldsEnum == FieldsEnum.Email){
                    Pattern VALID_Email_ADDRESS_REGEX =
                            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = VALID_Email_ADDRESS_REGEX.matcher(str);
                    if(matcher.find()){
                        if (!errorFound) {
                            fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }
                    }

                }
                else {
                    if(!errorFound){
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        worker.setProperty(fieldsEnum.name(), str);
                    }
                }
            } else if( fieldsList.get(fieldsEnum).field instanceof ComboBox) {
                if(!errorFound) {
                    String str = ((ComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem().toString();
                    worker.setProperty(fieldsEnum.name(), str);
                }
            } else{
                if(!errorFound) {
                    LocalDate date = ((DatePicker) fieldsList.get(fieldsEnum).field).getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    worker.setProperty(fieldsEnum.name(), date.format(formatter));
                }
            }
        }
        System.out.println(worker.toString());
        return worker;
    }

    private ComboBox getCredentialsNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Ordinary", "Administrator");
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Active", "Inactive");
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

}
