package chess;

import javafx.geometry.Pos;
import pieces.Pawn;
import pieces.*;
import pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import javax.swing.*;
import java.util.ArrayList;

/**
 * The actual Board containing a list of FieldLabels, the Constructor initializes it with 64 labels
 * aswell as a list of all pieces
 * @author Stefan Hasler
 * @version 2.0
 */
public class Chessboard extends GridPane {

    /*** A two Dimensional Array containing all Squares(FieldLabels)*/
    private FieldLabel[][] labels = new FieldLabel[8][8];

    /*** A Arraylist containing all Pieces*/
    private ArrayList<Piece> b_pieces = new ArrayList<>();
    private ArrayList<Piece> w_pieces = new ArrayList<>();

    /*** Halfmove counter*/
    private int turn=0;
    /*** Fullmove counter*/
    private int fullturn=0;

    /*** 50 Rule move counter */
    private int ruleCounter=0;

    /*** A reference to Blacks King*/
    private King b_King;

    /*** A reference to Whites King*/
    private King w_King;


    /**
     * Constructor initializing the Board with 64 Fieldlabels
     */
    public Chessboard(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                FieldLabel label = new FieldLabel(i,j,(i % 2 == j % 2) ? Color.BLACK : Color.WHITE);
                label.setStyle((i % 2 == j % 2) ? "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;");
                label.setId(""+ (char)(97 + i) + (8-j));
                label.setText(""+ (char)(97 + i) + (8-j));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setAlignment(Pos.CENTER);
                label.setBoard(this);
                labels[i][j]=label;
                this.add(label, i, j);
            }
        Move.board = this;
    }

    /**
     * Method to toggle the Coordinates of the FieldLabels
     * @param show boolean true or false
     */
    public void doShowTextLabels(boolean show){
        if(show) {
            FieldLabel label;
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++){
                    label = labels[i][j];
                    label.setText(""+ (char)(97 + i) + (8-j));
                }
        }else{
            for (FieldLabel[] fieldLabel : labels) {
                for (FieldLabel fieldLabel1 : fieldLabel) {
                    fieldLabel1.setText("");
                }
            }
        }
    }

    /**
     * Gets called after every Halfmove
     * More functionality will be added
     */
    public void endTurn(){
        turn++;
        fullturn = turn/2 + 1;
        System.out.println("turn: " + turn + " fullturn: " +fullturn);
        System.out.println(getBoardAsFen());
    }

    /**
     * Getter for Turn
     * @return returns int number of halfmoves since start of game
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Getter for fullturn
     * @return returns int number of full moves since start of game
     */
    public int getFullturn() {
        return fullturn;
    }

    /**
     * Setter for fullturn
     * @param fullturn int number of full moves since start of game
     */
    public void setFullturn(int fullturn) {
        this.fullturn = fullturn;
    }
    /**
     * Getter for 50 move rule
     * @return returns int number of moves before the 50 move rule applys (half moves)
     */
    public int getRuleCounter() {
        return ruleCounter;
    }
    /**
     * Setter for rule counter
     * @param ruleCounter int number of moves before the 50 move rule applys (half moves)
     */
    public void setRuleCounter(int ruleCounter) {
        this.ruleCounter = ruleCounter;
    }

    /**
     * Setter for Turn
     * @param turn int number of halfmoves since start of game
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * Add Method for ArrayList<Piece> pieces
     * @param p Piece that has been added to the Board
     */
    public void addPiece(Piece p){
        if(p.getColor() == Color.BLACK)
            b_pieces.add(p);
        else
            w_pieces.add(p);
    }

    /**
     * Remove Method for ArrayList<Piece> pieces
     * @param p Piece that needs to be removed
     */
    public void removePiece(Piece p){


        if(p == null) return;
        System.out.println("ich remove jetzt: " + p);
        if(p.getColor() == Color.BLACK)
            if(!b_pieces.remove(p)){
                System.out.println("hat nicht geklappt!");
            }
        else
            w_pieces.remove(p);
    }

    /**
     * Getter for Two Dimensional Array of all FieldLabels
     * @return FieldLabels[8][8]
     */
    public FieldLabel[][] getLabels() {
        return labels;
    }

    /**
     * Getter for ArrayList<Piece> pieces
     * @return ArrayList<Piece>
     */
    public ArrayList<Piece> getB_pieces() {
        return b_pieces;
    }
    public ArrayList<Piece> getW_pieces() {
        return w_pieces;
    }

    /**
     * Sets Board to a Position by a Fen String
     * @param fen Fend String to set the Board by
     */
    //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
    public void setBoardByFen(String fen){
        String fenBoard = fen.substring(0, fen.indexOf(' ')).replace("/","");
        String fenInfo = fen.substring(fen.indexOf(' ')+1).replace("/","");//.replace(" ","");

        //System.out.println(fenBoard);
        //System.out.println(fenInfo);


        int posString=0;
        char c;
        int turnIndex=0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(posString >= fenBoard.length()) break;
                c = fenBoard.charAt(posString++);
                Piece piece;
                switch(c){
                    //Black pieces
                    case 'p':
                        piece =  new Pawn(new ImageView(new Image("Imgs\\B_Pawn.png")), labels[j][i], Color.BLACK, "Black Pawn");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'n':
                        piece = new Knight(new ImageView(new Image("Imgs\\B_Knight.png")), labels[j][i], Color.BLACK, "Black Knight");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'b':
                        piece = new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), labels[j][i], Color.BLACK, "Black Bishop");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'r':
                        piece =new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[j][i], Color.BLACK, "Black Rook");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'q':
                        piece = new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), labels[j][i], Color.BLACK, "Black Queen");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'k':
                        piece = new King(new ImageView(new Image("Imgs\\B_King.png")), labels[j][i], Color.BLACK, "Black King");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        b_King = (King)piece;
                        break;
                    //White pieces
                    case 'P':
                        piece = new Pawn(new ImageView(new Image("Imgs\\W_Pawn.png")), labels[j][i], Color.WHITE, "White Pawn");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'N':
                        piece = new Knight(new ImageView(new Image("Imgs\\W_Knight.png")), labels[j][i], Color.WHITE, "White Knight");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'B':
                        piece = new Bishop(new ImageView(new Image("Imgs\\W_Bishop.png")), labels[j][i], Color.WHITE, "White Bishop");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'R':
                        piece = new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[j][i], Color.WHITE, "White Rook");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'Q':
                        piece = new Queen(new ImageView(new Image("Imgs\\W_Queen.png")), labels[j][i], Color.WHITE, "White Queen");
                        getLabels()[j][i].setPiece(piece);
                        this.addPiece(piece);
                        break;
                    case 'K':
                        piece = new King(new ImageView(new Image("Imgs\\W_King.png")), labels[j][i], Color.WHITE, "White King");
                        getLabels()[j][i].setPiece(piece);
                        w_King = (King)piece;
                        this.addPiece(piece);
                        break;
                    //nl
                    case '/': continue;
                }
                //blank space
                if((c > 49) && (c<=57)){
                    j+=(c-49);
                }


            }
        }

        for (int j = 0; j < fenInfo.length(); j++) {
            c = fenInfo.charAt(j);

            switch(c){
                case 'w': Turn.setColorToMove(Color.WHITE); break; //System.out.println("Whites Turn"); break;//Weiß am zug
                case 'b': Turn.setColorToMove(Color.BLACK); break;  //System.out.println("Blacks Turn"); break; //Schwarz am Zug
                case 'K': getW_King().setCanCastleKing(true); break; //System.out.println("w king");
                case 'Q':  getW_King().setCanCastleQueen(true); break; //System.out.println("w queen");
                case 'k':  getB_King().setCanCastleKing(true); break; //System.out.println("b king");
                case 'q': getB_King().setCanCastleQueen(true); break; // System.out.println("b queen");

            } //1 = 49   -> 9 = 57   -=45
            //en Passant
            if((c > 97) && (c<=105) && fenInfo.charAt(j+1) != ' '){
                System.out.println("En Passant bei: " + c + fenInfo.charAt(j+1) ); //enPassant ist möglich bei dem feld
                turnIndex = j+3;
            }
            if(c == 45){    //-
                turnIndex = j+2;
            }
        }
        String number="";
        for (int i = turnIndex; i < fenInfo.length(); i++) {
            if(fenInfo.charAt(i) == ' '){
                ruleCounter = Integer.parseInt(number);
                number = "";
                i++;
            }
            number = number + fenInfo.charAt(i);
        }
        fullturn = Integer.parseInt(number);

        turn = (fullturn-1)*2  + (Turn.getColorToMove() == Color.WHITE ? 0 : 1);


        //System.out.println("turn: " + turn + " fullturn " + fullturn + " rule " + ruleCounter);
    }

    public void clearAll(){
        for (FieldLabel[] e : labels) {
            for (FieldLabel h : e) {
                if(h.hasPiece())

                    h.removePiece();
            }
        }
        b_pieces.removeAll(b_pieces);
        w_pieces.removeAll(w_pieces);

    }


    public ArrayList<Piece> getPiecesByColor(Color c){
        return c == Color.BLACK ? b_pieces : w_pieces;
    }

    /**
     * Returns the King of either Black or White
     * @param c Enum Color of King
     * @return King Object
     */
    public King getKing(Color c){
        for (Piece e: getPiecesByColor(c))
        {
            if(e.getName().contains("King")&&e.getColor()==c)
                return (King)e;
        }
        return null;
    }

    /**
     * Returns the Fen String for the Board in its current state
     * @return String Fen
     * !!!Not finished!!!
     */
    public String getBoardAsFen(){
        StringBuilder fen = new StringBuilder();
        for (int i = 0; i < 8; i++)
        {
            int emptyLabels = 0;
            for (int j = 0; j < 8; j++)
            {
                FieldLabel current = getLabelByCoordinates(j,i);

                if(current.getPiece()!=null){
                    if(emptyLabels != 0){
                        fen.append(emptyLabels);
                        emptyLabels = 0;
                    }
                    String name = current.getPiece().getClass().getSimpleName();
                    if(name.equals("Knight"))
                        fen.append((current.getPiece().getColor() == Color.WHITE ? "N":"n"));
                    else
                        fen.append((current.getPiece().getColor() == Color.WHITE ? name.charAt(0):Character.toLowerCase(name.charAt(0))));
                }else
                    emptyLabels++;

            }
            if(emptyLabels != 0)
                fen.append(emptyLabels);
            if(i != 7)
                fen.append('/');
        }
        fen.append(Turn.getColorToMove() == Color.WHITE ? " w " : " b ");
        if(getW_King().isCanCastleKing()) fen.append("K");
        if(getW_King().isCanCastleQueen()) fen.append("Q");
        if(getB_King().isCanCastleKing()) fen.append("k");
        if(getB_King().isCanCastleQueen()) fen.append("q");

        fen.append(" - ");      //Hier ACHTUNG!! da wäre en passant

        fen.append(ruleCounter + " " + fullturn);
        return fen.toString();
    }

    /**
     * Turns Coordinates into the Fieldlabel
     * can Return Null if x or y are invalid
     * @param x x-coordinate of FieldLabel
     * @param y y-Coordinate of FieldLabel
     * @return FieldLabel object or Null
     */
    public FieldLabel getLabelByCoordinates(int x, int y) {
        try { return labels[x][y]; } catch (ArrayIndexOutOfBoundsException ignored){}
        return null;
    }

    /**
     * Getter for Black King
     * @return King Object of Black
     */
    public King getB_King() {
        return b_King;
    }

    /**
     * Getter for White King
     * @return King Object of white
     */
    public King getW_King() {
        return w_King;
    }

}
