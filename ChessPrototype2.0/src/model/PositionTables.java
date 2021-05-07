package model;

import model.pieces.*;

/**
 * contains static PositionTables for each Piece for each GamePhase
 * used by A.I to help it making moves
 */
public class PositionTables {
    private final static Chessboard chessboard = Chessboard.getInstance();
    private static Field[][] f = chessboard.getFields();

    private static int[][] pawnMapWhiteOpening = new int[][]{
            {100,  100,  100,  100,  100,  100,  100,  100},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 20, 20, 10,  5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {5, 10, 10,-35,-35, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static int[][] pawnMapWhiteMidgame = new int[][]{
            {100,  100,  100,  100,  100,  100,  100,  100},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {5,  5,  5, 20, 20,  5,  5,  5},
            {5,  5,  5,  0,  0,  5,  5,  5},
            {5, 10, 10,-35,-35, 10, 10,  5},
            {0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static int[][] pawnMapWhiteEndgame = new int[][]{
            {100,  100,  100,  100,  100,  100,  100,  100},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {25, 25, 25, 30, 30, 25, 25, 25},
            {20,  20, 20, 25, 25, 20, 20,  20},
            {10,  10,  10, 20, 20, 10, 10,  10},
            {5, -5,-10,  -20,  -20,-10, -5,  5},
            {-2, -2, -2,-20,-20, -2, -2,  -2},
            {0,  0,  0,  0,  0,  0,  0,  0},
    };

    private static int[][] pawnMapBlackOpening = new int[][]{
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10,-35,-35, 10, 10,  5},
            {5, -5,-10,  0,  0,-10, -5,  5},
            {0,  0,  0, 20, 20,  0,  0,  0},
            {5,  5, 10, 20, 20, 10,  5,  5},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {100,  100,  100,  100,  100,  100,  100,  100},

    };

    private static int[][] pawnMapBlackMidgame = new int[][]{
            {0,  0,  0,  0,  0,  0,  0,  0},
            {5, 10, 10,-35,-35, 10, 10,  5},
            {5,  5,  5,  0,  0,  5,  5,  5},
            {5,  5,  5, 20, 20,  5,  5,  5},
            {5,  5, 10, 25, 25, 10,  5,  5},
            {10, 10, 20, 30, 30, 20, 10, 10},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {100,  100,  100,  100,  100,  100,  100,  100},
    };

    private static int[][] pawnMapBlackEndgame = new int[][]{
            {0,  0,  0,  0,  0,  0,  0,  0},
            {-2, -2, -2,-20,-20, -2, -2,  -2},
            {5, -5,-10,  -20,  -20,-10, -5,  5},
            {10,  10,  10, 20, 20, 10, 10,  10},
            {20,  20, 20, 25, 25, 20, 20,  20},
            {25, 25, 25, 30, 30, 25, 25, 25},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {100,  100,  100,  100,  100,  100,  100,  100},
    };

    private static int[][] knightMapOpening = new int[][]{
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 17, 10, 10, 17,  0,-30},
            {-30,  5, 10, 10, 10, 10,  5,-30},
            {-30,  0, 10, 10, 10, 10,  0,-30},
            {-30,  5, 17, 10, 10, 17,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50},
    };
    private static int[][] knightMapMidgame = new int[][]{
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 15, 15, 15, 15,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 15, 15, 15, 15,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50},
    };
    private static int[][] knightMapEndgame = new int[][]{
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 15, 15, 15, 15,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 15, 15, 15, 15,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50},
    };


    private static int[][] bishopMapWhiteOpening = new int[][] {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 0, 0,  5,  0, -10},
            {-10,  7,  5, 6, 6,  5,  7, -10},
            {  6,  0, 10, 10, 10, 10,  0, 6},
            {-10, 6, 0, 5, 5, 0, 6,-10},
            {  6,  12,  8,  12,  12,  8,  12,6},
            {-20,-30,-10,-10,-10,-30,-10,-20},
    };
    private static int[][] bishopMapWhiteMidgame = new int[][] {
            {-20,-10,-10,-10,-10,-10, -10, -20},
            {-10,  0,  0,  0,  0,  0,  0, -10},
            {-10,  0,  5,  0,  0,  5,  0, -10},
            {-10,  7,  5,  6,  6,  5,  7, -10},
            {  6,  0, 10, 10, 10, 10,  0,  6},
            {-10,  6,  0,  0,  0,  0,  6, -10},
            {  6,  12, 8, 12, 12,  8, 12,  6},
            {-20,-20,-20,-20,-20,-20,-20, -20},
    };
    private static int[][] bishopMapWhiteEndgame = new int[][] {
            {0,  0,   0,  0, 0,  0,   0, 0},
            {0,  3,   0,  3, 3,  0,   3, 0},
            {0,  0,  10, 10, 10, 10,  0, 0},
            {0,  3,  10, 15, 15, 10,  3, 0},
            {0,  3,  10, 15, 15, 10,  3, 0},
            {0,  0,  10, 10, 10, 10,  0, 0},
            {0,  3,   0,  3,  3,  0,  3, 0},
            {0,   0,  0,  0,  0,  0,  0, 0},
    };


    private static int[][] bishopMapBlackOpening = new int[][] {
            {-20,-30,-10,-10,-10,-30,-10,-20},
            {  6,  12,  8,  12,  12,  8,  12,6},
            {-10, 6, 0, 5, 5, 0, 6,-10},
            {  6,  0, 10, 10, 10, 10,  0, 6},
            {-10,  7,  5, 6, 6,  5,  7, -10},
            {-10,  0,  5, 0, 0,  5,  0, -10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20},
    };
    private static int[][] bishopMapBlackMidgame = new int[][] {
            {-20,-20,-20,-20,-20,-20,-20, -20},
            {  6,  12, 8, 12, 12,  8, 12,  6},
            {-10,  6,  0,  0,  0,  0,  6, -10},
            {  6,  0, 10, 10, 10, 10,  0,  6},
            {-10,  7,  5,  6,  6,  5,  7, -10},
            {-10,  0,  5,  0,  0,  5,  0, -10},
            {-10,  0,  0,  0,  0,  0,  0, -10},
            {-20,-10,-10,-10,-10,-10, -10, -20},
    };
    private static int[][] bishopMapBlackEndgame = new int[][] {
            {0,   0,  0,  0,  0,  0,  0, 0},
            {0,  3,   0,  3,  3,  0,  3, 0},
            {0,  0,  10, 10, 10, 10,  0, 0},
            {0,  3,  10, 15, 15, 10,  3, 0},
            {0,  3,  10, 15, 15, 10,  3, 0},
            {0,  0,  10, 10, 10, 10,  0, 0},
            {0,  3,   0,  3, 3,  0,   3, 0},
            {0,  0,   0,  0, 0,  0,   0, 0},
    };


    private static int[][] rookMapWhiteOpening = new int[][] {
            {-15,  -15,  -15,  -15,  -15,  -15,  -15, -15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {0,  0,  0,  10,  10,  10,  0, 0},
    };
    private static int[][] rookMapWhiteMidgame = new int[][] {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  3,  3,  3,  3,  0,-10},
            {-5,  0,  3,  3,  3,  3,  0, -5},
            {0,  0,  3,  3,  3,  3,  0, -5},
            {-10,  3,  3,  3,  3,  3,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {5, 5, 5,  5,  5, 5, 5,  5}
    };
    private static int[][] rookMapWhiteEndgame = new int[][] {
            {0,  0,  0,  0,  0,  0,  0, 0},
            {0,  0,  0,  0,  0,  0,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  0,  0,  0,  0,  0, 0},
            {0,  0,  0,  0,  0,  0,  0, 0},
    };


    private static int[][] rookMapBlackOpening = new int[][] {
            {0,  0,  0,  10,  10,  10,  0, 0},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15,-15},
            {-15,  -15,  -15,  -15,  -15,  -15,  -15, -15},

    };
    private static int[][] rookMapBlackMidgame = new int[][] {
            {5, 5, 5,  5,  5, 5, 5,  5},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-10,  3,  3,  3,  3,  3,  0,-10},
            {0,  0,  3,  3,  3,  3,  0, -5},
            {-5,  0,  3,  3,  3,  3,  0, -5},
            {-10,  0,  3,  3,  3,  3,  0,-10},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20},
    };
    private static int[][] rookMapBlackEndgame = new int[][] {
            {0,  0,  0,  0,  0,  0,  0, 0},
            {0,  0,  0,  0,  0,  0,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  3,  3,  3,  3,  0, 0},
            {0,  0,  0,  0,  0,  0,  0, 0},
            {0,  0,  0,  0,  0,  0,  0, 0},
    };

    private static int[][] queenMapOpening = new int[][] {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20},
    };
    private static int[][] queenMapMidgame = new int[][] {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20},
    };
    private static int[][] queenMapEndgame = new int[][] {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20},
    };


    private static int[][] kingMapWhiteOpening = new int[][] {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {10, 5, -10,-10,-10,-10, 5, 10},
            {20, 33, 30,  0,  0, 0, 33, 20},
    };
    private static int[][] kingMapWhiteMidgame = new int[][] {
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {10, 5, -10,-10,-10,-10, 5, 10},
            {20, 33, 30,  0,  0, 0, 33, 20},
    };
    private static int[][] kingMapWhiteEndgame = new int[][] {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50},
    };


    private static int[][] kingMapBlackOpening = new int[][] {
            {20, 33, 30,  0,  0, 0, 33, 20},
            {10, 5, -10,-10,-10,-10, 5, 10},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
    };
    private static int[][] kingMapBlackMidgame = new int[][] {
            {20, 33, 30,  0,  0, 0, 33, 20},
            {10, 5, -10,-10,-10,-10, 5, 10},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
    };
    private static int[][] kingMapBlackEndgame = new int[][] {
            {-50,-30,-30,-30,-30,-30,-30,-50},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-50,-40,-30,-20,-20,-30,-40,-50},
    };

    //HashMap<String, Turn> openingBook = Map.ofEntries(
    //);

    public static double getValue(Piece p) {
        double v =  getMapForPiece(p)[p.getField().getLine()][p.getField().getColumn()];
        return v/200;
    }


    private static int[][] getMapForPiece(Piece p) {
        int[][] map = null;

        if(chessboard.getGamephase().equals(Gamephase.OPENING)){
            if (p instanceof Pawn)
                map = p.getColor().equals(Color.WHITE)?pawnMapWhiteOpening:pawnMapBlackOpening;
            else if (p instanceof Queen)
                map = queenMapOpening;
            else if (p instanceof Knight)
                map = knightMapOpening;
            else if (p instanceof Bishop)
                map = p.getColor().equals(Color.WHITE)?bishopMapWhiteOpening:bishopMapBlackOpening;
            else if (p instanceof Rook)
                map = p.getColor().equals(Color.WHITE)?rookMapWhiteOpening:rookMapBlackOpening;
            else
                map = p.getColor().equals(Color.WHITE)?kingMapWhiteOpening:kingMapBlackOpening;
        }else if(chessboard.getGamephase().equals(Gamephase.MIDGAME)){
            if (p instanceof Pawn)
                map = p.getColor().equals(Color.WHITE)?pawnMapWhiteMidgame:pawnMapBlackMidgame;
            else if (p instanceof Queen)
                map = queenMapMidgame;
            else if (p instanceof Knight)
                map = knightMapMidgame;
            else if (p instanceof Bishop)
                map = p.getColor().equals(Color.WHITE)?bishopMapWhiteMidgame:bishopMapBlackMidgame;
            else if (p instanceof Rook)
                map = p.getColor().equals(Color.WHITE)?rookMapWhiteMidgame:rookMapBlackMidgame;
            else
                map = p.getColor().equals(Color.WHITE)?kingMapWhiteMidgame:kingMapBlackMidgame;
            return map;
        }else{
            if (p instanceof Pawn)
                map = p.getColor().equals(Color.WHITE)?pawnMapWhiteEndgame:pawnMapBlackEndgame;
            else if (p instanceof Queen)
                map = queenMapEndgame;
            else if (p instanceof Knight)
                map = knightMapEndgame;
            else if (p instanceof Bishop)
                map = p.getColor().equals(Color.WHITE)?bishopMapWhiteEndgame:bishopMapBlackEndgame;
            else if (p instanceof Rook)
                map = p.getColor().equals(Color.WHITE)?rookMapWhiteEndgame:rookMapBlackEndgame;
            else
                map = p.getColor().equals(Color.WHITE)?kingMapWhiteEndgame:kingMapBlackEndgame;
            return map;
        }

        return map;
    }
}
