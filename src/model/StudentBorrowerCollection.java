package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class StudentBorrowerCollection extends EntityBase implements IView {

    private static final String myTableName = "StudentBorrower";

    private Vector<StudentBorrower> studentBorrowers;
    protected Properties dependencies;
    protected View nextView;
    protected int selectedStudentBorrower = -1;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public StudentBorrowerCollection()
    {
        super(myTableName);

        studentBorrowers = new Vector<StudentBorrower>();

        setDependencies();
    }

    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelStudentBorrowerList","ResultViewCancelled");
        myRegistry.setDependencies(dependencies);
    }

    private void retrieveHelper(String query) throws InvalidPrimaryKeyException {
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            studentBorrowers = new Vector<StudentBorrower>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);

                StudentBorrower studentBorrower = new StudentBorrower(nextPatronData);
                if (studentBorrower != null)
                {
                    studentBorrowers.add(studentBorrower);
                }
            }

        }
        else
        {
            studentBorrowers = new Vector<StudentBorrower>();
        }
    }


    public void findStudentBorrowers() throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName;
        retrieveHelper(query);

    }

    public void findBooksCriteria(Properties props) throws InvalidPrimaryKeyException {
        String query = "SELECT * FROM " + myTableName + " WHERE (";
        Enumeration theWhereFields = props.propertyNames();
        while (theWhereFields.hasMoreElements()){
            String theFieldName = (String)theWhereFields.nextElement();
            String theFieldValue = props.getProperty(theFieldName).replace("'", "\'");
            query += "`"+ theFieldName + "` = '" + theFieldValue.trim() + "' AND ";
        }
        query = query.substring(0, query.lastIndexOf("AND")) + ")";
        System.out.println(query);
        retrieveHelper(query);
    }



    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("StudentBorrowers"))
            return studentBorrowers;
        else if (key.equals("StudentBorrowerList"))
            return this;
        else if(key.equals("ViewStudentBorrower")) {
            if (selectedStudentBorrower >= 0) {
                return studentBorrowers.get(selectedStudentBorrower);
            } else {
                return null;
            }
        }
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        System.out.println(key);
        if(key.equals("ViewStudentBorrower")){
            selectedStudentBorrower = (int) value;
        }
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public StudentBorrower retrieve(String studentBorrowerId)
    {
        StudentBorrower retValue = null;
        for (int cnt = 0; cnt < studentBorrowers.size(); cnt++)
        {
            StudentBorrower nextAcct = studentBorrowers.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("studentBorrowerId");
            if (nextAccNum.equals(studentBorrowerId) == true)
            {
                retValue = nextAcct;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
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
//        Scene currentScene = myViews.get("StudentBorrowerCollectionView");

//        if (currentScene == null)
//        {
//            // create our initial view
//            View newView = ViewFactory.createView("StudentBorrowerCollectionView", this);
//            currentScene = new Scene(newView);
//            myViews.put("StudentBorrowerCollectionView", currentScene);
//        }
//        swapToView(currentScene);
    }

    protected View createView(){
        View currentView = myViews.get("StudentBorrowerCollectionView");

        if (currentView == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("StudentBorrowerCollectionView", this);
            currentView = newView;
            myViews.put("StudentBorrowerCollectionView", currentView);
        }
        return currentView;
    }


    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}

