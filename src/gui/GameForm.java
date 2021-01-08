package gui;

import enums.KiStrength;
import game.LocalGame;
import game.Position;
import game.cells.Ship;
import game.cells.Shot;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameForm extends Application{

    private LocalGame localGame;
    private HBox gameBaseHBox;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // CREATE GAME LOCALGAME AS TEST
        // TODO methoden usw. anpassen, dass sie mit allen typen von game funktionieren
        this.localGame = new LocalGame(10, 10, KiStrength.INTERMEDIATE);
        localGame.getField().addShipRandom(new int[]{5, 5, 4, 3}); // Ein paar random schiffe hinzufügen
        localGame.startGame(); // starten

        primaryStage.setTitle("Main Game");

        //MENU BAR
        Menu fileMenu = new Menu("File");
        MenuItem save = new MenuItem("save");
        MenuItem load = new MenuItem("load");
        MenuItem exit = new MenuItem("exit");
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(load);
        fileMenu.getItems().add(exit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        final Label label = new Label("Spielfeld Test");
        label.setFont(new Font("Arial", 20));

        // baseVBox ist das Grundlegende Layout von meinem Main Window
        VBox baseVBox = new VBox();
        baseVBox.setSpacing(5);
        baseVBox.setPadding(new Insets(0, 0, 10, 0));
        baseVBox.getChildren().add(menuBar);
        baseVBox.getChildren().add(label);

        // die HBox für meine Spielfelder. Spacing damit dazwischen Platz ist, ggf. muss man da auch einzelne HBoxen machen
        this.gameBaseHBox = new HBox();
        gameBaseHBox.setSpacing(20);
        gameBaseHBox.getChildren().add(this.createEnemyGameVBox());
        gameBaseHBox.getChildren().add(this.createFriendlyGameVBox());

        baseVBox.getChildren().add(gameBaseHBox); // HBox in baseVBox einfügen


        Scene scene = new Scene(baseVBox, 960, 600);

        primaryStage.setScene(scene);
        //scene.getStylesheets().add(MainMenuForm.class.getResource("gameStyle.css").toExternalForm());
        primaryStage.show();
    }

    private void onCellClicked(MouseEvent e){
        // Zuerst checken, ob es noch Schiffe auf dem Feld gibt -> muss vielleicht auch für beide Felder durchgeführt werden.
        if (this.localGame.getField().getShipCount() == 0) {
            System.out.println("Keine Schiffe mehr übrig");
            return;
        }

        // Da das Event nur von einer HBox ausgelöst werden kann, kann es direkt zur HBox gecastet werden
        HBox b = (HBox)e.getSource();

        // Die IDs der HBoxen ist die Position im Feld, wie in einem 2d array x:y, deswegen wird der ID String mit : gesplitted.
        // Danach kann man mit der shoot Methode des jeweiligen Felds ganz einfach geschossen werden.
        // Da die shoot Methoden beide Felder updaten, müssen danach auch beide aktualisiert werden.
        String[] ids = b.getId().split(":");

        // System.out.println("IDS: " + Integer.parseInt(ids[0]) + " : " + Integer.parseInt(ids[1]));
        this.localGame.shoot(new Position(Integer.parseInt(ids[0]), Integer.parseInt(ids[1])));
        this.updateEnemyGameVbox();
        this.updateFriendlyGameVBox();
    }

    private VBox createFriendlyGameVBox(){
        VBox gameBaseVBox = new VBox(); // VBox ist die Basis für das Spielfeld in der GUI -> im Prinzip die Reihen
        gameBaseVBox.setId("myField");
        for (int i = 0; i < this.localGame.getField().getHeight(); i++) {
            // Für jede Reihe wird dann eben eine HBox für diese Reihe erstellt
            HBox row = new HBox();
            for (int j = 0; j < this.localGame.getField().getLength(); j++) {
                // In jeder Reihe gibt es noch eine Anzahl Zellen: macht hoffentlich Sinn
                HBox cell = new HBox();
                cell.setId(j + ":" + i); // Setze ID für die Zelle, um sie nachher wiedererkennen zu können
                cell.getStyleClass().add("my-hbox"); // Ein Style setzen

                Label lbl = new Label(); // Label zum anzeigen von Text, Label können ja im Prinzip auch Bilder usw anzeigen, wenn man möchte.

                // Da es das Spielfeld des Spielers ist, werden seine Schiffe hier mit einer 1 dargestellt, alles andere als 0
                // Mit this.localGame.getField().getPlayfield()[i][j] kann die Korrespondierende Zelle aus dem spielfeld ausgelesen werden
                if (this.localGame.getField().getPlayfield()[i][j].getClass() == Ship.class) {
                    lbl.setText("1");
                }
                else {
                    lbl.setText("0");
                }

                lbl.setFont(new Font("Courier", 14)); // Monospace Font, damit alle Zellen gleich breit sind
                cell.getChildren().add(lbl); // Füge das Label in die Cell HBox ein
                row.getChildren().add(cell); // Füge die cellHBox in die rowHBox ein
            }
            gameBaseVBox.getChildren().add(row); // Füge jeder Reihe in die Spiel VBox ein
        }
        return gameBaseVBox;
    }

    private void updateFriendlyGameVBox() {
        // Hier wird das ganze beim erstellen im prinzip rückwärts durchgeführt
        // Aus dem gameBaseHBox werden die Spielfelder extrahiert
        VBox gameBaseVBox = ((VBox) this.gameBaseHBox.getChildren().get(1));
        for (int i = 0; i < this.localGame.getField().getHeight(); i++) {
            HBox row = ((HBox) gameBaseVBox.getChildren().get(i));
            for (int j = 0; j < this.localGame.getField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j)); // Das ist nacheinander jede cellHBox
                Label lbl = ((Label) cell.getChildren().get(0)); // Das ist das Label aus jeder cellHBox

                // Hier wird wieder die Zelle aus dem spielfeld im Backend ausgelesen. Wenn die getroffene Zelle ein
                // Schiff war, ist sie dann 9, wenn der Shot ein miss war, ist sie 5
                if (this.localGame.getField().getPlayfield()[i][j].getClass() == Shot.class) {
                    Shot shot = ((Shot) this.localGame.getField().getPlayfield()[i][j]);
                    if (shot.getWasShip())
                        lbl.setText("9");
                    else
                        lbl.setText("5");
                }
            }
        }
    }

    private VBox createEnemyGameVBox(){
        // Ist im Prinzip genau das gleiche wie bei der createFriendlyGameVBox Methode
        VBox gameBaseVBox = new VBox();
        gameBaseVBox.setId("enemyField");
        for (int i = 0; i < this.localGame.getField().getHeight(); i++) {
            HBox row = new HBox();
            for (int j = 0; j < this.localGame.getField().getLength(); j++) {
                HBox cell = new HBox();
                cell.setId(j + ":" + i);
                cell.getStyleClass().add("my-hbox");

                // Füge hier einen eventListener hinzu, damit die klicks auf die "Zelle" auch verarbeitet werden können
                // this::Methode ist im Prinzip wie ein lambda. Bei der Methode KEIN () hinzufügen, da sie ja nicht beim
                // erstellen ausgeführt werden soll, sondern erst später.
                cell.setOnMouseClicked(this::onCellClicked); // add eventListener so we can react to clicks
                Label lbl = new Label("0"); // da der Spieler das gegnerische Feld nicht sehen soll, setze erstmal alle Labels auf 0
                lbl.setFont(new Font("Courier", 14)); // set some font
                cell.getChildren().add(lbl);
                row.getChildren().add(cell);
            }
            gameBaseVBox.getChildren().add(row);
        }
        return gameBaseVBox;
    }

    private void updateEnemyGameVbox() {
        // im Prinzip das gleiche wie updateFriendlyGameVBox, nur eben für das gegnerische Feld
        VBox gameBaseVBox = ((VBox) this.gameBaseHBox.getChildren().get(0));
        for (int i = 0; i < this.localGame.getField().getHeight(); i++) {
            HBox row = ((HBox) gameBaseVBox.getChildren().get(i));
            for (int j = 0; j < this.localGame.getField().getLength(); j++) {
                HBox cell = ((HBox) row.getChildren().get(j));
                Label lbl = ((Label) cell.getChildren().get(0));
                if (this.localGame.getEnemyField().getPlayfield()[i][j].getClass() == Shot.class) {
                    Shot shot = ((Shot) this.localGame.getEnemyField().getPlayfield()[i][j]);
                    if (shot.getWasShip())
                        lbl.setText("9");
                    else
                        lbl.setText("5");
                }
            }
        }
    }
}
