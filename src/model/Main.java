package model;

import event.Event;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.*;

import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by Sammytech on 3/5/17.
 */
public class Main implements IView, IModel {

    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected Scene mainScene;

    protected Stage myStage;
//    protected Hashtable<String, Scene> myViews;
    private Worker myWorker;

    public Main(Worker worker){
        myStage = MainStageContainer.getInstance();
//        myViews = new Hashtable<String, Scene>();
        myWorker = worker;

        myRegistry = new ModelRegistry("Main");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Main",
                    "Could not instantiate Registry", Event.ERROR);
        }
        setDependencies();
    }
    protected void setDependencies()
    {
        dependencies = new Properties();
        myRegistry.setDependencies(dependencies);
    }
    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        return null;
    }

    @Override
    public void subscribe(String key, IView subscriber) {

    }

    @Override
    public void unSubscribe(String key, IView subscriber) {

    }

    @Override
    public void stateChangeRequest(String key, Object value) {

    }

    public void createAndShowView() {
        if(mainScene == null) {
            View mainView = ViewFactory.createView("MainView", this);
            mainScene = new Scene(mainView);
        }
        swapToView(mainScene);
    }

    public void swapToView(Scene newScene)
    {
        if (newScene == null)
        {
            System.out.println("Main.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();

        //Place in center
        WindowPosition.placeCenter(myStage);
    }



}
