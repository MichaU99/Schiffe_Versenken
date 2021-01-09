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
        Scene scene = new Scene(root,800,600);

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
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

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
                OnlineClientGame.kiPlays=true;
                return new OnlineClientGame(serverName,pn,kiStrength);
            }
            else{
                OnlineClientGame.kiPlays=false;
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

    public void KIvsKIbtn(ActionEvent actionEvent) {
        if(checkBox.isSelected()){
            choiceBox.setDisable(false);
        }
        else choiceBox.setDisable(true);
    }


}
