package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameForm extends Application{

    private TableView<String> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Main Game");

        Menu fileMenu = new Menu("File");
        MenuItem save = new MenuItem("save");
        MenuItem load = new MenuItem("load");
        MenuItem exit = new MenuItem("exit");
        fileMenu.getItems().add(save);
        fileMenu.getItems().add(load);
        fileMenu.getItems().add(exit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);

        final Label label = new Label("Tabellen Test");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstCol = new TableColumn("First Column");
        TableColumn secondCol = new TableColumn("Second Column");
        TableColumn thirdCol = new TableColumn("Third Column");

        table.getColumns().addAll(firstCol, secondCol, thirdCol);

        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(0, 0, 10, 0));
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(label);
        vBox.getChildren().add(table);

        Scene scene = new Scene(vBox, 960, 600);

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
