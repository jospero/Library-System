package userinterface.worker;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Worker;
import userinterface.View;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by Sammytech on 3/9/17.
 */
public abstract class WorkerInformationView extends View {


    enum FieldsEnum{
        BANNERID, PASSWORD, FIRSTNAME, LASTNAME, PHONE, EMAIL, CREDENTIALS, DATEOFLASTCREDENTIALSTATUS, DATEOFHIRE,
        STATUS
    }

    private class Fields{
        Label label = new Label();
        Node field;
    }
    HashMap<FieldsEnum, Fields> fieldsList = new HashMap<>();
    HashMap<FieldsEnum, String> fieldsStr = new HashMap<>();

    boolean enableFields;



    public WorkerInformationView(IModel model, boolean enableFields, String classname) {
        super(model, classname);
        this.enableFields = enableFields;

        fieldsStr.put(FieldsEnum.BANNERID, messages.getString("bid"));
        fieldsStr.put(FieldsEnum.PASSWORD, messages.getString("login_pass"));
        fieldsStr.put(FieldsEnum.FIRSTNAME, messages.getString("fname"));
        fieldsStr.put(FieldsEnum.LASTNAME, messages.getString("lname"));
        fieldsStr.put(FieldsEnum.PHONE, messages.getString("phone_num"));
        fieldsStr.put(FieldsEnum.EMAIL, messages.getString("email"));
        fieldsStr.put(FieldsEnum.CREDENTIALS, messages.getString("creds"));
        fieldsStr.put(FieldsEnum.DATEOFLASTCREDENTIALSTATUS, messages.getString("date_latest_creds_status"));
        fieldsStr.put(FieldsEnum.DATEOFHIRE, messages.getString("date_hire"));
        fieldsStr.put(FieldsEnum.STATUS, messages.getString("status"));

//        getFieldsString();
    }

    public final GridPane getWorkerInformation(){
        GridPane workerInfo = new GridPane();
        workerInfo.setHgap(10);
        workerInfo.setVgap(10);
        workerInfo.setPadding(new Insets(0, 10, 0, 10));

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
                } else if(fEnum == FieldsEnum.DATEOFHIRE || fEnum == FieldsEnum.DATEOFLASTCREDENTIALSTATUS) {
                    DatePicker datePicker = new DatePicker();
                    field.field = datePicker;

                } else {
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

    private ComboBox getCredentialsNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Ordinary", "Administrator");
        comboBox.setValue("Ordinary");
        return comboBox;
    }

    private ComboBox getStatusNode(){
        ComboBox comboBox = new ComboBox();
        comboBox.getItems().addAll("Active", "Inactive");
        comboBox.setValue("Active");
        return comboBox;
    }

}
