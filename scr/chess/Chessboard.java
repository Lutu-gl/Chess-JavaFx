package chess;

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
 * @version 1.41
 */
public class Chessboard extends GridPane {
    private FieldLabel[][] labels = new FieldLabel[8][8];
    private ArrayList<Piece> pieces = new ArrayList<>();

    private int turn=0;

    public Chessboard(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                FieldLabel label = new FieldLabel(i,j,(i % 2 == j % 2) ? Color.BLACK : Color.WHITE);
                label.setStyle((i % 2 == j % 2) ? "-fx-background-color: lightsalmon" : "-fx-background-color: saddlebrown");
                label.setId(""+ (char)(97 + i) + (8-j));
                label.setText(""+ (char)(97 + i) + (8-j));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setBoard(this);
                labels[i][j]=label;
                this.add(label, i, j);
            }

    }
    public void turn(){

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

    public void resetBoard(){
    }

    /**
     *
     * Function sets the pieces by a given fen
     *
     * @param fen This is the fen
     */
    //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1
    public void setBoardByFen(String fen){
        String fenBoard = fen.substring(0, fen.indexOf(' ')).replace("/","");
        String fenInfo = fen.substring(fen.indexOf(' ')+1).replace("/","").replace(" ","");

        System.out.println(fenBoard);
        System.out.println(fenInfo);

        int posString=0;
        char c;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(posString >= fenBoard.length()) break;
                c = fenBoard.charAt(posString++);

                switch(c){
                    //Black pieces
                    case 'p': getLabels()[j][i].setPiece(new Pawn(new ImageView(new Image("Imgs\\B_Pawn.png")), labels[j][i], Color.BLACK, "Black Pawn")); break;
                    case 'n': getLabels()[j][i].setPiece(new Knight(new ImageView(new Image("Imgs\\B_Knight.png")), labels[j][i], Color.BLACK, "Black Knight"));break;
                    case 'b': getLabels()[j][i].setPiece(new Bishop(new ImageView(new Image("Imgs\\B_Bishop.png")), labels[j][i], Color.BLACK, "Black Bishop"));break;
                    case 'r': getLabels()[j][i].setPiece(new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[j][i], Color.BLACK, "Black Rook"));break;
                    case 'q': getLabels()[j][i].setPiece(new Queen(new ImageView(new Image("Imgs\\B_Queen.png")), labels[j][i], Color.BLACK, "Black Queen"));break;
                    case 'k': getLabels()[j][i].setPiece(new King(new ImageView(new Image("Imgs\\B_King.png")), labels[j][i], Color.BLACK, "Black King"));break;
                    //White pieces
                    case 'P': getLabels()[j][i].setPiece(new Pawn(new ImageView(new Image("Imgs\\W_Pawn.png")), labels[j][i], Color.WHITE, "White Pawn"));break;
                    case 'N': getLabels()[j][i].setPiece(new Knight(new ImageView(new Image("Imgs\\W_Knight.png")), labels[j][i], Color.WHITE, "White Knight"));break;
                    case 'B': getLabels()[j][i].setPiece(new Bishop(new ImageView(new Image("Imgs\\W_Bishop.png")), labels[j][i], Color.WHITE, "White Bishop"));break;
                    case 'R': getLabels()[j][i].setPiece(new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[j][i], Color.WHITE, "White Rook"));break;
                    case 'Q': getLabels()[j][i].setPiece(new Queen(new ImageView(new Image("Imgs\\W_Queen.png")), labels[j][i], Color.WHITE, "White Queen"));break;
                    case 'K': getLabels()[j][i].setPiece(new King(new ImageView(new Image("Imgs\\W_King.png")), labels[j][i], Color.WHITE, "White King"));break;
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
                case 'w': System.out.println("Whites Turn"); break;//Weiß am zug
                case 'b': System.out.println("Blacks Turn"); break; //Schwarz am Zug
                case 'K': System.out.println("Whites Kingside"); break; //Weiß kann Kingside castlen
                case 'Q': System.out.println("Whites Queenside"); break; //Weiß kann Queenside castlen
                case 'k': System.out.println("Black Kingside"); break; //Schwarz kann Kingside castlen
                case 'q': System.out.println("Black Queenside"); break; //Schwarz kann Queenside castlen

            }
            //en Passant
            if((c > 97) && (c<=105)){
                System.out.println("En Passant bei: " + c + fenInfo.charAt(j+1) ); //enPassant ist möglich bei dem feld
            }

        }

    }
    public String getBoardByFen(){
        return "";
    }

    public FieldLabel getLabelByCoordinates(int x, int y) {
        return labels[x][y];
    }
}
