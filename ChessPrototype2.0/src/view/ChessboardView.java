package view;

import controller.Controller;
import controller.DragOverHandler;
import controller.DragStartHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import model.Chessboard;
import model.Field;
import model.Turn;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChessboardView {
    private static ArrayList<ArrayList<FieldLabel>> board;
    public static Scene init( int l, int w) {
        GridPane gridPane = new GridPane();
        board = new ArrayList<>();

        for (int i = 0; i < l; i++) {
            ArrayList<FieldLabel> buffer = new ArrayList<>();
            for (int x = 0; x < w; x++) {
                FieldLabel lbl = new FieldLabel(i, x);
                lbl.getStyleClass().add((i+x)%2==0 ? "whiteField" : "blackField");
                lbl.setOnMouseClicked(Controller.getInstance());
                //lbl.setOnDragDetected(DragStartHandler.getInstance());

                // Start dragEvent
                lbl.setOnDragDetected(e -> {
                    FieldLabel clickedFieldLabel = (FieldLabel) e.getSource();
                    Chessboard chessboard = Chessboard.getInstance();
                    Field clickedField = chessboard.getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
                    if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) {
                        Controller.getInstance().selectLabel(clickedFieldLabel);
                        Controller.getInstance().markAvailableMoves(clickedField);
                        Controller.getInstance().setSource(clickedFieldLabel);
                        Dragboard db = lbl.startDragAndDrop(TransferMode.ANY);

                        /* Put a string on a dragboard */
                        ClipboardContent content = new ClipboardContent();
                        content.putString("Drag event");
                        db.setContent(content);
                    }

                    e.consume();
                });

                // Check if field is legal
                lbl.setOnDragOver(e -> {
                    e.acceptTransferModes(TransferMode.MOVE);
                    e.consume();
                });

                // Drop piece
                lbl.setOnDragDropped(e -> {

                    Controller controller = Controller.getInstance();
                    FieldLabel targetLabel = (FieldLabel) e.getGestureTarget();
                    Chessboard chessboard = Chessboard.getInstance();

                    controller.setTarget(targetLabel);

                    ArrayList<FieldLabel> highlighted = controller.getHighlighted();
                    Turn turn = null;
                    for (int j = 0; j < highlighted.size(); j++) {
                        if (highlighted.get(j).getLine() == controller.getTarget().getLine() && highlighted.get(j).getColumn() == controller.getTarget().getColumn()) {
                            turn = new Turn(controller.getSource(), controller.getTarget());
                            break;
                        }
                    }

                    controller.unmarkAvailableMoves();
                    controller.unSelectLabel();
                    e.setDropCompleted(true);
                    e.consume();
                    if (turn == null) return;
                    chessboard.handleTurn(turn);
                });

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

        return new Scene(gridPane, w*100+50, l*100+50);
    }

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

    public static void display() {
        //System.out.println(Chessboard.getInstance().getBoardAsFen());
        deleteImages();
        setFEN(Chessboard.getInstance().getBoardAsFen());
    }

    private static void deleteImages(){
        board.forEach(line -> line.forEach(element -> element.setGraphic(null)));
    }

    public static ArrayList<ArrayList<FieldLabel>> getBoard() {
        return board;
    }

}
