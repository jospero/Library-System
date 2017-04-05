package userinterface.studentborrower;

import Utilities.Utilities;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import model.StudentBorrower;
import model.Worker;
import userinterface.InformationView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Vector;

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
            if(fEnum != StudentBorrower.DATABASE.Status && fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);

                if(fEnum == StudentBorrower.DATABASE.BorrowerStatus){
                    field.field = getBorrowerStatusNode();
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(studentBorrower.get(row));
                } else if(fEnum == StudentBorrower.DATABASE.DateOfLastBorrowerStatus || fEnum == StudentBorrower.DATABASE.DateOfRegistration) {

                    LocalDate localDate;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Utilities.getStringNorm("dateFormat"));

                    DateTimeFormatter dbformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty()){
                        localDate = LocalDate.parse(studentBorrower.get(row), dbformatter);
                    } else {
                        localDate = LocalDate.now();
                    }
                    JFXTextField fTF = new JFXTextField(localDate.format(formatter));
                    fTF.setEditable(false);
                    field.field = fTF;
                } else if(fEnum == StudentBorrower.DATABASE.Notes) {
                    JFXTextArea ta = new JFXTextArea();
                    field.field = ta;
                }   else
                 {
                    JFXTextField fTF = new JFXTextField();
                    fTF.setEditable(enableFields);
                    fTF.setPromptText(str);
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        fTF.setText(studentBorrower.get(row));
                     if(fEnum == StudentBorrower.DATABASE.BannerId){
                         fTF.setEditable(!modify);
                     }
                    field.field = fTF;
                }
                fieldsList.put(fEnum, field);
//                field.label.setPrefHeight(400);
                field.label.setWrapText(true);
//                field.label.setTextAlignment(TextAlignment.JUSTIFY);
//                field.label.wrapTextProperty().bind(studentBorrowerInfo.widthProperty().);
                studentBorrowerInfo.add(field.label, 0, row);
                studentBorrowerInfo.add(field.field, 1, row);
//                studentBorrowerInfo.add(field.label, 0, row);
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

    final public Properties validateStudentBorrower() {
        Properties studentBorrower = new Properties();
        boolean errorFound = false;
        System.out.println("Checkkking student");
        for(StudentBorrower.DATABASE fieldsEnum: fieldsList.keySet()){
            if(fieldsList.get(fieldsEnum).field instanceof TextField || fieldsList.get(fieldsEnum).field instanceof TextArea) {
                String str = ((TextInputControl) fieldsList.get(fieldsEnum).field).getText();
                if (str.isEmpty() && fieldsEnum != StudentBorrower.DATABASE.Notes) {
//                    error(fieldsList.get(fieldsEnum).field);
                    if (!errorFound) {
                        errorFound = true;
                        studentBorrower = new Properties();
                    }
                } else if (fieldsEnum == StudentBorrower.DATABASE.BannerId) {
                    System.out.println("error student");
                    if (str.matches("[0-9]+")) {
                        System.out.println("error student");
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        if (!errorFound) {
                            studentBorrower.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
                        System.out.println("found student");
//                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            studentBorrower = new Properties();
                        }
                    }

                } else if (fieldsEnum == StudentBorrower.DATABASE.Phone) {
                    if (Utilities.validatePhoneNumber(str)) {
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        if (!errorFound) {
                            studentBorrower.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
//                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            studentBorrower = new Properties();
                        }
                    }
                }
                else if (fieldsEnum == StudentBorrower.DATABASE.Email){
                    if(Utilities.validateEmail(str)){
                        fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                        if (!errorFound) {
                            studentBorrower.setProperty(fieldsEnum.name(), str);
                        }
                    } else {
//                        error(fieldsList.get(fieldsEnum).field);
                        if (!errorFound) {
                            errorFound = true;
                            studentBorrower = new Properties();
                        }
                    }

                }
                else {
                    fieldsList.get(fieldsEnum).field.getStyleClass().removeAll("error");
                    if(!errorFound){
                        studentBorrower.setProperty(fieldsEnum.name(), str);
                    }
                }
            } else if( fieldsList.get(fieldsEnum).field instanceof ComboBox) {
                if(!errorFound) {
                    String str = ((ComboBox) fieldsList.get(fieldsEnum).field).getSelectionModel().getSelectedItem().toString();
                    studentBorrower.setProperty(fieldsEnum.name(), str);
                }
            } else{
                if(!errorFound) {
                    LocalDate date = ((DatePicker) fieldsList.get(fieldsEnum).field).getValue();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    studentBorrower.setProperty(fieldsEnum.name(), date.format(formatter));
                }
            }
        }
        System.out.println(studentBorrower.toString());
        return studentBorrower;
    }

    private ComboBox getBorrowerStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(Utilities.getStringLang("borrowstat"), Utilities.getStringLang("borrowstatdel"));
        comboBox.setValue(Utilities.getStringLang("borrowstat"));
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll(Utilities.getStringLang("statusact"), Utilities.getStringLang("statusinact"));
        comboBox.setValue(Utilities.getStringLang("statusact"));
        return comboBox;
    }

}
