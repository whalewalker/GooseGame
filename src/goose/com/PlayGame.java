package goose.com;

import java.util.Scanner;

import static goose.com.Helper.getOperatorName;
import static goose.com.Helper.isValidCommand;

public class PlayGame {
    static final String RED_BOLD = "\033[1;31m";
    static final String BLUE_BOLD = "\033[1;34m";
    static final String RESET = "\033[0m";
    static final String GREEN_BOLD = "\033[1;32m";

    static GooseGame gooseGame = new GooseGame();
    static boolean canExit = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println(GREEN_BOLD + "*********** Hello Welcome To Goose Game! *********** " +
                getGameCommand() + RESET);

         while (gooseGame.getWin() || canExit) {
             String userInput = getUserInput(scanner);
             try {
                 isValidCommand(userInput);
                 performOperation(userInput);
                 System.out.printf(BLUE_BOLD + "System response:=> %s\n" + RESET, gooseGame.getMessageLogger().toString());

                 gooseGame.resetLogger();
             } catch (GooseGameException ex) {
                 System.out.printf(RED_BOLD + "System response:=> %s\n" + RESET, ex.getMessage());
                 System.out.println(getGameCommand());
             }
         }

        System.out.println(GREEN_BOLD + "Goose game ended!" + RESET);
    }

    private static String getUserInput(Scanner scanner) {
        String terminalMessage = "Enter command: ";
        System.out.printf("\n%s", terminalMessage);
        return scanner.nextLine();
    }

    public static void performOperation(String userInput) throws GooseGameException {
        String[] userInputArr = userInput.trim().split(" ");
        String operator = getOperatorName(userInputArr);
        Action.performOperation(operator, userInputArr, gooseGame);
    }

    private static String getGameCommand() {
        return "\n===== COMMAND USED IN THIS GAME ==== " +
                "\n1. add player [player name] \n2. move [player name] [die1 value], [die2 value] " +
                "\n3. move [player name] \n===== COMMAND USAGE ===== \n1. add player Pippo " +
                "\n2. move Pippo 1, 2 \n3. move Pippo";
    }

}