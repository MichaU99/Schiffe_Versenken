package JavaFx;

import enums.KiStrength;
import game.OnlineHostGame;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class Controller_HostGame implements Initializable {
    public Stage stage;

    @FXML
    private ChoiceBox<KiStrength> choiceBox;
    @FXML
    private CheckBox checkBox;
    @FXML
    private TextField fsTbx;
    @FXML
    private TextField portTbx;
    @FXML
    private TextField fiveTbx;
    @FXML
    private TextField fourTbx;
    @FXML
    private TextField threeTbx;
    @FXML
    private TextField twoTbx;

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

        OnlineHostGame game = validateInput();
        if (game == null) {
            return;
        } else {
            Controller_LoadingScreen.onlineGame = game;
        }

        Parent root= FXMLLoader.load(getClass().getResource("Layout_LoadingScreen.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Validiert die Eingaben und gibt falls alles ok ist ein daraus generiertes OnlineHostGame zurück
     */
    private OnlineHostGame validateInput() {
        boolean status = true;
        int fs = 0;
        int pn = 0;
        int five = 0;
        int four = 0;
        int three = 0;
        int two = 0;

        try {
            fs = Integer.parseInt(fsTbx.getText());
            if (fs < 5 || fs > 30) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(fsTbx, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(fsTbx, 3000);
        }

        try {
            pn = Integer.parseInt(portTbx.getText());
            if (pn < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(fsTbx, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(fsTbx, 3000);
        }

        try {
            five = Integer.parseInt(fiveTbx.getText());
            if (five < 0 ) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(fiveTbx, 3000);
        }

        try {
            four = Integer.parseInt(fourTbx.getText());
            if (four < 0 ) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(fourTbx, 3000);
        }

        try {
            three = Integer.parseInt(threeTbx.getText());
            if (three < 0 ) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(threeTbx, 3000);
        }

        try {
            two = Integer.parseInt(twoTbx.getText());
            if (two < 0 ) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(twoTbx, 3000);
        }

        ArrayList<Integer> shipLengthsAr = new ArrayList<>();
        for (int i = 0; i < five; i++) {
            shipLengthsAr.add(5);
        }
        for (int i = 0; i < four; i++) {
            shipLengthsAr.add(4);
        }
        for (int i = 0; i < three; i++) {
            shipLengthsAr.add(3);
        }
        for (int i = 0; i < two; i++) {
            shipLengthsAr.add(2);
        }

        int[] shipLengths = new int[shipLengthsAr.size()];
        for (int i = 0; i < shipLengthsAr.size(); i++) {
            shipLengths[i] = shipLengthsAr.get(i);
        }

        if (!status) {
            return null;
        }
        KiStrength kiStrength=choiceBox.getSelectionModel().getSelectedItem();
        GameOptions go = new GameOptions(fs, kiStrength, five, four, three, two);
        OnlineHostGame onlineHostGame=new OnlineHostGame(fs, fs, pn, shipLengths, go);
        if(checkBox.isSelected()) onlineHostGame.kiPlays=true;
        return onlineHostGame;
    }

    /**
     * Lässt dich bei aktivierung eine Ki, zum für dich spielen, wählen
     */
    public void KIvsKIbtn(ActionEvent event){
        if(checkBox.isSelected()){
            choiceBox.setDisable(false);
        }
        else choiceBox.setDisable(true);
    }

    private void onTbxError(javafx.scene.control.TextField textField, int duration) {
        textField.getStyleClass().add("textfeldWRONG");
        //textField.setStyle("-fx-text-inner-color: red; -fx-border-color: red; -fx-border-radius: 2 2 2 2; -fx-background-radius: 2 2 2 2;");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                //textField.setStyle("-fx-text-inner-color: black; -fx-border-color: transparent;");
                textField.getStyleClass().remove("textfeldWRONG");
                textField.getStyleClass().add("textfeld2");
            }
        }, duration);
    }


}
