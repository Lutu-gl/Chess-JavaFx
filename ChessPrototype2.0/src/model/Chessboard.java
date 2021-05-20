package model;

import controller.Controller;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.ai.Gamephase;
import model.ai.GamephaseObserver;
import model.ai.AI;
import model.pieces.*;
import view.ChessboardView;
import view.PlaySound;
import view.Sound;
import view.WinningScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

/**
 * Chessboard used for all calculations, implements the singleton pattern
 * @version 5.8
 * @since 1.0
 */

public class Chessboard {

    private Field[][] fields;
    private ArrayList<Turn> turns = new ArrayList<>();
    private int turn, ply;
    private Color colorToMove;
    private int ruleCounter;
    private Pawn enPassantable, whitePromotionable, blackPromotionable;
    private ArrayList<String> fens = new ArrayList<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> eatenPieces = new ArrayList<>();
    private Gamestate gamestate;
    private Gamephase gamephase = Gamephase.MIDGAME;
    private boolean whiteCastlePermissionLong, whiteCastlePermissionShort, blackCastlePermissionLong, blackCastlePermissionShort, playerConnected;
    private King b_king, w_king;
    private Sound playSound;
    private int sizeOfBoard;
    ArrayList<Observer> observers = new ArrayList<>();
    private boolean[] playsAI = new boolean[2];
    private long whiteTime= 90000;  //5min
    private long blackTime= 90000;
    private long whiteInkrement = 0;
    private long blackInkrement = 0;
    private long timeStopped=0L;
    private Timer timer=new Timer();
    public boolean withTime=true;
    private Color currentAIMovingColor =null;
    private long turnTimeAdder=0L;
    private HashMap<String, String> openingBook;
    public boolean AIThinking=false;
    public boolean debug = false;
    private boolean isAllowedToMakeMove = true;
    private int queensEaten = 0;

    // Singleton pattern
    private static Chessboard instance = null;


    private Chessboard() {
        register(new GamestateObserver(this));
        register(new GamephaseObserver(this));
    }

    public static Chessboard getInstance() {
        if (instance == null)
            instance = new Chessboard();

        return instance;
    }

    public void reset() {
        if (instance != null)
            instance = null;
    }

    /**
     *  Constructor to set up a board of size * size for 2 Human players
     * @param size Needs to be >0, recommended 8, entering very high/low values may break break the game
     */
    public void createBoard(int size){
        createBoard(size, false, false);
    }

    /**
     * Constructor to set up a board of size * size for Human and A.I players
     * Sets time to 5minutes and 0 Increment to 0
     * @param size Needs to be >0, recommended 8, entering very high/low values may break break the game
     * @param whiteAI true to let A.I play white
     * @param blackAI true to let A.I play black
     */
    public void createBoard(int size, boolean whiteAI, boolean blackAI){
        createBoard(size, whiteAI, blackAI, 300, 300, 0, 0);
    }

    /**
     * Constructor to set up a board of size * size for Human and A.I players, also enables to enter custom Time and Increment for both Players
     * @param size Needs to be >0, recommended 8, entering very high/low values may break break the game
     * @param whiteAI true to let A.I play white
     * @param blackAI true to let A.I play black
     * @param whiteTime long time in seconds, should be >0
     * @param blackTime long time in seconds, should be >0
     * @param whiteInkrement increases timer for white by x seconds after making move
     * @param blackInkrement increases timer for black by x seconds after making move
     */
    public void createBoard(int size, boolean whiteAI, boolean blackAI, long whiteTime, long blackTime, long whiteInkrement, long blackInkrement){
        this.whiteTime = (long) (whiteTime*1e3);
        this.blackTime = (long) (blackTime*1e3);
        this.whiteInkrement = (long) (whiteInkrement*1e3);
        this.blackInkrement = (long) (blackInkrement*1e3);

        this.sizeOfBoard = size;
        gamestate = Gamestate.PLAYING;
        playsAI[0] = whiteAI;
        playsAI[1] = blackAI;
        if (playsAI[0] || playsAI[1]) {
            openingBook = new HashMap<>();
            File myObj = new File("openingBook.csv");
            if (myObj.exists()) {
                Scanner myReader = null;
                try {
                    myReader = new Scanner(myObj);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                while (myReader.hasNextLine()) {
                    String[] data = myReader.nextLine().split(";");
                    openingBook.put(data[0], data[1]);
                }
            }
        }
        fields = new Field[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                fields[i][j] = new Field(j, i);
            }
        }
        if(whiteAI && blackAI){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Chessboard ch = Chessboard.getInstance();
                    if(timeStopped == 0) return;
                    long timeNow=0L;

                    if(currentAIMovingColor == Color.WHITE){
                        timeNow = (ch.whiteTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        Controller.getInstance().updateTime((timeNow/1000.00), Color.WHITE);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.WHITE);
                            if(checkIfColorCanWinWithTheMaterial(Color.BLACK)) gamestate = Gamestate.BLACK_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    else{
                        timeNow = (ch.blackTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        //System.out.println(whiteTime/1e9 + "  -  " + timeNow/1e9);
                        Controller.getInstance().updateTime((timeNow/1000.000), Color.BLACK);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.BLACK);
                            if(checkIfColorCanWinWithTheMaterial(Color.WHITE)) gamestate = Gamestate.WHITE_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    observers.get(0).update();      //I dont like this!
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1);
        }
        else if(!whiteAI && !blackAI){
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Chessboard ch = Chessboard.getInstance();
                    if(timeStopped == 0) return;
                    long timeNow=0L;

                    if(colorToMove == Color.WHITE){
                        timeNow = (ch.whiteTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        Controller.getInstance().updateTime((timeNow/1000.00), Color.WHITE);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.WHITE);
                            if(checkIfColorCanWinWithTheMaterial(Color.BLACK)) gamestate = Gamestate.BLACK_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    else{
                        timeNow = (ch.blackTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        //System.out.println(whiteTime/1e9 + "  -  " + timeNow/1e9);
                        Controller.getInstance().updateTime((timeNow/1000.000), Color.BLACK);
                        if(timeNow <= 0){
                            if(checkIfColorCanWinWithTheMaterial(Color.WHITE)) gamestate = Gamestate.WHITE_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    observers.get(0).update();      //I dont like this!
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1);
        }
        else if(blackAI && !whiteAI)
        {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Chessboard ch = Chessboard.getInstance();
                    if(timeStopped == 0) return;
                    long timeNow=0L;
                    if(!ch.AIThinking){
                        timeNow = (ch.whiteTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        Controller.getInstance().updateTime((timeNow/1000.00), Color.WHITE);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.WHITE);
                            if(checkIfColorCanWinWithTheMaterial(Color.BLACK)) gamestate = Gamestate.BLACK_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    else{
                        timeNow = (ch.blackTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        //System.out.println(whiteTime/1e9 + "  -  " + timeNow/1e9);
                        Controller.getInstance().updateTime((timeNow/1000.000), Color.BLACK);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.BLACK);
                            if(checkIfColorCanWinWithTheMaterial(Color.WHITE)) gamestate = Gamestate.WHITE_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    observers.get(0).update();  //I dont like this!
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1);
        }
        else{
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Chessboard ch = Chessboard.getInstance();
                    if(timeStopped == 0) return;
                    long timeNow=0L;
                    if(!ch.AIThinking){
                        timeNow = (ch.blackTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        //System.out.println(whiteTime/1e9 + "  -  " + timeNow/1e9);
                        Controller.getInstance().updateTime((timeNow/1000.000), Color.BLACK);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.BLACK);
                            if(checkIfColorCanWinWithTheMaterial(Color.WHITE)) gamestate = Gamestate.WHITE_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }else{
                        timeNow = (ch.whiteTime - (System.currentTimeMillis() - timeStopped) + turnTimeAdder);
                        Controller.getInstance().updateTime((timeNow/1000.00), Color.WHITE);
                        if(timeNow <= 0){
                            Controller.getInstance().updateTime(0, Color.WHITE);
                            if(checkIfColorCanWinWithTheMaterial(Color.BLACK)) gamestate = Gamestate.BLACK_WINS_ON_TIME;
                            else gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
                        }
                    }
                    observers.get(0).update();  //I dont like this!
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 1);
            controller.Controller.getInstance().flipTimers();

        }


    }


    public void printBoard(){
        if(fields == null){
            System.out.println("No board has been created yet");
            return;
        }
        /*for (int i = 0; i < fields.length; i++)
        {
            for (int j = 0; j < fields.length; j++)
            {
                if(fields[j][i].hasPiece())
                    System.out.println(fields[j][i].getPiece().getShortName());
                else
                    System.out.print("\\");
                if(j+1 == fields.length){
                    System.out.println();
                }
                else
                    System.out.print("-");
            }
            System.out.println("-------------------");
        }*/


        for (int i = 0; i < fields.length; i++) {
            for (int x = 0; x < fields[i].length; x++) {

                if (fields[i][x].hasPiece())
                    System.out.print(fields[i][x].getPiece().getShortName());
                else
                    System.out.print("-");
            }
            System.out.println();
        }
        System.out.println("\n-------------------\n");


    }


    /**
     * Adds Piece p to its corresponding ArrayList
     * @param p Piece to add
     */
    public void addPiece(Piece p){
        if(p==null) return;
        if(p.getColor() == Color.BLACK)
            blackPieces.add(p);
        else
            whitePieces.add(p);
    }

    public void addFen(String fen){
        fen  = fen.substring(0, fen.lastIndexOf(' '));
        fen  = fen.substring(0, fen.lastIndexOf(' '));

        fens.add(fen);
    }

    /**
     * Removes Piece from its Corresponding ArrayList
     * @param p Piece to delete
     * @return returns true if Piece has been successfully removed
     */
    public boolean removePiece(Piece p){
        if(p.getColor() == Color.BLACK)
            return blackPieces.remove(p);
        else
            return whitePieces.remove(p);
    }

    /**
     * Adds Piece to eatenPieces ArrayList, useful to keep references around so the GarbageCollector doesnt delete them after a Piece is taken
     * @param p Piece to add
     */
    public void addPieceToEaten(Piece p){
        if(p == null) return;
        eatenPieces.add(p);
    }

    /**
     * Removes Piece from eatenPieces ArrayList
     * @param p Piece to remove
     * @return returns true if Piece has been removed successfully
     */
    public boolean removePieceFromEaten(Piece p){
        return eatenPieces.remove(p);
    }

    /**
     * Quick way to get the Black king
     * @return can be Null if King doesnt exist
     */
    public King getB_king() {
        return b_king;
    }

    /**
     * Quick way to get the Black king
     * @return can be Null if King doesnt exist
     */
    public King getW_king() {
        return w_king;
    }

    private Turn t;

    /**
     * Handles turn for a Player and for A.I, also gets called by A.I when calculating best Move, for this Chessboard.debug and Chessboard.withTime gets set to false
     * inorder to not display the move on Chessboard.View.
     * @param currentT turn to handle, cannot be null
     * @return true if the move is played successfully
     */
    public boolean handleTurn(Turn currentT){

        if (playerConnected && !playsAI[colorToMove.equals(Color.WHITE)?0:1] && !debug) {
            try {
                System.out.println("Ich sende irgendwas");
                System.out.println(generateNotation(currentT));
                Server.getOutputStream().writeUTF(generateNotation(currentT));
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Fehler beim Senden an den Server!");
            }
        }

        //System.out.println(whiteInkrement);
        if(timeStopped != 0 && withTime){
            if(colorToMove == Color.WHITE){
                whiteTime-=System.currentTimeMillis() - timeStopped;
                whiteTime += whiteInkrement;
                //Controller.getInstance().updateTime(whiteTime/1000.00, Color.WHITE);
            }else{
                blackTime-=System.currentTimeMillis() - timeStopped;
                blackTime += blackInkrement;
                //Controller.getInstance().updateTime(blackTime/1000.00, Color.BLACK);
            }
            turnTimeAdder = System.currentTimeMillis() - timeStopped;
            System.out.println(whiteTime / 1e3 + " " + blackTime/1e3);
        }
        t = currentT;
        // TODO: Farbe von Check ändern, ist momentan nur Rot
        if (!debug)
            Controller.getInstance().fieldToFieldLabel((colorToMove.equals(Color.WHITE)?w_king:b_king).getField()).unmarkAsCheck();

        Piece p = t.getMovingPiece();
        Field f = t.getTargetField();
        boolean s = true;
        t.setGamestate(gamestate);
        t.setRuleCounter(ruleCounter);
        t.setEnpassantable(enPassantable);
        t.setTurnsPlayed(turn);
        t.setCastlePermissions(getCastlePermissions());

        if (p instanceof King) {
            if (p.getColor().equals(Color.WHITE)) {
                // Check if it is left castling
                if (w_king.getField().getColumn() - f.getColumn() == 2) {
                    movePiece(fields[fields.length - 1][0].getPiece(), fields[fields.length - 1][p.getField().getColumn() - 1], false);
                    PlaySound.play(Sound.CASTLE);
                    t.setCastleTurn(0);
                    s = false;
                }
                // Check if it is right castling
                else if (f.getColumn() - w_king.getField().getColumn() == 2) {
                    movePiece(fields[fields.length - 1][fields.length - 1].getPiece(), fields[fields.length - 1][p.getField().getColumn() + 1], false);
                    PlaySound.play(Sound.CASTLE);
                    t.setCastleTurn(1);
                    s = false;
                }
                whiteCastlePermissionLong = false;
                whiteCastlePermissionShort = false;
            } else {
                // Check if it is left castling
                if (b_king.getField().getColumn() - f.getColumn() == 2) {
                    movePiece(fields[0][0].getPiece(), fields[0][p.getField().getColumn() - 1], false);
                    PlaySound.play(Sound.CASTLE);
                    t.setCastleTurn(0);
                    s = false;
                }
                // Check if it is right castling
                else if (f.getColumn() - b_king.getField().getColumn() == 2) {
                    movePiece(fields[0][fields.length - 1].getPiece(), fields[0][p.getField().getColumn() + 1], false);
                    PlaySound.play(Sound.CASTLE);
                    t.setCastleTurn(1);
                    s = false;
                }
                blackCastlePermissionLong = false;
                blackCastlePermissionShort = false;
            }
        } else if (p instanceof Rook) {
            if (p.getColor().equals(Color.WHITE)) {
                if (p.getField().getLine() == 7 && p.getField().getColumn() == 0)
                    whiteCastlePermissionLong = false;
                else if (p.getField().getLine() == 7 && p.getField().getColumn() == 7)
                    whiteCastlePermissionShort = false;
            } else {
                if (p.getField().getLine() == 0 && p.getField().getColumn() == 0)
                    blackCastlePermissionLong = false;
                else if (p.getField().getLine() == 0 && p.getField().getColumn() == 7)
                    blackCastlePermissionShort = false;
            }
        }

        if (f.hasPiece() && f.getPiece() instanceof Rook) {
            if (f.getPiece().getColor().equals(Color.WHITE)) {
                if (f.getLine() == fields.length-1 && f.getColumn() == 0)
                    whiteCastlePermissionLong = false;
                else if (f.getLine() == fields.length-1 && f.getColumn() == fields.length-1)
                    whiteCastlePermissionShort = false;
            } else {
                if (f.getLine() == 0 && f.getColumn() == 0)
                    blackCastlePermissionLong = false;
                else if (f.getLine() == 0 && f.getColumn() == fields.length-1)
                    blackCastlePermissionShort = false;
            }
        }


        //enPassant als möglich setzen und enPassant handeln:
        enPassantable = null;
        if(t.getMovingPiece() instanceof Pawn){
            //En passant als möglich setzen
            if( (t.getTargetField().getLine() == t.getSourceField().getLine() + 2) || t.getTargetField().getLine() == t.getSourceField().getLine() - 2){
                int line = t.getTargetField().getLine();
                int column = t.getTargetField().getColumn();


                //Look if right is a Pawn
                if (column + 1 < sizeOfBoard) {
                    Field field = fields[line][column+1];
                    if(field.isExists() && field.hasPiece() && field.getPiece() instanceof Pawn && field.getPiece().getColor() != colorToMove){
                        enPassantable = (Pawn) t.getMovingPiece();
                    }
                }
                //Look if left is a Pawn
                if(column - 1 >= 0){
                    Field field = fields[line][column-1];
                    if(field.isExists() && field.hasPiece() && field.getPiece() instanceof Pawn && field.getPiece().getColor() != colorToMove){
                        enPassantable = (Pawn) t.getMovingPiece();
                    }
                }
            }

            //En passant handeln
            if(!t.getTargetField().hasPiece() && t.getSourceField().getColumn() != t.getTargetField().getColumn()){
                Piece removeThisPawn = fields[t.getTargetField().getLine() + (colorToMove == Color.WHITE ? 1 : -1) ][t.getTargetField().getColumn()].getPiece();
                t.setEnpassantTurn(true);
                t.setEatenPiece(removeThisPawn);
                removePiece(removeThisPawn); //Den Bauer löschen (weil das targetField ein anderes ist als wo der andere Bauer steht und das dann nicht gelöscht wird....)
                addPieceToEaten(removeThisPawn);
                removeThisPawn.getField().setPiece(null); //So löscht man den Bauer wirklich
                PlaySound.play(Sound.CAPTURE);
                s = false;
            }
        }


        movePiece(p, f, s);

        colorToMove = colorToMove.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;




        //handeln der 50 move rule
        ruleCounter++;
        if(t.getMovingPiece() instanceof Pawn || t.getEatenPiece() != null){
            ruleCounter = 0;
        }else if(ruleCounter >= 100){
            gamestate = Gamestate.DRAW;
        }

        //handeln der moves
        if(colorToMove == Color.WHITE){
            turn++;
        }

        //handlern der 3 fold repetition
        int threefoldCounter=0;
        addFen(getBoardAsFen());
        if(!debug){
            Controller.getInstance().addPgnToDisplay(turnToPGN(t));
        }
        String fen = fens.get(fens.size()-1);
        for(String fe : fens){
            if(fen.equals(fe)){
                threefoldCounter++;
            }
        }
        if(threefoldCounter >= 3){
            gamestate = Gamestate.PLAYER_CAN_CLAIM_DRAW;
        }

        // Den Zug hinzufügen
        turns.add(t);

        endTurn();
        return true;
    }

    public void movePiece(Piece p, Field f) {
        movePiece(p, f, true);
    }

    /**
     * Moves Piece to Field
     * @param p Piece to move
     * @param f Field to move Piece to
     * @param s Play sound when making move
     */
    public void movePiece(Piece p, Field f, boolean s){
        p.setTimesMoved(p.getTimesMoved()+1);   //times moved erhöhen
        if(!debug) System.out.println("Jetzt wurde das piece insgesammt " + p.getTimesMoved() + " oft gemoved!");
        if(p.getTimesMoved() > 100){
            System.out.println("Hier Fehler:" + p);
        }

        playSound = Sound.MOVE;

        if (f.hasPiece()){
            addPieceToEaten(f.getPiece());
            removePiece(f.getPiece());
            playSound = Sound.CAPTURE;
        }

        p.getField().setPiece(null);//Remove Piece from Source
        p.setField(f); //Update Field in Piece
        f.setPiece(p); //Move Piece to new Field

        //Promotion

        whitePromotionable = null;
        blackPromotionable = null;
        if(p instanceof Pawn) {
            // Falls ein Mensch spielt
            //if ((p.getColor() == Color.WHITE && !playsAI[0]) || ( p.getColor() == Color.BLACK && !playsAI[1])){
            ((Pawn)p).promoteIfPossible(t);
            /*} else {
                if (p.getColor().equals(Color.WHITE) && p.getField().getLine() == 0)
                    whitePromotionable = (Pawn) p;
                else if ()
                    blackPromotionable = (Pawn) p;
            }*/
        }



        King king = (colorToMove.equals(Color.WHITE)) ? b_king : w_king;
        if (king.isInCheck()) {
            if (!debug)
                Controller.getInstance().fieldToFieldLabel(king.getField()).markAsCheck();
            if (checkIfMate(colorToMove)){
                playSound = Sound.MATE;
                gamestate = colorToMove.equals(Color.WHITE) ? Gamestate.WHITE_WINS : Gamestate.BLACK_WINS;
            } else
                playSound = Sound.CHECK;
        }
        // Check if it is stalemate
        else {
            if (whitePieces.size() + blackPieces.size() <= 2) {
                gamestate = Gamestate.DRAW;
            }

            ArrayList<Piece> pieces = colorToMove.equals(Color.WHITE) ? blackPieces : whitePieces;
            boolean stalemate = true;
            for (Piece piece : pieces) {
                for (Field move : piece.getMoves()) {
                    if (isLegal(move, piece.getField(), colorToMove.equals(Color.WHITE)?Color.BLACK:Color.WHITE)) {
                        stalemate = false;
                        break;
                    }
                }
                if (!stalemate) {
                    break;
                }
            }
            if (stalemate) {
                playSound = Sound.STALEMATE;
                gamestate = Gamestate.STALEMATE;
            }
        }

        if (s)
            PlaySound.play(playSound);

        //printBoard();
    }


    /**
     * sets game back to Turn t
     */
    public void undoTurn(Turn t) {

        boolean breakCondition = true;
        while (breakCondition) {
            int counter = turns.size()-1;
            if (t.getTurnNumber() == turns.get(counter).getTurnNumber())
                breakCondition = false;
            undoTurnHelper(turns.get(counter));
        }
    }

    private void undoTurnHelper(Turn t){

        // Set back the color!
        colorToMove = t.getColorToMove();

        // If it was a castling move, put also the rook back to its original position and set castle permissions
        if (t.isCastleTurn()) {
            Piece rook;
            Field destination;
            int line = colorToMove.equals(Color.WHITE) ? sizeOfBoard-1 : 0;
            King king = colorToMove.equals(Color.WHITE) ? w_king : b_king;
            // Check if it was long castling
            if (t.getCastleSide() == 0) {
                rook = fields[line][king.getField().getColumn()+1].getPiece();
                destination = fields[line][0];
            }
            // Otherwise it was short castling
            else {
                rook = fields[line][king.getField().getColumn()-1].getPiece();
                destination = fields[line][sizeOfBoard-1];
            }
            // move the rook
            rook.setTimesMoved(rook.getTimesMoved()-2);
            movePiece(rook, destination, false);
        }
        // If it was an enpassant turn
        if (t.isEnpassantTurn()) {
            int offset = colorToMove.equals(Color.WHITE) ? 1 : -1;
            fields[t.getTargetField().getLine()+offset][t.getTargetField().getColumn()].setPiece(t.getEatenPiece());
            addPiece(t.getEatenPiece());
            removePieceFromEaten(t.getEatenPiece());
            t.setEatenPiece(null);
        }

        if (t.isPromotionTurn()) {
            removePiece(t.getTargetField().getPiece());
            addPiece(t.getMovingPiece());
        }

        // Set castling permissions back
        whiteCastlePermissionLong = t.getCastlePermissions()[0];
        whiteCastlePermissionShort = t.getCastlePermissions()[1];
        blackCastlePermissionLong = t.getCastlePermissions()[2];
        blackCastlePermissionShort = t.getCastlePermissions()[3];

        // Set back the enpassantable pawn
        enPassantable = t.getEnpassantable();

        // Move back the piece to its original place and restore eaten pieces
        t.getSourceField().setPiece(t.getMovingPiece()); //move Piece Back to source
        t.getMovingPiece().setField(t.getSourceField()); //Update Field in Piece
        if (t.getEatenPiece() != null) {//if there has been a eaten Piece it gets reset aswell
            t.getEatenPiece().setField(t.getTargetField());
            t.getTargetField().setPiece(t.getEatenPiece());
            addPiece(t.getEatenPiece());
            removePieceFromEaten(t.getEatenPiece());
        } else
            t.getTargetField().setPiece(null);

        t.getMovingPiece().setTimesMoved(t.getMovingPiece().getTimesMoved() - 1);

        // Check if the kings must be marked or unmarked as check!
        if (!debug) {
            if (w_king.isInCheck())
                Controller.getInstance().fieldToFieldLabel(w_king.getField()).markAsCheck();
            else
                Controller.getInstance().fieldToFieldLabel(w_king.getField()).unmarkAsCheck();
            if (b_king.isInCheck())
                Controller.getInstance().fieldToFieldLabel(b_king.getField()).markAsCheck();
            else
                Controller.getInstance().fieldToFieldLabel(b_king.getField()).unmarkAsCheck();
        }

        // Set back the gamestate
        gamestate = t.getGamestate();

        // Ganzzüge zurücksetzen
        turn = t.getTurnsPlayed();

        // Set back the 50 move rule counter
        ruleCounter = t.getRuleCounter();

        // remove the fen from fens
        fens.remove(fens.size()-1);

        // Remove the turn from played turns and display the board
        turns.remove(turns.size()-1);

        if(isAllowedToMakeMove)
            endTurn();

    }
    //Am ende von jedem Zug

    /**
     * Indicates that a Turn has been played, if Chessboard.debug is set to true displays move on view.ChessboardView
     * Also if a A.I is set to true, Makes A.I take a turn
     */
    public void endTurn(){
        Controller controller = Controller.getInstance();
        //System.out.println("Im endTurn drinnen!");

        if(!debug){
            if(!checkIfColorCanWinWithTheMaterial(Color.WHITE) && !checkIfColorCanWinWithTheMaterial(Color.BLACK)){
                gamestate = Gamestate.DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
            }
        }

        notifyObserver();

        if (!gamestate.equals(Gamestate.PLAYING)) {
            WinningScreen.whiteWins();
        }

        //System.out.println(getBoardAsFen());
        if (!debug && turns.size() > 0){
            Controller.getInstance().markLastPlayedMove();
            ChessboardView.display();
            if(withTime) timeStopped = System.currentTimeMillis();
            if(playsAI[1] && colorToMove==Color.WHITE) AIThinking=false;
            if(playsAI[0] && colorToMove==Color.BLACK) AIThinking=false;
            if(playsAI[1] && colorToMove==Color.BLACK) AIThinking=true;
            if(playsAI[0] && colorToMove==Color.WHITE) AIThinking=true;
        }
        turnTimeAdder = 0;

        // Check if player from other network plays
        if (playerConnected && playsAI[colorToMove.equals(Color.WHITE)?0:1] && gamestate.equals(Gamestate.PLAYING) && !debug) {
            Service<Turn> service = new Service<Turn>() {
                @Override
                protected Task<Turn> createTask() {
                    return new Task<Turn>() {
                        @Override
                        protected Turn call() throws Exception {
                            System.out.println("IIICH WAAAARTE!");
                            Turn s = convertNotation(Server.getInputStream().readUTF());
                            System.out.println(s);
                            return s;
                        }
                    };
                }
            };
            service.setOnSucceeded(e -> {
                handleTurn(service.getValue());
                e.consume();
            });
            service.start();
        }
        // Check if bot plays
        else if (playsAI[colorToMove.equals(Color.WHITE)?0:1] && gamestate.equals(Gamestate.PLAYING) && !debug) {
            //AIThinking=true;
            Service<Turn> service = new Service<>() {
                @Override
                protected Task<Turn> createTask() {
                    return new Task<>() {
                        @Override
                        protected Turn call() throws Exception {
                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            long availableTime = colorToMove.equals(Color.WHITE)?whiteTime:blackTime;
                            Future<Turn> bestMove = executor.submit(new AI());
                            /*
                                Here we let the algorithm work and check in small time periods, if it is already finished
                                After the calculated time above we interrupt the Callable and we get the best move calculated so far
                             */
                            System.out.println("Zeit für Zug: "+ availableTime*0.05);
                            for (int i = 0; i < 20; i++) {
                                if (bestMove.isDone()) break;
                                Thread.sleep((long)(((availableTime)*0.05)/20));
                            }
                            executor.shutdownNow();
                            return bestMove.get();
                        }
                    };
                }
            };
            /*service.setOnRunning(e -> {
                System.out.println("Berechnung begonnen!");
            });*/
            service.setOnSucceeded(e -> {
                Turn t = service.getValue();
                t.setBlackTime(Chessboard.instance.getBlackTime());
                t.setWhiteTime(Chessboard.instance.getWhiteTime());
                handleTurn(t);
                e.consume();
            });
            service.start();
        }

        if(!debug) controller.handlePremove();
    }

    /**
     * Converts chess notation into a Turn object
     * @param s chess notation example e2e4
     * @return Turn object
     */
    public Turn convertNotation(String s) {
        int column1 = s.charAt(0)-97, line1 = Math.abs(Integer.parseInt(String.valueOf(s.charAt(1)))-8);
        int column2 = s.charAt(2)-97, line2 = Math.abs(Integer.parseInt(String.valueOf(s.charAt(3)))-8);
        return new Turn(fields[line1][column1], fields[line2][column2]);
    }

    /**
     * Converts a Turn object into chess notation
     * @param t Turn object
     * @return chess notation example e2e4
     */
    public String generateNotation(Turn t) {
        return ""+((char) (t.getSourceField().getColumn()+97)) + (Math.abs(t.getSourceField().getLine()-sizeOfBoard)) +
                ((char) (t.getTargetField().getColumn()+97)) + (Math.abs(t.getTargetField().getLine()-sizeOfBoard));
    }

    /**
     * checks if move would put king in check
     * @param destination Field which Piece wants to move to
     * @param start Field of Piece
     * @param color color of Piece
     * @return true if move is legal
     */
    public boolean isLegal(Field destination, Field start, Color color) {

        // Backup the possible eaten piece

        Piece eatenPiece = destination.getPiece();


        // Play position
        destination.setPiece(start.getPiece());
        destination.getPiece().setField(destination);
        start.setPiece(null);

        //printBoard();


        // Check if check
        boolean inCheck = (color.equals(Color.WHITE)?w_king:b_king).isInCheck();


        // Undo move
        start.setPiece(destination.getPiece());
        start.getPiece().setField(start);
        destination.setPiece(eatenPiece);

        //printBoard();

        return !inCheck;

    }

    private boolean checkIfMate(Color color) {
        Color invertedColor = color.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        ArrayList<Piece> pieces = color.equals(Color.WHITE) ? blackPieces : whitePieces;
        for (Piece p : pieces) {
            for (Field move : p.getMoves()) {
                if (isLegal(move, p.getField(), invertedColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets Board by a specific fen.
     * @param fen needs to be in correct FEN notation
     */
    public void setBoardByFen(String fen){
        /*
            0 => positions
            1 => turn
            2 => castle
            3 => en passant
            4 => half turns
            5 => turns
         */
        String[] groups = fen.split(" ");

        // First of all set all the pieces on the board
        String[] positions = groups[0].split("/");
        for (int i = 0; i < positions.length; i++) {
            int position = 0;
            String empty = "";
            for (int x = 0; x < positions[i].length(); x++) {
                int ascii = positions[i].charAt(x);
                // If fen is a number aka an empty space
                if (ascii >= 48 && ascii <= 57) {
                    empty += positions[i].charAt(x);
                    continue;
                } else if (empty.length() > 0) {
                    position += Integer.parseInt(empty);
                    empty = "";
                }

                Piece p = null;
                Field field = fields[i][position];

                // if fen is an uppercase letter aka a white piece
                if (ascii >= 65 && ascii <= 90) {
                    switch (ascii) {
                        case 'P' -> p = new Pawn(Color.WHITE, "White Pawn", field);
                        case 'N' -> p = new Knight(Color.WHITE, "White Knight", field);
                        case 'B' -> p = new Bishop(Color.WHITE, "White Bishop", field);
                        case 'R' -> p = new Rook(Color.WHITE, "White Rook", field);
                        case 'Q' -> {
                            p = new Queen(Color.WHITE, "White Queen", field);
                        }
                        case 'K' -> {
                            p = new King(Color.WHITE, "White King", field);
                            w_king = (King) p;
                        }
                    }
                    whitePieces.add(p);
                }
                // if fen is a lowercase letter aka a black piece
                else {
                    switch (ascii) {
                        case 'p' -> p = new Pawn(Color.BLACK, "Black Pawn", field);
                        case 'n' -> p = new Knight(Color.BLACK, "Black Knight", field);
                        case 'b' -> p = new Bishop(Color.BLACK, "Black Bishop", field);
                        case 'r' -> p = new Rook(Color.BLACK, "Black Rook", field);
                        case 'q' -> p = new Queen(Color.BLACK, "Black Queen", field);
                        case 'k' -> {
                            p = new King(Color.BLACK, "Black King", field);
                            b_king = (King) p;
                        }
                    }
                    blackPieces.add(p);
                }
                field.setPiece(p);
                p.setField(field);
                fields[i][position] = field;
                position++;
            }
        }

        // Now set the player whose turn it is
        colorToMove = groups[1].equals("w") ? Color.WHITE : Color.BLACK;

        // Now check if the players has castle permissions
        whiteCastlePermissionLong = false;
        whiteCastlePermissionShort = false;
        blackCastlePermissionLong = false;
        blackCastlePermissionShort = false;
        for (int i = 0; i < groups[2].length(); i++) {
            switch (groups[2].charAt(i)) {
                case 'K' -> whiteCastlePermissionLong = true;
                case 'Q' -> whiteCastlePermissionShort = true;
                case 'k' -> blackCastlePermissionLong = true;
                case 'q' -> blackCastlePermissionShort = true;
            }
        }
        //Wenn der Fen richtig ist, dann sollten hier keine Exceptions geworfen werden
        if (!groups[3].equals("-")){
            Field field = fields[Math.abs(Integer.parseInt(String.valueOf(groups[3].charAt(1)))-8) + (colorToMove == Color.WHITE ? 1 : -1)][groups[3].charAt(0) - 'a'];
            enPassantable = (Pawn) field.getPiece();
        }

        // set 50 half turn rule
        ruleCounter = Integer.parseInt(groups[4]);

        // set turns played
        turn = Integer.parseInt(groups[5]);

    }// -1 == false, 1 == true but not on same line/column, 2 == true and on same line, 3 == true and on same column
    private int turnToPGNHelper(Piece p, Field source, Field target){
        Piece k=null;
        switch (Character.toLowerCase(p.getShortName())){
            case 'p':
            case 'k':
                return -1;
            case 'n': k = new Knight(p.getColor()==Color.WHITE?Color.BLACK:Color.WHITE, "TestKnight created for PGN", target); break;
            case 'b': k = new Bishop(p.getColor()==Color.WHITE?Color.BLACK:Color.WHITE, "TestBishop created for PGN", target); break;
            case 'r': k = new Rook(p.getColor()==Color.WHITE?Color.BLACK:Color.WHITE, "TestRook created for PGN", target); break;
            case 'q': k = new Queen(p.getColor()==Color.WHITE?Color.BLACK:Color.WHITE, "TestQueen created for PGN", target); break;
            }
        if (k == null)
        {
            System.err.println("possible error in turnToPGNHelper");
            return -1;
        }
        for (Field f :k.getMoves()) {
            if(f.hasPiece()&&f.getPiece().getShortName() == p.getShortName())
            {
                System.out.println(f + " " + source);
                if(f.getLine() == source.getLine())
                    return 2;
                else if(f.getColumn() == source.getColumn())
                    return 3;
                else
                    return 1;
            }
        }

        return -1;
    }

    /**
     * Converts a Turn to a PGN notation
     * @param t turn to convert to PGN
     * @return PGN Notation as String
     */
    public String turnToPGN(Turn t){
        StringBuilder s = new StringBuilder();

        King k = colorToMove == Color.BLACK ? b_king : w_king;
        if(t.getColorToMove() == Color.WHITE)
            s.append((turn)+".");

        switch(Character.toLowerCase(t.getMovingPiece().getShortName())){
            case 'p': if(t.getEatenPiece() != null) s.append(t.getSourceField().getName().charAt(0));break;
            case 'n': s.append('N');break;
            case 'b': s.append('B');break;
            case 'k':
                if(t.isCastleTurn()){
                    if(t.getTargetField().getColumn() >=6){
                        return s.append("0-0 ").toString();
                    }
                    else{
                        return s.append("0-0-0 ").toString();
                    }
                }
                else
                    s.append('K');
                break;
            case 'r': s.append("R"); break;
            case 'q': s.append('Q'); break;
        }

        //System.out.println(t.getTargetField().getName());
        //System.out.println("return: " + turnToPGNHelper(t.getMovingPiece(), t.getSourceField(), t.getTargetField()));
        switch (turnToPGNHelper(t.getMovingPiece(), t.getSourceField(), t.getTargetField())) {
            case 3 -> s.append(t.getSourceField().getName().charAt(1));
            case 1,2 -> s.append(t.getSourceField().getName().charAt(0));
        }

        if(t.getEatenPiece() != null)
            s.append('x');

        s.append(t.getTargetField().getName());
        if(t.isPromotionTurn()){
            System.out.println(t.getMovingPiece().getField().getPiece().getName());
            switch (Character.toLowerCase(t.getMovingPiece().getField().getPiece().getShortName()))
            {
                case 'q' -> s.append("=Q");
                case 'n' -> s.append("=N");
                case 'r' -> s.append("=R");
                case 'b' -> s.append("=B");
            }
        }
        if(k.isInCheck())
            s.append('+');
        if(t.getColorToMove() != Color.BLACK)
        s.append(" ");
        return s.toString();
    }
    /**
     * @return Returns Fen of current Board state
     */
    public String getBoardAsFen(){
        String[] groups = new String[]{"", "", "", "", "", ""};
        // Get the position of the chessboard
        for (int i = 0; i < fields.length; i++) {
            int counter=0;
            for (int x = 0; x < fields[i].length; x++) {
                if (fields[i][x].hasPiece()){
                    if(counter > 0){
                        groups[0] += counter;
                        counter=0;
                    }
                    groups[0] += fields[i][x].getPiece().getShortName();
                }else{
                    counter++;
                }
            }
            if(counter > 0){
                groups[0] += counter;
            }
            groups[0] += '/';
        }
        groups[0] = groups[0].substring(0, groups[0].length()-1);


        //rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1

        groups[1] = colorToMove.equals(Color.WHITE) ? "w" : "b";

        groups[2] = (whiteCastlePermissionLong ? "K" : "") + (whiteCastlePermissionShort ? "Q" : "") + (blackCastlePermissionLong ? "k" : "") + (blackCastlePermissionShort ? "q" : "");
        if(!whiteCastlePermissionLong && !whiteCastlePermissionShort && !blackCastlePermissionLong && !blackCastlePermissionShort){
            groups[2] = "-";
        }

        if(enPassantable == null){
            groups[3] = "-";
        }else{
            groups[3] = fields[enPassantable.getField().getLine() + (colorToMove == Color.WHITE ? -1 : 1)][enPassantable.getField().getColumn()].getName();
        }

        groups[4] = ruleCounter +"";

        groups[5] = turn+"";

        return String.join(" ", groups);

    }


    private boolean checkIfColorCanWinWithTheMaterial(Color color){
        ArrayList<Piece> pieces = (Color.WHITE == color) ? getWhitePieces() : getBlackPieces();

        int knights = 0;
        int bishops = 0;
        ArrayList<Piece> bishopList = new ArrayList<>();
        for(Piece p : pieces){
            if(p instanceof King) continue;
            if(p instanceof Pawn) return true;
            if(p instanceof Rook) return true;
            if(p instanceof Queen) return true;
            if(p instanceof Bishop){
                bishopList.add(p);
                bishops++;
            }
            if(p instanceof Knight) knights++;
        }
        if(knights > 0 && bishops > 0) return true;
        if(bishops > 2 || knights > 2) return true;

        ArrayList<Piece> enemyPieces = (Color.WHITE == color) ? getBlackPieces() : getWhitePieces();

        int enemyBishops=0;
        int enemyKnights=0;
        int enemyOtherPieces=0;
        ArrayList<Piece> enemyBishopsList = new ArrayList<>();
        for(Piece p : enemyPieces){
            if(p instanceof King) continue;
            if(p instanceof Bishop){
                enemyBishops++;
                enemyBishopsList.add(p);
                continue;
            }
            if(p instanceof Knight){
                enemyKnights++;
                continue;
            }
            enemyOtherPieces++;
        }


        if(bishops > 0 && enemyOtherPieces > 0) return true;
        if(knights > 0 && enemyOtherPieces > 0) return true;

        if(knights > 1) return true;
        if(knights > 0 && enemyBishops > 0) return true;
        if(knights > 0 && enemyKnights > 0) return true;
        if(bishops > 0 && enemyKnights > 0) return true;


        if(bishops > 0 && enemyBishops > 0) {
            for (int i = 0; i < enemyBishops; i++) {
                Piece enemyBishop = enemyBishopsList.get(i);
                for (int j = 0; j < bishops; j++) {
                    Piece myBishop = bishopList.get(j);
                    if(((myBishop.getField().getColumn() + myBishop.getField().getLine()) % 2 != (enemyBishop.getField().getColumn() + enemyBishop.getField().getLine()) % 2)) {
                        return true;
                    }
                }
            }
        }

        boolean[] colors = {false, false};
        if(bishopList.size() > 1){
            for (Piece p : bishopList) {
                if((p.getField().getLine() + p.getField().getColumn()) % 2 == 1){
                    colors[0] = true;
                }else{
                    colors[1] = true;
                }
            }
            if(colors[0] && colors[1]){
                return true;
            }
        }

        return false;
    }

    public Color getCurrentAIMovingColor() {
        return currentAIMovingColor;
    }

    public void setCurrentAIMovingColor(Color currentAIMovingColor) {
        this.currentAIMovingColor = currentAIMovingColor;
    }

    public void notifyObserver(){
        for(Observer o : observers){
            o.update();
        }
    }
    public void register(Observer o) {
        observers.add(o);
    }

    public void unregister(Observer o) {
        observers.remove(o);
    }

    public Field[][] getFields() {
        return fields;
    }

    public ArrayList<Turn> getTurns() {
        return turns;
    }

    public int getTurn() {
        return turn;
    }

    public int getPly() {
        return ply;
    }

    public int getRuleCounter() {
        return ruleCounter;
    }

    public ArrayList<String> getFens() {
        return fens;
    }

    public ArrayList<Piece> getWhitePieces() {
        return whitePieces;
    }

    public ArrayList<Piece> getBlackPieces() {
        return blackPieces;
    }

    public ArrayList<Piece> getEatenPieces() {
        return eatenPieces;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public Pawn getEnPassantable() {
        return enPassantable;
    }

    public void setEnPassantable(Pawn enPassantable) {
        this.enPassantable = enPassantable;
    }

    public Color getColorToMove() {
        return colorToMove;
    }

    public void setColorToMove(Color color) {
        colorToMove = color;
    }

    /**
     * @return boolean Array with every Castle Permission
     * [0] = white long castle permission(Queen side)
     * [1] = white short castle permission(King side)
     * [2] = black long castle permission(Queen side)
     * [3] = black short castle permission(King side)
     */
    public boolean[] getCastlePermissions() {
        return new boolean[]{whiteCastlePermissionLong, whiteCastlePermissionShort, blackCastlePermissionLong, blackCastlePermissionShort};
    }

    /**
     * @return
     * [0] = white AI
     * [1] = black AI
     */
    public boolean[] getPlaysAI() {
        return playsAI;
    }

    /**
     *
     * @param playsAI
     * [0] = white A.I
     * [1] = black A.I
     */
    public void setPlaysAI(boolean[] playsAI) {
        this.playsAI = playsAI;
    }

    public long getBlackTime() {
        return blackTime;
    }

    public long getWhiteTime() {
        return whiteTime;
    }

    public long getTimeStopped() {
        return timeStopped;
    }

    public Timer getTimer() {
        return timer;
    }

    public Gamephase getGamephase() {
        return gamephase;
    }

    public void setGamephase(Gamephase gamephase) {
        this.gamephase = gamephase;
    }

    public HashMap<String, String> getOpeningBook() {
        return openingBook;
    }

    public int getSizeOfBoard() {
        return sizeOfBoard;
    }

    public void setSizeOfBoard(int sizeOfBoard) {
        this.sizeOfBoard = sizeOfBoard;
    }

    public boolean isPlayerConnected() {
        return playerConnected;
    }

    public void setPlayerConnected(boolean playerConnected) {
        this.playerConnected = playerConnected;
    }

    public void setWhiteTime(long whiteTime) {
        this.whiteTime = whiteTime;
    }

    public void setBlackTime(long blackTime) {
        this.blackTime = blackTime;
    }

    public boolean isAllowedToMakeMove() {
        return isAllowedToMakeMove;
    }

    public void setAllowedToMakeMove(boolean allowedToMakeMove) {
        isAllowedToMakeMove = allowedToMakeMove;
    }
}
