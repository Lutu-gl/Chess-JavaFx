package Chess;

import Pieces.Pawn;
import Pieces.*;
import Pieces.Piece;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * The actual Board containing a list of FieldLables, the Constructor initializes it with 64 labels
 * aswell as a list of all pieces
 * @author Stefan Hasler
 * @version 1.4
 */
public class Chessboard extends GridPane {
    private FieldLabel[][] labels = new FieldLabel[8][8];
    private ArrayList<Piece> pieces = new ArrayList<>();

    public Chessboard(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                FieldLabel label = new FieldLabel(i,j,(i % 2 == j % 2) ? Color.BLACK : Color.WHITE);
                label.setStyle((i % 2 == j % 2) ? "-fx-background-color: saddlebrown" : "-fx-background-color: lightsalmon");
                label.setId(""+ (char)(97 + i) + (j+1));
                label.setText(""+ (char)(97 + i) + (j+1));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setBoard(this);
                labels[i][j]=label;
                this.add(label, i, j);
            }

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
        fen = fen.replace("/","");

        System.out.println(fen);

        int posString=0;
        char c;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(posString >= fen.length()) break;
                c = fen.charAt(posString++);

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
                    System.out.println(j);
                    j+=(int) (c-49);
                    System.out.println("nach hinzurechnen: " + j);
                }


                //System.out.println(labels[j][i].getX() + " " + labels[j][i].getY());


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
