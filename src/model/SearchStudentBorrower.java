package model;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchStudentBorrower implements IView, IModel {

    private final SearchFor searchFor;
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
    private StudentBorrowerCollection studentBorrowerCollection;
    private int selectedStudentBorrower = -1;
    public SearchStudentBorrower(SearchFor searchFor) {
        this.searchFor = searchFor;
        nextView = new ArrayList<>();
        myRegistry = new ModelRegistry("SearchStudentBorrower");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "SearchStudentBorrower",
                    "Could not instantiate Registry", Event.ERROR);
        }
        setDependencies();

    }

    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("ProcessSearch", "UpdateSearch");
        dependencies.setProperty("ViewStudentBorrowerCancelled", "ParentView");
        dependencies.setProperty("SearchCancelled", "ViewCancelled");
        dependencies.setProperty("View", "SubViewChange");
        myRegistry.setDependencies(dependencies);
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        System.out.println("SearchStudentBorrower" + key);
        if(key.equals("SubViewChange")){
            return nextView.get(nextView.size()-1);
        } if(key.equals("ParentView")){
            return "SearchStudentBorrowerView";
        } if(key.equals("StudentBorrowers") || key.equals("UpdateSearch")){
            if(studentBorrowerCollection != null)
                return studentBorrowerCollection.getState("StudentBorrowers");
        }
        return null;
    }

    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        System.out.println("SCR "+ key);
        if(key.equals("ProcessSearch")){
            studentBorrowerCollection = new StudentBorrowerCollection();
            try {
                studentBorrowerCollection.findBooksCriteria((Properties) value);
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
        } else if(key.equals("View")){
            if(searchFor == SearchFor.MODIFY){
                selectedStudentBorrower = (int) value;
                StudentBorrower studentBorrower = ((Vector<StudentBorrower>)studentBorrowerCollection.
                        getState("StudentBorrowers")).get(selectedStudentBorrower);
                studentBorrower.subscribe("ViewStudentBorrowerCancelled", this);
                nextView.add(ViewFactory.createView("ModifyStudentBorrowerView", studentBorrower));
            }
        } else if(key.equals("ViewStudentBorrowerCancelled")){
            StudentBorrower studentBorrower = ((Vector<StudentBorrower>)studentBorrowerCollection.
                    getState("StudentBorrowers")).get(selectedStudentBorrower);
            studentBorrower.unSubscribe("ViewStudentBorrowerCancelled", this);
            if(value != null && (boolean)value){
                myRegistry.updateSubscribers("UpdateSearch", this);
            }
            nextView.remove(nextView.size()-1);
        }
        myRegistry.updateSubscribers(key, this);

    }
}
