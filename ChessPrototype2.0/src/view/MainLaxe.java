package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Chessboard;

public class MainLaxe extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stage;

    public static Scene hostScene, joinScene;

    public static int size, timeWhite, timeBlack, inkrementWhite, inkrementBlack;
    public static boolean whiteAi, blackAi, invertBoard;


    public static void startGame() {
        Chessboard board = Chessboard.getInstance();

        System.out.println("Size: "+size);
        System.out.println("White ai: "+whiteAi);
        System.out.println("Black ai: "+blackAi);
        System.out.println("timeWhite: "+timeWhite);
        System.out.println("timeBlack:"+timeBlack);
        System.out.println("ikWhite: "+inkrementWhite);
        System.out.println("inkBlack: "+inkrementBlack);

        board.createBoard(size, whiteAi, blackAi, timeWhite, timeBlack, inkrementWhite, inkrementBlack); //In Sekunden!

        String fen = "";
        fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; //Default fen
        board.addFen(fen);
        board.setBoardByFen(fen);
        board.printBoard();

        // Display the Chessboard
        // Set graphic view of the chess board. Normally it is 8x8
        Scene scene = ChessboardView.init(size, size, FieldBackground.STANDARD, PieceDesign.STANDARD, invertBoard);
        scene.getStylesheets().add(MainLaxe.class.getResource("stylesheet.css").toString());

        MainLaxe.changeScene(scene);
        // Set the figures with the FEN Code
        ChessboardView.display();
        if (board.isPlayerConnected() && board.getPlaysAI()[0])
            board.endTurn();
    }

    public static void changeScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent panel, panel2, panel3;
        //panel = FXMLLoader.load(getClass().getResource(fxmlResource));
        panel = FXMLLoader.load(getClass().getResource("menu.fxml"));
        panel2 = FXMLLoader.load(getClass().getResource("host.fxml"));
        panel3 = FXMLLoader.load(getClass().getResource("join.fxml"));
        Scene scene = new Scene(panel);
        hostScene = new Scene(panel2);
        joinScene = new Scene(panel3);
        stage = new Stage();
        stage.setTitle("Chess!");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.show();
    }
}