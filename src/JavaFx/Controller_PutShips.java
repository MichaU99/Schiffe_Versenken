package JavaFx;

import game.Game;
import game.LocalGame;
import game.OnlineGame;
import game.Position;
import game.cells.Cell;
import game.cells.Ship;
import guiLogic.ClickedShips;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller_PutShips implements Initializable {
    private ClickedShips generated_Ships=null;
    private ArrayList<Integer> noch_zu_setzende_schiffe= new ArrayList<>();
    private int maxShipLen=5;
    @FXML
    private GridPane GridP;
    @FXML
    private ListView<String> listView;
    @FXML
    private Button Start_bt;
    @FXML
    private Button optionbtn;
    private Game game;
    public static boolean online = false;
    private GameOptions options;
    private Position loeschpos=null;
    String waterCell="-fx-background-color: #00BFFF; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    String shipCell="-fx-background-color: #000000; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    String loeschMarkCell="-fx-background-color: #7700ff; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";
    String markCell="-fx-background-color: #A52A2A; -fx-margin: 5 5 5 5;-fx-border-color: #000000;-fx-pref-height: 5em;-fx-pref-width: 5em;-fx-min-height: 1em;-fx-min-width: 1em";

    // TODO: 30.12.2020 Minimalgröße der Stage festsetzen


    /**
     * Initialisiert das PutShips Layout, unterscheidet dabei zwischen Online und Local Play.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Controller_GameScreen.saveGame=false;
        GridP.getColumnConstraints().clear();
        GridP.getRowConstraints().clear();
        if(online){
            //System.out.println(game);
            game=Controller_GameScreen.game;
            gui.GameOptions gOptions= ((OnlineGame) game).gameOptions;
            optionbtn.setDisable(true);
            optionbtn.setVisible(false);
            noch_zu_setzende_schiffe=new ArrayList<>();
            noch_zu_setzende_schiffe=gOptions.getShipList();
            updateListView();
        }
        else {
            updateGameOptions();
            game = new LocalGame(options.getFieldSize(), options.getFieldSize(), options.getKiStrength());
        }
        makeField();
    }

    /**
     * Updated ListView um alle unplatzierten Schiffe anzuzeigen
     */
    private void updateListView(){
        listView.getItems().clear();
        for (Integer integer : noch_zu_setzende_schiffe) {
            listView.getItems().add("Schiff der Länge: " + integer);
        }
    }

    /**
     * Zieht sich aus den GameOptions die aktuellen Einstellungen
     */
    private void updateGameOptions(){
        Start_bt.setDisable(true);

        try {
            ObjectInputStream inputStream=new ObjectInputStream(new FileInputStream("game.options"));
            options=((GameOptions) inputStream.readObject());
        } catch (IOException | ClassNotFoundException e) {
            options = new GameOptions();
            try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("game.options"))) {
                os.writeObject(options);
                os.flush();
            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
        }
        noch_zu_setzende_schiffe=new ArrayList<>();
        noch_zu_setzende_schiffe=options.getShipList();
        updateListView();
    }

    /**
     * Generiert ein einfaches Wasserfeld
     */
    private void makeField(){
        for(int x = 0; x< game.getField().getLength(); x++){
            for(int y = 0; y< game.getField().getHeight(); y++){
                HBox l = new HBox();
                l.setStyle(waterCell);
                GridPane.setConstraints(l,x,y);
                GridP.getChildren().add(l);
            }
        }
    }

    /**
     * Markiert geklickte Felder fürs Schiffeplatzieren, erkennt ob geklicktes Feld valide ist
     */

    public void klickShipintoExistance(MouseEvent event){
        int x,y;
        if(event==null ||event.getTarget()==null) return;
        try {
            x = GridPane.getColumnIndex((Node) event.getTarget());
            y = GridPane.getRowIndex((Node) event.getTarget());
        }
        catch (NullPointerException doNothing){
            return;
        }


        Position pos=new Position(x,y); //in Pos liegt die aus der GridPane erhaltene position des Klicks
        deleteLoeschpos(); //Reset der löschpos
        if(!noch_zu_setzende_schiffe.isEmpty()) maxShipLen=noch_zu_setzende_schiffe.get(0);
        else maxShipLen=0;

        if(game.getField().getPlayfield()[y][x].getClass()== Ship.class){ //Setzt Löschmarkierung falls auf ein bereits gesetztes Schiff geklickt wurde
            deletedMarked();
            loeschpos=pos;
            HBox hbox=new HBox();
            hbox.setStyle(loeschMarkCell);
            GridPane.setConstraints(hbox,x,y);
            GridP.getChildren().add(hbox);
        }
        else if(this.game.getField().getPlayfield()[y][x].getClass()!= Cell.class){ return;} //Fängt Klicks auf Felder ab, die keine validen Felder für Schiffe wären
        else if(generated_Ships!=null && ClickedShips.first!=null && ClickedShips.first.isInList(pos)) {//Feld kommt in List vor (war bereits markiert)
            ClickedShips.first.targetedDelete(pos);
            HBox hbox=new HBox();
            hbox.setStyle(waterCell);
            GridPane.setConstraints(hbox,x,y);
            GridP.getChildren().add(hbox);
        }
        else{
            if(generated_Ships==null || ClickedShips.first==null){
                generated_Ships= new ClickedShips(null,pos); //Für den "ersten" Klick
                HBox hbox = new HBox();
                hbox.setStyle(markCell);
                GridPane.setConstraints(hbox, x, y);
                GridP.getChildren().add(hbox);
                }
            else if(!check_valid_pos(pos)){//Maximale Schiffsgröße erreicht, momentanerfolgt dann die  Löschung des ältesten Klicks
                deletedMarked();
            }
            else {
                generated_Ships = new ClickedShips(ClickedShips.first, pos);
                HBox hbox = new HBox();
                hbox.setStyle(markCell);
                GridPane.setConstraints(hbox, x, y);
                GridP.getChildren().add(hbox);
            }
        }

    }

    /**
     * Methode des autofill Buttons, setzt alle noch ungesetzten Schiffe nach Möglichkeit zufällig ins Feld
     * @param event
     */
    public void autofill(ActionEvent event){
        if(game.getField().addShipRandomKeepShips(noch_zu_setzende_schiffe)) {
            noch_zu_setzende_schiffe.clear();
            Start_bt.setDisable(false);
            updateGame();
            updateListView();
        }
        else{
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText("NICHT ALLE SCHIFFE PASSEN INS FELD\nVergrößern Sie entweder das Feld oder\nwählen Sie weniger Schiffe in den Optionen");
            alert.showAndWait();
        }
    }

    /**
     * Updated visuell die GridPane nach den bereits addierten Schiffen
     */
    private void updateGame(){
        GridP.getChildren().clear();
        for(int x = 0; x< game.getField().getLength(); x++){
            for(int y = 0; y< game.getField().getHeight(); y++){
                HBox cell = new HBox();
                if (game.getField().getCell(new Position(x, y)) instanceof Ship) {
                    cell.setStyle(shipCell);
                }
                else {
                    cell.setStyle(waterCell);
                }
                GridPane.setConstraints(cell,x,y);
                GridP.getChildren().add(cell);
            }
        }
    }

    /**
     * Überprüft ob eine Position eine valide Platzierung für ein Schiffselement wäre
     * @param pos Position die zu überprüfen ist
     * @return
     */
    public boolean check_valid_pos(Position pos){
        System.out.println(generated_Ships.getLengh());
        if (generated_Ships.getLengh()>=maxShipLen) return false; //Schiff ist Länger als das längste Schiff
        if(generated_Ships.getLengh()>0){
            if (generated_Ships.getLengh()==1 && (ClickedShips.first.getPosition().getX()==pos.getX() || ClickedShips.first.getPosition().getY()==pos.getY() )) return true;
            else if(ClickedShips.first.next!=null && ClickedShips.first.getPosition().getX()==generated_Ships.next.getPosition().getX()) {
                    if(ClickedShips.first.getPosition().getX()!=pos.getX()) return false;
            }
            else if(ClickedShips.first.next!=null && ClickedShips.first.getPosition().getY()==generated_Ships.next.getPosition().getY()) {
                if(ClickedShips.first.getPosition().getY()!=pos.getY()) return false;

            }
            else return false;
        }
        return true;
    }

    /**
     * Löscht alle aktuell markierten Positionen aus der Liste und visuell
     */
    private void deletedMarked(){
        Position[] todel=ClickedShipsToArray();

        for(int i=0;i< todel.length;i++){
            HBox hbox=new HBox();
            hbox.setStyle(waterCell);
            GridPane.setConstraints(hbox,todel[i].getX(),todel[i].getY());
            GridP.getChildren().add(hbox);
        }
        deleteLoeschpos();
        ClickedShips.first=null;
    }

    /**
     * Entfernt die Löschmarkierung
     */
    private void deleteLoeschpos(){
        if(loeschpos==null) return;
        HBox hbox=new HBox();
        hbox.setStyle(shipCell);
        GridPane.setConstraints(hbox,loeschpos.getX(),loeschpos.getY());
        GridP.getChildren().add(hbox);
        loeschpos=null;
    }

    /**
     * Fügt alle markierten Felder als ein Schiff zum Spiel hinzu falls es valide ist.
     * valide: Entspricht in der Länge einem Schiff der Liste
     * @param event
     */
    public void addShip(ActionEvent event){
        if(generated_Ships!=null && noch_zu_setzende_schiffe.contains(generated_Ships.getLengh())) {
            if(!working_ship()){
                deletedMarked();
            }
            else {
                Position[] todel = ClickedShipsToArray();

                game.getField().addShip(new Ship(todel)); //Erzeugt ein neues Schiff mit den Positionen aus todel
                noch_zu_setzende_schiffe.remove(noch_zu_setzende_schiffe.indexOf(generated_Ships.getLengh())); //Entfernt einen den Eintrag der Länge des eingefügten Schiffs aus der Liste
                listView.getItems().remove("Schiff der Länge: " + generated_Ships.getLengh());

                for (int i = 0; i < todel.length; i++) {
                    HBox hbox = new HBox();
                    hbox.setStyle(shipCell);
                    GridPane.setConstraints(hbox, todel[i].getX(), todel[i].getY());
                    GridP.getChildren().add(hbox);

                }
                ClickedShips.first = null;
                if(noch_zu_setzende_schiffe.isEmpty()) Start_bt.setDisable(false);
            }
        }
        else System.out.println("Anzahl gewählter Felder stimmt nicht mit noch zu setzenden Schiffen überein");
    }

    /**
     * Interne Methode die prüft ob Markierungen ein zusammenhängendes Schiff bilden
     * @return
     */
    private boolean working_ship(){//Interne Methode die prüft ob ein Schiff zusammenhängend ist
        int min=1000;
        if(ClickedShips.first.next!=null && ClickedShips.first.getPosition().getY()==ClickedShips.first.next.getPosition().getY()) {
            for (ClickedShips lauf=ClickedShips.first; lauf!=null; lauf=lauf.next) {
                if (min > lauf.getPosition().getX()) min=lauf.getPosition().getX();
            }
            zusammenhang1:
            for(int i=0;i<generated_Ships.getLengh();i++){
                for (ClickedShips lauf=ClickedShips.first; lauf!=null; lauf=lauf.next) {
                    if (lauf.getPosition().getX() ==min+i) continue zusammenhang1;
                }
                return false;
            }
        }
        else if(ClickedShips.first.next!=null && ClickedShips.first.getPosition().getX()==ClickedShips.first.next.getPosition().getX()) {
            for (ClickedShips lauf=ClickedShips.first; lauf!=null; lauf=lauf.next) {
                if (min > lauf.getPosition().getY()) min=lauf.getPosition().getY();
            }
            zusammenhang2:
            for(int i=0;i<generated_Ships.getLengh();i++){
                for (ClickedShips lauf=ClickedShips.first; lauf!=null; lauf=lauf.next) {
                    if (lauf.getPosition().getY() ==min+i) continue zusammenhang2;
                }
                return false;
            }
        }
        else return false; //Sollte nicht erreichbar sein
        return true;
    }

    /**
     * Gibt die interne Datenstruktur generated_ships als array von Positions zurück
     * @return
     */
    private Position[] ClickedShipsToArray(){
        ArrayList<Position> pos=new ArrayList<>();
        ClickedShips lauf=ClickedShips.first;
        while (lauf!=null ){
            pos.add(lauf.getPosition());
            lauf=lauf.next;
        }
        return pos.toArray(Position[]::new);
    }

    /**
     * Schaut ob alle Schiffe gesetzt wurden, setzt game in Controller_GameScreen und wechselt die Szene dorthin.
     * TL;DR: Wechselt zum Gamescreen
     * @param event
     * @throws IOException
     */
    public void startGame(ActionEvent event) throws IOException {
        if(noch_zu_setzende_schiffe.isEmpty()){
            Controller_GameScreen.game=this.game;
            Parent root= FXMLLoader.load(getClass().getResource("Layout_GameScreen.fxml"));
            Scene scene = new Scene(root,800,600);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        else System.out.println("Es gibt noch ungesetzte Schiffe");
    }

    /**
     * Falls ein Schiff markiert wurde wird es aus dem Spiel entfernt und wieder der Liste zum neuplatzieren hinzugefügt
     * @param event
     */
    public void remove(ActionEvent event){ //Tastendruck auf Schiff, entfernt Schiff
        if (loeschpos!=null) {
            noch_zu_setzende_schiffe.add(((Ship)(game.getField().getCell(loeschpos))).getPositions().length);
            game.getField().removeShip(loeschpos);
            deletedMarked();
            updateGame();
            updateListView();

        }
        else{
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Für das entfernen von Schiffen,\nmuss erst ein Schiffsfeld zum entfernen gewählt werden");
            alert.showAndWait();
        }
    }

    /**
     * Ändert die Szene von Layout_PutShips to Layout_PutShipOptions
     * @param event
     * @throws IOException
     */
    public void backToStart(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_NewGame.fxml"));
        Scene scene = new Scene(root,800,600);

        Stage stage = (Stage) GridP.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Ändert die Szene von Layout_PutShips to Layout_PutShipOptions
     * @param event
     * @throws IOException
     */
    public void goToOptions(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("Layout_PutShips_Options.fxml"));
        Scene scene = new Scene(root,800,600);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setOnHiding(windowEvent -> updateGameOptions());
        stage.setScene(scene);
        stage.show();
    }

}
