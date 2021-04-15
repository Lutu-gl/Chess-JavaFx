import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.image.Image;
import model.Chessboard;
import model.Color;
import model.Field;
import model.Turn;
import model.pieces.Piece;
import org.junit.jupiter.api.Test;
import view.ChessboardView;
import view.Main;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.Chessboard;

import java.util.ArrayList;


public class ChessRulesetTest extends Application {
    private final Chessboard chessboard = Chessboard.getInstance();
    @Test
    void chessInDepth() {
        launch();
    }

    int moveGenerationTest(int depth) {

        // break condition
        if (depth == 0)
            return 1;

        ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();

        int numberPositions = 0;
        // Iterate over all pieces
        for (Piece piece : pieces) {
            // Iterate over all moves of the piece
            for (Field move : piece.getMoves()) {
                // Play the move
                Turn t = new Turn(piece.getField(), move);
                Color c = chessboard.getColorToMove();
                chessboard.handleTurn(t);
                chessboard.printBoard();
                // Recursively call moveGenerationTest()
                numberPositions += moveGenerationTest(depth-1);
                chessboard.undoTurn(t, c);
            }
        }
        return numberPositions;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //assertEquals(2, calculator.add(1, 1));
        int[] possibilities = new int[]{20, 400, 8902, 197281, 4865609, 119060324};
        chessboard.createBoard(8);
        chessboard.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        // Display the Chessboard
        // Set Title of window
        primaryStage.setTitle("Chess!");
        // Set graphic view of the chess board. Normally it is 8x8
        primaryStage.setScene(ChessboardView.init(8, 8));
        // Set the figures with the FEN Code
        ChessboardView.display();
        // Set the stylesheet
        primaryStage.getScene().getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        // Set the icon of the window
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        // Display the window
        primaryStage.show();

        System.out.println(moveGenerationTest(3));

        System.exit(0);
    }
}
