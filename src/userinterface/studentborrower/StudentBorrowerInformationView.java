package userinterface.studentborrower;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.StudentBorrower;
import userinterface.View;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class StudentBorrowerInformationView extends View {

    enum FieldsEnum{
        BANNERID, FIRSTNAME, LASTNAME, PHONE, EMAIL, BORROWERSTATUS, DATEOFLASTBORROWERSTATUS, DATEOFREGISTRATION,
        NOTES, STATUS
    }

    private class Fields{
        Label label = new Label();
        Node field;
    }
    HashMap<FieldsEnum, Fields> fieldsList = new HashMap<>();
    HashMap<FieldsEnum, String> fieldsStr = new HashMap<>();

    boolean enableFields;



    public StudentBorrowerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, classname);
        this.enableFields = enableFields;

        fieldsStr.put(FieldsEnum.BANNERID, messages.getString("bid"));
        fieldsStr.put(FieldsEnum.FIRSTNAME,messages.getString("fname"));
        fieldsStr.put(FieldsEnum.LASTNAME, messages.getString("lname"));
        fieldsStr.put(FieldsEnum.PHONE, messages.getString("phone_num"));
        fieldsStr.put(FieldsEnum.EMAIL, messages.getString("email"));
        fieldsStr.put(FieldsEnum.BORROWERSTATUS,messages.getString("sb_status"));
        fieldsStr.put(FieldsEnum.DATEOFLASTBORROWERSTATUS, messages.getString("date_latest_sb_status"));
        fieldsStr.put(FieldsEnum.DATEOFREGISTRATION, messages.getString("date_reg"));
        fieldsStr.put(FieldsEnum.NOTES, messages.getString("notes"));
        fieldsStr.put(FieldsEnum.STATUS, messages.getString("status"));

//        getFieldsString();
    }

    public final GridPane getStudentBorrowerInformation(){
        GridPane studentBorrowerInfo = new GridPane();
        studentBorrowerInfo.setHgap(10);
        studentBorrowerInfo.setVgap(10);
        studentBorrowerInfo.setPadding(new Insets(0, 10, 0, 10));

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
