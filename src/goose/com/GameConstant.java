package goose.com;

import java.util.List;

public interface GameConstant {
    List<String> GOOSE_COUNT = List.of("5", "9", "14", "18", "23", "27");
    int BRIDGE_COUNT = 12;
    int WIN_COUNT = 63;
    int LIMIT = 6;

    String MAKE_MOVE = "move";
    String ADD_PLAYER = "add";
    String ROLL_MESSAGE = "rolls";
    String MOVE_MESSAGE = "moves from";
    String WIN_MESSAGE = "Wins!!";
    String BOUNCE_MESSAGE = "bounces!";
    String BRIDGE_MESSAGE = "The Bridge";
    String JUMP_MESSAGE = "jumps";
    String GOOSE_MESSAGE = "The Goose";
    String DEFAULT = "default";
    String RETRY_MESSAGE = "moves again and goes";
    String PRANK_MESSAGE = "who returns to";
}
