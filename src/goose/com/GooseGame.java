package goose.com;

import java.util.HashMap;
import java.util.Map;

import static goose.com.GameConstant.*;

public class GooseGame {
    private final String[] board;
    private final Map<String, Player> players;
    private boolean win;
    private StringBuilder messageLogger;
    private MessageFormatter messageFormatter;

    public GooseGame() {
        this.board = new String[63];
        this.win = false;
        this.players = new HashMap<>();
        this.messageLogger = new StringBuilder();
        this.messageFormatter = new MessageFormatter();
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public String[] getBoard() {
        return board;
    }

    public StringBuilder getMessageLogger() {
        return messageLogger;
    }

    public void resetLogger() {
        this.messageLogger = new StringBuilder();
    }

    public void logMessage(String message) {
        this.messageLogger = new StringBuilder(message);
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Player addPlayer(String playerName) throws GooseGameException {
        if (players.containsKey(playerName)) {
            throw new GooseGameException(String.format("%s: already existing player", playerName));
        }
        Player savedPlayer = savePlayer(new Player(playerName));
        logMessage();
        return savedPlayer;
    }

    private void logMessage() {
        StringBuilder message = new StringBuilder("players: ");
        for (String playersName : this.players.keySet()) {
            message.append(playersName).append(", ");
        }
        logMessage(message.substring(0, message.toString().length() - 2));
    }

    public void makeMove(String playerName, int die1, int die2) throws GooseGameException {
        Player currentPlayer = getAPlayer(playerName);
        if (currentPlayer == null) {
            throw new GooseGameException(String.format("%s: %s", playerName, "is not a player"));
        }
        movePlayerPieceOnBoard(currentPlayer, die1, die2);
    }

    private void movePlayerPieceOnBoard(Player currentPlayer, int die1, int die2) {
        String playerOnSpace;
        int pieceCount = currentPlayer.getPreviousPieceCount() + (die1 + die2);
        setupMessageFormatter(currentPlayer, die1, die2);

        if (pieceCount > this.board.length - 1) {
            int newPieceCount = currentPlayer.getPreviousPieceCount() + 1;
            playerOnSpace = getPlayerOnSpace(newPieceCount);
            updatePreviousPlayerOnSpace(currentPlayer.getPreviousPieceCount(), playerOnSpace);
            movePlayerToSpace(currentPlayer.getName(), newPieceCount);
            updateCurrentPlayerOnSpace(currentPlayer, newPieceCount, currentPlayer.getPreviousPieceCount());
            if (!isWin()) messageFormatter.setAction("bounce");
        } else {
            playerOnSpace = getPlayerOnSpace(pieceCount);
            updatePreviousPlayerOnSpace(currentPlayer.getPreviousPieceCount(), playerOnSpace);
            int count = movePlayerToSpace(currentPlayer.getName(), pieceCount, die1, die2);
            updateCurrentPlayerOnSpace(currentPlayer, count, count);
        }
        formatMessageLogger();
    }

    private void updateCurrentPlayerOnSpace(Player currentPlayer, int newPieceCount, int previousPieceCount) {
        updatePlayer(currentPlayer, newPieceCount);
        updateMessageDto(previousPieceCount, currentPlayer);
    }

    private void updatePreviousPlayerOnSpace(int pieceCount, String playerOnSpace) {
        removePlayerFromPreviousSpace(pieceCount);
        updatePrankPlayerMove(playerOnSpace, pieceCount);
    }

    private void setupMessageFormatter(Player currentPlayer, int die1, int die2) {
        messageFormatter.setDie1(die1);
        messageFormatter.setDie2(die2);
        if (currentPlayer.getPreviousPieceCount() == 0)
            messageFormatter.setFrom("Start");
        else
            messageFormatter.setFrom(String.valueOf(currentPlayer.getPreviousPieceCount()));
    }

    private void updateMessageDto(int previousPieceCount, Player currentPlayer) {
        messageFormatter.setTo(String.valueOf(previousPieceCount));
        messageFormatter.setCurrentPlayer(players.get(currentPlayer.getName()));
    }

    private void updatePrankPlayerMove(String playerOnSpace, int pieceCount) {
        if (playerOnSpace != null) {
            messageFormatter.setAction("prank");
            Player prankPlayer = getAPlayer(playerOnSpace);
            removePlayerFromPreviousSpace(prankPlayer.getPreviousPieceCount());
            movePlayerToSpace(playerOnSpace, pieceCount);
            updatePlayer(prankPlayer, pieceCount);
            messageFormatter.setPrankPlayer(players.get(prankPlayer.getName()));
        }

    }

    private void updatePlayer(Player player, int pieceCount) {
        player.setPreviousPieceCount(pieceCount);
        savePlayer(player);
    }

    private Player savePlayer(Player player) {
        return players.put(player.getName(), player);
    }

    private int movePlayerToSpace(String name, int pieceCount, int die1, int die2) {
        int shortcutCount = getShortcut(pieceCount, die1, die2);
        this.board[shortcutCount - 1] = name;
        checkWin();
        return shortcutCount;
    }

    private void movePlayerToSpace(String name, int pieceCount) {
        if (pieceCount > 0) this.board[pieceCount - 1] = name;
         else this.board[pieceCount] = name;
        checkWin();
    }

    private void checkWin() {
        if (this.board[WIN_COUNT - 1] != null) {
            setWin(true);
            messageFormatter.setAction("win");
        }
    }

    private int getShortcut(int pieceCount, int die1, int die2) {
        switch (pieceCount) {
            case 6: {
                return Action.getShortcutCount("bridge", pieceCount, die1, die2, this.messageFormatter);
            }
            case 5:
            case 9:
            case 14:
            case 18:
            case 23:
            case 27: {
                return Action.getShortcutCount("goose", pieceCount, die1, die2, this.messageFormatter);
            }
            default: {
                return pieceCount;
            }
        }
    }

    private void removePlayerFromPreviousSpace(int previousPieceCount) {
        if (previousPieceCount > 0) {
            this.board[previousPieceCount - 1] = null;
        }
    }

    private String getPlayerOnSpace(int pieceCount) {
        return getBoard()[pieceCount - 1];
    }

    private Player getAPlayer(String playerName) {
        return players.get(playerName);
    }

    private void formatMessageLogger() {
        StringBuilder builder = Action.format(DEFAULT, messageFormatter, new StringBuilder());

        String message = messageFormatter.getAction().equals(DEFAULT) ? builder.toString() :
                Action.format(messageFormatter.getAction(), messageFormatter, builder).toString();

        logMessage(message);
        // Reset message dto
        this.messageFormatter = new MessageFormatter();
    }
}
