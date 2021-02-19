package Chess;

import Pieces.Queen;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
/**
 * Main Method which contains the Main Method
 * @author Stefan Hasler
 * @version "%I",1.0
 *
 */
public class Main extends Application {
    /**
     * Sets up the Chessboard
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Chess");
        Chessboard board = new Chessboard();
        Scene sc = new Scene(board, 700, 700);
        primaryStage.setScene(sc);
        primaryStage.show();
        board.getLabels().get(0).setFigure(new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), board.getLabels().get(0), Color.BLACK, "Black Queen"));

    }


    public static void main(String[] args) {
        launch(args);
    }
}
