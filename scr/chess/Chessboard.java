package chess;

import javafx.scene.control.CheckBox;
import pieces.Pawn;
import pieces.*;
import pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * The actual Board containing a list of FieldLabels, the Constructor initializes it with 64 labels
 * aswell as a list of all pieces
 * @author Stefan Hasler
 * @version 1.45
 */
public class Chessboard extends GridPane {
    private FieldLabel[][] labels = new FieldLabel[8][8];
    private ArrayList<Piece> pieces = new ArrayList<>();
    private int turn=0;

    private King b_King;
    private King w_King;


    public Chessboard(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                FieldLabel label = new FieldLabel(i,j,(i % 2 == j % 2) ? Color.BLACK : Color.WHITE);
                label.setStyle((i % 2 == j % 2) ? "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;");
                label.setId(""+ (char)(97 + i) + (8-j));
                label.setText(""+ (char)(97 + i) + (8-j));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setBoard(this);
                labels[i][j]=label;
                this.add(label, i, j);
            }
        Move.board = this;
    }

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

    public void endTurn(){
        turn++;
    }
    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void addPiece(Piece p){
        pieces.add(p);
    }

    public void removePiece(Piece p){
        pieces.remove(p);
    }

    public FieldLabel[][] getLabels() {
        return labels;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void resetBoard(){
    }

    /**
     * Sets Board to a Position
     * @param fen fen to set Board by
     */
    //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
    public void setBoardByFen(String fen){
        String fenBoard = fen.substring(0, fen.indexOf(' ')).replace("/","");
        String fenInfo = fen.substring(fen.indexOf(' ')+1).replace("/","").replace(" ","");

        //System.out.println(fenBoard);
        //System.out.println(fenInfo);

        int posString=0;
        char c;

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
                case 'w': //System.out.println("Whites Turn"); break;//Weiß am zug
                case 'b': //System.out.println("Blacks Turn"); break; //Schwarz am Zug
                case 'K': //System.out.println("Whites Kingside"); break; //Weiß kann Kingside castlen
                case 'Q': //System.out.println("Whites Queenside"); break; //Weiß kann Queenside castlen
                case 'k': //System.out.println("Black Kingside"); break; //Schwarz kann Kingside castlen
                case 'q': //System.out.println("Black Queenside"); break; //Schwarz kann Queenside castlen

            }
            //en Passant
            if((c > 97) && (c<=105)){
                System.out.println("En Passant bei: " + c + fenInfo.charAt(j+1) ); //enPassant ist möglich bei dem feld
            }

        }

    }
    public King getKing(Color c){
        for (Piece e: getPieces())
        {
            if(e.getName().contains("King")&&e.getColor()==c)
                return (King)e;
        }
        return null;
    }

    //To be implemented
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
        fen.append(" w - 0 1");
        return fen.toString();
    }
    public FieldLabel getLabelByCoordinates(int x, int y) {
        return labels[x][y];
    }

    public Chessboard getClone(){
        try { return (Chessboard)this.clone(); } catch (CloneNotSupportedException ignored) { } return null;

    }
    public King getB_King() {
        return b_King;
    }

    public King getW_King() {
        return w_King;
    }

}
