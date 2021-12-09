package chess;

import java.awt.Point;
import java.util.ArrayList;

import chess.memento.PieceMemento;
import chess.ui.PieceView;

public class ChessPiece {

    // Position de la pièce sur l'échiquier
    private int gridPosX;
    private int gridPosY;

    private int type;
    private int color;

    private PieceView pieceView;

    // Pour créer des pièces à mettre sur les cases vides
    public ChessPiece(int x, int y, ChessBoard b) {
        type = ChessUtils.TYPE_NONE;
        color = ChessUtils.COLORLESS;
        gridPosX = x;
        gridPosY = y;

        pieceView = new PieceView(b);
    }

    // Création d'une pièce normale. La position algébrique en notation d'échecs
    // lui donne sa position sur la grille.
    public ChessPiece(String name, String pos, ChessBoard b) {
        color = ChessUtils.getColor(name);
        type = ChessUtils.getType(name);

        pieceView = new PieceView(color, type, b);

        setAlgebraicPos(pos);
    }

    //Change la position avec la notation algébrique
    public void setAlgebraicPos(String pos) {
        Point pos2d = ChessUtils.convertAlgebraicPosition(pos);

        gridPosX = pos2d.x;
        gridPosY = pos2d.y;
    }

    // Crée la liste de pièces avec leur position de départ pour un jeu d'échecs standard
    public static ArrayList<ChessPiece> createInitialPieces(ChessBoard board) {

        ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();

        pieces.add(new ChessPiece("wr", "a1", board));
        pieces.add(new ChessPiece("wr", "h1", board));
        pieces.add(new ChessPiece("wn", "b1", board));
        pieces.add(new ChessPiece("wn", "g1", board));
        pieces.add(new ChessPiece("wb", "c1", board));
        pieces.add(new ChessPiece("wb", "f1", board));
        pieces.add(new ChessPiece("wq", "d1", board));
        pieces.add(new ChessPiece("wk", "e1", board));

        for (int i = 0; i < 8; i++) {
            pieces.add(new ChessPiece("wp", ((char) ('a' + i)) + "2", board));
        }

        pieces.add(new ChessPiece("br", "a8", board));
        pieces.add(new ChessPiece("br", "h8", board));
        pieces.add(new ChessPiece("bn", "b8", board));
        pieces.add(new ChessPiece("bn", "g8", board));
        pieces.add(new ChessPiece("bb", "c8", board));
        pieces.add(new ChessPiece("bb", "f8", board));
        pieces.add(new ChessPiece("bq", "d8", board));
        pieces.add(new ChessPiece("bk", "e8", board));

        for (int i = 0; i < 8; i++) {
            pieces.add(new ChessPiece("bp", ((char) ('a' + i)) + "7", board));
        }

        return pieces;
    }

    //Pour savoir si c'est une pièce vide (pour les cases vides de l'échiquier).
    public boolean isNone() {
        return type == ChessUtils.TYPE_NONE;
    }

    public PieceView getUI() {
        return pieceView;
    }

    public int getType() {
        return type;
    }

    public int getColor() {
        return color;
    }

    public int getGridX() {
        return gridPosX;
    }

    public int getGridY() {
        return gridPosY;
    }

    public void setGridPos(Point pos) {
        gridPosX = pos.x;
        gridPosY = pos.y;
    }

    public boolean verifyMove(Point startPos, Point endPos) {
        int deltaX = startPos.x - endPos.x;
        int deltaY = startPos.y - endPos.y;

        switch (type) {
            case ChessUtils.TYPE_PAWN: {
                if (deltaX != 0) return false;

                if (color == ChessUtils.WHITE)
                    return deltaY == 1 || deltaY == 2 && startPos.y == 6;
                else if (color == ChessUtils.BLACK)
                    return deltaY == -1 || deltaY == -2 && startPos.y == 1;

                return false;
            }
            case ChessUtils.TYPE_KNIGHT: {
                return Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2 ||
                        Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1;
            }
            case ChessUtils.TYPE_BISHOP: {
                return Math.abs(deltaX) == Math.abs(deltaY);
            }
            case ChessUtils.TYPE_ROOK: {
                return deltaX == 0 ^ deltaY == 0;
            }
            case ChessUtils.TYPE_QUEEN: {
                return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0 ^ deltaY == 0;
            }
            case ChessUtils.TYPE_KING: {
                return Math.abs(deltaX) < 2 && Math.abs(deltaY) < 2;
            }
            case ChessUtils.TYPE_NONE:
            default:
                return false;
        }
    }

    public PieceMemento createMemento(){
        return new PieceMemento(this);
    }

    public void restoreMemento(PieceMemento m, ChessBoard b) {
        ChessPiece p = makePieceString(m, b);

        pieceView = p.pieceView;
        gridPosX = p.gridPosX;
        gridPosY = p.gridPosY;
        type = p.type;
        color = p.color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece piece = (ChessPiece) o;
        return gridPosX == piece.gridPosX &&
                gridPosY == piece.gridPosY &&
                type == piece.type &&
                color == piece.color;
    }
}
