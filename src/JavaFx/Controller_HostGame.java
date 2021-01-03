package JavaFx;

import enums.KiStrength;
import game.OnlineHostGame;
import gui.GameOptions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Controller_HostGame {
    public Stage stage;

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

    public void changeToMultGameChooseRole(ActionEvent event) throws IOException {//Wechselt die Szene von NewGame zu PutShips
        Parent root= FXMLLoader.load(getClass().getResource("Layout_Mult_ChooseRole.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private OnlineHostGame validateInput() {
        int status = 0;
        try {
            int fs = Integer.parseInt(this.fsTbx.getText());
            if (fs > 30 || fs < 5) {
                throw new IndexOutOfBoundsException();
            }

            status = 1;
            int pn = Integer.parseInt(portTbx.getText());
            if (pn < 0) {
                throw new IndexOutOfBoundsException();
            }

            status = 2;
            int five = Integer.parseInt(fiveTbx.getText());
            if (five < 0) {
                throw new IndexOutOfBoundsException();
            }

            status = 3;
            int four = Integer.parseInt(fourTbx.getText());
            if (four < 0) {
                throw new IndexOutOfBoundsException();
            }

            status = 4;
            int three = Integer.parseInt(threeTbx.getText());
            if (three < 0) {
                throw new IndexOutOfBoundsException();
            }

            status = 5;
            int two = Integer.parseInt(twoTbx.getText());
            if (two < 0) {
                throw new IndexOutOfBoundsException();
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

            gui.GameOptions go = new GameOptions(fs, KiStrength.INTERMEDIATE, five, four, three, two);
            OnlineHostGame hostGame = new OnlineHostGame(fs, fs, pn, shipLengths, go);
            return hostGame;
        } catch (NumberFormatException e) {
            switch (status) {
                case 0:
                    onTbxError(fsTbx, 3000);
                    break;
                case 1:
                    onTbxError(portTbx, 3000);
                    break;
                case 2:
                    onTbxError(fiveTbx, 3000);
                    break;
                case 3:
                    onTbxError(fourTbx, 3000);
                    break;
                case 4:
                    onTbxError(threeTbx, 3000);
                    break;
                case 5:
                    onTbxError(twoTbx, 3000);
                    break;
                default:
                    break;
            }
        } catch (IndexOutOfBoundsException e) {
            switch (status) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }
        }
        return null;
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
