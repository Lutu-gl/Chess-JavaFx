package model;

import model.pieces.Piece;

import java.util.ArrayList;

public class Chessboard {
    private Field[][] fields;
    private ArrayList<Turn> turns = new ArrayList<>();
    private int turn, ply;
    private int ruleCounter;
    private ArrayList<String> fens;
    private ArrayList<Piece> whitePieces = new ArrayList<>();
    private ArrayList<Piece> blackPieces = new ArrayList<>();
    private ArrayList<Piece> eatenPieces = new ArrayList<>();
    private Gamestate state;

    public void addPiece(Piece p){
        if(p.getColor() == Color.BLACK)
            blackPieces.add(p);
        else
            whitePieces.add(p);
    }

    public void addFen(String fen){
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
