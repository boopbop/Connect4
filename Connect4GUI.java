package ui;

import core.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.IOException;

/**
 * GUI for a connect 4 game.
 * at the beginning choose to play player v player or player v computer.
 * players use buttons corresponding to each column in order to do their turn.
 * the layout and GridCell class were both inspired by the tic-tac-toe game
 * that was posted on blackboard.
 *
 * @author Brandon Glazier
 * @version 30 October 2018
 */
public class Connect4GUI extends Application implements java.io.Serializable {

    private static Connect4 game = new Connect4();
    private static String compPlay;
    private GridCell[][] cell = new GridCell[6][7];
    String lblString = "Play against person or computer?";
    private int startMSG = 1;

    private boolean isServer = false;

    public void setServer( boolean isServer) {
        this.isServer = isServer;
    }

    public boolean getServer() {
        return isServer;
    }

    private Connect4Client client;

    public static Connect4 getGame() {
        return game;
    }

    public static void setGame(Connect4 game) {
        Connect4GUI.game = game;
    }

    public static void setCompPlay(String compPlay) {
        Connect4GUI.compPlay = compPlay;
    }

    public GridCell[][] getCell() {
        return cell;
    }

    public void setCell(GridCell[][] cell) {
        this.cell = cell;
    }

    public String getLblString() {
        return lblString;
    }

    public void setLblString(String lblString) {
        this.lblString = lblString;
    }

    public int getStartMSG() {
        return startMSG;
    }

    public void setStartMSG(int startMSG) {
        this.startMSG = startMSG;
    }

    public void setConnect4Client(Connect4Client client) {
        this.client = client;
    }

    /**
     * sets up all of the label, buttons, pane, and layout and behavior
     * for the player and computer buttons
     * initially ui is set up for a person to choose if they want
     * to go against a player or computer and once that is done it will
     * switch to the connect 4 layout
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        game.emptyGrid();

        GridPane pane = new GridPane();

        Label lblStatus = new Label(lblString);
        lblStatus.setFont(new Font(30));

        Button player = new Button("Player");
        Button computer = new Button("Computer");
        HBox hb = new HBox(player, computer);
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(150);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) { pane.add(cell[i][j] = new GridCell(), j, i); }
        }

        Button c1 = new Button("Column 1");
        Button c2 = new Button("Column 2");
        Button c3 = new Button("Column 3");
        Button c4 = new Button("Column 4");
        Button c5 = new Button("Column 5");
        Button c6 = new Button("Column 6");
        Button c7 = new Button("Column 7");

        c1.setOnAction(event -> {
            buttonFunctionality(1);
            lblStatus.setText(lblString);
        });

        c2.setOnAction(event -> {
            buttonFunctionality(2);
            lblStatus.setText(lblString);
        });

        c3.setOnAction(event -> {
            buttonFunctionality(3);
            lblStatus.setText(lblString);
        });

        c4.setOnAction(event -> {
            buttonFunctionality(4);
            lblStatus.setText(lblString);
        });

        c5.setOnAction(event -> {
            buttonFunctionality(5);
            lblStatus.setText(lblString);
        });

        c6.setOnAction(event -> {
            buttonFunctionality(6);
            lblStatus.setText(lblString);
        });

        c7.setOnAction(event -> {
            buttonFunctionality(7);
            lblStatus.setText(lblString);
        });

        HBox h = new HBox(c1, c2, c3, c4, c5, c6, c7);
        BorderPane borderPane = new BorderPane();

        if(startMSG == 1) {
            borderPane.setTop(lblStatus);
            borderPane.setCenter(hb);
        }
        else {
            compPlay = "p";
            borderPane.setTop(h);
            borderPane.setCenter(pane);
            lblStatus.setText(turn().equals("X") ?
                    "Player 1's turn to play" : "Player 2's turn to play");
            borderPane.setBottom(lblStatus);
        }

        //both buttons change the layout to the "default connect 4 layout"
        //the player button makes it so player 2 is treated as a player
        //the computer button makes it so player 2 is treated as a computer instead
        player.setOnAction(event -> {
            compPlay = "p";
            borderPane.setTop(h);
            borderPane.setCenter(pane);
            lblStatus.setText("Player 1's turn to play");
            borderPane.setBottom(lblStatus);
        });
        computer.setOnAction(event -> {
            compPlay = "c";
            borderPane.setTop(h);
            borderPane.setCenter(pane);
            lblStatus.setText("Player 1's turn to play");
            borderPane.setBottom(lblStatus);
        });


        Scene scene = new Scene(borderPane, 450, 170);
        primaryStage.setTitle("Connect4");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void dontShowMSG() {
        startMSG++;
    }

    public static String getCompPlay() {
        return compPlay;
    }

    public boolean win() {return game.won();}

    public boolean draw() {return game.draw();}

    public String turn() {
        String turn = game.currentTurn();
        return turn;
    }

    /**
     * method made to avoid a ton of repeat code for each of the 7
     * buttons.
     * if the column the button represents has space for a circle, it will place one.
     * if the computer is active, it will do its turn immediately afterwards
     * @param col used during the addElement method call and inserting the value
     *            (not used for computer turns)
     */
    public void buttonFunctionality(int col) {
        int row;

        if (turn().equals("X") || compPlay.equals("p")) {
            row = game.addElement(col);

            if(row != -1) {
                cell[row][col-1].insertCircle(turn());
                checkGameState();
            }
        }
        if(turn().equals("O") && compPlay.equals("c")) {
            int[] rC = game.chooseTurn();
            cell[rC[0]][rC[1]].insertCircle(turn());
            checkGameState();
        }

        if( !isServer)
            client.sendMove();
    }

    /**
     * uses functionality from Connect4.java to check for win,
     * draw, or switch turn from one player to the next
     */
    public void checkGameState() {
        if (win()) {
            lblString = turn().equals("X") ?
                    "Player 1 won!" : "Player won 2!";
            game.turn("empty");
        }
        else if (draw()) {
            lblString = "Draw! The game is over";
            game.turn("empty");
        }
        else {
            game.turn(turn().equals("X") ? "O" : "X");

            lblString = turn().equals("X") ?
                    "Player 1's turn to play" : "Player 2's turn to play";
        }
    }


    /**
     * class made to act as a grid to place the circles in
     */
    public class GridCell extends Pane implements java.io.Serializable {

        /**
         * set up the border and override region
         * preferred size
         */
        public GridCell() {
            setStyle("-fx-border-color: purple");
            this.setPrefSize(2000, 2000);
        }

        /**
         * makes a circle of one of two colors depending on
         * the turn and draws it in the cell
         * @param t the current turn, X or O
         */
        public void insertCircle(String t) {

            if (t.equals("X")) {
                Circle circle = new Circle();
                circle.centerXProperty().bind(this.widthProperty().divide(2));
                circle.centerYProperty().bind(this.heightProperty().divide(2));
                circle.setRadius(50);
                circle.setStroke(Color.LIMEGREEN);
                circle.setFill(Color.LIMEGREEN);
                this.getChildren().add(circle);
            }
            else if (t.equals("O")) {
                Circle circle = new Circle();
                circle.centerXProperty().bind(this.widthProperty().divide(2));
                circle.centerYProperty().bind(this.heightProperty().divide(2));
                circle.setRadius(50);
                circle.setStroke(Color.HOTPINK);
                circle.setFill(Color.HOTPINK);
                this.getChildren().add(circle);
            }
        }
    }

}
