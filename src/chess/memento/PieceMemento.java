package chess.memento;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessUtils;

import java.io.FileWriter;
import java.io.IOException;

public class PieceMemento {

    String piece;

    public PieceMemento(ChessPiece p) {
        piece = ChessUtils.makeAlgebraicPosition(p.getGridX(), p.getGridY()) + "-" + ChessUtils.makePieceName(p.getColor(), p.getType());
    }

    public void saveToStream(FileWriter fw) {
        try {
            fw.append(piece).append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ChessPiece readFromStream(String s, ChessBoard c) {
        String[] line = s.split("-");
        String pos = line[0];
        String name = line[1];
        return new ChessPiece(name, pos, c);
    }

    public String getPiece() {
        return piece;
    }
}
