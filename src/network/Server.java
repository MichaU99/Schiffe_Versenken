package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int portNumber;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void setPortNumber(int portNumber) {
        assert portNumber >= 0 : "Portnumber set negative";
        this.portNumber = portNumber;
    }

    public Server() {
        this.portNumber = 5555;
    }

    public Server(int portNumber) {
        this.setPortNumber(portNumber);
    }

    public boolean waitForConnection() {
        // THIS METHOD WILL BLOCK
        try {
            this.serverSocket = new ServerSocket(this.portNumber);
            this.clientSocket = this.serverSocket.accept();
            this.out = new PrintWriter(this.clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.clientSocket.isConnected();
    }

    public void writeLine(String out) {
        this.out.write(out);
    }

    public String readLine() {
        try {
            return this.in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        try {
            this.in.close();
            this.out.close();
            this.clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
