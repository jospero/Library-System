package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;

import javax.swing.text.DateFormatter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

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
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
        }
        myRegistry.updateSubscribers(key, this);
    }

    private void processCheckIn(String barcode) throws InvalidPrimaryKeyException {
        String query = "SELECT * FROM " + myTableName + " WHERE (" + DATABASE.Barcode.name() +" = " + barcode + ")";
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null){

            for(Properties p : allDataRetrieved){
                // copy all the retrieved data into persistent state
                if(p.getProperty(DATABASE.CheckInDate.name()) == null){
                    processCheckInHelper(p);
                    updateRental();
                    break;
                }
            }

        } else {
            throw new InvalidPrimaryKeyException("Barcode does not exist");
        }
    }

    private void updateRental() {
        DateTimeFormatter formatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        persistentState.setProperty(DATABASE.CheckInDate.name(), LocalDate.now().format(formatter));
        persistentState.setProperty(DATABASE.CheckInWorkerId.name(), (String) myWorkerHolder.getState(Worker.DATABASE.BannerId.name()));

        try {
            successFlag = true;
            Properties whereClause = new Properties();
            whereClause.setProperty(DATABASE.Barcode.name(),
                    persistentState.getProperty(DATABASE.Barcode.name()));
            updatePersistentState(mySchema, persistentState, whereClause);
            updateStatusMessage = "Rental data for Barcode : " + persistentState.getProperty(DATABASE.Barcode.name()) + " updated successfully in database!";
            System.out.println("Fuck boi shit");
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

    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}
