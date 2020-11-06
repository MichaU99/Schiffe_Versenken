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



public class Main extends Application implements EventHandler<ActionEvent>{

    Button button,button1,button2;//Video
    BackgroundImage backgroundImage; // hintergrund
    /* @Override
     public void start(Stage primaryStage) throws Exception{
         Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
         primaryStage.setTitle("Hello World");
         primaryStage.setScene(new Scene(root, 300, 275));
         primaryStage.show();
     } */
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Schiffe versenken");



        Text t = new Text();
        t.setText("NEW GAME");
        //button.setFont(Font.font ("Verdana", 20));
        //button.setFill(Color.RED);

        button = new Button();
        button.setText("NEW GAME");
        button.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button1 = new Button();
        button1.setText("SAVED GAMES");
        button1.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");
        button2 = new Button();
        button2.setText("OPTIONS");
        button2.setStyle("-fx-background-color: #000000;-fx-text-fill: #ffffff ");

        button.setOnAction(this); // weil hier in der klasse
        button1.setOnAction(this);
        button2.setOnAction(this);

        DropShadow shadow = new DropShadow(); // Schatten bei Mauspfeil
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(shadow);
        });
        button1.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button1.setEffect(shadow);
        });
        button2.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button2.setEffect(shadow);
        });
        // schatten entfernung bei Mauspfeil weg
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setEffect(null);
        });
        button1.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button1.setEffect(null);
        });
        button2.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button2.setEffect(null);
        });

        VBox vbox = new VBox(); // Vertikale Anordnung, geklaut von unteb
        vbox.setSpacing(20);
        /*
        Image HintergrundSchiff = new Image(getClass().getResource("Bild1.jpg").toExternalForm());
        ImageView hintergrundSchiff = new ImageView(getClass().getResource("Bild1.jpg").toExternalForm());

        Background background = new Background(backgroundImage);
        vbox.setBackground(background);
        vbox.getChildren().add(hintergrundSchiff);
        */


        vbox.getChildren().add(button);
        vbox.getChildren().add(button1);
        vbox.getChildren().add(button2);
        vbox.setAlignment(Pos.CENTER); // Zentriert


        vbox.setId("hintergrundSchiff1");
        Scene scene = new Scene(vbox);
        scene.getStylesheets().addAll(this.getClass().getResource("hintergrundSchiff.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    @Override
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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
