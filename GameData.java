package core;

import ui.Connect4GUI;

import java.io.Serializable;

public class GameData implements Serializable {
    private static Connect4 game = new Connect4();
    private static String compPlay;
    private Connect4GUI.GridCell[][] cell = new Connect4GUI.GridCell[6][7];
    String lblString = "Play against person or computer?";
    private int startMSG = 1;

    public static Connect4 getGame() {
        return game;
    }

    public static void setGame(Connect4 game) {
        GameData.game = game;
    }

    public static String getCompPlay() {
        return compPlay;
    }

    public static void setCompPlay(String compPlay) {
        GameData.compPlay = compPlay;
    }

    public Connect4GUI.GridCell[][] getCell() {
        return cell;
    }

    public void setCell(Connect4GUI.GridCell[][] cell) {
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
}
