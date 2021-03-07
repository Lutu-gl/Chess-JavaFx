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

        //System.out.println((endTime-startTime));
        //Hier Kommen Menüaufrufe her -> ok!
        board.setBoardByFen("3k4/8/8/8/8/4RR2/4K3/8 w - - 0 1");
        //board.setBoardByFen("8/8/8/8/8/8/8/8 w - - 0 1");
        //System.out.println(board.getBoardAsFen());

        /*Default fen*/
        //board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        //System.out.println(board.getBoardAsFen());

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
