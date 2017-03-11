package userinterface;

import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;

/**
 * Created by Sammytech on 3/5/17.
 */
public class MainView extends View {
    private Group mainView;
    private VBox container;

    public MainView(IModel model) {
        super(model, "MainView");
        this.getStylesheets().add("resources/css/common.css");
        container = new VBox();
//        container.setPrefWidth(WIDTH);

        container.getChildren().add(createMenu());

        swapContentView((View) myModel.getState("ChangeView"));
//        container.getChildren().add(mainView);
        getChildren().add(container);

        myModel.subscribe("ChangeView", this);
    }

    private MenuBar createMenu(){
        MenuBar menuBar = new MenuBar();

        // --- Menu Book
        Menu menuBook = new Menu("Book");
        MenuItem addBook = new MenuItem("Add Book");
        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        addBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("AddBook", null);
            }
        });

        MenuItem modifyBook = new MenuItem("Modify Book");
        modifyBook.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+B"));
        modifyBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("ModifyBook", null);
            }
        });

        MenuItem deleteBook = new MenuItem("Delete Book");
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        deleteBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Delete Book Pressed");
            }
        });

        MenuItem listBook = new MenuItem("List Books Checked Out");
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        listBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("List Book Pressed");
            }
        });

        MenuItem checkOutBook = new MenuItem("Check out Book");
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        checkOutBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Check out Book Pressed");
            }
        });

        MenuItem checkInBook = new MenuItem("Check in Book");
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        checkInBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Check In Book Pressed");
            }
        });

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        menuBook.getItems().addAll(addBook,modifyBook,deleteBook, separatorMenuItem, listBook, checkInBook, checkOutBook);

        // --- Menu Worker
        Menu menuWorker = new Menu("Worker");
        MenuItem addWorker = new MenuItem("Add Woker");
        addWorker.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
        addWorker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("AddWorker", null);
            }
        });

        MenuItem modifyWorker= new MenuItem("Modify Worker");
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        modifyWorker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Modify Worker Pressed");
            }
        });

        MenuItem deleteWorker = new MenuItem("Delete Worker");
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        deleteWorker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Delete Worker Pressed");
            }
        });

        menuWorker.getItems().addAll(addWorker, modifyWorker, deleteWorker);

        // --- Menu Student
        Menu menuStudent = new Menu("Student Borrower");
        MenuItem addStudent = new MenuItem("Add Student Borrower");
        addStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        addStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("AddStudentBorrower", null);
            }
        });

        MenuItem modifyStudent = new MenuItem("Modify Student Borrower");
//        modifyStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        modifyStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Modify Student Pressed");
            }
        });

        MenuItem deleteStudent = new MenuItem("Delete Student Borrower");
//        addStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        deleteStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Delete Student Pressed");
            }
        });

        menuStudent.getItems().addAll(addStudent, modifyStudent, deleteStudent);


        menuBar.getMenus().addAll(menuBook, menuWorker, menuStudent);
        return menuBar;
    }

    private void addBook() {

    }

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("ChangeView")){
            System.out.println("working");
            swapContentView((View) value);
        }
    }

    private void swapContentView(View content){
        if(mainView != null){
            container.getChildren().remove(mainView);
        }
        mainView = content;
        mainView.getChildren().get(0).getStyleClass().add("page");
        container.getChildren().add(mainView);

    }
}
