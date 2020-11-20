package game.cells;

public class Shot extends Cell{

    private boolean wasShip = false;

    public void setWasShip(boolean wasShip) {
        this.wasShip = wasShip;
    }

    public Shot() {

        super();
    }

    public Shot(boolean wasShip) {
        super();
        this.wasShip = wasShip;
    }

    @Override
    public String toString() {
        if (this.wasShip)
            return "ㅅ";
        return "ㅁ";
    }
}
