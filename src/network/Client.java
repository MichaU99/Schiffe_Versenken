package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String hostname;
    private int portnumber;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public void setHostname(String hostname) {
        assert !hostname.isEmpty() : "Hostname can't be set to empty String";
        this.hostname = hostname;
    }

    public void setPortnumber(int portnumber) {
        assert portnumber >= 0 : "Portnumber set negative";
        this.portnumber = portnumber;
    }

    public Client() {
        this.hostname = "localhost";
        this.portnumber = 55555;
    }

    public Client(String hostname, int portnumber) {
        this.setHostname(hostname);
        this.setPortnumber(portnumber);
    }

    public boolean openConnection() {
        try {
            this.socket = new Socket(hostname, portnumber);
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.socket.isConnected();
    }

    public void closeConnection() {
        try {
            this.in.close();
            this.out.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readLine() {
        try {
            String line = this.in.readLine();
            System.out.println("CLIENT read: " + line);
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeLine(String out){
        System.out.println("CLIENT write: " + out);
        this.out.println(out);
    }
}
