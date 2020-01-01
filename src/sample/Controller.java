package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;


public class Controller {
    public static void display(String title, String message){
        Stage win = new Stage();

        win.initModality(Modality.APPLICATION_MODAL);
        win.setTitle(title);
        win.setMinWidth(250);

        Label label1 = new Label(message);
        Button btn1 = new Button("Close");
        btn1.setOnAction(e -> win.close());

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label1, btn1);
        layout.setAlignment(Pos.CENTER);

        Scene myscene = new Scene(layout);

        win.setScene(myscene);
        win.showAndWait();

    }
}
