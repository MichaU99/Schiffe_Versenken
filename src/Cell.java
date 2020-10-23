public class Cell {

    public Cell(){

    }

    public int shot(){
        // returns -1 if shot was missed, otherwise the remaining health of the ship
        return -1;
    }

    @Override
    public String toString() {
        return "0";
    }
}
