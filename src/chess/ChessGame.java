package chess;

import chess.memento.BoardMemento;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

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

    public ChessBoard getBoard() {
        return board;
    }

    public void resetBoard() {
        board = new ChessBoard(boardPosX, boardPosY);
    }

    public void saveBoard(String path) throws Exception {
        board.createMemento().saveToFile(new FileWriter(path));
    }

    //Charge une planche de jeu à partir d'un fichier.
    public void loadBoard(String path) throws Exception {
        board = BoardMemento.readFromFile(new File(path), boardPosX, boardPosY);
    }

    public void movePiece(String move) {
        board.move(new ChessMove(move, getBoard().createMemento()));
        initialState = board.createMemento();
    }

    public boolean compareBoard(ChessBoard o) {
        return board.equals(o);
    }
}
