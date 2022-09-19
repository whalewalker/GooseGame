package goose.com;

import java.util.List;

import static goose.com.GameConstant.*;

public enum Formatter {

    DEFAULT {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            String currentPlayerName = formatter.getCurrentPlayer().getName();
            String from = formatter.getFrom();

            return builder.append(String.format("%s %s %d, %d. %s %s %s to %s",
                    currentPlayerName,
                    ROLL_MESSAGE, formatter.getDie1(),
                    formatter.getDie2(), currentPlayerName,
                    MOVE_MESSAGE, from, DESTINATION.format(builder, formatter))
            );
        }
    },

    DESTINATION {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            String to = formatter.getTo();
            String action = formatter.getAction();
            List<Integer> pieceMovement = formatter.getPieceMovement();

            return new StringBuilder(action.equals("bridge") ?
                    BRIDGE_MESSAGE : action.equals("goose") ?
                    String.valueOf(pieceMovement.get(0)) :
                    action.equals("bounce") ? String.valueOf(WIN) : to);
        }
    },


    WIN {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            String currentPlayerName = formatter.getCurrentPlayer().getName();
            return builder.append(String.format(". %s %s", currentPlayerName, WIN_MESSAGE));

        }
    },

    BRIDGE {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            String currentPlayerName = formatter.getCurrentPlayer().getName();
            String to = formatter.getTo();
            return builder.append(String.format(". %s %s to %s", currentPlayerName, JUMP_MESSAGE, to));
        }
    },

    BOUNCE {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            String currentPlayerName = formatter.getCurrentPlayer().getName();
            String to = formatter.getTo();
            return builder.append(String.format(". %s %s %s returns to %s", currentPlayerName,
                    BOUNCE_MESSAGE, currentPlayerName, to));
        }
    },

    GOOSE {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            String to = formatter.getTo();
            List<Integer> pieceMovement = formatter.getPieceMovement();
            String currentPlayerName = formatter.getCurrentPlayer().getName();

            if (pieceMovement.size() > 1) {
                for (int count = 1; count < formatter.getGooseCount(); count++) {
                    builder.append(String.format(", %s. %s %s to %s",
                            GOOSE_MESSAGE, currentPlayerName,
                            RETRY_MESSAGE, pieceMovement.get(count)));
                }
            }
            return builder.append(String.format(", %s. %s %s to %s", GOOSE_MESSAGE, currentPlayerName,
                    RETRY_MESSAGE, to));
        }
    },

    PRANK {
        @Override
        StringBuilder format(StringBuilder builder, MessageFormatter formatter) {
            return builder.append(String.format(". On %s there is %s, %s %s", formatter.getCurrentPlayer().getPreviousPieceCount(),
                    formatter.getPrankPlayer().getName(), PRANK_MESSAGE,
                    formatter.getPrankPlayer().getPreviousPieceCount()
            ));
        }
    };

    abstract StringBuilder format(StringBuilder builder, MessageFormatter formatter);
}
