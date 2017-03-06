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

public class StudentBorrower extends EntityBase implements IView {

	private static final String myTableName = "StudentBorrower";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	private boolean successFlag = true;

	public StudentBorrower(String patronId) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (patronId = " + patronId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one patron. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple books matching id : "
					+ patronId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedAccountData = allDataRetrieved.elementAt(0);
				processNewPatronHelper(retrievedAccountData);

			}
		}
		// If no account found for this user name, throw an exception
		else
		{
			throw new InvalidPrimaryKeyException("No patron matching id : "
				+ patronId + " found.");
		}
	}


	// Can also be used to create a NEW Account (if the system it is part of
	// allows for a new account to be set up)
	//----------------------------------------------------------
	public StudentBorrower(Properties props)
	{
		super(myTableName);

		setDependencies();
		processNewPatronHelper(props);
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
		if(key.equals("ProcessNewPatron")){
			processNewPatron((Properties) value);
		}
		myRegistry.updateSubscribers(key, this);
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("NewPatronCancelled", "ViewCancelled");
		dependencies.setProperty("ProcessNewPatron","UpdateStatusMessage");
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
			if (persistentState.getProperty("patronId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("patronId",
				persistentState.getProperty("patronId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Patron data for patron id : " + persistentState.getProperty("patronId") + " updated successfully in database!";
			}
			else
			{
				Integer patronId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("patronId", "" + patronId.intValue());
				updateStatusMessage = "Patron data for new patron : " +  persistentState.getProperty("patronId")
					+ "installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
			successFlag = false;
			updateStatusMessage = "Error in installing patron data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();
		v.addElement(persistentState.getProperty("patronId"));
		v.addElement(persistentState.getProperty("name"));
		v.addElement(persistentState.getProperty("address"));
		v.addElement(persistentState.getProperty("city"));
		v.addElement(persistentState.getProperty("stateCode"));
		v.addElement(persistentState.getProperty("zip"));
		v.addElement(persistentState.getProperty("email"));
		v.addElement(persistentState.getProperty("dateOfBirth"));
		v.addElement(persistentState.getProperty("status"));

		return v;
	}

	private void processNewPatron(Properties props){
		processNewPatronHelper(props);
		updateStateInDatabase();
	}

	private void processNewPatronHelper(Properties props){
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


	protected void createAndShowView()
	{
		Scene currentScene = myViews.get("NewBookView");

		if (currentScene == null)
		{
			// create our initial view
			View newView = ViewFactory.createView("NewPatronView", this);
			currentScene = new Scene(newView);
			myViews.put("NewPatronView", currentScene);
		}
		swapToView(currentScene);
	}

	@Override
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}
}
