package userinterface.book;

import impresario.IModel;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import model.Book;
import userinterface.TitleView;
import userinterface.View;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 5/9/17.
 */
public class RentalCollectionView extends View {

    protected ObservableList tableData;
    protected ListView rentalResult;

    public RentalCollectionView(IModel model) {
        super(model, "RentalCollectionView");
        model.subscribe("UpdateRental", this);
        // create a container for showing the contents
        VBox container = new VBox(10);
        container.setPadding(new Insets(15, 5, 5, 5));

        // create our GUI components, add them to this panel
        container.getChildren().add(TitleView.createTitle("LIST BOOKS CHECKED OUT"));



        rentalResult = getRentalResult();
        rentalResult.getStyleClass().add("searchList");
        container.getChildren().add(rentalResult);

        getChildren().add(container);
        myModel.stateChangeRequest("ProcessRental", null);
//        populateFields();
    }

    private ListView getRentalResult() {

        ListView<RentalTableModel> listView = new ListView<>();
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.setCellFactory(new Callback<ListView<RentalTableModel>, ListCell<RentalTableModel>>() {
            @Override
            public ListCell<RentalTableModel> call(ListView<RentalTableModel> param) {
                return new RentalListViewCell();
            }
        });
        tableData = FXCollections.observableArrayList();
        listView.prefHeightProperty().bind(Bindings.size(tableData).multiply(RentalListViewCell.getCustomHeight()+ 10));
        return listView;
    }

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("UpdateRental")){
            tableData.clear();
            Vector entryList = (Vector)value;
            System.out.println(value);
            if(entryList.size() > 0) {
                Enumeration entries = entryList.elements();
                insertDataToTable(entries);
                rentalResult.setItems(tableData);
            } else{

            }
        }
    }

    protected void insertDataToTable(Enumeration entries) {

        while (entries.hasMoreElements())
        {
            Properties tempProp = (Properties) entries.nextElement();
            Vector<String> view = new Vector<>();
            for(Object eachProp : tempProp.values()){
                view.addElement((String) eachProp);
            }

            // add this list entry to the list
            RentalTableModel nextRowData = new RentalTableModel(view);
            tableData.add(nextRowData);
        }


    }
}
