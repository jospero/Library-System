package model;

import Utilities.Utilities;
import exception.BookCheckInException;
import exception.InvalidPrimaryKeyException;
import impresario.IView;

import javax.swing.text.DateFormatter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by KingMiguel on 4/18/17.
 */
public class Rental extends EntityBase implements IView {
    public enum DATABASE{
        Id, BorrowerId, Barcode, CheckOutDate, CheckOutWorkerId, DueDate, CheckInDate, CheckInWorkerId

    }

    private static final String myTableName = "Rental";
    protected Properties dependencies;
//    private Vector<Properties> rentals;
    private WorkerHolder myWorkerHolder;
    private String updateStatusMessage = "";
    private boolean successFlag = true;
    private String snackBarError = "";
    protected Rental(WorkerHolder workerHolder){
        super(myTableName);
        myWorkerHolder = workerHolder;
        setDependencies();

    }

//    protected Rental(String ) {
//        super(myTableName);
//        setDependencies();
//    }

    private void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("ProcessCheckIn", "UpdateStatusMessage");
        dependencies.setProperty("ProcessCheckOut", "UpdateStatusMessage");
        dependencies.setProperty("CheckBorrowerId", "BorrowerSuccessFlag");
        myRegistry.setDependencies(dependencies);
    }


    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        System.out.println(key);
        if (key.equals("UpdateStatusMessage") || key.equals("Error"))
            return updateStatusMessage;
        else if(key.equals("SuccessFlag") || key.equals("BorrowerSuccessFlag")){
            return successFlag;
        } else if(key.equals("SnackBarErrorMessage")){
            return snackBarError;
        }
        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if(key.equals("ProcessCheckIn")){
            processCheckIn((String)value);
        } else if(key.equals("ProcessCheckOut")){
            String barcode = ((Properties) value).getProperty(DATABASE.Barcode.name());
            String bannerId = ((Properties) value).getProperty(DATABASE.BorrowerId.name());
            processCheckOut(barcode, bannerId);
        } else if (key.equals("CheckBorrowerId")){
            String borrowerId = ((Properties) value).getProperty(DATABASE.BorrowerId.name());
            successFlag = checkStudentId(borrowerId);

        } else if (key.equals("CheckBarcode")){
            String barcode= ((Properties) value).getProperty(DATABASE.Barcode.name());
            successFlag = checkBarcode(barcode);
        }
        else if(key.equals("SnackBarErrorMessage")){
            snackBarError = (String) value;
        }
        System.out.println(key);
        myRegistry.updateSubscribers(key, this);
    }

    private void processCheckOut(String barcode, String bannerId) {
        if(checkBarcode(barcode) && checkStudentId(bannerId)){
           //////////////////////////////
            if(!checkIfCheckedOut(barcode)){
                persistentState = new Properties();
                persistentState.setProperty(DATABASE.BorrowerId.name(), bannerId);
                persistentState.setProperty(DATABASE.Barcode.name(), barcode);

                persistentState.setProperty(DATABASE.DueDate.name(), getFutureDate(10));
                persistentState.setProperty(DATABASE.CheckOutDate.name(), getCurrentDate());
                persistentState.setProperty(DATABASE.CheckOutWorkerId.name(),
                        (String) myWorkerHolder.getState(Worker.DATABASE.BannerId.name()));
                try {
                    successFlag = true;
                    insertPersistentState(mySchema, persistentState);
                    updateStatusMessage = "Rental added to Database";
                    System.out.println("Created Rental");
                } catch (SQLException e) {
                    successFlag = false;
                    updateStatusMessage = e.getMessage();
                }

            } else {
                System.out.println("im so lost rn holy shit");
                successFlag = false;
                updateStatusMessage = Utilities.getStringLang("alr_check_out");
            }
        } else {
            successFlag = false;
            updateStatusMessage = Utilities.getStringLang("inv_bar");
        }
    }

    private boolean checkIfCheckedOut(String barcode) {
        String query = "SELECT * FROM " + myTableName + " WHERE (" + DATABASE.Barcode.name() + " = " + barcode + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved != null) {

            for (Properties p : allDataRetrieved) {
                // copy all the retrieved data into persistent state
                if (p.getProperty(DATABASE.CheckInDate.name()) == null) {
                    return true;
                }
            }

        }
        return false;
    }

    private boolean checkBarcode(String barcode){
        String query = "SELECT * FROM Book WHERE (" + Book.DATABASE.Barcode.name() +" = " + barcode + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null && allDataRetrieved.size() == 1){
            Properties prop = allDataRetrieved.get(0);
            System.out.println(allDataRetrieved.get(0));
            return true;
        }
        return false;
    }

    private boolean checkStudentId(String bannerId){
        String query = "SELECT * FROM StudentBorrower WHERE (" + StudentBorrower.DATABASE.BannerId.name()
                +" = " + bannerId + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
        if (allDataRetrieved != null && allDataRetrieved.size() == 1){
            Properties prop = allDataRetrieved.get(0);

            return true;
        }
        return false;
    }

    private void processCheckIn(String barcode){
        if(checkBarcode(barcode)) {
            String query = "SELECT * FROM " + myTableName + " WHERE (" + DATABASE.Barcode.name() + " = " + barcode + ")";
            Vector<Properties> allDataRetrieved = getSelectQueryResult(query);
            if (allDataRetrieved != null) {
                boolean exists = false;

                for (Properties p : allDataRetrieved) {
                    // copy all the retrieved data into persistent state
                    if (p.getProperty(DATABASE.CheckInDate.name()) == null) {
                        processCheckInHelper(p);
                        updateRental();
                        exists = true;
                        break;
                    }
                }

                if (!exists) {
                    successFlag = false;
                    updateStatusMessage = "Book has not been checked out";
                }
            }
        }
        else {
            successFlag = false;
            updateStatusMessage = "Invalid Barcode";
        }
    }

    private void updateRental() {

        persistentState.setProperty(DATABASE.CheckInDate.name(), getCurrentDate());
        persistentState.setProperty(DATABASE.CheckInWorkerId.name(), (String) myWorkerHolder.getState(Worker.DATABASE.BannerId.name()));

        try {
            Properties whereClause = new Properties();
            whereClause.setProperty(DATABASE.Id.name(),
                    persistentState.getProperty(DATABASE.Id.name()));
            updatePersistentState(mySchema, persistentState, whereClause);
            successFlag = true;
            updateStatusMessage = "Rental data for Barcode : " + persistentState.getProperty(DATABASE.Barcode.name()) + " updated successfully in database!";
//            System.out.println("Fuck boi shit");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void deepCopy(Properties oldProps, Properties newProps){
        Enumeration allKeys = oldProps.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = oldProps.getProperty(nextKey);

            if (nextValue != null)
            {
                newProps.setProperty(nextKey, nextValue);
            }
        }
    }
    private void processCheckInHelper(Properties props) {
        persistentState = new Properties();
       deepCopy(props, persistentState);
    }

    private String getCurrentDate(){
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.now().format(formatter);

    }
    private String getFutureDate(int num){
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.DATE, num);
        Date future = c.getTime();
        return formatter.format(future);
    }
    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
