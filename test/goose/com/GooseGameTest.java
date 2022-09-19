package goose.com;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GooseGameTest {
    private GooseGame gooseGame;

    @BeforeEach
    void setup(){
        gooseGame = new GooseGame();
    }

    @AfterEach
    void tearDown(){
        gooseGame = null;
    }

    @Test
    void testGooseGameHasBoardWhenInitialized(){
        assertNotNull(gooseGame.getBoard());
        assertEquals(63, gooseGame.getBoard().length);
    }

    @Test
    void testGooseGameBoardIsEmptyWhenInitialized(){
        String [] board = gooseGame.getBoard();
        for (String column : board){
            assertNull(column);
        }
    }

    @Test
    void testAPlayCanBeAddedToGooseGame(){
        String playerName = "Pippo";
        boolean isAdded = gooseGame.addPlayer(playerName);
        assertTrue(isAdded);
        assertEquals(1, gooseGame.getPlayers().size());
    }

    @Test
    void testMultiplePlayerCanBeAddedToGooseGame(){
        String player1 = "Pippo";
        String player2 = "Pluto";
        gooseGame.addPlayer(player1);
        gooseGame.addPlayer(player2);
        assertEquals(2, gooseGame.getPlayers().size());
    }

    @Test
    void testCannotCreateDuplicatePlayer(){
        gooseGame.addPlayer("Pippo");
        boolean isAdded = gooseGame.addPlayer("Pippo");
        assertFalse(isAdded);
        assertEquals(1, gooseGame.getPlayers().size());
    }

    @Test
    void testGooseGamePlayerCanMoveOnTheBoard(){
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        int dice1 = 3;
        int dice2 = 2;
        gooseGame.makeMove(playerName, dice1, dice2);
        assertEquals("pippo", gooseGame.getBoard()[5]);
        assertEquals(5, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPieceCount());
    }

    @Test
    void testMultiplePlayerCanMoveOnTheBoard(){
        //First Player
        String player1 = "Pippo";
        gooseGame.addPlayer(player1);
        gooseGame.makeMove(player1, 3, 2);
        assertEquals("pippo", gooseGame.getBoard()[5]);
        assertEquals(5, gooseGame.getPlayers().get(player1.toLowerCase()).getPreviousPieceCount());

        //Second Player
        String player2 = "Pluto";
        gooseGame.addPlayer(player2);
        gooseGame.makeMove(player2, 4, 4);
        assertEquals("pluto", gooseGame.getBoard()[8]);
        assertEquals(8, gooseGame.getPlayers().get(player2.toLowerCase()).getPreviousPieceCount());
    }

    @Test
    void testGooseGameThrowExceptionWhenPlayerIsNotCreatedButMakeAMove(){
        String playerName = "Pippo";
        int dice1 = 3;
        int dice2 = 2;
        assertThrows(IllegalArgumentException.class,
                ()-> gooseGame.makeMove(playerName, dice1, dice2));
    }

    @Test
    void testGooseGamePlayerCanWin(){
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        int dice1 = 60;
        int dice2 = 2;
        gooseGame.makeMove(playerName, dice1, dice2);
        assertEquals("pippo", gooseGame.getBoard()[62]);
        assertTrue(gooseGame.isWin());
    }

    @Test
    void testWhenGooseGamePlayerMoveToTheBridgeThePlayerJumpToSpace12(){
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        int dice1 = 1;
        int dice2 = 5;
        gooseGame.makeMove(playerName, dice1, dice2);
        assertEquals("pippo", gooseGame.getBoard()[11]);
        assertEquals(6, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPieceCount());
    }
}