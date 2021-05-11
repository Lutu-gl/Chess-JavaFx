package controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.*;
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

    private FieldLabel lastLabelSource;
    private FieldLabel lastLabelTarget;


    private FieldLabel sourcePreMove = null;
    private FieldLabel targetPreMove = null;
    private boolean isPremove = false;

    Chessboard chessboard = Chessboard.getInstance();
    private Controller(){

    }

    public static Controller getInstance(){
        if(instance == null) instance = new Controller();
        return instance;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        //System.out.println("Gamephase: " + chessboard.getGamephase());
        // This is just testing to start the AI
        if (chessboard.getPlaysAI()[0]&&chessboard.getPlaysAI()[1])
            chessboard.endTurn();
        System.out.println(chessboard.AIThinking);
        //if(chessboard.AIThinking) return;
        if(chessboard.AIThinking){
            FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();

            if(sourcePreMove != null && targetPreMove != null){
                unSelectLabelPremove();
            }

            if(sourcePreMove == null){
                //Schauen ob Piece oben ist.
                 if(clickedFieldLabel.getGraphic() == null) return;
                 sourcePreMove = clickedFieldLabel;
                 clickedFieldLabel.selectPremoveSource();
            } else {
                 if(targetPreMove != null){
                     unSelectLabelPremove();
                     return;
                 }
                 targetPreMove = clickedFieldLabel;

                 if(targetPreMove == sourcePreMove){
                     unSelectLabelPremove();
                     return;
                 }

                 targetPreMove.selectPremoveTarget();
                 isPremove = true;
            }
            
            return;
        }else{
            if(sourcePreMove != null || targetPreMove != null){
                unSelectLabel();
            }
        }

        if (!chessboard.getGamestate().equals(Gamestate.PLAYING) ||
           (chessboard.getColorToMove().equals(Color.WHITE) && chessboard.getPlaysAI()[0]) ||
           (chessboard.getColorToMove().equals(Color.BLACK) && chessboard.getPlaysAI()[1])) return;

        FieldLabel clickedFieldLabel = (FieldLabel) mouseEvent.getSource();
        Field clickedField = chessboard.getFields()[clickedFieldLabel.getLine()][clickedFieldLabel.getColumn()];
        if(source == null)
        {
            if (!clickedField.hasPiece() || !clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) return;
            source = clickedFieldLabel;
            selectLabel(source);
            markAvailableMoves(chessboard.getFields()[source.getLine()][source.getColumn()], source);
        }
        else {
            if (clickedField.hasPiece() && clickedField.getPiece().getColor().equals(chessboard.getColorToMove())) {
                source.unselect();
                unmarkAvailableMoves();
                if (clickedFieldLabel != source) {
                    source = clickedFieldLabel;
                    selectLabel(source);
                    markAvailableMoves(chessboard.getFields()[source.getLine()][source.getColumn()], source);
                } else { //Wenns das selbe Piece ist, dann wird source auf null gesetzt!
                    source = null;
                }
                return;
            }
            target = clickedFieldLabel;
            for (int i = 0; i < highlighted.size(); i++) {
                if (highlighted.get(i).getLine() == target.getLine() && highlighted.get(i).getColumn() == target.getColumn()) {
                    Turn turn = new Turn(chessboard.getFields()[source.getLine()][source.getColumn()], chessboard.getFields()[target.getLine()][target.getColumn()]);
                    unSelectLabel();


                    source = null;

                    unmarkAvailableMoves();
                    chessboard.handleTurn(turn);
                    break;
                }
            }
        }
    }
    public void handlePremove(){    //wird von endTurn aufgerufen wenn es einen Premove gibt!

        //if(!isPremove) return;
        if(sourcePreMove == null || targetPreMove== null){
            unSelectLabelPremove();
            return;
        }
        Field clickedFieldSource = chessboard.getFields()[sourcePreMove.getLine()][sourcePreMove.getColumn()];
        Field clickedFieldTarget = chessboard.getFields()[targetPreMove.getLine()][targetPreMove.getColumn()];

        if (!clickedFieldSource.hasPiece() || !clickedFieldSource.getPiece().getColor().equals(chessboard.getColorToMove())){
            unSelectLabelPremove();
            return;
        }
        if(clickedFieldTarget.hasPiece() && clickedFieldTarget.getPiece().getColor() == chessboard.getColorToMove()){
            unSelectLabelPremove();
            return;
        }

        if(!chessboard.isLegal(clickedFieldTarget, clickedFieldSource, chessboard.getColorToMove())){
            unSelectLabelPremove();
            return;
        }
        for(Field f : clickedFieldSource.getPiece().getMoves()){
            if(f == clickedFieldTarget){
                Turn turn = new Turn(chessboard.getFields()[sourcePreMove.getLine()][sourcePreMove.getColumn()], chessboard.getFields()[targetPreMove.getLine()][targetPreMove.getColumn()]);
                chessboard.handleTurn(turn);
            }
        }
        unSelectLabelPremove();
    }

    public void markAvailableMoves(Field f, FieldLabel source) {
        for (Field d : f.getPiece().getMoves())
        {

            if (!chessboard.isLegal(d, chessboard.getFields()[source.getLine()][source.getColumn()], chessboard.getColorToMove())) continue;


            if(!d.hasPiece()){
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).mark();
            }
            else{
                ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()).outline();
            }
            highlighted.add(ChessboardView.getBoard().get(d.getLine()).get(d.getColumn()));
        }
    }

    public void markLastPlayedMove(){
        if (chessboard.getTurns().size() == 0)
            return;

        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelSource != null) lastLabelSource.getStyleClass().remove("lastPlayedFieldBlack");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldWhite");
        if(lastLabelTarget != null) lastLabelTarget.getStyleClass().remove("lastPlayedFieldBlack");


        ArrayList<Turn> turns = chessboard.getTurns();
        Turn lastTurn = turns.get(turns.size()-1);

        lastLabelSource = fieldToFieldLabel(lastTurn.getSourceField());
        lastLabelTarget = fieldToFieldLabel(lastTurn.getTargetField());

        if((lastLabelSource.getLine() + lastLabelSource.getColumn()) % 2 == 0){
            lastLabelSource.getStyleClass().add("lastPlayedFieldWhite");
        }else{
            lastLabelSource.getStyleClass().add("lastPlayedFieldBlack");
        }
        if((lastLabelTarget.getLine() + lastLabelTarget.getColumn()) % 2 == 0){
            lastLabelTarget.getStyleClass().add("lastPlayedFieldWhite");
        }else{
            lastLabelTarget.getStyleClass().add("lastPlayedFieldBlack");
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

    public void selectLabelPremove(FieldLabel f) {
        f.select();
        selectedLabel = f;
    }
    public void unSelectLabelPremove() {
        if (sourcePreMove == null) return;
        sourcePreMove.unselect();
        sourcePreMove = null;
        if (targetPreMove == null) return;
        targetPreMove.unselect();
        targetPreMove = null;
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

    public void updateTime(double time, Color c){
        //System.out.println("Time "+ time + c);
        switch(c){
            case BLACK -> Platform.runLater(() -> ((Label)ChessboardView.getTimerVBox().getChildren().get(0)).setText(Double.toString(time))); // 0 == black Label 1 == scrollPane 2 == white Label
            case WHITE -> Platform.runLater(() -> ((Label)ChessboardView.getTimerVBox().getChildren().get(2)).setText(Double.toString(time)));
        }

    }

    public void addMoveToDisplay(String s){
        VBox vb = ChessboardView.getMovesVBox();
        TextArea t = (TextArea) vb.getChildren().get(0);
        if(chessboard.getColorToMove() == Color.WHITE){
            t.appendText(s + "\n");
        }
        else
            t.appendText(s);
            //Platform.runLater(() -> (TextField)(ChessboardView.getMovesVBox().getChildren().get(ChessboardView.getMovesVBox().getChildren().size()-1)));

        //Platform.runLater(() -> ChessboardView.getMovesVBox().getChildren().add(new Text(s)));
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

    public boolean isPremove() {
        return isPremove;
    }

    public void setPremove(boolean premove) {
        isPremove = premove;
    }
    public FieldLabel getSourcePreMove() {
        return sourcePreMove;
    }

    public void setSourcePreMove(FieldLabel sourcePreMove) {
        this.sourcePreMove = sourcePreMove;
    }

    public FieldLabel getTargetPreMove() {
        return targetPreMove;
    }

    public void setTargetPreMove(FieldLabel targetPreMove) {
        this.targetPreMove = targetPreMove;
    }
}
