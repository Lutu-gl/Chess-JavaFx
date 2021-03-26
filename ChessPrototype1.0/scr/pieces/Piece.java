package pieces;

import chess.*;
import gameLogic.Color;
import gameLogic.Move;
import javafx.scene.image.ImageView;


import java.util.ArrayList;

/**
 * Topclass for each Piece
 */
public abstract class Piece{

    /*** Imageview of the Piece(B_Rook, B_Queen, ect...)*/
    ImageView img;

    /*** Fieldlabel this Piece is on top of*/
    FieldLabel fieldLabel;

    /*** Enum Color(Black or white)*/
    Color color;

    /*** String Name of the Piece("Black Queen", "White Knight"))*/
    String name;

    /***The unique id of every Piece*/
    int id;

    /*** ArrayList<Move> of all allowed moves for the Piece*/
    ArrayList<Move> validMoves = new ArrayList<>();

    /*** ArrayList<FieldLabel> containing all moves regardless if they are legal or not*/
    ArrayList<FieldLabel> attackingSquares = new ArrayList<>();


    /**
     * The Constructor initializes the Piece with all given information, it also sets the Imageview height
     * @param img Imageview of the Picture that it can display
     * @param l The Label it is ontop of
     * @param color The color of the Piece(Black or white)
     * @param name The name of the Piece("White Queen")
     */
    public Piece(ImageView img, FieldLabel l, Color color, String name){
        img.setFitHeight(75);
        img.setPreserveRatio(true);
        this.img = img;
        this.fieldLabel = l;
        this.color = color;
        this.name = name;
    }

    /**
     * Getter Method for String name
     * @return name of Piece
     */
    public String getName() {
        return name;
    }

    /**
     * Setter Method for String name
     * @param name String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Getter for Imageview
     * @return the Image as an Imageview Object
     */
    public ImageView getImg() {
        return img;
    }

    /**
     * Setter for Imageview Object
     * @param img Imageview for the piece
     */
    public void setImg(ImageView img) {
        this.img = img;
    }

    /**
     * Getter for Fieldlabel Object
     * @return Fieldlabel Object
     */
    public FieldLabel getFieldLabel() {
        return fieldLabel;
    }

    /**
     * Setter for Fieldlabel
     * @param l Fieldlabel Object
     */
    public void setFieldLabel(FieldLabel l) {
        this.fieldLabel = l;
    }

    /**
     * Getter for ENUM Color
     * @return Color Enum
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter for Color enum
     * @param color Color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return  name;
    }

    /**
     * Returns a Arraylist of every Possible move for that Piece, does not account for putting own king in Check
     * @param board The chessboard that is used for that game
     * @return ArrayList<Move> can be empty if no Moves exist
     */
    public abstract ArrayList<Move> calculateValidMoves(Chessboard board);

    /**
     * Calculates every possible move regardless if its a legal move or not
     * @return Arraylist of all Fieldlables that it can move to
     */
    public abstract ArrayList<FieldLabel> calculateAttackingSquares();

    /**
     * This Method gets Called after every legitimate halfmove, can be used if necessary
     * @param m The valid halfmove
     */
    public void postTurn(Move m){

    }
}
