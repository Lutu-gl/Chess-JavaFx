package model;

import controller.Controller;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.pieces.*;
import view.ChessboardView;
import view.PlaySound;
import view.Sound;

import java.util.*;
import java.util.concurrent.*;

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
    private Gamestate state;
    private boolean whiteCastlePermissionLong, whiteCastlePermissionShort, blackCastlePermissionLong, blackCastlePermissionShort, endGame = true;
    private King b_king, w_king;
    private Sound playSound;
    private int sizeOfBoard;
    ArrayList<Observer> observers = new ArrayList<>();
    private boolean[] playsAI = new boolean[2];

    private long whiteTime= 1_000_000L;  //5min
    private long blackTime= 1_000_000L;
    private long timeStopped=0L;
    private Timer timer=new Timer();
    public boolean withTime=true;

    public boolean debug = false;

    // Singleton pattern
    private static Chessboard instance = null;


    private Chessboard() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Chessboard chessboard = Chessboard.getInstance();
                //System.out.println("yetzt");
                //long whiteTime=chessboard.getWhiteTime(), blackTime = chessboard.getBlackTime(), timeStopped=chessboard.getTimeStopped();
                if(timeStopped == 0) return;
                long timeNow=0L;

                if(colorToMove == Color.WHITE){
                    timeNow = (whiteTime - (System.currentTimeMillis() - timeStopped));
                    //System.out.println(timeNow/1e9 + "  -  " + blackTime / 1e9);
                    if(timeNow <= 0){
                        state = Gamestate.BLACK_WINS;
                    }
                }else{
                     timeNow = (blackTime - (System.currentTimeMillis() - timeStopped));
                    //System.out.println(whiteTime/1e9 + "  -  " + timeNow/1e9);
                    if(timeNow <= 0){
                        state = Gamestate.WHITE_WINS;
                    }
                }
                notifyObserver();
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100);


        register(new GamestateObserver(this));
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

    public void createBoard(int size){
        createBoard(size, false, false);
    }

    public void createBoard(int size, boolean whiteAI, boolean blackAI){
        this.sizeOfBoard = size;
        state = Gamestate.PLAYING;
        playsAI[0] = whiteAI;
        playsAI[1] = blackAI;
        fields = new Field[size][size];
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                fields[i][j] = new Field(j, i);
            }
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



    public void addPiece(Piece p){
        if(p.getColor() == Color.BLACK)
            blackPieces.add(p);
        else
            whitePieces.add(p);
    }

    //Ende (Also die Züge und ruleCounter) entfernen
    public void addFen(String fen){
        fen  = fen.substring(0, fen.lastIndexOf(' '));
        fen  = fen.substring(0, fen.lastIndexOf(' '));

        fens.add(fen);
    }

    public boolean removePiece(Piece p){
        if(p.getColor() == Color.BLACK)
            return blackPieces.remove(p);
        else
            return whitePieces.remove(p);
    }

    //Wenn jemand a besserer Nume für des einfollt
    private int queensEaten = 0;
    public void addPieceToEaten(Piece p){
        if (!debug && p instanceof Queen) {
            queensEaten++;
            if (queensEaten >= 2)
                endGame = true;
        }
        eatenPieces.add(p);
    }

    //Same
    public boolean removePieceFromEaten(Piece p){
        return eatenPieces.remove(p);
    }

    public King getB_king() {
        return b_king;
    }

    public King getW_king() {
        return w_king;
    }

    private Turn t;
    public void changeTForPromotion(Field f) {
        t.setPromotionTurn(f);
    }

    public boolean handleTurn(Turn currentT){
        if(timeStopped != 0 && withTime){
            if(colorToMove == Color.WHITE){
                whiteTime-=System.currentTimeMillis() - timeStopped;;
            }else{
                blackTime-=System.currentTimeMillis() - timeStopped;;
            }
            System.out.println(whiteTime / 1e3 + " " + blackTime/1e3);
        }
        t = currentT;
        // TODO: Farbe von Check ändern, ist momentan nur Rot
        if (!debug)
            Controller.getInstance().fieldToFieldLabel((colorToMove.equals(Color.WHITE)?w_king:b_king).getField()).unmarkAsCheck();

        Piece p = t.getMovingPiece();
        Field f = t.getTargetField();
        boolean s = true;
        t.setGamestate(state);
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
            state = Gamestate.DRAW;
        }

        //handeln der moves
        if(colorToMove == Color.WHITE){
            turn++;
        }

        //handlern der 3 fold repetition
        int threefoldCounter=0;
        addFen(getBoardAsFen());

        String fen = fens.get(fens.size()-1);
        for(String fe : fens){
            if(fen.equals(fe)){
                threefoldCounter++;
            }
        }
        if(threefoldCounter >= 3){
            state = Gamestate.PLAYER_CAN_CLAIM_DRAW;
        }

        // Den Zug hinzufügen
        turns.add(t);

        endTurn();
        return true;
    }

    public void movePiece(Piece p, Field f) {
        movePiece(p, f, true);
    }

    public void movePiece(Piece p, Field f, boolean s){


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
                ((Pawn)p).promoteIfPossible();
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
                state = colorToMove.equals(Color.WHITE) ? Gamestate.WHITE_WINS : Gamestate.BLACK_WINS;
            } else
                playSound = Sound.CHECK;
        }
        // Check if it is stalemate
        else {
            if (whitePieces.size() + blackPieces.size() <= 2) {
                state = Gamestate.DRAW;
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
                state = Gamestate.STALEMATE;
            }
        }

        if (s)
            PlaySound.play(playSound);

        //printBoard();
    }

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
        state = t.getGamestate();

        // Ganzzüge zurücksetzen
        turn = t.getTurnsPlayed();

        // Set back the 50 move rule counter
        ruleCounter = t.getRuleCounter();

        // remove the fen from fens
        fens.remove(fens.size()-1);

        // Remove the turn from played turns and display the board
        turns.remove(turns.size()-1);
        endTurn();

    }
    //Am ende von jedem Zug
    public void endTurn(){
        notifyObserver();
        //System.out.println(getBoardAsFen());
        if (!debug){
            ChessboardView.display();
            if(withTime) timeStopped = System.currentTimeMillis();
        }

        // Check if bot plays
        int index = colorToMove.equals(Color.WHITE)?0:1;
        if (playsAI[index] && state.equals(Gamestate.PLAYING) && !debug) {
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
                            for (int i = 0; i < 1000; i++) {
                                if (bestMove.isDone()) break;
                                Thread.sleep((long)(((availableTime)*0.05)/1000));
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
                handleTurn(service.getValue());
                e.consume();
            });
            service.start();

            //Platform.runLater(new AI());
        }
    }

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
                            endGame = false;
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

    }
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

        if(enPassantable == null){
            groups[3] = "-";
        }else{
            groups[3] = fields[enPassantable.getField().getLine() + (colorToMove == Color.WHITE ? -1 : 1)][enPassantable.getField().getColumn()].getName();
        }

        groups[4] = ruleCounter +"";

        groups[5] = turn+"";

        return String.join(" ", groups);

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

    public Gamestate getState() {
        return state;
    }

    public void setState(Gamestate state) {
        this.state = state;
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

    public boolean[] getCastlePermissions() {
        return new boolean[]{whiteCastlePermissionLong, whiteCastlePermissionShort, blackCastlePermissionLong, blackCastlePermissionShort};
    }

    public boolean[] getPlaysAI() {
        return playsAI;
    }

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

    public boolean isEndGame() {
        return endGame;
    }
}
