package userinterface.worker;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class WorkerTableModel {

    private final SimpleStringProperty bannerId;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty credentials;
    private final SimpleStringProperty dateOfLastestCredentialStatus;
    private final SimpleStringProperty dateOfHire;
    private final SimpleStringProperty status;

    //----------------------------------------------------------------------------
    public WorkerTableModel(Vector<String> bookData)
    {
        bannerId =  new SimpleStringProperty(bookData.elementAt(0));
        firstName =  new SimpleStringProperty(bookData.elementAt(1));
        lastName =  new SimpleStringProperty(bookData.elementAt(2));
        phone =  new SimpleStringProperty(bookData.elementAt(3));
        email =  new SimpleStringProperty(bookData.elementAt(4));
        credentials =  new SimpleStringProperty(bookData.elementAt(5));
        dateOfLastestCredentialStatus =  new SimpleStringProperty(bookData.elementAt(6));
        dateOfHire =  new SimpleStringProperty(bookData.elementAt(7));
        status =  new SimpleStringProperty(bookData.elementAt(8));
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

    public String getCredentials() {
        return credentials.get();
    }



    public void setCredentials(String credentials) {
        this.credentials.set(credentials);
    }

    public String getDateOfLastestCredentialStatus() {
        return dateOfLastestCredentialStatus.get();
    }



    public void setDateOfLastestCredentialStatus(String dateOfLastestCredentialStatus) {
        this.dateOfLastestCredentialStatus.set(dateOfLastestCredentialStatus);
    }

    public String getDateOfHire() {
        return dateOfHire.get();
    }



    public void setDateOfHire(String dateOfHire) {
        this.dateOfHire.set(dateOfHire);
    }

    public String getStatus() {
        return status.get();
    }



    public void setStatus(String status) {
        this.status.set(status);
    }
}
