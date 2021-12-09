package chess;

import chess.memento.BoardMemento;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ChessMove {
    private Point end;
    private Point start;
    private BoardMemento boardMemento;

    public ChessMove(Point e, Point s, BoardMemento m){
        end = e;
        start = s;
        boardMemento = m;
    }

    public ChessMove(String s, BoardMemento m) {
        start = ChessUtils.convertAlgebraicPosition(s.split("-")[0]);
        end = ChessUtils.convertAlgebraicPosition(s.split("-")[1]);
        boardMemento = m;
    }

    public static ChessMove loadFromStream(Scanner s, BoardMemento memento) {
        return new ChessMove(s.next(), memento);
    }

    public void saveToStream(FileWriter fw) {
        try {
            fw.append(ChessUtils.makeAlgebraicPosition(start.x, start.y)).append("-").append(ChessUtils.makeAlgebraicPosition(end.x, end.y)).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Point getEnd() {
        return end;
    }

    public Point getStart() {
        return start;
    }

}
