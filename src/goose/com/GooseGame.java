package goose.com;

import java.util.LinkedHashMap;
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
        this.players = new LinkedHashMap<>();
        this.messageLogger = new StringBuilder();
        this.messageFormatter = new MessageFormatter();
    }

    public boolean getWin() {
        return !win;
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

    public void addPlayer(String playerName) throws GooseGameException {
        if (players.containsKey(playerName.toLowerCase())) {
            throw new GooseGameException(String.format("%s: already existing player", playerName));
        }
        savePlayer(new Player(playerName.toLowerCase()));
        logMessage();
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
            throw new GooseGameException(String.format("%s: %s", playerName.toLowerCase(), "is not a player"));
        }
        movePlayerPieceOnBoard(currentPlayer, die1, die2);
    }

    private void movePlayerPieceOnBoard(Player currentPlayer, int die1, int die2) {
        String playerOnSpace;
        int position = currentPlayer.getPreviousPosition() + (die1 + die2);
        setupMessageFormatter(currentPlayer, die1, die2);

        if (position > this.board.length) {
            int newPosition = currentPlayer.getPreviousPosition() + 1;
            playerOnSpace = getPlayerOnSpace(newPosition);
            updatePreviousPlayerOnSpace(currentPlayer.getPreviousPosition(), playerOnSpace);
            movePlayerToSpace(currentPlayer.getName(), newPosition);
            updateCurrentPlayerOnSpace(currentPlayer, newPosition);
            if (getWin()) messageFormatter.setAction("bounce");
        } else {
            playerOnSpace = getPlayerOnSpace(position);
            updatePreviousPlayerOnSpace(currentPlayer.getPreviousPosition(), playerOnSpace);
            int count = movePlayerToSpace(currentPlayer.getName(), position, die1, die2);
            updateCurrentPlayerOnSpace(currentPlayer, count);
        }
        formatMessageLogger();
    }

    private void updateCurrentPlayerOnSpace(Player currentPlayer, int newPosition) {
        updatePlayer(currentPlayer, newPosition);
        updateMessageDto(currentPlayer);
    }

    private void updatePreviousPlayerOnSpace(int position, String playerOnSpace) {
        removePlayerFromPreviousSpace(position);
        updatePrankPlayerMove(playerOnSpace, position);
    }

    private void setupMessageFormatter(Player currentPlayer, int die1, int die2) {
        messageFormatter.setDie1(die1);
        messageFormatter.setDie2(die2);
        if (currentPlayer.getPreviousPosition() == 0)
            messageFormatter.setFrom("Start");
        else
            messageFormatter.setFrom(String.valueOf(currentPlayer.getPreviousPosition()));
    }

    private void updateMessageDto(Player currentPlayer) {
        messageFormatter.setTo(String.valueOf(currentPlayer.getPreviousPosition()));
        messageFormatter.setCurrentPlayer(players.get(currentPlayer.getName()));
    }

    private void updatePrankPlayerMove(String playerOnSpace, int position) {
        if (playerOnSpace != null) {
            messageFormatter.setAction("prank");
            Player prankPlayer = getAPlayer(playerOnSpace);
            removePlayerFromPreviousSpace(prankPlayer.getPreviousPosition());
            movePlayerToSpace(playerOnSpace, position);
            updatePlayer(prankPlayer, position);
            messageFormatter.setPrankPlayer(players.get(prankPlayer.getName()));
        }

    }

    private void updatePlayer(Player player, int position) {
        player.setPreviousPosition(position);
        savePlayer(player);
    }

    private void savePlayer(Player player) {
         players.put(player.getName(), player);
    }

    private int movePlayerToSpace(String name, int position, int die1, int die2) {
        int shortcutCount = getShortcut(position, die1, die2);
        this.board[shortcutCount - 1] = name;
        checkWin();
        return shortcutCount;
    }

    private void movePlayerToSpace(String name, int position) {
        if (position > 0) this.board[position - 1] = name;
         else this.board[position] = name;
        checkWin();
    }

    private void checkWin() {
        if (this.board[WIN_COUNT - 1] != null) {
            setWin(true);
            messageFormatter.setAction("win");
        }
    }

    private int getShortcut(int position, int die1, int die2) {
        switch (position) {
            case 6: {
                return Action.getShortcutCount("bridge", position, die1, die2, this.messageFormatter);
            }
            case 5:
            case 9:
            case 14:
            case 18:
            case 23:
            case 27: {
                return Action.getShortcutCount("goose", position, die1, die2, this.messageFormatter);
            }
            default: {
                return position;
            }
        }
    }

    private void removePlayerFromPreviousSpace(int previousPosition) {
        if (previousPosition > 0) {
            this.board[previousPosition - 1] = null;
        }
    }

    private String getPlayerOnSpace(int position) {
        return getBoard()[position - 1];
    }

    private Player getAPlayer(String playerName) {
        return players.get(playerName.toLowerCase());
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
