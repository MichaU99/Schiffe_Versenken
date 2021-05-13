package JavaFx;

import game.OnlineClientGame;
import game.OnlineGame;
import game.OnlineHostGame;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller_LoadingScreen {
    public Stage stage;
    public static OnlineGame onlineGame;
    public static boolean wasSave=false;
    @FXML
    private Label statusLbl;

    public void initialize() {
        if (onlineGame instanceof OnlineHostGame) {
            waitForConnection((OnlineHostGame) onlineGame);
        }
        else if (onlineGame instanceof OnlineClientGame) {
            connect((OnlineClientGame) onlineGame);
        }
    }

    /**
     * Sucht als ClientGame nach passenden Netzwerkverbindungen und wechselt bei Erfolg in die PutShips + setzt das passende Game in den GameScreen
     */
    private void connect(OnlineClientGame clientGame) {
        final int[] counter = {0};
        updateStatusLabel("Trying to connect!");
        new Thread(() -> {
            while (!clientGame.establishConnection()) {
                counter[0]++;
                Platform.runLater(() -> updateStatusLabel("Fail, retry: " + counter[0]));
            }
            Platform.runLater(() -> updateStatusLabel("Success!"));

            Controller_PutShips.online = true;
            Controller_GameScreen.game = clientGame;
            if(wasSave){
                Controller_GameScreen.saveGame=true;
                System.out.println("Versucht Verbindung mit Savedatei herzustellen");
                Platform.runLater(() -> {
                    try {

                        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
                        this.stage=GuiMain.stage;
                        // Stage stage = (Stage) statusLbl.getScene().getWindow();
                        Scene oldScene = stage.getScene();
                        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
                        scene.getStylesheets().add("JavaFx/Shot.css");
                        stage.setMinHeight(600);
                        stage.setMinWidth(1000);
                        stage.setScene(scene);
                        stage.setTitle("ClientGame");
                        stage.centerOnScreen();
                        stage.setMaximized(true);
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            else Platform.runLater(() -> {
                System.out.println("Versucht Verbindung herzustellen");
                try {
                    Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
                    //Stage stage = (Stage) statusLbl.getScene().getWindow();
                    this.stage=GuiMain.stage;
                    Scene oldScene = stage.getScene();
                    if(oldScene.getWidth()>1000&&oldScene.getHeight()>600){
                        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
                        scene.getStylesheets().add("JavaFx/Shot.css");
                        stage.setMinHeight(600);
                        stage.setMinWidth(1000);
                        stage.setScene(scene);
                        stage.setTitle("ClientGame");
                        stage.centerOnScreen();
                        stage.show();
                    }else{
                        Scene scene = new Scene(root,1000,600);
                        scene.getStylesheets().add("JavaFx/Shot.css");
                        stage.setMinHeight(600);
                        stage.setMinWidth(1000);
                        stage.setScene(scene);
                        stage.setTitle("ClientGame");
                        stage.centerOnScreen();
                        stage.show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    /**
     *Wartet als HostGame auf eingehende Verbindungsanfragen und wechselt bei erfolgreichem Connect in die PutShips+ setzt ein OnlineHostGame nach GameScreen
     */
    private void waitForConnection(OnlineHostGame hostGame) {
        updateStatusLabel("Waiting for Connection!");
        new Thread(() -> {
            if(wasSave){
                System.out.println("Läd Spiel");
                boolean status = hostGame.waitForConnectionLoadSave(String.valueOf(onlineGame.ID));
                if(status){
                    Platform.runLater(() ->{
                        try {
                        Controller_GameScreen.game=onlineGame;
                        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
                        Stage stage = (Stage) statusLbl.getScene().getWindow();
                        Scene oldScene = stage.getScene();
                        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
                        scene.getStylesheets().add("JavaFx/Shot.css");
                        stage.setMinHeight(600);
                        stage.setMinWidth(1000);
                        stage.setScene(scene);
                        stage.setMaximized(true);
                        stage.setTitle("HostGame");
                        stage.centerOnScreen();
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                    ;}
            }
            else {
                boolean status = hostGame.waitForConnection();
                System.out.println("Has waited");

            if (status) {
                Controller_PutShips.online = true;
                Controller_GameScreen.game = hostGame;
                Platform.runLater(() -> {
                    try {
                        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
                        Stage stage = (Stage) statusLbl.getScene().getWindow();
                        Scene oldScene = stage.getScene();
                        if(oldScene.getWidth()>1000&&oldScene.getHeight()>600){
                            Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
                            scene.getStylesheets().add("JavaFx/Shot.css");
                            stage.setMinHeight(600);
                            stage.setMinWidth(1000);
                            stage.setScene(scene);
                            stage.setTitle("HostGame");
                            stage.centerOnScreen();
                            stage.show();
                        }else{
                            Scene scene = new Scene(root,1000,600);
                            scene.getStylesheets().add("JavaFx/Shot.css");
                            stage.setMinHeight(600);
                            stage.setMinWidth(1000);
                            stage.setScene(scene);
                            stage.setTitle("HostGame");
                            stage.centerOnScreen();
                            stage.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            }
        }).start();
    }

    public void onBackBtnClick(ActionEvent event) throws IOException {
        Parent root = null;
        if (onlineGame instanceof OnlineHostGame) {
            onlineGame.freeSocket();
            root = FXMLLoader.load(getClass().getResource("Layout_HostGame.fxml"));
        } else if (onlineGame instanceof OnlineClientGame) {
            onlineGame.freeSocket();
            root = FXMLLoader.load(getClass().getResource("NewGame_Muliti_Client.fxml"));

        }

        assert root != null: "Unexpected game type!";
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.setTitle("SchiffeVersenken");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void updateStatusLabel(String text) {
        this.statusLbl.setText(text);
    }
}
