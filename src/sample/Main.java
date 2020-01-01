/*
* A SCHOOL REGISTER PROGRAM THAT HELPS THE TEACHER MANAGE A LARGE OR SMALL CLASS OF STUDENTS - FUNCTIONS INCLUDE; AN ATTENDANCE REGISTER,
* GRADES RECORDING AND STORAGE, REPORT CARD MAKING AND SOME OTHER STUFF I MIGHT FIGURE OUT ALONG THE WAY*/

package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.DefaultStringConverter;

import javax.script.Bindings;
import java.io.File;
import java.sql.*;
import java.util.Calendar;
import java.util.Collections;

public class Main extends Application {

    //OUT-CLASS DEFINING VARIABLES
    Button btn1, login;
    Scene scene1;
    Label label1, label2;
    Stage window, initWindow;
    TreeView<String> tree;
    Statement stmt, stmt2, stmt3;
    boolean show;
    private final TableView<Grades> table = new TableView<>();
    private final ObservableList<Student> data = FXCollections.observableArrayList();
    private final ObservableList<Student> data4 = FXCollections.observableArrayList();
    final TableView<Student> table2 = new TableView<>();
    Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
        @Override public TableCell call(TableColumn p) {return new EditingCell();}};
    VBox vbox, center,centerBlock, showing;

    final TableView<Grades> table5 = new TableView<>();
    final ObservableList<Grades> data1 = FXCollections.observableArrayList();
    BorderPane every;
    Connection connection;
    TextField classE, subE, classEnt, dateEnt, subject, className, subName, className1, subName1 ;
    Calendar now = Calendar.getInstance();
    ObservableList<String> cbValues = FXCollections.observableArrayList("Present", "Absent", "Late", "Sick", "A. Absent");
    String attendance, classNamed, myText; int testNUm;
    private Pane splashLayout;
    private ProgressBar loadProgress;
    private Label progressText;
    private Stage mainStage;
    private static final int SPLASH_WIDTH = 700;
    private static final int SPLASH_HEIGHT = 227;
    private final TableView<Register> table1 = new TableView<>();
    private final ObservableList<Register> data2 = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        initializeDB();
        //INITIALIZE DATABASE
        initWindow = primaryStage;
        initWindow.getIcons().add(new Image(Main.class.getResourceAsStream( "ico.png" )));

        //INITIALIZE THE FIRST LOG IN

        final Task<ObservableList<String>> friendTask = new Task<ObservableList<String>>() {
            @Override
            protected ObservableList<String> call() throws InterruptedException {
                ObservableList<String> foundFriends =
                        FXCollections.<String>observableArrayList();

                updateMessage("Loading essential files . . .");
                for (int i = 0; i < 15; i++) {
                    Thread.sleep(400);
                    updateProgress(i + 1, 15);
                    //updateMessage("Finding friends . . . found " + nextFriend);
                }
                Thread.sleep(400);
                updateMessage("loading done!");

                return foundFriends;
            }
        };

        showSplash(initWindow, friendTask, () -> FirstLogIn());
        new Thread(friendTask).start();
    }

    //INITIALIZES THE SPLASH WINDOW
    @Override
    public void init() {
        File file = new File("C:\\Users\\ITWABI\\IdeaProjects\\FirstGUIapp\\src\\sample\\pic14.jpg");
        Image image = new Image(file.toURI().toString());
        ImageView splash = new ImageView(image);


        loadProgress = new ProgressBar();
        loadProgress.setPrefWidth(SPLASH_WIDTH + 20);
        progressText = new Label("Will find friends for peanuts . . .");
        splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress, progressText);
        progressText.setAlignment(Pos.CENTER);
        splashLayout.setStyle(
                "-fx-padding: 5; " +
                        "-fx-background-color: cornsilk; " +
                        "-fx-border-width:5; " +
                        "-fx-border-color: linear-gradient( to bottom, dodgerblue, darkviolet);"
        );
        splashLayout.setEffect(new DropShadow());
    }


    //THE SPLASH WINDOW
    private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
        progressText.textProperty().bind(task.messageProperty());
        loadProgress.progressProperty().bind(task.progressProperty());
        task.stateProperty().addListener((observableValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                loadProgress.progressProperty().unbind();
                loadProgress.setProgress(1);
                initStage.toFront();
                FadeTransition fadeSplash = new FadeTransition(Duration.seconds(1.2), splashLayout);
                fadeSplash.setFromValue(1.0);
                fadeSplash.setToValue(0.0);
                fadeSplash.setOnFinished(actionEvent -> initStage.hide());
                fadeSplash.play();

                initCompletionHandler.complete();
            } // todo add code to gracefully handle other task states.
        });

        Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
        final Rectangle2D bounds = Screen.getPrimary().getBounds();
        initStage.setScene(splashScene);
        initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - SPLASH_WIDTH / 2);
        initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - SPLASH_HEIGHT / 2);
        initStage.initStyle(StageStyle.TRANSPARENT);
        //initStage.setAlwaysOnTop(true);
        initStage.show();
    }

    //THE SPLASH HANDLER
    public interface InitCompletionHandler {
        void complete();
    }

    //THE FIRST LOGIN PAGE
    public void FirstLogIn(){

        //CREATE THE MAIN WINDOW
        window = new Stage();
        window.setTitle("Teacher log-In");
        window.setWidth(1300);
        window.setHeight(750);


        // ALL THE FORM WIDGETS DEFINED HERE - LABELS, THE NAME AND PASSWORD ENTRY AND THE LOG-IN BUTTON
        label1 = new Label("SCHOOL REGISTER APP");
        label1.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 30));
        label1.setAlignment(Pos.TOP_CENTER);label1.setStyle("-fx-text-fill: beige;");
        label1.setPadding(new Insets(10, 10, 100, 10));

        label2 = new Label("TEACHER LOG-IN");
        label2.setFont(new Font("century gothic bold", 20));
        label2.setTextAlignment(TextAlignment.CENTER);
        label2.setPadding(new Insets(10, 5, 50, 5));
        label2.setStyle("-fx-text-fill: darkcyan;");

        Label nameL = new Label("Name: ");
        nameL.setFont(new Font("century gothic bold", 15));

        TextField nameE = new TextField();
        nameE.setMaxWidth(300);
        nameE.setPromptText("enter Username");

        Label passL = new Label("Password: ");
        passL.setFont(new Font("century gothic bold", 15));

        PasswordField passEnt = new PasswordField();
        passEnt.setPromptText("enter your teacher password");
        passEnt.setMaxWidth(300);

        login = new Button("LOG-IN");
        login.setMinWidth(100);
        login.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        login.setOnMouseEntered(e -> login.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        login.setOnMouseExited(e -> login.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));
        login.setOnAction(e -> {
            if (nameE.getText().isEmpty() || passEnt.getText().isEmpty()){
                Confirmation.display("Error!", "the fields cannot be empty!!");
            }else{check(nameE.getText(), passEnt.getText());}
            nameE.clear();passEnt.clear();
        });

        Button user = new Button("new User");
        user.setMinWidth(100);
        user.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        user.setOnMouseEntered(e->user.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        user.setOnMouseExited(e -> user.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));
        user.setOnAction(e -> CreateUser());

        HBox mine = new HBox(20, login, user);
        mine.setAlignment(Pos.CENTER);

        //CREATE THE V_BOX TO ADD ALL THE WIDGETS
        VBox vLayout = new VBox();vLayout.setId("enter");
        vLayout.getChildren().addAll(label1, label2, nameL, nameE, passL, passEnt, mine);
        vLayout.setSpacing(10);
        vLayout.setAlignment(Pos.TOP_CENTER);
        vLayout.setStyle("-fx-padding: 10;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-insets: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: darkcyan;"
        );
        vLayout.setMaxHeight(500);
        vLayout.setMaxWidth(600);

        //CREATE THE BORDER-PANE TO CARRY THE V_BOX LAYOUT OVERALL
        BorderPane layout = new BorderPane();
        layout.setCenter(vLayout);
        layout.setId("main");

        // SCENES DEFINED HERE
        scene1 = new Scene(layout, 300, 350);
        scene1.getStylesheets().add(getClass().getResource("my.css").toExternalForm());
        window.setScene(scene1);
        window.show();
    }

    //CHECK VALIDITY OF A USER
    public void check(String user, String pass){

        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Public\\Documents\\users.db");

            // Create a statement
            stmt3 = connection.createStatement();
        }
        catch (Exception ex) {
            Confirmation.display("error", ex.getMessage());}

        try {
            String queryString ="select username, password from users ";

            ResultSet rset = stmt3.executeQuery(queryString);

            while (rset.next()){
                String userName = rset.getString("username");
                String passes = rset.getString("password");

                int i = 0;
                while (pass.equals(passes) && (i < 1)) {
                    if (userName.equals(user)) {
                        createUserDB(userName, passes);
                        Menu();
                    }else {
                        Confirmation.display("Error!", "wrong username or password!!!!");
                    }
                    i++;
                }
            }

        }catch (SQLException ex) {
            Confirmation.display("error", ex.getMessage());}
    }

    //CREATE USER DATABASE BASED ON CREDENTIALS ENTERED
    private void createUserDB(String username, String password) {
        String login = username+"-"+password;
        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");


            // Establish a connection
            Connection connection =
                    DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Public\\Documents\\"+ login + ".db");


            // Create a statement
            stmt = connection.createStatement();
            stmt2 = connection.createStatement();
            stmt3 = connection.createStatement();
        }
        catch (Exception ex) {
            Confirmation.display("error", ex.getMessage());}
    }

    //CREATE A NEW USER
    public void CreateUser(){
        Label label1 = new Label("SCHOOL REGISTER APP");
        label1.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 30));
        label1.setAlignment(Pos.TOP_CENTER);label1.setStyle("-fx-text-fill: beige;");
        label1.setPadding(new Insets(10, 10, 100, 10));

        Label label2 = new Label("CREATE NEW USER");
        label2.setFont(new Font("century gothic bold", 20));
        label2.setTextAlignment(TextAlignment.CENTER);
        label2.setPadding(new Insets(10, 5, 50, 5));
        label2.setStyle("-fx-text-fill: darkcyan;");

        Label fullname = new Label("FullName: ");
        fullname.setFont(new Font("century gothic bold", 15));

        TextField fullNameT = new TextField();
        fullNameT.setMaxWidth(300);
        fullNameT.setPromptText("enter fullname");

        Label nameL = new Label("UserName: ");
        nameL.setFont(new Font("century gothic bold", 15));

        TextField nameE = new TextField();
        nameE.setMaxWidth(300);
        nameE.setPromptText("enter Username");

        Label passL = new Label("Password: ");
        passL.setFont(new Font("century gothic bold", 15));

        PasswordField passEnt = new PasswordField();
        passEnt.setPromptText("enter your teacher password");
        passEnt.setMaxWidth(300);

        Button login = new Button("CREATE");
        login.maxWidth(400);
        login.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        login.setOnMouseEntered(e -> login.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        login.setOnMouseExited(e -> login.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);"));
        login.setOnAction(e -> {
            if (fullNameT.getText().isEmpty() || nameE.getText().isEmpty() || passEnt.getText().isEmpty()){
                Confirmation.display("Error!!", "The fields cannot be empty.");
            }else {addUser(fullNameT.getText(), nameE.getText(), passEnt.getText());}
        });

        Button back = new Button("BACK");
        back.maxWidth(400);
        back.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        back.setOnMouseEntered(e -> back.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        back.setOnMouseExited(e -> back.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);"));
        back.setOnAction(e -> window.setScene(scene1));

        HBox btns = new HBox(15, login, back);
        btns.setAlignment(Pos.BASELINE_CENTER);

        //CREATE THE V_BOX TO ADD ALL THE WIDGETS
        VBox vLayout = new VBox();vLayout.setId("user");
        vLayout.getChildren().addAll(label1, label2, fullname, fullNameT, nameL, nameE, passL, passEnt, btns);
        vLayout.setSpacing(10);
        vLayout.setAlignment(Pos.TOP_CENTER);
        vLayout.setStyle("-fx-padding: 10;" +
                        "-fx-border-style: solid inside;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-insets: 5;" +
                        "-fx-border-radius: 5;" +
                        "-fx-border-color: darkcyan;"
        );
        vLayout.setMaxHeight(530);
        vLayout.setMaxWidth(600);

        //CREATE THE BORDER-PANE TO CARRY THE V_BOX LAYOUT OVERALL
        BorderPane layout = new BorderPane();
        layout.setCenter(vLayout);
        layout.setId("main");

        // SCENES DEFINED HERE
        Scene scene2 = new Scene(layout, 300, 350);
        scene2.getStylesheets().add(getClass().getResource("my.css").toExternalForm());
        window.setScene(scene2);
    }

    //ADD NEW USER TO THE USERS DATABASE
    public void addUser(String fullname, String username, String password){

        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Public\\Documents\\users.db");

            // Create a statement
            stmt3 = connection.createStatement();
        }
        catch (Exception ex) {
            ex.printStackTrace();}

        try {
            String queryString ="INSERT INTO users(fullname, username, password)" +
                    "VALUES ( '"+fullname +"', '"+ username +"', '"+ password + "');";

            stmt3.executeUpdate(queryString);
            stmt3.close();

        }catch (SQLException ex) {
            Confirmation.display("error", ex.getMessage());}

        createUserDB(username, password);
        Confirmation.display("success", "user created!!!");
        window.setScene(scene1);
    }

    //A METHOD THAT CONTAINS THE OUTLOOK OF THE APP AFTER THE LOGIN
    public void Menu(){
        show = false;
        //WIDGETS FOR THE TOP-BAR
        Label another1 = new Label("TEACHERS CLASS MANAGER");
        another1.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 30));
        another1.setStyle("-fx-text-fill: navy;");

        //WIDGETS FOR THE NAVIGATION MENU
        Label another = new Label("NAVIGATION MENU");
        another.setFont(Font.font("times new roman",FontWeight.BOLD,18));
        another.setStyle("-fx-text-fill: navy;");

        Button btn3 = new Button("Logout");
        btn3.setStyle("-fx-background-color: powderblue;" +
                "-fx-text-fill: navy;");
        btn3.setPadding(new Insets(2, 40, 2, 45));

        //THE TREE-VIEW FOR THE NAV WINDOW
        TreeItem<String> root, attendance, grades, home, aboutUs, logOut, help;

        //Root
        root = new TreeItem<>();
        root.setExpanded(false);

        //home selection
        home = makeBranch("HOME", root);

        //Record attendance
        attendance = makeBranch("ATTENDANCE", root);
        makeBranch("Record Attendance", attendance);
        makeBranch("Check Records", attendance);

        //Record grades
        grades = makeBranch("STUDENT GRADES", root);
        makeBranch("Record new grades", grades);
        makeBranch("Check student grades", grades);

        aboutUs = makeBranch("ABOUT US", root);

        help = makeBranch("HELP", root);

        logOut = makeBranch("LOGOUT", root);

        //Create the tree and hide the main Root
        tree = new TreeView<>(root);
        tree.setShowRoot(false);
        tree.setMinWidth(250);
        tree.setStyle("-fx-fill: deepskyblue");
        tree.getStyleClass().add("myTree");


        HBox topBar = new HBox();
        topBar.getChildren().addAll( another1);
        topBar.setMinHeight(40);topBar.setMinWidth(100);
        topBar.setAlignment(Pos.CENTER);
        topBar.setStyle("-fx-background-color: linear-gradient(#e6f4f6, #eeeeee);" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 0.5;" +
                "-fx-border-insets: 1.5;" +
                "-fx-border-color: blue;");

        VBox leftBar = new VBox();
        leftBar.getChildren().addAll(another, tree);
        leftBar.setMinHeight(300);
        leftBar.setAlignment(Pos.TOP_CENTER);
        leftBar.setPadding(new Insets(10,10,10,10));
        leftBar.setStyle("-fx-background-color: #eeeeee;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 0.5;" +
                "-fx-border-insets: 1.5;" +
                "-fx-border-color: blue;");
        leftBar.setSpacing(10);


        centerBlock = new VBox();
        centerBlock.setStyle("-fx-background-color: linear-gradient(lightblue, darkcyan) ;"+
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 0.5;" +
                "-fx-border-insets: 1.5;" +
                "-fx-border-color: blue;" );
        centerBlock.setPadding(new Insets(10, 10, 10, 10));
        centerBlock.setAlignment(Pos.CENTER);

        center = new VBox();

        tree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if ((newValue != null) && !show) {
                if (newValue.getValue().equals("HOME")) {
                    Homepage();
                }
                else if (newValue.getValue().equals("Record Attendance")){
                    classRegister();
                }
                else if (newValue.getValue().equals("Check Records")){
                    CheckAttLog();
                }
                else if (newValue.getValue().equals("ABOUT US")){
                    AboutUsMenu();
                }
                else if(newValue.getValue().equals("Record new grades")){
                    EnterGradesLog();
                }
                else if(newValue.getValue().equals("Check student grades")){
                    checkGradesLog();
                }
                else if(newValue.getValue().equals("ATTENDANCE")){
                    AttendanceMenu();
                }
                else if(newValue.getValue().equals("STUDENT GRADES")){
                    GradesMenu();
                }
                else if(newValue.getValue().equals("LOGOUT")){
                    boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
                    if (result){
                        window.setScene(scene1);
                    }else{assert true;}
                }
                else{
                    HelpMenu();
                }
            }
        });

        every = new BorderPane();
        every.setTop(topBar);
        every.setLeft(leftBar);
        every.setCenter(Homepage());

        Scene menuScene = new Scene(every, 700, 500);
        menuScene.getStylesheets().add(getClass().getResource("my.css").toExternalForm());
        window.setScene(menuScene);
        window.show();
    }

    //CREATE NAVIGATION BRANCHES
    public TreeItem<String> makeBranch(String title, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(title);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    //THE HOMEPAGE
    public VBox Homepage(){

        Label well = new Label("SCHOOL APPLICATION!!");
        well.setFont(Font.font("century gothic", FontWeight.BOLD, 32));
        well.setStyle("-fx-text-fill: beige;");

        String intro = "Hi Teachers.";
        String second = " this application is going to help you keep the records of your class,";
        String third  = "you can; record the student attendance, record student grades and check all";
        String fourth = " the records you have saved. I hope this app satisfies you.";
        String fifth = " Enjoy teaching!!!";

        Label myText = new Label();
        myText.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText.setText(intro);
        myText.setStyle("-fx-text-fill: beige;");

        Label myText2 = new Label(second);Label myText3 = new Label(third);
        Label myText4 = new Label(fourth);Label myText5 = new Label(fifth);

        myText2.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText2.setStyle("-fx-text-fill: beige;");

        myText3.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText3.setStyle("-fx-text-fill: beige;");

        myText4.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText4.setStyle("-fx-text-fill: beige;");

        myText5.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText5.setStyle("-fx-text-fill: beige;");

        Label lbl = new Label("GET STARTED");
        lbl.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        lbl.setStyle("-fx-text-fill: beige;" +
                "-fx-background-color: darkgreen;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: white;");
        HBox get = new HBox(lbl);
        get.setAlignment(Pos.CENTER);

        Button btn1 = new Button("New Class");btn1.setMinWidth(200);btn1.setMinHeight(60);
        btn1.setStyle("-fx-background-color: red; -fx-text-fill: beige;");
        btn1.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));
        btn1.setId("redButton");
        btn1.setOnMouseEntered( e -> btn1.setStyle("-fx-background-color: orangered;" ));
        btn1.setOnMouseExited(e -> btn1.setStyle("-fx-background-color: red;" ));
        btn1.setOnAction(e -> {
            classNamed = Confirmation.getAnswer("new class", "please enter the class name: ");
            MakeClass();
        });

        Button btn2 = new Button("Attendance");btn2.setMinWidth(200);btn2.setMinHeight(60);
        btn2.setStyle("-fx-background-color: lime; -fx-text-fill: beige;");
        btn2.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));btn2.setId("yellow");
        btn2.setOnMouseEntered( e -> btn2.setStyle("-fx-background-color: limegreen;" ));
        btn2.setOnMouseExited(e -> btn2.setStyle("-fx-background-color: lime;" ));
        btn2.setOnAction(e -> AttendanceMenu());

        Button btn3 = new Button("Grades");btn3.setMinWidth(200);btn3.setMinHeight(60);
        btn3.setStyle("-fx-background-color: darkturquoise; -fx-text-fill: beige;");
        btn3.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));btn3.setId("green");
        btn3.setOnMouseEntered( e -> btn3.setStyle("-fx-background-color: cyan;" ));
        btn3.setOnMouseExited(e -> btn3.setStyle("-fx-background-color: darkturquoise;" ));
        btn3.setOnAction(e -> GradesMenu());

        HBox buttons = new HBox(20, btn1, btn2, btn3);
        buttons.setAlignment(Pos.BASELINE_CENTER);

        VBox myTex = new VBox(15, well, myText, myText2, myText3, myText4, myText5);
        myTex.setAlignment(Pos.TOP_CENTER);

        VBox home = new VBox(40);
        home.setId("homePage");
        home.getChildren().addAll(myTex, get, buttons);
        home.setAlignment(Pos.TOP_CENTER);
        home.setPadding(new Insets(40,10,10,10));

        every.setCenter(home);

        return home;
    }

    //METHOD INITIALIZES THE CONNECTION TO THE DATABASE
    private void initializeDB() {
        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\Public\\Documents\\users.db");

            // Create a statement
            stmt = connection.createStatement();
            stmt2 = connection.createStatement();
            stmt3 = connection.createStatement();
        }
        catch (Exception ex) {
            ex.printStackTrace();}


        try {
            String queryString ="CREATE TABLE IF NOT EXISTS users" +
                        "(fullname char(30)," +
                        "username char(16) NOT NULL," +
                        "password char(16)," +
                        "PRIMARY KEY (username));";

            stmt2.executeUpdate(queryString);
            stmt2.close();

        }catch (SQLException ex) {
            Confirmation.display("error", ex.getMessage());}
    }

    //THE MENU THAT INTRODUCES THE ATTENDANCE SECTION
    public void AttendanceMenu(){

        Label well = new Label("ATTENDANCE RECORDS!!");
        well.setFont(Font.font("century gothic", FontWeight.BOLD, 32));
        well.setStyle("-fx-text-fill: beige;");

        String intro = "Hello there!.";
        String second = " this part of the application will enable you to record your class attendance.";
        String third  = "you can; record the student attendances, save them and be able to check the records.";
        String fourth = " you may make a new class, if you so wish, and record its attendance!!";
        String fifth = " Enjoy teaching!!!";

        Label myText = new Label();
        myText.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText.setText(intro);
        myText.setStyle("-fx-text-fill: beige;");

        Label myText2 = new Label(second);Label myText3 = new Label(third);
        Label myText4 = new Label(fourth);Label myText5 = new Label(fifth);

        myText2.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText2.setStyle("-fx-text-fill: beige;");

        myText3.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText3.setStyle("-fx-text-fill: beige;");

        myText4.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText4.setStyle("-fx-text-fill: beige;");

        myText5.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText5.setStyle("-fx-text-fill: beige;");

        Label lbl = new Label("GET STARTED");
        lbl.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        lbl.setStyle("-fx-text-fill: beige;" +
                "-fx-background-color: darkgreen;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: white;");
        HBox get = new HBox(lbl);
        get.setAlignment(Pos.CENTER);

        Button btn1 = new Button("New Class");btn1.setMinWidth(200);btn1.setMinHeight(60);
        btn1.setStyle("-fx-background-color: red; -fx-text-fill: beige;");
        btn1.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));
        btn1.setId("redButton");
        btn1.setOnMouseEntered( e -> btn1.setStyle("-fx-background-color: orangered;" ));
        btn1.setOnMouseExited(e -> btn1.setStyle("-fx-background-color: red;" ));
        btn1.setOnAction(e -> {
            classNamed = Confirmation.getAnswer("new class", "please enter the class name: ");
            MakeClass();
        });

        Button btn2 = new Button("new class Attendance");btn2.setMinWidth(200);btn2.setMinHeight(60);
        btn2.setStyle("-fx-background-color: lime; -fx-text-fill: beige;");
        btn2.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));btn2.setId("yellow");
        btn2.setOnMouseEntered( e -> btn2.setStyle("-fx-background-color: limegreen;" ));
        btn2.setOnMouseExited(e -> btn2.setStyle("-fx-background-color: lime;" ));
        btn2.setOnAction( e -> classRegister() );


        Button btn3 = new Button("check class Attendance");btn3.setMinWidth(200);btn3.setMinHeight(60);
        btn3.setStyle("-fx-background-color: darkturquoise; -fx-text-fill: beige;");
        btn3.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));btn3.setId("green");
        btn3.setOnMouseEntered( e -> btn3.setStyle("-fx-background-color: cyan;" ));
        btn3.setOnMouseExited(e -> btn3.setStyle("-fx-background-color: darkturquoise;"));
        btn3.setOnAction(e -> CheckAttLog() );

        HBox buttons = new HBox(20, btn1, btn2, btn3);
        buttons.setAlignment(Pos.BASELINE_CENTER);

        VBox myTex = new VBox(15, well, myText, myText2, myText3, myText4, myText5);
        myTex.setAlignment(Pos.TOP_CENTER);

        VBox home = new VBox(40);
        home.setId("attendancePage");
        home.getChildren().addAll(myTex, get, buttons);
        home.setAlignment(Pos.TOP_CENTER);
        home.setPadding(new Insets(40,10,10,10));

        every.setCenter(home);


    }

    //RECORD CLASS ATTENDANCE LOG IN PAGE
    public void classRegister(){
        label2 = new Label("RECORD CLASS ATTENDANCE");
        label2.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 26));
        label2.setTextAlignment(TextAlignment.CENTER);
        label2.setAlignment(Pos.TOP_CENTER);
        label2.setPadding(new Insets(10, 5, 50, 5));
        label2.setStyle("-fx-text-fill: black;");

        Label subL = new Label("Enter subject: ");
        subL.setFont(new Font("century gothic bold", 20));
        subE = new TextField();
        subE.setMaxWidth(500);
        subE.setPromptText("e.g. BIOLOGY");
        HBox one = new HBox(8, subL, subE);
        one.setMinWidth(500);
        one.setAlignment(Pos.CENTER);

        Label classL = new Label("Enter class: ");
        classL.setFont(new Font("century gothic bold", 20));
        classE = new TextField();
        classE.setPromptText("e.g. form1");
        classE.setMaxWidth(500);
        HBox two = new HBox(31,classL, classE);
        two.setMinWidth(500);two.setAlignment(Pos.CENTER);

        VBox in = new VBox(28, one, two);
        in.setAlignment(Pos.CENTER);

        Button go = new Button("GO");
        go.setMinWidth(60);
        go.setStyle("-fx-background-color: linear-gradient(darkkhaki, khaki);");
        go.setOnMouseEntered(e -> go.setStyle("-fx-background-color: linear-gradient(moccasin, tan);"));
        go.setOnMouseExited(e -> go.setStyle("-fx-background-color: linear-gradient(darkkhaki, khaki);"));
        go.setOnAction(e -> {
            if (subE.getText().isEmpty() || classE.getText().isEmpty()){
                Confirmation.display("Error!", "the fields cannot be empty!!");}
            else{showStudents();}
        });

        //CREATE THE V_BOX TO ADD ALL THE WIDGETS
        vbox = new VBox();
        vbox.getChildren().addAll(label2, in, go);
        vbox.setSpacing(30);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-background-color: transparent;"+
                "-fx-border-radius: 6;");
        vbox.setMaxWidth(450);vbox.setMinHeight(350);
        VBox mine = new VBox();
        mine.getChildren().addAll(vbox);
        mine.setAlignment(Pos.CENTER);
        mine.setId("AttendanceLogIn");


        every.setCenter(mine);
        //scrollPane.setPadding(new Insets(150,100,100,250));
    }

    //THE RECORD ATTENDANCE OUTLOOK PAGE
    public void showStudents() {
        //A TABLE WIDGET TO SHOW THE STUDENTS

        table2.setEditable(true);

        TableColumn<Student, String> IdCol = new TableColumn<>("Student No.");
        IdCol.setMinWidth(150);
        IdCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        IdCol.setCellFactory(TextFieldTableCell.forTableColumn());
        IdCol.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setId(myValue);
        });

        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setMinWidth(150);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit(( TableColumn.CellEditEvent<Student, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setFirstName(myValue);
        });

        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(( TableColumn.CellEditEvent<Student, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setLastName(myValue);
        });

        TableColumn<Student, String> checkCol = new TableColumn<>("Attendance");
        checkCol.setMinWidth(150);
        checkCol.setCellValueFactory(
                new PropertyValueFactory<>("Attendance"));
        checkCol.setCellFactory(ChoiceBoxTableCell.forTableColumn(new DefaultStringConverter(), cbValues));
        checkCol.setOnEditCommit((TableColumn.CellEditEvent<Student, String> t) -> {
            String status = t.getNewValue();
            t.getRowValue().setAttendance(status);
        });


        table2.setItems(data);
        table2.setMaxWidth(850);table2.setMinHeight(500);
        table2.getColumns().addAll(IdCol, firstNameCol, lastNameCol, checkCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            Show(classE.getText(), subE.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> SaveToDB());
        saveButton.setMinWidth(60);

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e -> {
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}});

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_CENTER);
        back.setOnAction(e -> Homepage());

        Label myClass = new Label("Class: " + classE.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + subE.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton,  back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));

        //add a student by entering details in the textfield
        Label snId = new Label("Student ID: ");
        snId.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addID = new TextField();
        addID.setPromptText("Student ID");
        addID.setMaxWidth(120);

        Label fName = new Label("FirstName: ");
        fName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addFirstName = new TextField();
        addFirstName.setMaxWidth(120);
        addFirstName.setPromptText("First Name");

        Label lName = new Label("LastName: ");
        lName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addLastName = new TextField();
        addLastName.setMaxWidth(120);
        addLastName.setPromptText("Last Name");

        Button addStudent = new Button("Add New Student");
        addStudent.setOnAction(e -> {
            //CheckRecords(addID.getText(), addFirstName.getText(), addLastName.getText());
            data.add(new Student(addFirstName.getText(), addLastName.getText(), addID.getText(), attendance));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table2, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);

        every.setCenter(mine);
    }

    //GET SAVED STUDENTS FROM THE DATABASE
    public void Show(String classname, String subName){
        String className = classname +"-"+subName;
        try {
            String queryString ="select studentID, firstname, lastname from '" + className + "' ";

            ResultSet rSet = stmt.executeQuery(queryString);

            while (rSet.next()){
                String id = rSet.getString("studentID");
                String firstName = rSet.getString("firstname");
                String lastName = rSet.getString("lastname");

                data.add(new Student(firstName, lastName, id, "Present"));
            }
        }catch (SQLException ex) {
            Confirmation.display("error", ex.getMessage());}
    }

    //A METHOD THAT SAVES THE ATTENDANCE RECORDS TO THE DATABASE
    public void SaveToDB(){
        String date = (now.get(Calendar.DATE)) + "-"
                + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        String className = classE.getText();
        String sub = subE.getText();
        String classToday = sub + "_" + className + "_" + date;

        //System.out.println(classToday);

        try {
            String queryString ="CREATE TABLE IF NOT EXISTS '"+classToday+
                    "'(studentID char(10) NOT NULL," +
                    "firstname char(16)," +
                    "lastname char(16)," +
                    "Attendance char(16)," +
                    "PRIMARY KEY (studentID));";

            stmt2.executeUpdate(queryString);
            stmt2.close();

        }catch (SQLException ex) {
            ex.printStackTrace();}

        for (Student person : table2.getItems()) {

            String id = person.getId();
            String name1 = person.getFirstName();
            String name2 = person.getLastName();
            String att = person.getAttendance();

            String formatted = String.format(" %s %s %s (%s)", id, name1 , name2, att);
            //System.out.println(formatted);

            try {
                String sql = "INSERT INTO'" + classToday + "'(studentID, firstname, lastname, Attendance) " +
                        "VALUES ('" + id + "', '"+ name1 +"','"+ name2 + "', '" + att + "' );";

                stmt3.executeUpdate(sql);
                stmt3.close();

            }catch (SQLException ex) {
                Confirmation.display("error", ex.getMessage());}
        }
        Confirmation.display("Success!!", "your attendance register has been successfully saved!");
    }

    //THE LOG IN PAGE TO CHECK THE ATTENDANCE RECORDS
    public void CheckAttLog(){
        Label main = new Label("CHECK ATTENDANCE RECORDS");
        main.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 26));
        main.setTextAlignment(TextAlignment.CENTER);
        main.setAlignment(Pos.TOP_CENTER);
        main.setPadding(new Insets(10, 5, 50, 5));
        main.setStyle("-fx-text-fill: black;");

        Label subL = new Label("Enter subject: ");
        subL.setFont(new Font("century gothic bold", 20));
        subject = new TextField();
        subject.setMaxWidth(350);
        subject.setPromptText("e.g. BIOLOGY");
        HBox one = new HBox(57, subL,subject);one.setAlignment(Pos.CENTER);one.setMinWidth(500);

        Label classL = new Label("Enter class: ");
        classL.setFont(new Font("century gothic bold", 20));
        classEnt = new TextField();
        classEnt.setPromptText("e.g. form1");
        classEnt.setMaxWidth(350);
        HBox two = new HBox(80, classL, classEnt);two.setAlignment(Pos.CENTER);two.setMinWidth(500);

        Label dateL = new Label("Attendance Date: ");
        dateL.setFont(new Font("century gothic bold", 20));
        dateEnt = new TextField();
        dateEnt.setPromptText("e.g. 12-2-2018");
        dateEnt.setMaxWidth(350);
        HBox three = new HBox(15, dateL, dateEnt);three.setAlignment(Pos.CENTER);three.setMinWidth(500);

        VBox all = new VBox(30, one, two, three);

        Button go = new Button("GO");
        go.setMinWidth(60);
        go.setStyle("-fx-background-color: linear-gradient(darkkhaki, khaki);");
        go.setOnMouseEntered(e -> go.setStyle("-fx-background-color: linear-gradient(moccasin, tan);"));
        go.setOnMouseExited(e -> go.setStyle("-fx-background-color: linear-gradient(darkkhaki, khaki);"));
        go.setOnAction(e -> {
            if (subject.getText().isEmpty() || classEnt.getText().isEmpty() || dateEnt.getText().isEmpty()){
                Confirmation.display("Error!", "the fields cannot be empty!!");}
            else {CheckRecords();}
        } );

        //CREATE THE V_BOX TO ADD ALL THE WIDGETS
        VBox vbox = new VBox();
        vbox.getChildren().addAll(main, all, go);
        vbox.setSpacing(25);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-background-color: transparent;" +
                "-fx-border-radius: 6;");
        vbox.setMaxWidth(450);vbox.setMinHeight(370);
        VBox mine = new VBox();
        mine.getChildren().addAll(vbox);
        mine.setAlignment(Pos.CENTER);
        mine.setId("records");

        every.setCenter(mine);
    }

    //THE OUTLOOK OF THE CHECK RECORDS PAGE
    public void CheckRecords(){

        final TableView<Student> table4 = new TableView<>();

        TableColumn<Student, String> IdColumn = new TableColumn<>("Student No.");
        IdColumn.setMinWidth(150);
        IdColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setMinWidth(150);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));

        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("lastName"));

        TableColumn<Student, String> checkCol = new TableColumn<>("Attendance");
        checkCol.setMinWidth(150);
        checkCol.setCellValueFactory(
                new PropertyValueFactory<>("Attendance"));

        table4.setEditable(false);
        table4.setItems(data4);
        table4.setMaxWidth(750);table4.setMinHeight(550);
        table4.getColumns().addAll(IdColumn, firstNameCol, lastNameCol, checkCol);

        Button show = new Button("Show Attendance");
        show.setMinWidth(60);
        show.setOnAction(e -> {
            Check(subject.getText(), classEnt.getText(), dateEnt.getText());
            show.setDisable(true);
        });

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_RIGHT);
        back.setOnAction(e -> Homepage());

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e -> {
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}});

        Label myClass = new Label("Class: " + classEnt.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + subject.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl3 = new Label("Attendance Date: " + dateEnt.getText());
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, show, back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));

        VBox showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table4);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);
        every.setCenter(mine);
    }

    //A METHOD THAT GETS THE RECORDS FROM THE DATABASE
    public void Check(String sub, String classes, String date){

        String tableName = sub +"_"+ classes +"_"+ date;

        try {
            String queryString ="select studentID, firstname, lastname, Attendance from '" + tableName + "' ";

            ResultSet rSet = stmt.executeQuery(queryString);


            while (rSet.next()){
                String id = rSet.getString("studentID");
                String firstName = rSet.getString("firstname");
                String lastName = rSet.getString("lastname");
                String attendance = rSet.getString("Attendance");

                Student student = new Student(firstName, lastName, id, attendance);

                data4.add(student);

            }

        }catch (SQLException ex) {
            Confirmation.display("error", ex.getMessage());}
    }

    //THE MENU THAT INTRODUCES THE GRADES SECTION
    public void GradesMenu(){

        Label well = new Label("GRADES MANAGER!!");
        well.setFont(Font.font("century gothic", FontWeight.BOLD, 32));
        well.setStyle("-fx-text-fill: beige;");

        String intro = "Hello there!.";
        String second = "Tired of grades sheet piling up???? no worries,";
        String third  = "this part of the application will enable you to record and store your Student";
        String fourth = " grades which you can easily peruse through!!! only three assessments available though,";
        String fifth = " contact us if you need more tests input!!!";

        Label myText = new Label();
        myText.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText.setText(intro);
        myText.setStyle("-fx-text-fill: beige;");

        Label myText2 = new Label(second);Label myText3 = new Label(third);
        Label myText4 = new Label(fourth);Label myText5 = new Label(fifth);

        myText2.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText2.setStyle("-fx-text-fill: beige;");

        myText3.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText3.setStyle("-fx-text-fill: beige;");

        myText4.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText4.setStyle("-fx-text-fill: beige;");

        myText5.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText5.setStyle("-fx-text-fill: beige;");

        Label lbl = new Label("GET STARTED");
        lbl.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        lbl.setStyle("-fx-text-fill: beige;" +
                "-fx-background-color: darkgreen;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: white;");
        HBox get = new HBox(lbl);
        get.setAlignment(Pos.CENTER);

        Button btn1 = new Button("New Class");btn1.setMinWidth(200);btn1.setMinHeight(60);
        btn1.setStyle("-fx-background-color: red; -fx-text-fill: beige;");
        btn1.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));
        btn1.setId("redButton");
        btn1.setOnMouseEntered( e -> btn1.setStyle("-fx-background-color: orangered;" ));
        btn1.setOnMouseExited(e -> btn1.setStyle("-fx-background-color: red;" ));
        btn1.setOnAction(e -> {
            classNamed = Confirmation.getAnswer("new class", "please enter the class name: ");
            MakeClass();
        });

        Button btn2 = new Button("Record class Grades");btn2.setMinWidth(200);btn2.setMinHeight(60);
        btn2.setStyle("-fx-background-color: lime; -fx-text-fill: beige;");
        btn2.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));btn2.setId("yellow");
        btn2.setOnMouseEntered( e -> btn2.setStyle("-fx-background-color: limegreen;" ));
        btn2.setOnMouseExited(e -> btn2.setStyle("-fx-background-color: lime;" ));
        btn2.setOnAction( e -> EnterGradesLog() );


        Button btn3 = new Button("check class Grades");btn3.setMinWidth(200);btn3.setMinHeight(60);
        btn3.setStyle("-fx-background-color: darkturquoise; -fx-text-fill: beige;");
        btn3.setFont(Font.font("segoe ui", FontWeight.SEMI_BOLD, 12));btn3.setId("green");
        btn3.setOnMouseEntered( e -> btn3.setStyle("-fx-background-color: cyan;" ));
        btn3.setOnMouseExited(e -> btn3.setStyle("-fx-background-color: darkturquoise;"));
        btn3.setOnAction( e -> checkGradesLog() );

        HBox buttons = new HBox(20, btn1, btn2, btn3);
        buttons.setAlignment(Pos.BASELINE_CENTER);

        VBox myTex = new VBox(15, well, myText, myText2, myText3, myText4, myText5);
        myTex.setAlignment(Pos.TOP_CENTER);

        VBox home = new VBox(40);
        home.setId("GradesPage");
        home.getChildren().addAll(myTex, get, buttons);
        home.setAlignment(Pos.TOP_CENTER);
        home.setPadding(new Insets(40,10,10,10));

        every.setCenter(home);

    }

    //THE LOG IN PAGE TO RECORD GRADES
    public void EnterGradesLog(){

        Label main = new Label("ENTER STUDENT GRADES");
        main.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 26));
        main.setTextAlignment(TextAlignment.CENTER);
        main.setAlignment(Pos.TOP_CENTER);
        main.setPadding(new Insets(10, 5, 50, 5));
        main.setStyle("-fx-text-fill: black;");

        Label subL = new Label("Enter subject: ");
        subL.setFont(new Font("century gothic bold", 20));
        subName = new TextField();
        subName.setMaxWidth(350);
        subName.setPromptText("e.g. BIOLOGY");
        HBox one = new HBox(57, subL,subName);one.setAlignment(Pos.CENTER);one.setMinWidth(500);

        Label classL = new Label("Enter class: ");
        classL.setFont(new Font("century gothic bold", 20));
        className = new TextField();
        className.setPromptText("e.g. form1");
        className.setMaxWidth(350);
        HBox two = new HBox(80, classL, className);two.setAlignment(Pos.CENTER);two.setMinWidth(500);

        VBox all = new VBox(40, one, two);

        Button go = new Button("GO");
        go.setMinWidth(60);
        go.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        go.setOnMouseEntered(e -> go.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        go.setOnMouseExited(e -> go.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);"));
        go.setOnAction(e ->{

            myText = Confirmation.getNum("enter", "how many test grades do you want to enter? ");
            testNUm = Integer.parseInt(myText);
            if (testNUm == 1){
                if (subName.getText().isEmpty() || className.getText().isEmpty()){
                    Confirmation.display("Error!", "the fields cannot be empty!!");}
                else{EnterGrades1();}

            }else if (testNUm == 2){
                if (subName.getText().isEmpty() || className.getText().isEmpty()){
                    Confirmation.display("Error!", "the fields cannot be empty!!");}
                else{EnterGrades2();}

            }else if (testNUm == 3){
                if (subName.getText().isEmpty() || className.getText().isEmpty()){
                    Confirmation.display("Error!", "the fields cannot be empty!!");}
                else{EnterGrades3();}

            }else{Confirmation.display("Error", "the available number of test grades to enter is 3.\nContact us for help and support");}


        });

        //CREATE THE V_BOX TO ADD ALL THE WIDGETS
        VBox vbox = new VBox();
        vbox.getChildren().addAll(main, all, go);
        vbox.setSpacing(25);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: black;" +
                "-fx-background-color: linear-gradient(#e6f4f6, #eeeeee);" +
                "-fx-border-radius: 6;");
        vbox.setMaxWidth(450);vbox.setMinHeight(370);
        VBox mine = new VBox();
        mine.getChildren().addAll(vbox);
        mine.setAlignment(Pos.CENTER);
        mine.setId("grades");

        every.setCenter(mine);
    }

    //THE PAGES THAT RECORD THE GRADES BASED ON THE USERS SELECTION
    public void EnterGrades1(){

        table5.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setStudentID(t.getNewValue());
                    }
                }
        );

        TableColumn name = new TableColumn("FIRSTNAME");
        name.setMinWidth(150);
        name.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );

        TableColumn lastNameCol = new TableColumn("SURNAME");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLastName(t.getNewValue());
                    }
                }
        );


        TableColumn grade1 = new TableColumn("GRADE 1");
        grade1.setMinWidth(150);
        grade1.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("firstGrade"));
        grade1.setCellFactory(cellFactory);
        grade1.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setFirstGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn subTotalCol = new TableColumn("OVERALL GRADE");
        subTotalCol.setMinWidth(150);
        subTotalCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("subTotal"));


        table5.setItems(data1);
        table5.getColumns().addAll(idCol, name, lastNameCol, grade1, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            ShowThem(className.getText(), subName.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e->{
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}
        });

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_CENTER);
        back.setOnAction(e -> Homepage());

        Label myClass = new Label("Class: " + className.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + subName.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton,  back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));

        //add a student by entering details in the textfield
        Label snId = new Label("Student ID: ");
        snId.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addID = new TextField();
        addID.setPromptText("Student ID");
        addID.setMaxWidth(120);

        Label fName = new Label("FirstName: ");
        fName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addFirstName = new TextField();
        addFirstName.setMaxWidth(120);
        addFirstName.setPromptText("First Name");

        Label lName = new Label("LastName: ");
        lName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addLastName = new TextField();
        addLastName.setMaxWidth(120);
        addLastName.setPromptText("Last Name");

        Button addStudent = new Button("Add New Student");
        addStudent.setOnAction(e -> {
            data1.add(new Grades(addID.getText(), addFirstName.getText(), addLastName.getText(),0,0,0, testNUm));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table5, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);

        every.setCenter(mine);
    }

    public void EnterGrades2(){

        table5.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setStudentID(t.getNewValue());
                    }
                }
        );

        TableColumn name = new TableColumn("FIRSTNAME");
        name.setMinWidth(150);
        name.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );

        TableColumn lastNameCol = new TableColumn("SURNAME");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLastName(t.getNewValue());
                    }
                }
        );


        TableColumn grade1 = new TableColumn("GRADE 1");
        grade1.setMinWidth(150);
        grade1.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("firstGrade"));
        grade1.setCellFactory(cellFactory);
        grade1.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setFirstGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn grade2 = new TableColumn("GRADE 2");
        grade2.setMinWidth(150);
        grade2.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("secondGrade"));
        grade2.setCellFactory(cellFactory);
        grade2.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSecondGrade(t.getNewValue().intValue());
                    }
                }
        );



        TableColumn subTotalCol = new TableColumn("OVERALL GRADE");
        subTotalCol.setMinWidth(150);
        subTotalCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("subTotal"));


        table5.setItems(data1);
        table5.getColumns().addAll(idCol, name, lastNameCol, grade1, grade2, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            ShowThem(className.getText(), subName.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e->{
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}
        });

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_CENTER);
        back.setOnAction(e -> Homepage());

        Label myClass = new Label("Class: " + className.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + subName.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton,  back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));

        //add a student by entering details in the textfield
        Label snId = new Label("Student ID: ");
        snId.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addID = new TextField();
        addID.setPromptText("Student ID");
        addID.setMaxWidth(120);

        Label fName = new Label("FirstName: ");
        fName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addFirstName = new TextField();
        addFirstName.setMaxWidth(120);
        addFirstName.setPromptText("First Name");

        Label lName = new Label("LastName: ");
        lName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addLastName = new TextField();
        addLastName.setMaxWidth(120);
        addLastName.setPromptText("Last Name");

        Button addStudent = new Button("Add New Student");
        addStudent.setOnAction(e -> {
            data1.add(new Grades(addID.getText(), addFirstName.getText(), addLastName.getText(),0,0,0, testNUm));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table5, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);

        every.setCenter(mine);
    }

    public void EnterGrades3(){

        table5.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setStudentID(t.getNewValue());
                    }
                }
        );

        TableColumn name = new TableColumn("FIRSTNAME");
        name.setMinWidth(150);
        name.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );

        TableColumn lastNameCol = new TableColumn("SURNAME");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLastName(t.getNewValue());
                    }
                }
        );


        TableColumn grade1 = new TableColumn("GRADE 1");
        grade1.setMinWidth(150);
        grade1.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("firstGrade"));
        grade1.setCellFactory(cellFactory);
        grade1.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setFirstGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn grade2 = new TableColumn("GRADE 2");
        grade2.setMinWidth(150);
        grade2.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("secondGrade"));
        grade2.setCellFactory(cellFactory);
        grade2.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSecondGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn grade3 = new TableColumn("GRADE 3");
        grade3.setMinWidth(150);
        grade3.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("thirdGrade"));
        grade3.setCellFactory(cellFactory);
        grade3.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setThirdGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn subTotalCol = new TableColumn("OVERALL GRADE");
        subTotalCol.setMinWidth(150);
        subTotalCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("subTotal"));


        table5.setItems(data1);
        table5.getColumns().addAll(idCol, name, lastNameCol, grade1, grade2, grade3, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            ShowThem(className.getText(), subName.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e->{
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}
        });

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_CENTER);
        back.setOnAction(e -> Homepage());

        Label myClass = new Label("Class: " + className.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + subName.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton,  back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));

        //add a student by entering details in the textfield
        Label snId = new Label("Student ID: ");
        snId.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addID = new TextField();
        addID.setPromptText("Student ID");
        addID.setMaxWidth(120);

        Label fName = new Label("FirstName: ");
        fName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addFirstName = new TextField();
        addFirstName.setMaxWidth(120);
        addFirstName.setPromptText("First Name");

        Label lName = new Label("LastName: ");
        lName.setFont(Font.font("Century gothic", FontWeight.BOLD, 16));
        TextField addLastName = new TextField();
        addLastName.setMaxWidth(120);
        addLastName.setPromptText("Last Name");

        Button addStudent = new Button("Add New Student");
        addStudent.setOnAction(e -> {
            data1.add(new Grades(addID.getText(), addFirstName.getText(), addLastName.getText(),0,0,0, testNUm));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table5, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);

        every.setCenter(mine);
    }
    ////////////////////////////////////////////////////////////////

    //SHOW THE STUDENTS FORM THE DATABASE
    public void ShowThem(String myClass, String sub){
        String show = myClass+"-"+ sub;

        try {
            String queryString ="select studentID, firstname, lastname from '" + show + "'; ";

            ResultSet rSet = stmt.executeQuery(queryString);

            while (rSet.next()){
                String id = rSet.getString("studentID");
                String firstName = rSet.getString("firstname");
                String lastName = rSet.getString("lastname");

                data1.add(new Grades(id, firstName, lastName, 0, 0, 0, testNUm));
            }
        }catch (SQLException ex) {
            Confirmation.display("error",ex.getMessage());}
    }

    //SAVE THE RECORDED DATA TO THE DATABASE
    public void saveGrades(){

        String myClass = className.getText();
        String sub = subName.getText();
        String classGrades = "GRADES_"+sub + "_" + myClass;

        //System.out.println(classGrades);

        try {
            String queryString ="CREATE TABLE IF NOT EXISTS '"+classGrades+
                    "'(studentID char(10) NOT NULL," +
                    "firstname char(16)," +
                    "lastname char(16)," +
                    "grade1 INTEGER," +
                    "grade2 INTEGER," +
                    "grade3 INTEGER," +
                    "overall INTEGER," +
                    "PRIMARY KEY (studentID));";

            stmt2.executeUpdate(queryString);
            stmt2.close();

        }catch (SQLException ex) {
            ex.printStackTrace();}

        for (Grades students : table5.getItems()) {

            String id = students.getStudentID();
            String name1 = students.getName();
            String name2 = students.getLastName();
            int grade1 = students.getFirstGrade();
            int grade2 = students.getSecondGrade();
            int grade3 = students.getThirdGrade();
            int overall = students.getSubTotal();


            String formatted = String.format(" %s %s %s (%d) (%d) (%d) (%d)", id, name1 , name2, grade1, grade2, grade3, overall);
            //System.out.println(formatted);

            try {
                String sql = "INSERT INTO'" + classGrades + "'(studentID, firstname, lastname, grade1, grade2, grade3, overall) " +
                        "VALUES ('" + id + "', '"+ name1 +"','"+ name2 + "', '" + grade1 + "', '" + grade2 + "' , '" + grade3 +
                        "', '" + overall + "');";

                stmt3.executeUpdate(sql);
                stmt3.close();

            }catch (SQLException ex) {
                Confirmation.display("error", ex.getMessage());}
        }
        Confirmation.display("success", "your student grades have been successfully saved!");
    }

    //THE LOG IN PAGE TO CHECK THE RECORDED GRADES
    public void checkGradesLog(){

        Label main = new Label("CHECK STUDENT GRADES");
        main.setFont(Font.font("Palatino linotype", FontWeight.BOLD, 26));
        main.setTextAlignment(TextAlignment.CENTER);
        main.setAlignment(Pos.TOP_CENTER);
        main.setPadding(new Insets(10, 5, 50, 5));
        main.setStyle("-fx-text-fill: black;");

        Label subL = new Label("Enter subject: ");
        subL.setFont(new Font("century gothic bold", 20));
        subName1 = new TextField();
        subName1.setMaxWidth(350);
        subName1.setPromptText("e.g. BIOLOGY");
        HBox one = new HBox(57, subL,subName1);one.setAlignment(Pos.CENTER);one.setMinWidth(500);

        Label classL = new Label("Enter class: ");
        classL.setFont(new Font("century gothic bold", 20));
        className1 = new TextField();
        className1.setPromptText("e.g. form 1");
        className1.setMaxWidth(350);
        HBox two = new HBox(80, classL, className1);two.setAlignment(Pos.CENTER);two.setMinWidth(500);

        VBox all = new VBox(40, one, two);

        Button go = new Button("GO");
        go.setMinWidth(60);
        go.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        go.setOnAction(e -> {
            if (subName1.getText().isEmpty() || className1.getText().isEmpty()){
                Confirmation.display("Error!", "the fields cannot be empty!!");}
            else{checkGrades();}
        });

        //CREATE THE V_BOX TO ADD ALL THE WIDGETS
        VBox vbox = new VBox();
        vbox.getChildren().addAll(main, all, go);
        vbox.setSpacing(25);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid;" +
                "-fx-border-color: navy;" +
                "-fx-background-color: linear-gradient(lightsteelblue, lightslategray);" +
                "-fx-border-radius: 6;");
        vbox.setMaxWidth(450);vbox.setMinHeight(370);
        VBox mine = new VBox();
        mine.getChildren().addAll(vbox);
        mine.setAlignment(Pos.CENTER);
        mine.setId("checkGrades");

        every.setCenter(mine);
    }

    //THE OUTLOOK OF THE CHECK RECORDS
    public void checkGrades(){
        table.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setStudentID(t.getNewValue());
                    }
                }
        );

        TableColumn name = new TableColumn("FIRSTNAME");
        name.setMinWidth(150);
        name.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );

        TableColumn lastNameCol = new TableColumn("SURNAME");
        lastNameCol.setMinWidth(150);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("lastName"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setLastName(t.getNewValue());
                    }
                }
        );

        TableColumn grade1 = new TableColumn("GRADE 1");
        grade1.setMinWidth(150);
        grade1.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("firstGrade"));
        grade1.setCellFactory(cellFactory);
        grade1.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setFirstGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn grade2 = new TableColumn("GRADE 2");
        grade2.setMinWidth(150);
        grade2.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("secondGrade"));
        grade2.setCellFactory(cellFactory);
        grade2.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSecondGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn grade3 = new TableColumn("GRADE 3");
        grade3.setMinWidth(150);
        grade3.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("thirdGrade"));
        grade3.setCellFactory(cellFactory);
        grade3.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setThirdGrade(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn subTotalCol = new TableColumn("OVERALL GRADE");
        subTotalCol.setMinWidth(150);
        subTotalCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("subTotal"));

        table.setItems(data1);
        table.getColumns().addAll(idCol, name, lastNameCol, grade1, grade2, grade3, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            showGradeRecords(subName1.getText(), className1.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e -> {
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}});

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_CENTER);
        back.setOnAction(e -> Homepage());

        Label myClass = new Label("Class: " + className1.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + subName1.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton,  back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));

        showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);

        every.setCenter(mine);
    }

    //METHOD THAT GETS THE RECORDED FROM THE DATABASE
    public void showGradeRecords(String sub, String classes){

        String classGrades = "GRADES_"+sub + "_" + classes;

        try {
            String queryString ="select studentID, firstname, lastname, grade1, grade2, grade3, overall from '" + classGrades + "' ";

            ResultSet rSet = stmt.executeQuery(queryString);

            while (rSet.next()){
                String id = rSet.getString("studentID");
                String firstName = rSet.getString("firstname");
                String lastName = rSet.getString("lastname");
                int grd1 = rSet.getInt("grade1");
                int grd2 = rSet.getInt("grade2");
                int grd3 = rSet.getInt("grade3");
                int ovue = rSet.getInt("overall");

                data1.add(new Grades(id, firstName, lastName, grd1, grd2, grd3, testNUm));
            }

        }catch (SQLException ex) {
            ex.printStackTrace();}
    }

    //A METHOD THAT LETS THE USER MAKE A NEW CLASS
    public void MakeClass(){

        table1.setEditable(true);

        Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
            @Override public TableCell call(TableColumn p) {return new EditingCell();}};


        TableColumn<Register, String> idCol = new TableColumn<>("STUDENT ID");
        idCol.setMinWidth(200);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("regNo"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit((TableColumn.CellEditEvent<Register, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setRegNo(myValue);
        });

        TableColumn<Register, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setMinWidth(200);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<>("first"));
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit(( TableColumn.CellEditEvent<Register, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setFirst(myValue);
        });

        TableColumn<Register, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setMinWidth(200);
        lastNameCol.setCellValueFactory(
                new PropertyValueFactory<>("last"));
        lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameCol.setOnEditCommit((TableColumn.CellEditEvent<Register, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setLast(myValue);
        });

        TableColumn<Register, String> residenceCol = new TableColumn<>("RESIDENCE");
        residenceCol.setMinWidth(200);
        residenceCol.setCellValueFactory(
                new PropertyValueFactory<>("residence"));
        residenceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        residenceCol.setOnEditCommit(( TableColumn.CellEditEvent<Register, String> t) -> {
            String myValue = t.getNewValue();
            t.getRowValue().setResidence(myValue);
        });


        table1.getColumns().addAll(idCol, firstNameCol, lastNameCol, residenceCol);
        table1.setMinHeight(500);
        table1.setItems(data2);

        //add a student by entering details in the textfield
        Label snId = new Label("Student ID: ");
        snId.setFont(Font.font("Century gothic", FontWeight.LIGHT, 11));
        TextField addID = new TextField();
        addID.setPromptText("Student ID");
        addID.setMaxWidth(120);

        Label fname = new Label("firstName: ");
        fname.setFont(Font.font("Century gothic", FontWeight.LIGHT, 11));
        TextField addFirstName = new TextField();
        addFirstName.setMaxWidth(120);
        addFirstName.setPromptText("First Name");

        Label lname = new Label("LastName: ");
        lname.setFont(Font.font("Century gothic", FontWeight.LIGHT, 11));
        TextField addLastName = new TextField();
        addLastName.setMaxWidth(120);
        addLastName.setPromptText("Last Name");

        Label resi = new Label("Residence: ");
        resi.setFont(Font.font("Century gothic", FontWeight.LIGHT, 11));
        TextField residence = new TextField();
        residence.setMaxWidth(120);
        residence.setPromptText("Residence");

        Button addButtn = new Button("Add New Student");
        addButtn.setOnAction(e -> {
            data2.addAll(new Register(addID.getText(), addFirstName.getText(), addLastName.getText(), residence.getText()));
            addID.clear();addFirstName.clear();addLastName.clear();residence.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fname, addFirstName, lname, addLastName, resi, residence, addButtn);
        myBox.setSpacing(12);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> SaveClassToDB(classNamed));

        Button logout = new Button("LOG OUT");
        logout.setMinWidth(60);
        logout.setOnAction(e -> {
            boolean result = Confirmation.LogOut("Log-Out", "Are you sure you want to logout??\n unsaved data will be lost");
            if (result){
                window.setScene(scene1);
            }else{assert true;}});

        Button back = new Button("HOME");
        back.setMinWidth(60);
        back.setAlignment(Pos.TOP_CENTER);
        back.setOnAction(e -> Homepage());

        Label myClass = new Label("Class: " + classNamed);
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl3, saveButton,  back, logout);
        box2.setSpacing(30);
        box2.setStyle("-fx-background-color: #e6f4f6;" +
                "-fx-border-style: solid;" +
                "-fx-border-radius: 7;" +
                "-fx-border-color: steelblue;");
        box2.setPadding(new Insets(8, 8, 8, 8));
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, myBox, table1);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);

        every.setCenter(mine);

    }

    //A METHOD THAT LETS THE USER SAVE THE NEW CLASS TO THE DATABASE
    public void SaveClassToDB(String theClass){

        try {
            String queryString ="CREATE TABLE IF NOT EXISTS '"+theClass+
                    "'(studentID char(10) NOT NULL," +
                    "firstname char(16)," +
                    "lastname char(16)," +
                    "residence char(16)," +
                    "PRIMARY KEY (studentID));";

            stmt3.executeUpdate(queryString);
            stmt3.close();

        }catch (SQLException ex) {
            ex.printStackTrace();}

        for (Register person : table1.getItems()) {

            String id = person.getRegNo();
            String name1 = person.getFirst();
            String name2 = person.getLast();
            String res = person.getResidence();

            String formatted = String.format(" %s %s %s (%s)", id, name1 , name2, res);
            System.out.println(formatted);

            try {
                String sql = "INSERT INTO'" + theClass + "'(studentID, firstname, lastname, residence) " +
                        "VALUES ('" + id + "', '"+ name1 +"','"+ name2 + "', '" + res + "' );";

                stmt3.executeUpdate(sql);
                stmt3.close();

            }catch (SQLException ex) {
                Confirmation.display("error", ex.getMessage());}
        }
        Confirmation.display("success!!", "your students have been successfully saved!");
    }

    //THE 'ABOUT US' MENU
    public void AboutUsMenu(){

        Label well = new Label("ABOUT US!!");
        well.setFont(Font.font("century gothic", FontWeight.BOLD, 32));
        well.setStyle("-fx-text-fill: navy;");

        String intro = "Hello there!.";
        String second = "This simple app was written by Ahmed Twabi,";
        String third  = "He is a developer studying computer science at Chancellor College.";
        String fourth = "this app was part of a scheme to try and simplify a teachers job.";
        String fifth = " \t\t\t\t\t\tcontacts are; \n \n email: itwabi@gmail.com\t\t\t\t\tphone: 0886 124 051";

        Label myText = new Label();
        myText.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText.setText(intro);
        myText.setStyle("-fx-text-fill: navy;");

        Label myText2 = new Label(second);Label myText3 = new Label(third);
        Label myText4 = new Label(fourth);Label myText5 = new Label(fifth);

        myText2.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText2.setStyle("-fx-text-fill: navy;");

        myText3.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText3.setStyle("-fx-text-fill: navy;");

        myText4.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText4.setStyle("-fx-text-fill: navy;");

        myText5.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText5.setStyle("-fx-text-fill: navy;");

        VBox myTex = new VBox(15, well, myText, myText2, myText3, myText4, myText5);
        myTex.setAlignment(Pos.TOP_CENTER);

        VBox home = new VBox(40);
        home.setId("aboutUs");
        home.getChildren().addAll(myTex);
        home.setAlignment(Pos.TOP_CENTER);
        home.setPadding(new Insets(40,10,10,10));

        every.setCenter(home);

    }

    //THE 'HELP' MENU
    public void HelpMenu(){

        Label well = new Label("HELP!!");
        well.setFont(Font.font("century gothic", FontWeight.BOLD, 32));
        well.setStyle("-fx-text-fill: navy;");

        String intro = "Facing difficulties using the app??";
        String second = "if you are constantly meeting challenges using this app,";
        String third  = "don't stress! we got you covered. you can;";
        String fourth = "read all the tutorials of this app at: tutorialspoint.com or...";
        String fifth = " \t\t\t\t\t\tyou can contact us on; \n \n email: itwabi@gmail.com\t\t\t\t\tphone: 0886 124 051";

        Label myText = new Label();
        myText.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText.setText(intro);
        myText.setStyle("-fx-text-fill: navy;");

        Label myText2 = new Label(second);Label myText3 = new Label(third);
        Label myText4 = new Label(fourth);Label myText5 = new Label(fifth);

        myText2.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText2.setStyle("-fx-text-fill: navy;");

        myText3.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText3.setStyle("-fx-text-fill: navy;");

        myText4.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText4.setStyle("-fx-text-fill: navy;");

        myText5.setFont(Font.font("century gothic", FontWeight.BOLD, 19));
        myText5.setStyle("-fx-text-fill: navy;");

        VBox myTex = new VBox(15, well, myText, myText2, myText3, myText4, myText5);
        myTex.setAlignment(Pos.TOP_CENTER);

        VBox home = new VBox(40);
        home.setId("aboutUs");
        home.getChildren().addAll(myTex);
        home.setAlignment(Pos.TOP_CENTER);
        home.setPadding(new Insets(40,10,10,10));

        every.setCenter(home);

    }

    //THE CLASS HOLDING THE STUDENT GETTERS AND SETTERS
    public static class Student {
        private final SimpleStringProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty attend;

        private Student(String fName, String lName, String idi, String week) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.id = new SimpleStringProperty(idi);
            this.attend = new SimpleStringProperty(week);
        }
        public String getFirstName() {return firstName.get();}

        public void setFirstName(String fName) {firstName.set(fName);}

        public String getLastName() {return lastName.get();}

        public void setLastName(String fName) {lastName.set(fName);}

        public String getId(){return id.get();}

        public void setId(String idi){id.set(idi);}

        public String getAttendance(){return attend.get();}

        public void setAttendance(String wk){this.attend.set(wk);}
    }

    //THE CLASS HOLDING THE GRADES' SECTION GETTER AND SETTERS
    public static class Grades {

        private final SimpleStringProperty studentID;
        private final SimpleStringProperty name;
        private final SimpleStringProperty lastName;
        private final SimpleIntegerProperty firstGrade;
        private final SimpleIntegerProperty secondGrade;
        private final SimpleIntegerProperty thirdGrade;
        private final SimpleIntegerProperty subTotal;

        private Grades(String reg, String name, String lName, int grade1, int grade2, int grade3, int num) {
            this.studentID = new SimpleStringProperty(reg);
            this.name = new SimpleStringProperty(name);
            this.lastName = new SimpleStringProperty(lName);
            this.firstGrade = new SimpleIntegerProperty(grade1);
            this.secondGrade = new SimpleIntegerProperty(grade2);
            this.thirdGrade = new SimpleIntegerProperty(grade3);
            this.subTotal = new SimpleIntegerProperty();

            NumberBinding average = this.gradeProperty1().add(this.gradeProperty2()).add(gradeProperty3());
            this.subTotalProperty().bind(average.divide(num));
        }

        public String getStudentID() {
            return studentID.get();
        }

        public SimpleStringProperty idProperty() {
            return studentID;
        }

        public void setStudentID(String name) {
            this.studentID.set(name);
        }


        public String getName() {
            return name.get();
        }

        public SimpleStringProperty nameProperty() {
            return name;
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getLastName() {
            return lastName.get();
        }

        public SimpleStringProperty LastNameProperty() {
            return lastName;
        }

        public void setLastName(String name) {
            this.lastName.set(name);
        }

        public int getFirstGrade() {return firstGrade.get();}

        public int getSecondGrade(){return  secondGrade.get();}

        public int getThirdGrade(){return  thirdGrade.get();}


        public SimpleIntegerProperty gradeProperty1() {
            return firstGrade;}
        public SimpleIntegerProperty gradeProperty2() {
            return secondGrade;}
        public SimpleIntegerProperty gradeProperty3() {
            return thirdGrade;}

        public void setFirstGrade(int price) {this.firstGrade.set(price);}

        public void setSecondGrade(int price1) {this.secondGrade.set(price1);}

        public void setThirdGrade(int price2) {this.thirdGrade.set(price2);}


        public int getSubTotal() {
            return subTotal.get();
        }

        public SimpleIntegerProperty subTotalProperty() {
            return subTotal;
        }

        public void setSubTotal(int subTotal) {
            this.subTotal.set(subTotal);
        }
    }

    //A CLASS TO REGISTER THE NEWLY MADE CLASS BY THE USER
    public class Register {
        private final SimpleStringProperty regNo;
        private final SimpleStringProperty first;
        private final SimpleStringProperty last;
        private final SimpleStringProperty residence;

        Register(String id, String fname, String lname, String residence){
            this.regNo = new SimpleStringProperty(id);
            this.first = new SimpleStringProperty(fname);
            this.last = new SimpleStringProperty(lname);
            this.residence = new SimpleStringProperty(residence);
        }

        public String getRegNo(){return regNo.get();}

        public void setRegNo(String idi){regNo.set(idi);}

        public String getFirst() {return first.get();}

        public void setFirst(String fName) {first.set(fName);}

        public String getLast() {return last.get();}

        public void setLast(String fName) {last.set(fName);}

        public String getResidence() {return residence.get();}

        public void setResidence(String fName) {residence.set(fName);}
    }

}


