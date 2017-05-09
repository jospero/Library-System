package userinterface.book;

import impresario.IModel;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;
import userinterface.TitleView;
import userinterface.View;

/**
 * Created by Sammytech on 5/9/17.
 */
public class RentalCollectionView extends View {



    public RentalCollectionView(IModel model) {
        super(model, "RentalCollectionView");
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(TitleView.createTitle("LIST BOOKS CHECKED OUT"));

        getChildren().add(container);

//        populateFields();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
