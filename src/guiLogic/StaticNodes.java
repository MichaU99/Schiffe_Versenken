package guiLogic;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.Timer;
import java.util.TimerTask;

public class StaticNodes {
    public static VBox getBaseVboxWithMenubar() {
        Menu fileMenu = new Menu("File");
        MenuItem save = new MenuItem("save");
        MenuItem load = new MenuItem("load");
        MenuItem exit = new MenuItem("exit");
        exit.setOnAction(event -> System.exit(0));
        fileMenu.getItems().addAll(save, load, exit);

        Menu debugMenu = new Menu("debug");
        //debugMenu.getItems().addAll(hboxSize);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().addAll(debugMenu);

        VBox baseVBox = new VBox();
        baseVBox.getChildren().add(menuBar);

        return baseVBox;
    }

    public static VBox getContentVbox() {
        VBox contentVBox = new VBox(10);
        contentVBox.setPadding(new Insets(10, 10, 10, 10));
        return contentVBox;
    }

    public static Region getHSpacer() {
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public static Region getVSpacer() {
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        return spacer;
    }

    public static HBox createLabelForNode(Node node, String lblText) {
        HBox hBox = new HBox(20);
        Label lbl = new Label(lblText);
        lbl.setPrefWidth(150);
        hBox.getChildren().addAll(lbl, node);
        return hBox;
    }

    public static HBox centerNode(Node node) {
        HBox hBox = new HBox();
        hBox.getChildren().addAll(getHSpacer(), node, getHSpacer());
        return hBox;
    }

    public static void onTbxError(TextField textField, int duration) {
        textField.setStyle("-fx-text-inner-color: red; -fx-border-color: red; -fx-border-radius: 2 2 2 2; -fx-background-radius: 2 2 2 2;");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                textField.setStyle("-fx-text-inner-color: black; -fx-border-color: transparent;");
            }
        }, duration);
    }
}
