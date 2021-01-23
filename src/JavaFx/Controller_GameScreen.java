package JavaFx;

import game.*;
import game.cells.Ship;
import game.cells.Shot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    private int schiffeZerstoert = 0;
    private Timer timer;
    private int timerInterval = 1000;
    public static Game game;
    public static boolean saveGame = false;
    String waterCell = "-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    String shipCell = "-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    String markCell = "-fx-border-width:0.5em;  -fx-margin: 1 1 1 1;-fx-border-color: #ffccff;-fx-pref-height: 1em;-fx-pref-width:1em;-fx-min-height: 1em;-fx-min-width: 1em";

    String shotWater = "-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    String shotShip = "-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    Position markedPos = null; //Speichert welche Felder bereits aufgedeckt wurden und welche nicht
    private String PLAYER1_NAME = "YOU";
    private String PLAYER2_NAME = "ENEMY";
    Thread thread;

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
    @FXML
    private Button auto_btn;

    /**
     * TL;DR: Initialisiert die GUI abhängig vom Spieltyp.
     * Allgemein:
     * Entfernt die Constraints der GridPane um über die css-Constraints eine richtig scalierende GP zu bekommen,
     * danach wird das eigene Feld über updateField initialisiert und das MouseClickEvent sicherheitshalber initialisiert.
     * <p>
     * Für saveGames:
     * Die Methode startGame wird nicht aufgerufen um das Spiel nicht zu resetten.
     * <p>
     * Für KivsKiGames:
     * Gegnerisches Feld wird sichtbar initalisiert mit updateFieldUnddisclosed und die richtige Benutzeroberfläche mit setButtons nutzbar gemacht(ki=true)
     * <p>
     * Sonst:
     * Gegenerisches Feld mit updateField initalisiert und richtige Benutzeroberfläche mit setButtons nutzbar gemacht(ki=false)
     * <p>
     * Für OnlineHostGame:
     * Setzt Tag auf eigenen Namen
     * Falls Ki spielt:
     * Benutzeroberfläche wird anders initalisiert
     * <p>
     * Für OnlineClientGame:
     * Setzt PlayerTag und beginnt Spielethread
     * Falls Ki spielt:
     * Benutzeroberfläche anders initalisiert und Shoot_btn ohne MarkedField aktivierbar
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Clears constrains from fxml to have a uniform field
        GP_Enemy.getColumnConstraints().clear();
        GP_Enemy.getRowConstraints().clear();
        GP_Player.getRowConstraints().clear();
        GP_Player.getColumnConstraints().clear();

        /*
        GP_Enemy.setOnMouseClicked(new EventHandler<MouseEvent>() { //Wird entfernt falls eine KI für dich schießt
            @Override
            public void handle(MouseEvent mouseEvent) {
                markField(mouseEvent);
            }
        });
         */
        Platform.runLater(()->updateField(GP_Player));
        if (!saveGame){
            thread= new Thread(()->game.startGame());
            thread.start();
        }


        //Unterscheidet zwischen Beobachteten Spielen (KivsKiGame), in denen beide Felder von anfang an komplett für den Beobachter bekannt sind
        // und normal gespielten Spielern in denen nur ein Feld bekannt ist
        if (game instanceof KiVsKiGame) {
            updateFieldUndisclosed(GP_Enemy);
            setButtons(true);
        } else {
            Platform.runLater(()->updateField(GP_Enemy));
            setButtons(false);
            //Unterscheidet zwischen den verschiedenen gespielten Spieltypen
            if (game instanceof OnlineHostGame) {
                if (game.kiPlays) {
                    auto_btn.setVisible(true);
                    auto_btn.setDisable(false);
                    Shoot_bt.setDisable(false);
                    GP_Enemy.setOnMouseClicked(null);
                }
                playerTag.setText(PLAYER1_NAME);
            } else if (game instanceof OnlineClientGame) {
                playerTag.setText(PLAYER2_NAME);
                OnlineClientGame clientGame = ((OnlineClientGame) game);
                if (clientGame.kiPlays) {
                    auto_btn.setVisible(true);
                    auto_btn.setDisable(false);
                    Shoot_bt.setDisable(true);
                    GP_Enemy.setOnMouseClicked(null);
                    while ( thread!=null && thread.isAlive()){}
                    thread=new Thread(() -> {
                        while (!clientGame.isMyTurn()) { //Boolean als objekt
                            clientGame.enemyShot();
                            Platform.runLater(() -> updateField(GP_Player));
                            Platform.runLater(()->checkGameEnded(1));
                        }
                        Shoot_bt.setDisable(false);
                        Platform.runLater(() -> playerTag.setText(PLAYER1_NAME));
                    });
                    thread.start();
                } else {
                    while ( thread!=null && thread.isAlive()){}
                    thread=new Thread(() -> {
                        while (!clientGame.isMyTurn()) { //Boolean als objekt
                            clientGame.enemyShot();
                            Platform.runLater(() -> updateField(GP_Player));
                            Platform.runLater(()->checkGameEnded(1));
                        }
                        Platform.runLater(() ->{
                            playerTag.setText(PLAYER1_NAME);
                            Shoot_bt.setDisable(false);}
                            );
                    });
                    thread.start();
                }
            }
        }
    }


    /**
     * Aktiviert die für den Spieltyp relevanten Buttons.
     * Unterscheidet nur zwischen KivsKi Modus und sonst.
     * <p>
     * Folgende Elemente werden aktiviert bzw. deaktiviert je nach ki:
     * <p>
     * startbt + gamespdbox (choiceBox) -> ki=true
     * <p>
     * Shoot_bt + GP_Enemy MouseClickEvent ->ki=false
     *
     * @param ki true-> game ist kivski game; false-> game is not ki game
     */
    public void setButtons(Boolean ki) {
        auto_btn.setDisable(true);
        auto_btn.setVisible(false);
        if (ki) {

            Shoot_bt.setDisable(true);
            Shoot_bt.setVisible(false);
            startbt.setVisible(true);
            gamespdbox.setVisible(true);
            startbt.setDisable(false);
            gamespdbox.setDisable(false);

            GP_Enemy.setOnMouseClicked(null);
            gamespdbox.setValue("1x");
            gamespdbox.getItems().addAll("0.25x", "0.5x", "1x", "2x", "4x");
        } else {
            GP_Enemy.setOnMouseClicked(this::markField);
            Shoot_bt.setDisable(true);
            Shoot_bt.setVisible(true);
            startbt.setVisible(false);
            gamespdbox.setVisible(false);
            startbt.setDisable(true);
            gamespdbox.setDisable(true);
        }
    }

    /**
     * Markiert Felder auf die geschossen werden soll,
     * entfernt bei einer zweiten Markierung die Erste.
     *
     * @param event
     */
    public void markField(MouseEvent event) {
        int x, y;
        try {
            x = GridPane.getColumnIndex((Node) event.getTarget());
            y = GridPane.getRowIndex((Node) event.getTarget());
        } catch (NullPointerException doNothing) {
            return;
        }
        if(this.game.getEnemyField().getPlayfield()[y][x].getClass()== Shot.class) return;
        HBox cell;
        if (markedPos != null) {
            cell = new HBox();
            if (game.getEnemyField().getCell(markedPos) instanceof Shot) {
                Shot s = ((Shot) game.getEnemyField().getCell(markedPos));
                if (s.getWasShip()) {
                    cell.setStyle(shotShip);
                    cell.getStyleClass().add("cross");
                } else {
                    cell.setStyle(shotWater);
                    cell.getStyleClass().add("crossW");
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
        cell.getStyleClass().add("bomb");
        GridPane.setConstraints(cell, markedPos.getX(), markedPos.getY());
        GP_Enemy.getChildren().add(cell);
        if (game.isMyTurn()) Shoot_bt.setDisable(false);
    }

    /**
     * Methode des Buttons Shoot_bt, ruft die shoot Methode des Spiel mit der aktuell markierten Position auf,
     * reagiert auf Rückgabewert (rc) mit unterschiedlichern Handlungen.
     * <p>
     * (rc=0 Wasser getroffen, rc=1 Schiff getroffen, rc=2 Schiff versenkt).
     * <p>
     * Unterscheidet zwischen localem und OnlineSpiel
     *
     * @param event wird nicht verwendet, ist nur wegen der Button Einbindung notwendig
     */
    public void shootbtn(ActionEvent event) {
        Position tmpPos = markedPos;
        markedPos = null;
        LastShotTag.setVisible(true);
        Shoot_bt.setDisable(true);
        if(tmpPos==null && ((game instanceof OnlineHostGame && !game.kiPlays)||(game instanceof OnlineClientGame && !game.kiPlays))) return;
        if (game instanceof LocalGame && game.isMyTurn()) {
            if (tmpPos == null) return;
            int rc = game.shoot(tmpPos);
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
                            if (markedPos != null) Shoot_bt.setDisable(false);
                            break;
                        } else if (rc1 == 2) {
                            Platform.runLater(() -> LastShotTag.setText("Destroyed"));
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
            int rc = onlineGame.shoot(tmpPos);
            updateField(GP_Enemy);

            if (rc == 0) {
                playerTag.setText(PLAYER2_NAME);
                LastShotTag.setText("Last Shot: Miss");
                new Thread(() -> {
                    while (!onlineGame.isMyTurn()) {
                        onlineGame.enemyShot();
                        Platform.runLater(() -> updateField(GP_Player));
                        Platform.runLater(()->checkGameEnded(1));
                    }
                    if (markedPos != null || (game instanceof OnlineHostGame && game.kiPlays) || (game instanceof OnlineClientGame && game.kiPlays))
                        Shoot_bt.setDisable(false);
                    Platform.runLater(() -> playerTag.setText(PLAYER1_NAME));
                }).start();
            } else if (rc == 1) {
                LastShotTag.setText("Last Shot: Hit");
                if ((game instanceof OnlineHostGame && game.kiPlays) || (game instanceof OnlineClientGame && game.kiPlays))
                    Shoot_bt.setDisable(false);
            } else if (rc == 2) {
                LastShotTag.setText("Destroyed");
                Platform.runLater(()->checkGameEnded(0));
            }
        }
    }

    /**
     * Event for both the start and stop Button in KIvsKIGames.
     * Button changes Text when clicked on it Start->Stop->Start
     *
     * @param event Actionevent on ButtonClick
     */
    // TODO: 10.01.2021 Methode sinnvoll?
    public void startstopbtnOnClick(ActionEvent event) {
        if (startbt.getText().equals("Start")) {
            startbt.setText("Stop");
            onKvkStartBtnClick();
        } else {
            startbt.setText("Start");
            onKvkStopBtnClick();
        }
    }

    /**
     * Überprüft ob das Spiel vorbei ist (alle Schiffe eines Spielers zerstört sind)
     * und zeigt bei Spielende das Gegnerfeld + passendes alert
     * who=0-> Checkt ob Spiel gewonnen
     * who=1 -> Checkt ob Spiel verloren
     */
    private void checkGameEnded(int who) {
        Stage stage;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Ended!");

        switch (who) {
            case 0:
                game.getEnemyField().decreaseshipAmount();
            if (game.getEnemyField().getShipAmount()<=0) {
                updateFieldUndisclosed(GP_Player);
                updateFieldUndisclosed(GP_Enemy);
                alert.setContentText("You won!");
                alert.showAndWait();
                stage = (Stage) (ap.getScene().getWindow());
                stage.close();
            }
            break;
            case 1:
                if (game.didYouLose()) {
                updateFieldUndisclosed(GP_Player);
                updateFieldUndisclosed(GP_Enemy);
                alert.setContentText("You lost!");
                alert.showAndWait();
                stage = (Stage) (ap.getScene().getWindow());
                stage.close();
            }
                break;
            default: assert (false):"FEHLER IM AUFRUF DER whoWon(who)";
        }
    }

        private void checkGameEnded() {
            Stage stage;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Game Ended!");
            switch (game.whoWon()) {
                case 0:
                    updateFieldUndisclosed(GP_Player);
                    updateFieldUndisclosed(GP_Enemy);
                    alert.setContentText("You won!");
                    alert.showAndWait();
                    stage = (Stage) (ap.getScene().getWindow());
                    stage.close();
                    break;
                case 1:
                    updateFieldUndisclosed(GP_Player);
                    updateFieldUndisclosed(GP_Enemy);
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
                    if (game.getEnemyField().getCell(new Position(x, y)) instanceof Shot) {
                        Shot s = ((Shot) game.getEnemyField().getCell(new Position(x, y)));
                        if (s.getWasShip()) {
                            cell.setStyle(shotShip);
                            cell.getStyleClass().add("cross");
                        } else {
                            cell.setStyle(shotWater);
                            cell.getStyleClass().add("crossW");
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
                            cell.getStyleClass().add("cross");
                        } else {
                            cell.setStyle(shotWater);
                            cell.getStyleClass().add("crossW");
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

    /**
     * Ändert die aktuelle Scene von Layout_GameScreen auf Layout_NewGame
     * @param event
     * @throws IOException
     */
    public void backToStart(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Stage stage = (Stage) ap.getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        stage.setMinHeight(600);
        stage.setMinWidth(800);
        stage.setScene(scene);
        stage.setTitle("SchiffeVersenken");
        stage.centerOnScreen();
        stage.show();
    }

    /**
     * Speichert das aktuelle GFame in eine Save Datei mit passender Dateiendung
     * @param event
     */
    public void onSaveClick(ActionEvent event) {
        Stage primaryStage= (Stage) ap.getScene().getWindow();
        FileChooser fs = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter;
        fs.setInitialDirectory(new File("./"));
        if (game instanceof LocalGame) {
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.lsave)", "*.lsave");
        } else if (game instanceof OnlineGame) {
            if (!game.isMyTurn()) return;
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.hsave)", "*.hsave");
        } else {
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.ksave)", "*.ksave");
        }

        fs.getExtensionFilters().add(extensionFilter);
        File file = fs.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                if (game instanceof OnlineHostGame) {
                    ((OnlineGame) game).saveGame(file);
                }
                else if(game instanceof OnlineClientGame){
                    ((OnlineClientGame)game).saveGameAsHostGame(file);
                }
                else {
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
    private void updateFieldUndisclosed(GridPane gridPane) {
        gridPane.getChildren().clear();
        for (int x = 0; x < game.getField().getLength(); x++) {
            for (int y = 0; y < game.getField().getHeight(); y++) {
                HBox cell = new HBox();
                if (gridPane.equals(GP_Enemy)) {
                    if (game.getEnemyField().getCell(new Position(x, y)) instanceof Shot) {
                        Shot s = ((Shot) game.getEnemyField().getCell(new Position(x, y)));
                        if (s.getWasShip()) {
                            cell.setStyle(shotShip);
                            cell.getStyleClass().add("cross");
                        } else {
                            cell.setStyle(shotWater);
                            cell.getStyleClass().add("crossW");
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
                        cell.getStyleClass().add("cross");

                    }
                    else {
                        cell.setStyle(shotWater);
                        cell.getStyleClass().add("crossW");

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

    /**
     * Event für Ki im OnlineSpiel
     *
     * Lässt deine Ki in regelmäßigen Abständen automatisch schießen.
     * @param event
     */
    public void auto_btnClick(ActionEvent event){
        if(auto_btn.getText().equals("Auto")){
            auto_btn.setText("Stop");
            Shoot_bt.setDisable(true);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    OnlineGame onlineGame = ((OnlineGame) game);
                    int rc = onlineGame.shoot(null);
                    Platform.runLater(()->updateField(GP_Enemy));

                    if (rc == 0) {
                        Platform.runLater(()->{
                            playerTag.setText(PLAYER2_NAME);
                            LastShotTag.setText("Last Shot: Miss");
                        });
                            while (!onlineGame.isMyTurn()) {
                                onlineGame.enemyShot(); // TODO: 15.01.2021  Kann Fehler verursachen
                                Platform.runLater(()-> {
                                    checkGameEnded(1);
                                    updateField(GP_Player);
                                });
                            }

                            Platform.runLater(() ->{
                                playerTag.setText(PLAYER1_NAME);
                            });
                    } else if (rc == 1) {
                        Platform.runLater(()->{
                            LastShotTag.setText("Last Shot: Hit");
                        });

                    } else if (rc == 2) {
                        Platform.runLater(()->{
                            LastShotTag.setText("Destroyed");
                            checkGameEnded(0);
                        });

                    }
                }
            }, 0, timerInterval);
            // TODO: 09.01.2021 Soll eine Endlosschleife beginnen die shooted und Gegnerische Shots abwartet bis entweder das Spiel vorbei ist oder der Button nochmal geklickt wird
        }
        else{
            timer.cancel();
            auto_btn.setText("Auto");
            Shoot_bt.setDisable(false);
            timer.cancel();
            // TODO: 09.01.2021 Beendet den Thread oben und legt den Schussbefehl wieder auf den Shoot_btn
        }
    }


    //----------------- Ki vs Ki Methods ----------------

    /**
     * Event für das KivsKiGame
     * Startet einen timed Thread der im Abstand timerIntervall schießt und die Felder updated
     */
    private void onKvkStartBtnClick() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.shoot(null);
                Platform.runLater(()->{ updateField(GP_Enemy);
                updateField(GP_Player);
                });
            }
        }, 0, timerInterval);
    }

    /**
     * Beendet dem Thread in onKvkStartBtnClick()
     */
    private void onKvkStopBtnClick() {
        timer.cancel();
    }

    /**
     * Ändert die den timerIntervall aus onKvkStartBtnClick() und startet den Thread daraus neu
     * @param event
     */
    // TODO: 10.01.2021 Geschwindigkeit ändert sich erst beim Klick auf die Box statt der Bestätigung
    public void onKvkDelayCbxChange(MouseEvent event) {
        if(timer==null) return;
        ChoiceBox source = ((ChoiceBox) event.getSource());

        gamespdbox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> {

            switch (t1.intValue()){
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

        });

    }
    //----------------- Ki vs Ki End --------------------
}
