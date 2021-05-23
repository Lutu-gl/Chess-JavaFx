package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.Chessboard;

/**
 * Class for displaying the main menu and handling and handling the scene changes
 */
public class MainLaxe extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public static Stage stage;

    public static Scene hostScene, joinScene, mainScene;

    public static int size, timeWhite, timeBlack, inkrementWhite, inkrementBlack;
    public static boolean whiteAi, blackAi, invertBoard, hideTurnBackButton;
    public static PieceDesign pieceDesign;
    public static FieldBackground fieldDesign;

    /**
     * Starts the game with the set parameters and displays the board
     */
    public static void startGame() {
        Chessboard board = Chessboard.getInstance();


        System.out.println("Size: "+size);
        System.out.println("White ai: "+whiteAi);
        System.out.println("Black ai: "+blackAi);
        System.out.println("timeWhite: "+timeWhite);
        System.out.println("timeBlack:"+timeBlack);
        System.out.println("ikWhite: "+inkrementWhite);
        System.out.println("inkBlack: "+inkrementBlack);
        System.out.println("PieceDesign: "+pieceDesign);
        System.out.println("FieldDesign: "+fieldDesign);

        // Display the Chessboard
        // Set graphic view of the chess board. Normally it is 8x8
        Scene scene = ChessboardView.init(size, size, fieldDesign, pieceDesign, invertBoard, hideTurnBackButton);

        board.createBoard(size, whiteAi, blackAi, timeWhite, timeBlack, inkrementWhite, inkrementBlack); //In Sekunden!
        String fen = "";
        fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; //Default fen
        board.addFen(fen);
        board.setBoardByFen(fen);
        board.printBoard();

        scene.getStylesheets().add(MainLaxe.class.getResource("stylesheet.css").toString());

        MainLaxe.changeScene(scene);
        // Set the figures with the FEN Code
        ChessboardView.display();
        if (board.isPlayerConnected() && board.getPlaysAI()[0])
            board.endTurn();
    }

    /**
     * Changes the primary Scene
     * @param scene
     */
    public static void changeScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);

        stage.setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    /**
     * Start method to load the FXML files and display the main menu
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent panel, panel2, panel3;
        //panel = FXMLLoader.load(getClass().getResource(fxmlResource));
        panel = FXMLLoader.load(getClass().getClassLoader().getResource("menu.fxml"));
        panel2 = FXMLLoader.load(getClass().getClassLoader().getResource("host.fxml"));
        panel3 = FXMLLoader.load(getClass().getClassLoader().getResource("join.fxml"));
        mainScene = new Scene(panel);
        hostScene = new Scene(panel2);
        joinScene = new Scene(panel3);
        stage = new Stage();
        stage.setTitle("Chess!");
        stage.setScene(mainScene);
        stage.getIcons().add(new Image(Main.class.getResourceAsStream("/icon.png")));
        stage.show();
    }
}