package goose.com;

import java.util.ArrayList;
import java.util.List;

public class MessageFormatter {
    private String from;
    private String to;
    private Player prankPlayer;
    private Player currentPlayer;
    private final List<Integer> pieceMovement = new ArrayList<>();
    private int gooseCount;
    private int die1;
    private int die2;
    private String action = "default";


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Player getPrankPlayer() {
        return prankPlayer;
    }

    public void setPrankPlayer(Player prankPlayer) {
        this.prankPlayer = prankPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getGooseCount() {
        return gooseCount;
    }

    public void setGooseCount(int gooseCount) {
        this.gooseCount = gooseCount;
    }

    public int getDie1() {
        return die1;
    }

    public void setDie1(int die1) {
        this.die1 = die1;
    }

    public int getDie2() {
        return die2;
    }

    public void setDie2(int die2) {
        this.die2 = die2;
    }

    public List<Integer> getPieceMovement() {
        return pieceMovement;
    }

    public void addToMove(int position, int value) {
        pieceMovement.add(value);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
