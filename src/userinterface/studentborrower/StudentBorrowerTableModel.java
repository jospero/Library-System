package userinterface.studentborrower;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class StudentBorrowerTableModel {

    private final SimpleStringProperty bannerId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty borrowerStatus;
    private final SimpleStringProperty dateOfLastBorrowerStatus;
    private final SimpleStringProperty dateOfRegistration;
    private final SimpleStringProperty notes;
    private final SimpleStringProperty status;

    //----------------------------------------------------------------------------
    public StudentBorrowerTableModel(Vector<String> bookData)
    {
        bannerId =  new SimpleStringProperty(bookData.elementAt(0));
        firstName =  new SimpleStringProperty(bookData.elementAt(1));
        lastName =  new SimpleStringProperty(bookData.elementAt(2));
        phone =  new SimpleStringProperty(bookData.elementAt(3));
        email =  new SimpleStringProperty(bookData.elementAt(4));
        borrowerStatus =  new SimpleStringProperty(bookData.elementAt(5));
        dateOfLastBorrowerStatus =  new SimpleStringProperty(bookData.elementAt(6));
        dateOfRegistration =  new SimpleStringProperty(bookData.elementAt(7));
        notes =  new SimpleStringProperty(bookData.elementAt(8));
        status =  new SimpleStringProperty(bookData.elementAt(9));
    }

    public String getBannerId() {
        return bannerId.get();
    }



    public void setBannerId(String bannerId) {
        this.bannerId.set(bannerId);
    }

    public String getFirstName() {
        return firstName.get();
    }


    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }



    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPhone() {
        return phone.get();
    }


    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }



    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getBorrowerStatus() {
        return borrowerStatus.get();
    }


    public void setBorrowerStatus(String borrowerStatus) {
        this.borrowerStatus.set(borrowerStatus);
    }

    public String getDateOfLastBorrowerStatus() {
        return dateOfLastBorrowerStatus.get();
    }

    public void setDateOfLastBorrowerStatus(String dateOfLastBorrowerStatus) {
        this.dateOfLastBorrowerStatus.set(dateOfLastBorrowerStatus);
    }

    public String getDateOfRegistration() {
        return dateOfRegistration.get();
    }



    public void setDateOfRegistration(String dateOfRegistration) {
        this.dateOfRegistration.set(dateOfRegistration);
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
