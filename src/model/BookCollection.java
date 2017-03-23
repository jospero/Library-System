package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class BookCollection extends EntityBase implements IView {

    private static final String myTableName = "Book";

    private Vector<Book> books;
    protected Properties dependencies;
    protected View nextView;
    protected int selectedBook = -1;
    // GUI Components

    // constructor for this class
    //----------------------------------------------------------
    public BookCollection()
    {
        super(myTableName);

        books = new Vector<Book>();

        setDependencies();
    }

    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("CancelBookList","ResultViewCancelled");
        myRegistry.setDependencies(dependencies);
    }

    private void retrieveHelper(String query) throws InvalidPrimaryKeyException {
        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            books = new Vector<Book>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextPatronData = (Properties)allDataRetrieved.elementAt(cnt);

                Book book = new Book(nextPatronData);
                if (book != null)
                {
                    books.add(book);
                }
            }

        }
        else
        {
            books = new Vector<Book>();
        }
    }


    public void findBooksOlderThanDate(String year) throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName + " WHERE (pubYear <= " + year + ")";
        retrieveHelper(query);

    }

    public void findBooks() throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName;
        retrieveHelper(query);
    }

    public void findBooksNewerThanDate(String year) throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName + " WHERE (pubYear >= " + year + ")";
        retrieveHelper(query);

    }

    public void findBooksWithTitleLike(String title) throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName + " WHERE (title LIKE '%" + title + "%')";
        retrieveHelper(query);

    }

    public void findBooksWithAuthorLike(String author) throws InvalidPrimaryKeyException{
        String query = "SELECT * FROM " + myTableName + " WHERE (author LIKE %" + author + "%)";
        retrieveHelper(query);

    }



    /**
     *
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("Books"))
            return books;
        else if (key.equals("BookList"))
            return this;
        else if(key.equals("ViewBook")) {
            if (selectedBook >= 0) {
                return books.get(selectedBook);
            } else {
                return null;
            }
        }
        return null;
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        System.out.println(key);
        if(key.equals("ViewBook")){
            selectedBook = (int) value;
        }
        myRegistry.updateSubscribers(key, this);
    }

    //----------------------------------------------------------
    public Book retrieve(String bookId)
    {
        Book retValue = null;
        for (int cnt = 0; cnt < books.size(); cnt++)
        {
            Book nextAcct = books.elementAt(cnt);
            String nextAccNum = (String)nextAcct.getState("bookId");
            if (nextAccNum.equals(bookId) == true)
            {
                retValue = nextAcct;
                return retValue; // we should say 'break;' here
            }
        }

        return retValue;
    }


    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    protected void createAndShowView()
    {
//        Scene currentScene = myViews.get("BookCollectionView");

//        if (currentScene == null)
//        {
//            // create our initial view
//            View newView = ViewFactory.createView("BookCollectionView", this);
//            currentScene = new Scene(newView);
//            myViews.put("BookCollectionView", currentScene);
//        }
//        swapToView(currentScene);
    }

    protected View createView(){
        View currentView = myViews.get("BookCollectionView");

        if (currentView == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("BookCollectionView", this);
            currentView = newView;
            myViews.put("BookCollectionView", currentView);
        }
        return currentView;
    }


    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}

