import static org.junit.jupiter.api.Assertions.assertEquals;

import model.*;
import model.pieces.King;
import model.pieces.Piece;
import org.junit.jupiter.api.Test;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.ArrayList;


public class ChessRulesetTest {
    private final Chessboard chessboard = Chessboard.getInstance();
    private static int count = 0;

    int moveGenerationTest(int depth) {

        // break condition
        if (depth == 0) {
            return 1;
        }

        if (!chessboard.getState().equals(Gamestate.PLAYING)){
            return 0;
        }


        ArrayList<Piece> pieces = chessboard.getColorToMove().equals(Color.WHITE) ? chessboard.getWhitePieces() : chessboard.getBlackPieces();


        // Create the turns
        ArrayList<Turn> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            for (Field move : piece.getMoves()) {
                moves.add(new Turn(piece.getField(), move));
            }
        }



        int numberPositions = 0;
        // Iterate through all the moves

        for (Turn move : moves) {
            chessboard.handleTurn(move);
            numberPositions += moveGenerationTest(depth-1);
            chessboard.undoTurn(move);
        }
        return numberPositions;
        /*
        for (Turn move : moves) {
            Color color = chessboard.getColorToMove();
            Gamestate state = chessboard.getState();
            chessboard.movePiece(move.getMovingPiece(), move.getTargetField(), false);


            King king = color.equals(Color.WHITE) ? chessboard.getW_king() : chessboard.getB_king();
            if (king.isInCheck()) {
                chessboard.undoTurn(move, color, state);
                continue;
            }
//            System.out.println(move);
//            chessboard.printBoard();
            count++;

            chessboard.setColorToMove(color.equals(Color.WHITE)?Color.BLACK:Color.WHITE);
            numberPositions += moveGenerationTest(depth-1);
            chessboard.undoTurn(move, color, state);
        }

         */
//        System.out.println("-----------\n");
    }

    @Test
    public void chessInDepth()  {
        int[] possibilities = new int[]{20, 400, 8902, 197281, 4865609, 119060324};
        chessboard.createBoard(8);
        //chessboard.setBoardByFen("8/8/6pp/6pk/6pp/p5Pr/P7/K7 b - - 0 1");
        chessboard.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        //chessboard.setBoardByFen("B7/1K6/8/8/8/7N/7p/7k w - - 0 1");
        chessboard.debug = true;

//        System.out.println(moveGenerationTest(5));
//        System.out.println(count - moveGenerationTest((1)) - moveGenerationTest(2) - moveGenerationTest(3) - moveGenerationTest(4));

        System.out.println(moveGenerationTest(4));

        int counter = 1;

//        for (Integer expected : possibilities) {
//            assertEquals(expected, moveGenerationTest(counter));
//            counter++;
//        }

        System.exit(0);
    }
}
