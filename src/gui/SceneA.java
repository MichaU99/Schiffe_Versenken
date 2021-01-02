package gui;

import enums.KiStrength;
import game.LocalGame;
import game.Position;
import game.cells.Cell;
import game.cells.Block;
import game.cells.Ship;
import game.cells.Shot;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class SceneA {

    private static LocalGame localGame = new LocalGame(10, 10, KiStrength.INTERMEDIATE);
    private static VBox playerField;
    private static VBox enemyField;
    private static ArrayList<Position> positions = new ArrayList<>();
    private static GameOptions gameOptions;
    private static Stage stage;

    public static Scene create(Stage stage) {
        SceneA.stage = stage;
        stage.setTitle("Main Game");

        Region spacer = new Region();
        Region spacer2 = new Region();
        Region hSpacer = new Region();
        Region hSpacer2 = new Region();
        Region hSpacer3 = new Region();
        Region hSpacer4 = new Region();
        Region hSpacer5 = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(hSpacer, Priority.ALWAYS);
        HBox.setHgrow(hSpacer2, Priority.ALWAYS);
        HBox.setHgrow(hSpacer3, Priority.ALWAYS);
        HBox.setHgrow(hSpacer4, Priority.ALWAYS);
        HBox.setHgrow(hSpacer5, Priority.ALWAYS);

        Label lblHeading = new Label("Platziere deine Schiffe");
        lblHeading.setStyle("-fx-font-size: 20");
        Label lbl1 = new Label("5er Schiffe zu plazieren: ");
        lbl1.getStyleClass().add("lbl-style");
        Label lbl2 = new Label("4er Schiffe zu plazieren:");
        lbl2.getStyleClass().add("lbl-style");
        Label lbl3 = new Label("3er Schiffe zu plazieren:");
        lbl3.getStyleClass().add("lbl-style");
        Label lbl4 = new Label("2er Schiffe zu plazieren:");
        lbl4.getStyleClass().add("lbl-style");

        Button addShipBtn = new Button("Add Ship");
        addShipBtn.getStyleClass().add("btn-style");
        addShipBtn.setOnAction(SceneA::onAddShipBtnClick);
        Button removeShipBtn = new Button("Remove Ship");
        removeShipBtn.getStyleClass().add("btn-style");
        removeShipBtn.setOnAction(SceneA::onRemoveShipBtnClick);

        Button autoFillBtn = new Button("Autofill");
        autoFillBtn.getStyleClass().add("btn-style");
        autoFillBtn.setOnAction(SceneA::onAutoFillBtnClick);
        Button startBtn = new Button("Start Game");
        startBtn.getStyleClass().add("btn-style");
        startBtn.setOnAction(SceneA::onStartBtnClick);

        loadOptions();

        playerField = createFriendlyGame();
        playerField.setPrefSize(800, 800);
        playerField.setStyle("-fx-border: solid; -fx-border-color: black");

        ScrollPane pane = new ScrollPane();
        pane.setContent(playerField);

        HBox shipOpHBox = new HBox(10);
        shipOpHBox.getChildren().addAll(addShipBtn, hSpacer3, removeShipBtn);

        VBox lblVBox = new VBox(10);
        lblVBox.setPrefSize(400, 400);
        lblVBox.setStyle("-fx-border: solid; -fx-border-color: blue;");
        lblVBox.getChildren().addAll(spacer, lbl1, lbl2, lbl3, lbl4, spacer2, shipOpHBox);

        HBox midHBox = new HBox(20);
        midHBox.getChildren().addAll(pane, hSpacer, lblVBox, hSpacer4);

        HBox gameOpHBox = new HBox(50);
        gameOpHBox.setStyle("-fx-border: solid; -fx-border-color: red");
        gameOpHBox.getChildren().addAll(autoFillBtn, hSpacer2, startBtn);

        VBox contentVBox = new VBox(20);
        contentVBox.setPadding(new Insets(10, 10, 10, 10));
        contentVBox.getChildren().add(lblHeading);
        contentVBox.getChildren().add(midHBox);
        contentVBox.getChildren().add(gameOpHBox);

        //MENU BAR
        Menu fileMenu = new Menu("File");
        MenuItem save = new MenuItem("save");
        MenuItem load = new MenuItem("load");
        MenuItem exit = new MenuItem("exit");
        fileMenu.getItems().addAll(save, load, exit);

        Menu debugMenu = new Menu("debug");
        MenuItem hboxSize = new MenuItem("HboxSize");
        hboxSize.setOnAction(e -> System.out.println(gameOpHBox.getWidth()));
        debugMenu.getItems().addAll(hboxSize);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().addAll(debugMenu);

        VBox baseVBox = new VBox();
        baseVBox.getChildren().add(menuBar);
        baseVBox.getChildren().add(contentVBox);

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("gameStyle.css").toExternalForm());
//        stage.setMinHeight(600);
//        stage.setMinWidth(960);

        return scene;
    }

    private static void onAddShipBtnClick(ActionEvent event) {
        addShip();
    }

    private static void onRemoveShipBtnClick(ActionEvent event) {
        for(Position position: positions) {
            localGame.getField().removeShip(position);
        }
        positions.clear();
        updateFriendlyGame();
    }

    private static void onAutoFillBtnClick(ActionEvent event) {
        Integer[] curLengths = localGame.getField().getShipCountByClass();

        int remCVL = gameOptions.getCarrier() - curLengths[0];
        int remBB = gameOptions.getBattleship() - curLengths[1];
        int remCL = gameOptions.getCruiser() - curLengths[2];
        int remDD = gameOptions.getDestroyer() - curLengths[3];

        ArrayList<Integer> remaining = new ArrayList<>();
        for (int i = 0; i < remCVL; i++) {
            remaining.add(5);
        }
        for (int i = 0; i < remBB; i++) {
            remaining.add(4);
        }
        for (int i = 0; i < remCL; i++) {
            remaining.add(3);
        }
        for (int i = 0; i < remDD; i++) {
            remaining.add(2);
        }
        localGame.getField().addShipRandomKeepShips(remaining);
        updateFriendlyGame();
    }

    private static void onStartBtnClick(ActionEvent event) {
        localGame.startGame();
        stage.setScene(SceneB.create(stage, localGame));
    }

    private static VBox createFriendlyGame() {

        VBox gameBaseVBox = new VBox(); // VBox ist die Basis für das Spielfeld in der GUI -> im Prinzip die Reihen
        gameBaseVBox.setId("myField");
        for (int i = 0; i < SceneA.localGame.getField().getHeight(); i++) {
            // Für jede Reihe wird dann eben eine HBox für diese Reihe erstellt
            HBox row = new HBox();
            VBox.setVgrow(row, Priority.ALWAYS);
            for (int j = 0; j < SceneA.localGame.getField().getLength(); j++) {
                // In jeder Reihe gibt es noch eine Anzahl Zellen: macht hoffentlich Sinn
                HBox cell = new HBox();
                HBox.setHgrow(cell, Priority.ALWAYS);
                cell.setId(j + ":" + i); // Setze ID für die Zelle, um sie nachher wiedererkennen zu können
                cell.getStyleClass().add("hbox-water"); // Ein Style setzen
                cell.setOnMouseClicked(SceneA::onFriendlyFieldClicked);

                Label lbl = new Label("0"); // Label zum anzeigen von Text, Label können ja im Prinzip auch Bilder usw anzeigen, wenn man möchte.
                lbl.setFont(new Font("Courier", 14)); // Monospace Font, damit alle Zellen gleich breit sind

                cell.getChildren().add(lbl); // Füge das Label in die Cell HBox ein
                row.getChildren().add(cell); // Füge die cellHBox in die rowHBox ein
            }
            gameBaseVBox.getChildren().add(row); // Füge jeder Reihe in die Spiel VBox ein
        }
        return gameBaseVBox;
    }

    private static void onFriendlyFieldClicked(MouseEvent event) {
        HBox cell = ((HBox) event.getSource());
        cell.getStyleClass().remove(0);
        cell.getStyleClass().add("hbox-marked");

        SceneA.positions.add(new Position(cell.getId().split(":")));
        if (positions.size() == 5) {
            SceneA.addShip();
        }
    }

    private static void updateFriendlyGame() {
        for (int i = 0; i < SceneA.localGame.getField().getHeight(); i++) {
            HBox row = ((HBox) SceneA.playerField.getChildren().get(i));
            for (int j = 0; j < SceneA.localGame.getField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                cell.getStyleClass().remove(0);
                Label lbl = ((Label) cell.getChildren().get(0));
                if (SceneA.localGame.getField().getPlayfield()[i][j].getClass() == Cell.class ||
                        SceneA.localGame.getField().getPlayfield()[i][j].getClass() == Block.class) {
                    lbl.setText("0");
                    cell.getStyleClass().add("hbox-water");
                }
                else if (SceneA.localGame.getField().getPlayfield()[i][j].getClass() == Ship.class) {
                    lbl.setText("1");
                    cell.getStyleClass().add("hbox-ship");
                }
                else if (SceneA.localGame.getField().getPlayfield()[i][j].getClass() == Shot.class) {
                    Shot shot = ((Shot) SceneA.localGame.getField().getPlayfield()[i][j]);
                    if (shot.getWasShip()) {
                        lbl.setText("9");
                        cell.getStyleClass().add("hbox-hit");
                    }
                    else {
                        lbl.setText("5");
                        cell.getStyleClass().add("hbox-miss");
                    }
                }
            }
        }
    }

    private static VBox createEnemyGame() {

        VBox gameBaseVBox = new VBox();
        gameBaseVBox.setId("enemyField");
        for (int i = 0; i < SceneA.localGame.getField().getHeight(); i++) {
            HBox row = new HBox();
            for (int j = 0; j < SceneA.localGame.getField().getLength(); j++) {
                HBox cell = new HBox();
                cell.setId(j + ":" + i);
                cell.getStyleClass().add("hbox-water");
                cell.setOnMouseClicked(SceneA::onEnemyFieldClicked);
                // Füge hier einen eventListener hinzu, damit die klicks auf die "Zelle" auch verarbeitet werden können
                // this::Methode ist im Prinzip wie ein lambda. Bei der Methode KEIN () hinzufügen, da sie ja nicht beim
                // erstellen ausgeführt werden soll, sondern erst später.
                //cell.setOnMouseClicked(this::onCellClicked); // add eventListener so we can react to clicks
                Label lbl = new Label("0"); // da der Spieler das gegnerische Feld nicht sehen soll, setze erstmal alle Labels auf 0
                lbl.setFont(new Font("Courier", 14)); // set some font
                cell.getChildren().add(lbl);
                row.getChildren().add(cell);
            }
            gameBaseVBox.getChildren().add(row);
        }
        return gameBaseVBox;
    }

    private static void onEnemyFieldClicked(MouseEvent event) {
        HBox cell = ((HBox) event.getSource());

        SceneA.localGame.shoot(new Position(cell.getId().split(":")));
        SceneA.updateFriendlyGame();
        SceneA.updateEnemyGame();
    }

    private static void updateEnemyGame() {
        for (int i = 0; i < SceneA.localGame.getEnemyField().getHeight(); i++) {
            HBox row = ((HBox) SceneA.enemyField.getChildren().get(i));
            for (int j = 0; j < SceneA.localGame.getEnemyField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                cell.getStyleClass().remove(0);
                Label lbl = ((Label) cell.getChildren().get(0));
                if (SceneA.localGame.getEnemyField().getPlayfield()[i][j].getClass() == Shot.class) {
                    Shot shot = ((Shot) SceneA.localGame.getEnemyField().getPlayfield()[i][j]);
                    if (shot.getWasShip()) {
                        lbl.setText("9");
                        cell.getStyleClass().add("hbox-hit");
                    } else {
                        lbl.setText("5");
                        cell.getStyleClass().add("hbox-miss");
                    }
                } else {
                    lbl.setText("0");
                    cell.getStyleClass().add("hbox-water");
                }
            }
        }
    }

    private static void addShip() {
        boolean xStraight = true;
        boolean yStraight = true;
        int firstX = SceneA.positions.get(0).getX();
        int firstY = SceneA.positions.get(0).getY();
        for (int i = 1; i < SceneA.positions.size(); i++) {
            if (SceneA.positions.get(i).getX() != firstX)
                xStraight = false;
            if (SceneA.positions.get(i).getY() != firstY)
                yStraight = false;
        }

        if (xStraight || yStraight)
            SceneA.localGame.getField().addShip(new Ship(positions));

        SceneA.positions.clear();
        SceneA.updateFriendlyGame();
    }

    private static void loadOptions() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("game.config"));
            gameOptions = ((GameOptions) in.readObject());
        } catch (FileNotFoundException e) {
            gameOptions = new GameOptions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
