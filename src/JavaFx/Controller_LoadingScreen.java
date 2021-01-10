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
        if(wasSave) {
            if (onlineGame.youInitatedSave && onlineGame instanceof OnlineClientGame) {
                // TODO: 10.01.2021 Method to turn ClientGame into HostGame
            }
            else if(!onlineGame.youInitatedSave && onlineGame instanceof OnlineHostGame){
                // TODO: 10.01.2021 Method to turn HostGame into ClientGame
            }
        }
        if (onlineGame instanceof OnlineHostGame) {
            waitForConnection((OnlineHostGame) onlineGame);
        }
        else if (onlineGame instanceof OnlineClientGame) {
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
            if(wasSave){
                System.out.println("Versucht Verbindung mit Savedatei herzustellen");
                Platform.runLater(() -> {
                    try {
                        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
                        Scene scene = new Scene(root,800,600);
                        this.stage=GuiMain.stage;
                        // Stage stage = (Stage) statusLbl.getScene().getWindow();
                        stage.setScene(scene);
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
                    Scene scene = new Scene(root,800,600);
                    //Stage stage = (Stage) statusLbl.getScene().getWindow();
                    this.stage=GuiMain.stage;
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
            if(wasSave){
                System.out.println("Läd Spiel");
                boolean status = hostGame.waitForConnectionLoadSave(String.valueOf(onlineGame.ID));
                if(status){
                    Platform.runLater(() ->{
                        try {
                        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
                        Scene scene = new Scene(root,800,600);
                        Stage stage = (Stage) statusLbl.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                    ;}
            }
            else {
                boolean status = hostGame.waitForConnection();

            if (status) {
                Controller_PutShips.online = true;
                Controller_GameScreen.game = hostGame;
                Platform.runLater(() -> {
                    try {
                        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
                        Scene scene = new Scene(root,800,600);
                        Stage stage = (Stage) statusLbl.getScene().getWindow();
                        stage.setScene(scene);
                        stage.show();
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
            root = FXMLLoader.load(getClass().getResource("NewGame_Muliti_Client.fxml"));
        }

        assert root != null: "Unexpected game type!";
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void updateStatusLabel(String text) {
        this.statusLbl.setText(text);
    }
}
