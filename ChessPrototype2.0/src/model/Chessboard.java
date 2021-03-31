package model;

import model.pieces.*;

import java.util.ArrayList;

public class Chessboard {
    private Field[][] fields;
    private ArrayList<Turn> turns = new ArrayList<>();
    private int turn, ply;
    private int currentTurn; // 0 => white; 1 => black;
    private int ruleCounter;
    private ArrayList<String> fens = new ArrayList<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> eatenPieces = new ArrayList<>();
    private Gamestate state;
    private boolean whiteCastlePermissionShort, whiteCastlePermissionLong, blackCastlePermissionShort, blackCastlePermissionLong;

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
                fields[j][i] = new Field(j, i);
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

        return false;
    }
    public void movePiece(Piece p, Field f){

    }
    public void undoTurn(){

    }
    public void endTurn(){

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
            for (int x = 0; x < positions[i].length(); x++) {
                int ascii = positions[i].charAt(x);
                // If fen is a number aka an empty space
                if (ascii >= 48 && ascii <= 57) {
                    int empty = ascii-48;
                    for (int j = 0; j < position+empty; j++)
                        fields[i][j] = new Field(i, j);
                    position += empty;
                    continue;
                }

                Piece p = null;
                Field field = new Field(i, position);

                // if fen is an uppercase letter aka a white piece
                if (ascii >= 65 && ascii <= 90) {

                    switch (ascii) {
                        case 'P' -> p = new Pawn(Color.WHITE, "White Pawn", field, 1);
                        case 'N' -> p = new Knight(Color.WHITE, "White Knight", field, 3);
                        case 'B' -> p = new Bishop(Color.WHITE, "White Bishop", field, 3);
                        case 'R' -> p = new Rook(Color.WHITE, "White Rook", field, 5);
                        case 'Q' -> p = new Queen(Color.WHITE, "White Queen", field, 9);
                        case 'K' -> p = new King(Color.WHITE, "White King", field, Integer.MAX_VALUE);
                    }
                }
                // if fen is a lowercase letter aka a black piece
                else {
                    switch (ascii) {
                        case 'p' -> p = new Pawn(Color.BLACK, "Black Pawn", field, 1);
                        case 'n' -> p = new Knight(Color.BLACK, "Black Knight", field, 3);
                        case 'b' -> p = new Bishop(Color.BLACK, "Black Bishop", field, 3);
                        case 'r' -> p = new Rook(Color.BLACK, "Black Rook", field, 5);
                        case 'q' -> p = new Queen(Color.BLACK, "Black Queen", field, 9);
                        case 'k' -> p = new King(Color.BLACK, "Black King", field, Integer.MAX_VALUE);
                    }

                }
                field.setPiece(p);
                fields[i][position] = field;
                whitePieces.add(p);
                position++;
            }
        }

        // Now set the player whose turn it is
        currentTurn = groups[1].equals("w") ? 0 : 1;

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
            for (int x = 0; x < fields[i].length; x++) {
                if (fields[i][x].hasPiece()){

                }
            }
        }
        return "";
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



}
