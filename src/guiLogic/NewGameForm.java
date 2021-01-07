package guiLogic;

import JavaFx.MainMenuForm;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NewGameForm {

    public static Scene create(Stage primaryStage) {
        primaryStage.setTitle("New Game");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        Button spBtn = new Button("Single player");
        spBtn.setOnAction(event -> {
//            LocalGame game = new LocalGame(10, 10, KiStrength.INTERMEDIATE);
            primaryStage.setScene(PlaceShipsForm.create(primaryStage, false, null));
        });

        Button mpBtn = new Button("Multiplayer");
        mpBtn.setOnAction(event -> primaryStage.setScene(MultChooseRoleForm.create(primaryStage)));

        Button kiVKiBtn = new Button("Ki vs Ki");
        kiVKiBtn.setOnAction(event -> primaryStage.setScene(KiVsKiForm.create(primaryStage)));

        Button backBtn = new Button("Back");
        backBtn.setOnAction(event -> primaryStage.setScene(MainMenuForm.create(primaryStage)));


        VBox btnVBox = new VBox(20);
        btnVBox.getChildren().addAll(spBtn, mpBtn, kiVKiBtn, backBtn);

        HBox centerHBox = new HBox(StaticNodes.getHSpacer(), btnVBox, StaticNodes.getHSpacer());

        contentVBox.getChildren().add(centerHBox);

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainMenu.css").toExternalForm());
        return scene;
    }
}
