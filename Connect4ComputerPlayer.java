package core;
import java.util.Random;

/**
 * a bare bones class to be inherited by
 * Connect4
 */
public class Connect4ComputerPlayer {
    Random rand = new Random();

    /**
     * method to be implemented by Connect4
     */
    public int[] chooseTurn() {
        return new int[1];
    }
}
