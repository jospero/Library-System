package userinterface;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Created by Sammytech on 2/21/17.
 */
public class TitleView {
    public static HBox createTitle(String title) {
        HBox container = new HBox();
        container.setPadding(new Insets(10, 0,10,0));
        container.setAlignment(Pos.CENTER);

        Text titleText = new Text(" "+title+" ");
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleText.setId("title");
//        titleText.setStyle("-fx-fill: #FEFEFE;");
        titleText.setWrappingWidth(300);
        titleText.setTextAlignment(TextAlignment.CENTER);
//        titleText.setFill(Color.DARKGREEN);
        container.getChildren().add(titleText);

        return container;
    }
}
