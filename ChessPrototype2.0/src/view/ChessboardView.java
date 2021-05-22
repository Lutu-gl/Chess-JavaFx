package view;

import controller.Controller;
import controller.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private static PieceDesign design;
    /**
     * Sets up Board and static Attributes
     * @param l lenght of board
     * @param w width of board
     * @param color Style of Board
     * @return created Scene with FieldLabels etc.
     */
    public static Scene init( int l, int w, FieldBackground color, PieceDesign design, boolean invertBoard) {
        BorderPane bp = new BorderPane();
        GridPane gridPane = new GridPane();
        board = new ArrayList<>();
        ChessboardView.design = design;
        for (int i = 0; i < l; i++) {
            ArrayList<FieldLabel> buffer = new ArrayList<>();
            for (int x = 0; x < w; x++) {
                FieldLabel lbl = new FieldLabel(i, x);
                lbl.getStyleClass().add("field");
                lbl.getStyleClass().add((i+x)%2==0 ? color.getS1() : color.getS2());
                lbl.setOnMouseClicked(Controller.getInstance());

                // Set handler for drag and drop
                lbl.setOnDragDetected(new DragDetectedHandler());
                lbl.setOnDragOver(new DragOverHandler());
                lbl.setOnDragDropped(new DragDroppedHandler());

                // if board should be inverted
                if (invertBoard)
                    lbl.setRotate(180);
                buffer.add(lbl);
                gridPane.add(lbl, x, i);
            }
            board.add(buffer);

            // Add the numbers on the right side of the board
            FieldLabel number = new FieldLabel(""+(l-i));
            number.getStyleClass().add("index");
            // if board should be inverted
            if (invertBoard)
                number.setRotate(180);
            gridPane.add(number, w+1, i);
        }

        // Add the letters on the bottom of the board
        for (int i = 0; i < w; i++) {
            FieldLabel letter = new FieldLabel(""+((char) (i+65)));
            letter.getStyleClass().add("letter");
            // if board should be inverted
            if (invertBoard)
                letter.setRotate(180);
            gridPane.add(letter, i, l+1);
        }
        //Button turnBack = new Button("Turn back!");
        //turnBack.setOnAction(new TurnBackHandler());
        //gridPane.add(turnBack, w+2, 0);

        Button turnBack = new Button("Turn back!");
        turnBack.setOnAction(new TurnBackHandler());

        // if board should be inverted
        if (invertBoard)
            gridPane.setRotate(180);

        bp.setCenter(gridPane);

        Label t1Label = new Label(), t2Label = new Label();
        //System.out.println(MainLaxe.timeWhite);
        t2Label.setText(Controller.getInstance().secondsToFormattedString(MainLaxe.timeWhite));
        t1Label.setText(Controller.getInstance().secondsToFormattedString(MainLaxe.timeBlack));

        t1Label.setTextAlignment(TextAlignment.CENTER);
        t1Label.setStyle("-fx-background-color: #262421;-fx-text-fill: #bababa;");
        t1Label.setFont(new Font("Arial", 30));

        t2Label.setTextAlignment(TextAlignment.CENTER);
        t2Label.setStyle("-fx-background-color: #262421;-fx-text-fill: #bababa;");
        t2Label.setFont(new Font("Arial", 30));


        //ScrollPane scPane = new ScrollPane();
        //scPane.setMinSize(250,250);
        //scPane.setMaxSize(250, 250);

        VBox movesVBox = new VBox();
        movesVBox.setMinSize(250, 250);
        Button concede = new Button("Aufgeben");
        concede.setOnAction(new ConcedeHandler());
        //movesVBox.setMaxSize(250, 250);
        TextArea tf = new TextArea();
        tf.setEditable(false);
        tf.getStyleClass().add("movesTextArea");
        tf.setPrefSize(500, 500);
        tf.setLayoutX(0);
        tf.setLayoutY(0);
        movesVBox.getChildren().addAll(concede, turnBack, tf);
        //movesVBox.getChildren().addAll(turnBack, tf);
        movesVBox.setSpacing(10);

        tf.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                tf.setScrollTop(Double.MIN_VALUE);
            }
        });

        //scPane.vvalueProperty().bind(movesVBox.heightProperty());
        //scPane.setContent(movesVBox);
        VBox timerVBox = new VBox(t1Label, movesVBox, t2Label);
        movesVBox.setFillWidth(true);
        //scPane.setFitToWidth(true);
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
                    image = new ImageView(new Image(Main.class.getResourceAsStream("/"+ChessboardView.design.getDesign()+"W_"+lines[i].charAt(x)+".png")));
                }
                else {
                    image = new ImageView(new Image(Main.class.getResourceAsStream("/"+ChessboardView.design.getDesign()+"B_"+lines[i].charAt(x)+".png")));
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
