package ki;

import enums.KiStrength;
import game.FieldImpl;
import game.Position;
import game.cells.Cell;
import game.cells.Ship;
import game.cells.Shot;

import java.util.ArrayList;
import java.util.Random;

public class Ki {
    private FieldImpl enemyField; // this is the players playfield
    public FieldImpl kiEnemyField; // this is the vision of the ki of the enemy Field;
    private FieldImpl kiField; // this is the ki's field | is this needed?
    private final KiStrength kiStrength;
    private ArrayList<Integer> enemyRemainingShipLengths = new ArrayList<>();

    public Ki(FieldImpl enemyField, FieldImpl kiField, KiStrength kiStrength){
        this.enemyField = enemyField;
        this.kiField = kiField;
        this.kiStrength = kiStrength;
        this.kiEnemyField = new FieldImpl(enemyField.getHeight(), enemyField.getLength());
    }

    public int shoot() {
        assert this.enemyRemainingShipLengths.size() > 0: "Es wurden keine Schiffe hinzugefügt oder alle Schiffe sind entfernt";

        switch (this.kiStrength) {
            case BEGINNER -> {
                return this.shootRandom();
            }
            case INTERMEDIATE -> {
                return this.shootRows();
            }
        }
        return -1;
    }

    private int shootRandom() {
        Random r = new Random();

        while (true) {
            int height = r.nextInt(this.enemyField.getHeight());
            int length = r.nextInt(this.enemyField.getLength());

            if (!(this.enemyField.getPlayfield()[height][length] instanceof Shot)) {
                return this.enemyField.registerShot(new Position(length, height));
            }
        }
    }

    Position nextShot = new Position(0, 0);
    ArrayList<Position> shipFound = new ArrayList<>();
    char direction = 'e';
    private int shootRows() {
        System.out.println(nextShot + ", dir: " + direction + ", shipFoundSize: " + shipFound.size());
        int rc = this.enemyField.registerShot(nextShot);  //TODO this should be changed to also work in online context
        System.out.println("rc: " + rc);

        this.kiEnemyField.getPlayfield()[nextShot.getY()][nextShot.getX()] = new Shot();
        if (rc == 0) {

            if (shipFound.size() == 1) {
                // this means that ONE of the previous shots hit a ship but this one didn't -> direction of the ship isn't fixed yet
                switch (direction) {
                    case 'e':
                        // try to shoot backwards aka west but consider bounds of array
                        if (nextShot.getX() - 2 < 0) {
                            if (nextShot.getY() + 1 >= this.enemyField.getHeight()) {
                                nextShot = new Position(nextShot.getX() - 1, nextShot.getY() - 1);
                                direction = 'n';
                            }
                            else {
                                nextShot = new Position(nextShot.getX() - 1, nextShot.getY() + 1);
                                direction = 's';
                            }
                        }
                        else {
                            nextShot = new Position(nextShot.getX() - 2, nextShot.getY());
                            direction = 'w';
                        }
                        break;

                    case 'w':
                        // try to shoot down aka south
                        if (nextShot.getY() + 1 >= this.enemyField.getHeight()) {
                            nextShot = new Position(nextShot.getX() + 1, nextShot.getY() - 1);
                            direction = 'n';
                        }
                        else {
                            nextShot = new Position(nextShot.getX() + 1, nextShot.getY() + 1);
                            direction = 's';
                        }
                        break;

                    case 's':
                        // every direction except up aka north failed, shoot north
                        nextShot = new Position(nextShot.getX(), nextShot.getY() - 2);
                        direction = 'n';
                        break;
                }
                // after this every option for the ship directions are done
                // this means we have to have hit at least one more shot which means we can fix the direction of the ship
            }

            if (shipFound.size() > 1) {
                // this means TWO or more previous shots hit. this means the direction is fixed
                switch (direction) {
                    case 'e':
                        // ship is horizontal and other positions have to be left
                        nextShot = new Position(nextShot.getX() - (shipFound.size() + 1), nextShot.getY());
                        direction = 'w';
                        break;

                    case 's':
                        // ship is vertical and other positions have to be top
                        nextShot = new Position(nextShot.getX(), nextShot.getY() - (shipFound.size() + 1));
                        direction = 'n';
                        break;

                    case 'w':
                        nextShot = new Position(nextShot.getX() - 1, nextShot.getY());
                        break;

                    case 'n':
                        nextShot = new Position(nextShot.getX(), nextShot.getY() - 1);
                        break;
                }
            }

            if (shipFound.size() == 0) {
                nextShot = getNextShotPosition(nextShot);
            }
        }

        if (rc == 1) {
            shipFound.add(nextShot);
            switch (direction) {
                case 'e':
                    if (nextShot.getX() + 1 >= this.enemyField.getLength()) {
                        nextShot = new Position(nextShot.getX() - 1, nextShot.getY());
                        direction = 'w';
                        //return this.shootRows();
                    }
                    else {
                        nextShot = new Position(nextShot.getX() + 1, nextShot.getY());
                    }
                    break;

                case 'w':
                    nextShot = new Position(nextShot.getX() - 1, nextShot.getY());
                    break;

                case 's':
                    nextShot = new Position(nextShot.getX(), nextShot.getY() + 1);
                    if (nextShot.getY() >= this.enemyField.getHeight()) {
                        int nextY = nextShot.getY() - 1;
                        while (this.kiEnemyField.getPlayfield()[nextY][nextShot.getX()].getClass() != Cell.class) {
                            nextY--;
                        }
                        nextShot = new Position(nextShot.getX(), nextY);
                        direction = 'n';
                    }
                    break;

                case 'n':
                    nextShot = new Position(nextShot.getX(), nextShot.getY() - 1);
                    break;
            }
        }

        if (rc == 2) {
            shipFound.add(nextShot);
            this.kiEnemyField.addShip(new Ship(shipFound));
            for (Position position: shipFound) {
                this.kiEnemyField.getPlayfield()[position.getY()][position.getX()] = new Shot(true);
            }
            this.enemyRemainingShipLengths.remove((Integer) shipFound.size());
            if (this.enemyRemainingShipLengths.size() > 0){
                nextShot = this.getNextShotPosition(shipFound.get(0));
            }
            shipFound.clear();
            direction = 'e';
        }
        return rc;
    }

    private Position getNextShotPosition(Position lastShot) {
        System.out.println("Übrige Schiffe: " + enemyRemainingShipLengths.size());
        Position nextShot = lastShot;
        int shortestShip = this.enemyRemainingShipLengths.get(this.enemyRemainingShipLengths.size() - 1);
        while (this.kiEnemyField.getPlayfield()[nextShot.getY()][nextShot.getX()].getClass() != Cell.class) {
            int nextX = nextShot.getX() + shortestShip; // add length of shortest ship of the field for optimal coverage
            int nextY = nextShot.getY();
            if (nextX >= this.enemyField.getLength()) {
                nextY++;
                if (nextY % shortestShip == 0) {
                    nextX = 0;
                }
                else if (nextY % shortestShip == 1){
                    nextX = 1;
                }
                else if (nextY % shortestShip == 2){
                    nextX = 2;
                }
                else if (nextY % shortestShip == 3){
                    nextX = 3;
                }
                else if (nextY % shortestShip == 4){
                    nextX = 4;
                }
            }
            if (nextY >= this.enemyField.getHeight()){
                nextX = 0;
                nextY = 0;
            }
            assert nextY < this.enemyField.getHeight() : "nextY ist: " + nextY;
            nextShot = new Position(nextX, nextY);
        }

        // System.out.println(this.kiEnemyField.getPlayfield()[nextShot.getY()][nextShot.getY()].getClass());

        return nextShot;
    }
}
