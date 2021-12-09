package chess.tests;

import chess.ChessBoard;
import chess.ChessGame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MoveTest {

    private ChessGame game = new ChessGame();

    @Before
    public void setup() {
        game = new ChessGame();
    }

    @Test
    public void testBasicCollision() throws Exception {
        game.loadBoard("boards/normalStart");
        ChessBoard result = ChessBoard.readFromFile("boards/normalStart");
        //Move tower over a pawn of the same color
        game.movePiece("a1-a2");
        assertTrue(game.compareBoard(result));
    }

    @Test
    public void testMoveBishop() throws Exception {
        game.loadBoard("boards/saves/testsBishop/bishopBasic");

        ChessBoard result = ChessBoard.readFromFile("boards/saves/testsBishop/apresdeplacement/bishopbasic1");
        game.movePiece("e4-c6");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsBishop/apresdeplacement/bishopbasic2");
        game.movePiece("c6-b5");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsBishop/apresdeplacement/bishopbasic3");
        game.movePiece("b5-e2");
        assertTrue(game.compareBoard(result));

        game.movePiece("e2-b3");
        assertTrue(game.compareBoard(result));

        game.movePiece("e2-e6");
        assertTrue(game.compareBoard(result));
    }

    @Test
    public void testMoveKing() throws Exception {
        game.loadBoard("boards/saves/testsKing/kingBasic");

        ChessBoard result = BoardMemento.readFromFile("boards/saves/testsKing/apresdeplacement/kingbasic1");
        game.movePiece("e4-d5");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsKing/apresdeplacement/kingbasic2");
        game.movePiece("d5-d4");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsKing/apresdeplacement/kingbasic3");
        game.movePiece("d4-e3");
        assertTrue(game.compareBoard(result));

        game.movePiece("e3-e5");
        assertTrue(game.compareBoard(result));
    }

    @Test
    public void testMoveKnight() throws Exception {
        game.loadBoard("boards/saves/testsKnight/knightBasic");

        ChessBoard result = BoardMemento.readFromFile("boards/saves/testsKnight/apresdeplacement/knightbasic1");
        game.movePiece("e4-d6");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsKnight/apresdeplacement/knightbasic2");
        game.movePiece("d6-c4");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsKnight/apresdeplacement/knightbasic3");
        game.movePiece("c4-e3");
        assertTrue(game.compareBoard(result));

        game.movePiece("e3-e1");
        assertTrue(game.compareBoard(result));

        game.movePiece("e3-d4");
        assertTrue(game.compareBoard(result));
    }

    @Test
    public void testMovePawn() throws Exception {
        game.loadBoard("boards/saves/testsPawn/pawnBasic");

        ChessBoard result = ChessBoard.readFromFile("boards/saves/testsPawn/apresdeplacement/pawnbasic1");
        game.movePiece("e2-e4");
        assertTrue(game.compareBoard(result));

        result = ChessBoard.readFromFile("boards/saves/testsPawn/apresdeplacement/pawnbasic2");
        game.movePiece("e4-e5");
        assertTrue(game.compareBoard(result));

        game.movePiece("e5-e4");
        assertTrue(game.compareBoard(result));

        game.movePiece("e5-e7");
        assertTrue(game.compareBoard(result));

        game.movePiece("e5-d5");
        assertTrue(game.compareBoard(result));
    }

    @Test
    public void testMoveQueen() throws Exception {
        game.loadBoard("boards/saves/testsQueen/queenBasic");

        ChessBoard result = ChessBoard.readFromFile("boards/saves/testsQueen/apresdeplacement/queenbasic1");
        game.movePiece("e4-a8");
        assertTrue(game.compareBoard(result));

        result = ChessBoard.readFromFile("boards/saves/testsQueen/apresdeplacement/queenbasic2");
        game.movePiece("a8-a3");
        assertTrue(game.compareBoard(result));

        result = ChessBoard.readFromFile("boards/saves/testsQueen/apresdeplacement/queenbasic3");
        game.movePiece("a3-e3");
        assertTrue(game.compareBoard(result));

        result = ChessBoard.readFromFile("boards/saves/testsQueen/apresdeplacement/queenbasic4");
        game.movePiece("e3-f3");
        assertTrue(game.compareBoard(result));

        game.movePiece("f3-e5");
        assertTrue(game.compareBoard(result));
    }

    @Test
    public void testMoveRook() throws Exception {
        game.loadBoard("boards/saves/testsRook/rookBasic");

        ChessBoard result = ChessBoard.readFromFile("boards/saves/testsRook/apresdeplacement/rookbasic1");
        game.movePiece("e4-e8");
        assertTrue(game.compareBoard(result));

        result = ChessBoard.readFromFile("boards/saves/testsRook/apresdeplacement/rookbasic2");
        game.movePiece("e8-c8");
        assertTrue(game.compareBoard(result));

        result = BoardMemento.readFromFile("boards/saves/testsRook/apresdeplacement/rookbasic3");
        game.movePiece("c8-c3");
        assertTrue(game.compareBoard(result));

        game.movePiece("c3-d4");
        assertTrue(game.compareBoard(result));
    }
}
