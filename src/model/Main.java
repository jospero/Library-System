package model;

import event.Event;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

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
    private WorkerHolder myWorkerHolder;

    public Main(WorkerHolder workerHolder){
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, View>();
        myWorkerHolder = workerHolder;

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
        dependencies.setProperty("Add", "ChangeView");
        dependencies.setProperty("Modify", "ChangeView");
        dependencies.setProperty("CheckOut", "ChangeView");
        dependencies.setProperty("Delete", "ChangeView");
        dependencies.setProperty("Welcome", "ChangeView");
        dependencies.setProperty("ViewCancelled", "ChangeView");
        dependencies.setProperty("SubViewChange", "ChangeView");
        dependencies.setProperty("ParentView", "ChangeView");
        dependencies.setProperty("CheckIn", "ChangeView");
        myRegistry.setDependencies(dependencies);
    }
    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if(key.equals("ChangeView")){
            return currentView;
        } else if(key.equals("WorkerHolder")){
            return myWorkerHolder;
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
        if(key.equals("Add")){
            EntityBase model = null;
            String viewName = "";
            if(value.equals("Book")){
                model = new Book(new Properties());
                viewName = "AddBookView";
            } else if(value.equals("Worker")){
                model = new Worker(new Properties());
                viewName = "AddWorkerView";
            } else if(value.equals("StudentBorrower")){
                model = new StudentBorrower(new Properties());
                viewName = "AddStudentBorrowerView";
            }
            if(model != null && myViews.get(viewName) == null){
                myViews.clear();
                model.subscribe("ViewCancelled", this);
                currentView = ViewFactory.createView(viewName, model);
                myViews.put(viewName, currentView);
            }
        } else if (key.equals("Modify") || key.equals("Delete")) {
            SearchFor search;
            if (key.equals("CheckOut")) {
                search = SearchFor.CheckOut;
            }
            if(key.equals("Modify"))
                search = SearchFor.MODIFY;
            else
                search = SearchFor.DELETE;
            IModel searchModel = null;
            String viewName = "";
            if(value.equals("Book")){
                searchModel = new SearchBook(search);
                viewName = "SearchBookView";
            } else if(value.equals("Worker")){
                searchModel = new SearchWorker(search);
                viewName = "SearchWorkerView";
            } else if(value.equals("StudentBorrower")){
                searchModel = new SearchStudentBorrower(search);
                viewName = "SearchStudentBorrowerView";
            }
            if(searchModel != null && myViews.get(viewName) == null) {
                myViews.clear();
                searchModel.subscribe("SubViewChange", this);
                searchModel.subscribe("ParentView", this);
                searchModel.subscribe("ViewCancelled", this);
                currentView = ViewFactory.createView(viewName, searchModel);
                myViews.put(viewName, currentView);
            }
        } else if (key.equals("Welcome") || key.equals("ViewCancelled")) {
            myViews.clear();
            currentView = ViewFactory.createView("WelcomeView", myWorkerHolder);
            myViews.put("WelcomeView", currentView);
        } else if (key.equals("SubViewChange")){
            currentView = (View) value;
        } else if(key.equals("ParentView")){
            currentView = myViews.get(value);
        } else if (key.equals("CheckOut")) {
            myViews.clear();
            Book book = new Book(new Properties());
            book.subscribe("ViewCancelled", this);
            currentView = ViewFactory.createView("CheckOutBookView", book);
            myViews.put("CheckOutBookView", currentView);
        }else if(key.equals("CheckIn")) {
//            myViews.clear();
//            currentView = ViewFactory.createView("CheckInBookView", myWorkerHolder);
//            myViews.put("CheckInBookView", currentView);
//        }
//

            myViews.clear();
            Rental rental = new Rental(myWorkerHolder);
            rental.subscribe("ViewCancelled", this);
            currentView = ViewFactory.createView("CheckInBookView", rental);
            myViews.put("CheckInBookView", currentView);

       }
        myRegistry.updateSubscribers(key, this);

    }

    public void createAndShowView() {
        if(mainScene == null) {
            currentView = ViewFactory.createView("WelcomeView", myWorkerHolder);
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


//    public static Group getMainViewContainer() {
//
//
//    }
}
