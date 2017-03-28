package userinterface.studentborrower;

import impresario.IModel;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.StudentBorrower;
import userinterface.InformationView;

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
            if(fieldsStr.containsKey(fEnum)){
                String str = fieldsStr.get(fEnum);
                Fields field = new Fields();
                field.label.setText(str);
                if(fEnum == StudentBorrower.DATABASE.BorrowerStatus){
                    field.field = getBorrowerStatusNode();
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(studentBorrower.get(row));
                } else if(fEnum == StudentBorrower.DATABASE.Status){
                    field.field = getStatusNode();
                    if(studentBorrower.get(row) != null && !studentBorrower.get(row).isEmpty())
                        ((ComboBox)field.field).setValue(studentBorrower.get(row));
                } else if(fEnum == StudentBorrower.DATABASE.DateOfLastBorrowerStatus || fEnum == StudentBorrower.DATABASE.DateOfRegistration) {
                    DatePicker datePicker = new DatePicker();
                    field.field = datePicker;
                } else if(fEnum == StudentBorrower.DATABASE.Notes) {
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
