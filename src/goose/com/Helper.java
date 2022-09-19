package goose.com;

import java.security.SecureRandom;

import static goose.com.GameConstant.GOOSE_COUNT;

public class Helper {
    static final SecureRandom randomNumbers = new SecureRandom();

    private static boolean isNumeric(String str) {
        return str.matches("\\d+(\\.\\d+)?");
    }

    static String getPlayerName(String[] userInputArr, int position) {
        return userInputArr[position];
    }

    public static int rollDie() {
        return (1 + randomNumbers.nextInt(6));
    }

    static String getOperatorName(String[] userInputArr) {
        return userInputArr[0];
    }

    //FIXME
    static int[] getDiceValues(String[] userInputArr) throws GooseGameException {
        if (userInputArr.length == 2) {
            return new int[]{rollDie(), rollDie()};
        }
        // Check if user does not parse the second die value
        else if (userInputArr.length == 3) {
            throw new GooseGameException("Invalid Command");
        } else {
            // Check if die value is numeric
            if (!(isNumeric(userInputArr[2].substring(0, 1).trim()) && isNumeric(userInputArr[3].trim()))) {
                throw new GooseGameException("Invalid Command");
            }
            int die1 = Integer.parseInt(userInputArr[2].substring(0, 1).trim());
            int die2 = Integer.parseInt(userInputArr[3].trim());

            // Check if dice value is within range 1 - 6
            if ((die1 > 6 || die1 < 1) || (die2 > 6 || die2 < 1)) {
                throw new GooseGameException("Dice values must be between 1 and 6");
            }
            return new int[]{die1, die2};
        }
    }

    static boolean isGoose(int pieceCount) {
        return GOOSE_COUNT.contains(String.valueOf(pieceCount));
    }

    static void isValidCommand(String userInput) throws GooseGameException {
        int inputLength = userInput.split(" ").length;
        if (inputLength > 4 || inputLength < 2) throw new GooseGameException("Invalid Command");
    }

}
