package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainMenuForm extends Application{

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Main Menu");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Battle Ships");
        scenetitle.setId("welcome-text");
        grid.add(scenetitle, 0, 0);

        Button localBtn = new Button("Local Game");
        localBtn.setAlignment(Pos.CENTER);
        localBtn.setMinWidth(200);
        localBtn.setOnAction(this::localBtnPressed);
        grid.add(localBtn, 0, 1);

        Button onlineBtn = new Button("Online Game");
        onlineBtn.setAlignment(Pos.CENTER);
        onlineBtn.setMinWidth(200);
        onlineBtn.setOnAction(this::onlineBtnPressed);
        grid.add(onlineBtn, 0, 2);

        Button optionsBtn = new Button("Options");
        optionsBtn.setAlignment(Pos.CENTER);
        optionsBtn.setMinWidth(200);
        optionsBtn.setOnAction(this::optionsBtnPressed);
        grid.add(optionsBtn, 0, 3);

        Button exitBtn = new Button("Exit");
        exitBtn.setAlignment(Pos.CENTER);
        exitBtn.setMinWidth(200);
        exitBtn.setOnAction(e -> System.exit(0));
        grid.add(exitBtn, 0, 4);

        //grid.setGridLinesVisible(true);

        Scene scene = new Scene(grid, 300, 275);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(275);
        primaryStage.setMinHeight(300);
        scene.getStylesheets().add(MainMenuForm.class.getResource("MainMenu.css").toExternalForm());

        primaryStage.show();
    }

    private void localBtnPressed(ActionEvent e){

    }

    private void onlineBtnPressed(ActionEvent e) {

    }

    private void optionsBtnPressed(ActionEvent e) {

    }
}
