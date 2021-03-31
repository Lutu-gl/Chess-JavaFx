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
        board.createBoard(size);
        board.addFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        board.setBoardByFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
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
