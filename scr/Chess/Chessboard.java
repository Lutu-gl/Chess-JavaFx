package Chess;

import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

/**
 * The actual Board containing a list of FieldLables, the Constructor initializes it with 64 labels
 * @author Stefan Hasler
 * @version 1.0
 */
public class Chessboard extends GridPane {
    private final ArrayList<FieldLabel> labels = new ArrayList<>();

    public Chessboard(){
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++){
                FieldLabel label = new FieldLabel(i,j,(i % 2 == j % 2) ? Color.BLACK : Color.WHITE);
                label.setStyle((i % 2 == j % 2) ? "-fx-background-color: saddlebrown" : "-fx-background-color: lightsalmon");
                label.setId(""+ (char)(97 + i) + (j+1));
                label.setText(""+ (char)(97 + i) + (j+1));
                label.setTextAlignment(TextAlignment.CENTER);
                labels.add(label);
                this.add(label, i, j);
            }

    }

    public ArrayList<FieldLabel> getLabels() {
        return labels;
    }

    public void resetBoard(){
    }
    public void setBoardByFen(String fen){
        
    }
    public String getBoardByFen(){
        return "";
    }
}
