package userinterface;

import impresario.IModel;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/5/17.
 */
public class MainView extends View {
    public MainView(IModel model) {
        super(model, "MainView");
        VBox container = new VBox();
        container.setPrefSize(500, 600);
        MenuBar menuBar = new MenuBar();

        // --- Menu Book
        Menu menuFile = new Menu("Book");

        // --- Menu Worker
        Menu menuEdit = new Menu("Worker");

        // --- Menu Student
        Menu menuView = new Menu("Student");

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);

        container.getChildren().add(menuBar);
        getChildren().add(container);
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
