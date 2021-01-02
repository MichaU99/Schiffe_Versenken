package JavaFx;

import game.Game;
import game.Position;
import game.cells.Ship;
import game.cells.Shot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller_GameScreen implements Initializable {

    private boolean[][] knownField;
    private Game game;
    String waterCell="-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String shipCell="-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String shotCell="-fx-background-color: #ecfd01; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String markCell="-fx-border-width:1em;  -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    Position markedPos=null; //Speichert welche Felder bereits aufgedeckt wurden und welche nicht

    @FXML
    private GridPane GP_Player;
    @FXML
    private GridPane GP_Enemy;
    @FXML
    private Button Shoot_bt;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Shoot_bt.setDisable(true);
        game=Controller_PutShips.getGame();
        knownField=new boolean[game.getField().getHeight()][game.getField().getLength()];
        updateField(GP_Player);
        makeFieldPlayer();
        makeFieldEnemy();

        // Object[] array=GP_Player.getChildren().toArray();
        // Object[] array=GP_Enemy.getChildren().toArray();

    }
    public void markField(MouseEvent event){
        int x=GridPane.getColumnIndex((Node)event.getTarget());
        int y=GridPane.getRowIndex((Node)event.getTarget());
        HBox cell = new HBox();
        if(markedPos!=null) {
            cell = new HBox();
            if(!knownField[markedPos.getX()][markedPos.getY()]){
                cell.setStyle(waterCell);
            }
            else if (game.getField().getCell(markedPos) instanceof Ship) {
                cell.setStyle(shipCell);
            }
            else if (game.getField().getCell(markedPos) instanceof Shot) { //Unsicher ob Vergleich richtig
                cell.setStyle(shotCell);
            }
            else {
                cell.setStyle(waterCell);
            }
            GridPane.setConstraints(cell, markedPos.getX(), markedPos.getY());
            GP_Enemy.getChildren().add(cell);
        }
        cell=new HBox();
        markedPos = new Position(x, y);
        cell.setStyle(markCell);
        GridPane.setConstraints(cell, markedPos.getX(), markedPos.getY());
        GP_Enemy.getChildren().add(cell);
    }


    private void makeFieldPlayer(){

    }

    private void makeFieldEnemy(){
        for(int x=0;x<game.getEnemyField().getLength();x++){
            for(int y=0;y<game.getEnemyField().getHeight();y++){
                HBox k = new HBox();
                k.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                GridPane.setConstraints(k,x,y);
                GP_Enemy.getChildren().add(k);
            }
        }
    }
    public void shootbtn(ActionEvent event){
        System.out.println("Bla");
    }

    private void updateField(GridPane gridPane){
        gridPane.getChildren().clear();
        for(int x = 0; x< game.getField().getLength(); x++){
            for(int y = 0; y< game.getField().getHeight(); y++){
                HBox cell = new HBox();
                if (game.getField().getCell(new Position(x, y)) instanceof Ship) {
                    cell.setStyle(shipCell);
                }
                else if (game.getField().getCell(new Position(x, y)) instanceof Shot) { //Unsicher ob Vergleich richtig
                    cell.setStyle(shotCell);
                }
                else {
                    cell.setStyle(waterCell);
                }
                GridPane.setConstraints(cell,x,y);
                gridPane.getChildren().add(cell);
            }
        }
    }
}
