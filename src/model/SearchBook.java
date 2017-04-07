package model;

import event.Event;
import exception.InvalidPrimaryKeyException;
import impresario.IModel;
import impresario.IView;
import impresario.ModelRegistry;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchBook implements IView, IModel {

    private final SearchFor searchFor;
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
    private BookCollection bookCollection;
    private int selectedBook = -1;
    public SearchBook(SearchFor searchFor) {
        this.searchFor = searchFor;
        nextView = new ArrayList<>();
        myRegistry = new ModelRegistry("SearchBook");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "SearchBook",
                    "Could not instantiate Registry", Event.ERROR);
        }
        setDependencies();

    }

    protected void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("ProcessSearch", "UpdateSearch");
        dependencies.setProperty("ViewBookCancelled", "ParentView");
        dependencies.setProperty("SearchCancelled", "ViewCancelled");
        dependencies.setProperty("View", "SubViewChange");
        myRegistry.setDependencies(dependencies);
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }

    @Override
    public Object getState(String key) {
        System.out.println("SearchBook" + key);
        if(key.equals("SubViewChange")){
            return nextView.get(nextView.size()-1);
        } if(key.equals("ParentView")){
            return "SearchBookView";
        } if(key.equals("Books") || key.equals("UpdateSearch")){
            if(bookCollection != null)
                return bookCollection.getState("Books");
        }
        return null;
    }

    @Override
    public void subscribe(String key, IView subscriber) {
        myRegistry.subscribe(key, subscriber);
    }

    @Override
    public void unSubscribe(String key, IView subscriber) {
        myRegistry.unSubscribe(key, subscriber);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        System.out.println("SCR "+ key);
        if(key.equals("ProcessSearch")){
            bookCollection = new BookCollection();
            Properties props = (Properties) value;
            if(props.contains("barcode")) {
                try {
                    bookCollection.findBookBarcode(props.getProperty("Barcode"));
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
            } else{
                try {
                    bookCollection.findBooksCriteria(props);
                } catch (InvalidPrimaryKeyException e) {
                    e.printStackTrace();
                }
            }
        } else if(key.equals("View")){
            if(searchFor == SearchFor.MODIFY){
                selectedBook = (int) value;
                Book book = ((Vector<Book>)bookCollection.getState("Books")).get(selectedBook);
                book.subscribe("ViewBookCancelled", this);
                nextView.add(ViewFactory.createView("ModifyBookView", book));
            }
        } else if(key.equals("ViewBookCancelled")){
            Book book = ((Vector<Book>)bookCollection.getState("Books")).get(selectedBook);
            book.unSubscribe("ViewBookCancelled", this);
            if(value != null && (boolean)value){
                myRegistry.updateSubscribers("UpdateSearch", this);
            }
            nextView.remove(nextView.size()-1);

        }
        myRegistry.updateSubscribers(key, this);

    }
}
