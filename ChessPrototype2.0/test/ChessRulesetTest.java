import static org.junit.jupiter.api.Assertions.assertEquals;

import model.*;
import model.pieces.King;
import model.pieces.Piece;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class ChessRulesetTest {
    private final Chessboard chessboard = Chessboard.getInstance();
    private static int count = 0;

    int moveGenerationTest(int depth) {

        //System.out.println(depth);
        //chessboard.printBoard();

        // break condition
        if (depth == 0) {
            return 1;
        }

        ArrayList<Turn> moves = generateMoves();

        int numberPositions = 0;

        for (Turn move : moves) {
            chessboard.handleTurn(move);
            numberPositions += moveGenerationTest(depth - 1);
            chessboard.undoTurn(move);
        }

        return numberPositions;
    }

    private ArrayList<Turn> generateMoves() {
        ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();

        // Create the turns
        ArrayList<Turn> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            for (Field move : piece.getMoves()) {
                Turn turn = new Turn(piece.getField(), move);
                if (chessboard.isLegal(turn.getTargetField(), turn.getSourceField(), turn.getColorToMove()))
                    moves.add(turn);
            }
        }
        return moves;
    }

    @Test
    public void chessInDepth()  {
        int[] possibilities = new int[]{1, 20, 400, 8902, 197281, 4865609, 119060324};
        chessboard.createBoard(8);
        chessboard.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        chessboard.debug = true;

        for (int i = 0; i < possibilities.length; i++)
            assertEquals(possibilities[i], moveGenerationTest(i));

    }
}
