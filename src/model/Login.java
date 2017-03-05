package model;

import event.Event;
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

//	private AccountHolder myAccountHolder;

    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

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
        myRegistry.setDependencies(dependencies);
    }

    private void createAndShowLoginView()
    {
        Scene currentScene = (Scene)myViews.get("LoginView");

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
