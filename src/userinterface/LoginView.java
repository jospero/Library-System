package userinterface;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.util.Properties;

/**
 * Created by Sammytech on 3/4/17.
 */



public class LoginView extends View{
    private JFXTextField bannerId;
    private JFXPasswordField password;
    private JFXSnackbar bar;
    public LoginView(IModel model) {
        super(model, "LoginView");
        String css = this.getClass().getResource("/resources/css/login.css").toExternalForm();
        this.getStylesheets().add(css);
        VBox container = new VBox();
        container.setPadding(new Insets(40,40,0,40));
        container.setSpacing(25);
        HBox title = TitleView.createTitle(Utilities.getStringLang("brockport_library"));
        title.setSpacing(30);
//        ImageView lock = new ImageView(new File("resources/images/lock-icon.png").toURI().toString());
        ImageView lock = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream("resources/images/lock-icon.png")));
        lock.setFitWidth(80);
        lock.setPreserveRatio(true);
        title.getChildren().add(lock);

        container.setId("login");
        container.setPrefWidth(500);


        //  Fields
        VBox fields = new VBox();
        fields.setSpacing(50);
        //  BannerId
//        Label userLabel = new Label(Utilities.getStringLang("bid"));

        bannerId = new JFXTextField();
        bannerId.setPromptText(Utilities.getStringLang("bid"));
        bannerId.setLabelFloat(true);
//        bannerId.setId("bannerIdtf");
//        bannerId.getStyleClass().add("textfield");
        bannerId.setPadding(new Insets(10,0, 10,40));
        bannerId.setPrefHeight(40);
//        fields.getChildren().addAll(userLabel, bannerId);
        fields.getChildren().addAll(bannerId);

        // Password
//        Label passwordLabel = new Label(Utilities.convertToTitleCase(Utilities.getStringLang("login_pass")));

        password = new JFXPasswordField();
        password.setPromptText(Utilities.convertToTitleCase(Utilities.getStringLang("login_pass")));
        password.setLabelFloat(true);
//        password.getStyleClass().add("textfield");
//        password.setId("passwordtf");
        password.setPadding(new Insets(10,0, 10,40));
        password.setPrefHeight(40);

        password.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processLogin();
            }
        });
        fields.getChildren().addAll(password);

        container.getChildren().addAll(title, fields);
        // Login Button
        HBox buttonContainer = new HBox();
        buttonContainer.setPrefHeight(60);
        buttonContainer.setPadding(new Insets(10,0,0,0));
        JFXButton loginButton = new JFXButton(Utilities.getStringLang("login_btn"));
        loginButton.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
        loginButton.getStyleClass().add("button-raised");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefWidth(buttonContainer.getPrefWidth());
        loginButton.setMaxHeight(buttonContainer.getPrefHeight());
        HBox.setHgrow(loginButton, Priority.ALWAYS);

//        loginButton.setPadding(new Insets(10,0,10,0));
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                processLogin();
            }
        });
        buttonContainer.getChildren().add(loginButton);
        container.getChildren().add(buttonContainer);

        // MessageView
        AnchorPane pane = new AnchorPane();
        pane.setPrefHeight(60);
        pane.setPrefWidth(container.getPrefWidth());
        bar = new JFXSnackbar(pane);
        container.getChildren().add(pane);
        getChildren().add(container);

        myModel.subscribe("LoginError", this);
    }

    private void processLogin() {
        boolean validField = validateLogin();
        if(validField){
            String bannerId_str = bannerId.getText();
            String password_str = password.getText();
            Properties loginProp = new Properties();
            loginProp.setProperty("BannerId", bannerId_str);
            loginProp.setProperty("Password", password_str);
            myModel.stateChangeRequest("ProcessLogin", loginProp);
        } else {
            displayErrorMessage(Utilities.getStringLang("error_string") + ": "+
                    Utilities.getStringLang("invalid_login"));
        }

    }

    private boolean validateLogin(){
        String bannerId_str = bannerId.getText();
        String password_str = password.getText();
        if(bannerId_str.isEmpty() || password_str.isEmpty())
            return false;
        return true;
    }

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("LoginError")){
            displayErrorMessage((String) value);
        }
    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        bar.show(message, 5000);
        bannerId.requestFocus();
    }

}
