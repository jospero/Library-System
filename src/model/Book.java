package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

public class Book extends EntityBase implements IView {

	public enum DATABASE{
		Barcode, Title, Authors, Discipline, Publisher, YearOfPublication, ISBN, Condition, SuggestedPrice, Notes, Status
	}

	private static final String myTableName = "Book";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	private boolean successFlag = true;

	public Book(String Barcode) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (Barcode = " + Barcode + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one book. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple books matching id : "
					+ Barcode + " found.");
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
//		    bookErrorMessage="No book matching id : " + Barcode + " found.";
			throw new InvalidPrimaryKeyException("No book matching id : "
				+ Barcode + " found.");
		}
	}

	public static HashMap<DATABASE, String> getFields(){
		HashMap<DATABASE, String> fieldsStr = new HashMap<>();
		fieldsStr.put(DATABASE.Barcode, DATABASE.Barcode.name());
		fieldsStr.put(DATABASE.Title, "Title");
		fieldsStr.put(DATABASE.Authors, "Author(s)");
		fieldsStr.put(DATABASE.Discipline, "Discipline");
		fieldsStr.put(DATABASE.Publisher, "Publisher");
		fieldsStr.put(DATABASE.YearOfPublication, "Year of Publication");
		fieldsStr.put(DATABASE.ISBN, "ISBN");
		fieldsStr.put(DATABASE.Condition, "Condition");
		fieldsStr.put(DATABASE.SuggestedPrice, "Suggested Price");
		fieldsStr.put(DATABASE.Notes, "Notes");
		fieldsStr.put(DATABASE.Status, "Status");
		return fieldsStr;
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
        } else if(key.equals("ProcessModifyBook")){
			processModifyBook((Properties) value);
		}
	    myRegistry.updateSubscribers(key, this);
		System.out.println("Created Book");
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("AddBookCancelled","ViewCancelled");
		dependencies.setProperty("ModifyBookCancelled","ViewCancelled");
        dependencies.setProperty("ProcessNewBook","UpdateStatusMessage");
        dependencies.setProperty("ProcessModifyBook","UpdateStatusMessage");
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
			if (persistentState.getProperty(DATABASE.Barcode.name()) != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty(DATABASE.Barcode.name(),
				persistentState.getProperty(DATABASE.Barcode.name()));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Book data for book id : " + persistentState.getProperty(DATABASE.Barcode.name()) + " updated successfully in database!";
			}
			else
			{
				Integer Barcode =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty(DATABASE.Barcode.name(), "" + Barcode.intValue());
				updateStatusMessage = "Book data for new book : " +  persistentState.getProperty(DATABASE.Barcode.name())
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
			successFlag = true;
			insertPersistentState(mySchema, persistentState);
			updateStatusMessage = "Book added to Database";
			System.out.println("Created Book");
		} catch (SQLException e) {
			successFlag = false;
			updateStatusMessage = e.getMessage();
		}

	}

	private void modifyBook(){
		try {
			successFlag = true;
			Properties whereClause = new Properties();
			whereClause.setProperty(DATABASE.Barcode.name(),
					persistentState.getProperty(DATABASE.Barcode.name()));
			updatePersistentState(mySchema, persistentState, whereClause);
			updateStatusMessage = "Book data for Barcode : " + persistentState.getProperty(DATABASE.Barcode.name()) + " updated successfully in database!";
		} catch (SQLException e) {
			e.printStackTrace();
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
		for(DATABASE d : DATABASE.values()){
			v.addElement(persistentState.getProperty(d.name()));
		}
//		v.addElement(persistentState.getProperty(DATABASE.Barcode.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Title.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Discipline.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Authors.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Publisher.name()));
//		v.addElement(persistentState.getProperty(DATABASE.YearOfPublication.name()));
//		v.addElement(persistentState.getProperty(DATABASE.ISBN.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Condition.name()));
//		v.addElement(persistentState.getProperty(DATABASE.SuggestedPrice.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Notes.name()));
//		v.addElement(persistentState.getProperty(DATABASE.Status.name());

		return v;
	}

	public void processNewBook(Properties props){
        processNewBookHelper(props);
        createNewBook();
    }

	public void processModifyBook(Properties props){
		processNewBookHelper(props);
		modifyBook();
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
