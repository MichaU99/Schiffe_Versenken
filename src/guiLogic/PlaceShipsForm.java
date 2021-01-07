package guiLogic;

import JavaFx.GameOptions;
import JavaFx.GameOptionsForm;
import game.*;
import game.cells.Block;
import game.cells.Cell;
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
    private static Label statusLabel;
    private static Thread statusLabelThread;
    private static final ArrayList<Position> selectedPositions = new ArrayList<>();

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
        statusLabel = new Label();
        statusLabel.getStyleClass().add("lbl_style_3");

        VBox progressVBox = new VBox();

        Button autofillBtn = new Button("Autofill");
        autofillBtn.getStyleClass().add("btn_style_1");
        autofillBtn.setOnAction(PlaceShipsForm::onAutoFillBtnClick);

        Button removeBtn = new Button("remove ship");
        removeBtn.getStyleClass().add("btn_style_1");
        removeBtn.setOnAction(PlaceShipsForm::onRemoveBtnClick);

        Button removeAllBtn = new Button("remove all ships");
        removeAllBtn.getStyleClass().add("btn_style_2");
        removeAllBtn.setOnAction(event -> {
            game.getField().removeAllShips();
            updateGame1();
            updateLabels();
        });

        Button startBtn = new Button("start game");
        startBtn.getStyleClass().add("btn_style_2");
        startBtn.setOnAction(event -> {
            if (!checkStartConditions()) {
                return;
            }
            if (game instanceof LocalGame) {
                LocalGame localGame = ((LocalGame) game);
                localGame.startGame(gameOptions.getKiStrength());
                primaryStage.setScene(MainGameForm.create(primaryStage, game));
            } else if (game instanceof OnlineHostGame) {
                progressVBox.getChildren().addAll(StaticNodes.centerNode(new ProgressIndicator()),
                        StaticNodes.centerNode(new Label("Waiting for Client")));
                new Thread(() -> {
                    boolean status = game.startGame();
                    if (status) {
                        Platform.runLater(() -> primaryStage.setScene(MainGameForm.create(primaryStage, game)));
                    }
                }).start();

            } else if (game instanceof OnlineClientGame) {
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
        if (!online) {
            labelBtnVBox.getChildren().add(optionsBtn);
        }

        labelBtnVBox.getChildren().addAll(StaticNodes.getVSpacer(), labelVBox, btnHBox, StaticNodes.centerNode(removeAllBtn),
                StaticNodes.centerNode(startBtn), StaticNodes.getVSpacer(), progressVBox, StaticNodes.getVSpacer());

        HBox centerHBox = new HBox();
        centerHBox.getChildren().addAll(game1VBox, StaticNodes.getHSpacer(), labelBtnVBox, StaticNodes.getHSpacer());

        updateLabels();
        //------------------------

        contentVBox.getChildren().addAll(centerHBox, new HBox(StaticNodes.getHSpacer(), statusLabel));
        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainGame.css").toExternalForm());
        return scene;
    }

    private static void updateStatusLabel(String msg, int time) {
        if (statusLabelThread != null) {
            statusLabelThread.interrupt();
        }
        statusLabelThread = new Thread(() -> {
            try {
                Platform.runLater(() -> statusLabel.setText(msg));
                Thread.sleep(time);
                Platform.runLater(() -> statusLabel.setText(""));
                statusLabelThread = null;
            } catch (InterruptedException ignored) {
                // triggered when user does something wrong to free the object
            }
        });
        statusLabelThread.start();
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
        if(!game.getField().addShipRandomKeepShips(shipsLeft)) {
            updateStatusLabel("Ki couldn't place every ship", 5000);
        }
        updateGame1();
        updateLabels();
    }

    private static void onRemoveBtnClick(ActionEvent event) {
        if (selectedPositions.size() == 1) {
            if(!game.getField().removeShip(selectedPositions.get(0))) {
                updateStatusLabel("there is no ship", 3000);
            }
            updateGame1();
            updateLabels();
            selectedPositions.clear();
        } else {
            updateStatusLabel("click on a Ship first", 3000);
        }
    }

    private static void onOptionsBtnClick(ActionEvent event) {
        Stage secondaryStage = new Stage();
        secondaryStage.setOnHiding(windowEvent -> updateGame());
        secondaryStage.setScene(GameOptionsForm.create(secondaryStage));
        secondaryStage.show();
    }

    private static void updateGame() {
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

    private static boolean checkStartConditions() {
        Integer[] placedShips = game.getField().getShipCountByClass();
        int leftDestroyers = gameOptions.getDestroyer() - placedShips[3];
        int leftCruisers = gameOptions.getCruiser() - placedShips[2];
        int leftBattleships = gameOptions.getBattleship() - placedShips[1];
        int leftCarriers = gameOptions.getCarrier() - placedShips[0];

        if (leftCarriers > 0) {
            updateStatusLabel("Not all size 5 Ships placed!", 3000);
            return false;
        } else if (leftCarriers < 0) {
            updateStatusLabel("Too many size 5 Ships placed, remove some!", 3000);
            return false;
        }

        if (leftBattleships > 0) {
            updateStatusLabel("Not all size 4 Ships placed!", 3000);
            return false;
        } else if (leftBattleships < 0) {
            updateStatusLabel("Too many size 4 Ships placed, remove some!", 3000);
            return false;
        }

        if (leftCruisers > 0) {
            updateStatusLabel("Not all size 3 Ships placed!", 3000);
            return false;
        } else if (leftCruisers < 0) {
            updateStatusLabel("Too many size 3 Ships placed, remove some!", 3000);
            return false;
        }

        if (leftDestroyers > 0) {
            updateStatusLabel("Not all size 2 Ships placed!", 3000);
            return false;
        } else if (leftDestroyers < 0) {
            updateStatusLabel("Too many size 2 Ships placed, remove some!", 3000);
            return false;
        }

        return true;
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

        if (selectedPositions.size() == 1) {
            Cell c = game.getField().getCell(selectedPositions.get(0));
            if (c instanceof Block) {
                updateStatusLabel("no valid position", 3000);
                selectedPositions.clear();
                updateGame1();
            } else if (c.getClass() == Cell.class){
                maskField();
            }
        } else if (selectedPositions.size() == 2) {
            //check if positions are in a valid constellation
            if (selectedPositions.get(0).equals(selectedPositions.get(1))) {
                // Click on same Cell twice
                selectedPositions.clear();
                updateGame1();
            } else if ((selectedPositions.get(0).getX() != selectedPositions.get(1).getX()) &&
                    (selectedPositions.get(0).getY() != selectedPositions.get(1).getY())) {
                // invalid choice
                updateStatusLabel("invalid choice", 5000);
                selectedPositions.clear();
                updateGame1();
            } else {
                ArrayList<Position> shipPositions = new ArrayList<>();
                if (selectedPositions.get(0).getX() == selectedPositions.get(1).getX()) {
                    //ship vertical
                    for (int i = Math.min(selectedPositions.get(0).getY(), selectedPositions.get(1).getY());
                         i <= Math.max(selectedPositions.get(0).getY(), selectedPositions.get(1).getY());
                         i++) {
                        shipPositions.add(new Position(selectedPositions.get(0).getX(), i));
                    }
                } else {
                    //ship horizontal
                    for (int i = Math.min(selectedPositions.get(0).getX(), selectedPositions.get(1).getX());
                         i <= Math.max(selectedPositions.get(0).getX(), selectedPositions.get(1).getX());
                         i++) {
                        shipPositions.add(new Position(i, selectedPositions.get(0).getY()));
                    }
                } if (shipPositions.size() >= 2 && shipPositions.size() <= longestRemainingShip()) {
                    Integer[] placedShips = game.getField().getShipCountByClass();
                    int l;
                    switch (shipPositions.size()) {
                        case 2:
                            l = gameOptions.getDestroyer() - placedShips[3];
                            if (l > 0) {
                                if (game.getField().addShip(new Ship(shipPositions))) {
                                    Label lbl = ((Label) labelVBox.getChildren().get(4));
                                    lbl.setText("Size 2 Ships left: \t" + (l - 1));
                                } else {
                                    updateStatusLabel("invalid position", 3000);
                                }
                            }
                            break;
                        case 3:
                            l = gameOptions.getCruiser() - placedShips[2];
                            if (l > 0) {
                                if (game.getField().addShip(new Ship(shipPositions))) {
                                    Label lbl = ((Label) labelVBox.getChildren().get(3));
                                    lbl.setText("Size 3 Ships left: \t" + (l - 1));
                                }
                                else {
                                    updateStatusLabel("invalid position", 3000);
                                }
                            }
                            break;
                        case 4:
                            l = gameOptions.getBattleship() - placedShips[1];
                            if (l > 0) {
                                if (game.getField().addShip(new Ship(shipPositions))) {
                                    Label lbl = ((Label) labelVBox.getChildren().get(2));
                                    lbl.setText("Size 4 Ships left: \t" + (l - 1));
                                }
                                else {
                                    updateStatusLabel("invalid position", 3000);
                                }
                            }
                            break;
                        case 5:
                            l = gameOptions.getCarrier() - placedShips[0];
                            if (l > 0) {
                                if (game.getField().addShip(new Ship(shipPositions))) {
                                    Label lbl = ((Label) labelVBox.getChildren().get(1));
                                    lbl.setText("Size 5 Ships left: \t" + (l - 1));
                                }
                                else {
                                    updateStatusLabel("invalid position", 3000);
                                }
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    updateStatusLabel("invalid Position", 3000);
                }
                selectedPositions.clear();
                updateGame1();
            }
        }
    }

    private static int longestRemainingShip() {
        int longestRemShip;
        Integer[] placedShips = game.getField().getShipCountByClass();
        if (gameOptions.getCarrier() - placedShips[0] > 0) {
            longestRemShip = 5;
        } else if (gameOptions.getBattleship() - placedShips[1] > 0) {
            longestRemShip = 4;
        } else if (gameOptions.getCruiser() - placedShips[2] > 0) {
            longestRemShip = 3;
        } else if (gameOptions.getDestroyer() - placedShips[3] > 0) {
            longestRemShip = 2;
        } else {
            return -1;
        }
        return longestRemShip;
    }

    private static void maskField() {
        int longestRemShip = longestRemainingShip();

        ArrayList<Position> validPositions = new ArrayList<>();
        validPositions.add(selectedPositions.get(0));
        int startX = selectedPositions.get(0).getX();
        int startY = selectedPositions.get(0).getY();
        boolean east = true;
        boolean south = true;
        boolean west = true;
        boolean north = true;
        for (int i = 1; i < longestRemShip; i++) {
            if (east && (game.getField().getCell(new Position(startX + i, startY)) instanceof Ship ||
                    game.getField().getCell(new Position(startX + i, startY)) instanceof Block)) {
                east = false;
            } else {
                if (east) {
                    validPositions.add(new Position(startX + i, startY));
                }
            }
            if (west && (game.getField().getCell(new Position(startX - i, startY)) instanceof Ship ||
                    game.getField().getCell(new Position(startX - i, startY)) instanceof Block)){
                west = false;
            } else {
                if (west) {
                    validPositions.add(new Position(startX - i, startY));
                }
            }
            if (south && (game.getField().getCell(new Position(startX, startY + i)) instanceof Ship ||
                    game.getField().getCell(new Position(startX, startY + i)) instanceof Block)) {
                south = false;
            } else {
                if (south) {
                    validPositions.add(new Position(startX, startY + i));
                }
            }
            if (north && (game.getField().getCell(new Position(startX, startY - i)) instanceof Ship ||
                    game.getField().getCell(new Position(startX, startY - i)) instanceof Block)) {
                north = false;
            } else {
                if (north) {
                    validPositions.add(new Position(startX, startY - i));
                }
            }
        }

        for (int i = 0; i < game.getField().getHeight(); i++) {
            HBox row = ((HBox) game1VBox.getChildren().get(i));
            for (int j = 0; j < game.getField().getLength(); j++) {
                if (!validPositions.contains(new Position(j, i))) {
                    HBox cell = ((HBox) row.getChildren().get(j));
                    cell.getStyleClass().remove(1);
                    if (!(game.getField().getCell(new Position(cell.getId().split(":"))) instanceof Ship)) {
                        cell.getStyleClass().add("light_red_bg");
                    }
                }
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
                } else {
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
}
