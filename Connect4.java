package core;
import java.util.Scanner;

enum Turn{
    X, O, empty;

    @Override
    public String toString() {
        return this == empty ? "" : this.name();
    }
}

/**
 * this class makes a 2d array for a connect 4 grid
 * and establishes the logic for a connect 4 game
 * and inherits Connect4ComputerPlayer
 *
 * @author brandon Glazier
 * @version 16 October 2018
 */

public class Connect4 extends Connect4ComputerPlayer implements java.io.Serializable{

    private static int ROWS = 6;
    private static int COLS = 8;


    private String[][] grid;
    Scanner sc;
    Turn t;
    int turnNum;
    private int[] movesLeft = {5, 5, 5, 5, 5, 5, 5};

    public Connect4(){
        grid = new String[ROWS][COLS];
        sc = new Scanner(System.in);
        t = Turn.X;
        turnNum = 0;
    }

    /**
     * makes an empty 6 x 7 grid using a nested for loop
     *
     */
    public void emptyGrid() {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                if(j == 7) { grid[i][j] = "|"; }
                else { grid[i][j] = "|  "; }
            }
        }
    }

    /**
     * prints a 6 x 7 grid using a nested for loop
     */
    public void printGrid(){
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++)
                System.out.print(grid[i][j]);
            System.out.println();
        }
    }

    /**
     * changes the enum t to either X or O
     * @param player either X or O, changes the current value of t to it
     */
    public void turn(String player) { t = Turn.valueOf(player); }

    /**
     * returns the current value t has
     * @return
     */
    public String currentTurn() { return t.toString(); }

    /**
     * checks to see if the input column number is valid and not filled
     * and returns -1 if not and if it is, it will decrease the index at movesleft[col - 1] and
     * return the row number
     *
     * @param col the input column number that is used to check if valid or filled
     * @throws ArrayIndexOutOfBoundsException thrown if the column number is too big or small
     * @return returns -1 for an invalid input and the row number for a valid input
     */
    public int checkValid(int col) {
        try {
            if (movesLeft[col - 1] == -1) {
                return -1;
            } else {
                --movesLeft[col - 1];
                return movesLeft[col - 1] + 1;
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            return -1;
        }
    }

    /**
     * calls checkValid to see if the column is valid and saves the result if it is
     * not valid it returns -1 and if it is it adds the letter to the specified row and column
     * and returns 1
     *
     * @return returns -1 if the element was not added or the row if it was added
     */
    public int addElement(int col) {
        int res = checkValid(col);

        if(res == -1) {
            return -1;
        }
        else {
            grid[res][col-1] = "|"+t;
            turnNum++;
            return res;
        }
    }

    public boolean draw() {
        return turnNum == 42;
    }

    /**
     * checks to see if there have been enough turns for a win to occur
     * and then checks the grid across, down, up diagonal, and down diagonal
     *
     * @return true if a win occurs or false if a win does not occur or there
     * have been less than 7 turns
     */
    public boolean won() {

        if(turnNum < 7) {
            return false;
        }

        String comp = "|" + t;
        for (int c = 0; c < COLS - 3 ; c++ ){
            for (int r = 0; r < ROWS; r++){
                String point = grid[r][c];
                if(!point.equals(comp)) { continue; }

                if (grid[r][c].equals(comp) && grid[r][c+1].equals(comp) && grid[r][c+2].equals(comp) && grid[r][c+3].equals(comp)){
                    return true;
                }
            }
        }

        for (int r = 0; r < ROWS - 3 ; r++ ){
            for (int c = 0; c < COLS; c++){
                String point = grid[r][c];
                if(!point.equals(comp)) { continue; }

                if (grid[r + 1][c].equals(comp) && grid[r + 2][c].equals(comp) && grid[r + 3][c].equals(comp)){
                    return true;
                }
            }
        }

        for (int r = 3; r < ROWS; r++){
            for (int c = 0; c < COLS - 3; c++){
                String point = grid[r][c];
                if(!point.equals(comp)) { continue; }

                if (grid[r - 1][c + 1].equals(comp) && grid[r - 2][c + 2].equals(comp) && grid[r - 3][c + 3].equals(comp))
                    return true;
            }
        }

        for (int r = 3; r < ROWS; r++){
            for (int c = 3; c < COLS; c++){
                String point = grid[r][c];
                if(!point.equals(comp)) { continue; }

                if (grid[r - 1][c - 1].equals(comp) && grid[r - 2][c - 2].equals(comp) && grid[r - 3][c - 3].equals(comp))
                    return true;
            }
        }

        return false;
    }

    /**
     * Checks horizontally and vertically for a
     * win for either X or O
     *
     * @param comp either X or O
     * @exception IndexOutOfBoundsException caught
     * in case the checked value is close to one of the edges
     * @return return the column if there is an
     * imminent win or -1 if there is not
     */
    public int checkBlockWin(String comp) {
        for (int c = 0; c < COLS - 2 ; c++ ){
            for (int r = 0; r < ROWS; r++){

                String point = grid[r][c];
                if(!point.equals(comp)) { continue; }

                if (grid[r][c + 1].equals(comp) && grid[r][c + 2].equals(comp)){
                    try{
                        if(movesLeft[c-1] == r) {
                            return c - 1;
                        }
                    }
                    catch (IndexOutOfBoundsException e) {
                        if(movesLeft[c + 3] == r){
                            return c + 3;
                        }
                    }
                }
                try {
                    if(grid[r][c + 1].equals(comp) && grid[r][c+3].equals(comp) && r == movesLeft[c + 2]) {
                        return c + 2;
                    }
                    else if(grid[r][c + 2].equals(comp) && grid[r][c + 3].equals(comp) && r == movesLeft[c + 1]) {
                        return c + 1;
                    }
                }
                catch(IndexOutOfBoundsException e) {}
            }
        }

        for (int r = 0; r < ROWS - 2 ; r++ ){
            for (int c = 0; c < COLS; c++){
                String point = grid[r][c];
                if(!point.equals(comp)) { continue; }
                try {
                    if (grid[r + 1][c].equals(comp) && grid[r + 2][c].equals(comp) && grid[r + 3][c].equals("|  ")){
                        return c;
                    }
                }
                catch (IndexOutOfBoundsException e) {}
            }
        }

        return -1;
    }

    /**
     * inherited from Connect4ComputerPlayer
     * method that determines the computer's turn
     * if the player is about to win vertically or
     *  horizontally it will block
     * but if the player isn't about to win and the computer
     * is it will make the winning move
     * if those don't apply it will move randomly
     *
     * @return int array that has 2 numbers that correspond to
     *         row and column, used to make GUI less painful
     */
    public int[] chooseTurn() {
        int[] rC = new int[2];
        int goForWin;
        int valid;
        int randTurn;
        int block = checkBlockWin("|X");

        if(block != -1) {
            rC[1] = block;
            valid = addElement(block + 1);

            if(valid != -1) {
                rC[0] = valid;
                return rC;
            }
        }

        goForWin = checkBlockWin("|O");
        if(goForWin != -1) {
            rC[1] = goForWin;
            valid = addElement(goForWin + 1);

            if(valid != -1) {
                rC[0] = valid;
                return rC;
            }
        }

        do {
            randTurn = rand.nextInt(7) + 1;
            valid = addElement(randTurn);
            rC[0] = valid;
            rC[1] = (randTurn - 1);
        }while(valid == -1);

        return rC;
    }
}
