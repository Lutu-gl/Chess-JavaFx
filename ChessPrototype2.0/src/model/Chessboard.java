package model;

import controller.Controller;
import model.pieces.*;
import view.ChessboardView;
import view.PlaySound;
import view.Sound;

import java.util.ArrayList;

public class Chessboard {
    private Field[][] fields;
    private ArrayList<Turn> turns = new ArrayList<>();
    private int turn, ply;
    private Color colorToMove;
    private int ruleCounter;
    private Pawn enPassantable;
    private ArrayList<String> fens = new ArrayList<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> eatenPieces = new ArrayList<>();
    private Gamestate state;
    private boolean whiteCastlePermissionLong, whiteCastlePermissionShort, blackCastlePermissionLong, blackCastlePermissionShort;
    private King b_king, w_king;
    private Sound playSound;
    private int sizeOfBoard;
    ArrayList<Observer> observers = new ArrayList<>();


    // Singleton pattern
    private static Chessboard instance = null;


    private Chessboard() {
        register(new GamestateObserver(this));
    }

    public static Chessboard getInstance() {
        if (instance == null)
            instance = new Chessboard();

        return instance;
    }

    public void createBoard(int size){
        this.sizeOfBoard = size;
        state = Gamestate.PLAYING;
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
    public void addPieceToEaten(Piece p){
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

    public boolean handleTurn(Turn t){

        // TODO: Farbe von Check ändern, ist momentan nur Rot
        Controller.getInstance().fieldToFieldLabel((colorToMove.equals(Color.WHITE)?w_king:b_king).getField()).unmarkAsCheck();

        Piece p = t.getMovingPiece();
        Field f = t.getTargetField();
        boolean s = true;

        if (p instanceof King) {
            if (p.getColor().equals(Color.WHITE)) {
                whiteCastlePermissionLong = false;
                whiteCastlePermissionShort = false;
                // Check if it is left castling
                if (w_king.getField().getColumn() - f.getColumn() == 2) {
                    movePiece(fields[fields.length - 1][0].getPiece(), fields[fields.length - 1][p.getField().getColumn() - 1], false);
                    PlaySound.play(Sound.CASTLE);
                    s = false;
                }
                // Check if it is right castling
                else if (f.getColumn() - w_king.getField().getColumn() == 2) {
                    movePiece(fields[fields.length - 1][fields.length - 1].getPiece(), fields[fields.length - 1][p.getField().getColumn() + 1], false);
                    PlaySound.play(Sound.CASTLE);
                    s = false;
                }
            } else {
                blackCastlePermissionLong = false;
                blackCastlePermissionShort = false;
                // Check if it is left castling
                if (b_king.getField().getColumn() - f.getColumn() == 2) {
                    movePiece(fields[0][0].getPiece(), fields[0][p.getField().getColumn() - 1], false);
                    PlaySound.play(Sound.CASTLE);
                    s = false;
                }
                // Check if it is right castling
                else if (f.getColumn() - b_king.getField().getColumn() == 2) {
                    movePiece(fields[0][fields.length - 1].getPiece(), fields[0][p.getField().getColumn() + 1], false);
                    PlaySound.play(Sound.CASTLE);
                    s = false;
                }
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
                removePiece(removeThisPawn); //Den Bauer löschen (weil das targetField ein anderes ist als wo der andere Bauer steht und das dann nicht gelöscht wird....)
                addPieceToEaten(removeThisPawn);
                removeThisPawn.getField().setPiece(null); //So löscht man den Bauer wirklich
                PlaySound.play(Sound.CAPTURE);
                s = false;
            }
        }



        movePiece(p, f, s);
        colorToMove = colorToMove.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;
        //Promotion
        if(t.getMovingPiece() instanceof Pawn)
            ((Pawn)t.getMovingPiece()).promoteIfPossible();



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

        King king = (colorToMove.equals(Color.WHITE)) ? b_king : w_king;
        if (king.isInCheck()) {
            Controller.getInstance().fieldToFieldLabel(king.getField()).markAsCheck();
            if (checkIfMate(colorToMove)){
                playSound = Sound.MATE;
                state = colorToMove.equals(Color.WHITE) ? Gamestate.WHITE_WINS : Gamestate.BLACK_WINS;
            } else
                playSound = Sound.CHECK;
        }

        if (s)
            PlaySound.play(playSound);

        //printBoard();
    }

    public void undoTurn(Turn t, Color color){
        t.getSourceField().setPiece(t.getMovingPiece()); //move Piece Back to source
        t.getMovingPiece().setField(t.getSourceField()); //Update Field in Piece
        if(t.getEatenPiece() != null){//if there has been a eaten Piece it gets reset aswell
            t.getEatenPiece().setField(t.getTargetField());
            t.getTargetField().setPiece(t.getEatenPiece());
            addPiece(t.getEatenPiece());
            removePieceFromEaten(t.getEatenPiece());
        }
        else
            t.getTargetField().setPiece(null);
        colorToMove = color;
    }
    //Am ende von jedem Zug
    public void endTurn(){
        notifyObserver();
        System.out.println(getBoardAsFen());
        ChessboardView.display();
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
                    System.out.println(move);
                    System.out.println(p.getField());
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
                        case 'Q' -> p = new Queen(Color.WHITE, "White Queen", field);
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

    public boolean[] getCastlePermissions() {
        return new boolean[]{whiteCastlePermissionLong, whiteCastlePermissionShort, blackCastlePermissionLong, blackCastlePermissionShort};
    }

}
