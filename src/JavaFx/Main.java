package JavaFx;
// Anleitung für JavaFx instalation etc.
// https://www.jetbrains.com/help/idea/javafx.html#download-javafx

//dazu von Video
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {

    Button button,button1; //Video
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
        button = new Button();
        button.setText("Click me");

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        button1 = new Button();
        button1.setText("Click me");

        layout.getChildren().add(button1);

        Scene scene = new Scene(layout,300,250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
