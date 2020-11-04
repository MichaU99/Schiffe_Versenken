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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;



public class Main extends Application implements EventHandler<ActionEvent>{

    Button button,button1,button2; //Video
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


        VBox vbox = new VBox(); // Vertikale Anordnung
        vbox.setSpacing(10);
        vbox.getChildren().add(button);
        vbox.getChildren().add(button1);
        vbox.getChildren().add(button2);
        vbox.setAlignment(Pos.CENTER); // Zentriert

        BackgroundImage Schiff= new BackgroundImage(new Image("Bild1.jpg",32,32,false,true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
//then you set to your node
        vbox.setBackground(new Background(Schiff));


        Scene scene = new Scene(vbox,300,250);
        primaryStage.setScene(scene);
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