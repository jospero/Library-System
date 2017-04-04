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
public class WorkerCollection extends EntityBase implements IView {

    private static final String myTableName = "Worker";

    private Vector<Worker> workers;
    protected Properties dependencies;
    protected View nextView;
    protected int selectedWorker = -1;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public WorkerCollection()
    {
        super(myTableName);

        workers = new Vector<Worker>();

        setDependencies();
    }

    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelWorkerList","ResultViewCancelled");
        myRegistry.setDependencies(dependencies);
    }

    private void retrieveHelper(String query) throws InvalidPrimaryKeyException {
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            workers = new Vector<Worker>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);

                Worker worker = new Worker(nextPatronData);
                if (worker != null)
                {
                    workers.add(worker);
                }
            }

        }
        else
        {
            workers = new Vector<Worker>();
        }
    }


    public void findWorkers() throws InvalidPrimaryKeyException{
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
        if (key.equals("Workers"))
            return workers;
        else if (key.equals("WorkerList"))
            return this;
        else if(key.equals("ViewWorker")) {
            if (selectedWorker >= 0) {
                return workers.get(selectedWorker);
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
        if(key.equals("ViewWorker")){
            selectedWorker = (int) value;
        }
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Worker retrieve(String workerId)
    {
        Worker retValue = null;
        for (int cnt = 0; cnt < workers.size(); cnt++)
        {
            Worker nextAcct = workers.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("workerId");
            if (nextAccNum.equals(workerId) == true)
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
//        Scene currentScene = myViews.get("WorkerCollectionView");

//        if (currentScene == null)
//        {
//            // create our initial view
//            View newView = ViewFactory.createView("WorkerCollectionView", this);
//            currentScene = new Scene(newView);
//            myViews.put("WorkerCollectionView", currentScene);
//        }
//        swapToView(currentScene);
    }

    protected View createView(){
        View currentView = myViews.get("WorkerCollectionView");

        if (currentView == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("WorkerCollectionView", this);
            currentView = newView;
            myViews.put("WorkerCollectionView", currentView);
        }
        return currentView;
    }


    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}

