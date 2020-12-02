package JavaFx;
// Anleitung für JavaFx instalation etc.
// https://www.jetbrains.com/help/idea/javafx.html#download-javafx

//dazu von Video


import game.Field;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class JavaFxMain extends Application {

    Button button_Start_NewGame,button_Start_SavesGames,button_Start_Options;//Video
    Scene scene_Start,scene_NewGame,scene_Battelfield;
    Button button_NewGame_Einzelspiel,button_NewGame_Multispieler;
    Field playerField,enemyField;
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
        button_NewGame_Einzelspiel = new Button();
        button_NewGame_Einzelspiel.setText("Singelplayer");
        button_NewGame_Einzelspiel.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button_NewGame_Multispieler = new Button();
        button_NewGame_Multispieler.setText("Multiplayer");
        button_NewGame_Multispieler.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        //Actions Buttons newGame
        button_NewGame_Einzelspiel.setOnAction(e -> primaryStage.setScene(scene_Battelfield));

        DropShadow shadow = new DropShadow(); // Schatten bei Mauspfeil
        button_Start_NewGame.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> button_Start_NewGame.setEffect(shadow));
        button_Start_SavesGames.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> button_Start_SavesGames.setEffect(shadow));
        button_Start_Options.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> button_Start_Options.setEffect(shadow));
        button_NewGame_Einzelspiel.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> button_NewGame_Einzelspiel.setEffect(shadow));
        button_NewGame_Multispieler.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> button_NewGame_Multispieler.setEffect(shadow));

        // schatten entfernung bei Mauspfeil weg
        button_Start_NewGame.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> button_Start_NewGame.setEffect(null));
        button_Start_SavesGames.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> button_Start_SavesGames.setEffect(null));
        button_Start_Options.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> button_Start_Options.setEffect(null));
        button_NewGame_Einzelspiel.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> button_NewGame_Einzelspiel.setEffect(null));
        button_NewGame_Multispieler.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> button_NewGame_Multispieler.setEffect(null));

        //Layout Button Start
        VBox vbox_Start = new VBox(); // Vertikale Anordnung, geklaut von unten
        vbox_Start.setId("hintergrundSchiff1");  // für Hintegrundbildanzeigen gebraucht
        vbox_Start.setSpacing(30);
        vbox_Start.getChildren().addAll(button_Start_NewGame,button_Start_SavesGames,button_Start_Options); // buttons hinzufügen
        vbox_Start.setAlignment(Pos.CENTER); // Zentriert  // zentriete sein

        //Layout Buttons NEwGame
        VBox vbox_NewGame = new VBox(); // Vertikale Anordnung, geklaut von unten
        vbox_NewGame.setId("hintergrundSchiff1");  //für das hintergundbild
        vbox_NewGame.setSpacing(30);
        vbox_NewGame.getChildren().addAll(button_NewGame_Einzelspiel,button_NewGame_Multispieler); // buttons hinzufügen
        vbox_NewGame.setAlignment(Pos.CENTER); // Zentriert  // zentriete sein

        //Felder layout
        BorderPane borderPane_Battelfield = new BorderPane();
        borderPane_Battelfield.setPrefSize(600,600);
        borderPane_Battelfield.setRight(new Text("iamhere"));



        primaryStage.setMinWidth(500); //Min Fenster !!!! gerade nicht im gebrauch da
        primaryStage.setMinHeight(500); //                Max-Fenster-Probleme

        scene_Start = new Scene(vbox_Start,800,600); // set erste Scene
        scene_Start.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());
        //^ hier wird nur das Hintergrundbild gesetzt
        scene_NewGame = new Scene(vbox_NewGame,800,600); // set New Game Scene
        scene_NewGame.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());

        // ersteinmal zum Ausprobieren
        scene_Battelfield = new Scene(contentBattelship(),800,600);
        scene_Battelfield.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());


        primaryStage.setScene(scene_Start);
        //primaryStage.setMaximized(true);
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
    private Parent contentBattelship(){
        //Felderlayout
        BorderPane borderPane_Battelfield = new BorderPane();
        borderPane_Battelfield.setPrefSize(600,600);
        borderPane_Battelfield.setRight(new Text("iamhere"));


        /*VBox reihen_x = new VBox();

        for (int i = 0; i < 10; i++) { // ist y-Achse Höhe
            HBox reihe_y = new HBox();
            for (int j = 0; j < 10; j++) {  // ist x-Achse Breite
                Cell c = new Cell(i,j);
                //c.setOnMouseClicked(handler);
                reihe_y.getChildren().add(c);
            }
            reihen_x.getChildren().add(reihe_y);
        }
        borderPane_Battelfield.getChildren().add(reihen_x);
        */
        VBox reihen_x = new VBox();

        for (int i = 0; i < 10; i++) { // ist y-Achse Höhe
            HBox reihe_y = new HBox();
            for (int j = 0; j < 10; j++) {  // ist x-Achse Breite
                //Cell c = new Cell(i, j);
                //c.setOnMouseClicked(handler);
              // reihe_y.getChildren().add(c);
            }
            reihen_x.getChildren().add(reihe_y);
        }


        //playerField =
        //enemyField = new Board (true);
        VBox vbox_Field = new VBox();
        vbox_Field.getChildren().addAll(reihen_x);
        vbox_Field.setAlignment(Pos.CENTER);
        borderPane_Battelfield.setCenter(vbox_Field);

        return borderPane_Battelfield;
    }

    /*public class Board  {
        boolean enemy;

        public Board(boolean enemy) {
            this.enemy = enemy;
            VBox reihen_x = new VBox();

            for (int i = 0; i < 10; i++) { // ist y-Achse Höhe
                HBox reihe_y = new HBox();
                for (int j = 0; j < 10; j++) {  // ist x-Achse Breite
                    Cell c = new Cell(i, j);
                    //c.setOnMouseClicked(handler);
                    reihe_y.getChildren().add(c);
                }
                reihen_x.getChildren().add(reihe_y);
            }
            //getChildren().add(reihen_x);
        }
    }*/


    public static void main(String[] args) {
        launch(args);
    }
}
