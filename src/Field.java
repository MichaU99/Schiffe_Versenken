import java.util.Random;

public class Field implements FieldInterface{
    private Cell[][] playfield;
    private int height;
    private int length;

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public Field(int height, int length){
        this.playfield = new Cell[height][length];
        this.height = height;
        this.length = length;
        this.resetField();
    }

    public void resetField(){
        for (int i = 0; i < playfield.length; i++) {
            for (int j = 0; j < playfield.length; j++) {
                this.playfield[i][j] = new Cell();
            }
        }
    }

    public boolean addShip(Ship ship){
        // check if any position of ship is already occupied
        for(Position position: ship.getPositions()){
            if (this.playfield[position.getY()][position.getX()].getClass() != Cell.class)
                return false;
        }

        // place ship on playfield
        for(Position position: ship.getPositions()){
            this.playfield[position.getY()][position.getX()] = ship;
        }

        // block fields arround ship
        for(Position position: ship.getPositions()) {
            if (position.getX() > 0 && this.playfield[position.getY()][position.getX() - 1].getClass() == Cell.class)
                this.playfield[position.getY()][position.getX() - 1] = new Block();
            if (position.getX() < this.playfield.length - 1 &&
                    this.playfield[position.getY()][position.getX() + 1].getClass() == Cell.class)
                this.playfield[position.getY()][position.getX() + 1] = new Block();
            if (position.getY() > 0 &&
                    this.playfield[position.getY() - 1][position.getX()].getClass() == Cell.class)
                this.playfield[position.getY() - 1][position.getX()] = new Block();
            if (position.getY() < this.playfield.length - 1 &&
                    this.playfield[position.getY() + 1][position.getX()].getClass() == Cell.class)
                this.playfield[position.getY() + 1][position.getX()] = new Block();
            if (position.getX() > 0 && position.getY() > 0 &&
                    this.playfield[position.getY() - 1][position.getX() - 1].getClass() == Cell.class)
                this.playfield[position.getY() - 1][position.getX() - 1] = new Block();
            if (position.getX() < this.playfield.length - 1 && position.getY() < this.playfield.length - 1 &&
                    this.playfield[position.getY() + 1][position.getX() + 1].getClass() == Cell.class)
                this.playfield[position.getY() + 1][position.getX() + 1] = new Block();
            if (position.getX() > 0 && position.getY() < this.playfield.length - 1 &&
                    this.playfield[position.getY() + 1][position.getX() - 1].getClass() == Cell.class)
                this.playfield[position.getY() + 1][position.getX() - 1] = new Block();
            if (position.getX() < this.playfield.length - 1 && position.getY() > 0 &&
                    this.playfield[position.getY() - 1][position.getX() + 1].getClass() == Cell.class)
                this.playfield[position.getY() - 1][position.getX() + 1] = new Block();
            }
        return true;
    }

    public boolean addShipRandom(int length){
        Random r = new Random();
        boolean placed = false;
        char [] directions = {'n', 'e', 's', 'w'};

        while (!placed){
            Position startPos = new Position(r.nextInt(10), r.nextInt(10));
            Position[] positions = new Position[length];
            char direction = directions[r.nextInt(4)];

            switch (direction) {
                case 'n' -> {
                    if (startPos.getY() - length < 0) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX(), startPos.getY() - i);
                    }
                    placed = this.addShip(new Ship(positions));
                }
                case 's' -> {
                    if (startPos.getY() + length > this.playfield.length - 1) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX(), startPos.getY() + i);
                    }
                    placed = this.addShip(new Ship(positions));
                }
                case 'w' -> {
                    if (startPos.getX() - length < 0) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX() - i, startPos.getY());
                    }
                    placed = this.addShip(new Ship(positions));
                }
                case 'e' -> {
                    if (startPos.getX() + length > this.playfield.length - 1) {
                        continue;
                    }
                    positions[0] = startPos;
                    for (int i = 1; i < length; i++) {
                        positions[i] = new Position(startPos.getX() + i, startPos.getY());
                    }
                    placed = this.addShip(new Ship(positions));
                }
            }
        }
        return true;
    }

    public boolean addShipRandom(int [] lengths){
        for (int length: lengths){
            addShipRandom(length);
        }
        return true;
    }

    public int registerShot(Position position){
        Cell cell = this.playfield[position.getY()][position.getX()];
        return cell.shot();
    }

    public void printField(){
        for (int i = 65; i < this.playfield.length + 65; i++) {
            System.out.print("\t" + (char)i);
        }
        System.out.println();
        for (int i = 0; i < this.playfield.length; i++) {
            System.out.print(i + 1);
            for (Cell cell: this.playfield[i]){
                System.out.print("\t" + cell.toString());
            }
            System.out.println();
        }
    }
}
