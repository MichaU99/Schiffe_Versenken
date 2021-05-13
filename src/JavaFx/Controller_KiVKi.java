package JavaFx;

import enums.KiStrength;
import game.KiVsKiGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;


public class Controller_KiVKi implements Initializable {
    public Stage stage;
    @FXML
    private TextField fsize;
    @FXML
    private TextField ships5;
    @FXML
    private TextField ships4;
    @FXML
    private TextField ships3;
    @FXML
    private TextField ships2;
    @FXML
    private ChoiceBox<KiStrength> firstDifficulty;
    @FXML
    private ChoiceBox<KiStrength> secondDifficulty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fsize.setText("10");
        ships5.setText("1");
        ships4.setText("2");
        ships3.setText("3");
        ships2.setText("4");
        firstDifficulty.setValue(KiStrength.BEGINNER);
        secondDifficulty.setValue(KiStrength.BEGINNER);
    }

    public void changeToNewGameChooseRole(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public void changeToGameScreen(ActionEvent event) throws IOException { // name falsch aber wechselt auf gamescreen

        if (!validateInput()) {
            return;
        }

        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));


        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        scene.getStylesheets().add("JavaFx/Shot.css");
        stage.setMinHeight(600);
        stage.setMinWidth(1000);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.centerOnScreen();
        stage.show();

    }


    private boolean validateInput() {
        int fs = 0;
        int five = 0;
        int four = 0;
        int three = 0;
        int two = 0;
        boolean status = true;

        try {
            fs = Integer.parseInt(fsize.getText());
            if (fs < 5 || fs > 30) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(fsize, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(fsize, 3000);
        }

        try {
            five = Integer.parseInt(ships5.getText());
            if (five < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(ships5, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(ships5, 3000);
        }

        try {
            four = Integer.parseInt(ships4.getText());
            if (four < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(ships4, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(ships4, 3000);
        }

        try {
            three = Integer.parseInt(ships3.getText());
            if (three < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(ships3, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(ships3, 3000);
        }

        try {
            two = Integer.parseInt(ships2.getText());
            if (two < 0) {
                throw new IndexOutOfBoundsException();
            }
        } catch (NumberFormatException e) {
            status = false;
            onTbxError(ships2, 3000);
        } catch (IndexOutOfBoundsException e) {
            status = false;
            onTbxError(ships2, 3000);
        }

        if (!status) {
            return false;
        }


        ArrayList<Integer> shipList = new ArrayList<>();
        for (int i = 0; i < five; i++) {
            shipList.add(5);
        }
        for (int i = 0; i < four; i++) {
            shipList.add(4);
        }
        for (int i = 0; i < three; i++) {
            shipList.add(3);
        }
        for (int i = 0; i < two; i++) {
            shipList.add(2);
        }

        KiVsKiGame game = new KiVsKiGame(fs, fs, firstDifficulty.getSelectionModel().getSelectedItem(),
                secondDifficulty.getSelectionModel().getSelectedItem());
        if (game.startGame(shipList)) {
            Controller_GameScreen.game = game;
            return true;
        } else {
            return false;
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
}
