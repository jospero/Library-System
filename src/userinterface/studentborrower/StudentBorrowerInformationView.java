package userinterface.studentborrower;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.StudentBorrower;
import userinterface.InformationView;
import userinterface.View;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class StudentBorrowerInformationView extends InformationView<StudentBorrowerInformationView.FieldsEnum> {

    enum FieldsEnum{
        BANNERID, FIRSTNAME, LASTNAME, PHONE, EMAIL, BORROWERSTATUS, DATEOFLASTBORROWERSTATUS, DATEOFREGISTRATION,
        NOTES, STATUS
    }


    public StudentBorrowerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, enableFields, classname);

    }

    @Override
    protected void setupFields() {
        fieldsStr.put(FieldsEnum.BANNERID, "Banner ID");
        fieldsStr.put(FieldsEnum.FIRSTNAME, "Firstname");
        fieldsStr.put(FieldsEnum.LASTNAME, "Lastname");
        fieldsStr.put(FieldsEnum.PHONE, "Phone");
        fieldsStr.put(FieldsEnum.EMAIL, "Email");
        fieldsStr.put(FieldsEnum.BORROWERSTATUS, "Borrower Status");
        fieldsStr.put(FieldsEnum.DATEOFLASTBORROWERSTATUS, "Date of Last Borrower Status");
        fieldsStr.put(FieldsEnum.DATEOFREGISTRATION, "Date of Registration");
        fieldsStr.put(FieldsEnum.NOTES, "Notes");
        fieldsStr.put(FieldsEnum.STATUS, "Status");
    }

    public final GridPane getInformation(){
        GridPane studentBorrowerInfo = super.getInformation();
        int row = 0;
        Vector<String> studentBorrower = ((StudentBorrower) myModel).getEntryListView();
        for(FieldsEnum fEnum : FieldsEnum.values()){
            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == FieldsEnum.BORROWERSTATUS){
                    field.field = getBorrowerStatusNode();
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(studentBorrower.get(row));
                } else if(fEnum == FieldsEnum.STATUS){
                    field.field = getStatusNode();
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(studentBorrower.get(row));
                } else if(fEnum == FieldsEnum.DATEOFLASTBORROWERSTATUS || fEnum == FieldsEnum.DATEOFREGISTRATION) {
                    DatePicker datePicker = new DatePicker();
                    field.field = datePicker;
                } else if(fEnum == FieldsEnum.NOTES) {
                    TextArea ta = new TextArea();
                    field.field = ta;
                }   else
                 {
                    TextField fTF = new TextField();
                    fTF.setEditable(enableFields);
                    fTF.setPromptText(str);
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        fTF.setText(studentBorrower.get(row));
                    field.field = fTF;
                }
                fieldsList.put(fEnum, field);
                studentBorrowerInfo.add(field.label, 0, row);
                studentBorrowerInfo.add(field.field, 1, row);
//                studentBorrowerInfo.add(field.label, 0, row);
                row++;
            }
        }


        return studentBorrowerInfo;


    }

    private ComboBox getBorrowerStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Good Standing", "Delinquent");
        comboBox.setValue("Good Standing");
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Active", "Inactive");
        comboBox.setValue("Active");
        return comboBox;
    }

}
