package JavaFx;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.IOException;


public class Controller_Start_options implements Initializable {

    @FXML
    private ChoiceBox<String> song;
    @FXML
    private ChoiceBox<String> layout;
    @FXML
    private Slider sound;
    @FXML
    private Button back_btn;
    @FXML
    private Button credits_btn;

    public Stage stage;

    private static void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

        String musicfile;

        switch (t1.intValue()) {
            case 0:
                musicfile = "C:/Users/Niklas/Programmieren/Programmierpraktikum/TwoStepsFromHellVictory.mp3";
                break;
            //case 1:

            // break;
            //case 2:

            // break;
            default:
                musicfile = "";
                break;
        }

        Media media = new Media(new File(musicfile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        //mediaPlayer.setAutoPlay(true);        //funktioniert ohne mediaPlayer.play() nicht
        mediaPlayer.play();

    }


    public void initialize(URL url, ResourceBundle resourceBundle) {

        back_btn.setVisible(true);
        back_btn.setDisable(false);
        credits_btn.setVisible(true);
        credits_btn.setDisable(false);
        song.setVisible(true);
        layout.setVisible(true);
        sound.setVisible(true);

        //song.setValue("Test");
        song.getItems().addAll("Victory", "--", "--");

        String musicfile;

    }


    public void goBackStart(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Layout_Start.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene oldScene = stage.getScene();
        Scene scene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }


    public void chooceMusic(MouseEvent event) {



        ChoiceBox source = ((ChoiceBox) event.getSource());

        song.getSelectionModel().selectedIndexProperty().addListener(Controller_Start_options::changed);

    }
}
