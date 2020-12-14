package JavaFx;

import game.cells.Cell;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Controller_PutShips implements Initializable {
    private ClickedShips generated_Ships=null;
    private ArrayList<Integer> noch_zu_setzende_schiffe= new ArrayList<>();
    private int maxShipLen=5;
    @FXML
    private GridPane GridP;
    @FXML
    private ListView list;
    @FXML
    private Button Start_bt;
    private LocalGame localGame;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Start_bt.setDisable(true);
        List<Integer> standardlist= Arrays.asList(5,4,3,3,2);
        noch_zu_setzende_schiffe.addAll(standardlist);
        for(int i=0;i<noch_zu_setzende_schiffe.size();i++) {
            list.getItems().add("Schiff der Länge: "+noch_zu_setzende_schiffe.get(i));
        }
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
               // Label h = new Label("1");
               // l.getChildren().add(h);
                GridPane.setConstraints(l,x,y);
                GridP.getChildren().add(l);
            }
        }
    }

    public void klickShipintoExistance(MouseEvent event) throws IOException { //

        if(event==null ||event.getTarget()==null) return;
        int x=GridPane.getColumnIndex((Node)event.getTarget());
        int y=GridPane.getRowIndex((Node)event.getTarget());
        Position pos=new Position(x,y);

        if(this.localGame.getField().getPlayfield()[y][x].getClass()!= Cell.class){ return;}
        else if(generated_Ships!=null && generated_Ships.first!=null && generated_Ships.isInList(pos)) {//Feld kommt in List vor
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
                deletedMarked();
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
    public void autofill(ActionEvent event){
        //if(Platzierte Schiffe=0) Fülle gesamtes Feld den Schiffen aus noch_zu_setzende_schiffe aus
        //if else(Platzierte_Schiffe>0, vielleicht mit der ersten if zusammen je nach umsetzung im backend) Behält die bereits gesetzten Schiffe und setzt die noch verbleibenden zufällig dazu
        //else Falls alle Schiffe gesetzt sind soll Autofill alle gesetzten Schiffe löschen und zufällig neu setzen
    }

    public boolean check_valid_pos(Position pos){
        System.out.println(generated_Ships.getLengh());
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
    private void deletedMarked(){//Löscht alle aktuell markierten Positionen aus der Liste und visuell
        Position[] todel=ClickedShipsToArray();

        for(int i=0;i< todel.length;i++){
            HBox hbox=new HBox();
            hbox.setStyle("-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
            GridPane.setConstraints(hbox,todel[i].getX(),todel[i].getY());
            GridP.getChildren().add(hbox);
        }
        generated_Ships.first=null;
    }

    public void addShip(ActionEvent event){ //Falls genug Felder markiert sind addiert die Methode das Schiff zum Spiel
        if(generated_Ships!=null && noch_zu_setzende_schiffe.contains(generated_Ships.getLengh())) {
            if(!working_ship()){
                deletedMarked();
            }
            else {
                Position[] todel = ClickedShipsToArray();

                localGame.getField().addShip(new Ship(todel)); //Erzeugt ein neues Schiff mit den Positionen aus todel
                noch_zu_setzende_schiffe.remove(noch_zu_setzende_schiffe.indexOf(generated_Ships.getLengh())); //Entfernt einen den Eintrag der Länge des eingefügten Schiffs aus der Liste
                list.getItems().remove("Schiff der Länge: " + generated_Ships.getLengh());

                for (int i = 0; i < todel.length; i++) {
                    HBox hbox = new HBox();
                    hbox.setStyle("-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em");
                    GridPane.setConstraints(hbox, todel[i].getX(), todel[i].getY());
                    GridP.getChildren().add(hbox);

                }
                generated_Ships.first = null;
                if(noch_zu_setzende_schiffe.isEmpty()) Start_bt.setDisable(false);
            }
        }
        else System.out.println("Anzahl gewählter Felder stimmt nicht mit noch zu setzenden Schiffen überein");
    }

    private boolean working_ship(){//Interne Methode die prüft ob ein Schiff zusammenhängend ist
        int min=1000;
        if(generated_Ships.first.next!=null && generated_Ships.first.getPosition().getY()==generated_Ships.first.next.getPosition().getY()) {
            for (ClickedShips lauf=generated_Ships.first; lauf!=null; lauf=lauf.next) {
                if (min > lauf.getPosition().getX()) min=lauf.getPosition().getX();
            }
            zusammenhang1:
            for(int i=0;i<generated_Ships.getLengh();i++){
                for (ClickedShips lauf=generated_Ships.first; lauf!=null; lauf=lauf.next) {
                    if (lauf.getPosition().getX() ==min+i) continue zusammenhang1;
                }
                return false;
            }
        }
        else if(generated_Ships.first.next!=null && generated_Ships.first.getPosition().getX()==generated_Ships.first.next.getPosition().getX()) {
            for (ClickedShips lauf=generated_Ships.first; lauf!=null; lauf=lauf.next) {
                if (min > lauf.getPosition().getY()) min=lauf.getPosition().getY();
            }
            zusammenhang2:
            for(int i=0;i<generated_Ships.getLengh();i++){
                for (ClickedShips lauf=generated_Ships.first; lauf!=null; lauf=lauf.next) {
                    if (lauf.getPosition().getY() ==min+i) continue zusammenhang2;
                }
                return false;
            }
        }
        else return false; //Sollte nicht erreichbar sein
        return true;
    }

    private Position[] ClickedShipsToArray(){//Gibt die interne Datenstruktur generated_ships als array von Positions zurück
        ArrayList<Position> pos=new ArrayList();
        ClickedShips lauf=generated_Ships.first;
        while (lauf!=null ){
            pos.add(lauf.getPosition());
            lauf=lauf.next;
        }
        return pos.toArray(Position[]::new);
    }

    public void startGame(ActionEvent event) throws IOException {
        if(noch_zu_setzende_schiffe.isEmpty()){
            Parent root= FXMLLoader.load(getClass().getResource("Layout_PlayGame"));
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        else System.out.println("Es gibt noch ungesetzte Schiffe");
    }

    public void remove(ActionEvent event){ //Tastendruck auf Schiff, entfernt Schiff
        //Muss hier auf einen Input in der GridPane warten oder die Methode terminieren falls etwas anderes gedrückt wird
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
