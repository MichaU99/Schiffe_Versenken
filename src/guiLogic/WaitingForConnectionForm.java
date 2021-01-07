package guiLogic;

import JavaFx.*;
import game.OnlineClientGame;
import game.OnlineHostGame;
import game.OnlineGame;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;

public class WaitingForConnectionForm {

    private static Timer timer;
    private static Timer timer2;
    private static VBox labelVBox;
    public static boolean wasSave;
    public static String filename;

    public static Scene create(Stage primaryStage, OnlineGame game, boolean wasSave) {

        primaryStage.setTitle("Waiting for connection");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        WaitingForConnectionForm.wasSave = wasSave;

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        Label label = new Label();
        label.getStyleClass().add("-fx-font-size: 16px; -fx-font-weight: bold;");
        ProgressIndicator progressIndicator = new ProgressIndicator();

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> {
            timer.cancel(); timer.purge();

            if (game instanceof OnlineClientGame) {
                primaryStage.setScene(ClientModeForm.create(primaryStage));
            }
            else if (game instanceof OnlineHostGame) {
                game.freeSocket();
                timer2.cancel(); timer2.purge();
                primaryStage.setScene(ServerModeForm.create(primaryStage));
            }
        });

        labelVBox = new VBox(10);
        labelVBox.getChildren().addAll(StaticNodes.centerNode(progressIndicator), StaticNodes.centerNode(label),
                StaticNodes.centerNode(backBtn));

        contentVBox.getChildren().addAll(labelVBox);

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainMenu.css").toExternalForm());

        if (game instanceof OnlineClientGame) {
            connect(((OnlineClientGame) game), primaryStage);
        }
        else if (game instanceof OnlineHostGame) {
            startCounter();
            waitForConnection(((OnlineHostGame) game), primaryStage);
        }

        return scene;
    }

    private static void updateLabel(String text) {
        HBox container = ((HBox) labelVBox.getChildren().get(1));
        Label lbl = ((Label) container.getChildren().get(1));
        lbl.setText(text);
    }

    private static void connect(OnlineClientGame game, Stage primaryStage) {
        final int[] counter = {0};
        updateLabel("Trying to connect");
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                while (!game.establishConnection()) {
                    counter[0]++;
                    Platform.runLater(() -> updateLabel("Fail, retry: " + counter[0]));
                }
                Platform.runLater(() -> updateLabel("Success!"));
                if (wasSave) {
                    Platform.runLater(() -> primaryStage.setScene(MainGameForm.create(primaryStage, game)));
                }
                else {
                    Platform.runLater(() -> primaryStage.setScene(PlaceShipsForm.create(primaryStage, true, game)));
                }
            }
        }, 500);
    }

    private static void waitForConnection(OnlineHostGame game, Stage primaryStage) {
        new Thread(() -> {
            if (wasSave) {
                boolean status = game.waitForConnectionLoadSave(filename);
                if (status)
                    Platform.runLater(() -> primaryStage.setScene(MainGameForm.create(primaryStage, game)));
            }
            else {
                boolean status = game.waitForConnection();
                if (status)
                    Platform.runLater(() -> primaryStage.setScene(PlaceShipsForm.create(primaryStage, true, game)));
            }
        }).start();
    }

    private static void startCounter() {
        final int[] counter = {0};
        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                counter[0]++;
                Platform.runLater(() -> updateLabel("Waiting for connection: " + counter[0]));
            }
        }, 0, 1000);
    }
}
