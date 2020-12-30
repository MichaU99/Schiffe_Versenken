package JavaFx;

import enums.KiStrength;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_GameOptions {

   /* public void goToOptions(ActionEvent event) throws IOException { //Wechselt die Szene von Start zu NewGame
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_PutShips_Options.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }*/
    private int fieldSize;
    private KiStrength kiStrength;
    private int carrier;    // length = 5
    private int battleship; // length = 4
    private int cruiser;    // length = 3
    private int destroyer;  // length = 2



    public void goBackToPutShips(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(600); // hier ist extra min fenster gesetzt, so richtig ?
        stage.setMinHeight(400);
        stage.setScene(scene);
        stage.show();
    }


}
