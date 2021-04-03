package view;

import controller.ClickHandler;
import controller.Controller;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import model.Chessboard;

import java.util.ArrayList;

public class ChessboardView {
    private static ArrayList<ArrayList<FieldLabel>> board;
    public static Scene init( int l, int w) {
        GridPane gridPane = new GridPane();
        board = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            ArrayList<FieldLabel> buffer = new ArrayList<>();
            for (int x = 0; x < w; x++) {
                FieldLabel lbl = new FieldLabel(i,x);
                lbl.getStyleClass().add((i+x)%2==0 ? "whiteField" : "blackField");
                lbl.setOnMouseClicked(Controller.getInstance());
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
        setFEN(Chessboard.getInstance().getBoardAsFen());
    }

    public static ArrayList<ArrayList<FieldLabel>> getBoard() {
        return board;
    }

}
