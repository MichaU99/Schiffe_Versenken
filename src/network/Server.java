package network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server implementiert den Server teil einer Socket Verbindung mit einem Client und den zugehörigen Funktionen,
 * wie Verbindungsaufbau, abbruch usw.
 *
 * Implementiert {@link Serializable} um einen Object Stream erzeugen zu koennen
 * Das {@link Socket} objekt und die Reader bzw. Writer werden nicht serialisiert, da dies nicht nötig ist, deshalb markierung mit
 * transient.
 */
public class Server implements Serializable {
    private int portNumber;
    private transient ServerSocket serverSocket;
    private transient Socket clientSocket;
    private transient PrintWriter out;
    private transient BufferedReader in;

    public void setPortNumber(int portNumber) {
        assert portNumber >= 0 : "Portnumber set negative";
        this.portNumber = portNumber;
    }

    /**
     * Standardkonstruktur für Server  <br>
     * setzt portnumber auf der gehört werden soll auf vorgegebene Werte
     */
    public Server() {
        this.portNumber = 5555;
    }

    /**
     * Konstruktor für benutzerdefinierte portnumber
     * @param portNumber int der portnummer, über die die Verbindung laufen soll. Keine negativen Werte zugelassen
     *
     * @see Server#setPortNumber(int)
     */
    public Server(int portNumber) {
        this.setPortNumber(portNumber);
    }

    /**
     * Wartet bis sich ein Client auf den Port des Servers verbindet.
     * Öffnet die zugehörige clientSocket und reader und writer auf diese
     * @return {@code true} wenn Verbindungs aufgebaut werden konnte, sonst {@code false}
     */
    public boolean waitForConnection() {
        // THIS METHOD WILL BLOCK
        try {
            this.serverSocket = new ServerSocket(this.portNumber);
            this.clientSocket = this.serverSocket.accept();
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return this.clientSocket.isConnected();
    }

    /**
     * Schreibt eine Zeile auf den OutputStream der Socket
     * @param out der zu schreibende String
     */
    public void writeLine(String out) {
        System.out.println("SERVER write: " + out);
        this.out.println(out);
    }

    /**
     * Ließt eine Zeile aus dem InputStream der Socket.
     * Zeile ist bin \n
     * @return die gelesene zeile, null wenn nichts gelesen werden konnte bzw. die Verbindungs von der anderen Seite
     * geschlossen wurde
     */
    public String readLine() {
        try {
            String line = this.in.readLine();
            System.out.println("SERVER read: " + line);
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Beendet die Verbindungs und schließt die Reader und Writer auf die Socket
     */
    public void closeConnection() {
        if (this.in != null) {
            try {
                this.in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (this.out != null)
            this.out.close();
        if (this.serverSocket != null) {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
