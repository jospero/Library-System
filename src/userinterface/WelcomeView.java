package userinterface;

import Utilities.Utilities;
import impresario.IModel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;

/**
 * Created by Sammytech on 3/6/17.
 */
public class WelcomeView extends View {

    public WelcomeView(IModel model) {
        super(model, "WelcomeView");
        File file = new File("resources/css/welcome.css");
        this.getStylesheets().add(file.toURI().toString());
        VBox box = new VBox();
        box.setSpacing(40);
        box.setStyle("-fx-background-color: #00533e");

        box.setAlignment(Pos.CENTER);
        String labelString = Utilities.getStringLang("welcome_string") +" "+ myModel.getState("FirstName") + " " + myModel.getState("LastName");
        Label label = new Label(labelString.toUpperCase());
        label.setFont(Font.font(40));
        label.setId("welcome");
        file = new File("resources/images/SUNY_Brockport_Logo.png");
        final ImageView imv = new ImageView();
        imv.setPreserveRatio(true);
        imv.setFitHeight(300);
        final Image image2 = new Image(file.toURI().toString());
        imv.setImage(image2);
        box.getChildren().addAll(imv, label);
        getChildren().add(box);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
