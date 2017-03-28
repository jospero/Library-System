package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

public class StudentBorrower extends EntityBase implements IView {

	public enum DATABASE{
		BannerId, FirstName, LastName, Phone, Email, BorrowerStatus, DateOfLastBorrowerStatus, DateOfRegistration,
		Notes, Status
	}

	private static final String myTableName = "StudentBorrower";


	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	private boolean successFlag = true;

	public StudentBorrower(String bannerId) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (bannerId = " + bannerId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one account at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one studentBorrower. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple studentBorrowers matching id : "
					+ bannerId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedAccountData = allDataRetrieved.elementAt(0);
				processNewStudentBorrowerHelper(retrievedAccountData);


			}
		}
		// If no account found for this user name, throw an exception
		else
		{
//		    studentBorrowerErrorMessage="No studentBorrower matching id : " + bannerId + " found.";
			throw new InvalidPrimaryKeyException("No studentBorrower matching id : "
				+ bannerId + " found.");
		}
	}

	public static HashMap<DATABASE, String> getFields(){
		HashMap<DATABASE, String> fieldsStr = new HashMap<>();
		fieldsStr.put(DATABASE.BannerId, "Banner ID");
		fieldsStr.put(DATABASE.FirstName, "Firstname");
		fieldsStr.put(DATABASE.LastName, "Lastname");
		fieldsStr.put(DATABASE.Phone, "Phone");
		fieldsStr.put(DATABASE.Email, "Email");
		fieldsStr.put(DATABASE.BorrowerStatus, "Borrower Status");
		fieldsStr.put(DATABASE.DateOfLastBorrowerStatus, "Date of Last Borrower Status");
		fieldsStr.put(DATABASE.DateOfRegistration, "Date of Registration");
		fieldsStr.put(DATABASE.Notes, "Notes");
		fieldsStr.put(DATABASE.Status, "Status");
		return fieldsStr;
	}

	// Can also be used to create a NEW Account (if the system it is part of
	// allows for a new account to be set up)
	//----------------------------------------------------------
	public StudentBorrower(Properties props)
	{
		super(myTableName);

		setDependencies();
		processNewStudentBorrowerHelper(props);
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
		if(key.equals("ProcessNewStudentBorrower")){
		    processNewStudentBorrower((Properties) value);
        }
	    myRegistry.updateSubscribers(key, this);
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("AddStudentBorrowerCancelled","ViewCancelled");
		dependencies.setProperty("ModifyStudentBorrowerCancelled","ViewCancelled");
        dependencies.setProperty("ProcessNewStudentBorrower","UpdateStatusMessage");
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
			if (persistentState.getProperty("bannerId") != null)
			{
				Properties whereClause = new Properties();
				whereClause.setProperty("bannerId",
				persistentState.getProperty("bannerId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "StudentBorrower data for studentBorrower id : " + persistentState.getProperty("bannerId") + " updated successfully in database!";
			}
			else
			{
				Integer bannerId =
					insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("bannerId", "" + bannerId.intValue());
				updateStatusMessage = "StudentBorrower data for new studentBorrower : " +  persistentState.getProperty("bannerId")
					+ " installed successfully in database!";
			}
		}
		catch (SQLException ex)
		{
		    successFlag = false;
			updateStatusMessage = "Error in installing studentBorrower data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
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
//		v.addElement(persistentState.getProperty("BannerId"));
//		v.addElement(persistentState.getProperty("FirstName"));
//		v.addElement(persistentState.getProperty("LastName"));
//		v.addElement(persistentState.getProperty("Phone"));
//		v.addElement(persistentState.getProperty("E-mail"));
//		v.addElement(persistentState.getProperty("BorrowerStatus"));
//		v.addElement(persistentState.getProperty("DateOfLastBorrowerStatus"));
//		v.addElement(persistentState.getProperty("DateOfRegistration"));
//		v.addElement(persistentState.getProperty("Notes"));
//		v.addElement(persistentState.getProperty("Status"));

		return v;
	}

	public void processNewStudentBorrower(Properties props){
        processNewStudentBorrowerHelper(props);
        updateStateInDatabase();
    }

    private void processNewStudentBorrowerHelper(Properties props){
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
            	System.out.println(nextKey + "  " + nextValue);
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
//		Scene currentScene = myViews.get("NewStudentBorrowerView");
//
//		if (currentScene == null)
//		{
//			// create our initial view
//			View newView = ViewFactory.createView("NewStudentBorrowerView", this);
//			currentScene = new Scene(newView);
//			myViews.put("NewStudentBorrowerView", currentScene);
//		}
//		swapToView(currentScene);
	}

	@Override
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}
}
