package game;

import enums.ProtComs;
import game.cells.Shot;
import network.BattleshipProtocol;
import network.Server;

import java.io.*;

public class OnlineHostGame extends Game{
    private Server server;
    private int[] shipLengths; // the user has to specify the amount of each ship he wants to place before the game

    public OnlineHostGame(int playFieldHeight, int playFieldLength, int portNumber, int[] shipLengths) {
        super(playFieldHeight, playFieldLength);
        this.server = new Server();
        this.server.setPortNumber(portNumber);
        this.shipLengths = shipLengths;
    }

    public boolean waitForConnection() {
        // wait for connection, when connection is established exchange game Configuration
        if (!this.server.waitForConnection())
            return false;
//        System.out.println("Connected");

        this.server.writeLine(BattleshipProtocol.formatSize(this.getField().getLength(), this.getField().getHeight()));
//        System.out.println("SIZE CONFIG SENT");
        if (!this.server.readLine().equals("done"))
            return false;
//        System.out.println("size config Ok");

        this.server.writeLine(BattleshipProtocol.formatShips(this.shipLengths));
        if (!this.server.readLine().equals("done"))
            return false;

        return true;
    }

    public boolean startGame() {
        // sende nach Protokoll ready -> spiel kann gestartet werden
        this.server.writeLine("ready");
        return this.server.readLine().equals("ready");
    }

    public void shoot(Position position) {
        // shoot Funktion mit extra steps im Prinzip.
        // es muss eben noch über die Sockets die Kommunikation übertragen werden
        this.server.writeLine(BattleshipProtocol.formatShot(position.getX(), position.getY()));
        Object[] answer = BattleshipProtocol.processInput(this.server.readLine());
        if (answer[0] != ProtComs.ANSWER){
            return;
        }

        if ((int) answer[1] >= 1) {
            // User can shoot again
            this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot(true);
        }
        else {
            // User missed -> enemy can shoot
            this.enemyField.getPlayfield()[position.getY()][position.getX()] = new Shot();
            this.server.writeLine("next");

            while (true) {
                String line = this.server.readLine();
                Object[] answer2 = BattleshipProtocol.processInput(line);
                if (line.equals("next")) {
                    break;
                }
                if (answer2[0] == ProtComs.SAVE) {
                    try {
                        super.saveGame((String) answer2[1]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                if (answer2[0] != ProtComs.SHOT) {
                    break;
                }
                Position enemyShot = (Position) answer2[1];
                this.server.writeLine(BattleshipProtocol.formatAnswer(this.field.registerShot(enemyShot)));
            }
        }
    }

    @Override
    public void saveGame(String id) throws IOException {
        super.saveGame(id);
        this.server.writeLine(BattleshipProtocol.formatSave(id));
    }

    @Override
    public void loadGame(String id) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(id + ".txt"));
        OnlineHostGame temp = (OnlineHostGame) in.readObject();
        this.field = temp.field;
        this.enemyField = temp.enemyField;
        this.server = temp.server;
    }
}
