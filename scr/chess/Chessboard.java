package Chess;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class Chessboard extends GridPane {
    private ArrayList<FieldLabel> labels = new ArrayList<>();

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
