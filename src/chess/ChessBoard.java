package chess;

import chess.memento.BoardMemento;
import chess.memento.PieceMemento;
import chess.ui.BoardView;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import java.awt.*;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Stack;

//Représente la planche de jeu avec les pièces.

public class ChessBoard {

    // Grille de jeu 8x8 cases. Contient des références aux piéces présentes sur
    // la grille.
    // Lorsqu'une case est vide, elle contient une pièce spéciale
    // (type=ChessPiece.NONE, color=ChessPiece.COLORLESS).
    private ChessPiece[][] grid;

    private BoardView boardView;

    private Stack<ChessMove> pastMoves;

    private BoardMemento initialState;

    public ChessBoard(int x, int y) {
        initialize();

        initialState = createMemento();

        boardView = new BoardView(x, y);

        pastMoves = new Stack<>();
    }

    public ChessBoard(BoardMemento m) {
        restoreMemento(m, this);
    }

    public BoardMemento getState() {
        return initialState;
    }

    public void setState(BoardMemento initialState) {
        this.initialState = initialState;
    }

    public void saveToFile(FileWriter fw) {
        initialState.saveToFile(fw);

        ArrayList<ChessMove> moves = new ArrayList<>(pastMoves);

        for (int i = 0; i < moves.size(); i++) {
            moves.get(i).saveToStream(fw);
        }
    }

    private void initialize() {
        // Initialise la grille avec des pièces vides.
        grid = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = new ChessPiece(i, j);
            }
        }
    }

    public void assignSquare(Point gridPos, ChessPiece piece) {
        grid[gridPos.x][gridPos.y] = piece;
        piece.setGridPos(gridPos);
    }

    public void clearSquare(Point gridPos) {
        clearSquare(gridPos.x, gridPos.y);
    }

    // Place une pièce vide dans la case
    public void clearSquare(int x, int y) {
        grid[x][y] = new ChessPiece(x, y);
    }

    public ChessPiece getPiece(Point gridPos) {
        return grid[gridPos.x][gridPos.y];
    }

    // Place une pièce sur le planche de jeu.
    public void putPiece(ChessPiece piece) {
        Point2D pos = boardView.gridToPane(piece.getGridX(), piece.getGridY());
        piece.getUI().getPane().relocate(pos.getX(), pos.getY());
        getUI().getPane().getChildren().add(piece.getUI().getPane());
        grid[piece.getGridX()][piece.getGridY()] = piece;
    }

    public void removePiece(Point gridPos) {
        ChessPiece piece = getPiece(gridPos);
        getUI().getPane().getChildren().remove(piece.getUI().getPane());
        clearSquare(gridPos);
    }

    public BoardView getUI() {
        return boardView;
    }

    //Les cases vides contiennent une pièce spéciale
    public boolean isEmpty(Point pos) {
        return (grid[pos.x][pos.y].getType() == ChessUtils.TYPE_NONE);
    }

    //Vérifie si une coordonnée dans la grille est valide
    public boolean isValid(Point pos) {
        return (pos.x >= 0 && pos.x <= 7 && pos.y >= 0 && pos.y <= 7);
    }

    //Vérifie si les pièces à deux positions dans la grille sont de la même couleur.
    public boolean isSameColor(Point pos1, Point pos2) {
        return grid[pos1.x][pos1.y].getColor() == grid[pos2.x][pos2.y].getColor();
    }

    //Effectue un mouvement à partir de la notation algébrique des cases ("e2-b5" par exemple)
    public void algebraicMove(String move) {
        if (move.length() != 5) {
            throw new IllegalArgumentException("Badly formed move");
        }
        String start = move.substring(0, 2);
        String end = move.substring(3, 5);
        move(new ChessMove(ChessUtils.convertAlgebraicPosition(start), ChessUtils.convertAlgebraicPosition(end), createMemento()));
    }

    //Effectue un mouvement sur l'échiqier. Quelques règles de base sont implantées ici.
    public boolean move(ChessMove cm) {
        ChessPiece toMove = getPiece(cm.getStart());

        if (!toMove.verifyMove(cm.getStart(), cm.getEnd())) {
            return false;
        }

        //Vérifie si les coordonnées sont valides
        if (!isValid(cm.getEnd()))
            return false;

            //Si la case destination est vide, on peut faire le mouvement
        else if (isEmpty(cm.getEnd())) {
            ChessPiece piece = getPiece(cm.getStart());
            assignSquare(cm.getEnd(), piece);
            clearSquare(cm.getStart());
            return true;
        }

        //Si elle est occuppé par une pièce de couleur différente, alors c'est une capture
        else if (!isSameColor(cm.getStart(), cm.getEnd())) {
            ChessPiece piece = getPiece(cm.getStart());
            removePiece(cm.getEnd());
            assignSquare(cm.getEnd(), piece);
            clearSquare(cm.getStart());


            return true;
        }

        return false;
    }

    public boolean move(Node node, Point2D pos, Point2D newPos) {
        Point gridPos = getUI().paneToGrid(pos.getX(), pos.getY());
        Point newGridPos = getUI().paneToGrid(newPos.getX(), newPos.getY());

        Point2D finalPos;
        ChessMove m = new ChessMove(boardView.paneToGrid(pos.getX(), pos.getY()), boardView.paneToGrid(newPos.getX(), newPos.getY()), createMemento());
        boolean res = move(m);

        if (move(m)) {
            finalPos = getUI().gridToPane(newGridPos.x, newGridPos.y);
            pastMoves.add(m);
        } else {
            finalPos = getUI().gridToPane(gridPos.x, gridPos.y);
        }

        node.relocate(finalPos.getX(), finalPos.getY());

        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard board = (ChessBoard) o;

        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (!grid[i][j].equals(board.grid[i][j]))
                    return false;

        return true;
    }

    public BoardMemento createMemento() {
        PieceMemento[][] mementos = new PieceMemento[8][8];

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++) {
                ChessPiece p = grid[i][j];
                if (!p.isNone())
                mementos[i][j] = new PieceMemento(grid[i][j]);
            }
        }
            
        return new BoardMemento(mementos);
    }

    public void restoreMemento(BoardMemento m, ChessBoard b) {
        for (ChessPiece[] pieces : grid) {
            for (ChessPiece p : pieces) {
                if (p != null)
                    removePiece(new Point(p.getGridX(), p.getGridY()));
            }
        }

        initialize();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                ChessPiece p = grid[i][j];

                if (m.getListPieces() != null) {
                    p.restoreMemento(m.getListPieces()[i][j], this);

                    Point2D pos = boardView.gridToPane(p.getGridX(), p.getGridY());

                    p.getUI().getPane().relocate(pos.getX(), pos.getY());
                }
            }
        }
    }



}
