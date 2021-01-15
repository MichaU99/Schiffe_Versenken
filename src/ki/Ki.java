package ki;

import enums.KiStrength;
import game.Field;
import game.Position;
import game.cells.Block;
import game.cells.Cell;
import game.cells.Ship;
import game.cells.Shot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Ki implements Serializable {
    private Field enemyField; // this is the players playfield
    public Field kiEnemyField; // this is the vision of the ki of the enemy Field;
    private final KiStrength kiStrength;

    public Ki(Field enemyField, KiStrength kiStrength){
        this.enemyField = enemyField;
        this.kiStrength = kiStrength;
        this.kiEnemyField = new Field(enemyField.getHeight(), enemyField.getLength());
    }

    public int shoot() {
        //assert this.enemyField.getShipCount() > 0: "Keine Schiffe mehr da";

        switch (this.kiStrength) {
            case BEGINNER -> {
                return this.shootRandom();
            }
            case INTERMEDIATE -> {
                return this.shootRandomThenHit();
            }
            case STRONG -> {
                return this.shootRows();
            }
            case HELL -> {
                return this.perfectSpin();
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

    ArrayList<Ship> perfectShipArrayList;
    Ship perfectShip;
    boolean notSoPerfectShot = false;
    int perfectCount = 0;
    private int perfectSpin() {
        if (notSoPerfectShot) {
            notSoPerfectShot = false;
            return 0;
        }

        Random r = new Random();
        if (perfectShipArrayList == null) {
            perfectShipArrayList = this.enemyField.extractShips(this.enemyField.getHeight(), this.enemyField.getLength());
        }
        if (perfectShip == null) {
            perfectShip = perfectShipArrayList.remove(r.nextInt(perfectShipArrayList.size()));
        }
        Position[] positions = perfectShip.getPositions();
        int rc = this.enemyField.registerShot(positions[perfectCount]);
        perfectCount++;
        if (rc == 2) {
            perfectShip = null;
            notSoPerfectShot = true;
            perfectCount = 0;
        }

        return rc;
    }

    Position interLastHit;
    Character interDir = 'e';
    int interCounter = 1;
    private int shootRandomThenHit() {
        Random r = new Random();

        if (interLastHit != null) {
            if (interDir == 'e') {
                int nextX = interLastHit.getX() + interCounter;
                if (nextX >= this.enemyField.getLength() || this.enemyField.getCell(new Position(nextX, interLastHit.getY())) instanceof Shot) {
                    interDir = 'w';
                    interCounter = 1;
                    return shootRandomThenHit();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, interLastHit.getY()));
                    if (rc == 0) {
                        interDir = 'w';
                        interCounter = 1;
                    } else if (rc == 1) {
                        interCounter++;
                    } else {
                        interDir = 'e';
                        interCounter = 1;
                        interLastHit = null;
                    }
                    return rc;
                }
            }
            else if (interDir == 'w') {
                int nextX = interLastHit.getX() - interCounter;
                if (nextX < 0 || this.enemyField.getCell(new Position(nextX, interLastHit.getY())) instanceof Shot) {
                    interDir = 'n';
                    interCounter = 1;
                    return shootRandomThenHit();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, interLastHit.getY()));
                    if (rc == 0) {
                        interDir = 'n';
                        interCounter = 1;
                    } else if (rc == 1) {
                        interCounter++;
                    } else {
                        interDir = 'e';
                        interCounter = 1;
                        interLastHit = null;
                    }
                    return rc;
                }
            }
            else if (interDir == 'n') {
                int nextY = interLastHit.getY() - interCounter;
                if (nextY < 0 || this.enemyField.getCell(new Position(interLastHit.getX(), nextY)) instanceof Shot) {
                    interDir = 's';
                    interCounter = 1;
                    return shootRandomThenHit();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(interLastHit.getX(), nextY));
                    if (rc == 0) {
                        interDir = 's';
                        interCounter = 1;
                    } else if (rc == 1) {
                        interCounter++;
                    } else {
                        interDir = 'e';
                        interCounter = 1;
                        interLastHit = null;
                    }
                    return rc;
                }
            }
            else if (interDir == 's') {
                int nextY = interLastHit.getY() + interCounter;
                if (nextY >= this.enemyField.getHeight() || this.enemyField.getCell(new Position(interLastHit.getX(), nextY)) instanceof Shot) {
                    interDir = 'e';
                    interCounter = 1;
                    return shootRandomThenHit();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(interLastHit.getX(), nextY));
                    if (rc == 0) {
                        interDir = 'e';
                        interCounter = 1;
                    } else if (rc == 1) {
                        interCounter++;
                    } else {
                        interDir = 'e';
                        interCounter = 1;
                        interLastHit = null;
                    }
                    return rc;
                }
            }
        }

        while (true) {
            int y = r.nextInt(this.enemyField.getHeight());
            int x = r.nextInt(this.enemyField.getLength());
            Position nextShot = new Position(x, y);


            if (!(this.enemyField.getCell(nextShot) instanceof Shot)) {
                int rc = this.enemyField.registerShot(nextShot);
                if (rc == 1)
                    interLastHit = nextShot;
                return rc;
            }
        }
    }

    Position strongNextShot = new Position(0, 0);
    Position strongLastHit;
    Character strongDir = 'e';
    int strongCounter = 1;
    ArrayList<Position> strongHits = new ArrayList<>();
    public static int[] kiShipLengths;
    private int shootRows() {
        if (strongLastHit != null) {
            if (strongDir == 'e') {
                int nextX = strongLastHit.getX() + strongCounter;
                Position nextS = new Position(nextX, strongLastHit.getY());
                if ((nextX >= this.enemyField.getLength()) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 'w';
                    strongCounter = 1;
                    return shootRows();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, strongLastHit.getY()));
                    this.kiEnemyField.registerShot(new Position(nextX, strongLastHit.getY()));
                    if (rc == 0) {
                        strongDir = 'w';
                        strongCounter = 1;
                    } else if (rc == 1) {
                        strongHits.add(new Position(nextX, strongLastHit.getY()));
                        strongCounter++;
                    } else {
                        strongHits.add(new Position(nextX, strongLastHit.getY()));
                        this.kiEnemyField.addShip(new Ship(strongHits));
                        addShotsToKiEnemyField(strongHits);
                        strongHits.clear();

                        strongDir = 'e';
                        strongCounter = 1;
                        strongLastHit = null;
                    }
                    return rc;
                }
            }
            else if (strongDir == 'w') {
                int nextX = strongLastHit.getX() - strongCounter;
                Position nextS = new Position(nextX, strongLastHit.getY());
                if ((nextX < 0) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 'n';
                    strongCounter = 1;
                    return shootRows();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, strongLastHit.getY()));
                    this.kiEnemyField.registerShot(new Position(nextX, strongLastHit.getY()));
                    if (rc == 0) {
                        strongDir = 'n';
                        strongCounter = 1;
                    } else if (rc == 1) {
                        strongHits.add(new Position(nextX, strongLastHit.getY()));
                        strongCounter++;
                    } else {
                        strongHits.add(new Position(nextX, strongLastHit.getY()));
                        this.kiEnemyField.addShip(new Ship(strongHits));
                        addShotsToKiEnemyField(strongHits);
                        strongHits.clear();

                        strongDir = 'e';
                        strongCounter = 1;
                        strongLastHit = null;
                    }
                    return rc;
                }
            }
            else if (strongDir == 'n') {
                int nextY = strongLastHit.getY() - strongCounter;
                Position nextS = new Position(strongLastHit.getX(), nextY);
                if ((nextY < 0) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 's';
                    strongCounter = 1;
                    return shootRows();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(strongLastHit.getX(), nextY));
                    this.kiEnemyField.registerShot(new Position(strongLastHit.getX(), nextY));
                    if (rc == 0) {
                        strongDir = 's';
                        strongCounter = 1;
                    } else if (rc == 1) {
                        strongHits.add(new Position(strongLastHit.getX(), nextY));
                        strongCounter++;
                    } else {
                        strongHits.add(new Position(strongLastHit.getX(), nextY));
                        this.kiEnemyField.addShip(new Ship(strongHits));
                        addShotsToKiEnemyField(strongHits);
                        strongHits.clear();

                        strongDir = 'e';
                        strongCounter = 1;
                        strongLastHit = null;
                    }
                    return rc;
                }
            }
            else if (strongDir == 's') {
                int nextY = strongLastHit.getY() + strongCounter;
                Position nextS = new Position(strongLastHit.getX(), nextY);
                if ((nextY >= this.enemyField.getHeight()) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 'e';
                    strongCounter = 1;
                    return shootRows();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(strongLastHit.getX(), nextY));
                    this.kiEnemyField.registerShot(new Position(strongLastHit.getX(), nextY));
                    if (rc == 0) {
                        strongDir = 'e';
                        strongCounter = 1;
                    } else if (rc == 1) {
                        strongHits.add(new Position(strongLastHit.getX(), nextY));
                        strongCounter++;
                    } else {
                        strongHits.add(new Position(strongLastHit.getX(), nextY));
                        this.kiEnemyField.addShip(new Ship(strongHits));
                        addShotsToKiEnemyField(strongHits);
                        strongHits.clear();

                        strongDir = 'e';
                        strongCounter = 1;
                        strongLastHit = null;
                    }
                    return rc;
                }
            }
        }

        while (true) {
            Cell cell = this.kiEnemyField.getCell(strongNextShot);
            if (!(cell instanceof Shot) && (!(cell instanceof Block))) {
                int rc = this.enemyField.registerShot(strongNextShot);
                this.kiEnemyField.registerShot(strongNextShot);
                if (rc == 1) {
                    strongLastHit = strongNextShot;
                    strongHits.add(strongNextShot);
                }
                return rc;
            }
            else {
                int shortestRemainingShip = this.enemyField.getShipLengths()[this.enemyField.getShipLengths().length - 1];

                int nextX = strongNextShot.getX() + shortestRemainingShip;
                int nextY = strongNextShot.getY();
                if (nextX >= this.enemyField.getLength()) {
                    nextY +=1;
                    if (nextY % shortestRemainingShip == 0)
                        nextX = 0;
                    else if (nextY % shortestRemainingShip == 1)
                        nextX = 1;
                    else if (nextY % shortestRemainingShip == 2)
                        nextX = 2;
                    else if (nextY % shortestRemainingShip == 3)
                        nextX = 3;
                    else if (nextY % shortestRemainingShip == 4)
                        nextX = 4;
                }
                strongNextShot = new Position(nextX, nextY);
            }
        }
    }

    public void updateField(Boolean versenkt,Position shot){
        // TODO: 15.01.2021  
    }
    public static void makeShiplist4Ki(int[] shipLengths){
        kiShipLengths=shipLengths;

    }
    private void addShotsToKiEnemyField(ArrayList<Position> positions) {
        for (Position pos: positions)
            this.kiEnemyField.registerShot(pos);
    }
}
