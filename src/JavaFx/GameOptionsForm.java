package JavaFx;

import enums.KiStrength;
import guiLogic.StaticNodes;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;

public class GameOptionsForm {

    private static TextField fsTf;
    private static TextField fiveTf;
    private static TextField fourTf;
    private static TextField threeTf;
    private static TextField twoTf;
    private static ComboBox<KiStrength> ks1Cbx;

    public static Scene create(Stage secondaryStage) {
        secondaryStage.setTitle("Game Options");
        secondaryStage.setWidth(600);
        secondaryStage.setHeight(400);

        VBox baseVBox = StaticNodes.getBaseVboxWithMenubar();
        VBox contentVBox = StaticNodes.getContentVbox();

        fsTf = new TextField();
        fiveTf = new TextField();
        fourTf = new TextField();
        threeTf = new TextField();
        twoTf = new TextField();

        ks1Cbx = new ComboBox<>();
        ks1Cbx.getItems().addAll(KiStrength.BEGINNER, KiStrength.INTERMEDIATE, KiStrength.STRONG, KiStrength.HELL);
        ks1Cbx.getSelectionModel().select(KiStrength.INTERMEDIATE);

        Button defaultBtn = new Button("default");
        defaultBtn.getStyleClass().add("small_btn");
        defaultBtn.setOnAction(event -> loadDefault());
        Button restoreBtn = new Button("restore");
        restoreBtn.getStyleClass().add("small_btn");
        restoreBtn.setOnAction(event -> loadCurSettings());
        Button closeBtn = new Button("close");
        closeBtn.getStyleClass().add("small_btn");
        closeBtn.setOnAction(event -> {
            saveCurSettings();
            secondaryStage.hide();
        });

        HBox btnHBox = new HBox();
        btnHBox.getChildren().addAll(defaultBtn, StaticNodes.getHSpacer(), restoreBtn, StaticNodes.getHSpacer(), closeBtn);

        VBox genVBox = new VBox(10);
        genVBox.getChildren().addAll(StaticNodes.createLabelForNode(fsTf, "Fieldsize"),
                StaticNodes.createLabelForNode(ks1Cbx, "Ki Strength"),
                StaticNodes.createLabelForNode(fiveTf, "No. 5 Ships"),
                StaticNodes.createLabelForNode(fourTf, "No. 4 Ships"),
                StaticNodes.createLabelForNode(threeTf, "No. 3 Ships"),
                StaticNodes.createLabelForNode(twoTf, "No. 2 Ships"),
                btnHBox);

        HBox centerHBox = new HBox(StaticNodes.getHSpacer(), genVBox, StaticNodes.getHSpacer());

        contentVBox.getChildren().add(centerHBox);
        loadCurSettings();

        baseVBox.getChildren().addAll(StaticNodes.getVSpacer(), contentVBox, StaticNodes.getVSpacer());
        Scene scene = new Scene(baseVBox, 300, 275);
        scene.getStylesheets().add(MainMenuForm.class.getResource("MainMenu.css").toExternalForm());
        return scene;
    }

    private static void loadCurSettings() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("game.options"))) {
            GameOptions options = ((GameOptions) inputStream.readObject());
            fsTf.setText(String.valueOf(options.getFieldSize()));
            fiveTf.setText(String.valueOf(options.getCarrier()));
            fourTf.setText(String.valueOf(options.getBattleship()));
            threeTf.setText(String.valueOf(options.getCruiser()));
            twoTf.setText(String.valueOf(options.getDestroyer()));
            ks1Cbx.getSelectionModel().select(options.getKiStrength());
        } catch (FileNotFoundException e) {
            loadDefault();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadDefault() {
        GameOptions options = new GameOptions();
        fsTf.setText(String.valueOf(options.getFieldSize()));
        fiveTf.setText(String.valueOf(options.getCarrier()));
        fourTf.setText(String.valueOf(options.getBattleship()));
        threeTf.setText(String.valueOf(options.getCruiser()));
        twoTf.setText(String.valueOf(options.getDestroyer()));
        ks1Cbx.getSelectionModel().select(KiStrength.INTERMEDIATE);
    }

    private static void saveCurSettings() {
        int fs = Integer.parseInt(fsTf.getText());
        int five = Integer.parseInt(fiveTf.getText());
        int four = Integer.parseInt(fourTf.getText());
        int three = Integer.parseInt(threeTf.getText());
        int two = Integer.parseInt(twoTf.getText());
        KiStrength kiStrength = ks1Cbx.getValue();
        GameOptions options = new GameOptions(fs, kiStrength, five, four, three, two);

        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("game.options"))) {
            outputStream.writeObject(options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
