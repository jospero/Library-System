import event.Event;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Login;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

/**
 * Created by Sammytech on 2/28/17.
 */
public class LibraryGUI extends Application
{

    private Login myLogin;		// the main behavior for the application

    /** Main frame of the application */
    private Stage mainStage;


    // start method for this class, the main application object
    //----------------------------------------------------------
    public void start(Stage primaryStage)
    {
        System.out.println("Educational Opportunity Program Login Version 1.00");

        // Create the top-level container (main frame) and add contents to it.
        MainStageContainer.setStage(primaryStage, "EOP Login");
        mainStage = MainStageContainer.getInstance();
        //com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon("resources/images/shield.png").getImage());
        // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
        // 'X' IN THE WINDOW), and show it.
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
        });

        try
        {
            myLogin = new Login();
        }
        catch(Exception exc)
        {
            System.err.println("LIBRARY - could not create Login!");
            new Event(Event.getLeafLevelClassName(this), "Login.<init>", "Unable to create Librarian object", Event.ERROR);
            exc.printStackTrace();
        }


        WindowPosition.placeCenter(mainStage);

        mainStage.show();
    }


    /**
     * The "main" entry point for the application. Carries out actions to
     * set up the application
     */
    //----------------------------------------------------------
    public static void main(String[] args)
    {

        launch(args);
    }

}
