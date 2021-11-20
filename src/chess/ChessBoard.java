package chess;

import chess.ui.BoardView;
import javafx.geometry.Point2D;
import javafx.scene.Node;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

//Représente la planche de jeu avec les pièces.

public class ChessBoard {

    // Grille de jeu 8x8 cases. Contient des références aux piéces présentes sur
    // la grille.
    // Lorsqu'une case est vide, elle contient une pièce spéciale
    // (type=ChessPiece.NONE, color=ChessPiece.COLORLESS).
    private final ChessPiece[][] grid;

    private final BoardView boardView;

    public ChessBoard(int x, int y) {
        // Initialise la grille avec des pièces vides.
        grid = new ChessPiece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = new ChessPiece(i, j, this);
            }
        }

        boardView = new BoardView(x, y);
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
        grid[x][y] = new ChessPiece(x, y, this);
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
        move(ChessUtils.convertAlgebraicPosition(start), ChessUtils.convertAlgebraicPosition(end));
    }

    //Effectue un mouvement sur l'échiqier. Quelques règles de base sont implantées ici.
    public boolean move(Point startPos, Point endPos) {
        ChessPiece toMove = getPiece(startPos);

        if (!toMove.verifyMove(startPos, endPos)) {
            return false;
        }

        //Vérifie si les coordonnées sont valides
        if (!isValid(endPos))
            return false;

            //Si la case destination est vide, on peut faire le mouvement
        else if (isEmpty(endPos)) {
            ChessPiece piece = getPiece(startPos);
            assignSquare(endPos, piece);
            clearSquare(startPos);
            return true;
        }

        //Si elle est occuppé par une pièce de couleur différente, alors c'est une capture
        else if (!isSameColor(startPos, endPos)) {
            ChessPiece piece = getPiece(startPos);
            removePiece(endPos);
            assignSquare(endPos, piece);
            clearSquare(startPos);
            return true;
        }

        return false;
    }

    public void move(Node node, Point2D pos, Point2D newPos) {
        Point gridPos = getUI().paneToGrid(pos.getX(), pos.getY());
        Point newGridPos = getUI().paneToGrid(newPos.getX(), newPos.getY());

        Point2D finalPos;

        if (move(gridPos, newGridPos)) {
            finalPos = getUI().gridToPane(newGridPos.x, newGridPos.y);
        } else {
            finalPos = getUI().gridToPane(gridPos.x, gridPos.y);
        }

        node.relocate(finalPos.getX(), finalPos.getY());
    }

    //Fonctions de lecture et de sauvegarde d'échiquier dans des fichiers. À implanter.
    public static ChessBoard readFromFile(String fileName) throws Exception {
        return readFromFile(new File(fileName), 0, 0);
    }

    public static ChessBoard readFromFile(File file, int x, int y) throws Exception {
        ChessBoard c = new ChessBoard(x, y);
        Scanner s = new Scanner(file);

        while (s.hasNext()) {
            c.putPiece(ChessPiece.readFromStream(s.next(), c));
        }

        return c;
    }

    public void saveToFile(File file) throws Exception {
        FileWriter fw = new FileWriter(file);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!grid[i][j].isNone())
                    fw.write(grid[i][j].saveToStream());
            }
        }

        fw.close();
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
}
