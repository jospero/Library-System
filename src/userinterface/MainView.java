package userinterface;

import Utilities.Utilities;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import model.WorkerHolder;

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
        Menu menuBook = new Menu(Utilities.getStringLang("book"));
        MenuItem addBook = new MenuItem(Utilities.getStringLang("add_book"));
        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+B"));
        addBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Add", "Book");
            }
        });

        MenuItem modifyBook = new MenuItem(Utilities.getStringLang("mod_book"));
        modifyBook.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+B"));
        modifyBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Modify", "Book");
            }
        });

        MenuItem deleteBook = new MenuItem(Utilities.getStringLang("del_book"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        deleteBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Delete", "Book");
            }
        });

        MenuItem listBook = new MenuItem(Utilities.getStringLang("list_check_outs"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        listBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("List Book Pressed");
            }
        });

        MenuItem checkOutBook = new MenuItem(Utilities.getStringLang("check_out_book"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        checkOutBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Check out Book Pressed");
            }
        });

        MenuItem checkInBook = new MenuItem(Utilities.getStringLang("check_in_book"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        checkInBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                System.out.println("Check In Book Pressed");
            }
        });

        SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        menuBook.getItems().addAll(addBook,modifyBook,deleteBook, separatorMenuItem, listBook, checkInBook, checkOutBook);
        menuBar.getMenus().add(menuBook);
        // --- Menu Worker
        if(((WorkerHolder)myModel.getState("WorkerHolder")).getState("Credentials").equals("Administrator")) {
            Menu menuWorker = new Menu(Utilities.getStringLang("worker"));
            MenuItem addWorker = new MenuItem(Utilities.getStringLang("add_worker"));
            addWorker.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
            addWorker.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                    myModel.stateChangeRequest("Add", "Worker");
                }
            });

            MenuItem modifyWorker = new MenuItem(Utilities.getStringLang("mod_worker"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
            modifyWorker.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                    myModel.stateChangeRequest("Modify", "Worker");
                }
            });

            MenuItem deleteWorker = new MenuItem(Utilities.getStringLang("del_worker"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
            deleteWorker.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                    myModel.stateChangeRequest("Delete", "Worker");
                }
            });

            menuWorker.getItems().addAll(addWorker, modifyWorker, deleteWorker);
            menuBar.getMenus().add(menuWorker);
        }
        // --- Menu Student
        Menu menuStudent = new Menu(Utilities.getStringLang("sb"));
        MenuItem addStudent = new MenuItem(Utilities.getStringLang("add_sb"));
        addStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        addStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Add", "StudentBorrower");
            }
        });

        MenuItem modifyStudent = new MenuItem(Utilities.getStringLang("mod_sb"));
        modifyStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        modifyStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Modify", "StudentBorrower");
            }
        });

        MenuItem deleteStudent = new MenuItem(Utilities.getStringLang("del_sb"));
        deleteStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        deleteStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Delete", "StudentBorrower");
            }
        });

        menuStudent.getItems().addAll(addStudent, modifyStudent, deleteStudent);


        menuBar.getMenus().add( menuStudent);
        return menuBar;
    }

    private void addBook() {

    }

    @Override
    public void updateState(String key, Object value) {
        if(key.equals("ChangeView")){
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
//        ((View)mainView).ft.playFromStart();

    }
}
