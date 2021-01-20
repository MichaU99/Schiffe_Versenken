package JavaFx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GuiMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }
        public static Stage stage;
        @Override
        public void start (Stage stage) throws Exception {
            this.stage=stage;
            stage.setMinHeight(600);
            stage.setMinWidth(800);
            stage.setOnCloseRequest(windowEvent -> System.exit(0));
            Parent root = FXMLLoader.load(getClass().getResource("Layout_Start.fxml"));
            Scene scene = new Scene(root,800,600);
            //scene.getStylesheets().add(this.getClass().getResource("./gameStyle.css").toExternalForm());

            this.stage.setTitle("SchiffeVersenken");
            this.stage.centerOnScreen();
            this.stage.setScene(scene);
            this.stage.show();

    }
}
