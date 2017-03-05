package model;

import event.Event;
import exception.InvalidLoginException;
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by Sammytech on 2/28/17.
 */
public class Login implements IView, IModel {

    // For Impresario
    private Properties dependencies;
    private ModelRegistry myRegistry;

	private Worker myWorker;

    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    private String loginErrorMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public Login()
    {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        // STEP 3.1: Create the Registry object - if you inherit from
        // EntityBase, this is done for you. Otherwise, you do it yourself
        myRegistry = new ModelRegistry("Login");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Login",
                    "Could not instantiate Registry", Event.ERROR);
        }

        // STEP 3.2: Be sure to set the dependencies correctly
        setDependencies();

        // Set up the initial view
        createAndShowLoginView();
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("ProcessLogin", "LoginError");
        myRegistry.setDependencies(dependencies);
    }

    private void createAndShowLoginView()
    {
        Scene currentScene = myViews.get("LoginView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("LoginView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("LoginView", currentScene);
        }

        swapToView(currentScene);

    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        if(key.equals("LoginError")){
            return loginErrorMessage;
        }
        return "";
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
        if(key.equals("ProcessLogin")){
            if (value != null)
            {
                loginErrorMessage = "";

                boolean flag = loginWorker((Properties)value);
                if (flag == true)
                {
                    createAndShowMainView();
                } else {
                    myRegistry.updateSubscribers(key, this);
                }

            }
        }

    }

    private void createAndShowMainView() {
        Main main = new Main(myWorker);
        main.subscribe("Logout", this);
        main.createAndShowView();

    }

    /**
     * Login Worker corresponding to bannerId and password.
     */
    //----------------------------------------------------------
    public boolean loginWorker(Properties props)
    {
        try
        {
            myWorker = new Worker(props);
            System.out.println("Account Holder: " + myWorker.getState("Password") + " successfully logged in");
            return true;
        }
        catch (InvalidLoginException ex)
        {
            loginErrorMessage = "ERROR: " + ex.getMessage();
            return false;
        }
    }

    //-----------------------------------------------------------------------------
    public void swapToView(Scene newScene)
    {


        if (newScene == null)
        {
            System.out.println("Login.swapToView(): Missing view for display");
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
