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
public class SearchWorker implements IView, IModel {

    private final SearchFor searchFor;
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
    public SearchWorker(SearchFor searchFor) {
        this.searchFor = searchFor;
        nextView = new ArrayList<>();
        myRegistry = new ModelRegistry("SearchWorker");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "SearchWorker",
                    "Could not instantiate Registry", Event.ERROR);
        }
        setDependencies();

    }

    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("ProcessSearch", "SubViewChange");
        dependencies.setProperty("ViewWorkerCancelled", "SubViewChange");
        dependencies.setProperty("ResultViewCancelled", "ParentView");
        dependencies.setProperty("SearchWorkerCancelled", "ViewCancelled");
        dependencies.setProperty("ViewWorker", "SubViewChange");
        dependencies.setProperty("ShowParent", "ParentView");
        myRegistry.setDependencies(dependencies);
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        System.out.println("SearchWorker" + key);
        if(key.equals("SubViewChange")){
            return nextView.get(nextView.size()-1);
        } if(key.equals("ParentView")){
            return "SearchWorkerView";
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
            WorkerCollection workerCollection = new WorkerCollection();
            try {
                workerCollection.findBooksCriteria((Properties) value);
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
            workerCollection.subscribe("ResultViewCancelled", this);
            workerCollection.subscribe("ViewWorker", this);
            nextView.add(workerCollection.createView());
        } else if(key.equals("ViewWorker")){
            if(searchFor == SearchFor.MODIFY){
                System.out.println(searchFor);
                Worker worker = (Worker) value;
                worker.subscribe("ViewWorkerCancelled", this);
                worker.subscribe("ShowParent", this);
                nextView.add(ViewFactory.createView("ModifyWorkerView", worker));
            }
        } else if(key.equals("ViewWorkerCancelled")){
            nextView.remove(nextView.size()-1);
        }
        myRegistry.updateSubscribers(key, this);

    }
}
