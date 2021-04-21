import static org.junit.jupiter.api.Assertions.assertEquals;

import model.*;
import model.pieces.King;
import model.pieces.Piece;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class ChessRulesetTest {
    private Chessboard chessboard;
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

        //moves.forEach(System.out::println);

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
        doTest("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 8, new int[]{1, 20, 400, 8902, 197281, 4865609});
    }

    @Test
    public void castleInDepth()  {
        doTest("r3k2r/p6p/8/8/8/8/P6P/R3K2R w KQkq - 0 1", 8, new int[]{1, 16, 242, 4494, 79781, 1625831});
    }

    @Test
    public void promotionInDepth() {
        doTest("k7/7P/8/8/8/8/8/K7 w - - 0 1", 8, new int[]{1,7});
    }

    private void doTest(String fen, int boardSize, int[] possibilities) {
        Chessboard.getInstance().reset();
        chessboard = Chessboard.getInstance();
        chessboard.createBoard(boardSize, true, true);
        chessboard.setBoardByFen(fen);
        chessboard.debug = true;

        chessboard.printBoard();
        for (int i = 0; i < possibilities.length; i++)
            assertEquals(possibilities[i], moveGenerationTest(i));
    }
}
