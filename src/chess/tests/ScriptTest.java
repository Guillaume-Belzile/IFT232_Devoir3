package chess.tests;

import chess.ChessBoard;
import chess.ChessGame;
import chess.memento.BoardMemento;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

public class ScriptTest {

    private ChessGame game = new ChessGame();

    @Before
    public void setup() {
        game = new ChessGame();
    }

    @Test
    public void KnightsTour() throws Exception {
        ChessBoard res = BoardMemento.readFromFile("boards/saves/testsKnight/knightsTour/KnightsTourResult");

        game.loadBoard("boards/saves/testsKnight/knightsTour/KnightsTourStart");

        Scanner s = new Scanner(new FileInputStream("boards/saves/testsKnight/knightsTour/KnightsTourScript"));
        while (s.hasNextLine()) {
            game.movePiece(s.nextLine());
        }

        assertTrue(game.compareBoard(res));
    }

}
