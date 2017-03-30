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

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchStudentBorrower implements IView, IModel {

    private final SearchFor searchFor;
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
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
        dependencies.setProperty("ProcessSearch", "SubViewChange");
        dependencies.setProperty("ViewStudentBorrowerCancelled", "SubViewChange");
        dependencies.setProperty("ResultViewCancelled", "ParentView");
        dependencies.setProperty("SearchStudentBorrowerCancelled", "ViewCancelled");
        dependencies.setProperty("ViewStudentBorrower", "SubViewChange");
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
            StudentBorrowerCollection studentBorrowerCollection = new StudentBorrowerCollection();
            try {
                studentBorrowerCollection.findBooksCriteria((Properties) value);
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
            studentBorrowerCollection.subscribe("ResultViewCancelled", this);
            studentBorrowerCollection.subscribe("ViewStudentBorrower", this);
            nextView.add(studentBorrowerCollection.createView());
        } else if(key.equals("ViewStudentBorrower")){
            if(searchFor == SearchFor.MODIFY){
                StudentBorrower studentBorrower = (StudentBorrower) value;
                studentBorrower.subscribe("ViewStudentBorrowerCancelled", this);
                nextView.add(ViewFactory.createView("ModifyStudentBorrowerView", studentBorrower));
            }
        } else if(key.equals("ViewStudentBorrowerCancelled")){
            nextView.remove(nextView.size()-1);
        }
        myRegistry.updateSubscribers(key, this);

    }
}
