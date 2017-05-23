package userinterface;

import Utilities.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import impresario.IModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.Book;
import userinterface.book.BookTableModel;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 4/6/17.
 */
abstract public class SearchView<T> extends View {

    protected ObservableList tableData;
    protected HashMap<T, String> fields;
    protected JFXButton searchButton;
    protected JFXButton cancelButton;
    protected ListView searchResult;
    protected HBox notFoundBox;
    protected TitledPane gridTitlePane;
    public SearchView(IModel model, String classname) {

        super(model, classname);

        model.subscribe("UpdateSearch", this);
        fields = getFields();
        VBox root = new VBox();
        root.setPadding(new Insets(20,30,30,30));
        root.setAlignment(Pos.TOP_CENTER);

        gridTitlePane = new TitledPane();
        gridTitlePane.setFocusTraversable(false);
        gridTitlePane.setId("searchBox");

        GridPane grid = createSearch();
        grid.setHgap(30);
        grid.setVgap(30);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(100/getMaxColumn());
        for(int i = 0; i < getMaxColumn(); i++){
            grid.getColumnConstraints().add(col1);
        }
        HBox buttonPane = setupSearchButton();
        grid.setPadding(new Insets(30,30,0,30));
        buttonPane.setPadding(new Insets(30,30,0,30));
//        grid.add(buttonPane, 0, row, 2, 1);
        gridTitlePane.setText(Utilities.getStringLang("advanced_search"));
        VBox searchBox = new VBox();
        searchBox.getChildren().addAll(grid, buttonPane);
        gridTitlePane.setContent(searchBox);


        root.getChildren().add(gridTitlePane);

        searchResult = getSearchResult();
        searchResult.getStyleClass().add("searchList");
        searchResult.setVisible(false);

        searchResult.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(searchResult.getSelectionModel().getSelectedItem() != null){
                    int index = searchResult.getSelectionModel().getSelectedIndex();
//                    searchResult.getSelectionModel().clearSelection();
                    viewItem(index);

                }
            }
        });
//        searchResult.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//                System.out.println("Onchange");
//                if(searchResult.getSelectionModel().getSelectedItem() != null){
//                    int index = searchResult.getSelectionModel().getSelectedIndex();
//                    searchResult.getSelectionModel().clearSelection();
//                    System.out.println("What");
//                    viewItem(index);
//
//                }
//            }
//        });
        root.getChildren().add(searchResult);

        notFoundBox = new HBox();
        Label notFoundLabel = new Label("No Search Result Found");
        notFoundBox.getChildren().add(notFoundLabel);
        root.getChildren().add(notFoundBox);
        notFoundBox.setVisible(false);
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
                myModel.stateChangeRequest("SearchCancelled", null);
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
    protected abstract void insertDataToTable(Enumeration entries);
//    protected abstract void viewItem(int selectedIndex);
    protected abstract int getMaxColumn();

    protected void viewItem(int selectedIndex) {
        System.out.println("View item" + selectedIndex);
        myModel.stateChangeRequest("View", selectedIndex);
    }

    protected final void setupField(JFXTextField node, String prompt){
        node.setPadding(new Insets(30, 0, 30, 0));
        node.setLabelFloat(true);
        node.setPromptText(prompt);
    }

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("UpdateSearch")){
            tableData.clear();
            Vector entryList = (Vector)value;
            System.out.println(value);
            if(entryList.size() > 0) {
                Enumeration entries = entryList.elements();
                insertDataToTable(entries);
                searchResult.setItems(tableData);
                notFoundBox.setVisible(false);
                searchResult.setVisible(true);
            } else{
                searchResult.setVisible(false);
                notFoundBox.setVisible(true);
            }
        }
    }


}
