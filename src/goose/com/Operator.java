package goose.com;

import static goose.com.Helper.getDiceValues;
import static goose.com.Helper.getPlayerName;

public enum Operator {

    ADD{
        void apply(String[] userInput, GooseGame gooseGame) throws GooseGameException {
            if (!userInput[1].equalsIgnoreCase("player"))
                throw new GooseGameException("Invalid Command");
            if (userInput.length != 3) throw new GooseGameException("Invalid Command");
            String playerName = getPlayerName(userInput, 2);
            gooseGame.addPlayer(playerName);
        }
    },

    MOVE{
        void apply(String[] userInput, GooseGame gooseGame) throws GooseGameException {
            int[] values = getDiceValues(userInput);
            String playerName = getPlayerName(userInput, 1);
            gooseGame.makeMove(playerName, values[0], values[1]);
        }
    };

    abstract void apply(String[] userInput, GooseGame gooseGame) throws GooseGameException;
}
