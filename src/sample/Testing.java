package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Testing extends Application {

    BorderPane layout;
    Text text;
    StackPane myScroll;
    Label label2;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage win = new Stage();

        win.setTitle("something");
        win.setMinWidth(450);
        win.setMinHeight(350);


        Label label1 = new Label("ewewewewewelse");
        label1.setFont(new Font("palatino linotype", 20));



        Button btn1 = new Button("OK");
        btn1.setOnAction(e -> win.close());
        btn1.setAlignment(Pos.TOP_LEFT);

        VBox dof = new VBox();
        dof.getChildren().addAll(label1, label2);
        dof.setStyle("-fx-background-color: cadetblue;");


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(dof);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(120, 120);
        scrollPane.setPrefWidth(200);
        scrollPane.setStyle("-fx-color: deepskyblue;");

        scrollPane.vvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {

            }
        });
        scrollPane.hvalueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                System.out.println(new_val.intValue());
            }
        });


        Scene myScene = new Scene(scrollPane);

        win.setScene(myScene);
        win.show();
    }
}