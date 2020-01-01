package sample;


import javafx.application.Application;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import java.beans.EventHandler;
import java.lang.Object;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;
import javafx.util.converter.DoubleStringConverter;

import javax.print.DocFlavor;
import javax.xml.crypto.Data;
import java.sql.*;
import java.util.Calendar;

/*from w w w. ja va 2 s. c o m*/
public class testi extends Application {

    Statement stmt, stm2, stmt3;
    private final TableView<Person> table = new TableView<>();
    private final ObservableList<Person> data = FXCollections.observableArrayList();
    private final ObservableList<Grades> data2 = FXCollections.observableArrayList();
    ObservableList<String> cbValues = FXCollections.observableArrayList("Present", "Absent", "Late", "Sick");
    ObservableList<Double> values = FXCollections.observableArrayList();
    final TableView<Grades> table5 = new TableView<>();
    int one, two, three, four;
    final HBox hb = new HBox();
    VBox vbox;
    Label lbl;
    Scene myScene, records;
    Callback<TableColumn, TableCell> cellFactory = new Callback<TableColumn, TableCell>() {
        @Override public TableCell call(TableColumn p) {return new EditingCell();}};

    String weeks, id, firstName, lastName, myText;
    int testNUm;
    Statement stmt2;
    TextField enter, enter2;
    Stage window;
    Calendar now = Calendar.getInstance();
    double num1, num2, num3, total;
    Person grades;
    Number myAll;
    Scene something;Scene scene;
    Connection connection;

    public static void main(String[] args) {
        launch(args);
    }
    @Override

    public void start(Stage stage) {
        window = stage;
        window.setWidth(800);
        window.setHeight(700);

        //scene.getStylesheets().add(getClass().getResource("my.css").toExternalForm());

        //my first scene////////////////////////////////////////////////////////////
        Button btn = new Button("go");
        Button btn2 = new Button("create class");

        enter = new TextField();
        enter.setPromptText("enter class name");enter.setMaxWidth(200);
        enter2 = new TextField();
        enter2.setPromptText("enter subject name");enter2.setMaxWidth(200);


        VBox ne = new VBox();
        ne.getChildren().addAll(enter, enter2, btn, btn2);
        ne.setSpacing(13);

        myScene = new Scene(ne);
        btn.setOnAction(e-> {
            myText = Confirmation.getNum("enter", "how many test grades do you want to enter? ");
            testNUm = Integer.parseInt(myText);
            if (testNUm == 1){
                EnterGrades1();
            }else if (testNUm == 2){
                EnterGrades2();
            }else if (testNUm == 3){
                EnterGrades3();
            }else{Confirmation.display("Error", "the available number of test grades to enter is 3.\nContact us for help and support");}

        });

        ///////////////////////////////////////////////////////////////////////////////



        //MY LOGIN TO TEST THE CHANGING DATABASE/////////////////////////////////

        Button butn2 = new Button("new account");
        butn2.setOnAction(e-> newUser());

        TextField ent = new TextField();
        ent.setPromptText("enter username: ");ent.setMaxWidth(200);

        TextField ent2 = new TextField();
        ent2.setPromptText("enter password: ");ent2.setMaxWidth(200);

        Button butn = new Button("log-in");
        butn.setOnAction(e -> check(ent.getText(), ent2.getText()));

        VBox bedr = new VBox();
        bedr.getChildren().addAll(ent, ent2, butn, butn2);
        bedr.setSpacing(13);

        something = new Scene(bedr);
        //ending here//////////////////////////////////////////////////////////

        window.setScene(something);
        window.show();
    }


    public void EnterGrades1(){

        table5.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
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


        table5.setItems(data2);
        table5.getColumns().addAll(idCol, name, lastNameCol, grade1, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            ShowThem(enter.getText(), enter2.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());



        Label myClass = new Label("Class: " + enter.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + enter2.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton);
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
            data2.add(new Grades(addID.getText(), addFirstName.getText(), addLastName.getText(),0,0,0, testNUm));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        VBox showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table5, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);
        Scene mys = new Scene(mine);
        window.setScene(mys);
    }

    public void EnterGrades2(){

        table5.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
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


        table5.setItems(data2);
        table5.getColumns().addAll(idCol, name, lastNameCol, grade1, grade2, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            ShowThem(enter.getText(), enter2.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());



        Label myClass = new Label("Class: " + enter.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + enter2.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton);
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
            data2.add(new Grades(addID.getText(), addFirstName.getText(), addLastName.getText(),0,0,0, testNUm));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        VBox showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table5, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);
        Scene mys = new Scene(mine);
        window.setScene(mys);
    }



    public void EnterGrades3(){

        table5.setEditable(true);

        TableColumn idCol = new TableColumn("STUDENT ID");
        idCol.setMinWidth(150);
        idCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("studentID"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, String>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
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
                new javafx.event.EventHandler<TableColumn.CellEditEvent<Grades, Number>>() {
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


        table5.setItems(data2);
        table5.getColumns().addAll(idCol, name, lastNameCol, grade1, grade2, grade3, subTotalCol);

        final Button showClass = new Button("Show Students");
        showClass.setOnAction(e -> {
            ShowThem(enter.getText(), enter2.getText());
            showClass.setDisable(true);
        });
        showClass.setMinWidth(100);

        Button saveButton = new Button("Save");
        saveButton.setMinWidth(60);
        saveButton.setOnAction(e -> saveGrades());



        Label myClass = new Label("Class: " + enter.getText());
        myClass.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Label lbl2 = new Label("Subject: " + enter2.getText());
        lbl2.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        String date = (now.get(Calendar.DATE)) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.YEAR);
        Label lbl3 = new Label("Date: " + date);
        lbl3.setFont(Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        HBox box2 = new HBox();
        box2.getChildren().addAll(myClass, lbl2, lbl3, showClass, saveButton);
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
            data2.add(new Grades(addID.getText(), addFirstName.getText(), addLastName.getText(),0,0,0, testNUm));
            addID.clear();addFirstName.clear();addLastName.clear();
        });

        HBox myBox = new HBox();
        myBox.getChildren().addAll(snId, addID, fName, addFirstName, lName, addLastName, addStudent);
        myBox.setSpacing(12);myBox.setMinWidth(500);myBox.setAlignment(Pos.CENTER);

        VBox showing = new VBox();
        showing.setSpacing(15);
        showing.setPadding(new Insets(10, 0, 0, 10));
        showing.getChildren().addAll(box2, table5, myBox);
        showing.setAlignment(Pos.CENTER);

        VBox mine = new VBox(5, showing);
        Scene mys = new Scene(mine);
        window.setScene(mys);
    }


    public void ShowThem(String myClass, String sub){
        String show = myClass+"-"+ sub;

        try {
            String queryString ="select studentID, firstname, lastname from '" + show + "'; ";

            ResultSet rSet = stmt.executeQuery(queryString);

            while (rSet.next()){
                String id = rSet.getString("studentID");
                String firstName = rSet.getString("firstname");
                String lastName = rSet.getString("lastname");

                data2.add(new Grades(id, firstName, lastName, 0, 0, 0, testNUm));
            }
        }catch (SQLException ex) {
            Confirmation.display("error",ex.getMessage());}
    }

    public void saveGrades(){

        String myClass = enter.getText();
        String sub = enter2.getText();
        String classGrades = "GRADES_"+sub + "_" + myClass;

        System.out.println(classGrades);

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
            System.out.println(formatted);

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

    public void check(String user, String pass){

        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ITWABI\\IdeaProjects\\" +
                    "FirstGUIapp\\src\\sample\\users.db");

            // Create a statement
            stmt3 = connection.createStatement();
        }
        catch (Exception ex) {
            ex.printStackTrace();}

        try {
            String queryString ="select username, password from users ";

            ResultSet rset = stmt3.executeQuery(queryString);

            while (rset.next()){
                String userName = rset.getString("username");
                String passes = rset.getString("password");

                int i = 0;
                while (pass.equals(passes) && (i < 1)) {
                    if (userName.equals(user)) {
                        initializeDB(userName, passes);
                        window.setScene(myScene);
                    }else {
                        Confirmation.display("Error!", "wrong username or password!!!!");
                    }
                    i++;
                }
            }

        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public void addUser(String fullname, String username, String password){

        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establish a connection
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ITWABI\\IdeaProjects\\" +
                    "FirstGUIapp\\src\\sample\\users\\users.db");

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
            ex.printStackTrace();
        }

        initializeDB(username, password);
        Confirmation.display("success", "user created!!!");
        window.setScene(something);
    }


    public void newUser(){
        TextField ent = new TextField();
        ent.setPromptText("enter full name");ent.setMaxWidth(200);

        TextField ent2 = new TextField();
        ent2.setPromptText("enter preferred username");ent2.setMaxWidth(200);

        TextField ent3 = new TextField();
        ent3.setPromptText("enter preferred password");ent3.setMaxWidth(200);

        Button butn = new Button("create");
        butn.setOnAction(e ->
            addUser(ent.getText(), ent2.getText(), ent3.getText()));

        VBox bedr = new VBox();
        bedr.getChildren().addAll(ent, ent2, ent3, butn);
        bedr.setSpacing(13);

        Scene another = new Scene(bedr);
        window.setScene(another);
    }

    private void initializeDB(String username, String password) {
        String login = username+"-"+password;
        System.out.println(login);
        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver loaded");

            // Establish a connection
            Connection connection =
                    DriverManager.getConnection("jdbc:sqlite:C:\\Users\\ITWABI\\IdeaProjects\\FirstGUIapp\\src\\sample\\"
                            + login + ".db");

            System.out.println("Database connected");

            // Create a statement
            stmt = connection.createStatement();
            stm2 = connection.createStatement();
            stmt3 = connection.createStatement();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void CheckRecords( String classes, String sub){

        String tablename = classes + "Grades_"+ sub;
        try {
            String queryString ="select studentID, firstname, lastname, Attendance from '" + tablename + "' ";

            ResultSet rset = stmt.executeQuery(queryString);

            while (rset.next()){
                String id = rset.getString("studentID");
                String firstName = rset.getString("firstname");
                String lastName = rset.getString("lastname");
                String attendance = rset.getString("Attendance");


                data.add(new Person(firstName, lastName, id, attendance));
            }

        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



    public void Show(){
        String className = enter.getText();

            try {
                String queryString ="select studentID, firstname, lastname from '" + className + "' ";

                ResultSet rset = stmt.executeQuery(queryString);

                while (rset.next()){
                    id = rset.getString("studentID");
                    firstName = rset.getString("firstname");
                    lastName = rset.getString("lastname");


                    grades = new Person(firstName, lastName, id, weeks);

                    data.add(grades);
                }
            }catch (SQLException ex) {
                ex.printStackTrace();}
    }

    public static class Person {
        private final SimpleStringProperty id;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;
        private final SimpleStringProperty attend;

        private Person(String fName, String lName, String idi, String week) {
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

        public void setAttendance(String wk){attend.set(wk);}
    }


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


}