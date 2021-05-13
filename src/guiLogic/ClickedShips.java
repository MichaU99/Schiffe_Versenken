package guiLogic;

import game.Position;


public class ClickedShips {
    public static ClickedShips first;
    private Position myPos;
    public ClickedShips next;
    static private int lengh;

    /**
     * Konstruktor der ClickedShips
     * @param c Erstes Element der Kette, null falls es keins gibt
     * @param p Zu speichernde Position
     */
    public ClickedShips(ClickedShips c,Position p){
        this.myPos=p;
        if(first==null){
            lengh=0;
            this.next=null;
        }
        else this.next=c;

        first=this;
        lengh++;
    }

    /**
     * Gibt den Wert des letzten Elements zurück
     * @return Letzte gespeicherte Position
     */
    public Position getLast(){
        if(next==null) return myPos;
        else return next.getLast();
    }

    /**
     * Entfernt das letzte Element aus der Kette
     */
    public boolean deleteLast(){
        if (lengh==1) first=null;
        else  next.recusivedeleteLast(lengh--);
        return true;
    }

    /**
     * Hilfsfunktion von {@link #deleteLast()}, sucht mittels der Länge der Kette nach dem letzten Glied um es zu Löschen
     * @param zaehler Zählt die bereits abgegangenen Elemente runter, bei 2 wird das nächste Element gelöscht
     */
    private void recusivedeleteLast(int zaehler){
        if (zaehler==2){
            next=null;
            return;
        }
        else recusivedeleteLast(zaehler-1);
    }

    /**
     * Entfernt eine markierung an der gewählten Position
     * @param pos Gewählte Position
     * @return Gibt zurück ob das Element entfernt werden konnte
     */
    public boolean targetedDelete(Position pos){
        if(myPos.equals(pos) && next==null){
            first=null; //Falls nur ein Element in Clicked Ships ist
            lengh--;
            return true;
        }
        if(myPos.equals(pos)){
            first=next;
            lengh--;
            return true;
        }
        if(next==null) return false; //Falls das gesuchte Element nicht in ClickedShips existiert
        if(next.myPos.equals(pos)) {
            lengh--;
            next = next.next;
            return true;
        }

        return next.targetedDelete(pos);
    }

    /**
     * Gibt zurück ob ein übergebenes Element in der Liste ist
     */
    public boolean isInList(Position pos){
        if(myPos.equals(pos)) return true;
        if(next==null) return false;
        else return next.isInList(pos);
    }

    public Position getPosition(){
        return myPos;
    }

    public ClickedShips nextClickedShips(){
        return next;
    }

    public Integer getLengh(){
        return lengh;
    }
}
