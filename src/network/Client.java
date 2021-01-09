package network;

import java.io.*;
import java.net.Socket;

/**
 * Client implementiert den Client teil einer Socket Verbindung mit einem Server und den zugehörigen Funktionen,
 * wie Verbindungsaufbau, abbruch usw.
 *
 * Implementiert {@link Serializable} um einen Object Stream erzeugen zu koennen
 * Das {@link Socket} objekt und die Reader bzw. Writer werden nicht serialisiert, da dies nicht nötig ist, deshalb markierung mit
 * transient.
 */
public class Client implements Serializable {
    private String hostname;
    private int portnumber;
    private transient Socket socket;
    private transient PrintWriter out;
    private transient BufferedReader in;

    public void setHostname(String hostname) {
        assert !hostname.isEmpty() : "Hostname can't be set to empty String";
        this.hostname = hostname;
    }

    public void setPortnumber(int portnumber) {
        assert portnumber >= 0 : "Portnumber set negative";
        this.portnumber = portnumber;
    }

    public int getPortnumber() {
        return portnumber;
    }

    /**
     * Standardkonstruktur für Client  <br>
     * setzt hostname und portnumber auf vorgegebene Werte
     */
    public Client() {
        this.hostname = "localhost";
        this.portnumber = 55555;
    }

    /**
     * Konstruktor für benutzerdefinierten hostname und portnumber
     * @param hostname String des Hosts, also zu welcher Addrese sich verbunden werden soll. Kein leerer Wert zugelassen
     * @param portnumber int der portnummer, über die die Verbindung laufen soll. Keine negativen Werte zugelassen
     *
     * @see Client#setHostname(String)
     * @see Client#setPortnumber(int)
     */
    public Client(String hostname, int portnumber) {
        this.setHostname(hostname);
        this.setPortnumber(portnumber);
    }

    /**
     * Verbindungsaufbau und öffnen der Writer bzw. Reader auf die Socket
     * @return {@code true} wenn Verbindungs aufgebaut werden konnte, sonst {@code false}
     * @throws IOException Wenn die Verbindung abgelehnt wurde bzw. nicht aufgebaut werden konnte
     */
    public boolean openConnection() throws IOException {
        this.socket = new Socket(hostname, portnumber);
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        return this.socket.isConnected();
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
        if (this.socket != null) {
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            System.out.println("CLIENT read: " + line);
            return line;
        } catch (Exception e) {
            // TODO: 09.01.2021 Spiel beenden 
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Schreibt eine Zeile auf den OutputStream der Socket
     * @param out der zu schreibende String
     */
    public void writeLine(String out){
        System.out.println("CLIENT write: " + out);
        this.out.println(out);
    }
}
