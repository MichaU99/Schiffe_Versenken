package JavaFx;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller_StartingScreens implements Initializable {

    public Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {    }

    public void changeToNewGame(ActionEvent event) throws IOException { //Wechselt die Szene von Start zu NewGame
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void backToStart(ActionEvent event) throws IOException{ //Wechselt die Szene von NewGame zu Start
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Start.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void changeToSingleGame(ActionEvent event) throws IOException{//Wechselt die Szene von NewGame zu PutShips
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void changeToMultGameChooseRole(ActionEvent event) throws IOException{//Wechselt die Szene von NewGame zu PutShips
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Mult_ChooseRole.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void changeToHostGame(ActionEvent event) throws IOException{//Wechselt die Szene von NewGame zu PutShips
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Hostgame.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void changeToJoinServer(ActionEvent event) throws IOException{//Wechselt die Szene von NewGame zu PutShips
        Parent  root= FXMLLoader.load(getClass().getResource("NewGame_Muliti_Client.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void changeToStartOptions(ActionEvent event) throws IOException{ // Wechselt in die Szene von Startoptions
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Start_Options.fxml"));
        Scene scene = new Scene(root);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void close(ActionEvent event){
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}
