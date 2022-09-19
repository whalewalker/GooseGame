package goose.com;

public class Player {
    private String name;
    private int previousPieceCount;

    public Player(String name) {
        this.name = name;
        this.previousPieceCount = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPreviousPieceCount() {
        return previousPieceCount;
    }

    public void setPreviousPieceCount(int previousPieceCount) {
        this.previousPieceCount = previousPieceCount;
    }
}
