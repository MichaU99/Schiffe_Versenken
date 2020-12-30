package JavaFx;

import enums.KiStrength;
import game.LocalGame;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_GameScreen implements Initializable {

    @FXML
    private GridPane GP_Player;
    @FXML
    private GridPane GP_Enemy;
    @FXML
    private Button Shoot_bt;
    private LocalGame localGame;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Shoot_bt.setDisable(true);

        this.localGame = new LocalGame(10, 10, KiStrength.INTERMEDIATE);
        makeFieldPlayer();
        makeFieldEnemy();

        // Object[] array=GP_Player.getChildren().toArray();
        // Object[] array=GP_Enemy.getChildren().toArray();

    }

    private void makeFieldPlayer(){
        for(int x=0;x<localGame.getField().getLength();x++){
            for(int y=0;y<localGame.getField().getHeight();y++){
                HBox l = new HBox();
                l.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                GridPane.setConstraints(l,x,y);
                GP_Player.getChildren().add(l);
            }
        }
    }

    private void makeFieldEnemy(){
        for(int x=0;x<localGame.getEnemyField().getLength();x++){
            for(int y=0;y<localGame.getEnemyField().getHeight();y++){
                HBox k = new HBox();
                k.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                GridPane.setConstraints(k,x,y);
                GP_Enemy.getChildren().add(k);
            }
        }
    }
}
