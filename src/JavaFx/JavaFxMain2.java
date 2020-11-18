package JavaFx;
// Anleitung für JavaFx instalation etc.
// https://www.jetbrains.com/help/idea/javafx.html#download-javafx

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class JavaFxMain2 extends Application {

    Button button_Start_NewGame,button_Start_SavesGames,button_Start_Options,button,button_NewGame_Multispieler;//Video
    Scene scene_Start,scene_NewGame;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Schiffe versenken");


        //Buttons für Startbildschirm
        button_Start_NewGame = new Button();
        button_Start_NewGame.setText("NEW GAME");
        button_Start_NewGame.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button_Start_SavesGames = new Button();
        button_Start_SavesGames.setText("SAVED GAMES");
        button_Start_SavesGames.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button_Start_Options = new Button();
        button_Start_Options.setText("OPTIONS");
        button_Start_Options.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button.setText("Singelplayer");
        //Actions Buttons Startbildschirm:
        button_Start_NewGame.setOnAction(e -> System.out.println("hi")); // jetzt gleich hier Handel befehlt(weil hier in der klasse)
        button_Start_SavesGames.setOnAction(e -> System.out.println("bye"));
        button_Start_Options.setOnAction(e -> System.out.println("hallo"));

        //Buttons für NewGame

        //button_NewGame_Einzelspiel.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        //button_NewGame_Multispieler.setText("Multiplayer");
        //button_NewGame_Multispieler.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");


        VBox vbox_Start = new VBox(); // Vertikale Anordnung, geklaut von unten
        vbox_Start.setId("hintergrundSchiff1");  //Brauchen wir das?
        vbox_Start.setSpacing(30);
        vbox_Start.getChildren().addAll(button_Start_NewGame,button_Start_SavesGames,button_Start_Options); // buttons hinzufügen
        vbox_Start.setAlignment(Pos.CENTER); // Zentriert  // zentriete sein

        scene_Start = new Scene(vbox_Start); // set erste Scene
        scene_Start.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());
        //^ hier wird nur das Hintergrundbild gesetzt


        primaryStage.setScene(scene_Start);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

}
