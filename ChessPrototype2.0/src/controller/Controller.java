package controller;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import model.Chessboard;
import model.Field;
import model.Gamestate;
import model.Turn;
import model.pieces.Pawn;
import model.pieces.Piece;
import view.ChessboardView;
import view.FieldLabel;

import java.util.ArrayList;

public class Controller implements EventHandler<MouseEvent>{

    private FieldLabel source=null;
    private FieldLabel target=null;
    private ArrayList<FieldLabel> highlighted = new ArrayList<>();
    private FieldLabel selectedLabel=null;
    private FieldLabel checkLabel=null;
    private static Controller instance=null;

    private Controller(){

    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {

        if (!Chessboard.getInstance().getState().equals(Gamestate.PLAYING)) return;

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
                } else { //Wenns das selbe Piece ist, dann wird source auf null gesetzt!
                    source = null;
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

            if (!Chessboard.getInstance().isLegal(d, Chessboard.getInstance().getFields()[source.getLine()][source.getColumn()], Chessboard.getInstance().getColorToMove())) continue;


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
        if (highlighted == null) return;
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
        if (selectedLabel == null) return;
        selectedLabel.unselect();
        selectedLabel = null;
    }

    public FieldLabel fieldToFieldLabel(Field f){
        return ChessboardView.getBoard().get(f.getLine()).get(f.getColumn());
    }

    public void markCheck(FieldLabel f){
        checkLabel = f;
        f.markAsCheck();
    }
    public void unmarkCheck(){
        if(checkLabel==null) return;
        checkLabel.unmarkAsCheck();
        checkLabel = null;
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

    public Piece promotionDialog(Pawn pawn) {
        return new PromotionDialog(pawn).getResult();
    }
}
