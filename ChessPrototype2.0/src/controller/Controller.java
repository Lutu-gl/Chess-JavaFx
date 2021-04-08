package controller;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import model.Chessboard;
import model.Field;
import model.Turn;
import view.ChessboardView;
import view.FieldLabel;

import java.util.ArrayList;

public class Controller implements EventHandler<MouseEvent>{

    private FieldLabel source=null;
    private FieldLabel target=null;
    private ArrayList<FieldLabel> highlighted = new ArrayList<>();
    private FieldLabel selectedLabel=null;
    private static Controller instance=null;

    private Controller(){

    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {

        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Field clickedField = Chessboard.getInstance().getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
        if(source == null)
        {
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) return;
            source = clickedFieldLabel;
            selectLabel(source);
            markAvailableMoves(Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()]);
        }
        else {
            if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(Chessboard.getInstance().getColorToMove())) {
                source.unselect();
                unmarkAvailableMoves();
                if (clickedFieldLabel != source) {
                    source = clickedFieldLabel;
                    selectLabel(source);
                    markAvailableMoves(Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()]);
                }
                return;
            }
            target = clickedFieldLabel;
            for (int i = 0; i < highlighted.size(); i++) {
                if (highlighted.get(i).getLine() == target.getLine() && highlighted.get(i).getColumn() == target.getColumn()) {
                    Turn turn = new Turn(source, target);
                    unSelectLabel();
                    source = null;

                    unmarkAvailableMoves();
                    Chessboard.getInstance().handleTurn(turn);
                    break;
                }
            }
        }
    }

    public void markAvailableMoves(Field f) {
        for (Field d : f.getPiece().getMoves())
        {
            if(!d.hasPiece()){
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).mark();
            }
            else{
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).outline();
            }
            highlighted.add(ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()));
        }
    }

    public void unmarkAvailableMoves() {
        highlighted.forEach(fieldLabel -> {
            if(fieldLabel.isMarked())
                fieldLabel.unmark();
            else
                fieldLabel.removeOutline();
        });
        highlighted.clear();
    }

    public void selectLabel(FieldLabel f) {
        f.select();
        selectedLabel = f;
    }

    public void unSelectLabel() {
        selectedLabel.unselect();
        selectedLabel = null;
    }

    public void setSource(FieldLabel source) {
        this.source = source;
    }

    public void setTarget(FieldLabel target) {
        this.target = target;
    }

    public ArrayList<FieldLabel> getHighlighted() {
        return highlighted;
    }

    public FieldLabel getSource() {
        return source;
    }

    public FieldLabel getTarget() {
        return target;
    }

}
