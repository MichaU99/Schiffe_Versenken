package JavaFx;

import game.OnlineClientGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Controller_ClientGame {
    public Stage stage;

    @FXML
    private TextField serverTbx;
    @FXML
    private TextField portTbx;

    public void changeToMultGameChooseRole(ActionEvent event) throws IOException {//Wechselt die Szene von NewGame zu PutShips
        Parent root= FXMLLoader.load(getClass().getResource("Layout_Mult_ChooseRole.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeToLoadingScreen(ActionEvent event) throws IOException {//Wechselt die Szene von NewGame zu PutShips

        OnlineClientGame game = validateInput();
        if (game == null) {
            return;
        } else {
            Controller_LoadingScreen.onlineGame = game;
        }

        Parent root = FXMLLoader.load(getClass().getResource("Layout_LoadingScreen.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private OnlineClientGame validateInput() {
        try {
            int pn = Integer.parseInt(portTbx.getText());
            if (pn < 0) {
                throw new IndexOutOfBoundsException();
            }
            return new OnlineClientGame(serverTbx.getText(), pn);
        } catch (NumberFormatException e) {
            onTbxError(portTbx, 3000);
        } catch (IndexOutOfBoundsException e) {
            onTbxError(portTbx, 3000);
        }
        return null;
    }

    private void onTbxError(javafx.scene.control.TextField textField, int duration) {
        textField.setStyle("-fx-text-inner-color: red; -fx-border-color: red; -fx-border-radius: 2 2 2 2; -fx-background-radius: 2 2 2 2;");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                textField.setStyle("-fx-text-inner-color: black; -fx-border-color: transparent;");
            }
        }, duration);
    }
}
