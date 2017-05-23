package userinterface.book;

import javafx.beans.property.SimpleStringProperty;

import java.util.Vector;

/**
 * Created by Sammytech on 3/12/17.
 */
public class RentalTableModel {

    private final SimpleStringProperty barcode;
    private final SimpleStringProperty title;
    private final SimpleStringProperty bannerIdBorrower;
    private final SimpleStringProperty lastNameBorrower;
    private final SimpleStringProperty firstNameBorrower;
    private final SimpleStringProperty dateCheckOut;
    private final SimpleStringProperty dateDue;
    private final SimpleStringProperty bannerIdWorker;
    private final SimpleStringProperty firstNameWorker;
    private final SimpleStringProperty lastNameWorker;

    //----------------------------------------------------------------------------
    public RentalTableModel(Vector<String> rentalData)
    {
        dateCheckOut =  new SimpleStringProperty(rentalData.elementAt(0));
        lastNameBorrower =  new SimpleStringProperty(rentalData.elementAt(1));
        firstNameBorrower =  new SimpleStringProperty(rentalData.elementAt(2));
        title =  new SimpleStringProperty(rentalData.elementAt(3));
        dateDue =  new SimpleStringProperty(rentalData.elementAt(4));
        lastNameWorker =  new SimpleStringProperty(rentalData.elementAt(5));
        barcode =  new SimpleStringProperty(rentalData.elementAt(6));
        bannerIdWorker =  new SimpleStringProperty(rentalData.elementAt(7));
        firstNameWorker =  new SimpleStringProperty(rentalData.elementAt(8));
        bannerIdBorrower =  new SimpleStringProperty(rentalData.elementAt(9));
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

    public String getBannerIdBorrower() {
        return bannerIdBorrower.get();
    }


    public void setBannerIdBorrower(String bannerIdBorrower) {
        this.bannerIdBorrower.set(bannerIdBorrower);
    }

    public String getLastNameBorrower() {
        return lastNameBorrower.get();
    }


    public void setLastNameBorrower(String lastNameBorrower) {
        this.lastNameBorrower.set(lastNameBorrower);
    }

    public String getFirstNameBorrower() {
        return firstNameBorrower.get();
    }


    public void setFirstNameBorrower(String firstNameBorrower) {
        this.firstNameBorrower.set(firstNameBorrower);
    }

    public String getDateCheckOut() {
        return dateCheckOut.get();
    }


    public void setDateCheckOut(String dateCheckOut) {
        this.dateCheckOut.set(dateCheckOut);
    }

    public String getDateDue() {
        return dateDue.get();
    }

    public void setDateDue(String dateDue) {
        this.dateDue.set(dateDue);
    }

    public String getBannerIdWorker() {
        return bannerIdWorker.get();
    }

    public void setBannerIdWorker(String bannerIdWorker) {
        this.bannerIdWorker.set(bannerIdWorker);
    }

    public String getFirstNameWorker() {
        return firstNameWorker.get();
    }

    public void setFirstNameWorker(String firstNameWorker) {
        this.firstNameWorker.set(firstNameWorker);
    }

    public String getLastNameWorker() {
        return lastNameWorker.get();
    }

    public void setLastNameWorker(String lastNameWorker) {
        this.lastNameWorker.set(lastNameWorker);
    }
}
