package JavaFx;
// Anleitung für JavaFx instalation etc.
// https://www.jetbrains.com/help/idea/javafx.html#download-javafx

//dazu von Video

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



public class JavaFxMain extends Application {

    Button button_Start_NewGame,button_Start_SavesGames,button_Start_Options;//Video
    Button button_NewGame_Einzelspiel,button_NewGame_Multispieler;
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
        //Actions Buttons Startbildschirm:
        button_Start_NewGame.setOnAction(e -> primaryStage.setScene(scene_NewGame)); // jetzt gleich hier Handel befehlt(weil hier in der klasse)
        button_Start_SavesGames.setOnAction(e -> System.out.println("bye"));
        button_Start_Options.setOnAction(e -> System.out.println("hallo"));

        //Buttons für NewGame
        button_NewGame_Einzelspiel.setText("Singelplayer");
        button_NewGame_Einzelspiel.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button_NewGame_Multispieler.setText("Multiplayer");
        button_NewGame_Multispieler.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");


        DropShadow shadow = new DropShadow(); // Schatten bei Mauspfeil
        button_Start_NewGame.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button_Start_NewGame.setEffect(shadow);
        });
        button_Start_SavesGames.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button_Start_SavesGames.setEffect(shadow);
        });
        button_Start_Options.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button_Start_Options.setEffect(shadow);
        });
        // schatten entfernung bei Mauspfeil weg
        button_Start_NewGame.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button_Start_NewGame.setEffect(null);
        });
        button_Start_SavesGames.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button_Start_SavesGames.setEffect(null);
        });
        button_Start_Options.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button_Start_Options.setEffect(null);
        });

        VBox vbox_Start = new VBox(); // Vertikale Anordnung, geklaut von unten
        vbox_Start.setId("Start-Buttons");  //Brauchen wir das?
        vbox_Start.setSpacing(30);
        vbox_Start.getChildren().addAll(button_Start_NewGame,button_Start_SavesGames,button_Start_Options); // buttons hinzufügen
        vbox_Start.setAlignment(Pos.CENTER); // Zentriert  // zentriete sein

        VBox vbox_NewGame = new VBox(); // Vertikale Anordnung, geklaut von unten
        vbox_Start.setId("Start-NewGame");  //Brauchen wir das?
        vbox_Start.setSpacing(30);
        vbox_Start.getChildren().addAll(button_NewGame_Einzelspiel,button_NewGame_Multispieler); // buttons hinzufügen
        vbox_Start.setAlignment(Pos.CENTER); // Zentriert  // zentriete sein

        primaryStage.setMinWidth(500); //Min Fenster
        primaryStage.setMinHeight(500);


        Scene scene_Start = new Scene(vbox_Start); // set erste Scene
        scene_Start.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());
        //^ hier wird nur das Hintergrundbild gesetzt

        Scene scene_NewGame = new Scene(vbox_NewGame); // set New Game Scene
        scene_NewGame.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());


        primaryStage.setScene(scene_Start);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    /*@Override // kann glaub ich gelöscht werden weil wird gelich neben demButton jetzt geschrieben
    public void handle(ActionEvent event) {
        if(event.getSource()==button){
            System.out.println("neues Spiel beginnen");
        }
        if(event.getSource()==button1){
            System.out.println("gespeicherte Spiele zeigen");
        }
        if(event.getSource()==button2){
            System.out.println("Optionen offnen");
        }
    }*/

    public static void main(String[] args) {
        launch(args);
    }
}
