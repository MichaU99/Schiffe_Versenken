package JavaFx;

import enums.KiStrength;
import game.OnlineClientGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller_ClientGame implements Initializable {
    public Stage stage;
    @FXML
    private ChoiceBox<KiStrength> choiceBox;
    @FXML
    private CheckBox checkBox;
    @FXML
    private TextField serverTbx;
    @FXML
    private TextField portTbx;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.setDisable(true);
        choiceBox.getSelectionModel().select(KiStrength.INTERMEDIATE);
    }

    public void changeToMultGameChooseRole(ActionEvent event) throws IOException {//Wechselt die Szene von NewGame zu PutShips
        Parent root= FXMLLoader.load(getClass().getResource("Layout_Mult_ChooseRole.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
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
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Checkt die Eingegebenen Werte und gibt falls alles ok ist ein daraus generiertes OnlineClientGame zurück
     */
    private OnlineClientGame validateInput() {
        boolean status = true;
        int pn = 0;
        try {
            pn = Integer.parseInt(portTbx.getText());
            if (pn < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(portTbx, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(portTbx, 3000);
        }

        KiStrength kiStrength=choiceBox.getSelectionModel().getSelectedItem();
        String serverName = serverTbx.getText();
        if (serverName.isEmpty()) {
            status = false;
            onTbxError(serverTbx, 3000);
        }

        if (status) {
            if(checkBox.isSelected()){
                OnlineClientGame onlineClientGame=new OnlineClientGame(serverName,pn,kiStrength);
                onlineClientGame.kiPlays=true;
                return onlineClientGame;
            }
            else{
                return new OnlineClientGame(serverName, pn);
            }

        } else {
            return null;
        }
    }

    private void onTbxError(javafx.scene.control.TextField textField, int duration) {
        textField.getStyleClass().add("textfeldWRONG");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                textField.getStyleClass().remove("textfeldWRONG");
                textField.getStyleClass().add("textfeld2");
            }
        }, duration);
    }

    /**
     * Lässt dich falls aktiv eine Ki zum spielen wählen
     */
    public void KIvsKIbtn(ActionEvent actionEvent) {
        if(checkBox.isSelected()){
            choiceBox.setDisable(false);
        }
        else choiceBox.setDisable(true);
    }


}
