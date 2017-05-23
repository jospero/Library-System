package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import userinterface.ViewFactory;
import userinterface.book.AddBookView;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

/**
 * Created by Sammytech on 5/9/17.
 */
public class RentalCollection extends EntityBase implements IView {

    private static final String myTableName = "Rental";
    private static final Logger LOGGER = Logger.getLogger( AddBookView.class.getName() );
    private Vector<Properties> rentals;
    protected Properties dependencies;

    protected RentalCollection() {
        super(myTableName);
        rentals = new Vector<>();
        setDependencies();
    }
    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("ProcessRental", "UpdateRental");
        dependencies.setProperty("CancelBookList","ResultViewCancelled");
        myRegistry.setDependencies(dependencies);
    }

    private void retrieveHelper(String query) throws InvalidPrimaryKeyException {
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            rentals = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);

                rentals.add(duplicateRentals(nextPatronData));
            }

        }
        else
        {
            rentals = new Vector<>();
        }
    }

    public void getRentals() throws InvalidPrimaryKeyException {
        String query =  "SELECT Book.Barcode, Book.Title, Rental.CheckOutDate, Rental.DueDate, Worker.BannerId AS `BanBanefef`, Worker.FirstName, Worker.LastName, StudentBorrower.BannerId as `BanBan`, StudentBorrower.LastName, StudentBorrower.FirstName " +
                        "FROM " + myTableName + ", Book, StudentBorrower, Worker " +
                        " WHERE (Rental.Barcode = Book.Barcode) AND (Worker.BannerId = Rental.CheckOutWorkerId)" +
                        " AND (StudentBorrower.BannerId = Rental.BorrowerId) AND (Rental.CheckInDate IS NULL);";
        retrieveHelper(query);
        LOGGER.info(String.valueOf(rentals));
    }

    private Properties duplicateRentals(Properties props){
        Properties temp = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                temp.setProperty(nextKey, nextValue);
            }
        }
        return temp;
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
       if(key.equals("Rentals") || key.equals("UpdateRental")){
           return rentals;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if(key.equals("ProcessRental")){
            try {
                getRentals();
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
        }
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {

    }

    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }
}
