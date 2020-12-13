package JavaFx;

import enums.KiStrength;
import game.LocalGame;
import game.Position;
import game.cells.Ship;
import guiLogic.ClickedShips;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller_PutShips implements Initializable {
    private ClickedShips generated_Ships=null;
    private ArrayList<Integer> noch_zu_setzende_schiffe= new ArrayList<>();
    private int maxShipLen=3;
    @FXML
    private GridPane GridP;
    private LocalGame localGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        noch_zu_setzende_schiffe.add(5);
        this.localGame = new LocalGame(10, 10, KiStrength.INTERMEDIATE);
        makeField();
//        VBox game = this.createGame();
//        GridPane.setConstraints(game, 0, 0);
//        GridP.getChildren().add(game);

        Object[] array=GridP.getChildren().toArray();
        //GridPane.getColumnIndex

    }
    private void makeField(){
        for(int x=0;x<localGame.getField().getLength();x++){
            for(int y=0;y<localGame.getField().getHeight();y++){
                HBox l = new HBox();
                l.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                Label h = new Label("1");
                l.getChildren().add(h);
                GridPane.setConstraints(l,x,y);
                GridP.getChildren().add(l);
            }
        }
    }

    public void focusShip(MouseEvent event){ //Markiert das zu platzierende Schiff

    }

    public void klickShipintoExistance(MouseEvent event) throws IOException { //
        System.out.println("ColumnIndex: "+GridPane.getColumnIndex((Node)event.getTarget())+ " RowIndex: "+GridPane.getRowIndex((Node)event.getTarget()));
        int x=GridPane.getColumnIndex((Node)event.getTarget());
        int y=GridPane.getRowIndex((Node)event.getTarget());
        Position pos=new Position(x,y);


        if(generated_Ships!=null && generated_Ships.first!=null && generated_Ships.isInList(pos)) {//Feld kommt in List vor
            System.out.println(generated_Ships+" "+generated_Ships.isInList(pos));
            assert(generated_Ships.first.targetedDelete(pos)):"Muss vermutlich abgefangen werden falls es eintritt";
            HBox hbox=new HBox();
            hbox.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
            GridPane.setConstraints(hbox,x,y);
            GridP.getChildren().add(hbox);
        }
        else{
            if(generated_Ships==null || generated_Ships.first==null){
                generated_Ships= new ClickedShips(null,pos); //Für den "ersten" Klick
                HBox hbox = new HBox();
                hbox.setStyle("-fx-background-color: #A52A2A; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                GridPane.setConstraints(hbox, x, y);
                GridP.getChildren().add(hbox);
                }
            else if(!check_valid_pos(pos)){//Maximale Schiffsgröße erreicht, momentanerfolgt dann die  Löschung des ältesten Klicks
                Position[] todel=ClickedShipsToArray();


                for(int i=0;i< todel.length;i++){
                    HBox hbox=new HBox();
                    hbox.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                    GridPane.setConstraints(hbox,todel[i].getX(),todel[i].getY());
                    GridP.getChildren().add(hbox);
                }
                generated_Ships.first=null;
            }
            else {
                generated_Ships = new ClickedShips(generated_Ships.first, pos);
                HBox hbox = new HBox();
                hbox.setStyle("-fx-background-color: #A52A2A; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                GridPane.setConstraints(hbox, x, y);
                GridP.getChildren().add(hbox);
            }
        }

    }
    public void giveInfo(ActionEvent event){
        for(ClickedShips lauf=generated_Ships.first;lauf!=null;lauf=lauf.next) System.out.println(lauf.getPosition().toString());
    }
    public boolean check_valid_pos(Position pos){
        Position firstpos;
        if (generated_Ships.getLengh()>=maxShipLen) return false; //Schiff ist Länger als das längste Schiff
        if(generated_Ships.getLengh()>0){
            if (generated_Ships.getLengh()==1 && (generated_Ships.first.getPosition().getX()==pos.getX() || generated_Ships.first.getPosition().getY()==pos.getY() )) return true;
            else if(generated_Ships.first.next!=null && generated_Ships.first.getPosition().getX()==generated_Ships.next.getPosition().getX()) {
                    if(generated_Ships.first.getPosition().getX()!=pos.getX()) return false;
            }
            else if(generated_Ships.first.next!=null && generated_Ships.first.getPosition().getY()==generated_Ships.next.getPosition().getY()) {
                if(generated_Ships.first.getPosition().getY()!=pos.getY()) return false;

            }
            else return false;
        }
        return true;
    }

    public void addShip(ActionEvent event){ //Falls genug Felder markiert sind addiert die Methode das Schiff zum Spiel
        if(noch_zu_setzende_schiffe.contains(generated_Ships.getLengh())){
            localGame.getField().addShip(new Ship(ClickedShipsToArray()));
            generated_Ships=null;
        }
    }
    private Position[] ClickedShipsToArray(){
        ArrayList<Position> pos=new ArrayList();
        ClickedShips lauf=generated_Ships.first;
        while (lauf!=null ){
            pos.add(lauf.getPosition());
            lauf=lauf.next;
        }
        return pos.toArray(Position[]::new);
    }

    private VBox createGame(){
        VBox gameBaseVBox = new VBox(); // VBox ist die Basis für das Spielfeld in der GUI -> im Prinzip die Reihen
        gameBaseVBox.setId("myField");
        for (int i = 0; i < localGame.getField().getHeight(); i++) {
            // Für jede Reihe wird dann eben eine HBox für diese Reihe erstellt
            HBox row = new HBox();
            for (int j = 0; j < localGame.getField().getLength(); j++) {
                // In jeder Reihe gibt es noch eine Anzahl Zellen: macht hoffentlich Sinn
                HBox cell = new HBox();
                cell.setId(j + ":" + i); // Setze ID für die Zelle, um sie nachher wiedererkennen zu können
                cell.getStyleClass().add("hbox-water"); // Ein Style setzen
                //cell.setOnMouseClicked(SceneA::onFriendlyFieldClicked);

                Label lbl = new Label("0"); // Label zum anzeigen von Text, Label können ja im Prinzip auch Bilder usw anzeigen, wenn man möchte.
                lbl.setFont(new Font("Courier", 14)); // Monospace Font, damit alle Zellen gleich breit sind

                cell.getChildren().add(lbl); // Füge das Label in die Cell HBox ein
                row.getChildren().add(cell); // Füge die cellHBox in die rowHBox ein
            }
            gameBaseVBox.getChildren().add(row); // Füge jeder Reihe in die Spiel VBox ein
        }
        return gameBaseVBox;
    }

    public void backToStart(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void goToOptions(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips_Options.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public void goBackToPutShips(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips.fxml"));
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
