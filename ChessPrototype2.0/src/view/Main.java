package view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Chessboard;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        int size = 8;
        // Set the size and the FEN of the logic chessboard
        Chessboard board = Chessboard.getInstance();
        board.createBoard(size, false, true);
        String fen = "";

        fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; //Default fen
        //fen = "rnbqkbnr/2pp2p1/1p2pP2/pP6/8/2P3p1/P2P1P1P/RNBQKBNR w KQkq a6 0 8"; //en passant fen
        //fen = "1k6/ppp3PQ/8/8/p7/4K3/8/8 w - - 0 1";
        //fen = "rn2kbnr/ppp1p2p/4b3/3pqpp1/2P1P3/2NB1N1P/PP1P1PP1/R1BQK2R w KQkq - 0 7"; //Crazy fen
        //fen = "r3k2r/p1qppp1p/8/7b/8/6Q1/P2PP2P/R3K2R w KQkq - 3 15"; //Castle Fen
        //fen = "8/3k3p/8/8/8/3N4/8/3K4 w - - 0 1"; //check test
        //fen = "8/Q1qk3p/8/8/8/3N4/8/3K4 w - - 0 1"; //check test2
        //fen = "8/2p5/8/8/2K2qk1/8/8/5P2 w - - 0 1";
        //fen = "2k5/4PPP1/8/8/8/2K5/4ppp1/8 w - - 0 1"; //Pawn Promotion test
        //fen = "r3k2r/pppq1ppp/2np1n2/2b1p1B1/2B1P1b1/2NP1N2/PPP1QPPP/R3K2R w KQkq - 4 8";
        //fen = "r3k3/4n3/4P3/2p5/8/2K1N3/8/7R w - - 0 1";
        //fen = "rnbqkbnr/ppp2ppp/8/3pp3/3PP3/8/PPPK1PPP/RNBQ1BNR b - - 0 1";
        //fen = "r2qk2r/ppp1pppp/8/8/8/8/PPPPPPPP/RN2K2R w KQkq - 0 1";
        //fen = "r3k2r/pbppqpbp/1pn2np1/4p3/4P3/1PN2NP1/PBPPQPBP/R3K2R w KQkq - 0 1";
        //fen = "rnbqk2r/pppp1ppp/3b4/P3p3/2B1P3/5N2/1PPP1nPP/RNBQK2R b KQkq - 0 6";
        //fen = "rnbq1bnr/pppppkpp/8/8/8/8/PPPPP1PP/RNBQK2R w KQ - 0 1";
        //fen = "k7/8/8/2Q5/K7/8/8/8 w - - 0 1"; // Stalemate fen
        //fen = "k7/8/8/1Pp5/8/8/8/K7 w - c6 0 1";
        //fen = "k7/8/8/1Pp5/8/8/8/K7 w - c6 0 1";
        //fen = "k7/7P/8/8/8/8/8/K7 w - - 0 1";
        //fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        //fen = "r2qr1k1/ppp1bppp/2p2n2/4p3/4P1b1/1PN2N2/PBPP1PPP/R2Q1RK1 w - - 3 9";
        //fen = "8/k3q3/5P2/8/8/8/K7/8 w - - 0 1";
        //fen = "4r1k1/1q4p1/3Q4/p4P2/4b2P/PPp5/2P5/1K1R2R1 b - - 0 31";
        //fen = "2k5/8/8/3Q4/3K4/8/8/8 w - - 0 1";
        fen = "rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8";

        board.addFen(fen);
        board.setBoardByFen(fen);
        //board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        //System.out.println(board.getBlackPieces().get(0).getMoves());
        //System.out.println(board.getBlackPieces().get(0).getMoves());
        board.printBoard();

        // Display the Chessboard
        // Set Title of window
        primaryStage.setTitle("Chess!");
        // Set graphic view of the chess board. Normally it is 8x8
        primaryStage.setScene(ChessboardView.init(size, size));
        // Set the figures with the FEN Code
        ChessboardView.display();
        // Set the stylesheet
        primaryStage.getScene().getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        // Set the icon of the window
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        // Display the window
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
