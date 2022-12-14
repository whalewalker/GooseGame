package goose.com;

import static goose.com.GameConstant.BRIDGE_COUNT;
import static goose.com.GameConstant.LIMIT;
import static goose.com.Helper.isGoose;

public enum Scenario {
    BRIDGE{
        @Override
        int apply(int pieceCount, int die1, int die2, MessageFormatter formatter) {
            formatter.addAction("bridge");
            return BRIDGE_COUNT;
        }
    },
    GOOSE{
        @Override
        int apply(int pieceCount, int die1, int die2, MessageFormatter formatter) {
            int count = (pieceCount + die1 + die2);
            int index = 1;
            formatter.addAction("goose");
            formatter.addToMove(0, pieceCount);

            for (; index < LIMIT; index++) {
                if (isGoose(count)) {
                    formatter.addToMove(index, count);
                    count += (die1 + die2);
                } else break;
            }
            formatter.setGooseCount(index);
            return count;
        }
    };

    abstract int apply(int pieceCount, int die1, int die2, MessageFormatter formatter);

}
