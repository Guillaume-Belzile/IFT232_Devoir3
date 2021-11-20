package chess;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;

public class ChessGame {

    //Position de l'échiquier dans la fenêtre
    private int boardPosX = 200;
    private int boardPosY = 100;

    //Planche de jeu (incluant les pi�ces)
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard(boardPosX, boardPosY);
    }

    public ChessBoard getboard() {
        return board;
    }

    public void resetBoard() {
        board = new ChessBoard(boardPosX, boardPosY);
    }

    public void saveBoard(String path)throws Exception {
        board.saveToFile(new File(path));
    }

    //Charge une planche de jeu à partir d'un fichier.
    public void loadBoard(String path) throws Exception {
        board = ChessBoard.readFromFile(new File(path), boardPosX, boardPosY);
    }
}
