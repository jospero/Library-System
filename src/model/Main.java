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
    protected Hashtable<String, View> myViews;
    private View currentView;
    private Worker myWorker;

    public Main(Worker worker){
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, View>();
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
        dependencies.setProperty("AddBook", "ChangeView");
        dependencies.setProperty("ModifyBook", "ChangeView");
        dependencies.setProperty("AddStudentBorrower", "ChangeView");
        dependencies.setProperty("Welcome", "ChangeView");
        dependencies.setProperty("ViewCancelled", "ChangeView");

        myRegistry.setDependencies(dependencies);
    }
    @Override
    public void updateState(String key, Object value) {

        System.out.println("key");
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if(key.equals("ChangeView")){
            return currentView;
        } else if(key.equals("Worker")){
            return myWorker;
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
        if(key.equals("AddBook")){
            Book book = new Book(new Properties());
            book.subscribe("ViewCancelled", this);
            //            myViews.put("AddBookView", addBookView);
            currentView = ViewFactory.createView("AddBookView", book);
//            myRegistry.updateSubscribers("ChangeView", this);
        } else if (key.equals("ModifyWorker")) {
            currentView = ViewFactory.createView("ModifyBookView", null);
//            myRegistry.updateSubscribers("ChangeView");
        } else if (key.equals("AddStudentBorrower")){
            StudentBorrower studentBorrower = new StudentBorrower( new Properties());
            studentBorrower.subscribe("ViewCancelled", this);
            currentView  = ViewFactory.createView("AddStudentBorrowerView", studentBorrower);
//            myRegistry.updateSubscribers("ChangeView", this);
        } else if (key.equals("Welcome") || key.equals("ViewCancelled")) {
            currentView = ViewFactory.createView("WelcomeView", myWorker);
//            myRegistry.updateSubscribers("ChangeView", this);
        }

        myRegistry.updateSubscribers(key, this);

    }

    public void createAndShowView() {
        if(mainScene == null) {
            currentView = ViewFactory.createView("WelcomeView", myWorker);
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

        myStage.setTitle("Brockport EOP Libary");
        myStage.setScene(newScene);
        myStage.sizeToScene();

        //Place in center
        WindowPosition.placeCenter(myStage);
    }



}
