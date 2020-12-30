package gui;

import game.*;
import game.cells.Cell;
import game.cells.Ship;
import game.cells.Shot;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainGameForm {

    private static String PLAYER1_NAME;
    private static String PLAYER2_NAME;
    private static final int VBOX_FIELD_SIZE = 800;
    private static Game game;
    private static VBox game1VBox;
    private static VBox game2VBox;
    private static Label curPlayerNameLbl;
    private static Label lastPlayerShotLbl;

    private static Timer timer;
    private static int timerInterval = 1000;

    public static Scene create(Stage primaryStage, Game game) {
        PLAYER1_NAME = "Player 1";
        if (game instanceof LocalGame) {
            primaryStage.setTitle("Local Game");
            PLAYER2_NAME = "KI";
        }
        else if (game instanceof OnlineHostGame) {
            primaryStage.setTitle("Host");
            PLAYER2_NAME = "Enemy Player";
        }
        else {
            primaryStage.setTitle("Client");
            PLAYER2_NAME = "Enemy Player";
        }

//        primaryStage.setX(0);
//        primaryStage.setY(0);
        primaryStage.setWidth(1600);
        primaryStage.setHeight(860);

        MainGameForm.game = game;

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();
        //-------------------------- GENERAL

        game1VBox = createGame1();
        game2VBox = createGame2();
        updateGames();

        VBox game1OuterVBox = new VBox();
        Label game1DescLbl = new Label("Game 1 (Friendly)");
        game1DescLbl.getStyleClass().add("lbl_style_1");
        game1OuterVBox.getChildren().addAll(StaticNodes.centerNode(game1DescLbl), game1VBox);

        VBox game2OuterVBox = new VBox();
        Label game2DescLbl = new Label("Game 2 (Enemy)");
        game2DescLbl.getStyleClass().add("lbl_style_1");
        game2OuterVBox.getChildren().addAll(StaticNodes.centerNode(game2DescLbl), game2VBox);

        VBox currentPlayVBox = new VBox(5);
        Label curPlayerLbl = new Label("Current Player:");
        curPlayerLbl.getStyleClass().add("lbl_style_1");
        curPlayerLbl.setMinWidth(125);
        curPlayerNameLbl = new Label(PLAYER1_NAME);
        curPlayerNameLbl.getStyleClass().add("lbl_style_1");
        curPlayerNameLbl.setMinWidth(100);
        lastPlayerShotLbl = new Label(" ");
        lastPlayerShotLbl.getStyleClass().add("lbl_style_1");
        lastPlayerShotLbl.setMinWidth(100);
        currentPlayVBox.getChildren().addAll(StaticNodes.getVSpacer(), StaticNodes.centerNode(curPlayerLbl),
                StaticNodes.centerNode(curPlayerNameLbl), StaticNodes.centerNode(lastPlayerShotLbl), StaticNodes.getVSpacer());

        HBox gameHBox = new HBox(20);
        gameHBox.getChildren().addAll(game1OuterVBox, StaticNodes.getHSpacer(), currentPlayVBox,
                StaticNodes.getHSpacer(), game2OuterVBox);

        overwriteMenubarMethods(baseVBox, primaryStage);
        //-------------------------- GENERAL END

        contentVBox.getChildren().addAll(gameHBox);

        //-------------------------- SPECIFIC
        if (game instanceof KiVsKiGame) {
            Button startBtn = new Button("Start game");
            startBtn.setOnAction(MainGameForm::onKvkStartBtnClick);

            Button stopBtn = new Button("Stop game");
            stopBtn.setOnAction(MainGameForm::onKvkStopBtnClick);

            ComboBox<String> delayCbx = new ComboBox<>();
            delayCbx.getItems().addAll("0.25x", "0.5x", "1x", "2x", "4x");
            delayCbx.getSelectionModel().select(2);
            delayCbx.setOnAction(MainGameForm::onKvkDelayCbxChange);

            HBox opBtnHBox = new HBox(10);
            opBtnHBox.getChildren().addAll(startBtn, stopBtn, delayCbx);
            contentVBox.getChildren().addAll(opBtnHBox);
        }
        else if (game instanceof OnlineClientGame) {
            curPlayerNameLbl.setText(PLAYER2_NAME);
            OnlineClientGame clientGame = ((OnlineClientGame) game);
            new Thread(() -> {
                while (!clientGame.isMyTurn()) {
                    clientGame.enemyShot();
                    Platform.runLater(MainGameForm::updateGame1);
                }
                Platform.runLater(() -> curPlayerNameLbl.setText(PLAYER1_NAME));
            }).start();
        }
        //-------------------------- SPECIFIC END

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainGame.css").toExternalForm());
        return scene;
    }

    private static VBox createGame1() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(VBOX_FIELD_SIZE);
        vBox.setPrefHeight(VBOX_FIELD_SIZE);

        for (int i = 0; i < game.getField().getHeight(); i++) {
            HBox row = new HBox();
            VBox.setVgrow(row, Priority.ALWAYS);

            for (int j = 0; j < game.getField().getLength(); j++) {
                HBox cell = new HBox();
                HBox.setHgrow(cell, Priority.ALWAYS);
                cell.setId(j + ":" + i);
                if (game.getField().getCell(new Position(j, i)) instanceof Ship)
                    cell.getStyleClass().addAll("general_cell_style", "ship_grey_bg");
                else
                    cell.getStyleClass().addAll("general_cell_style", "water_blue_bg");

                row.getChildren().add(cell);
            }
            vBox.getChildren().add(row);
        }
        return vBox;
    }

    private static void updateGame1() {
        for (int i = 0; i < game.getField().getHeight(); i++) {
            HBox row = ((HBox) game1VBox.getChildren().get(i));
            for (int j = 0; j < game.getField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                cell.getStyleClass().remove(1);

                if (game.getField().getPlayfield()[i][j] instanceof Ship)
                    cell.getStyleClass().add("ship_grey_bg");
                else if (game.getField().getPlayfield()[i][j] instanceof Shot) {
                    Shot s = ((Shot) game.getField().getPlayfield()[i][j]);
                    if (s.getWasShip())
                        cell.getStyleClass().add("hit_red_bg");
                    else
                        cell.getStyleClass().add("miss_purple_bg");
                }
                else
                    cell.getStyleClass().add("water_blue_bg");
            }
        }
    }

    private static VBox createGame2() {
        VBox vBox = new VBox();
        vBox.setPrefWidth(VBOX_FIELD_SIZE);
        vBox.setPrefHeight(VBOX_FIELD_SIZE);

        for (int i = 0; i < game.getEnemyField().getHeight(); i++) {
            HBox row = new HBox();
            VBox.setVgrow(row, Priority.ALWAYS);

            for (int j = 0; j < game.getEnemyField().getLength(); j++) {
                HBox cell = new HBox();
                HBox.setHgrow(cell, Priority.ALWAYS);
                cell.setId(j + ":" + i);
                cell.getStyleClass().addAll("general_cell_style", "water_blue_bg");
                if (!(game instanceof KiVsKiGame)) //don't set event when game is Ki vs Ki!
                    cell.setOnMouseClicked(MainGameForm::onEnemyCellClick);

                row.getChildren().add(cell);
            }
            vBox.getChildren().add(row);
        }

        return vBox;
    }

    private static void updateGame2() {
        for (int i = 0; i < game.getEnemyField().getHeight(); i++) {
            HBox row = ((HBox) game2VBox.getChildren().get(i));
            for (int j = 0; j < game.getEnemyField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                cell.getStyleClass().remove(1);

                if (game instanceof KiVsKiGame) {
                    if (game.getEnemyField().getPlayfield()[i][j] instanceof Ship)
                        cell.getStyleClass().add("ship_grey_bg");
                    else if (game.getEnemyField().getPlayfield()[i][j] instanceof Shot) {
                        Shot s = ((Shot) game.getEnemyField().getPlayfield()[i][j]);
                        if (s.getWasShip())
                            cell.getStyleClass().add("hit_red_bg");
                        else
                            cell.getStyleClass().add("miss_purple_bg");
                    }
                    else
                        cell.getStyleClass().add("water_blue_bg");
                }
                else {
                    Cell c = game.getEnemyField().getCell(new Position(j, i));
                    if (c instanceof Shot) {
                        Shot s = ((Shot) c);
                        if (s.getWasShip())
                            cell.getStyleClass().add("hit_red_bg");
                        else
                            cell.getStyleClass().add("miss_purple_bg");
                    }
                    else
                        cell.getStyleClass().add("water_blue_bg");
                }
            }
        }
    }

    private static void onEnemyCellClick(MouseEvent event) {
        HBox cell = ((HBox) event.getSource());
        if (game instanceof LocalGame && game.isMyTurn()) {
            LocalGame localGame = ((LocalGame) game);
            int rc = localGame.shoot(new Position(cell.getId().split(":")));
            updateGame2();
            if (rc == 0) {
                curPlayerNameLbl.setText(PLAYER2_NAME);
                lastPlayerShotLbl.setText("Last Shot: Miss");
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
                        rc1 = localGame.shoot(null);
                        updateGame1();
                        if (rc1 == 0) {
                            Platform.runLater(() -> curPlayerNameLbl.setText(PLAYER1_NAME));
                            break;
                        } else if (rc1 == 2) {
                            Platform.runLater(MainGameForm::checkGameEnded);
                        }
                    }
                }).start();
            }
            else if (rc == 1) {
                lastPlayerShotLbl.setText("Last Shot: Hit");
            }
            else if (rc == 2) {
                lastPlayerShotLbl.setText("Destroyed");
                Platform.runLater(MainGameForm::checkGameEnded);
            }
        }
        else if ((game instanceof OnlineHostGame || game instanceof OnlineClientGame) && game.isMyTurn()) {
            OnlineGame onlineGame = ((OnlineGame) game);
            int rc = onlineGame.shoot(new Position(cell.getId().split(":")));
            updateGame2();

            if (rc == 0) {
                curPlayerNameLbl.setText(PLAYER2_NAME);
                new Thread(() -> {
                    while (!onlineGame.isMyTurn()) {
                        onlineGame.enemyShot();
                        Platform.runLater(MainGameForm::updateGame1);
                    }
                    Platform.runLater(() -> curPlayerNameLbl.setText(PLAYER1_NAME));
                }).start();
            }
        }
    }

    private static void checkGameEnded() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Game Ended!");
        switch (game.whoWon()) {
            case 0:
                alert.setContentText("You won!");
                alert.showAndWait();
                break;
            case 1:
                alert.setContentText("You lost!");
                alert.showAndWait();
                break;
            default:
                break;
        }
    }

    private static void updateGames() {
        updateGame1();
        updateGame2();
    }

    private static void overwriteMenubarMethods(VBox vBox, Stage primaryStage) {
        MenuBar menuBar = ((MenuBar) vBox.getChildren().get(0));
        menuBar.getMenus().get(0).getItems().get(0).setOnAction(event -> onSaveClick(primaryStage));
        menuBar.getMenus().get(0).getItems().get(1).setOnAction(event -> System.out.println("load"));
    }

    private static void onSaveClick(Stage primaryStage) {
        FileChooser fs = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter;
        if (game instanceof LocalGame) {
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.lsave)", "*.lsave");
        }
        else if (game instanceof OnlineHostGame || game instanceof OnlineClientGame) {
            if (!game.isMyTurn())
                return;
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.hsave)", "*.hsave");
        }
//        else if (game instanceof OnlineClientGame) {
//            if (!game.isMyTurn())
//                return;
//            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.csave)", "*.csave");
//        }
        else {
            extensionFilter = new FileChooser.ExtensionFilter("Save Files (*.ksave)", "*.ksave");
        }

        fs.getExtensionFilters().add(extensionFilter);
        File file = fs.showSaveDialog(primaryStage);
        if (file != null) {
            try {
                if (game instanceof OnlineHostGame) {
                    ((OnlineGame)game).saveGame(file);
                }
                else if (game instanceof OnlineClientGame) {
                    ((OnlineClientGame) game).saveGameAsHostGame(file);
                }
                else {
                    game.saveGame(file.getAbsolutePath());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //----------------- Ki vs Ki Methods ----------------
    private static void onKvkStartBtnClick(ActionEvent event) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                game.shoot(null);
                updateGames();
            }
        }, 0, timerInterval);
    }

    private static void onKvkStopBtnClick(ActionEvent event) {
        timer.cancel();
    }

    private static void onKvkDelayCbxChange(ActionEvent event) {
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

//TODO implement who won