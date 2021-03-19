package chess;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main Class which contains the Main Method
 * @author Stefan Hasler
 * @version 1.5
 */
public class Main extends Application {
    /**
     * Sets up the Chessboard
     * @param primaryStage stage given on startup by java
     */
    @Override
    public void start(Stage primaryStage){
        long startTime = System.currentTimeMillis();
        primaryStage.setTitle("Floppa Chess");
        Chessboard board = new Chessboard();
        board.doShowTextLabels(false);
        board.doShowTextLabels(true);

        Scene sc = new Scene(board, 700, 700);
        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:scr/Chess/Download.jpg"));
        board.doShowTextLabels(false);

        String fen = "";        //Bitte ab jetzt in die Variable String den fen hineinschreiben. Ist wichtig für die threefoldrepetition

        //Hier Kommen Menüaufrufe her -> ok!
        //board.setBoardByFen("1nbqk3/2pppp2/8/8/8/8/3PPP2/3QK3 w - - 0 1");
        //board.setBoardByFen("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1");
        //board.setBoardByFen("r3k2r/ppqppqpp/8/1B6/1N6/1P6/PP1PP1PP/R3K2R w KQkq - 0 1");    //Castle fen:)
        //board.setBoardByFen("r1bqk1nr/pppp1ppp/2n5/2b1p3/2B1P3/3P1N2/PPP2PPP/RNBQK2R b KQkq - 0 4");
        //board.setBoardByFen("r1bq1bkr/ppp3pp/2n5/3np3/2B5/5Q2/PPPP1PPP/RNB1K2R w KQ - 2 8");

        /*!!!!!!*/
        //board.setBoardByFen("rkb4r/p2p4/1pQ2Bpb/p1pp2Pp/1PBR2PN/P1PN1P1n/3RK3/1q5R b - 0 1"); //NullpointerException Fen
        //board.setBoardByFen("rkb4r/p2p4/1pQ3pb/p1pp2Pp/1PBR1PPN/P1PN1P1n/3RK2B/1q5R b kq - 0 1"); //same
        //fen = "rnbqkbnr/pppppppp/8/4P3/1pp5/8/PPPP1PPP/RNBQKBNR b KQkq b3 0 1";

        /*Default fen*/
        fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        //System.out.println(board.getBoardAsFen());

        board.addFen(fen);
        board.setBoardByFen(fen);

        /*
        PromotionDialog p1 = new PromotionDialog(p);
        p1.show();
        System.out.println(p.getColor().name());

        */

    }


    public static void main(String[] args) {
        launch(args);
    }
}
