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



public class Connect4Server extends Application{

    private int sessionNo = 1;


    public void start(Stage stg) {
        TextArea taLog = new TextArea();

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(taLog), 450, 200);
        stg.setTitle("TicTacToeServer"); // Set the stage title
        stg.setScene(scene); // Place the scene in the stage
        stg.show();

        new Thread( () -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> taLog.appendText(new Date() +
                        ": Server started at socket 8000\n"));

                // Ready to create a session for every two players
                while (true) {
                    Platform.runLater(() -> taLog.appendText(new Date() +
                            ": Wait for players to join session " + sessionNo + '\n'));

                    // Connect to player 1
                    Socket player1 = serverSocket.accept();
                    Connect4GUI gui = new Connect4GUI();
                    gui.setServer( true);
                    ObjectOutputStream toPlayer1 = new ObjectOutputStream(
                            player1.getOutputStream());

                    GameData gameData = new GameData();
                    toPlayer1.writeObject(gameData);

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() + ": Player 1 joined session "
                                + sessionNo + '\n');
                        taLog.appendText("Player 1's IP address" +
                                player1.getInetAddress().getHostAddress() + '\n');
                    });

                    // Connect to player 2
                    Socket player2 = serverSocket.accept();
                    ObjectOutputStream toPlayer2 = new ObjectOutputStream(
                            player2.getOutputStream());
                    toPlayer2.writeObject(gameData);

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() +
                                ": Player 2 joined session " + sessionNo + '\n');
                        taLog.appendText("Player 2's IP address" +
                                player2.getInetAddress().getHostAddress() + '\n');
                    });

                    // Display this session and increment session number
                    Platform.runLater(() ->
                            taLog.appendText(new Date() +
                                    ": Start a thread for session " + sessionNo++ + '\n'));


                    // Launch a new thread for this session of two players
                    new Thread(new HandleASession(player1, player2, gui)).start();
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    class HandleASession implements Runnable {
        private Socket player1;
        private Socket player2;
        private Connect4GUI gui;

        // Create and initialize cells

        private ObjectInputStream fromPlayer1;
        private ObjectOutputStream toPlayer1;
        private ObjectInputStream fromPlayer2;
        private ObjectOutputStream toPlayer2;

        // Continue to play
        private boolean continueToPlay = true;

        /** Construct a thread */
        public HandleASession(Socket player1, Socket player2, Connect4GUI gui) {
            this.player1 = player1;
            this.player2 = player2;
            this.gui = gui;
        }

        /** Implement the run() method for the thread */
        public void run() {
            try {
                // Create data input and output streams
                ObjectInputStream fromPlayer1 = new ObjectInputStream(
                        player1.getInputStream());
                ObjectOutputStream toPlayer1 = new ObjectOutputStream(
                        player1.getOutputStream());
                ObjectInputStream fromPlayer2 = new ObjectInputStream(
                        player2.getInputStream());
                ObjectOutputStream toPlayer2 = new ObjectOutputStream(
                        player2.getOutputStream());

                // Write anything to notify player 1 to start
                // This is just to let player 1 know to start
                toPlayer1.writeInt(1);

                // Continuously serve the players and determine and report
                // the game status to the players
                while (true) {
                    Connect4GUI p1gui = new Connect4GUI();
                    p1gui.setServer( true);

                    // Receive a move from player 1
                    Object obj = fromPlayer1.readObject();
                    GameData data = (GameData) obj;
                    p1gui.setCell( data.getCell());
                    p1gui.setLblString( data.getLblString());
                    p1gui.setStartMSG( data.getStartMSG());

                    p1gui.dontShowMSG();

                    p1gui.checkGameState();

                    data.setCell( p1gui.getCell());
                    data.setLblString( p1gui.getLblString());
                    data.setStartMSG( p1gui.getStartMSG());

                    sendMove(toPlayer2, data);
                    sendMove(toPlayer1, data);

                    // Check if Player 1 wins
                    if (gui.win()) {
                        break; // Break the loop
                    }
                    else if (gui.draw()) { // Check if all cells are filled
                        break;
                    }

                    // Receive a move from Player 2
                    obj = fromPlayer2.readObject();
                    data = (GameData) obj;

                    // Check if Player 2 wins
                    if (gui.win()) {
                        sendMove(toPlayer1, data);
                        break;
                    }
                    else { sendMove(toPlayer1, data); }
                }
            }
            catch(IOException | ClassNotFoundException ex) { ex.printStackTrace(); }
        }

        /** Send the move to other player */
        synchronized private void sendMove(ObjectOutputStream out, GameData gui)
                throws IOException {
            out.writeObject(gui);
        }


    }
}
