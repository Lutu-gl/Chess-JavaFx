package pieces;

import chess.Chessboard;
import chess.FieldLabel;
import javafx.scene.image.ImageView;
import chess.Color;

import java.util.ArrayList;

/**
 * Topclass for each Piece
 * @author Stefan Hasler
 * @version 1.3
 */
public abstract class Piece extends ImageView{

    /*** the Image of the Piece(B_Rook, B_Queen, ect...)*/
    ImageView img;

    /*** The label its ontop of, ect...)*/
    FieldLabel fieldLabel;

    /*** Whether its alive or dead)*/
    boolean isAlive = true;

    /*** The Pieces color(Black or white)*/
    Color color;

    /*** The Name of the Piece("Black Queen", "White Knight"))*/
    String name;

    /*** List of all allowed moves for the Piece*/
    ArrayList<FieldLabel> validMoves = new ArrayList<>();

    public Piece(ImageView img, FieldLabel l, Color color, String name){
        img.setFitHeight(70);
        img.setPreserveRatio(true);
        this.img = img;
        this.fieldLabel = l;
        this.color = color;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public FieldLabel getFieldLabel() {
        return fieldLabel;
    }

    public void setFieldLabel(FieldLabel l) {
        this.fieldLabel = l;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return  name;
    }

    /**
     *  Returns a Arraylist for every Possible move for that Piece, does not account for putting own king in Check
     * @param board The chessboard that is used for that game
     * @return list of every possible move as an Arraylist
     */
    public abstract ArrayList<FieldLabel> calculateValidMoves(Chessboard board);
}
