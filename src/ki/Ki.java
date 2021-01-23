package ki;

import JavaFx.Controller_GameScreen;
import enums.KiStrength;
import game.Field;
import game.OnlineHostGame;
import game.Position;
import game.cells.Block;
import game.cells.Cell;
import game.cells.Ship;
import game.cells.Shot;
import javafx.geometry.Pos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;



public class Ki implements Serializable {
    private Field enemyField; // this is the players playfield
    public Field kiEnemyField; // this is the vision of the ki of the enemy Field;
    private final KiStrength kiStrength;
    private boolean online=false;

    public Ki(Field enemyField, KiStrength kiStrength){
        this.enemyField = enemyField;
        this.kiStrength = kiStrength;
        this.kiEnemyField = new Field(enemyField.getHeight(), enemyField.getLength());
    }
    public Ki(Field enemyField, KiStrength kiStrength,boolean online){
        this.enemyField = enemyField;
        this.kiStrength = kiStrength;
        this.kiEnemyField = new Field(enemyField.getHeight(), enemyField.getLength());
        this.online=online;
    }

    public int shoot() {
        //assert this.enemyField.getShipCount() > 0: "Keine Schiffe mehr da";

        if(online){
            switch (this.kiStrength) {
                case BEGINNER -> {
                    return this.shootRandom();
                }
                case INTERMEDIATE -> {
                    return this.shootRandomThenHitOnline();
                }
                case STRONG -> {
                    return this.shootRowsOnline();
                }

            }
        }
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

    public void giveAnswer (int answer){
        if(online){
            switch (this.kiStrength) {
                case BEGINNER -> {

                }
                case INTERMEDIATE -> {
                    setDirShootRandomThenHitOnline(answer);
                }
                case STRONG -> {
                    setDirhootRowsOnline(answer);
                }

            }
        }
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

    Position interLastHitOnline;
    Character interDirOnline = 'e';
    int interCounterOnline = 1;
    Position interShootPos;
    private int shootRandomThenHitOnline(){
        Random r = new Random();

        if (interLastHitOnline != null) {
            if (interDirOnline == 'e') {
                int nextX = interLastHitOnline.getX() + interCounterOnline;
                if (nextX >= this.enemyField.getLength() || this.enemyField.getCell(new Position(nextX, interLastHit.getY())) instanceof Shot) {
                    interDirOnline = 'w';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, interLastHitOnline.getY()));

                    return rc;
                }
            }
            else if (interDirOnline == 'w') {
                int nextX = interLastHitOnline.getX() - interCounterOnline;
                if (nextX < 0 || this.enemyField.getCell(new Position(nextX, interLastHitOnline.getY())) instanceof Shot) {
                    interDir = 'n';
                    interCounter = 1;
                    return shootRandomThenHitOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, interLastHitOnline.getY()));
                    return rc;
                }
            }
            else if (interDirOnline == 'n') {
                int nextY = interLastHitOnline.getY() - interCounterOnline;
                if (nextY < 0 || this.enemyField.getCell(new Position(interLastHitOnline.getX(), nextY)) instanceof Shot) {
                    interDirOnline = 's';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(interLastHitOnline.getX(), nextY));
                    return rc;
                }
            }
            else if (interDirOnline == 's') {
                int nextY = interLastHitOnline.getY() + interCounterOnline;
                if (nextY >= this.enemyField.getHeight() || this.enemyField.getCell(new Position(interLastHitOnline.getX(), nextY)) instanceof Shot) {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(interLastHitOnline.getX(), nextY));
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
                interShootPos = nextShot;
                return rc;
            }
        }
    }
    private void setDirShootRandomThenHitOnline(int answer){
        if (interLastHitOnline != null) {
            if (interDirOnline == 'e') {
                if (answer == 0) {
                    interDirOnline = 'w';
                    interCounterOnline = 1;
                } else if (answer == 1) {
                    interCounterOnline++;
                } else {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                    interLastHitOnline = null;
                }
            } else if (interDirOnline == 'w') {
                if (answer == 0) {
                    interDirOnline = 'n';
                    interCounterOnline = 1;
                } else if (answer == 1) {
                    interCounterOnline++;
                } else {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                    interLastHitOnline = null;
                }
            } else if (interDirOnline == 'n') {
                if (answer == 0) {
                    interDirOnline = 's';
                    interCounterOnline = 1;
                } else if (answer == 1) {
                    interCounterOnline++;
                } else {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                    interLastHitOnline = null;
                }
            } else if (interDirOnline == 's') {
                if (answer == 0) {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                } else if (answer == 1) {
                    interCounterOnline++;
                } else {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                    interLastHitOnline = null;
                }
            }
        }

        if (answer == 1){
            interLastHitOnline = interShootPos;
        }
    }



    Position strongNextShot = new Position(0, 0);
    Position strongLastHit;
    Character strongDir = 'e';
    int strongCounter = 1;
    ArrayList<Position> strongHits = new ArrayList<>();
    public static int[] kiShipLengths;
    private Cell[][] enemyPlayfield;
    private int shootRows() {
        if (strongLastHit != null) {
            if (strongDir == 'e') {
                int nextX = strongLastHit.getX() + strongCounter;
                Position nextS = new Position(nextX, strongLastHit.getY());
                if ((nextX >= this.enemyField.getLength()) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 'w';
                    strongCounter = 1;
                    return shootRows();
                } else {
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
            } else if (strongDir == 'w') {
                int nextX = strongLastHit.getX() - strongCounter;
                Position nextS = new Position(nextX, strongLastHit.getY());
                if ((nextX < 0) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 'n';
                    strongCounter = 1;
                    return shootRows();
                } else {
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
            } else if (strongDir == 'n') {
                int nextY = strongLastHit.getY() - strongCounter;
                Position nextS = new Position(strongLastHit.getX(), nextY);
                if ((nextY < 0) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 's';
                    strongCounter = 1;
                    return shootRows();
                } else {
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
            } else if (strongDir == 's') {
                int nextY = strongLastHit.getY() + strongCounter;
                Position nextS = new Position(strongLastHit.getX(), nextY);
                if ((nextY >= this.enemyField.getHeight()) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDir = 'e';
                    strongCounter = 1;
                    return shootRows();
                } else {
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
                } else {
                    int shortestRemainingShip = this.enemyField.getShipLengths()[this.enemyField.getShipLengths().length - 1];

                    int nextX = strongNextShot.getX() + shortestRemainingShip;
                    int nextY = strongNextShot.getY();
                    if (nextX >= this.enemyField.getLength()) {
                        nextY += 1;
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

    Position strongNextShotOnline = new Position(0, 0);
    Position strongLastHitOnline;
    Character strongDirOnline = 'e';
    int strongCounterOnline = 1;
    ArrayList<Position> strongHitsOnline = new ArrayList<>();
    int nextX,nextY;
    private int shootRowsOnline(){
        if (strongLastHitOnline != null) {
            if (strongDirOnline == 'e') {
                nextX = strongLastHitOnline.getX() + strongCounterOnline;
                Position nextS = new Position(nextX, strongLastHitOnline.getY());
                if ((nextX >= this.enemyField.getLength()) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 'w';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    this.kiEnemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    return rc;
                }
            }
            else if (strongDirOnline == 'w') {
                nextX = strongLastHitOnline.getX() - strongCounterOnline;
                Position nextS = new Position(nextX, strongLastHit.getY());
                if ((nextX < 0) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 'n';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    this.kiEnemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    return rc;
                }
            }
            else if (strongDirOnline == 'n') {
                nextY = strongLastHitOnline.getY() - strongCounterOnline;
                Position nextS = new Position(strongLastHitOnline.getX(), nextY);
                if ((nextY < 0) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 's';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(strongLastHitOnline.getX(), nextY));
                    this.kiEnemyField.registerShot(new Position(strongLastHitOnline.getX(), nextY));
                    return rc;
                }
            }
            else if (strongDirOnline == 's') {
                nextY = strongLastHitOnline.getY() + strongCounterOnline;
                Position nextS = new Position(strongLastHitOnline.getX(), nextY);
                if ((nextY >= this.enemyField.getHeight()) || this.kiEnemyField.getCell(nextS) instanceof Shot || this.kiEnemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                }
                else {
                    int rc = this.enemyField.registerShot(new Position(strongLastHitOnline.getX(), nextY));
                    this.kiEnemyField.registerShot(new Position(strongLastHitOnline.getX(), nextY));
                    return rc;
                }
            }
        }
            while (true) {
                Cell cell = this.enemyField.getCell(strongNextShotOnline);
                if (!(cell instanceof Shot) && (!(cell instanceof Block))) {
                    int rc = this.enemyField.registerShot(strongNextShotOnline);
                    this.kiEnemyField.registerShot(strongNextShotOnline);
                    return rc;
                } else {
                    int shortestRemainingShip = kiShipLengths[kiShipLengths.length - 1]; // TODO: 20.01.2021 prüfen ob richtig das sammelz

                    int nextX = strongNextShot.getX() + shortestRemainingShip;
                    int nextY = strongNextShot.getY();
                    if (nextX >= this.enemyField.getLength()) {
                        nextY += 1;
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
                    strongNextShotOnline = new Position(nextX, nextY);
                }
            }
    }

    private void setDirhootRowsOnline(int answer){
        if (strongLastHitOnline != null) {
            if (strongDirOnline == 'e') {
                if (answer == 0) {
                    strongDirOnline = 'w';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    strongCounterOnline++;
                } else {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    this.kiEnemyField.addShip(new Ship(strongHits));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            }
            else if (strongDirOnline == 'w') {
                if (answer == 0) {
                    strongDirOnline = 'n';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    strongCounterOnline++;
                } else {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            }
            else if (strongDirOnline == 'n') {
                if (answer == 0) {
                    strongDirOnline = 's';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    strongCounterOnline++;
                } else {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            }
            else if (strongDirOnline == 's') {
                if (answer == 0) {
                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    strongCounterOnline++;
                } else {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            }
        }
        if(answer ==1){
            strongLastHitOnline = strongNextShotOnline;
            strongHitsOnline.add(strongNextShotOnline);
        }
    }

    /**
     * Wird im Multiplayerspiel nach Schüssen aufgerufen um die interne Datenstruktur des Gegnerischen Felds
     * aktuell zu halten
     * @param versenkt true falls der Schuss ein Schiff versenkt hat
     * @param shot Position an die geschossen wurde
     */
    /*
    public void updateField(Position shot) {
        //int count=1;
        count=1;
        shipfinish=false;
        int x= shot.getX();
        int y=shot.getY();
           checkLRUD(shot);
           if (updateDir=='w'){
               int i=1;
               do{
                 Position neuePosition= new Position(x-i,y);
                 checkLRUD(neuePosition);
                 i++;
               }while(shipfinish==false);
           }
            if (updateDir=='e'){
                int i=1;
                do{
                    Position neuePosition= new Position(x+i,y);
                    checkLRUD(neuePosition);
                    i++;
                }while(shipfinish==false);
            }
            if (updateDir=='n'){
                int i=1;
                do{
                    Position neuePosition= new Position(x,y-i);
                    checkLRUD(neuePosition);
                    i++;
                }while(shipfinish==false);
            }
            if (updateDir=='s'){
                int i=1;
                do{
                    Position neuePosition= new Position(x,y+i);
                    checkLRUD(neuePosition);
                    i++;
                }while(shipfinish==false);
            }
        if(shipfinish==true) {
            for (int i = 0; i < kiShipLengths.length; i++) {
                if (kiShipLengths[i] == count) {
                    for (int j = i; j < kiShipLengths.length - 1; j++) {
                        kiShipLengths[i] = kiShipLengths[i + 1];
                    }
                    break;
                }
            }
        }
    }
    private void checkLRUD(Position shot) {
        int x = shot.getX();
        int y = shot.getY();
        //links
        Position poLinks = new Position(x - 1, y);
        Cell cell = this.enemyField.getCell(poLinks);
        if (cell instanceof Shot && ((Shot) cell).getWasShip()) {
            updateDir = 'w';
            count++;
        } else {
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY()][shot.getX() - 1] = new Block();
        }
        //rechts
        Position poRechts = new Position(x + 1, y);
        Cell cell1 = this.enemyField.getCell(poRechts);
        if (cell1 instanceof Shot && ((Shot) cell1).getWasShip()) {
            updateDir = 'e';
            count++;
        } else {
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY()][shot.getX() + 1] = new Block();
        }
        //oben
        Position poOben = new Position(x, y - 1);
        Cell cell2 = this.enemyField.getCell(poOben);
        if (cell2 instanceof Shot && ((Shot) cell2).getWasShip()) {
            updateDir = 'n';
            count++;
        } else {
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() - 1][shot.getX()] = new Block();
        }
        //unten
        Position poUnten = new Position(x, y + 1);
        Cell cell3 = this.enemyField.getCell(poUnten);
        if (cell3 instanceof Shot && ((Shot) cell3).getWasShip()) {
            updateDir = 's';
            count++;
        } else {
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() + 1][shot.getX()] = new Block();
        }

        if (updateDir == 'w') {
            //diagonal oben rechts
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() - 1][shot.getX() + 1] = new Block();
            //diagonal unten rechts
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() + 1][shot.getX() + 1] = new Block();
            //prüfe ob nächstes Feld Wasser
            Position pruefPos = new Position(x - 1, y);
            Cell pruefCell = this.enemyField.getCell(pruefPos);
            if (!pruefCell.toString().equals('l')) {
                shipfinish = true;
                //diagonal unten links
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() + 1][shot.getX() - 1] = new Block();
                //diagonal oben links
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() - 1][shot.getX() - 1] = new Block();
            }
        }

        if (updateDir == 'e') {
            //diagonal oben links
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() - 1][shot.getX() - 1] = new Block();
            //diagonal unten links
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() + 1][shot.getX() - 1] = new Block();
            //prüfe ob nächstes Feld Wasser
            Position pruefPos1 = new Position(x + 1, y);
            Cell pruefCell1 = this.enemyField.getCell(pruefPos1);
            if (!pruefCell1.toString().equals('l')) {
                shipfinish = true;
                //diagonal unten rechts
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() + 1][shot.getX() + 1] = new Block();
                //diagonal oben rechts
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() - 1][shot.getX() + 1] = new Block();
            }
        }
        if (updateDir == 'n') {
            //diagonal unten rechts
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() + 1][shot.getX() + 1] = new Block();
            //diagonal unten links
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() + 1][shot.getX() - 1] = new Block();
            //prüfe ob nächstes Feld Wasser
            Position pruefPos2 = new Position(x, y - 1);
            Cell pruefCell2 = this.enemyField.getCell(pruefPos2);
            if (!pruefCell2.toString().equals('l')) {
                shipfinish = true;
                //diagonal oben links
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() - 1][shot.getX() - 1] = new Block();
                //diagonal oben rechts
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() - 1][shot.getX() + 1] = new Block();
            }
        }
        if (updateDir == 's') {
            //diagonal oben links
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() - 1][shot.getX() - 1] = new Block();
            //diagonal oben rechts
            enemyPlayfield = enemyField.getPlayfield();
            this.enemyPlayfield[shot.getY() - 1][shot.getX() + 1] = new Block();
            //prüfe ob nächstes Feld Wasser
            Position pruefPos3 = new Position(x, y + 1);
            Cell pruefCell3 = this.enemyField.getCell(pruefPos3);
            if (!pruefCell3.toString().equals('l')) {
                shipfinish = true;
                //diagonal unten rechts
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() + 1][shot.getX() + 1] = new Block();
                //diagonal unten links
                enemyPlayfield = enemyField.getPlayfield();
                this.enemyPlayfield[shot.getY() + 1][shot.getX() - 1] = new Block();
            }
        }
    }
     */
    /*
    ///////////////
    //Michaels Methoden
    ///////////////

    public void updateField2(Position pos){
        String shipdir="";
        int shiplen=0;
        Position nextShip;

        //Findet die Richtung heraus in die das Schiff läuft
        if(pos.getX()+1<enemyField.getLength() && (isShotShipDirection(new Position(pos.getX()+1,pos.getY())))) shipdir="right";
        else if( && ) shipdir="left";
        else if( && ) shipdir="up";
        else if( &&) shipdir="down";

        switch (shipdir){
            case "right":
                if(pos.getX()-1>=0) enemyField.getPlayfield()[pos.getX()-1][pos.getY()]= new Block();
                nextShip=pos;
                while (nextShip!=null && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()){
                    shiplen++;
                    blockCellsWaagerecht(nextShip);
                    nextShip=new Position(pos.getX()+1, pos.getY());
                }
                enemyField.getPlayfield()[nextShip.getX()][nextShip.getY()]=new Block();
                break;

            case "left":
                break;
            case "up":
                break;
            case "down":
                break;
            default:
                System.out.println("Fehler bei der Schiffsrichtungsbestimmung in Ki");
        }
        //Benutze shiplen hier sonst verschwindet der Wert hier
    }

    private void blockCellsWaagerecht(Position pos){
        Position upCell=null,downCell=null;
        if(checkvalidPos)upCell=new Position(pos.getX(), pos.getY()+1);
        if(checkValidPos)downCell=new Position(pos.getX(),pos.getY()-1);
        if(upCell!=null){
            enemyField.getPlayfield()[upCell.getX()][upCell.getY()]=new Block();
        }
        if(downCell!=null){

        }
        this.enemyPlayfield[shot.getY() + 1][shot.getX()] = new Block();
    }
    private void blockCellSenkrecht(Position pos){

    }
    */
    /**
     * Gibt zurück ob das übergebene Feld ein beschossenes Schiff ist
     * @param pos Position die zu überprüfen ist
     * @return True->Pos is a Shot Ship ; false->Position ist kein beschossenes Schiff
     */
    private boolean isShotShipDirection(Position pos) {
        if (enemyField.getCell(pos) instanceof Shot) {
            Shot s = ((Shot) enemyField.getCell(pos));
            return s.getWasShip();
        }
        return false;
    }
    //////////////
    //Ende Michaels Methoden
    /////////////

    //////// Weitere Methode mit
    ///////benutzung der anderen bereitsts geschriebenen Sachen
    public void updateField3(Position pos) {
        String shipdir = "";
        int shiplen = 0;
        Position nextShip;
        ArrayList<Position> Shippositions= new ArrayList<>();
        int verschiebung = 1;
        // richtung des Schiffes Abchecken:
        enemyPlayfield = enemyField.getPlayfield();
        //wenn nach links
        if ((enemyPlayfield[pos.getY()][pos.getX() - 1].getClass() == Cell.class) && (isShotShipDirection(new Position(pos.getX() - 1, pos.getY())))) {
            shipdir = "left";
        } else if ((enemyPlayfield[pos.getY()][pos.getX() + 1].getClass() == Cell.class) && (isShotShipDirection(new Position(pos.getX() + 1, pos.getY()))))
            shipdir = "right";
        else if ((enemyPlayfield[pos.getY() - 1][pos.getX()].getClass() == Cell.class) && (isShotShipDirection(new Position(pos.getX(), pos.getY() - 1))))
            shipdir = "up";
        else if ((enemyPlayfield[pos.getY() + 1][pos.getX()].getClass() == Cell.class) && (isShotShipDirection(new Position(pos.getX(), pos.getY() + 1))))
            shipdir = "down";

        switch (shipdir) {
            case "right":
                nextShip = pos;
                while (nextShip != null && (enemyPlayfield[nextShip.getY()][nextShip.getX()].getClass() == Cell.class) && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX() + verschiebung, pos.getY());
                    verschiebung++;
                }
                break;

            case "left":
                nextShip = pos;
                while (nextShip != null && (enemyPlayfield[nextShip.getY()][nextShip.getX()].getClass() == Cell.class) && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX() - verschiebung, pos.getY());
                    verschiebung++;
                }
                break;
            case "up":
                nextShip = pos;
                while (nextShip != null && (enemyPlayfield[nextShip.getY()][nextShip.getX()].getClass() == Cell.class) && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX(), pos.getY() - verschiebung);
                    verschiebung++;
                }
                break;
            case "down":
                nextShip = pos;
                while (nextShip != null && (enemyPlayfield[nextShip.getY()][nextShip.getX()].getClass() == Cell.class) && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX(), pos.getY() + verschiebung);
                    verschiebung++;
                }
                break;
            default:
                System.out.println("Fehler bei der Schiffsrichtungsbestimmung in Ki");
        }
        Ship versenktesShip= new Ship(true,Shippositions);
        enemyField.blockFields(versenktesShip);
         // entfernt versunkendes ship aus der Liste
            for (int i = 0; i < kiShipLengths.length; i++) {
                if (kiShipLengths[i] == shiplen) {
                    for (int j = i; j < kiShipLengths.length - 1; j++) {
                        kiShipLengths[i] = kiShipLengths[i + 1];
                    }
                    break;
                }
            }

    }
    //////// ENDE: Weitere Methode mit
    ///////benutzung der anderen bereitsts geschriebenen Sachen


    public static void makeShiplist4Ki(int[] shipLengths){
        kiShipLengths=shipLengths;


    }
    private void addShotsToKiEnemyField(ArrayList<Position> positions) {
        for (Position pos: positions)
            this.kiEnemyField.registerShot(pos);
    }
}

