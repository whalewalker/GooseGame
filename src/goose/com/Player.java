package goose.com;

public class Player {
    private String name;
    private int previousPosition;

    public Player(String name) {
        this.name = name;
        this.previousPosition = 0;
    }

    public String getName() {
        return name;
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }
}
