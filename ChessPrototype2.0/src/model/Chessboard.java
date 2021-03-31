package model;

import model.pieces.Knight;
import model.pieces.Pawn;
import model.pieces.Piece;

import java.util.ArrayList;

public class Chessboard {
    private Field[][] fields;
    private ArrayList<Turn> turns = new ArrayList<>();
    private int turn, ply;
    private int ruleCounter;
    private ArrayList<String> fens = new ArrayList<>();
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> eatenPieces = new ArrayList<>();
    private Gamestate state;
    private boolean whiteCastlePermission, blackCastlePermission;

    // Singleton pattern
    private static Chessboard instance = null;

    private Chessboard() {}

    public static Chessboard getInstance() {
        if (instance == null)
            instance = new Chessboard();
        return instance;
    }

    public static void init(String fen) {
        if (instance != null) return;
        instance = new Chessboard();
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
        for (int i = 0; i < fields.length; i++)
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
        }
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
        /*String[] groups = fen.split(" ");
        String[] positions = groups[0].split("/");
        for (int i = 0; i < positions.length; i++) {
            int position = 0;
            for (int x = 0; x < positions[i].length(); i++) {
                int ascii = positions[i].charAt(x);
                if (ascii >= 48 && ascii <= 57) {
                    int empty = ascii-48;
                    for (int j = 0; j < position+empty; j++)
                        fields[i][j] = new Field(i, j);
                    position += empty;
                    continue;
                }
                if (ascii >= 65 && ascii <= 90) {
                    Piece p = null;
                    Field field = new Field(i, position);
                    switch (ascii) {
                        case 'P' -> p = new Pawn(Color.WHITE, "White Pawn", field, 1);
                        case 'N' -> p = new Knight(Color.WHITE, "White Knight", field, 1);
                        case 'N' -> p = new Knight(Color.WHITE, "White Knight", field, 1);
                    }
                }
            }
        } */


    }
    public String getBoardAsFen(){

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
