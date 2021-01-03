package JavaFx;

import enums.KiStrength;
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

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_GameOptions implements Initializable {
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
    private ChoiceBox<KiStrength> gDifficulty;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCurSettings();
    }

    public void btndefault(ActionEvent event) throws IOException {
       loaddefault();
    }
    public void restorebtn(ActionEvent event) throws IOException {
        loadCurSettings();
    }
    private void loadCurSettings() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("game.options"))) {
            GameOptions options = ((GameOptions) inputStream.readObject());
            fsize.setText(String.valueOf(options.getFieldSize()));
            ships5.setText(String.valueOf(options.getCarrier()));
            ships4.setText(String.valueOf(options.getBattleship()));
            ships3.setText(String.valueOf(options.getCruiser()));
            ships2.setText(String.valueOf(options.getDestroyer()));
            gDifficulty.getSelectionModel().select(options.getKiStrength());
        } catch (FileNotFoundException e) {
            loaddefault();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void loaddefault(){
        GameOptions options = new GameOptions();
        fsize.setText(String.valueOf(options.getFieldSize()));
        ships5.setText(String.valueOf(options.getCarrier()));
        ships4.setText(String.valueOf(options.getBattleship()));
        ships3.setText(String.valueOf(options.getCruiser()));
        ships2.setText(String.valueOf(options.getDestroyer()));
        gDifficulty.getSelectionModel().select(KiStrength.INTERMEDIATE);
    }

    public void goBackToPutShips(ActionEvent event) throws IOException {
        int fs = Integer.parseInt(fsize.getText());
        int five = Integer.parseInt(ships5.getText());
        int four = Integer.parseInt(ships4.getText());
        int three = Integer.parseInt(ships3.getText());
        int two = Integer.parseInt(ships2.getText());
        KiStrength kiStrength = gDifficulty.getSelectionModel().getSelectedItem();
        GameOptions options = new GameOptions(fs, kiStrength, five, four, three, two);

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("game.options"))) {
            outputStream.writeObject(options);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(600); // hier ist extra min fenster gesetzt, so richtig ?
        stage.setMinHeight(400);
        stage.setScene(scene);
        stage.show();
    }



}
