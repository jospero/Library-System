package model;

import Utilities.Utilities;
import event.Event;
import exception.InvalidLoginException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;

import javax.swing.*;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by Sammytech on 2/28/17.
 */
public class Login implements IView, IModel {

    // For Impresario
    private Properties dependencies;
    private ModelRegistry myRegistry;

	private WorkerHolder myWorkerHolder;

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
//        createAndShowMainView();
    }


    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("ProcessLogin", "LoginError");
        dependencies.setProperty("Logout", "Logout");
        myRegistry.setDependencies(dependencies);
    }

    private void createAndShowLoginView()
    {
        Scene currentScene = myViews.get("LoginView");
        myStage.getIcons().add(new Image("resources/images/shield.png"));
//        try {
//            com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon("resources/images/shield.png").getImage());
//        } catch (Exception e) {
//            // Won't work on Windows or Linux.
//        }
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
        } else if(key.equals("Logout")){
            myWorkerHolder = null;
            createAndShowLoginView();
        }

    }

    private void createAndShowMainView() {
        Main main = new Main(myWorkerHolder);
        main.subscribe("Logout", this);
        main.createAndShowView();

    }

    /**
     * Login WorkerHolder corresponding to bannerId and password.
     */
    //----------------------------------------------------------
    public boolean loginWorker(Properties props)
    {

        try
        {
            myWorkerHolder = new WorkerHolder(props);
            System.out.println("Account Holder: " + myWorkerHolder.getState("Password") + " successfully logged in");
            return true;
        }
        catch (InvalidLoginException ex)
        {
            loginErrorMessage = Utilities.getStringLang("error_string")+ ": " + ex.getMessage();
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
