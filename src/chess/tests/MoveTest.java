package chess.tests;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MoveTest {
    @Test
    public void testBasicCollision() throws Exception {
        ChessGame game = new ChessGame();
        game.loadBoard("boards/normalStart");
        ChessBoard result = ChessBoard.readFromFile("boards/normalStart");
        //Move tower over a pawn of the same color
        game.movePiece("a1-a2");
        assertTrue(game.compareBoard(result));
    }

}
