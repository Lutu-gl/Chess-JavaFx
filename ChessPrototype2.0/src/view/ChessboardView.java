package view;

import controller.Controller;
import controller.DragDetectedHandler;
import controller.DragDroppedHandler;
import controller.DragOverHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import model.Chessboard;

import java.util.ArrayList;

/**
 * Chessboard for Displaying the Board in a Graphical way
 * @since 1.0
 * @version 4.6
 */
public class ChessboardView {

    private static ArrayList<ArrayList<FieldLabel>> board;
    private static Scene mainScene;
    private static VBox timerVBox;
    private static VBox movesVBox;

    /**
     * Sets up Board and static Attributes
     * @param l lenght of board
     * @param w width of board
     * @param color Style of Board
     * @return created Scene with FieldLabels etc.
     */
    public static Scene init( int l, int w, FieldBackground color) {
        BorderPane bp = new BorderPane();
        GridPane gridPane = new GridPane();
        board = new ArrayList<>();

        for (int i = 0; i < l; i++) {
            ArrayList<FieldLabel> buffer = new ArrayList<>();
            for (int x = 0; x < w; x++) {
                FieldLabel lbl = new FieldLabel(i, x);
                lbl.getStyleClass().add((i+x)%2==0 ? color.getS1() : color.getS2());
                lbl.setOnMouseClicked(Controller.getInstance());

                // Set handler for drag and drop
                lbl.setOnDragDetected(new DragDetectedHandler());
                lbl.setOnDragOver(new DragOverHandler());
                lbl.setOnDragDropped(new DragDroppedHandler());

                buffer.add(lbl);
                gridPane.add(lbl, x, i);
            }
            board.add(buffer);

            // Add the numbers on the right side of the board
            FieldLabel number = new FieldLabel(""+(l-i));
            number.getStyleClass().add("index");
            gridPane.add(number, w+1, i);
        }

        // Add the letters on the bottom of the board
        for (int i = 0; i < w; i++) {
            FieldLabel letter = new FieldLabel(""+((char) (i+65)));
            letter.getStyleClass().add("letter");
            gridPane.add(letter, i, l+1);
        }
        //Button turnBack = new Button("Turn back!");
        //turnBack.setOnAction(new TurnBackHandler());
        //gridPane.add(turnBack, w+2, 0);

        bp.setCenter(gridPane);

        Label t1Label = new Label(), t2Label = new Label();
        t2Label.setText(Long.toString(Chessboard.getInstance().getWhiteTime()/1000));
        t1Label.setText(Long.toString(Chessboard.getInstance().getBlackTime()/1000));

        t1Label.setTextAlignment(TextAlignment.CENTER);
        t1Label.setStyle("-fx-background-color: #262421;-fx-text-fill: #bababa;");
        t1Label.setFont(new Font("Arial", 30));

        t2Label.setTextAlignment(TextAlignment.CENTER);
        t2Label.setStyle("-fx-background-color: #262421;-fx-text-fill: #bababa;");
        t2Label.setFont(new Font("Arial", 30));

        ScrollPane scPane = new ScrollPane();
        scPane.setMinSize(250,250);
        scPane.setMaxSize(250, 250);

        VBox movesVBox = new VBox();
        TextArea tf = new TextArea();
        tf.setEditable(false);
        tf.getStyleClass().add("movesTextArea");
        tf.setPrefSize(500, 500);
        tf.setLayoutX(0);
        tf.setLayoutY(0);
        movesVBox.getChildren().add(tf);
        movesVBox.setSpacing(10);
        //scPane.vvalueProperty().bind(movesVBox.heightProperty());
        scPane.setContent(movesVBox);

        VBox timerVBox = new VBox(t1Label, scPane,t2Label);
        scPane.setFitToWidth(true);
        timerVBox.setSpacing(100);
        t1Label.setMinSize(200, 200);
        t2Label.setMinSize(200, 200);
        bp.setRight(timerVBox);

        ChessboardView.timerVBox = timerVBox;
        ChessboardView.movesVBox = movesVBox;
        mainScene = new Scene(bp, w*100+300, l*100+50);
        return mainScene;
    }

    /**
     * sets view.ChessboardView to specific fen
     * @param fen needs to be formatted correctly as FEN notation
     */
    public static void setFEN(String fen) {
        String[] lines = fen.substring(0, fen.indexOf(" ")).split("/");

        for (int i = 0; i < lines.length; i++) {
            int position = 0;
            String empty = "";
            for (int x = 0; x < lines[i].length(); x++) {
                int ascii = lines[i].charAt(x);
                if (ascii >= 48 && ascii <= 57) {
                    empty += lines[i].charAt(x);
                    continue;
                } else if (empty.length() > 0) {
                    position += Integer.parseInt(empty);
                    empty = "";
                }
                ImageView image = null;
                if (ascii >= 65 && ascii <= 90) {
                    image = new ImageView(new Image(Main.class.getResourceAsStream("/W_"+lines[i].charAt(x)+".png")));
                }
                else {
                    image = new ImageView(new Image(Main.class.getResourceAsStream("/B_"+lines[i].charAt(x)+".png")));
                }
                image.setFitHeight(100);
                image.setFitWidth(100);
                //board.get(i).get(position).setGraphic(new ImageView(new Image("B_"+lines[i].charAt(x)+".png")));
                board.get(i).get(position).setGraphic(image);
                position++;
            }
        }
    }

    /**
     * sets view.ChessboardView to model.Chessboard by its fen
     */
    public static void display() {
        //System.out.println(Chessboard.getInstance().getBoardAsFen());
        deleteImages();
        setFEN(Chessboard.getInstance().getBoardAsFen());
    }

    /**
     * removes Pieces on view.ChessboardView, does not affect model.Chessboard
     */
    private static void deleteImages(){
        board.forEach(line -> line.forEach(element -> element.setGraphic(null)));
    }

    public static ArrayList<ArrayList<FieldLabel>> getBoard() {
        return board;
    }

    public static Scene getMainScene() {
        return mainScene;
    }

    public static VBox getTimerVBox() {
        return timerVBox;
    }

    public static VBox getMovesVBox() {
        return movesVBox;
    }
}
