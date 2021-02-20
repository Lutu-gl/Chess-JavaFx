package Chess;

import Pieces.Piece;
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
    public void setBoardByFen(String fen){
        
    }
    public String getBoardByFen(){
        return "";
    }

    public FieldLabel getLabelByCoordinates(int x, int y) {
        return labels[x][y];
    }
}
