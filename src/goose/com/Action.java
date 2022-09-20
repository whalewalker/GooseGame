package goose.com;

import static goose.com.GameConstant.ADD_PLAYER;
import static goose.com.GameConstant.MAKE_MOVE;

public class Action {
    public static void performOperation(String action, String[] userInput, GooseGame gooseGame) throws GooseGameException {
        if (!(action.equals(MAKE_MOVE) || action.equals(ADD_PLAYER))) throw new GooseGameException("Invalid command");
        Operator.valueOf(action.toUpperCase()).apply(userInput, gooseGame);
    }

    public static StringBuilder format(String action, MessageFormatter formatter, StringBuilder builder) {
        return Formatter.valueOf(action.toUpperCase()).format(builder, formatter);
    }

    public static int getShortcutCount(String action, int pieceCount, int die1, int die2, MessageFormatter formatter) {
        return ShortCut.valueOf(action.toUpperCase()).calculate(pieceCount, die1, die2, formatter);
    }
}
