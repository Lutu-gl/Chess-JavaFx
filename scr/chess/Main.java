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
        long endTime = System.currentTimeMillis();

        //System.out.println((endTime-startTime));
        //Hier Kommen Menüaufrufe her
        //board.setBoardByFen("3qk3/8/8/3N4/8/8/3Q4/3K4 w - - 0 1");

        /*Default fen*/
        board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        System.out.println(board.getBoardAsFen());

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
