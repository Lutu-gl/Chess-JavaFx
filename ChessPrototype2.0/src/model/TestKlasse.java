package model;

import model.pieces.Piece;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TestKlasse {
    private static Chessboard chessboard;
    private static int count = 0;

    static int  moveGenerationTest(int depth) {

        //System.out.println(depth);
        //chessboard.getWhitePieces().forEach(System.out::println);
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

    private static ArrayList<Turn> generateMoves() {
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

    public static void main(String[] args) {
        chessboard = Chessboard.getInstance();
        chessboard.createBoard(8, false, false);
        chessboard.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        chessboard.debug = true;


        chessboard.printBoard();

        System.out.println();
        for (int i = 0; ; i++) {
            long mv = moveGenerationTest(i);
            System.out.println("gesuchte Tiefe " + i);
            System.out.println(mv);
            System.out.println("---------------------");

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt", true));
                writer.append(' ');
                writer.append("gesuchte Tiefe " + i + "\n");
                writer.append(Long.toString(mv) + "\n\n\n");
                writer.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
    }
}
