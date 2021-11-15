package chess;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

//Représente la planche de jeu avec les pièces.


public class ChessBoard {

	// Les cases ont un peu plus de 80 pixels
	public static final double squareSize = 80.3;

	// La bordure fait 80 pixels tout le tour
	public static final int borderSize = 80;

	// L'arrière plan est dans une fen�tre 800x800.
	public static final int sceneSize = 800;

	// La bordure fait 80 pixels tout le tour
	public static final int pieceDeltaX = 12;
	// La bordure fait 80 pixels tout le tour
	public static final int pieceDeltaY = 2;

	// Grille de jeu 8x8 cases. Contient des références aux piéces présentes sur
	// la grille.
	// Lorsqu'une case est vide, elle contient une pièce spéciale
	// (type=ChessPiece.NONE, color=ChessPiece.COLORLESS).
	private ChessPiece[][] grid;

	// Panneau d'interface représentant la planche de jeu
	private Pane boardPane;
	private ImageView boardView;
	private double startX;
	private double startY;

	public ChessBoard(int x, int y) {

		startX = x;
		startY = y;

		// Initialise la grille avec des pièces vides.
		grid = new ChessPiece[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				grid[i][j] = new ChessPiece(i, j, this);
			}
		}

		// Création de l'arrière-plan.
		Image boardImage;
		try {
			boardImage = new Image(new FileInputStream("images/board.jpg"));

		} catch (FileNotFoundException e) {

			return;
		}

		boardView = new ImageView(boardImage);

		boardView.setX(x);
		boardView.setY(y);

		boardView.setFitHeight(sceneSize);
		boardView.setFitWidth(sceneSize);

		boardView.setPreserveRatio(true);

		boardPane = new Pane(boardView);
	}

	// Place une pièce vide dans la case
	public void clearSquare(int x, int y) {
		grid[x][y] = new ChessPiece(x, y, this);
	}

	// Place une pièce sur le planche de jeu.
	public void putPiece(ChessPiece piece) {

		Point2D pos = gridToPane(piece, piece.getGridX(), piece.getGridY());
		piece.getPane().relocate(pos.getX(), pos.getY());
		getPane().getChildren().add(piece.getPane());
		grid[piece.getGridX()][piece.getGridY()] = piece;
	}

	public Point2D gridToPane(ChessPiece piece, int x, int y) {

		if (x < 0 || x > 7 || y < 0 || y > 7)
			throw new IllegalArgumentException("Piece out of grid: (" + x + "," + y + ")");

		return new Point2D(startX + x * squareSize + borderSize + pieceDeltaX,
				startY + y * squareSize + borderSize + pieceDeltaY);

	}

	public Pane getPane() {
		return boardPane;
	}

	//Convertit des coordonnées en pixels sur la fenêtre d'interface en coordonnées dans la grille de l'échiquier
	//Utilisé pour détecter qu'on a touché une case spécifique de la grille.
	public Point paneToGrid(double xPos, double yPos) {
		if (xPos < (borderSize + startX))
			xPos = borderSize + startX;
		if (xPos > startX + sceneSize - borderSize)
			xPos = startX + sceneSize - borderSize - (squareSize / 2);
		if (yPos < borderSize + startY)
			yPos = borderSize + startY;
		if (yPos > startY + sceneSize - borderSize)
			yPos = startY + sceneSize - borderSize - (squareSize / 2);
		int xGridPos = (int) ((xPos - (startX + borderSize)) / squareSize);
		int yGridPos = (int) ((yPos - (startY + borderSize)) / squareSize);
		return new Point(xGridPos, yGridPos);
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
	public void algebraicMove(String move){
		if(move.length()!=5){
			throw new IllegalArgumentException("Badly formed move");
		}
		String start = move.substring(0,2);
		String end = move.substring(3,5);
		move(ChessUtils.convertAlgebraicPosition(start),ChessUtils.convertAlgebraicPosition(end));
	}
	
	//Effectue un mouvement sur l'échiqier. Quelques règles de base sont implantées ici.
	public boolean move(Point gridPos, Point newGridPos) {

		//Vérifie si les coordonnées sont valides
		if (!isValid(newGridPos))
			return false;

		//Si la case destination est vide, on peut faire le mouvement
		else if (isEmpty(newGridPos)) {
			grid[newGridPos.x][newGridPos.y] = grid[gridPos.x][gridPos.y];
			grid[gridPos.x][gridPos.y] = new ChessPiece(gridPos.x, gridPos.y, this);
			return true;
		}

		//Si elle est occuppé par une pièce de couleur différente, alors c'est une capture
		else if (!isSameColor(gridPos, newGridPos)) {			
			getPane().getChildren().remove(grid[newGridPos.x][newGridPos.y].getPane());
			grid[newGridPos.x][newGridPos.y] = grid[gridPos.x][gridPos.y];
			grid[gridPos.x][gridPos.y] = new ChessPiece(gridPos.x, gridPos.y, this);

			return true;
		}

		return false;
	}
	//Fonctions de lecture et de sauvegarde d'échiquier dans des fichiers. À implanter.
	
	public static ChessBoard readFromFile(String fileName) throws Exception {
		return readFromFile(new File(fileName), 0, 0);
	}

	public static ChessBoard readFromFile(File file, int x, int y) throws Exception {
		

		throw new Exception("Pas implanté");
	}
	
	
	public void saveToFile(File file) throws Exception {

		throw new Exception("Pas implanté");
	}

}
