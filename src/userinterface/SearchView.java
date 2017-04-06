package userinterface;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.Book;
import userinterface.book.BookTableModel;

import java.util.HashMap;
import java.util.Properties;

/**
 * Created by Sammytech on 4/6/17.
 */
abstract public class SearchView<T> extends View {

    protected HashMap<T, String> fields;
    protected JFXButton searchButton;
    protected JFXButton cancelButton;
    protected ListView searchResult;
    public SearchView(IModel model, String classname) {
        super(model, classname);
        model.subscribe("UpdateSearch", this);
        fields = getFields();
        VBox root = new VBox();
        root.setAlignment(Pos.TOP_CENTER);

        TitledPane gridTitlePane = new TitledPane();
        gridTitlePane.setFocusTraversable(false);
        gridTitlePane.setId("searchBox");

        GridPane grid = createSearch();
        HBox buttonPane = setupSearchButton();
        grid.setPadding(new Insets(30,30,0,30));
        buttonPane.setPadding(new Insets(30,30,0,30));
//        grid.add(buttonPane, 0, row, 2, 1);
        gridTitlePane.setText("Advanced Search");
        VBox searchBox = new VBox();
        searchBox.getChildren().addAll(grid, buttonPane);
        gridTitlePane.setContent(searchBox);


        root.getChildren().add(gridTitlePane);

        searchResult = getSearchResult();
        searchResult.setVisible(false);
        root.getChildren().add(searchResult);
        getChildren().add(root);

    }




    private HBox setupSearchButton(){
        HBox buttonPane = new HBox();
        buttonPane.setSpacing(30);
        buttonPane.setPadding(new Insets(22));
        buttonPane.setAlignment(Pos.CENTER_RIGHT);
        searchButton = new JFXButton(Utilities.getStringLang("search"));
        cancelButton = new JFXButton(Utilities.getStringLang("cancel_btn"));
        searchButton.getStyleClass().add("button-raised");
        cancelButton.getStyleClass().add("button-raised");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                MainStageContainer.getInstance().close();
//                MainStageContainer.setStage(new Stage(), "Here");
//                MainStageContainer.getInstance().show();
                myModel.stateChangeRequest("SearchBookCancelled", null);
            }
        });

        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processSearch();

            }
        });

        buttonPane.setSpacing(20);
        buttonPane.setPadding(new Insets(20));
        buttonPane.getChildren().addAll(searchButton, cancelButton);

        return buttonPane;
    }

    protected void processSearch() {
        Properties prop = validateSearch();
        if(prop.size() > 0){
            myModel.stateChangeRequest("ProcessSearch", prop);
        }

    }

    protected abstract  GridPane createSearch();
    protected abstract  Properties validateSearch();
    protected abstract HashMap<T,String> getFields();
    protected abstract ListView getSearchResult();
    protected abstract void UpdateSearchResult(Object value);

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("UpdateSearch")){
            UpdateSearchResult(value);
        }
    }


}
