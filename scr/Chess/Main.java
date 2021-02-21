package Chess;

import Pieces.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
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
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Floppa Chess");
        Chessboard board = new Chessboard();
        Scene sc = new Scene(board, 700, 700);


        MouseListener ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };

        primaryStage.setScene(sc);
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:scr/Chess/Download.jpg"));

        board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
       //board.setBoardByFen("rnbqkbnr/pppp1ppp/4p3/8/1P6/P7/2PPPPPP/RNBQKBNR b KQkq - 0 2");
/*
        board.getLabels()[4][4].setPiece(new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), board.getLabels()[4][4], Color.BLACK, "Black Rook"));
        board.getLabels()[0][0].setPiece(new Bishop(new ImageView(new Image("Imgs\\W_Bishop.png")), board.getLabels()[0][0], Color.WHITE, "Black Rook"));
        board.getLabels()[1][1].setPiece(new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), board.getLabels()[1][1], Color.BLACK, "Black Rook"));

        board.getLabels()[0][2].setPiece(new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), board.getLabels()[0][2], Color.BLACK, "Black Rook"));
        board.getLabels()[0][5].setPiece(new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), board.getLabels()[0][5], Color.WHITE, "Black Rook"));


         */

    }


    public static void main(String[] args) {
        launch(args);
    }
}
