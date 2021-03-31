package view;

import controller.ClickHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class ChessboardView {
    private static ArrayList<ArrayList<Label>> board;
    public static Scene init( int l, int w) {
        GridPane gridPane = new GridPane();
        board = new ArrayList<>();
        for (int i = 0; i < l; i++) {
            ArrayList<Label> buffer = new ArrayList<>();
            for (int x = 0; x < w; x++) {
                Label lbl = new Label();
                lbl.getStyleClass().add((i+x)%2==0 ? "whiteField" : "blackField");
                lbl.setOnMouseClicked(new ClickHandler());
                buffer.add(lbl);
                gridPane.add(lbl, x, i);
            }
            board.add(buffer);

            // Add the numbers on the right side of the board
            Label number = new Label(""+(l-i));
            number.getStyleClass().add("index");
            gridPane.add(number, w+1, i);
        }

        // Add the letters on the bottom of the board
        for (int i = 0; i < w; i++) {
            Label letter = new Label(""+((char) (i+65)));
            letter.getStyleClass().add("letter");
            gridPane.add(letter, i, l+1);
        }

        return new Scene(gridPane, w*100+50, l*100+50);
    }
    public static void setFEN(String fen) {
        String[] lines = fen.split("/");
        for (int i = 0; i < lines.length; i++) {
            int position = 0;
            for (int x = 0; x < lines[i].length(); x++) {
                int ascii = lines[i].charAt(x);
                if (ascii >= 48 && ascii <= 57) {
                    position += ascii-48;
                    continue;
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

    public static ArrayList<ArrayList<Label>> getBoard() {
        return board;
    }

}
