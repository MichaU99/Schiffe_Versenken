package JavaFx;

import game.LocalGame;
import game.OnlineClientGame;
import game.OnlineHostGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller_StartingScreens{

    public Stage stage;

    /**
     * Wechselt die Szene von Start zu NewGame
     */
    public void changeToNewGame(ActionEvent event) throws IOException {
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Scene scene = new Scene(root,800,600);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Wechselt die Szene von NewGame zu Start
     */
    public void backToStart(ActionEvent event) throws IOException{
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Start.fxml"));
        Scene scene = new Scene(root,800,600);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Wechselt die Szene von NewGame zu PutShips
     */
    public void changeToSingleGame(ActionEvent event) throws IOException{
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Scene scene = new Scene(root,1000,600);
        scene.getStylesheets().add("JavaFx/Shot.css");
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinHeight(600);
        stage.setMinWidth(1000);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Wechselt die Szene von NewGame zu PutShips
     */
    public void changeToHostGame(ActionEvent event) throws IOException{
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Hostgame.fxml"));
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Wechselt die Szene von NewGame zu PutShips
     */
    public void changeToJoinServer(ActionEvent event) throws IOException{
        Parent  root= FXMLLoader.load(getClass().getResource("NewGame_Muliti_Client.fxml"));
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *Wechselt die Szene von NewGame zu PutShips
     */
    public void changeToMultGameChooseRole(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_Mult_ChooseRole.fxml"));
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *wechselt zu Optionen von Ki vs.Ki
     */
    public void changeToKiVKi(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_KiVKi.fxml"));
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();

    }

    /**
     *Wechselt in die Szene von Startoptions
     */
    public void changeToStartOptions(ActionEvent event) throws IOException{
        Parent  root= FXMLLoader.load(getClass().getResource("Layout_Start_Options.fxml"));
        Scene scene = new Scene(root,800,600);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Schließt Fenster bei Buttonclick
     * @param event Bezieht hieraus welches Fenster geschlossen werden soll
     */
    public void close(ActionEvent event){
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Load Game Knopf vom Startup Screen
     * Unterscheided anhand der Dateiendung zwischen den verschiedenen Lademöglichkeiten
     * und startet entweder direkt ins Spiel für Local oder startet in den Ladescreen mit gesetztem
     * save=true Hebel
     * @param event Bezieht hieraus die aktuelle stage in der die Scene
     */
    public void loadGame(ActionEvent event){
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fs = new FileChooser();
        fs.setTitle("Choose a savefile");
        fs.setInitialDirectory(new File("./"));
        File file = fs.showOpenDialog(primaryStage);
        if (file != null) {
            Controller_GameScreen.saveGame=true;
            String filepath = file.getPath();
            String ext = filepath.substring(filepath.lastIndexOf("."));
            if (ext.equals(".lsave")) {
                try {
                    Controller_GameScreen.game = LocalGame.loadGame(filepath);
                    Parent  root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
                    Scene scene = new Scene(root,1000,600);
                    scene.getStylesheets().add("JavaFx/Shot.css");
                    primaryStage.setMinWidth(1000);
                    primaryStage.setMinHeight(600);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (ext.equals(".hsave")) {
                try {
                    // TODO: 06.01.2021 Muss in den Hostgame waiting Screen laden, tut grade noch nicht
                    Controller_LoadingScreen.onlineGame = OnlineHostGame.loadGame(filepath);
                    //Controller_LoadingScreen.filename = file.getName();
                    Controller_LoadingScreen.wasSave=true;
                    Parent  root= FXMLLoader.load(getClass().getResource("Layout_LoadingScreen.fxml"));
                    Scene scene = new Scene(root,800,600);
                    primaryStage.setMinWidth(800);
                    primaryStage.setMinHeight(600);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                    //primaryStage.setScene(WaitingForConnectionForm.create(primaryStage, game, true));
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else if (ext.equals(".csave")) {
                try {
                    Controller_LoadingScreen.onlineGame = OnlineClientGame.loadGame(filepath);
                    //Controller_LoadingScreen.filename = file.getName();
                    //primaryStage.setScene(WaitingForConnectionForm.create(primaryStage, game, true));
                    Controller_LoadingScreen.wasSave=true;
                    Parent  root= FXMLLoader.load(getClass().getResource("Layout_LoadingScreen.fxml"));
                    Scene scene = new Scene(root,800,600);
                    primaryStage.setMinWidth(800);
                    primaryStage.setMinHeight(600);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setContentText("DIES IST KEINE SPEICHERDATEI DES SPIELS");
                alert.showAndWait();
            }
        }
    }

}
