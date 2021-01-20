package JavaFx;

import enums.KiStrength;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        int fs=0;
        int five=0;
        int four=0;
        int three=0;
        int two=0;
        try {
            fs = Integer.parseInt(fsize.getText());
        }catch (NumberFormatException e){
            fsize.setText("0");
        }

        try {
            five = Integer.parseInt(ships5.getText());
        }catch (NumberFormatException e){
            ships5.setText("0");
        }

        try {
            four = Integer.parseInt(ships4.getText());
        }catch (NumberFormatException e){
            ships4.setText("0");
        }

        try {
            three = Integer.parseInt(ships3.getText());
        }catch (NumberFormatException e){
            ships3.setText("0");
        }

        try {
            two = Integer.parseInt(ships2.getText());
        }catch (NumberFormatException e){
            ships2.setText("0");
        }
        if(five==0 && four==0 && three==0 && two==0){
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("KEINE SCHIFFE GESETZT\n Bitte setzen Sie ein Schiff");
            alert.showAndWait();
            return;
        }
        KiStrength kiStrength = gDifficulty.getSelectionModel().getSelectedItem();
        GameOptions options = new GameOptions(fs, kiStrength, five, four, three, two);

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("game.options"))) {
            outputStream.writeObject(options);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root,oldScene.getWidth(),oldScene.getHeight());
        scene.getStylesheets().add("JavaFx/Shot.css");
        stage.setMinWidth(1000);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }



}
