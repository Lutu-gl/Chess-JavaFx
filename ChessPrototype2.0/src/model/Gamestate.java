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
    PLAYER_CAN_CLAIM_DRAW,
    WHITE_WINS_ON_TIME,
    BLACK_WINS_ON_TIME,
    DRAW_BECAUSE_INSUFFICIENT_MATERIAL;
}
