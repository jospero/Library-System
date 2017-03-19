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
        dependencies.setProperty("AddBook", "ChangeView");
        dependencies.setProperty("AddWorker", "ChangeView");
        dependencies.setProperty("ModifyBook", "ChangeView");
        dependencies.setProperty("DeleteBook", "ChangeView");
        dependencies.setProperty("AddStudentBorrower", "ChangeView");
        dependencies.setProperty("Welcome", "ChangeView");
        dependencies.setProperty("ViewCancelled", "ChangeView");
        dependencies.setProperty("SubViewChange", "ChangeView");
        dependencies.setProperty("ParentView", "ChangeView");
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
        if(key.equals("AddBook")){
            Book book = new Book(new Properties());
            book.subscribe("ViewCancelled", this);
            currentView = ViewFactory.createView("AddBookView", book);
//            myViews.put("AddBookView", currentView);
        } else if (key.equals("ModifyBook") || key.equals("DeleteBook")) {
            SearchBook.SearchFor search;
            if(key.equals("ModifyBook"))
                search = SearchBook.SearchFor.MODIFY;
            else
                search = SearchBook.SearchFor.DELETE;
            SearchBook searchBook = new SearchBook(search);
            searchBook.subscribe("SubViewChange", this);
            searchBook.subscribe("ParentView", this);
            searchBook.subscribe("ViewCancelled", this);
            currentView = ViewFactory.createView("SearchBookView", searchBook);
            myViews.put("SearchBookView", currentView);
        }else if (key.equals("AddWorker")){
            System.out.println(key);
            Worker worker = new Worker( new Properties());
            worker.subscribe("ViewCancelled", this);
            currentView  = ViewFactory.createView("AddWorkerView", worker);
//            myViews.put("AddBookView", currentView);
        } else if (key.equals("AddStudentBorrower")){
            StudentBorrower studentBorrower = new StudentBorrower( new Properties());
            studentBorrower.subscribe("ViewCancelled", this);
            currentView  = ViewFactory.createView("AddStudentBorrowerView", studentBorrower);
//            myViews.put("AddBookView", currentView);
        } else if (key.equals("Welcome") || key.equals("ViewCancelled")) {
            currentView = ViewFactory.createView("WelcomeView", myWorkerHolder);
//            myViews.put("AddBookView", currentView);
        } else if (key.equals("SubViewChange")){
            currentView = (View) value;
        } else if(key.equals("ParentView")){
            currentView = myViews.get(value);
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
