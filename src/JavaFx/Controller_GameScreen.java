package JavaFx;

import game.*;
import game.cells.Ship;
import game.cells.Shot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller_GameScreen implements Initializable {
    private Timer timer;
    private int timerInterval = 1000;
    private Game game;
    String waterCell = "-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String shipCell = "-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String markCell = "-fx-border-width:1em;  -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    // TODO: 02.01.2021 shotWater und shotShip sollten jeweils ein rotes und dunkelblaues kreuz ohne Hintergrund enthalten 
    String shotWater = "-fx-background-color: #060b60; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String shotShip = "-fx-background-color: #e00c0c; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    Position markedPos = null; //Speichert welche Felder bereits aufgedeckt wurden und welche nicht
    private  String PLAYER1_NAME="Player1";
    private  String PLAYER2_NAME="Player2";

    @FXML
    private GridPane GP_Player;
    @FXML
    private GridPane GP_Enemy;
    @FXML
    private Button Shoot_bt;
    @FXML
    private AnchorPane ap;
    @FXML
    private Label playerTag;
    @FXML
    private Label LastShotTag;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        game = Controller_PutShips.getGame();
        updateField(GP_Player);
        game.startGame();

        //Unterscheidet zwischen Beobachteten Spielen, in denen beide Felder von anfang an komplett für den Beobachter bekannt sind
        // und normal gespielten Spielern in denen nur ein Feld bekannt ist
        if (game instanceof KiVsKiGame) {
            updateFieldDisclosed(GP_Enemy);
        } else {
            makeFieldEnemy();
            Shoot_bt.setDisable(true);
            if (game instanceof LocalGame) {

            } else if (game instanceof OnlineClientGame) {
                    playerTag.setText(PLAYER2_NAME);
                    OnlineClientGame clientGame = ((OnlineClientGame) game);
                    new Thread(() -> {
                        while (!clientGame.isMyTurn()) {
                            clientGame.enemyShot();
                            Platform.runLater(() -> updateField(GP_Player));
                        }
                        Platform.runLater(() -> playerTag.setText(PLAYER1_NAME));
                    }).start();
            }
        }


        // Object[] array=GP_Player.getChildren().toArray();
        // Object[] array=GP_Enemy.getChildren().toArray();
    }

    public void markField(MouseEvent event) {
        // TODO: 02.01.2021 Felder sollten gezielt gelöscht und gesetzt werden sonst gibt es Komplikationen mit dickem Rand
        int x = GridPane.getColumnIndex((Node) event.getTarget());
        int y = GridPane.getRowIndex((Node) event.getTarget());
        HBox cell;
        if (markedPos != null) {
            cell = new HBox();
            if (game.getEnemyField().getCell(markedPos) instanceof Shot) { //Unsicher ob Vergleich richtig
                // TODO: 02.01.2021 Prüfen ob es so funktioniert, soll eigentlich praktisch das Kreuz über die Originalcell legen, etwas fragwürdige umsetzung, vielleicht besser wenn man zwei css addieren kann
                Shot s = ((Shot) game.getEnemyField().getCell(markedPos));
                if (s.getWasShip()) {
                    cell.setStyle(shotShip);
                    /*
                    cell.setStyle(shipCell);
                    GridPane.setConstraints(cell, x, y);
                    gridPane.getChildren().add(cell);
                    cell= new HBox();
                    cell.setStyle(shotShip);
                    */
                } else {
                    cell.setStyle(shotWater);
                    /*
                    cell.setStyle(waterCell);
                    GridPane.setConstraints(cell, x, y);
                    gridPane.getChildren().add(cell);
                    cell= new HBox();
                    cell.setStyle(shotWater);
                    */
                }
            } else {
                cell.setStyle(waterCell);
            }
            GridPane.setConstraints(cell, markedPos.getX(), markedPos.getY());
            GP_Enemy.getChildren().add(cell);
        }
        cell = new HBox();
        markedPos = new Position(x, y);
        cell.setStyle(markCell);
        GridPane.setConstraints(cell, markedPos.getX(), markedPos.getY());
        GP_Enemy.getChildren().add(cell);
        Shoot_bt.setDisable(false);
    }


    /**
     * Erstellt ein neutrales (Wasser-)Feld für den Gegner
     **/
    private void makeFieldEnemy() {
        for (int x = 0; x < game.getEnemyField().getLength(); x++) {
            for (int y = 0; y < game.getEnemyField().getHeight(); y++) {
                HBox k = new HBox();
                k.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                GridPane.setConstraints(k, x, y);
                GP_Enemy.getChildren().add(k);
            }
        }
    }

    /**
     * Methode des Buttons Shoot, ruft die shoot Methode des Spiel auf die aktuell markierte Position auf
     * @param event wird nicht verwendet, ist nur wegen der Button Einbingung notwendig
     */
    public void shootbtn(ActionEvent event) {
        LastShotTag.setVisible(true);
        if (game instanceof LocalGame && game.isMyTurn()) {
            if (markedPos == null) return;
            Shoot_bt.setDisable(true);
            int rc = game.shoot(markedPos);
            updateField(GP_Enemy);
            if (rc == 0) {//Kein Treffer
                // TODO: 02.01.2021 Labelchanges sobald endgültiges Feld da ist
                playerTag.setText(PLAYER2_NAME);
                LastShotTag.setText("Last Shot: Miss");
                new Thread(() -> {
                    int rc1;
                    while (true) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (game.whoWon() != -1)
                            break;
                        rc1 = game.shoot(null);
                        Platform.runLater(() -> updateField(GP_Player));
                        if (rc1 == 0) {
                            Platform.runLater(() -> playerTag.setText(PLAYER1_NAME));
                            break;
                        } else if (rc1 == 2) {
                            LastShotTag.setText("Destroyed");
                            Platform.runLater(this::checkGameEnded);
                        }
                    }
                }).start();
            } else if (rc == 1) {
                LastShotTag.setText("Last Shot: Hit");
            } else if (rc == 2) {
                LastShotTag.setText("Destroyed");
                Platform.runLater(this::checkGameEnded);
            }
        } else if ((game instanceof OnlineHostGame || game instanceof OnlineClientGame) && game.isMyTurn()) {
            OnlineGame onlineGame = ((OnlineGame) game);
            int rc = onlineGame.shoot(markedPos);
            updateField(GP_Enemy);

            if (rc == 0) {
                playerTag.setText(PLAYER2_NAME);
                new Thread(() -> {
                    while (!onlineGame.isMyTurn()) {
                        onlineGame.enemyShot();
                        Platform.runLater(() -> updateField(GP_Player));
                    }
                    Platform.runLater(() -> playerTag.setText(PLAYER1_NAME));
                }).start();
            }
        }
    }


    private void checkGameEnded() {
        Stage stage;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Ended!");
        switch (game.whoWon()) {
            case 0:
                alert.setContentText("You won!");
                alert.showAndWait();
                stage = (Stage) (ap.getScene().getWindow());
                stage.close();
                break;
            case 1:
                alert.setContentText("You lost!");
                alert.showAndWait();
                stage = (Stage) (ap.getScene().getWindow());
                stage.close();
                break;
            default:
                break;
        }
    }

    /**
     * Aktualisiert das Feld nach Veränderungen, z.b nach Schuss auf ein Feld
     * Unterscheidet zwischen Spieler und Gegnerfeld, zeigt Felder auf den Gegnerfeld erst nach Schuss an
     *
     * @param gridPane gibt an welches Feld geupdated werden soll
     */
    private void updateField(GridPane gridPane) {
        if (GP_Enemy.equals(gridPane)) {
            gridPane.getChildren().clear();
            for (int x = 0; x < game.getField().getLength(); x++) {
                for (int y = 0; y < game.getField().getHeight(); y++) {
                    HBox cell = new HBox();
                    // TODO: 02.01.2021 Prüfen ob es so funktioniert, soll eigentlich praktisch das Kreuz über die Originalcell legen, etwas fragwürdige umsetzung, vielleicht besser wenn man zwei css addieren kann
                    if (game.getEnemyField().getCell(new Position(x, y)) instanceof Shot) {
                        Shot s = ((Shot) game.getEnemyField().getCell(new Position(x, y)));
                        if (s.getWasShip()) {
                            cell.setStyle(shotShip);
                            /*
                            cell.setStyle(shipCell);
                            //GridPane.setConstraints(cell, x, y);
                            //gridPane.getChildren().add(cell);
                            //cell= new HBox();
                            //cell.setStyle(shotShip);
                            */
                        } else {
                            cell.setStyle(shotWater);
                            /*
                            cell.setStyle(waterCell);
                            GridPane.setConstraints(cell, x, y);
                            gridPane.getChildren().add(cell);
                            cell= new HBox();
                            cell.setStyle(shotWater);
                           */
                        }
                    } else {
                        cell.setStyle(waterCell);
                    }
                    GridPane.setConstraints(cell, x, y);
                    gridPane.getChildren().add(cell);
                }
            }
        } else {
            gridPane.getChildren().clear();
            for (int x = 0; x < game.getField().getLength(); x++) {
                for (int y = 0; y < game.getField().getHeight(); y++) {
                    HBox cell = new HBox();
                    if (game.getField().getCell(new Position(x, y)) instanceof Shot) {
                        Shot s = ((Shot) game.getField().getCell(new Position(x, y)));
                        if (s.getWasShip()) {
                            cell.setStyle(shotShip);
                            /*
                            cell.setStyle(shipCell);
                            //GridPane.setConstraints(cell, x, y);
                            //gridPane.getChildren().add(cell);
                            //cell= new HBox();
                            //cell.setStyle(shotShip);
                            */
                        } else {
                            cell.setStyle(shotWater);
                            /*
                            cell.setStyle(waterCell);
                            GridPane.setConstraints(cell, x, y);
                            gridPane.getChildren().add(cell);
                            cell= new HBox();
                            cell.setStyle(shotWater);
                           */
                        }
                    } else if (game.getField().getCell(new Position(x, y)) instanceof Ship) {
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

    private void onSaveClick(Stage primaryStage) {
        FileChooser fs = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter;
        if (game instanceof LocalGame) {
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.lsave)", "*.lsave");
        } else if (game instanceof OnlineHostGame) {
            if (!game.isMyTurn())
                return;
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.hsave)", "*.hsave");
        } else if (game instanceof OnlineClientGame) {
            if (!game.isMyTurn())
                return;
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.csave)", "*.csave");
        } else {
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.ksave)", "*.ksave");
        }

        fs.getExtensionFilters().add(extensionFilter);
        File file = fs.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                if (game instanceof OnlineHostGame || game instanceof OnlineClientGame) {
                    ((OnlineGame) game).saveGame(file);
                } else {
                    game.saveGame(file.getAbsolutePath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Aktualisiert das Feld nach Veränderungen, z.b nach Schuss auf ein Feld
     * Spieler und Gegnerfeld sind von anfang an komplett bekannt sichtbar
     */
    private void updateFieldDisclosed(GridPane gridPane) {
        gridPane.getChildren().clear();
        for (int x = 0; x < game.getField().getLength(); x++) {
            for (int y = 0; y < game.getField().getHeight(); y++) {
                HBox cell = new HBox();
                if (gridPane.equals(GP_Enemy)) {
                    if (game.getEnemyField().getCell(new Position(x, y)) instanceof Shot) {
                        Shot s = ((Shot) game.getEnemyField().getCell(new Position(x, y)));
                        if (s.getWasShip()) {
                            cell.setStyle(shotShip);
                                /*
                                cell.setStyle(shipCell);
                                //GridPane.setConstraints(cell, x, y);
                                //gridPane.getChildren().add(cell);
                                //cell= new HBox();
                                //cell.setStyle(shotShip);
                                */
                        } else {
                            cell.setStyle(shotWater);
                                /*
                                cell.setStyle(waterCell);
                                GridPane.setConstraints(cell, x, y);
                                gridPane.getChildren().add(cell);
                                cell= new HBox();
                                cell.setStyle(shotWater);
                               */

                        }
                    }
                    else if (game.getEnemyField().getCell(new Position(x, y)) instanceof Ship) {
                        cell.setStyle(shipCell);
                    }
                    else {
                        cell.setStyle(waterCell);
                    }
                    GridPane.setConstraints(cell, x, y);
                    gridPane.getChildren().add(cell);
                }

                else {
                    if (game.getField().getCell(new Position(x, y)) instanceof Shot) {
                    Shot s = ((Shot) game.getField().getCell(new Position(x, y)));
                    if (s.getWasShip()) {
                        cell.setStyle(shotShip);
                                /*
                                cell.setStyle(shipCell);
                                //GridPane.setConstraints(cell, x, y);
                                //gridPane.getChildren().add(cell);
                                //cell= new HBox();
                                //cell.setStyle(shotShip);
                                */
                    }
                    else {
                        cell.setStyle(shotWater);
                                /*
                                cell.setStyle(waterCell);
                                GridPane.setConstraints(cell, x, y);
                                gridPane.getChildren().add(cell);
                                cell= new HBox();
                                cell.setStyle(shotWater);
                               */

                    }
                }

                 else if (game.getField().getCell(new Position(x, y)) instanceof Ship) {
                    cell.setStyle(shipCell);
                }
                 else {
                    cell.setStyle(waterCell);
                }
                GridPane.setConstraints(cell, x, y);
                gridPane.getChildren().add(cell);
                }
            }
        }
    }


    //----------------- Ki vs Ki Methods ----------------
    private void onKvkStartBtnClick(ActionEvent event) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.shoot(null);
                updateField(GP_Enemy);
                updateField(GP_Player);
            }
        }, 0, timerInterval);
    }

    private void onKvkStopBtnClick(ActionEvent event) {
        timer.cancel();
    }

    private void onKvkDelayCbxChange(ActionEvent event) {
        ComboBox source = ((ComboBox) event.getSource());
        switch (source.getSelectionModel().getSelectedIndex()) {
            case 0:
                timerInterval = 4000;
                break;
            case 1:
                timerInterval = 2000;
                break;
            case 2:
                timerInterval = 1000;
                break;
            case 3:
                timerInterval = 500;
                break;
            case 4:
                timerInterval = 250;
                break;
            default:
                break;
        }
        timer.cancel();
        onKvkStartBtnClick(null);
    }
    //----------------- Ki vs Ki End --------------------
}