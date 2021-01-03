package JavaFx;

import game.KiVsKiGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_KiVKi {
    public Stage stage;

    public void changeToNewGameChooseRole(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeToGameScreenChooseRole(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private KiVsKiGame validateInput() {
        return null;
    }
}
