package chess.memento;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ui.BoardView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BoardMemento {

    final PieceMemento[][] listPieces;

    public BoardMemento(PieceMemento[][] pieces) {
        listPieces = pieces;
    }

    //Fonctions de lecture et de sauvegarde d'échiquier dans des fichiers. À implanter.
    public static ChessBoard readFromFile(String fileName) throws Exception {
        return readFromFile(new File(fileName), 0, 0);
    }

    public static ChessBoard readFromFile(File file, int x, int y) throws Exception {
        ChessBoard c = new ChessBoard(x, y);
        Scanner s = new Scanner(file);

        while (s.hasNext()) {
            c.putPiece(PieceMemento.readFromStream(s.next(), c));
        }

        return c;
    }

    public void saveToFile(FileWriter fw){
        for (PieceMemento[] len : listPieces) {
            for (PieceMemento p : len) {
                if (p != null)
                    p.saveToStream(fw);
            }
        }
    }

    public PieceMemento[][] getListPieces() {
        return listPieces;
    }

}
