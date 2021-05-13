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
    private boolean online = false;

    /**
     * Konstruktor der Ki für Lokale Spiele
     *
     * @param enemyField Feld das beschossen werden soll
     * @param kiStrength Stärke der Ki
     */
    public Ki(Field enemyField, KiStrength kiStrength) {
        this.enemyField = enemyField;
        this.kiStrength = kiStrength;
        this.kiEnemyField = new Field(enemyField.getHeight(), enemyField.getLength());
    }

    /**
     * Konstruktor der Ki für Online Spiele
     *
     * @param enemyField Feld das beschossen werden soll
     * @param kiStrength Stärke der Ki
     */
    public Ki(Field enemyField, KiStrength kiStrength, boolean online) {
        this.enemyField = enemyField;
        this.kiStrength = kiStrength;
        this.kiEnemyField = new Field(enemyField.getHeight(), enemyField.getLength());
        this.online = online;
    }

    /**
     * Allgemeine shoot Methode, spricht mittels switch case die zur Ki Stärke passende Methode an
     */
    public int shoot() {
        //assert this.enemyField.getShipCount() > 0: "Keine Schiffe mehr da";
        if (online) {
            switch (this.kiStrength) {
                case BEGINNER:
                    return this.shootRandom();
                case INTERMEDIATE:
                    return this.shootRandomThenHitOnline();
                case STRONG:
                    return this.shootRowsOnline();

            }
        }
        switch (this.kiStrength) {
            case BEGINNER:
                return this.shootRandom();
            case INTERMEDIATE:
                return this.shootRandomThenHit();
            case STRONG:
                return this.shootRows();
            case HELL:
                return this.perfectSpin();
        }
        return -1;
    }

    /**
     * gibt die Antworten des Gegners beim OnlineGame an die jeweiligen Methoden weiter
     * @param answer 0 für Schuss ins Wasser, 1 für getroffenes Schiff und 2 für versenktes Schiff.
     */
    public void giveAnswer(int answer) {
        if (online) {
            switch (this.kiStrength) {
                case BEGINNER:

                    break;
                case INTERMEDIATE:
                    setDirShootRandomThenHitOnline(answer);
                    break;
                case STRONG:
                    setDirhootRowsOnline(answer);
                    break;

            }
        }
    }

    /**
     * Schießt vollkommen zufällig aufs Feld
     */
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

    /**
     * Wird verwendet für Ki Strength hell in im LocalSpiel.
     * Diese Ki weiß wo die Schiffe des gegners sind. Bei jedem Zug versenkt es eins der Schiffe und gibt den Zug dann an den Spieler weiter.
     * @return 0 für Schuss ins Wasser, 1 für getroffenes Schiff und 2 für versenktes Schiff.
     */
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

    /**
     * Wird verwendet für Ki Strength intermediate in im LocalSpiel.
     * Schießt zufällig auf das Spielfeld, bis ein Schiff getroffen wird. Dann versenkt die Ki dieses Schiff und schießt wieder
     * zufällig auf des Feld.
     * @return  0 für Schuss ins Wasser, 1 für getroffenes Schiff und 2 für versenktes Schiff.
     */
    private int shootRandomThenHit() {
        Random r = new Random();

        if (interLastHit != null) {
            if (interDir == 'e') {
                int nextX = interLastHit.getX() + interCounter;
                if (nextX >= this.enemyField.getLength() || this.enemyField.getCell(new Position(nextX, interLastHit.getY())) instanceof Shot) {
                    interDir = 'w';
                    interCounter = 1;
                    return shootRandomThenHit();
                } else {
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
            } else if (interDir == 'w') {
                int nextX = interLastHit.getX() - interCounter;
                if (nextX < 0 || this.enemyField.getCell(new Position(nextX, interLastHit.getY())) instanceof Shot) {
                    interDir = 'n';
                    interCounter = 1;
                    return shootRandomThenHit();
                } else {
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
            } else if (interDir == 'n') {
                int nextY = interLastHit.getY() - interCounter;
                if (nextY < 0 || this.enemyField.getCell(new Position(interLastHit.getX(), nextY)) instanceof Shot) {
                    interDir = 's';
                    interCounter = 1;
                    return shootRandomThenHit();
                } else {
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
            } else if (interDir == 's') {
                int nextY = interLastHit.getY() + interCounter;
                if (nextY >= this.enemyField.getHeight() || this.enemyField.getCell(new Position(interLastHit.getX(), nextY)) instanceof Shot) {
                    interDir = 'e';
                    interCounter = 1;
                    return shootRandomThenHit();
                } else {
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

    /**
     *  Wird verwendet für Ki Strength intermediate in im OnlineSpiel.
     *  Funktioniert wie {@link #shootRandomThenHit()}
     *  Muss hier erst auf die Antwort des anderen Spielers warten, daher gibt return keinen sinnvollen Wert zurück und Parameter für den
     *  nächsten Schuss werden von der Methode {@link #setDirhootRowsOnline(int) } geändert.
     *  @return gibt im OnlineGame keinen sinnvollen Wert zurück.
     */
    private int shootRandomThenHitOnline() {
        Random r = new Random();

        if (interLastHitOnline != null) {
            if (interDirOnline == 'e') {
                int nextX = interLastHitOnline.getX() + interCounterOnline;
                if (nextX >= this.enemyField.getLength() || this.enemyField.getCell(new Position(nextX, interLastHitOnline.getY())) instanceof Shot) {
                    interDirOnline = 'w';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                } else {
                    int rc = this.enemyField.registerShot(new Position(nextX, interLastHitOnline.getY()));

                    return rc;
                }
            } else if (interDirOnline == 'w') {
                int nextX = interLastHitOnline.getX() - interCounterOnline;
                if (nextX < 0 || this.enemyField.getCell(new Position(nextX, interLastHitOnline.getY())) instanceof Shot) {
                    interDirOnline = 'n';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                } else {
                    int rc = this.enemyField.registerShot(new Position(nextX, interLastHitOnline.getY()));
                    return rc;
                }
            } else if (interDirOnline == 'n') {
                int nextY = interLastHitOnline.getY() - interCounterOnline;
                if (nextY < 0 || this.enemyField.getCell(new Position(interLastHitOnline.getX(), nextY)) instanceof Shot) {
                    interDirOnline = 's';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                } else {
                    int rc = this.enemyField.registerShot(new Position(interLastHitOnline.getX(), nextY));
                    return rc;
                }
            } else if (interDirOnline == 's') {
                int nextY = interLastHitOnline.getY() + interCounterOnline;
                if (nextY >= this.enemyField.getHeight() || this.enemyField.getCell(new Position(interLastHitOnline.getX(), nextY)) instanceof Shot) {
                    interDirOnline = 'e';
                    interCounterOnline = 1;
                    return shootRandomThenHitOnline();
                } else {
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

    /**
     * Wird verwendet für Ki Strength intermediate in im OnlineSpiel, in der Methode {@link #giveAnswer(int)}.
     * Durch die übergebene Antwort des getätigten Schusses, setzt diese Methode benötigte Parameter neu für den nächsten Schuss der Ki.
     * @param answer Antwort des Gegners, 0 für Schuss ins Wasser, 1 für getroffenes Schiff und 2 für versenktes Schiff.
     */
    private void setDirShootRandomThenHitOnline(int answer) {
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

        if (answer == 1) {
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

    /**
     * Wird verwendet für Ki Strength strong im LocalSpiel.
     * Schießt von oben rechts, Reihne für Reihe, im Abstand des kleinsten noch auf dem Feld befindenen Schiffes.
     * Sobald ein Schiff getroffen wird, wird dieses versenkt.
     * Felder werden nicth beschoffen, wenn diese Teil der geblockten Felder sind um ein versenktes Schiff oder bereits beschossen wurden.
     * @return 0 für Schuss ins Wasser, 1 für getroffenes Schiff und 2 für versenktes Schiff.
     */
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
    static ArrayList<Integer> shipslist = new ArrayList<>();
    int nextX, nextY;

    /**
     * Wird verwendet für Ki Strength strong im OnlineSpiel.
     * Funktioniert wie {@link #shootRows()}
     * Muss hier erst auf die Antwort des anderen Spielers warten, daher gibt return keinen sinnvollen Wert zurück und Parameter für den
     * nächsten Schuss werden von der Methode {@link #setDirhootRowsOnline(int) } geändert.
     * @return gibt im OnlineGame keinen sinnvollen Wert zurück.
     */
    private int shootRowsOnline() {
        if (strongLastHitOnline != null) {
            if (strongDirOnline == 'e') {
                nextX = 0;
                nextX = strongLastHitOnline.getX() + strongCounterOnline;
                Position nextS = new Position(nextX, strongLastHitOnline.getY());
                if ((nextX >= this.enemyField.getLength()) || this.enemyField.getCell(nextS) instanceof Shot || this.enemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 'w';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                } else {
                    int rc = this.enemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    this.kiEnemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    return rc;
                }
            } else if (strongDirOnline == 'w') {
                nextX = 0;
                nextX = strongLastHitOnline.getX() - strongCounterOnline;
                Position nextS = new Position(nextX, strongLastHitOnline.getY());
                if ((nextX < 0) || this.enemyField.getCell(nextS) instanceof Shot || this.enemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 'n';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                } else {
                    int rc = this.enemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    this.kiEnemyField.registerShot(new Position(nextX, strongLastHitOnline.getY()));
                    return rc;
                }
            } else if (strongDirOnline == 'n') {
                nextY = 0;
                nextY = strongLastHitOnline.getY() - strongCounterOnline;
                Position nextS = new Position(strongLastHitOnline.getX(), nextY);
                if ((nextY < 0) || this.enemyField.getCell(nextS) instanceof Shot || this.enemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 's';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                } else {
                    int rc = this.enemyField.registerShot(new Position(strongLastHitOnline.getX(), nextY));
                    this.kiEnemyField.registerShot(new Position(strongLastHitOnline.getX(), nextY));
                    return rc;
                }
            } else if (strongDirOnline == 's') {
                nextY = 0;
                nextY = strongLastHitOnline.getY() + strongCounterOnline;
                Position nextS = new Position(strongLastHitOnline.getX(), nextY);
                if ((nextY >= this.enemyField.getHeight()) || this.enemyField.getCell(nextS) instanceof Shot || this.enemyField.getCell(nextS) instanceof Block) {
                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    return shootRowsOnline();
                } else {
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

                int shortestRemainingShip = shipslist.get(shipslist.size() - 1);

                int nextX = strongNextShotOnline.getX() + shortestRemainingShip;
                int nextY = strongNextShotOnline.getY();
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

    /**
     * Wird verwendet für Ki Strength strong im OnlineSpiel, in der Methode {@link #giveAnswer(int)}.
     * Durch die übergebene Antwort des getätigten Schusses, setzt diese Methode benötigte Parameter neu für den nächsten Schuss der Ki.
     * @param answer Antwort des Gegners, 0 für Schuss ins Wasser, 1 für getroffenes Schiff und 2 für versenktes Schiff.
     */
    private void setDirhootRowsOnline(int answer) {
        enemyPlayfield = enemyField.getPlayfield();
        if (strongLastHitOnline != null) {
            if (strongDirOnline == 'e') {
                if (answer == 0) {
                    strongDirOnline = 'w';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    Position aktuellePos = new Position(nextX, strongLastHitOnline.getY());
                    strongCounterOnline++;
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                } else {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    Position aktuellePos = new Position(nextX, strongLastHitOnline.getY());
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                    updateField3(aktuellePos);

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            } else if (strongDirOnline == 'w') {
                if (answer == 0) {
                    strongDirOnline = 'n';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    Position aktuellePos = new Position(nextX, strongLastHitOnline.getY());
                    strongCounterOnline++;
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                } else {
                    strongHitsOnline.add(new Position(nextX, strongLastHitOnline.getY()));
                    Position aktuellePos = new Position(nextX, strongLastHitOnline.getY());
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                    updateField3(aktuellePos);

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            } else if (strongDirOnline == 'n') {
                if (answer == 0) {
                    strongDirOnline = 's';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    Position aktuellePos = new Position(strongLastHitOnline.getX(), nextY);
                    strongCounterOnline++;
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                } else {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    Position aktuellePos = new Position(strongLastHitOnline.getX(), nextY);
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                    updateField3(aktuellePos);

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            } else if (strongDirOnline == 's') {
                if (answer == 0) {
                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                } else if (answer == 1) {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    Position aktuellePos = new Position(strongLastHitOnline.getX(), nextY);
                    strongCounterOnline++;
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                } else {
                    strongHitsOnline.add(new Position(strongLastHitOnline.getX(), nextY));
                    Position aktuellePos = new Position(strongLastHitOnline.getX(), nextY);
                    this.kiEnemyField.addShip(new Ship(strongHitsOnline));
                    addShotsToKiEnemyField(strongHitsOnline);
                    strongHitsOnline.clear();
                    Shot s = (Shot) enemyPlayfield[aktuellePos.getY()][aktuellePos.getX()];
                    s.setWasShip(true);
                    updateField3(aktuellePos);

                    strongDirOnline = 'e';
                    strongCounterOnline = 1;
                    strongLastHitOnline = null;
                }
            }
        }
        if (answer == 1) {
            strongLastHitOnline = strongNextShotOnline;
            strongHitsOnline.add(strongNextShotOnline);
            Shot s = (Shot) enemyPlayfield[strongLastHitOnline.getY()][strongLastHitOnline.getX()]; // TODO: 24.01.2021 Frage
            s.setWasShip(true);

        }
    }


    /**
     * Gibt zurück ob das übergebene Feld ein beschossenes Schiff ist
     *
     * @param pos Position die zu überprüfen ist
     * @return True->Pos is a Shot Ship ; false->Position ist kein beschossenes Schiff
     */
    private boolean isShotShipDirection(Position pos) {
        if (enemyField.getCell(pos) instanceof Shot) {
            Shot s = ((Shot) enemyField.getCell(pos));
            if(s.getWasShip()){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    int k=0;
    /**
     *  Wird verwendet für Ki Strength strong im OnlineSpiel, also in der Methode {@link #setDirhootRowsOnline(int)}.
     *  Die Methode ermittelt die Position und die Größe des Schiffes, das versenkt wurde aus der letzten getroffenen Position.
     *  mit der Methode {@link Field#blockFields(Ship)} werden die umliegenden Felder geblockt.
     * @param pos der letzte Schuss,der zum versenken des Schiffes geführt hat.
     */
    public void updateField3(Position pos) {
        String shipdir = "";
        int shiplen = 0;
        Position nextShip;
        ArrayList<Position> Shippositions = new ArrayList<>();
        int verschiebung = 1;
        // richtung des Schiffes Abchecken:
        enemyPlayfield = enemyField.getPlayfield();

        //wenn nach links
        if ((pos.getX() - 1) >= 0 && (isShotShipDirection(new Position(pos.getX() - 1, pos.getY())))) {
            shipdir = "left";
        } else if ((pos.getX() + 1) < this.enemyField.getLength() && (isShotShipDirection(new Position(pos.getX() + 1, pos.getY())))) {
            shipdir = "right";
        } else if ((pos.getY() - 1) >= 0 && (isShotShipDirection(new Position(pos.getX(), pos.getY() - 1)))) {
            shipdir = "up";
        } else if ((pos.getY() + 1) < this.enemyField.getLength() && (isShotShipDirection(new Position(pos.getX(), pos.getY() + 1)))) {
            shipdir = "down";
        }


        switch (shipdir) {
            case "right":
                nextShip = pos;
                while (nextShip != null && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX() + verschiebung, pos.getY());
                    verschiebung++;
                }
                break;

            case "left":
                nextShip = pos;
                while (nextShip != null && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX() - verschiebung, pos.getY());
                    verschiebung++;
                }
                break;
            case "up":
                nextShip = pos;
                while (nextShip != null && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX(), pos.getY() - verschiebung);
                    verschiebung++;
                }
                break;
            case "down":
                nextShip = pos;
                while (nextShip != null && enemyField.getCell(nextShip) instanceof Shot && ((Shot) enemyField.getCell(nextShip)).getWasShip()) {
                    shiplen++;
                    Shippositions.add(nextShip);
                    nextShip = new Position(pos.getX(), pos.getY() + verschiebung);
                    verschiebung++;
                }
                break;
            default:
                System.out.println("Fehler bei der Schiffsrichtungsbestimmung in Ki");
        }
        Ship versenktesShip = new Ship(true, Shippositions);
        enemyField.blockFields(versenktesShip);


        // entfernt versunkendes ship aus der Liste
        int tmp=shipslist.size();
        System.out.println("////");
        System.out.println("Zu entfernen:"+shiplen+"\nn= "+tmp+" Aktuelle Liste: ");
        for(int i:shipslist) System.out.print(i+"   ");
        System.out.println();
        for (int i = 0; i < shipslist.size(); i++) {
            if (shipslist.get(i) == shiplen) {
                System.out.println("wurde entfernt:"+shipslist.get(i));
                shipslist.remove(i);
                break;
            }

        }
        for (int i:shipslist) System.out.print(i+"   ");
        if(tmp-1!=shipslist.size()) System.out.println("ALAAAARM tmp="+tmp+" ist:"+shipslist.size());
        System.out.println();
        k++;
        System.out.println("Schiffe zerstört: "+k);
        System.out.println("////");
    }





    /**
     * Wird verwendet für Ki Strength strong im OnlineSpiel, also in der Methode {@link #shootRowsOnline()}.
     * Die Methode stellt eine ArrayList bereit mit allen Schiffen, die auf das Spielfeld gesetzt werden müssen.
     * @param shipLengths ein Array mit allen Schiffen des gespielten Spieles.
     */
    public static void makeShiplist4Ki(int[] shipLengths){
        kiShipLengths=shipLengths;
        shipslist.clear();
        for(int i =0; i < kiShipLengths.length;i++){
            shipslist.add(kiShipLengths[i]);
        }
    }

    /**
     * Wird verwendet für Ki Strength strong im LokalGame, also in der Methode {@link #shootRows()}.
     * Realisiert die Schüsse der Ki auf einen internen Feld des Gegners.
     * @param positions Position des Schusses
     */
    private void addShotsToKiEnemyField(ArrayList<Position> positions) {
        for (Position pos: positions)
            this.kiEnemyField.registerShot(pos);
    }
}

