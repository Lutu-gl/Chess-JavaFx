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
        primaryStage.setScene(ChessboardView.init(size, size, FieldBackground.STANDARD, PieceDesign.STANDARD, false, false));
        primaryStage.setMinHeight(950);
        primaryStage.setMinWidth(1200);
        board.createBoard(size, false, false, 100, 100, 3, 3); //In Sekunden!

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
        //fen = "8/6K1/1p1B1RB1/8/2Q5/2n1kP1N/3b4/4n3 w - - 0 1";
        //fen = "8/8/8/4k3/1R6/2R5/1K6/8 w - - 0 1";
        //fen = "4Q1k1/pp3r2/2rP3p/4B1N1/2q3P1/P7/5RP1/5RK1 b - - 0 1";
//        fen = "4b3/4PPP1/5K2/8/8/4k3/5pp1/8 w - - 0 1"; // PROMOTION TESTTT
//        fen = "8/8/8/8/6K1/8/4p1k1/8 w - - 0 1"; // PROMOTION TESTTT
//        fen = "6bb/5Prk/6bb/8/8/K7/8/8 w - - 0 1"; //really cool and realistic promotion fen to knight
//        fen = "8/8/8/8/8/k5BB/5pRK/6BB b - - 0 1"; //really cool and realistic promotion fen to knight for black
//        fen = "8/k1P5/3Q4/8/8/6K1/8/8 w - - 0 1";   //fen when promoto to queen is falsee
//        fen = "1k6/3Q1P2/8/8/8/6K1/8/8 w - - 0 1"; //fen promoto queen or rook
//        fen = "8/5P1k/5K2/8/8/8/8/8 w - - 0 1"; //fen promoto Rook not queen is false
//        fen = "6bb/6rk/4N1bb/8/8/K7/8/8 w - - 0 1"; //fen checkmate in one
//        fen = "rnbq1rk1/pppp1p1p/4pPp1/8/7Q/7R/PPPPP1P1/RNB1KBN1 b Qq - 0 1"; //kingsavetyEval test
//        fen = "r4rk1/ppp1npp1/5n1p/3ppP2/5q2/1B1P2NP/PPPQ1PP1/R4RK1 w Qq - 0 1";   //Threefold repetition fen in winning position
//        fen = "3r2k1/3p1ppp/3p4/P6P/5r2/1Pp1RP2/2K2P2/4R3 w - - 0 1";       //Backrank mate for ai to find / or block

        //fen = "k7/4ppp1/8/4PPP1/8/8/8/K7 w - - 0 1";
        //fen = "8/3R1r2/7P/1k6/8/3K4/8/8 w - - 0 1";
        //fen = "8/8/4k3/6p1/4nR2/8/6K1/8 b - - 0 58";
        //fen = "r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1"; //check castle pgn

        //Insufficent material tests:
//        fen = "k7/8/8/4B3/8/6Np/8/2K5 w - - 0 1"; //Knight Bishop vs Pawn: no Draw
//        fen = "8/2k5/8/5N2/8/8/2K5/8 w - - 0 1";  //One Knight: Draw
//        fen = "8/2k5/8/5B2/8/8/2K5/8 w - - 0 1";  //One Bishop: Draw
//        fen = "8/2k5/7p/8/8/4B3/2K5/8 w - - 0 1";  //One Bishop vs Pawn: no Draw
//        fen = "8/2k5/7p/8/8/4N3/2K5/8 w - - 0 1";  //One Knight vs Pawn: no Draw
//        fen = "8/2k1B3/8/8/8/3B4/2K5/8 w - - 0 1";  //BishopD BishopW vs none: no Draw
//        fen = "8/2k5/4B3/8/8/3B4/2K5/8 w - - 0 1";  //BishopW BishopW vs none: no Draw
//        fen = "8/2k5/8/4N3/8/3N4/2K5/8 w - - 0 1";  //Knight Knight vs none: no Draw
//        fen = "8/2k5/5n2/8/8/5N2/2K5/8 w - - 0 1"; //Knight vs Knight: no Draw
//        fen = "8/2k5/5n2/8/8/4B3/2K5/8 w - - 0 1"; //Bishop vs Knight: no Draw
//        fen = "8/2k5/5b2/8/8/3B4/2K5/8 w - - 0 1"; //BishopW vs BishopB: no Draw
//        fen = "8/2k5/4b3/8/8/3B4/2K5/8 w - - 0 1"; //BishopW vs BishopW: Draw
//        fen = "3k4/8/5b2/8/5B2/4B3/3K4/8 w - - 0 1"; //BishopB BishopB vs BishopB: Draw




        //fen = "k7/4P3/p1p1P3/ppp1P3/pP6/1P2P3/8/4K3 w - - 0 1";
        //fen = "r5k1/1R4bp/8/B2R2P1/8/8/4rP2/6K1 b - - 0 39";

        //fen = "4k3/8/8/1N3N2/8/8/P2K4/8 w - - 0 1"; //correct
        //fen = "4k3/4N3/8/5b2/8/4N3/8/4K3 w - - 0 1"; // correct
        //fen = "4k3/8/N7/8/4N3/8/8/4K3 w - - 0 1"; //check knight pgn correct
//        fen = "rnbqkbnr/1ppppppp/8/8/p1B1P3/5Q2/PPPP1PPP/RNB1K1NR w KQkq - 0 1";


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
        // Set the figures with the FEN Code
        ChessboardView.display();
        // Set the stylesheet
        primaryStage.getScene().getStylesheets().add(getClass().getResource("stylesheet.css").toString());
        // Set the icon of the window
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        // Display the window
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
