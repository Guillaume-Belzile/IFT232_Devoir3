package chess.ui;

import chess.ChessBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PieceView {

    // Utilisé pour générer les noms de fichiers contenant les images des pièces.
    private static final String[] names = {"pawn", "knight", "bishop", "rook", "queen", "king"};
    private static final String[] prefixes = {"w", "b"};

    // Taille d'une pièce dans l'interface
    private static final double pieceSize = 75.0;

    // Panneau d'interface contenant l'image de la pièce
    private Pane piecePane;

    // Référence à la planche de jeu. Utilisée pour déplacer la pièce.
    private final ChessBoard board;

    public PieceView(ChessBoard b) {
        board = b;
    }

    public PieceView(int color, int type, ChessBoard b) {
        board = b;

        Image pieceImage;
        try {
            pieceImage = new Image(new FileInputStream("images/" + prefixes[color] + names[type] + ".png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        ImageView pieceView = new ImageView(pieceImage);

        pieceView.setX(0);
        pieceView.setY(0);
        pieceView.setFitHeight(pieceSize);
        pieceView.setFitWidth(pieceSize);

        pieceView.setPreserveRatio(true);
        piecePane = new Pane(pieceView);
        enableDragging(piecePane);
    }

    // Gestionnaire d'événements pour le déplacement des pièces
    private void enableDragging(Node node) {
        final ObjectProperty<Point2D> mouseAnchor = new SimpleObjectProperty<>();
        final ObjectProperty<Point2D> gridPos = new SimpleObjectProperty<>();

        // Lorsque la pièce est saisie, on préserve la position de départ
        node.setOnMousePressed(event -> {
            Point2D pointDepart = new Point2D(event.getSceneX(), event.getSceneY());
            mouseAnchor.set(pointDepart);
            gridPos.set(pointDepart);
        });

        // À chaque événement de déplacement, on déplace la pièce et on met à
        // jour la position de départ
        node.setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseAnchor.get().getX();
            double deltaY = event.getSceneY() - mouseAnchor.get().getY();
            node.relocate(node.getLayoutX() + deltaX, node.getLayoutY() + deltaY);
            node.toFront();
            mouseAnchor.set(new Point2D(event.getSceneX(), event.getSceneY()));
        });

        // Lorsqu'on relâche la pièce, le mouvement correspondant est appliqué
        // au jeu d'échecs si possible.
        // L'image de la pièce est également centrée sur la case la plus proche.
        node.setOnMouseReleased(event -> {
            Point2D pos = gridPos.get();
            Point2D newPos = mouseAnchor.get();
            board.move(node, pos, newPos);
        });
    }

    //Accesseurs divers
    public Pane getPane() {
        return piecePane;
    }
}
