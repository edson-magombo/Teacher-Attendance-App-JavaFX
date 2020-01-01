package sample;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Confirmation {

    static boolean answer;

    public static void display(String title, String message){
        Stage win = new Stage();

        win.initModality(Modality.APPLICATION_MODAL);
        win.setTitle(title);
        win.setMinWidth(300);
        win.setMinHeight(200);

        Label label1 = new Label(message);
        label1.setFont(javafx.scene.text.Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        Button btn1 = new Button("OK");
        btn1.setOnAction(e -> win.close());
        btn1.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        btn1.setOnMouseEntered(e -> btn1.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        btn1.setOnMouseExited(e -> btn1.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label1, btn1);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 10;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-insets: 5;" +
                        "-fx-background-color: linear-gradient(steelblue, lightblue);");

        Scene myScene = new Scene(layout);

        win.setScene(myScene);
        win.showAndWait();
    }

    public static String getAnswer(String title, String message){
        Stage win = new Stage();

        win.initModality(Modality.APPLICATION_MODAL);
        win.setTitle(title);
        win.setMinWidth(250);
        win.setMinHeight(150);

        Label label1 = new Label(message);
        label1.setFont(javafx.scene.text.Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        TextField answer = new TextField();
        answer.setMaxWidth(100);

        Label label2 = new Label("enter subject name: ");
        label2.setFont(javafx.scene.text.Font.font("century gothic", FontWeight.SEMI_BOLD, 18));
        TextField answer2 = new TextField();
        answer2.setMaxWidth(100);

        Button btn1 = new Button("OK");
        btn1.setOnAction(e -> win.close());
        btn1.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        btn1.setOnMouseEntered(e -> btn1.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        btn1.setOnMouseExited(e -> btn1.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label1, answer, label2, answer2,  btn1);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-background-color: linear-gradient(steelblue, lightblue);");

        Scene myScene = new Scene(layout);

        win.setScene(myScene);
        win.showAndWait();

        return answer.getText() +"-"+ answer2.getText();
    }

    public static boolean LogOut(String title, String message) {

        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);
        Label label = new Label();
        label.setText(message);
        label.setFont(javafx.scene.text.Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        //Create two buttons
        Button yesButton = new Button("Yes");
        yesButton.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        yesButton.setOnMouseEntered(e -> yesButton.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        yesButton.setOnMouseExited(e -> yesButton.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));

        Button noButton = new Button("No");
        noButton.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        noButton.setOnMouseEntered(e -> noButton.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        noButton.setOnMouseExited(e -> noButton.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));

        HBox buttons = new HBox(15, noButton, yesButton);
        buttons.setAlignment(Pos.BASELINE_CENTER);
        //Clicking will set answer and close window
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);

        //Add buttons
        layout.getChildren().addAll(label, buttons);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-background-color: linear-gradient(steelblue, lightskyblue);");


        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

    public static String getNum(String title, String message){
        Stage win = new Stage();

        win.initModality(Modality.APPLICATION_MODAL);
        win.setTitle(title);
        win.setMinWidth(250);
        win.setMinHeight(150);

        Label label1 = new Label(message);
        label1.setFont(javafx.scene.text.Font.font("century gothic", FontWeight.SEMI_BOLD, 18));

        TextField answer = new TextField();
        answer.setMaxWidth(100);

        Button btn1 = new Button("OK");
        btn1.setOnAction(e -> win.close());
        btn1.setStyle("-fx-background-color: linear-gradient(deepskyblue, cadetblue);");
        btn1.setOnMouseEntered(e -> btn1.setStyle("-fx-background-color: linear-gradient(turquoise, skyblue);"));
        btn1.setOnMouseExited(e -> btn1.setStyle("-fx-background-color:linear-gradient(deepskyblue, cadetblue);"));

        VBox layout = new VBox(15);
        layout.getChildren().addAll(label1, answer, btn1);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-padding: 10;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-background-color: linear-gradient(steelblue, lightblue);");

        Scene myScene = new Scene(layout);

        win.setScene(myScene);
        win.showAndWait();

        return answer.getText();
    }

}
