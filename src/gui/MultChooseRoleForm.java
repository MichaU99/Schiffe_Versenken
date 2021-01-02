package gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MultChooseRoleForm {

    public static Scene create(Stage primaryStage) {
        primaryStage.setTitle("Choose Role");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        Button smBtn = new Button("Server Mode");
        smBtn.setOnAction(event -> primaryStage.setScene(ServerModeForm.create(primaryStage)));

        Button cmBtn = new Button("Client Mode");
        cmBtn.setOnAction(event -> primaryStage.setScene(ClientModeForm.create(primaryStage)));

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> primaryStage.setScene(NewGameForm.create(primaryStage)));


        VBox btnVBox = new VBox(20);
        btnVBox.getChildren().addAll(smBtn, cmBtn, backBtn);

        HBox centerHBox = new HBox(StaticNodes.getHSpacer(), btnVBox, StaticNodes.getHSpacer());

        contentVBox.getChildren().add(centerHBox);

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainMenu.css").toExternalForm());
        return scene;
    }
}
