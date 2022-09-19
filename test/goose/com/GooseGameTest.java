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
    void testThatGooseGameHasBoardWhenInitialized(){
        assertNotNull(gooseGame.getBoard());
        assertEquals(63, gooseGame.getBoard().length);
    }

    @Test
    void testThatGooseGameBoardIsEmptyWhenInitialized(){
        String [] board = gooseGame.getBoard();
        for (String column : board){
            assertNull(column);
        }
    }

    @Test
    void testThatAPlayCanBeAddedToGooseGame() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);

        String expectedMessage = "players: pippo";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals(1, gooseGame.getPlayers().size());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testThatMultiplePlayerCanBeAddedToGooseGame() throws GooseGameException {
        String player1 = "Pippo";
        String player2 = "Pluto";
        gooseGame.addPlayer(player1);
        gooseGame.addPlayer(player2);

        String expectedMessage = "players: pippo, pluto";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals(2, gooseGame.getPlayers().size());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void throwExceptionWhenDuplicatePlayerNameIsAdded() throws GooseGameException {
        gooseGame.addPlayer("Pippo");
        Exception exception = assertThrows(GooseGameException.class,
                () ->   gooseGame.addPlayer("Pippo"));

        String expectedMessage = "Pippo: already existing player";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void throwExceptionWhenUnregisteredPlayerMakeMove(){
        Exception exception = assertThrows(GooseGameException.class,
                () ->   gooseGame.makeMove("Pippo", 2, 5));

        String expectedMessage = "pippo: is not a player";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testThatPlayerCanMoveOnTheBoard() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        gooseGame.makeMove(playerName, 2, 2);

        String expectedMessage = "pippo rolls 2, 2. pippo moves from Start to 4";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals("pippo", gooseGame.getBoard()[3]);
        assertEquals(4, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testThatMultiplePlayerCanMoveOnTheBoard() throws GooseGameException {
        //First Player
        String player1 = "Pippo";
        gooseGame.addPlayer(player1);
        gooseGame.makeMove(player1, 1, 2);
        assertEquals("pippo", gooseGame.getBoard()[2]);
        assertEquals(3, gooseGame.getPlayers().get(player1.toLowerCase()).getPreviousPosition());
        assertEquals("pippo rolls 1, 2. pippo moves from Start to 3", gooseGame.getMessageLogger().toString());

        //Second Player
        String player2 = "Pluto";
        gooseGame.addPlayer(player2);
        gooseGame.makeMove(player2, 4, 4);
        assertEquals("pluto", gooseGame.getBoard()[7]);
        assertEquals(8, gooseGame.getPlayers().get(player2.toLowerCase()).getPreviousPosition());
        assertEquals("pluto rolls 4, 4. pluto moves from Start to 8", gooseGame.getMessageLogger().toString());
    }


    @Test
    void testThatWhenPlayerMoveToTheBridge_thenPlayerMoveToSpaceTwelveOnBoard() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        gooseGame.makeMove(playerName, 5, 1);

        String expectedMessage = "pippo rolls 5, 1. pippo moves from Start to The Bridge. pippo jumps to 12";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals("pippo", gooseGame.getBoard()[11]);
        assertEquals(12, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals(expectedMessage, actualMessage);
    }


    @Test
    void testThatWhenPlayerMoveTOTheGoose_thenPlayerMoveToTheSumOfTwoDiceRolledBefore() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        gooseGame.makeMove(playerName, 1, 2);
        gooseGame.makeMove(playerName, 1, 1);

        assertEquals("pippo", gooseGame.getBoard()[6]);
        assertEquals(7, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals("pippo rolls 1, 1. pippo moves from 3 to 5, The Goose. pippo moves again and goes to 7", gooseGame.getMessageLogger().toString());

        gooseGame.makeMove(playerName, 1, 1);
        assertEquals("pippo", gooseGame.getBoard()[10]);
        assertEquals(11, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals("pippo rolls 1, 1. pippo moves from 7 to 9, The Goose. pippo moves again and goes to 11", gooseGame.getMessageLogger().toString());


        gooseGame.makeMove(playerName, 2, 1);
        assertEquals("pippo", gooseGame.getBoard()[16]);
        assertEquals(17, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals("pippo rolls 2, 1. pippo moves from 11 to 14, The Goose. pippo moves again and goes to 17", gooseGame.getMessageLogger().toString());
    }

    @Test
    void testThatPlayerMoveOnTheGooseMultipleTimes_thenPlayerIsMoveMultipleTimes() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        gooseGame.makeMove(playerName, 5, 5);
        gooseGame.makeMove(playerName, 2, 2);

        String expectedMessage = "pippo rolls 2, 2. pippo moves from 10 to 14, The Goose. pippo moves again and goes to 18, The Goose. pippo moves again and goes to 22";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals("pippo", gooseGame.getBoard()[21]);
        assertEquals(22, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testThatWhenAPlayerLandOnSpaceOccupiedByAnotherPlayer_theSendPlayerToPreviousPosition() throws GooseGameException {
        //First Player
        String player1 = "Pippo";
        gooseGame.addPlayer(player1);
        gooseGame.makeMove(player1, 1, 2);
        assertEquals("pippo", gooseGame.getBoard()[2]);
        assertEquals(3, gooseGame.getPlayers().get(player1.toLowerCase()).getPreviousPosition());
        assertEquals("pippo rolls 1, 2. pippo moves from Start to 3", gooseGame.getMessageLogger().toString());

        //Second Player
        String player2 = "Pluto";
        gooseGame.addPlayer(player2);
        gooseGame.makeMove(player2, 1, 2);
        assertEquals("pluto", gooseGame.getBoard()[2]);
        assertEquals(3, gooseGame.getPlayers().get(player2.toLowerCase()).getPreviousPosition());
        assertEquals("pluto rolls 1, 2. pluto moves from Start to 3. On 3 there is pippo, who returns to 0", gooseGame.getMessageLogger().toString());
    }
    // bounce

    @Test
    void testThatPlayerMustWinWithExactDiceShoot() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        gooseGame.makeMove(playerName, 5, 5);
        gooseGame.makeMove(playerName, 2, 2);
        gooseGame.makeMove(playerName, 6, 6);
        gooseGame.makeMove(playerName, 6, 6);
        gooseGame.makeMove(playerName, 6, 6);
        gooseGame.makeMove(playerName, 6, 6);

        String expectedMessage = "pippo rolls 6, 6. pippo moves from 58 to 63. pippo bounces! pippo returns to 59";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals("pippo", gooseGame.getBoard()[58]);
        assertEquals(59, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testThatPlayerCanWin() throws GooseGameException {
        String playerName = "Pippo";
        gooseGame.addPlayer(playerName);
        gooseGame.makeMove(playerName, 5, 5);
        gooseGame.makeMove(playerName, 2, 2);
        gooseGame.makeMove(playerName, 6, 6);
        gooseGame.makeMove(playerName, 6, 6);
        gooseGame.makeMove(playerName, 6, 6);
        gooseGame.makeMove(playerName, 2, 3);

        String expectedMessage = "pippo rolls 2, 3. pippo moves from 58 to 63. pippo Wins!!";
        String actualMessage = gooseGame.getMessageLogger().toString();

        assertEquals("pippo", gooseGame.getBoard()[62]);
        assertEquals(63, gooseGame.getPlayers().get(playerName.toLowerCase()).getPreviousPosition());
        assertEquals(expectedMessage, actualMessage);
    }
    //win
}