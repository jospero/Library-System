package userinterface.worker;

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
        BANNERID, PASSWORD, FIRSTNAME, LASTNAME, PHONE, EMAIL, CREDENTIALS, DATEOFLASTCREDENTIALSTATUS, DATEOFHIRE,
        STATUS
    }
    boolean enableFields;

    public WorkerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);
    }

    @Override
    protected void setupFields() {
        fieldsStr.put(FieldsEnum.BANNERID, "Banner ID");
        fieldsStr.put(FieldsEnum.PASSWORD, "Password");
        fieldsStr.put(FieldsEnum.FIRSTNAME, "First Name");
        fieldsStr.put(FieldsEnum.LASTNAME, "Last Name");
        fieldsStr.put(FieldsEnum.PHONE, "Phone");
        fieldsStr.put(FieldsEnum.EMAIL, "Email");
        fieldsStr.put(FieldsEnum.CREDENTIALS, "Credentials");
        fieldsStr.put(FieldsEnum.DATEOFLASTCREDENTIALSTATUS, "Date of Last Credentials Status");
        fieldsStr.put(FieldsEnum.DATEOFHIRE, "Date of Hire");
        fieldsStr.put(FieldsEnum.STATUS, "Status");
    }

    public GridPane getInformation(){
        GridPane workerInfo = super.getInformation();
        int row = 0;
        Vector<String> worker = ((Worker) myModel).getEntryListView();
        for(FieldsEnum fEnum : FieldsEnum.values()){
            if(fieldsStr.containsKey(fEnum) && fEnum != FieldsEnum.PASSWORD){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == FieldsEnum.CREDENTIALS){
                    field.field = getCredentialsNode();
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(worker.get(row));
                } else if(fEnum == FieldsEnum.STATUS){
                    field.field = getStatusNode();
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(worker.get(row));
                }
                else if(fEnum == FieldsEnum.DATEOFHIRE || fEnum == FieldsEnum.DATEOFLASTCREDENTIALSTATUS) {
                    LocalDate localDate;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-DD");
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
                } else if (fieldsEnum == FieldsEnum.BANNERID || fieldsEnum == FieldsEnum.PHONE) {
                    try {
                        int i = Integer.parseInt(str);
                        if (!errorFound) {
                            fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } catch (NumberFormatException ex) {
                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }

                    }
                } else if (fieldsEnum == FieldsEnum.EMAIL){
                    Pattern VALID_EMAIL_ADDRESS_REGEX =
                            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
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
            } else {
                if(!errorFound) {
                    String str = ((ComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem().toString();
                    worker.setProperty(fieldsEnum.name(), str);
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
