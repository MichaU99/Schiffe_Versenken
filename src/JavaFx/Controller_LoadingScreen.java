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

    @FXML
    private Label statusLbl;

    public void initialize() {
        if (onlineGame instanceof OnlineHostGame) {
            waitForConnection((OnlineHostGame) onlineGame);
        } else if (onlineGame instanceof OnlineClientGame) {
            connect((OnlineClientGame) onlineGame);
        }
    }

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
            Platform.runLater(() -> {
                try {
                    Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) statusLbl.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }).start();
    }

    private void waitForConnection(OnlineHostGame hostGame) {
        updateStatusLabel("Waiting for Connection!");
        new Thread(() -> {
            boolean status = hostGame.waitForConnection();

            if (status) {
                Controller_PutShips.online = true;
                Controller_GameScreen.game = hostGame;
                Platform.runLater(() -> {
                    try {
                        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = (Stage) statusLbl.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();
    }

    public void onBackBtnClick(ActionEvent event) throws IOException {
        Parent root = null;
        if (onlineGame instanceof OnlineHostGame) {
            onlineGame.freeSocket();
            root = FXMLLoader.load(getClass().getResource("Layout_HostGame.fxml"));
        } else if (onlineGame instanceof OnlineClientGame) {
            root = FXMLLoader.load(getClass().getResource("NewGame_Muliti_Client.fxml"));
        }

        assert root != null: "Unexpected game type!";
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void updateStatusLabel(String text) {
        this.statusLbl.setText(text);
    }
}
