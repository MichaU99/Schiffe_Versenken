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
        public Stage stage;
        @Override
        public void start (Stage stage) throws Exception {



            this.stage=stage;
            Parent root = FXMLLoader.load(getClass().getResource("Layout_PutShip.fxml"));
            Scene scene = new Scene(root, 600, 400);

            //scene.getStylesheets().add(this.getClass().getResource("/GUIPictures/InsertBoats.css").toExternalForm());

            this.stage.setTitle("SchiffeVersenkenEXTREME");
            this.stage.setScene(scene);
            this.stage.show();

    }
}
