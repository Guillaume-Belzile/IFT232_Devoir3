package chess;

import java.awt.*;
import java.io.File;

public class ChessGame {

    //Position de l'échiquier dans la fenêtre
    private int boardPosX = 200;
    private int boardPosY = 100;

    //Planche de jeu (incluant les pi�ces)
    private ChessBoard board;

    private BoardMemento initialState;

    public ChessGame() {
        board = new ChessBoard(boardPosX, boardPosY);
    }

    public ChessBoard getboard() {
        return board;
    }

    public void resetBoard() {
        board = new ChessBoard(boardPosX, boardPosY);
    }

    public void saveBoard(String path) throws Exception {
        board.saveToFile(new File(path));
    }

    //Charge une planche de jeu à partir d'un fichier.
    public void loadBoard(String path) throws Exception {
        board = ChessBoard.readFromFile(new File(path), boardPosX, boardPosY);
    }

    public void movePiece(String move) {
        board.move(new ChessMove(move, getBoard().createMemento()));
        initialState = board.createMemento();
    }

    public boolean compareBoard(ChessBoard o) {
        return board.equals(o);
    }
}
