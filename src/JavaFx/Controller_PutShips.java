package JavaFx;

import enums.KiStrength;
import game.LocalGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_PutShips implements Initializable {
    @FXML
    private GridPane GridP;
    private LocalGame localGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.localGame = new LocalGame(10, 10, KiStrength.INTERMEDIATE);

        for(int x=0;x<10;x++){
            for(int y=0;y<10;y++){
                HBox l = new HBox();
                l.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                Label h = new Label("1");
                l.getChildren().add(h);
                GridPane.setConstraints(l,x,y);
                GridP.getChildren().add(l);
            }
        }
//        VBox game = this.createGame();
//        GridPane.setConstraints(game, 0, 0);
//        GridP.getChildren().add(game);

        Object[] array=GridP.getChildren().toArray();
        //GridPane.getColumnIndex
        //GridPane.getRowIndex
    }

    private VBox createGame(){
        VBox gameBaseVBox = new VBox(); // VBox ist die Basis für das Spielfeld in der GUI -> im Prinzip die Reihen
        gameBaseVBox.setId("myField");
        for (int i = 0; i < localGame.getField().getHeight(); i++) {
            // Für jede Reihe wird dann eben eine HBox für diese Reihe erstellt
            HBox row = new HBox();
            for (int j = 0; j < localGame.getField().getLength(); j++) {
                // In jeder Reihe gibt es noch eine Anzahl Zellen: macht hoffentlich Sinn
                HBox cell = new HBox();
                cell.setId(j + ":" + i); // Setze ID für die Zelle, um sie nachher wiedererkennen zu können
                cell.getStyleClass().add("hbox-water"); // Ein Style setzen
                //cell.setOnMouseClicked(SceneA::onFriendlyFieldClicked);

                Label lbl = new Label("0"); // Label zum anzeigen von Text, Label können ja im Prinzip auch Bilder usw anzeigen, wenn man möchte.
                lbl.setFont(new Font("Courier", 14)); // Monospace Font, damit alle Zellen gleich breit sind

                cell.getChildren().add(lbl); // Füge das Label in die Cell HBox ein
                row.getChildren().add(cell); // Füge die cellHBox in die rowHBox ein
            }
            gameBaseVBox.getChildren().add(row); // Füge jeder Reihe in die Spiel VBox ein
        }
        return gameBaseVBox;
    }

    public void backToStart(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void goToOptions(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips_Options.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void goBackToPutShips(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
