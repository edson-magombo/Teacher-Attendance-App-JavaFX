package sample;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.sql.*;

public class TableViewSample extends Application {

    private TableView<Grades> table = new TableView<Grades>();
    private final ObservableList<Grades> data =
            FXCollections.observableArrayList();
    final HBox hb = new HBox();
    Statement stmt;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initializeDB();
        Scene scene = new Scene(new Group());
        stage.setTitle("Book Store Sample");
        stage.setWidth(650);
        stage.setHeight(550);


        final Label label = new Label("Book Store");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);


        TableColumn name = new TableColumn("Name");
        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<CellEditEvent<Grades, String>>() {
                    @Override
                    public void handle(CellEditEvent<Grades, String> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
                    }
                }
        );


        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("price"));
        priceCol.setCellFactory(TextFieldTableCell.<Grades, Number>forTableColumn(new NumberStringConverter()));
        priceCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPrice(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn priceCol2 = new TableColumn("Price2");
        priceCol2.setMinWidth(100);
        priceCol2.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("price2"));
        priceCol2.setCellFactory(TextFieldTableCell.<Grades, Number>forTableColumn(new NumberStringConverter()));
        priceCol2.setOnEditCommit(
                new EventHandler<CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPrice2(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn priceCol3 = new TableColumn("Price3");
        priceCol3.setMinWidth(100);
        priceCol3.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("price3"));
        priceCol3.setCellFactory(TextFieldTableCell.<Grades, Number>forTableColumn(new NumberStringConverter()));
        priceCol3.setOnEditCommit(
                new EventHandler<CellEditEvent<Grades, Number>>() {
                    @Override
                    public void handle(CellEditEvent<Grades, Number> t) {
                        ((Grades) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setPrice3(t.getNewValue().intValue());
                    }
                }
        );

        TableColumn subTotalCol = new TableColumn("Sub Total");
        subTotalCol.setMinWidth(200);
        subTotalCol.setCellValueFactory(
                new PropertyValueFactory<Grades, String>("subTotal"));


        table.setItems(data);
        table.getColumns().addAll(name, priceCol, priceCol2, priceCol3, subTotalCol);

        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(name.getPrefWidth());
        final TextField addPrice = new TextField();
        addPrice.setMaxWidth(priceCol.getPrefWidth());
        addPrice.setPromptText("Price");
        final TextField addQuantity = new TextField();
        //addQuantity.setMaxWidth(quantityCol.getPrefWidth());
        addQuantity.setPromptText("Quantity");

        final Button addButton = new Button("Add");

        hb.getChildren().addAll(addName, addPrice, addQuantity, addButton);
        hb.setSpacing(3);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        try {
            String queryString ="select studentID, firstname, lastname from form_2 ";

            ResultSet rset = stmt.executeQuery(queryString);

            while (rset.next()){
                String firstName = rset.getString("firstname");
                int test1 = 0;int test2 = 0; int test3 = 0;
                double avg = 0.0;
                data.add(new Grades(firstName, test1, test2, test3));
            }
        }catch (SQLException ex) {
            ex.printStackTrace();}

        stage.setScene(scene);
        stage.show();
    }

    private void initializeDB() {
        try {
            // Load the JDBC driver
            Class.forName("org.sqlite.JDBC");
            // Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Driver loaded");

            // Establish a connection
            Connection connection = DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\School.db");

            System.out.println("Database connected");

            // Create a statement
            stmt = connection.createStatement();

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static class Grades {

        private final SimpleStringProperty name;
        private final SimpleIntegerProperty price;
        private final SimpleIntegerProperty price2;
        private final SimpleIntegerProperty price3;
        private final SimpleIntegerProperty quantity;
        private final SimpleIntegerProperty subTotal;

        private Grades(String name, int price, int price2, int price3) {
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleIntegerProperty(price);
            this.price2 = new SimpleIntegerProperty(price2);
            this.price3 = new SimpleIntegerProperty(price3);
            this.quantity = new SimpleIntegerProperty();
            this.subTotal = new SimpleIntegerProperty();
            NumberBinding average = this.priceProperty().add(this.price2Property()).add(price3Property());
            this.subTotalProperty().bind(average.divide(3));
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

        public int getPrice() {
            return price.get();}
        public int getPrice2(){return  price2.get();}
        public int getPrice3(){return  price3.get();}


        public SimpleIntegerProperty priceProperty() {
            return price;}
        public SimpleIntegerProperty price2Property() {
            return price2;}
        public SimpleIntegerProperty price3Property() {
            return price3;}

        public void setPrice(int price) {this.price.set(price);}
        public void setPrice2(int price1) {this.price2.set(price1);}
        public void setPrice3(int price2) {this.price3.set(price2);}


        public int getQuantity() {
            return quantity.get();}

        public SimpleIntegerProperty quantityProperty() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
        }

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