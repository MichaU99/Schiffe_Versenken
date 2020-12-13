package guiLogic;

import game.Position;


public class ClickedShips {
    public static ClickedShips first;
    private Position myPos;
    public ClickedShips next;
    static private int lengh;

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
    public Position getLast(){
        if(next==null) return myPos;
        else return next.getLast();
    }

    public boolean deleteLast(){
        if (lengh==1) first=null;
        else  next.recusivedeleteLast(lengh--);
        return true;
    }
    private void recusivedeleteLast(int zaehler){
        if (zaehler==2){
            next=null;
            return;
        }
        else recusivedeleteLast(zaehler-1);
    }
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
