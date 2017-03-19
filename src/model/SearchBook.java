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

/**
 * Created by Sammytech on 3/11/17.
 */
public class SearchBook implements IView, IModel {
    enum SearchFor{
        MODIFY, DELETE
    }

    private final SearchFor searchFor;
    protected Properties dependencies;
    protected ModelRegistry myRegistry;
    protected ArrayList<View> nextView;
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
        dependencies.setProperty("ProcessSearch", "SubViewChange");
        dependencies.setProperty("ViewBookCancelled", "SubViewChange");
        dependencies.setProperty("ResultViewCancelled", "ParentView");
        dependencies.setProperty("SearchBookCancelled", "ViewCancelled");
        dependencies.setProperty("ViewBook", "SubViewChange");
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
            BookCollection bookCollection = new BookCollection();
            try {
                bookCollection.findBooks();
            } catch (InvalidPrimaryKeyException e) {
                e.printStackTrace();
            }
            bookCollection.subscribe("ResultViewCancelled", this);
            bookCollection.subscribe("ViewBook", this);
            nextView.add(bookCollection.createView());
        } else if(key.equals("ViewBook")){
            if(searchFor == SearchFor.MODIFY){
                Book book = (Book) value;
                book.subscribe("ViewBookCancelled", this);
                nextView.add(ViewFactory.createView("ModifyBookView", book));
            }
        } else if(key.equals("ViewBookCancelled")){
            nextView.remove(nextView.size()-1);
        }
        myRegistry.updateSubscribers(key, this);

    }
}
