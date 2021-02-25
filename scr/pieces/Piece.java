package pieces;

import chess.*;
import javafx.scene.image.ImageView;


import java.util.ArrayList;

/**
 * Topclass for each Piece
 * @author Stefan Hasler
 * @version 1.35
 */
public abstract class Piece{

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
    ArrayList<Move> validMoves = new ArrayList<>();

    public Piece(ImageView img, FieldLabel l, Color color, String name){
        img.setFitHeight(75);
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
     *  Returns a Arraylist of every Possible move for that Piece, does not account for putting own king in Check
     * @param board The chessboard that is used for that game
     * @return list of Fieldlabels that are every possible move as an Arraylist
     */
    public abstract ArrayList<Move> calculateValidMoves(Chessboard board);

    public void postTurn(){

    }
}
