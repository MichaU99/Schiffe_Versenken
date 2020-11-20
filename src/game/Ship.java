package game;

public class Ship extends Cell {
    private Position[] positions;
    private int length;

    public Position[] getPositions() {
        return positions;
    }

    public Ship(Position[] positions){
        super(30,30);
        this.positions = positions;
        this.length = positions.length;
    }

    public int shot(){
        this.length--;
        return this.length;
    }

    public boolean destroyed(){
        return this.length <= 0;
    }

    @Override
    public String toString() {
        return "s";
    }
}
