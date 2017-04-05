package userinterface.worker;

import Utilities.Utilities;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import model.Book;
import model.Worker;
import userinterface.InformationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Vector;

import static model.Worker.getFields;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class WorkerInformationView extends InformationView<Worker.DATABASE> {

//    boolean enableFields;

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
                if(fEnum == Worker.DATABASE.Credentials){
                    field.field = getCredentialsNode();
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(worker.get(row));
                }
                else if(fEnum == Worker.DATABASE.DateOfHire || fEnum == Worker.DATABASE.DateOfLatestCredentialStatus) {
                    LocalDate localDate;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.getStringNorm("dateFormat"));

                    DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    if(worker.get(row) != null && !worker.get(row).isEmpty()){
                        localDate = LocalDate.parse(worker.get(row), dbformatter);
                    } else {
                        localDate = LocalDate.now();
                    }
                    JFXTextField fTF = new JFXTextField(localDate.format(formatter));
                    fTF.setEditable(false);
                    field.field = fTF;
                }
                else {
                    JFXTextField fTF = new JFXTextField();
                    fTF.setPromptText(str);
                    if(worker.get(row) != null && !worker.get(row).isEmpty())
                        fTF.setText(worker.get(row));
                    if(fEnum == Worker.DATABASE.BannerId){
                        fTF.setEditable(!modify);
                    }
                    field.field = fTF;
                    fTF.setEditable(enableFields);
                }
                fieldsList.put(fEnum, field);
//                field.label.setPrefHeight(400);
                field.label.setWrapText(true);
                workerInfo.add(field.label, 0, row);
                workerInfo.add(field.field, 1, row);
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
        Properties worker = new Properties();
        boolean errorFound = false;
        for(Worker.DATABASE fieldsEnum: fieldsList.keySet()){
            if(fieldsList.get(fieldsEnum).field instanceof TextField || fieldsList.get(fieldsEnum).field instanceof TextArea) {
                String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                if (str.isEmpty()) {
                    if (!errorFound) {
                        errorFound = true;
                        worker = new Properties();
                    }
                } else if (fieldsEnum == Worker.DATABASE.BannerId) {
                    if (str.matches("[0-9]+")) {
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        if (!errorFound) {
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }
                    }

                } else if (fieldsEnum == Worker.DATABASE.Phone) {
                    if (Utilities.validatePhoneNumber(str)) {
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        if (!errorFound) {
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }
                    }
                }
                else if (fieldsEnum == Worker.DATABASE.Email){
                    if(Utilities.validateEmail(str)){
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        if (!errorFound) {
                            worker.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        if (!errorFound) {
                            errorFound = true;
                            worker = new Properties();
                        }
                    }

                }
                else {
                    fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                    if(!errorFound){
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
        return worker;
    }

    private JFXComboBox getCredentialsNode(){
        JFXComboBox comboBox = new JFXComboBox();
        comboBox.getItems().addAll(Utilities.getStringLang("credsord"),Utilities.getStringLang("credsadmin"));
        comboBox.getSelectionModel().select(0);
        return comboBox;
    }

//    private ComboBox getStatusNode(){
//        ComboBox comboBox = new ComboBox();
//        comboBox.getItems().addAll(Utilities.getStringLang("statusact"), Utilities.getStringLang("statusinact"));
//        comboBox.getSelectionModel().select(0);
//        return comboBox;
//    }

}
