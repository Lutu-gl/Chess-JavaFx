package model;

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
    private ArrayList<String> fens = new ArrayList<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> eatenPieces = new ArrayList<>();
    private Gamestate state;
    private boolean whiteCastlePermissionShort, whiteCastlePermissionLong, blackCastlePermissionShort, blackCastlePermissionLong;
    private King b_king, w_king;

    // Singleton pattern
    private static Chessboard instance = null;


    private Chessboard() {}

    public static Chessboard getInstance() {
        if (instance == null)
            instance = new Chessboard();
        return instance;
    }

    public void createBoard(int size){
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

    //muas man no is ende fun string entfernen
    public void addFen(String fen){
        fens.add(fen.substring(0, fen.indexOf(" ")));
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

    public boolean handleTurn(Turn t){
        //System.out.println("Der gespielte Turn ist: " + t);
        movePiece(t.getMovingPiece(), t.getTargetField() );
        colorToMove = colorToMove.equals(Color.WHITE) ? Color.BLACK : Color.WHITE;

        endTurn();
        return true;
    }
    public void movePiece(Piece p, Field f){
        if (f.hasPiece())
            PlaySound.play(Sound.CAPTURE);
        else
            PlaySound.play(Sound.MOVE);
        p.getField().setPiece(null);//Remove Piece from Source
        p.setField(f); //Update Field in Piece
        f.setPiece(p); //Move Piece to new Field

    }
    public void undoTurn(){

    }
    //Am ende von jedem Zug
    public void endTurn(){

        //System.out.println(getBoardAsFen());
        ChessboardView.display();
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
                        case 'P' -> p = new Pawn(Color.WHITE, "White Pawn", field, 1, 'P');
                        case 'N' -> p = new Knight(Color.WHITE, "White Knight", field, 3, 'N');
                        case 'B' -> p = new Bishop(Color.WHITE, "White Bishop", field, 3, 'B');
                        case 'R' -> p = new Rook(Color.WHITE, "White Rook", field, 5, 'R');
                        case 'Q' -> p = new Queen(Color.WHITE, "White Queen", field, 9, 'Q');
                        case 'K' -> {
                            p = new King(Color.WHITE, "White King", field, Integer.MAX_VALUE, 'K');
                            w_king = (King) p;
                        }
                    }
                    whitePieces.add(p);
                }
                // if fen is a lowercase letter aka a black piece
                else {
                    switch (ascii) {
                        case 'p' -> p = new Pawn(Color.BLACK, "Black Pawn", field, 1, 'p');
                        case 'n' -> p = new Knight(Color.BLACK, "Black Knight", field, 3, 'n');
                        case 'b' -> p = new Bishop(Color.BLACK, "Black Bishop", field, 3, 'b');
                        case 'r' -> p = new Rook(Color.BLACK, "Black Rook", field, 5, 'r');
                        case 'q' -> p = new Queen(Color.BLACK, "Black Queen", field, 9, 'q');
                        case 'k' -> {
                            p = new King(Color.BLACK, "Black King", field, Integer.MAX_VALUE, 'k');
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
        whiteCastlePermissionShort = false;
        whiteCastlePermissionLong = false;
        blackCastlePermissionShort = false;
        blackCastlePermissionLong = false;
        for (int i = 0; i < groups[2].length(); i++) {
            switch (groups[2].charAt(i)) {
                case 'K' -> whiteCastlePermissionShort = true;
                case 'Q' -> whiteCastlePermissionLong = true;
                case 'k' -> blackCastlePermissionShort = true;
                case 'q' -> blackCastlePermissionLong = true;
            }
        }

        // en passent (muas i no schaugen)
        //if (!groups[3].equals("-"))

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

        groups[2] = (whiteCastlePermissionShort ? "K" : "") + (whiteCastlePermissionLong ? "Q" : "") + (blackCastlePermissionShort ? "k" : "") + (blackCastlePermissionLong ? "q" : "");

        groups[3] = "-";

        groups[4] = ruleCounter +"";

        groups[5] = turn+"";

        return String.join(" ", groups);

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

    public Color getColorToMove() {
        return colorToMove;
    }

}
