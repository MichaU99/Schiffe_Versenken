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

    public static ArrayList<Integer> shipList=null;
    public static KiStrength kiStrength1=null;
    public static KiStrength kiStrength2=null;
    public static Integer fieldsize=null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Können wir später rausnehmen nur für leichteres Debugging
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
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void changeToGameScreenChooseRole(ActionEvent event) throws IOException {
        ArrayList<Integer> ShipList = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(ships5.getText()); i++) {
            ShipList.add(5);
        }
        for (int i = 0; i < Integer.parseInt(ships4.getText()); i++) {
            ShipList.add(4);
        }
        for (int i = 0; i < Integer.parseInt(ships3.getText()); i++) {
            ShipList.add(3);
        }
        for (int i = 0; i < Integer.parseInt(ships2.getText()); i++) {
            ShipList.add(2);
        }

        kiStrength1=firstDifficulty.getValue();
        kiStrength2=secondDifficulty.getValue();

        fieldsize=Integer.parseInt(fsize.getText());

        Controller_PutShips.game=new KiVsKiGame(fieldsize,fieldsize,kiStrength1,kiStrength2);

        Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }


    private KiVsKiGame validateInput() {
        return null;
    }


}
