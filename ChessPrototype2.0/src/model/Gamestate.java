package model;

/**
 * enum to keep track in which state the Game is
 */
public enum Gamestate {
    PLAYING,
    WHITE_WINS,
    BLACK_WINS,
    STALEMATE,
    DRAW,
    PLAYER_CAN_CLAIM_DRAW;
}
