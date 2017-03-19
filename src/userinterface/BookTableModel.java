package userinterface;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class BookTableModel {

    private final SimpleStringProperty barcode;
    private final SimpleStringProperty title;
    private final SimpleStringProperty discipline;
    private final SimpleStringProperty authors;
    private final SimpleStringProperty publisher;
    private final SimpleStringProperty yearOfPublication;
    private final SimpleStringProperty isbn;
    private final SimpleStringProperty condition;
    private final SimpleStringProperty suggestedPrice;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty status;

    //----------------------------------------------------------------------------
    public BookTableModel(Vector<String> accountData)
    {
        barcode =  new SimpleStringProperty(accountData.elementAt(0));
        title =  new SimpleStringProperty(accountData.elementAt(1));
        discipline =  new SimpleStringProperty(accountData.elementAt(2));
        authors =  new SimpleStringProperty(accountData.elementAt(3));
        publisher =  new SimpleStringProperty(accountData.elementAt(4));
        yearOfPublication =  new SimpleStringProperty(accountData.elementAt(5));
        isbn =  new SimpleStringProperty(accountData.elementAt(6));
        condition =  new SimpleStringProperty(accountData.elementAt(7));
        suggestedPrice =  new SimpleStringProperty(accountData.elementAt(8));
        notes =  new SimpleStringProperty(accountData.elementAt(9));
        status =  new SimpleStringProperty(accountData.elementAt(10));
    }

    public String getBarcode() {
        return barcode.get();
    }

    public void setBarcode(String barcode) {
        this.barcode.set(barcode);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDiscipline() {
        return discipline.get();
    }



    public void setDiscipline(String discipline) {
        this.discipline.set(discipline);
    }

    public String getAuthors() {
        return authors.get();
    }



    public void setAuthors(String authors) {
        this.authors.set(authors);
    }

    public String getPublisher() {
        return publisher.get();
    }



    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getYearOfPublication() {
        return yearOfPublication.get();
    }



    public void setYearOfPublication(String yearOfPublication) {
        this.yearOfPublication.set(yearOfPublication);
    }

    public String getIsbn() {
        return isbn.get();
    }


    public void setIsbn(String isbn) {
        this.isbn.set(isbn);
    }

    public String getCondition() {
        return condition.get();
    }


    public void setCondition(String condition) {
        this.condition.set(condition);
    }

    public String getSuggestedPrice() {
        return suggestedPrice.get();
    }



    public void setSuggestedPrice(String suggestedPrice) {
        this.suggestedPrice.set(suggestedPrice);
    }

    public String getNotes() {
        return notes.get();
    }



    public void setNotes(String notes) {
        this.notes.set(notes);
    }

    public String getStatus() {
        return status.get();
    }



    public void setStatus(String status) {
        this.status.set(status);
    }
}
