package ui;
import core.*;
import java.util.Scanner;

/**
 * this class uses the Connect4 class to play connect
 * 4 in the text console
 *
 * @author brandon Glazier
 * @version 1 October 2018
 */
public class Connect4TextConsole {

    /**
     * makes an object of Connect4 to play connect 4
     */
    public static void main(String[] args) {
        Connect4 game = new Connect4();
        boolean win = false;
        int valid;
        Scanner sc = new Scanner(System.in);

        game.emptyGrid();
        game.printGrid();

        System.out.println("Begin Game. Enter ‘P’ if you want to play against another player; " +
                "enter ‘C’ to play against computer.");
        String pOrC = sc.nextLine();

        if(pOrC.equalsIgnoreCase("P")) {
            while (!win) {
                game.turn("X");
                do {
                    System.out.println("PlayerX – your turn. Choose a column number from 1-7.");
                    int col = sc.nextInt();
                    System.out.println(col);
                    valid = game.addElement(col);
                } while (valid == -1);
                game.printGrid();


                win = game.won();
                if (win) {
                    System.out.println("Player X Won the Game");
                    break;
                }

                game.turn("O");
                do {
                    System.out.println("PlayerO – your turn. Choose a column number from 1-7.");
                    int col = sc.nextInt();
                    System.out.println(col);
                    valid = game.addElement(col);
                } while (valid == -1);
                game.printGrid();

                win = game.won();
                if (win) {
                    System.out.println("Player O Won the Game");
                    break;
                }

                if (game.draw()) {
                    System.out.println("Nobody won");
                    break;
                }
            }
        }
        else if(pOrC.equalsIgnoreCase("C")) {
            System.out.println("Start game against computer. \n");
            while (!win) {
                game.turn("X");
                do {
                    System.out.println("PlayerX – your turn. Choose a column number from 1-7.");
                    int col = sc.nextInt();
                    System.out.println(col);
                    valid = game.addElement(col);
                } while (valid == -1);
                game.printGrid();


                win = game.won();
                if (win) {
                    System.out.println("Player X Won the Game");
                    break;
                }

                game.turn("O");
                System.out.println("Computer turn");
                game.chooseTurn();
                game.printGrid();

                win = game.won();
                if (win) {
                    System.out.println("Player O Won the Game");
                    break;
                }

                if (game.draw()) {
                    System.out.println("Nobody won");
                    break;
                }
            }
        }
    }
}


