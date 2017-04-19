package model;

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

    }


    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if(key.equals("ProcessCheckIn")){
            String barcode = ((Properties) value).getProperty(DATABASE.Barcode.name());
            try {
                processCheckIn(barcode);
            } catch (BookCheckInException e) {
                e.printStackTrace();
            }
        } else if(key.equals("ProcessCheckOut")){
            String barcode = ((Properties) value).getProperty(DATABASE.Barcode.name());
            String bannerId = ((Properties) value).getProperty(DATABASE.BorrowerId.name());
            processCheckOut(barcode, bannerId);
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void processCheckOut(String barcode, String bannerId) {
        if(checkBarcode(barcode) && checkStudentId(bannerId)){
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
                    System.out.println("Fuck no");
                }

            } else {
                System.out.println("Fuck no");
            }
        } else {
            System.out.println("Fuck no");
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

    private void processCheckIn(String barcode) throws BookCheckInException {
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
                    throw new BookCheckInException("Book has not been checked out");
                }
            } else {
                throw new BookCheckInException("Book has not been checked out");
            }
        }
    }

    private void updateRental() {

        persistentState.setProperty(DATABASE.CheckInDate.name(), getCurrentDate());
        persistentState.setProperty(DATABASE.CheckInWorkerId.name(), (String) myWorkerHolder.getState(Worker.DATABASE.BannerId.name()));

        try {
            successFlag = true;
            Properties whereClause = new Properties();
            whereClause.setProperty(DATABASE.Barcode.name(),
                    persistentState.getProperty(DATABASE.Barcode.name()));
            updatePersistentState(mySchema, persistentState, whereClause);
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
