package userinterface;

import Utilities.Utilities;
import com.jfoenix.controls.JFXSnackbar;
import impresario.IModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import model.WorkerHolder;

/**
 * Created by Sammytech on 3/5/17.
 */
public class MainView extends View {
    private Group mainView;
    private VBox container;
    private ScrollPane sp;
    private JFXSnackbar bar;
    public MainView(IModel model) {
        super(model, "MainView");
        String css = this.getClass().getResource("/resources/css/main.css").toExternalForm();
        this.getStylesheets().add(css);
        container = new VBox();
//        container.setPrefWidth(WIDTH);

        HBox menu = new HBox();
        menu.setId("menuContainer");
        menu.setMaxWidth(Double.MAX_VALUE);
        menu.setPrefHeight(40);
        final ImageView imageView = new ImageView(
                new Image(this.getClass().getClassLoader().getResourceAsStream("resources/images/logout.png"))
        );
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(20);

        Button logoutButton = new Button(Utilities.getStringLang("logout_btn"), imageView);
        logoutButton.setContentDisplay(ContentDisplay.LEFT);
        logoutButton.setFocusTraversable(false);
        logoutButton.setId("logout");
        MenuBar menuBar = createMenu();
        menuBar.prefHeightProperty().bind(menu.heightProperty());
        menuBar.setId("menubar");
menuBar.setPrefWidth(40);
logoutButton.setPrefHeight(40);
        menu.getChildren().addAll(menuBar, logoutButton);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        container.getChildren().add(menu);

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                myModel.stateChangeRequest("Logout", null);
            }
        });

        sp = new ScrollPane();
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        swapContentView((View) myModel.getState("ChangeView"));


//        sp.getStyleClass().add("page-scroll");




//        container.getChildren().add(sp);

        // MessageView
        AnchorPane pane = new AnchorPane();
        pane.setPrefHeight(60);
        pane.setPrefWidth(container.getPrefWidth());
        bar = new JFXSnackbar(pane);
        pane.setId("message");
        pane.setPickOnBounds(false);
//        container.getChildren().add(pane);
//        container.setStyle("-fx-background-color: #2aff52");
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(sp,  pane);
        container.getChildren().add(stackPane);
        getChildren().add(container);

        myModel.subscribe("ChangeView", this);
        myModel.subscribe("DisplayError",this);
    }

    private MenuBar createMenu(){
        MenuBar menuBar = new MenuBar();
        String style = "-fx-pref-height: 40; -fx-text-fill:#ffffff;";
        // --- Menu Book
        Menu menuBook = new Menu(Utilities.getStringLang("book"));
        menuBook.styleProperty().setValue(style);
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
                myModel.stateChangeRequest("ListCheckOuts", "Book");
                System.out.println("List Book Pressed");
            }
        });

        MenuItem checkOutBook = new MenuItem(Utilities.getStringLang("check_out_book"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        checkOutBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("CheckOut", "Book");
                System.out.println("Check out Book Pressed");
            }
        });

        MenuItem checkInBook = new MenuItem(Utilities.getStringLang("check_in_book"));
//        addBook.setAccelerator(KeyCombination.keyCombination("Ctrl+A"));
        checkInBook.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("CheckIn", "Book");
                System.out.println("Check In Book Pressed");
            }
        });

        //SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
        menuBook.getItems().addAll(addBook,modifyBook,deleteBook);
        menuBar.getMenus().add(menuBook);


        Menu menuTrans = new Menu(Utilities.getStringLang("transaction"));
        menuTrans.styleProperty().setValue(style);
        menuTrans.getItems().addAll(listBook, checkInBook, checkOutBook);
        menuBar.getMenus().add(menuTrans);


        // --- Menu Worker
        String cred = (String) ((WorkerHolder)myModel.getState("WorkerHolder")).getState("Credentials");
        if(cred.toLowerCase().trim().equals("administrator") || cred.trim().toLowerCase().equals("administrateur")) {
            Menu menuWorker = new Menu(Utilities.getStringLang("worker"));
            menuWorker.styleProperty().setValue(style);
            MenuItem addWorker = new MenuItem(Utilities.getStringLang("add_worker"));
            addWorker.setAccelerator(KeyCombination.keyCombination("Ctrl+W"));
            addWorker.setOnAction(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                    myModel.stateChangeRequest("Add", "Worker");
                }
            });

            MenuItem modifyWorker = new MenuItem(Utilities.getStringLang("mod_worker"));
            modifyWorker.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+W"));
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
        menuStudent.styleProperty().setValue(style);
        MenuItem addStudent = new MenuItem(Utilities.getStringLang("add_sb"));
        addStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        addStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Add", "StudentBorrower");
            }
        });

        MenuItem modifyStudent = new MenuItem(Utilities.getStringLang("mod_sb"));
        modifyStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+S"));
        modifyStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Modify", "StudentBorrower");
            }
        });

        MenuItem deleteStudent = new MenuItem(Utilities.getStringLang("del_sb"));
        deleteStudent.setAccelerator(KeyCombination.keyCombination("Ctrl+Alt+S"));
        deleteStudent.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
//                vbox.setVisible(false);
                myModel.stateChangeRequest("Delete", "StudentBorrower");
            }
        });

        menuStudent.getItems().addAll(addStudent, modifyStudent, deleteStudent);



        menuBar.getMenus().add( menuStudent);

        menuBar.setMaxWidth(Double.MAX_VALUE);
        return menuBar;
    }


    @Override
    public void updateState(String key, Object value) {
        if(key.equals("ChangeView")){
            swapContentView((View) value);
        } else if(key.equals("DisplayError")){
            System.out.println(value);
            displayErrorMessage((String) value);
        }
    }

    private void swapContentView(View content){
//        if(mainView != null){
//            container.getChildren().remove(mainView);
//        }
        mainView = content;
        mainView.getChildren().get(0).getStyleClass().add("page");
        sp.getStyleClass().add("page-scroll");
        sp.setContent(mainView);
//        sp.setPrefHeight();

        sp.setFitToWidth(true);
        sp.setFitToHeight(true);

    }

    /**
     * Display error message
     */
    //----------------------------------------------------------
    public void displayErrorMessage(String message)
    {
        bar.show(message, 2500);
    }
}
