package game.cells;

public class Shot extends Cell{
    // Eine Zelle egal welches Typs wird zu Shot, wenn auf diese gefeuert wurde.
    // Wenn diese ein Schiff war ist wasShip true, in jedem anderen Fall false.

    private boolean wasShip = false;

    public boolean getWasShip() {
        return this.wasShip;
    }

    public void setWasShip(boolean wasShip) {
        this.wasShip = wasShip;
    }

    public Shot() {

    }

    public Shot(boolean wasShip) {
        this.wasShip = wasShip;
    }

    @Override
    public String toString() {
        if (this.wasShip)
            return "ㅅ";
        return "ㅁ";
    }
}
