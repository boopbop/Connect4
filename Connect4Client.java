package core;
import ui.Connect4GUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import java.io.*;
import java.net.*;
import java.util.Date;

public class Connect4Client extends Application {

    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;
    private boolean continueToPlay = true;
    private Connect4GUI gui;
    private boolean waiting = true;
    private boolean myTurn = false;

    public void start(Stage stg) {

        connectToServer();
        try{
            Object obj = read();
            GameData data = (GameData) obj;
            gui = new Connect4GUI();
            gui.setCell( data.getCell());
            gui.setLblString( data.getLblString());
            gui.setStartMSG( data.getStartMSG());

            gui.setConnect4Client( this);
            gui.dontShowMSG();
            gui.start(stg);
        }
        catch(ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    synchronized public Object read() throws IOException, ClassNotFoundException{
     return fromServer.readObject();
    }

    public void connectToServer() {
        try {
            // Create a socket to connect to the server
            Socket socket = new Socket("localhost", 8000);

            // Create an input stream to receive data from the server
            fromServer = new ObjectInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new ObjectOutputStream(socket.getOutputStream());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // Control the game on a separate thread
        new Thread(() -> {
            try {
                // Get notification from the server
                //Connect4GUI obj = (Connect4GUI) fromServer.readObject();
                Object obj = read();
                gui = (Connect4GUI) obj;

                // Continue to play
                while (continueToPlay) {
                    if (gui.turn().equals("X")) {
                        waitForPlayerAction(); // Wait for player 1 to move
                        sendMove(); // Send the move to the server
                        receiveInfoFromServer(); // Receive info from the server
                    }
                    else if (gui.turn().equals("O")) {
                        receiveInfoFromServer(); // Receive info from the server
                        waitForPlayerAction(); // Wait for player 2 to move
                        sendMove(); // Send player 2's move to the server
                    }
                }
            }
            catch (Exception ex) { ex.printStackTrace(); }
        }).start();
    }

    private void waitForPlayerAction() throws InterruptedException {
        while (waiting) {
            Thread.sleep(100);
        }

        waiting = true;
    }

    public void sendMove() {
        try {
            synchronized (toServer) {
                GameData data = new GameData();
                data.setCell( gui.getCell());
                data.setLblString( gui.getLblString());
                data.setStartMSG( gui.getStartMSG());
                toServer.writeObject( data);
            }
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void receiveInfoFromServer() throws IOException {
        // Receive game status
        try {
            Object obj = read();
            gui = (Connect4GUI) obj;

            if (gui.win()) {
                continueToPlay = false;
            } else if (gui.draw()) {
                continueToPlay = false;
            } else {
                receiveMove();

            }
        }
        catch (ClassNotFoundException e) {}
    }

    private void receiveMove() throws IOException {
        // Get the other player's move

        try {
            Object obj = read();
            GameData data = (GameData) obj;
            gui.setLblString( data.getLblString());
            gui.setCell( data.getCell());
            gui.setStartMSG( data.getStartMSG());
        }
        catch (ClassNotFoundException e) {}
    }
}
