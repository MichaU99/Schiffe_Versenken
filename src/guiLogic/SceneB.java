package guiLogic;

import game.Game;
import game.KiVsKiGame;
import game.Position;
import game.cells.Block;
import game.cells.Cell;
import game.cells.Ship;
import game.cells.Shot;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class SceneB {

    private static Game game;
    private static VBox playerField;
    private static VBox enemyField;

    public static Scene create(Stage stage, Game game) {
        SceneB.game = game;

        Region hSpacer1 = new Region();
        HBox.setHgrow(hSpacer1, Priority.ALWAYS);

        Label lblHeading = new Label("Spielfeld test");

        Button shootBtn = new Button("Shoot");
        shootBtn.setOnAction(SceneB::shootBtnClick);

        playerField = createFriendlyGame();
        playerField.setStyle("-fx-border-style: solid; -fx-border-color: red;");
        playerField.setPrefSize(800, 800);
        enemyField = createEnemyGame();
        enemyField.setStyle("-fx-border-style: solid; -fx-border-color: blue;");
        enemyField.setPrefSize(800, 800);

        HBox mainHBox = new HBox(20);
        mainHBox.getChildren().addAll(playerField, hSpacer1, enemyField);

        VBox contentVBox = new VBox(20);
        contentVBox.setPadding(new Insets(10, 10, 10, 10));
        contentVBox.getChildren().add(lblHeading);
        contentVBox.getChildren().add(mainHBox);
        contentVBox.getChildren().add(shootBtn);
//        contentVBox.getChildren().add(gameOpHBox);

        //Menu Bar
        Menu fileMenu = new Menu("File");
        MenuItem save = new MenuItem("save");
        MenuItem load = new MenuItem("load");
        MenuItem exit = new MenuItem("exit");
        fileMenu.getItems().addAll(save, load, exit);

        Menu debugMenu = new Menu("debug");
        MenuItem hboxSize = new MenuItem("HboxSize");
        hboxSize.setOnAction(e -> System.out.println("Test"));
        debugMenu.getItems().addAll(hboxSize);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().addAll(debugMenu);

        VBox baseVBox = new VBox();
        baseVBox.getChildren().add(menuBar);
        baseVBox.getChildren().add(contentVBox);
        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("gameStyle.css").toExternalForm());
        return scene;
    }

    private static void shootBtnClick(ActionEvent event) {
        if (game instanceof KiVsKiGame) {
            game.shoot(null);
        }
        updateEnemyGame();
        updateFriendlyGame();
    }

    private static VBox createFriendlyGame() {

        VBox gameBaseVBox = new VBox(); // VBox ist die Basis für das Spielfeld in der GUI -> im Prinzip die Reihen
        gameBaseVBox.setId("myField");
        for (int i = 0; i < game.getField().getHeight(); i++) {
            // Für jede Reihe wird dann eben eine HBox für diese Reihe erstellt
            HBox row = new HBox();
            VBox.setVgrow(row, Priority.ALWAYS);
            for (int j = 0; j < game.getField().getLength(); j++) {
                // In jeder Reihe gibt es noch eine Anzahl Zellen: macht hoffentlich Sinn
                HBox cell = new HBox();
                HBox.setHgrow(cell, Priority.ALWAYS);
                cell.setId(j + ":" + i); // Setze ID für die Zelle, um sie nachher wiedererkennen zu können
                cell.getStyleClass().add("hbox-water"); // Ein Style setzen
                //cell.setOnMouseClicked(SceneA::onFriendlyFieldClicked);

                Label lbl = new Label("0"); // Label zum anzeigen von Text, Label können ja im Prinzip auch Bilder usw anzeigen, wenn man möchte.
                lbl.setFont(new Font("Courier", 14)); // Monospace Font, damit alle Zellen gleich breit sind

                cell.getChildren().add(lbl); // Füge das Label in die Cell HBox ein
                row.getChildren().add(cell); // Füge die cellHBox in die rowHBox ein
            }
            gameBaseVBox.getChildren().add(row); // Füge jeder Reihe in die Spiel VBox ein
        }
        return gameBaseVBox;
    }

    private static void updateFriendlyGame() {
        for (int i = 0; i < game.getField().getHeight(); i++) {
            HBox row = ((HBox) playerField.getChildren().get(i));
            for (int j = 0; j < game.getField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                cell.getStyleClass().remove(0);
                Label lbl = ((Label) cell.getChildren().get(0));
                if (game.getField().getPlayfield()[i][j].getClass() == Cell.class ||
                        game.getField().getPlayfield()[i][j].getClass() == Block.class) {
                    lbl.setText("0");
                    cell.getStyleClass().add("hbox-water");
                }
                else if (game.getField().getPlayfield()[i][j].getClass() == Ship.class) {
                    lbl.setText("1");
                    cell.getStyleClass().add("hbox-ship");
                }
                else if (game.getField().getPlayfield()[i][j].getClass() == Shot.class) {
                    Shot shot = ((Shot) game.getField().getPlayfield()[i][j]);
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
        for (int i = 0; i < game.getField().getHeight(); i++) {
            HBox row = new HBox();
            VBox.setVgrow(row, Priority.ALWAYS);
            for (int j = 0; j < game.getField().getLength(); j++) {
                HBox cell = new HBox();
                HBox.setHgrow(cell, Priority.ALWAYS);
                cell.setId(j + ":" + i);
                cell.getStyleClass().add("hbox-water");
                cell.setOnMouseClicked(SceneB::onEnemyFieldClicked);
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

        game.shoot(new Position(cell.getId().split(":")));
        updateFriendlyGame();
        updateEnemyGame();
    }

    private static void updateEnemyGame() {
        for (int i = 0; i < game.getEnemyField().getHeight(); i++) {
            HBox row = ((HBox) enemyField.getChildren().get(i));
            for (int j = 0; j < game.getEnemyField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                cell.getStyleClass().remove(0);
                Label lbl = ((Label) cell.getChildren().get(0));
                if (game.getEnemyField().getPlayfield()[i][j].getClass() == Shot.class) {
                    Shot shot = ((Shot) game.getEnemyField().getPlayfield()[i][j]);
                    if (shot.getWasShip()) {
                        lbl.setText("9");
                        cell.getStyleClass().add("hbox-hit");
                    }
                    else {
                        lbl.setText("5");
                        cell.getStyleClass().add("hbox-miss");
                    }
                }
                else {
                    lbl.setText("0");
                    cell.getStyleClass().add("hbox-water");
                }
            }
        }
    }
}
