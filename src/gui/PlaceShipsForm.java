package gui;

import game.*;
import game.cells.Ship;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class PlaceShipsForm {

    private static final int VBOX_FIELD_SIZE = 800;
    private static Game game;
    private static VBox game1VBox;
    private static GameOptions gameOptions;
    private static VBox labelVBox;
    private static ArrayList<Position> selectedPositions = new ArrayList<>();

    public static Scene create(Stage primaryStage, boolean online, Game g) {
        primaryStage.setTitle("Main Game");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(860);

        if (!online) {
            loadGameOptions();
            PlaceShipsForm.game = new LocalGame(gameOptions.getFieldSize(), gameOptions.getFieldSize(), gameOptions.getKiStrength());
        }
        else {
            game = g;
            gameOptions = ((OnlineGame) g).gameOptions;
        }

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        //------------------------
        Label lbl0 = new Label("Ship summarization");
        lbl0.getStyleClass().add("lbl_style_2");
        Label lbl1 = new Label();
        lbl1.getStyleClass().add("lbl_style_1");
        Label lbl2 = new Label();
        lbl2.getStyleClass().add("lbl_style_1");
        Label lbl3 = new Label();
        lbl3.getStyleClass().add("lbl_style_1");
        Label lbl4 = new Label();
        lbl4.getStyleClass().add("lbl_style_1");

        initLabels(lbl1, lbl2, lbl3, lbl4);

        VBox progressVBox = new VBox();

        Button autofillBtn = new Button("Autofill");
        autofillBtn.getStyleClass().add("btn_style_1");
        autofillBtn.setOnAction(PlaceShipsForm::onAutoFillBtnClick);

        Button removeBtn = new Button("remove ship");
        removeBtn.getStyleClass().add("btn_style_1");
        removeBtn.setOnAction(PlaceShipsForm::onRemoveBtnClick);

        Button startBtn = new Button("start game");
        startBtn.getStyleClass().add("btn_style_2");
        startBtn.setOnAction(event -> {
            if (game instanceof LocalGame) {
                LocalGame localGame = ((LocalGame) game);
                localGame.startGame(gameOptions.getKiStrength());
                primaryStage.setScene(MainGameForm.create(primaryStage, game));
            }
            else if (game instanceof OnlineHostGame) {
                progressVBox.getChildren().addAll(StaticNodes.centerNode(new ProgressIndicator()),
                        StaticNodes.centerNode(new Label("Waiting for Client")));
                new Thread(() -> {
                    boolean status = game.startGame();
                    if (status) {
                        Platform.runLater(() -> primaryStage.setScene(MainGameForm.create(primaryStage, game)));
                    }
                }).start();

            }
            else if (game instanceof OnlineClientGame) {
                progressVBox.getChildren().addAll(StaticNodes.centerNode(new ProgressIndicator()),
                        StaticNodes.centerNode(new Label("Waiting for Host")));
                new Thread(() -> {
                    boolean status = game.startGame();
                    if (status) {
                        Platform.runLater(() -> primaryStage.setScene(MainGameForm.create(primaryStage, game)));
                    }
                }).start();
            }
        });

        Button optionsBtn = new Button("Options");
        optionsBtn.getStyleClass().add("btn_style_2");
        optionsBtn.setOnAction(PlaceShipsForm::onOptionsBtnClick);

        game1VBox = createGame1();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(game1VBox);

        HBox btnHBox = new HBox();
        btnHBox.getChildren().addAll(autofillBtn, StaticNodes.getHSpacer(), removeBtn);

        labelVBox = new VBox(15);
        labelVBox.getChildren().addAll(lbl0, lbl1, lbl2, lbl3, lbl4);

        VBox labelBtnVBox = new VBox(10);
        if (!online)
            labelBtnVBox.getChildren().add(optionsBtn);
        labelBtnVBox.getChildren().addAll(StaticNodes.getVSpacer(), labelVBox, btnHBox, StaticNodes.centerNode(startBtn),
                StaticNodes.getVSpacer(), progressVBox, StaticNodes.getVSpacer());

        HBox centerHBox = new HBox();
        centerHBox.getChildren().addAll(game1VBox, StaticNodes.getHSpacer(), labelBtnVBox, StaticNodes.getHSpacer());
        //------------------------

        contentVBox.getChildren().add(centerHBox);
        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainGame.css").toExternalForm());
        return scene;
    }

    private static void onAutoFillBtnClick(ActionEvent event) {
        Integer[] placedShips = game.getField().getShipCountByClass();
        ArrayList<Integer> shipsLeft = new ArrayList<>();
        for (int i = 0; i < gameOptions.getCarrier() - placedShips[0]; i++) {
            shipsLeft.add(5);
        }
        for (int i = 0; i < gameOptions.getBattleship() - placedShips[1]; i++) {
            shipsLeft.add(4);
        }
        for (int i = 0; i < gameOptions.getCruiser() - placedShips[2]; i++) {
            shipsLeft.add(3);
        }
        for (int i = 0; i < gameOptions.getDestroyer() - placedShips[3]; i++) {
            shipsLeft.add(2);
        }
        game.getField().addShipRandomKeepShips(shipsLeft);
        updateGame1();
        updateLabels();
    }

    private static void onRemoveBtnClick(ActionEvent event) {
        if (selectedPositions.size() == 1) {
            game.getField().removeShip(selectedPositions.get(0));
            updateGame1();
            updateLabels();
            selectedPositions.clear();
        }
    }

    private static void onOptionsBtnClick(ActionEvent event) {
        Stage secondaryStage = new Stage();
        secondaryStage.setOnHiding(windowEvent -> { //TODO check what happens when use sets less ships then currently on field
            updateGame();
        });
        secondaryStage.setScene(GameOptionsForm.create(secondaryStage));
        secondaryStage.show();
    }

    private static void updateGame() {
        //TODO update ki strength
        loadGameOptions();
        game.getField().resizeField(gameOptions.getFieldSize(), gameOptions.getFieldSize());
        updateGame1();
        updateLabels();
    }

    private static void updateLabels() {
        Integer[] placedShips = game.getField().getShipCountByClass();
        Label lbl4 = ((Label) labelVBox.getChildren().get(4));
        lbl4.setText("Size 2 Ships left: \t" + (gameOptions.getDestroyer() - placedShips[3]));

        Label lbl3 = ((Label) labelVBox.getChildren().get(3));
        lbl3.setText("Size 3 Ships left: \t" + (gameOptions.getCruiser() - placedShips[2]));

        Label lbl2 = ((Label) labelVBox.getChildren().get(2));
        lbl2.setText("Size 4 Ships left: \t" + (gameOptions.getBattleship() - placedShips[1]));

        Label lbl1 = ((Label) labelVBox.getChildren().get(1));
        lbl1.setText("Size 5 Ships left: \t" + (gameOptions.getCarrier() - placedShips[0]));
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
                cell.getStyleClass().addAll("general_cell_style", "water_blue_bg");
                cell.setOnMouseClicked(PlaceShipsForm::onCellClick);

                row.getChildren().add(cell);
            }
            vBox.getChildren().add(row);
        }

        return vBox;
    }

    private static void onCellClick(MouseEvent event) {
        HBox cell = ((HBox) event.getSource());
        cell.getStyleClass().add("red_border");
        selectedPositions.add(new Position(cell.getId().split(":")));

        if (selectedPositions.size() == 2) {
            //check if positions are in a valid constellation
            if (selectedPositions.get(0).equals(selectedPositions.get(1))) {
                // Click on same Cell twice
                selectedPositions.clear();
                updateGame1();
            }
            else if ((selectedPositions.get(0).getX() != selectedPositions.get(1).getX()) &&
                    (selectedPositions.get(0).getY() != selectedPositions.get(1).getY())) {
                // invalid choice
                selectedPositions.clear();
                updateGame1();
            }
            else {
                ArrayList<Position> shipPositions = new ArrayList<>();
                if (selectedPositions.get(0).getX() == selectedPositions.get(1).getX()) {
                    //ship vertical
                    for (int i = Math.min(selectedPositions.get(0).getY(), selectedPositions.get(1).getY());
                         i <= Math.max(selectedPositions.get(0).getY(), selectedPositions.get(1).getY());
                         i++) {
                        shipPositions.add(new Position(selectedPositions.get(0).getX(), i));
                    }
                }
                else {
                    //ship horizontal
                    for (int i = Math.min(selectedPositions.get(0).getX(), selectedPositions.get(1).getX());
                         i <= Math.max(selectedPositions.get(0).getX(), selectedPositions.get(1).getX());
                         i++) {
                        shipPositions.add(new Position(i, selectedPositions.get(0).getY()));
                    }
                }
                if (shipPositions.size() >= 2 && shipPositions.size() <= 5) {
                    Integer[] placedShips = game.getField().getShipCountByClass();
                    int l;
                    switch (shipPositions.size()) {
                        case 2:
                            l = gameOptions.getDestroyer() - placedShips[3];
                            if (l > 0) {
                                game.getField().addShip(new Ship(shipPositions));
                                Label lbl = ((Label) labelVBox.getChildren().get(4));
                                lbl.setText("Size 2 Ships left: \t" + (l - 1));
                            }
                            break;
                        case 3:
                            l = gameOptions.getCruiser() - placedShips[2];
                            if (l > 0) {
                                game.getField().addShip(new Ship(shipPositions));
                                Label lbl = ((Label) labelVBox.getChildren().get(3));
                                lbl.setText("Size 3 Ships left: \t" + (l - 1));
                            }
                            break;
                        case 4:
                            l = gameOptions.getBattleship() - placedShips[1];
                            if (l > 0) {
                                game.getField().addShip(new Ship(shipPositions));
                                Label lbl = ((Label) labelVBox.getChildren().get(2));
                                lbl.setText("Size 4 Ships left: \t" + (l - 1));
                            }
                            break;
                        case 5:
                            l = gameOptions.getCarrier() - placedShips[0];
                            if (l > 0) {
                                game.getField().addShip(new Ship(shipPositions));
                                Label lbl = ((Label) labelVBox.getChildren().get(1));
                                lbl.setText("Size 5 Ships left: \t" + (l - 1));
                            }
                            break;
                        default:
                            break;
                    }
                }
                selectedPositions.clear();
                updateGame1();
            }
        }
    }

    private static void updateGame1() {
        game1VBox.getChildren().clear();
        for (int i = 0; i < game.getField().getHeight(); i++) {
            HBox row = new HBox();
            VBox.setVgrow(row, Priority.ALWAYS);
            for (int j = 0; j < game.getField().getLength(); j++) {
                HBox cell = new HBox();
                HBox.setHgrow(cell, Priority.ALWAYS);
                cell.setId(j + ":" + i);
                cell.setOnMouseClicked(PlaceShipsForm::onCellClick);
                cell.getStyleClass().add("general_cell_style");
                if (game.getField().getCell(new Position(j, i)) instanceof Ship) {
                    cell.getStyleClass().add("ship_grey_bg");
                }
                else {
                    cell.getStyleClass().add("water_blue_bg");
                }
                row.getChildren().add(cell);
            }
            game1VBox.getChildren().add(row);
        }
    }

    private static void loadGameOptions() {
        try (ObjectInputStream os = new ObjectInputStream(new FileInputStream("game.options"))) {
            gameOptions = ((GameOptions) os.readObject());
        } catch (FileNotFoundException e) {
            gameOptions = new GameOptions();
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("game.options"))) {
                os.writeObject(gameOptions);
                os.flush();
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void initLabels(Label lbl1, Label lbl2, Label lbl3, Label lbl4) {
        lbl1.setText("Size 5 Ships left: \t" + gameOptions.getCarrier());
        lbl2.setText("Size 4 Ships left: \t" + gameOptions.getBattleship());
        lbl3.setText("Size 3 Ships left: \t" + gameOptions.getCruiser());
        lbl4.setText("Size 2 Ships left: \t" + gameOptions.getDestroyer());
    }
}
