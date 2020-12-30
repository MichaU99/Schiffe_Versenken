package gui;

import game.OnlineClientGame;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientModeForm {

    public static Scene create(Stage primaryStage) {
        primaryStage.setTitle("Client Mode Config");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        TextField snTf = new TextField("127.0.0.1");
        TextField pnTf = new TextField("55555");

        Button startBtn = new Button("Start Game");
        startBtn.setStyle("-fx-pref-width: 100px; -fx-pref-height: 20px;");
        startBtn.setOnAction(event -> {
            OnlineClientGame game = checkInput(snTf, pnTf);
            if (!(game == null)) {
                primaryStage.setScene(WaitingForConnectionForm.create(primaryStage, game, false));
            }
        });

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-pref-width: 100px; -fx-pref-height: 20px;");
        backBtn.setOnAction(event -> primaryStage.setScene(MultChooseRoleForm.create(primaryStage)));

        HBox btnHBox = new HBox(20);
        btnHBox.getChildren().addAll(backBtn, StaticNodes.getHSpacer(), startBtn);

        VBox btnVBox = new VBox(20);
        btnVBox.getChildren().addAll(StaticNodes.createLabelForNode(snTf, "Servername"),
                StaticNodes.createLabelForNode(pnTf, "Portnumber"),
                btnHBox);

        HBox centerHBox = new HBox(StaticNodes.getHSpacer(), btnVBox, StaticNodes.getHSpacer());

        contentVBox.getChildren().add(centerHBox);

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainMenu.css").toExternalForm());
        return scene;
    }

    private static OnlineClientGame checkInput(TextField sn, TextField pn) {
        try {
            int pn_ = Integer.parseInt(pn.getText());
            return new OnlineClientGame(sn.getText(), pn_);
        } catch (NumberFormatException e) {
            StaticNodes.onTbxError(pn, 3000);
            return null;
        }
    }
}
