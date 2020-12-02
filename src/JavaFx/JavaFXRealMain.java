package JavaFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class JavaFXRealMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

        @Override
        public void start (Stage stage) throws Exception {
            Parent root = FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));

            Scene scene = new Scene(root, 600, 400);

            stage.setTitle("SchiffeVersenkenEXTREME");
            stage.setScene(scene);
            stage.show();

    }
}
