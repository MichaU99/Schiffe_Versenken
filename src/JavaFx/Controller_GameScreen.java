package JavaFx;

import game.*;
import game.cells.Ship;
import game.cells.Shot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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
    public static Game game;
    String waterCell = "-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String shipCell = "-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String markCell = "-fx-border-width:1em;  -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    // TODO: 02.01.2021 shotWater und shotShip sollten jeweils ein rotes und dunkelblaues kreuz ohne Hintergrund enthalten 
    String shotWater = "-fx-background-color: #060b60; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    String shotShip = "-fx-background-color: #e00c0c; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em";
    Position markedPos = null; //Speichert welche Felder bereits aufgedeckt wurden und welche nicht
    private  String PLAYER1_NAME="YOU";
    private  String PLAYER2_NAME="ENEMY";

    @FXML
    private GridPane GP_Player;
    @FXML
    private GridPane GP_Enemy;
    @FXML
    private Button Shoot_bt;
    @FXML
    private Button startbt;
    @FXML
    private AnchorPane ap;
    @FXML
    private Label playerTag;
    @FXML
    private Label LastShotTag;
    @FXML
    private HBox GridHBox;
    @FXML
    private ChoiceBox<String> gamespdbox;
    @FXML
    private AnchorPane anchorE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //game = Controller_PutShips.getGame();
        updateField(GP_Player);
        game.startGame();

        //Unterscheidet zwischen Beobachteten Spielen, in denen beide Felder von anfang an komplett für den Beobachter bekannt sind
        // und normal gespielten Spielern in denen nur ein Feld bekannt ist
        if (game instanceof KiVsKiGame) {
            updateFieldDisclosed(GP_Enemy);
            setButtons(true);
        } else {
            makeFieldEnemy();
            setButtons(false);
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

    /**
     * Sets the visibility and disables Buttons that are not needed for the current scene
     * @param ki true-> game ist ki game; false-> game is not ki game
     */
    public void setButtons(Boolean ki){
        if(ki){
            Shoot_bt.setDisable(true);
            Shoot_bt.setVisible(false);
            startbt.setVisible(true);
            gamespdbox.setVisible(true);
            startbt.setDisable(false);
            gamespdbox.setDisable(false);

            GP_Enemy.setOnMouseClicked(null);
            gamespdbox.setValue("1x");
            gamespdbox.getItems().addAll("0.25x", "0.5x", "1x", "2x", "4x");
        }
        else{
            GP_Enemy.setOnMouseClicked(this::markField);
            Shoot_bt.setDisable(true);
            Shoot_bt.setVisible(true);
            startbt.setVisible(false);
            gamespdbox.setVisible(false);
            startbt.setDisable(true);
            gamespdbox.setDisable(true);
        }
    }

    public void markField(MouseEvent event) {
        // TODO: 02.01.2021 Felder sollten gezielt gelöscht und gesetzt werden sonst gibt es Komplikationen mit dickem Rand
        int x,y;
        try {
            x = GridPane.getColumnIndex((Node) event.getTarget());
            y = GridPane.getRowIndex((Node) event.getTarget());
        }
        catch (NullPointerException doNothing){
            return;
        }
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
        GP_Enemy.getColumnConstraints().clear();
        GP_Enemy.getRowConstraints().clear();
        for (int x = 0; x < game.getEnemyField().getLength(); x++) {
            for (int y = 0; y < game.getEnemyField().getHeight(); y++) {
                HBox k = new HBox();
                k.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                /*ColumnConstraints columnConstraints=new ColumnConstraints();
                RowConstraints rowConstraints= new RowConstraints();
                columnConstraints.setHgrow(Priority.ALWAYS);
                columnConstraints.setMaxWidth(100);
                columnConstraints.setMinWidth(10);
                columnConstraints.setPrefWidth(100);

                rowConstraints.setMinHeight(10);
                rowConstraints.setPrefHeight(30);
                rowConstraints.setVgrow(Priority.ALWAYS);

                GP_Enemy.getRowConstraints().add(rowConstraints);
                GP_Enemy.getColumnConstraints().add(columnConstraints);*/
                GridPane.setConstraints(k, x, y);
                GP_Enemy.getChildren().add(k);
            }
        }
        //anchorE.getChildren().clear();
        //anchorE.getChildren().add(GP_Enemy);
    }

    /**
     * Methode des Buttons Shoot, ruft die shoot Methode des Spiel auf die aktuell markierte Position auf
     * @param event wird nicht verwendet, ist nur wegen der Button Einbingung notwendig
     */
    public void shootbtn(ActionEvent event) {
        LastShotTag.setVisible(true);
        Shoot_bt.setDisable(true);
        if (game instanceof LocalGame && game.isMyTurn()) {
            if (markedPos == null) return;
            int rc = game.shoot(markedPos);
            updateField(GP_Enemy);
            if (rc == 0) {//Kein Treffer
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
                            Platform.runLater(() ->LastShotTag.setText("Destroyed"));
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

    /**
     * Eventhandler for both the start and stop Button in KIvsKIGames.
     * Button changes Text when clicked on it Start->Stop->Start
     * @param event Actionevent on ButtonClick
     */
    public void startstopbtnOnClick(ActionEvent event){
        if(startbt.getText().equals("Start")){
            startbt.setText("Stop");
            onKvkStartBtnClick();
        }
        else{
            startbt.setText("Start");
            onKvkStopBtnClick();
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
    // TODO: 04.01.2021 Fehler im Thread 
    private void onKvkStartBtnClick() {
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

    private void onKvkStopBtnClick() {
        timer.cancel();
    }

    // TODO: 04.01.2021 Welcher Actionstyp ist das bei der ChoiceBox? 
    private void onKvkDelayCbxChange(ActionEvent event) {
        ChoiceBox source = ((ChoiceBox) event.getSource());
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
        onKvkStartBtnClick();
    }
    //----------------- Ki vs Ki End --------------------
}
