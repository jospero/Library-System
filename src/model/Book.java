package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Book extends EntityBase implements IView {

	private static final String myTableName = "Book";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	private boolean successFlag = true;

	public Book(String bookId) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (bookId = " + bookId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one book. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple books matching id : "
					+ bookId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedAccountData = allDataRetrieved.elementAt(0);
				processNewBookHelper(retrievedAccountData);


			}
		}
		// If no account found for this user name, throw an exception
		else
		{
//		    bookErrorMessage="No book matching id : " + bookId + " found.";
			throw new InvalidPrimaryKeyException("No book matching id : "
				+ bookId + " found.");
		}
	}


	// Can also be used to create a NEW Account (if the system it is part of
	// allows for a new account to be set up)
	//----------------------------------------------------------
	public Book(Properties props)
	{
		super(myTableName);

		setDependencies();
		processNewBookHelper(props);
	}
	
	
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage"))
			return updateStatusMessage;
		else if(key.equals("SuccessFlag")){
		    return successFlag;
        }
		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		if(key.equals("ProcessNewBook")){
		    processNewBook((Properties) value);
        }
	    myRegistry.updateSubscribers(key, this);
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("AddBookCancelled","ViewCancelled");
		dependencies.setProperty("ModifyBookCancelled","ViewCancelled");
        dependencies.setProperty("ProcessNewBook","UpdateStatusMessage");
		myRegistry.setDependencies(dependencies);
	}

	
	public void update()
	{
		updateStateInDatabase();
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase() 
	{
		try
		{
            successFlag = true;
			if (persistentState.getProperty("bookId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("bookId",
				persistentState.getProperty("bookId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Book data for book id : " + persistentState.getProperty("bookId") + " updated successfully in database!";
			}
			else
			{
				Integer bookId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("bookId", "" + bookId.intValue());
				updateStatusMessage = "Book data for new book : " +  persistentState.getProperty("bookId")
					+ " installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
		    successFlag = false;
			updateStatusMessage = "Error in installing book data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	private void createNewBook(){
		try {
			insertPersistentState(mySchema, persistentState);
			updateStatusMessage = "Book added to Database";
		} catch (SQLException e) {
			successFlag = false;
			updateStatusMessage = e.getMessage();
		}

	}

	/**
	 * This method is needed solely to enable the Account information to be displayable in a table
	 *
	 */
	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();
		v.addElement(persistentState.getProperty("Barcode"));
		v.addElement(persistentState.getProperty("Title"));
		v.addElement(persistentState.getProperty("Discipline"));
		v.addElement(persistentState.getProperty("Authors"));
		v.addElement(persistentState.getProperty("Publisher"));
		v.addElement(persistentState.getProperty("YearOfPublication"));
		v.addElement(persistentState.getProperty("ISBN"));
		v.addElement(persistentState.getProperty("Condition"));
		v.addElement(persistentState.getProperty("SuggestedPrice"));
		v.addElement(persistentState.getProperty("Notes"));
		v.addElement(persistentState.getProperty("Status"));

		return v;
	}

	public void processNewBook(Properties props){
        processNewBookHelper(props);
        createNewBook();
    }

    private void processNewBookHelper(Properties props){
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }
	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	protected void createAndShowView()
	{
//		Scene currentScene = myViews.get("NewBookView");
//
//		if (currentScene == null)
//		{
//			// create our initial view
//			View newView = ViewFactory.createView("NewBookView", this);
//			currentScene = new Scene(newView);
//			myViews.put("NewBookView", currentScene);
//		}
//		swapToView(currentScene);
	}

	@Override
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}
}
