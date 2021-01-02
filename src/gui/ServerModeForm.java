package gui;

import enums.KiStrength;
import game.OnlineHostGame;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ServerModeForm {

    public static Scene create(Stage primaryStage) {
        primaryStage.setTitle("Server Mode Config");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        TextField fsTf = new TextField("10");
        TextField pnTf = new TextField("55555");
        TextField fiveTf = new TextField("1");
        TextField fourTf = new TextField("2");
        TextField threeTf = new TextField("3");
        TextField twoTf = new TextField("4");

        Button startBtn = new Button("Start Game");
        startBtn.setStyle("-fx-pref-width: 100px; -fx-pref-height: 20px;");
        startBtn.setOnAction(event -> {
            OnlineHostGame game = createGame(fsTf, pnTf, fiveTf, fourTf, threeTf, twoTf);

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
        btnVBox.getChildren().addAll(StaticNodes.createLabelForNode(fsTf, "Fieldsize"),
                StaticNodes.createLabelForNode(pnTf, "Portnumber"),
                StaticNodes.createLabelForNode(fiveTf, "No. 5 Ships"),
                StaticNodes.createLabelForNode(fourTf, "No. 4 Ships"),
                StaticNodes.createLabelForNode(threeTf, "No. 3 Ships"),
                StaticNodes.createLabelForNode(twoTf, "No. 2 Ships"),
                btnHBox);

        HBox centerHBox = new HBox(StaticNodes.getHSpacer(), btnVBox, StaticNodes.getHSpacer());

        contentVBox.getChildren().add(centerHBox);

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());

        Scene scene = new Scene(baseVBox);
        scene.getStylesheets().add(SceneA.class.getResource("MainMenu.css").toExternalForm());
        return scene;
    }

    private static OnlineHostGame createGame(TextField fs, TextField pn, TextField five, TextField four, TextField three, TextField two) {
        int step = 0;
        try {
            int fs_ = Integer.parseInt(fs.getText());
            if (fs_ < 5 || fs_ > 30) throw new NumberFormatException();
            step++;

            int pn_ = Integer.parseInt(pn.getText());
            if (pn_ < 0) throw new NumberFormatException();
            step++;

            int five_ = Integer.parseInt(five.getText());
            if (five_ < 0) throw new NumberFormatException();
            step++;

            int four_ = Integer.parseInt(four.getText());
            if (four_ < 0) throw new NumberFormatException();
            step++;

            int three_ = Integer.parseInt(three.getText());
            if (three_ < 0) throw new NumberFormatException();
            step++;

            int two_ = Integer.parseInt(two.getText());
            if (two_ < 0) throw new NumberFormatException();

            ArrayList<Integer> shipLengthsAr = new ArrayList<>();
            for (int i = 0; i < five_; i++) {
                shipLengthsAr.add(5);
            }
            for (int i = 0; i < four_; i++) {
                shipLengthsAr.add(4);
            }
            for (int i = 0; i < three_; i++) {
                shipLengthsAr.add(3);
            }
            for (int i = 0; i < two_; i++) {
                shipLengthsAr.add(2);
            }

            int[] shipLengths = new int[shipLengthsAr.size()];
            for (int i = 0; i < shipLengthsAr.size(); i++) {
                shipLengths[i] = shipLengthsAr.get(i);
            }
            GameOptions go = new GameOptions(fs_, KiStrength.INTERMEDIATE, five_, four_, three_, two_);

            return new OnlineHostGame(fs_, fs_, pn_, shipLengths, go);

        } catch (NumberFormatException e) {
            switch (step) {
                case 0:
                    StaticNodes.onTbxError(fs, 3000);
                    break;
                case 1:
                    StaticNodes.onTbxError(pn, 3000);
                    break;
                case 2:
                    StaticNodes.onTbxError(five, 3000);
                    break;
                case 3:
                    StaticNodes.onTbxError(four, 3000);
                    break;
                case 4:
                    StaticNodes.onTbxError(three, 3000);
                    break;
                case 5:
                    StaticNodes.onTbxError(two, 3000);
                    break;
                default:
                    break;
            }
            return null;
        }
    }
}
