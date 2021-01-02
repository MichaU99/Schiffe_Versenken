package JavaFx;

import game.*;
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
    String markCell="-fx-border-width:1em;  -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    // TODO: 02.01.2021 shotWater und shotShip sollten jeweils ein rotes und dunkelblaues kreuz ohne Hintergrund enthalten 
    String shotWater="";
    String shotShip="";
    Position markedPos=null; //Speichert welche Felder bereits aufgedeckt wurden und welche nicht

    @FXML
    private GridPane GP_Player;
    @FXML
    private GridPane GP_Enemy;
    @FXML
    private Button Shoot_bt;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        game=Controller_PutShips.getGame();
        knownField=new boolean[game.getField().getHeight()][game.getField().getLength()];
        updateField(GP_Player);
        game.startGame();

        //Unterscheidet zwischen Beobachteten Spielen, in denen beide Felder von anfang an komplett für den Beobachter bekannt sind
        // und normal gespielten Spielern in denen nur ein Feld bekannt ist
        if(game instanceof KiVsKiGame){
            updateFieldDisclosed(GP_Enemy);
        }
        else{
            makeFieldEnemy();
            Shoot_bt.setDisable(true);
            if(game instanceof LocalGame){

            }
            else if(game instanceof OnlineGame){

            }
        }



        // Object[] array=GP_Player.getChildren().toArray();
        // Object[] array=GP_Enemy.getChildren().toArray();
    }

    public void markField(MouseEvent event){
        // TODO: 02.01.2021 Felder sollten gezielt gelöscht und gesetzt werden sonst gibt es Komplikationen mit dickem Rand
            int x=GridPane.getColumnIndex((Node)event.getTarget());
            int y=GridPane.getRowIndex((Node)event.getTarget());
            HBox cell;
            if(markedPos!=null) {
                cell = new HBox();
                if(!knownField[markedPos.getX()][markedPos.getY()]){
                    cell.setStyle(waterCell);
                }
                else if (game.getEnemyField().getCell(markedPos) instanceof Ship) {
                    cell.setStyle(shipCell);
                }
                else if (game.getEnemyField().getCell(markedPos) instanceof Shot) { //Unsicher ob Vergleich richtig
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
        Shoot_bt.setDisable(false);
    }



    /**
     * Erstellt ein neutrales Feld für den Gegner
     **/
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
        if(!game.isMyTurn()) return;
        if(game instanceof LocalGame && game.isMyTurn()) {
            if (markedPos == null) return;
            knownField[markedPos.getX()][markedPos.getY()] = true; //Kann man mit Backend rausnehmen
            Shoot_bt.setDisable(true);
            game.shoot(markedPos);
            updateField(GP_Enemy);
        }
        else{

        }
    }

    /**
     * Aktualisiert das Feld nach Veränderungen, z.b nach Schuss auf ein Feld
     * Unterscheidet zwischen Spieler und Gegnerfeld, zeigt Felder auf den Gegnerfeld erst nach Schuss an
     * @param gridPane gibt an welches Feld geupdated werden soll
     */
    private void updateField(GridPane gridPane) {

        if (GP_Enemy.equals(gridPane)) {
            gridPane.getChildren().clear();
            for (int x = 0; x < game.getField().getLength(); x++) {
                for (int y = 0; y < game.getField().getHeight(); y++) {
                    HBox cell = new HBox();
                    // TODO: 02.01.2021 Prüfen ob es so funktioniert, soll eigentlich praktisch das Kreuz über die Originalcell legen, etwas fragwürdige umsetzung, vielleicht besser wenn man zwei css addieren kann
                    if (game.getEnemyField().getCell(new Position(x, y)) instanceof Shot){
                        Shot s = ((Shot) game.getEnemyField().getPlayfield()[x][y]);
                        if(s.getWasShip()){
                            cell.setStyle(shipCell);
                            GridPane.setConstraints(cell, x, y);
                            gridPane.getChildren().add(cell);
                            cell= new HBox();
                            cell.setStyle(shotShip);
                        }
                        else{
                            cell.setStyle(waterCell);
                            GridPane.setConstraints(cell, x, y);
                            gridPane.getChildren().add(cell);
                            cell= new HBox();
                            cell.setStyle(shotWater);
                        }
                    }
                    else {
                        cell.setStyle(waterCell);
                    }
                    GridPane.setConstraints(cell, x, y);
                    gridPane.getChildren().add(cell);
                }
            }
        }
        else{
            gridPane.getChildren().clear();
            for (int x = 0; x < game.getField().getLength(); x++) {
                for (int y = 0; y < game.getField().getHeight(); y++) {
                    HBox cell = new HBox();
                    if (game.getField().getCell(new Position(x, y)) instanceof Shot) {
                        Shot s = ((Shot) game.getEnemyField().getPlayfield()[x][y]);
                        if (s.getWasShip()) {
                            cell.setStyle(shipCell);
                            GridPane.setConstraints(cell, x, y);
                            gridPane.getChildren().add(cell);
                            cell = new HBox();
                            cell.setStyle(shotShip);
                        } else {
                            cell.setStyle(waterCell);
                            GridPane.setConstraints(cell, x, y);
                            gridPane.getChildren().add(cell);
                            cell = new HBox();
                            cell.setStyle(shotWater);
                        }
                    }
                    else if (game.getField().getCell(new Position(x, y)) instanceof Ship) {
                        cell.setStyle(shipCell);
                    } else {
                        cell.setStyle(waterCell);
                    }
                    GridPane.setConstraints(cell, x, y);
                    gridPane.getChildren().add(cell);
                }
            }
        }
    }

    /**
     * Aktualisiert das Feld nach Veränderungen, z.b nach Schuss auf ein Feld
     * Spieler und Gegnerfeld sind von anfang an komplett bekannt sichtbar
     */
    private void updateFieldDisclosed(GridPane gridPane){

    }
}
