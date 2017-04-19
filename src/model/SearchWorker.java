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
public class SearchWorker implements IView, IModel {

    private final SearchFor searchFor;
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
    private WorkerCollection workerCollection;
    private int selectedWorker = -1;
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
        dependencies.setProperty("ProcessSearch", "UpdateSearch");
        dependencies.setProperty("DetailViewCancelled", "ParentView");
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
        System.out.println("SearchWorker" + key);
        if(key.equals("SubViewChange")){
            return nextView.get(nextView.size()-1);
        } if(key.equals("ParentView")){
            return "SearchWorkerView";
        }if(key.equals("Workers") || key.equals("UpdateSearch")){
            if(workerCollection != null)
                return workerCollection.getState("Workers");
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
            workerCollection = new WorkerCollection();
            try {
                workerCollection.findBooksCriteria((Properties) value);
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }

        } else if(key.equals("View")){
            if(searchFor == SearchFor.MODIFY){
                selectedWorker = (int) value;
                Worker worker = ((Vector<Worker>)workerCollection.getState("Workers")).get(selectedWorker);
                worker.subscribe("DetailViewCancelled", this);
                nextView.add(ViewFactory.createView("ModifyWorkerView", worker));
            }
        } else if(key.equals("DetailViewCancelled")){
            Worker worker = ((Vector<Worker>)workerCollection.getState("Workers")).get(selectedWorker);
            worker.unSubscribe("DetailViewCancelled", this);
            if(value != null && (boolean)value){
                myRegistry.updateSubscribers("UpdateSearch", this);
            }
            nextView.remove(nextView.size()-1);
        }
        myRegistry.updateSubscribers(key, this);

    }
}
