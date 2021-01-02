package gui;

import game.KiVsKiGame;
import game.LocalGame;
import game.OnlineHostGame;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenuForm extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setOnCloseRequest(windowEvent -> System.exit(0));
        primaryStage.setScene(create(primaryStage));
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);

        primaryStage.show();
    }

    public static Scene create(Stage primaryStage) {
        primaryStage.setTitle("Main Menu");
        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        Button ngBtn = new Button("New Game");
        ngBtn.setOnAction(event -> primaryStage.setScene(NewGameForm.create(primaryStage)));

        Button lgBtn = new Button("Load Game");
        lgBtn.setOnAction(event -> {
            FileChooser fs = new FileChooser();
            fs.setTitle("Choose a savefile");
            fs.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("all files", "*.*"),
                    new FileChooser.ExtensionFilter("local games", "*.lsave"),
                    new FileChooser.ExtensionFilter("online games", "*.hsave"),
                    new FileChooser.ExtensionFilter("ki v ki games", "*.ksave")
            );

            //fs.setInitialDirectory(new File("./"));
            File file = fs.showOpenDialog(primaryStage);
            if (file != null) {
                String filepath = file.getPath();
                String ext = filepath.substring(filepath.lastIndexOf("."));
                switch (ext) {
                    case ".lsave":
                        try {
                            LocalGame game = LocalGame.loadGame(filepath);
                            primaryStage.setScene(MainGameForm.create(primaryStage, game));
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ".hsave":
                        try {
                            OnlineHostGame game = OnlineHostGame.loadGame(filepath);
                            WaitingForConnectionForm.filename = file.getName();
                            primaryStage.setScene(WaitingForConnectionForm.create(primaryStage, game, true));
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ".ksave":
                        try {
                            KiVsKiGame game = KiVsKiGame.loadGame(filepath);
                            primaryStage.setScene(MainGameForm.create(primaryStage, game));
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error loading game");
                        alert.setContentText("Filetyp does not match");
                        alert.showAndWait();
                        break;
                }
            }
        });

        Button optionsBtn = new Button("Options");

        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(event -> System.exit(0));


        VBox btnVBox = new VBox(20);
        btnVBox.getChildren().addAll(ngBtn, lgBtn, optionsBtn, exitBtn);

        HBox centerHBox = new HBox(StaticNodes.getHSpacer(), btnVBox, StaticNodes.getHSpacer());

        contentVBox.getChildren().add(centerHBox);

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox, 300, 275);
        scene.getStylesheets().add(MainMenuForm.class.getResource("MainMenu.css").toExternalForm());
        return scene;
    }
}
