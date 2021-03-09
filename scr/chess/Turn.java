package chess;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import pieces.*;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * @author Stefan Hasler
 * @version 3.2
 * A Eventhandler to move pieces
 */

public class Turn implements EventHandler<MouseEvent>{

    /**The Move that the user wishes to perform*/
    private static Move move = null;

    /**The Color which has to move*/
    private static Color colorToMove = Color.WHITE;

    @Override
    public void handle(MouseEvent event) {
        //King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();


        FieldLabel evenSource = (FieldLabel) event.getSource();
        if(move == null)
        {
            move = new Move(evenSource, null);

            if(move.getSource().getPiece() != null && move.getSource().getPiece().getColor() == colorToMove) //Checks if theres a Piece on the Field and if its has the right color to move
            {
                move.setMovingPiece(move.getSource().getPiece());
                /*
                System.out.println("my moves: ");
                for (Move e :move.getMovingPiece().calculateValidMoves(Move.board))
                {
                    System.out.println(e);
                }
                 */
                highlightPiece(move.getSource());
            }
            else{
                move = null;
            }
        }
        else{ //A valid piece has been selected
            move.setTarget((FieldLabel) event.getSource());
            King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();

            //Checks if Target and Source are the same and if the move is legal
            if(isValidMove(move)!=null){
                move=isValidMove(move);
                String s = Move.board.getBoardAsFen();
                if(tryMove(move)){

                    //moving the piece
                    makeMove(move);
                    //undoMove();

                    //Checking for Promotion
                    checkPawnPromotion(move.getMovingPiece());
                    //Checking if castling rights were broken or not
                    checkCastleRights(move.getMovingPiece());
                    //Checking for Castle move
                    checkCastle(move);


                    //Ending Turn
                    unhighlightPiece(move.getSource());
                    move.getMovingPiece().postTurn(move);
                    Move.board.endTurn();
                    move = null;


                    colorToMove = colorToMove == Color.WHITE ? Color.BLACK : Color.WHITE;
                }
                else{
                    undoMove(s, move);
                    unhighlightPiece(move.getSource());
                    move = null;
                }
            }
            else{
                unhighlightPiece(move.getSource());
                move = null;
            }
        }
    }

    private void makeMove(Move m) {
        //Make sure Pieces are removed
        //try{move.getEatenPiece().getFieldLabel().removePiece(); Move.board.removePiece(move.getEatenPiece());}catch(NullPointerException ignored){};
        System.out.println(Move.board.getW_King().getFieldLabel());
        System.out.println(m.getMovingPiece().getFieldLabel());
        System.out.println(m.getMovingPiece() == Move.board.getW_King());


        m.getSource().removePiece();

        //Moving the Piece to the Targetfieldlabel
        m.getMovingPiece().setFieldLabel(m.getTarget());
        m.getTarget().setPiece(m.getMovingPiece());

        System.out.println(Move.board.getW_King().getFieldLabel());
        System.out.println(m.getMovingPiece().getFieldLabel());
        System.out.println(m.getMovingPiece() == Move.board.getW_King());
    }

    public static Color getColorToMove() {
        return colorToMove;
    }

    private void undoMove(String fen, Move m){

        Move.board.clearAll();
        //Move.board.setBoardByFen("8/8/8/8/8/8/8/8 w - - 0 1");
        Move.board.setBoardByFen(fen);

        m.setMovingPiece(Move.board.getLabelByCoordinates(m.getSource().getX(), m.getSource().getY()).getPiece());

    }

    public static void highlightPiece(FieldLabel label){
        label.setStyle(label.getStyle() + ';' + (label.getStyle().equals("-fx-background-color: #F0D9B5;") ? "-fx-background-color: #829769;" : "-fx-background-color: #646F40;"));
    }

    public static void unhighlightPiece(FieldLabel label){
        label.setStyle((label.getStyle().contains("-fx-background-color: #829769;") ?  "-fx-background-color: #F0D9B5;" : "-fx-background-color: #B58863;"));
    }

    private Move isValidMove(Move m){
        for (Move e : m.getMovingPiece().calculateValidMoves(Move.board))
        {
            //System.out.println(e.getTarget() + "----" + move.getTarget());
            if(e.getTarget().equals(m.getTarget())){
                return e;
            }
        }
        return null;
    }

    private void checkPawnPromotion(Piece p){

        if(p.getName().contains("Pawn") && (p.getFieldLabel().getY() == 0 || p.getFieldLabel().getY() == 7))
            new PromotionDialog((Pawn)p).show();
    }

    private void checkCastle(Move m){
        if(m.getMovingPiece() instanceof King){

            FieldLabel[][] labels = m.getTarget().getBoard().getLabels();

            if(m.getSource().getX() + 1 < m.getTarget().getX()) { //Kingside
                System.out.println("Es wurde gecastlelt");
                if(m.getMovingPiece().getColor() == Color.WHITE){ //White
                    labels[7][7].removePiece();
                    labels[5][7].setPiece(new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[5][7], Color.WHITE, "White Rook"));
                }else if(m.getMovingPiece().getColor() == Color.BLACK){//Black
                    labels[7][0].removePiece();
                    labels[5][0].setPiece(new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[5][0], Color.BLACK, "Black Rook"));
                }
            }

            if(m.getSource().getX() -1 > m.getTarget().getX()){ //Queenside
                System.out.println("Es wurde gecastlelt");
                if(m.getMovingPiece().getColor() == Color.WHITE){ //White
                    labels[0][7].removePiece();
                    labels[3][7].setPiece(new Rook(new ImageView(new Image("Imgs\\W_Rook.png")), labels[3][7], Color.WHITE, "White Rook"));
                }else if(m.getMovingPiece().getColor() == Color.BLACK){//Black
                    labels[0][0].removePiece();
                    labels[3][0].setPiece(new Rook(new ImageView(new Image("Imgs\\B_Rook.png")), labels[3][0], Color.BLACK, "Black Rook"));
                }
            }


        }
    }

    private void checkCastleRights(Piece p){

        if(!(p instanceof Rook || p instanceof King)) return;

        FieldLabel[][] labels  = p.getFieldLabel().getBoard().getLabels();

        if(!labels[4][1].hasPiece() && !labels[4][7].hasPiece() && !(labels[4][1].getPiece() instanceof King) && !(labels[4][7].getPiece() instanceof King)){
            return;
        }


        if(p instanceof King){
            ((King) p).setCanCastleKing(false);
            ((King) p).setCanCastleQueen(false);
            return;
        }

        if(p.getColor() == Color.WHITE){
            if(p.getFieldLabel().getX() > 4) Move.board.getW_King().setCanCastleKing(false);
            if(p.getFieldLabel().getX() < 4) Move.board.getW_King().setCanCastleQueen(false);
        }
        if(p.getColor() == Color.BLACK){
            if(p.getFieldLabel().getX() > 4) Move.board.getB_King().setCanCastleKing(false);
            if(p.getFieldLabel().getX() < 4) Move.board.getB_King().setCanCastleQueen(false);
        }
    }

    private boolean tryCastle(Move m){

        if(m.getMovingPiece() instanceof King){

            String s = Move.board.getBoardAsFen();
            FieldLabel[][] labels = m.getTarget().getBoard().getLabels();
            King k = (King) m.getMovingPiece();
            if(m.getSource().getX() + 1 < m.getTarget().getX()) { //Kingside
                System.out.println("kingside");
                if(m.getMovingPiece().getColor() == Color.WHITE){ //White
                    System.out.println("kingside castle white trymove");
                    if(k.isInCheck()) return false;

                    makeMove(new Move(labels[4][7],labels[5][7]));
                    System.out.println(k.getFieldLabel().getX());
                    if( k.isInCheck()){
                        System.out.println("if 1");
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[5][7],labels[6][7]));
                    if(k.isInCheck()){
                        System.out.println("if 2");
                        undoMove(s, m);
                        return false;
                    }
                    System.out.println("test");
                }else if(m.getMovingPiece().getColor() == Color.BLACK){//Black
                    if(((King) m.getMovingPiece()).isInCheck()) return false;
                    makeMove(new Move(labels[4][0],labels[5][0]));
                    if(((King) m.getMovingPiece()).isInCheck()){
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[5][0],labels[6][0]));
                    if(((King) m.getMovingPiece()).isInCheck()){
                        undoMove(s, m);
                        return false;
                    }
                }
            }

            if(m.getSource().getX() -1 > m.getTarget().getX()){ //Queenside
                System.out.println("Queenside!");
                if(m.getMovingPiece().getColor() == Color.WHITE){ //White
                    if(((King) m.getMovingPiece()).isInCheck()) return false;
                    makeMove(new Move(labels[4][7],labels[3][7]));
                    if(((King) m.getMovingPiece()).isInCheck()){
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[3][7],labels[2][7]));
                    if(((King) m.getMovingPiece()).isInCheck()){
                        undoMove(s, m);
                        return false;
                    }
                }else if(m.getMovingPiece().getColor() == Color.BLACK){//Black
                    if(((King) m.getMovingPiece()).isInCheck()) return false;
                    makeMove(new Move(labels[4][0],labels[3][0]));
                    if(((King) m.getMovingPiece()).isInCheck()){
                        undoMove(s, m);
                        return false;
                    }
                    makeMove(new Move(labels[3][0],labels[2][0]));
                    if(((King) m.getMovingPiece()).isInCheck()){
                        undoMove(s, m);
                        return false;
                    }
                }
            }
        }
        return true;
    }

    protected boolean tryMove(Move m){
        //For castling
        String s = Move.board.getBoardAsFen();
        System.out.println("Fen vor undo mvoe: " + Move.board.getBoardAsFen());

        boolean trycastle = true;

        trycastle = tryCastle(m);
        System.out.println("trycastle: " + trycastle);

        undoMove(s, m);

        System.out.println("Fen nach undo mvoe: " + Move.board.getBoardAsFen());


        King k = colorToMove == Color.WHITE ? Move.board.getW_King() : Move.board.getB_King();


        //System.out.println("Fen nach castle mvoe: " + Move.board.getBoardAsFen());

        makeMove(m);
        //System.out.println("Fen nach Try mvoe: " + Move.board.getBoardAsFen());

        if(k.isInCheck()){
            System.out.println("der move geht nicht");
            System.out.println("\t" + m);

            undoMove(s, m);
            return false;
        }
        //System.out.println(m + "#" + k.isInCheck() + "#" + k.toString());
        undoMove(s, m);
        return trycastle;
    }
}

/*

 */