package JavaFx;

import enums.KiStrength;
import game.KiVsKiGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;



public class Controller_KiVKi {
    public Stage stage;
    @FXML
    private TextField fsize;
    @FXML
    private TextField ships5;
    @FXML
    private TextField ships4;
    @FXML
    private TextField ships3;
    @FXML
    private TextField ships2;
    @FXML
    private ChoiceBox<KiStrength> firstDifficulty;
    @FXML
    private ChoiceBox<KiStrength> secondDifficulty;

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
