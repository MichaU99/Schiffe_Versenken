package JavaFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_PutShips implements Initializable {
    @FXML
    private GridPane GridP;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(int x=0;x<10;x++){
            for(int y=0;y<10;y++){
                HBox l= new HBox();
                l.getStyle();
                GridPane.setConstraints(l,x,y);
                GridP.getChildren().add(l);
            }
        }
        Object[] array=GridP.getChildren().toArray();
        //GridPane.getColumnIndex
        //GridPane.getRowIndex
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
