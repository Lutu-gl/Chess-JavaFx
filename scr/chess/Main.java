package chess;

import javafx.scene.image.ImageView;
import pieces.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Main Class which contains the Main Method
 * @author Stefan Hasler
 * @version 1.3
 */
public class Main extends Application {
    /**
     * Sets up the Chessboard
     * @param primaryStage stage given on startup by java
     */
    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Floppa Chess");
        Chessboard board = new Chessboard();
        board.doShowTextLabels(false);
        board.doShowTextLabels(true);

        Scene sc = new Scene(board, 700, 700);


        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:scr/Chess/Download.jpg"));

        //Hier Kommen Menüaufrufe her

       //board.setBoardByFen("rnbqkbnr/pppp1ppp/4p3/8/1P6/P7/2PPPPPP/RNBQKBNR b KQkq - 0 2");
        //board.getLabels()[4][4].setPiece(new King(new ImageView(new Image("Imgs\\B_King.png")), board.getLabels()[4][4], Color.BLACK, "Black King"));
        board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");

        //Rook p = new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), board.getLabels()[0][0], Color.WHITE, "Rook");

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
