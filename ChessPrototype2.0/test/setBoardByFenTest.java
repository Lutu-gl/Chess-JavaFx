import model.Chessboard;
import model.Color;
import model.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class setBoardByFenTest {


    /*
        Testing standard fen

        Lichess reference:
        https://lichess.org/editor

     */
    @Test
    void testStandardFen() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        Chessboard chessboard = Chessboard.getInstance();
        chessboard.createBoard(8);
        chessboard.setBoardByFen(fen);

        // Check if white is playing
        assertEquals(Color.WHITE, chessboard.getColorToMove(), "Der falsche Spieler ist am Zug!");

        // Check if everybody has castle permissions
        assertTrue(chessboard.getCastlePermissions()[0], "Weiß kann nicht lang rochieren!");
        assertTrue(chessboard.getCastlePermissions()[1], "Weiß kann nicht kurz rochieren!");
        assertTrue(chessboard.getCastlePermissions()[2], "Schwarz kann nicht lang rochieren!");
        assertTrue(chessboard.getCastlePermissions()[3], "Schwarz kann nicht kurz rochieren!");

        // Schauen ob die gespielten Züge richtig sind
        assertEquals(1, chessboard.getTurn(), "Falsche Anzahl an gespielten Zügen!");

        // Schauen ob das Spielfeld richtig ist
        char[][] expectedField = new char[][]{
                {'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','p','p','p'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'P','P','P','P','P','P','P','P'},
                {'R','N','B','Q','K','B','N','R'},
        };
        for (int line = 0; line < chessboard.getFields().length; line++) {
            for (int column = 0; column < chessboard.getFields()[line].length; column++) {
                assertEquals(expectedField[line][column], chessboard.getFields()[line][column].hasPiece()?chessboard.getFields()[line][column].getPiece().getShortName():'-', "Falsche Figur auf dem Schachbrett!");
            }
        }
        assertEquals(fen, chessboard.getBoardAsFen(), "Falsches Fen das eingegeben wurde mit dem Board Fen");

    }

    /*
        Testing random fen

        Lichess reference:
        https://lichess.org/editor/r3kb1r/p1p2ppp/2n1b3/3q4/2p2B2/P7/1P2QPPP/R3KB1R_b_-_-_0_1

     */

    @Test
    void randomLichessFen() {
        String fen = "r3kb1r/p1p2ppp/2n1b3/3q4/2p2B2/P7/1P2QPPP/R3KB1R b KQkq - 0 14";
        Chessboard chessboard = Chessboard.getInstance();
        chessboard.createBoard(8);
        chessboard.setBoardByFen(fen);
        chessboard.printBoard();

        // Check if black is playing
        assertEquals(Color.BLACK, chessboard.getColorToMove(), "Der falsche Spieler ist am Zug!");

        // Check if everybody has castle permissions
        assertTrue(chessboard.getCastlePermissions()[0], "Weiß kann nicht lang rochieren!");
        assertTrue(chessboard.getCastlePermissions()[1], "Weiß kann nicht kurz rochieren!");
        assertTrue(chessboard.getCastlePermissions()[2], "Schwarz kann nicht lang rochieren!");
        assertTrue(chessboard.getCastlePermissions()[3], "Schwarz kann nicht kurz rochieren!");

        // Schauen ob die gespielten Züge richtig sind
        assertEquals(14, chessboard.getTurn(), "Falsche Anzahl an gespielten Zügen!");

        // Schauen ob das Spielfeld richtig ist
        char[][] expectedField = new char[][]{
                {'r','-','-','-','k','b','-','r'},
                {'p','-','p','-','-','p','p','p'},
                {'-','-','n','-','b','-','-','-'},
                {'-','-','-','q','-','-','-','-'},
                {'-','-','p','-','-','B','-','-'},
                {'P','-','-','-','-','-','-','-'},
                {'-','P','-','-','Q','P','P','P'},
                {'R','-','-','-','K','B','-','R'},
        };
        for (int line = 0; line < chessboard.getFields().length; line++) {
            for (int column = 0; column < chessboard.getFields()[line].length; column++) {
                assertEquals(expectedField[line][column], chessboard.getFields()[line][column].hasPiece()?chessboard.getFields()[line][column].getPiece().getShortName():'-', line + " " + column/*"Falsche Figur auf dem Schachbrett!"*/);
            }
        }
        assertEquals(fen, chessboard.getBoardAsFen(), "Falsches Fen das eingegeben wurde mit dem Board Fen");

    }

    @Test
    void noCastleFen() {
        String fen = "rnbqkbnr/ppp1pppp/3p1P2/8/8/4P3/PPPP2PP/RNBQKBR1 b - - 0 4";
        Chessboard chessboard = Chessboard.getInstance();
        chessboard.createBoard(8);
        chessboard.setBoardByFen(fen);
        chessboard.printBoard();

        // Check if black is playing
        assertEquals(Color.BLACK, chessboard.getColorToMove(), "Der falsche Spieler ist am Zug!");

        // Check if everybody has castle permissions
        assertFalse(chessboard.getCastlePermissions()[0], "Weiß kann  lang rochieren!");
        assertFalse(chessboard.getCastlePermissions()[1], "Weiß kann  kurz rochieren!");
        assertFalse(chessboard.getCastlePermissions()[2], "Schwarz kann  lang rochieren!");
        assertFalse(chessboard.getCastlePermissions()[3], "Schwarz kann  kurz rochieren!");

        // Schauen ob die gespielten Züge richtig sind
        assertEquals(4, chessboard.getTurn(), "Falsche Anzahl an gespielten Zügen!");

        // Schauen ob das Spielfeld richtig ist
        char[][] expectedField = new char[][]{
                {'r','n','b','q','k','b','n','r'},
                {'p','p','p','-','p','p','p','p'},
                {'-','-','-','p','-','P','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','P','-','-','-'},
                {'P','P','P','P','-','-','P','P'},
                {'R','N','B','Q','K','B','R','-'},
        };
        for (int line = 0; line < chessboard.getFields().length; line++) {
            for (int column = 0; column < chessboard.getFields()[line].length; column++) {
                assertEquals(expectedField[line][column], chessboard.getFields()[line][column].hasPiece()?chessboard.getFields()[line][column].getPiece().getShortName():'-', line + " " + column/*"Falsche Figur auf dem Schachbrett!"*/);
            }
        }

        assertEquals(fen, chessboard.getBoardAsFen(), "Falsches Fen das eingegeben wurde mit dem Board Fen");
    }


    @Test
    void enPassantnoCastleFen() {
        String fen = "rnbqkbnr/ppppp2p/5p2/5Pp1/8/8/PPPPP1PP/RNBQKBNR w - g6 0 5";
        Chessboard chessboard = Chessboard.getInstance();
        chessboard.createBoard(8);
        chessboard.setBoardByFen(fen);
        chessboard.printBoard();

        // Check if black is playing
        assertEquals(Color.WHITE, chessboard.getColorToMove(), "Der falsche Spieler ist am Zug!");

        // Check if everybody has castle permissions
        assertFalse(chessboard.getCastlePermissions()[0], "Weiß kann  lang rochieren!");
        assertFalse(chessboard.getCastlePermissions()[1], "Weiß kann  kurz rochieren!");
        assertFalse(chessboard.getCastlePermissions()[2], "Schwarz kann  lang rochieren!");
        assertFalse(chessboard.getCastlePermissions()[3], "Schwarz kann  kurz rochieren!");

        // Schauen ob die gespielten Züge richtig sind
        assertEquals(5, chessboard.getTurn(), "Falsche Anzahl an gespielten Zügen!");

        // Schauen ob das Spielfeld richtig ist
        char[][] expectedField = new char[][]{
                {'r','n','b','q','k','b','n','r'},
                {'p','p','p','p','p','-','-','p'},
                {'-','-','-','-','-','p','-','-'},
                {'-','-','-','-','-','P','p','-'},
                {'-','-','-','-','-','-','-','-'},
                {'-','-','-','-','-','-','-','-'},
                {'P','P','P','P','P','-','P','P'},
                {'R','N','B','Q','K','B','N','R'},
        };
        for (int line = 0; line < chessboard.getFields().length; line++) {
            for (int column = 0; column < chessboard.getFields()[line].length; column++) {
                assertEquals(expectedField[line][column], chessboard.getFields()[line][column].hasPiece()?chessboard.getFields()[line][column].getPiece().getShortName():'-', line + " " + column/*"Falsche Figur auf dem Schachbrett!"*/);
            }
        }

        assertEquals(fen, chessboard.getBoardAsFen(), "Falsches Fen das eingegeben wurde mit dem Board Fen");
    }
}