package chess;

import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ChessGame extends Application {

	
	//Taille de la fenêtre
	private int gameSizeX = 1200;
	private int gameSizeY = 1000;
	
	//Position de l'échiquier dans la fenêtre
	private int boardPosX = 200;
	private int boardPosY = 100;

	//Objet racine de l'interface graphique
	private Scene scene;

	//Dialogue utilis� pour choisir des noms de fichiers
	private FileChooser fileDialog;

	//Panneau principal dans lequel se trouvent les éléments de jeu
	private Pane gamePane;

	//Planche de jeu (incluant les pi�ces)
	private ChessBoard board;

	
	//Méthode de démarrage standard pour une application JavaFX. 
	//L'initialisation de l'interface graphique se fait ici.
	@Override
	public void start(Stage stage) {

		//Création des boutons et des actions correspondantes.
		//Les actions sont exprimées sous forme de lambda-expressions (Java 8)
				
		fileDialog = new FileChooser();
		gamePane = new Pane();

		//Charge la planche de jeu par d�faut
		resetGame();

		//Bouton de redémarrage (recharge la planche de jeu par défaut)
		Button resetButton = new Button("Reset");
		resetButton.setLayoutX(250);
		resetButton.setLayoutY(50);
		gamePane.getChildren().add(resetButton);

		resetButton.setOnAction(event -> resetGame());

		
		//Bouton utilisé pour enregistrer les mouvements dnas un script
		Button recordButton = new Button("Record moves");
		recordButton.setLayoutX(325);
		recordButton.setLayoutY(50);
		gamePane.getChildren().add(recordButton);
		recordButton.setOnAction(event -> {

			File file = getSaveFile("Record moves...", "scripts/saves", stage);
			saveScript(file);
		});

		//Bouton utilisé pour sauvegarder la planche de jeu
		Button saveButton = new Button("Save board");
		saveButton.setLayoutX(425);
		saveButton.setLayoutY(50);
		gamePane.getChildren().add(saveButton);

		saveButton.setOnAction(event -> {

			File file = getSaveFile("Save Board...", "boards/saves", stage);
			saveBoard(file);
		});

		//Boîte de sélection utilisée pour activer l'intelligence artificielle
		CheckBox aiBox = new CheckBox("AI player");
		aiBox.setLayoutX(525);
		aiBox.setLayoutY(50);

		gamePane.getChildren().add(aiBox);

		
		//Bouton utilisé pour charger une planche de jeu
		Button loadButton = new Button("Load board");
		loadButton.setLayoutX(625);
		loadButton.setLayoutY(50);
		gamePane.getChildren().add(loadButton);

		loadButton.setOnAction(event -> {

			File file = getOpenFile("Open Board...", "boards", stage);

			resetGame(file);
		});

		
		//Bouton utilisé pour charger et exécuter une ancienne partie
		Button playButton = new Button("Play moves");
		playButton.setLayoutX(725);
		playButton.setLayoutY(50);
		gamePane.getChildren().add(playButton);

		playButton.setOnAction(event -> {
			File file = getOpenFile("Open Script...", "scripts", stage);
			loadScript(file);
		});

		//Bouton undo, utilisé pour défaire le dernier mouvement
		Button undoButton = new Button("Undo");
		undoButton.setLayoutX(825);
		undoButton.setLayoutY(50);
		gamePane.getChildren().add(undoButton);
		
		//Bouton redo, utilisé pour refaire un mouvement défait
		Button redoButton = new Button("Redo");
		redoButton.setLayoutX(900);
		redoButton.setLayoutY(50);
		gamePane.getChildren().add(redoButton);

		
		//Boîte de sélection utilisée pour activer l'édition d'échiquier (déposer manuellement des pièces)
		CheckBox editBox = new CheckBox("Edit board");
		editBox.setLayoutX(200);
		editBox.setLayoutY(950);

		gamePane.getChildren().add(editBox);

		//Étiquette pour la zone de capture des noirs
		Label blackCapture = new Label("Black captures");
		blackCapture.setLayoutX(50);
		blackCapture.setLayoutY(200);
		gamePane.getChildren().add(blackCapture);

		//Étiquette pour la zone de capture des blancs
		Label whiteCapture = new Label("White captures");
		whiteCapture.setLayoutX(1050);
		whiteCapture.setLayoutY(200);
		gamePane.getChildren().add(whiteCapture);

		//Préparation de la fenêtre principale
		scene = new Scene(gamePane, gameSizeX, gameSizeY);

		stage.setTitle("Super Mega Chess 3000");

		stage.setScene(scene);

		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	
	//Retire la planche de jeu de la fenêtre
	private void clearGame() {
		if (board != null) {
			gamePane.getChildren().remove(board.getPane());
		}
	}


	//Redémarre le jeu avec la planche de jeu par défaut.
	private void resetGame() {

		clearGame();
		board = new ChessBoard(boardPosX, boardPosY);
		ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
		pieces=ChessPiece.createInitialPieces(board);
		for(ChessPiece piece: pieces){
			board.putPiece(piece);
		}
		gamePane.getChildren().add(board.getPane());
		// Attention! Le board peut masquer les autres contrôles s'il n'est pas
		// placé complètement derrière eux.
		board.getPane().toBack();
	}
	
	//Redémarre le jeu avec une planche de jeu chargée d'un fichier
	private void resetGame(File file) {

		clearGame();
		
		//Obtient la planche de jeu avec ses pièces à partir d'un fichier
		loadBoard(file);

		gamePane.getChildren().add(board.getPane());
		// Attention! Le board peut masquer les autres contrôles s'il n'est pas
		// placé complètement derrière eux.
		board.getPane().toBack();
	}

	//Charge une planche de jeu à partir d'un fichier.
	private void loadBoard(File file) {
		try {
			board = ChessBoard.readFromFile(file, boardPosX, boardPosY);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, "Error reading file", ButtonType.OK);
			alert.showAndWait();
			return;
		}
	}

	//Sauvegarde la planche de jeu actuelle dans un fichier.
	private void saveBoard(File file) {

		try {
			board.saveToFile(file);
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, "Error writing file", ButtonType.OK);
			alert.showAndWait();
			return;
		}
	}

	//Démarre l'enregistrement des mouvements du jeu dans un fichier de script.
	private void saveScript(File file) {

		try {
			throw new Exception("Pas implanté!");
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, "Error writing file", ButtonType.OK);
			alert.showAndWait();
			return;
		}

	}

	//Charge un fichier de script 
	private void loadScript(File file) {

		try {
			throw new Exception("Pas implanté!");
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR, "Error reading file", ButtonType.OK);
			alert.showAndWait();
			return;
		}

	}

	// Utilisé pour obtenir un dialogue d'ouverture
	private File getOpenFile(String title, String baseDir, Stage stage) {

		fileDialog.setTitle(title);
		fileDialog.setInitialDirectory(new File(System.getProperty("user.dir") + "/" + baseDir));
		return fileDialog.showOpenDialog(stage);
	}

	// Utilisé pour obtenir un dialogue de sauvegarde
	private File getSaveFile(String title, String baseDir, Stage stage) {

		fileDialog.setTitle(title);
		fileDialog.setInitialDirectory(new File(System.getProperty("user.dir") + "/" + baseDir));
		return fileDialog.showSaveDialog(stage);
	}

}
