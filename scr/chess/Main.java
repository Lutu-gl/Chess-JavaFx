package chess;

import javafx.scene.image.ImageView;
import pieces.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Main Class which contains the Main Method
 * @author Stefan Hasler
 * @version 1.3
 *
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
        Scene sc = new Scene(board, 700, 700);

        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:scr/Chess/Download.jpg"));

        //Hier Kommen Menüaufrufe her

       //board.setBoardByFen("rnbqkbnr/pppp1ppp/4p3/8/1P6/P7/2PPPPPP/RNBQKBNR b KQkq - 0 2");
        //board.getLabels()[4][4].setPiece(new King(new ImageView(new Image("Imgs\\B_King.png")), board.getLabels()[4][4], Color.BLACK, "Black King"));
        board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
