package model.ai.openingBookParser;

import model.Chessboard;
import model.Turn;

import java.io.*;
import java.util.ArrayList;

/**
 * A simple and efficient client to run Stockfish from Java
 *
 * @author Rahul A R
 *
 */
public class Stockfish {

    private Process engineProcess;
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    private static final String PATH = "D:\\VPNSchule\\info\\test\\stockfish_13_win_x64_bmi2\\stockfish_13_win_x64_bmi2.exe";
    /**
     * Starts Stockfish engine as a process and initializes is
     * @return True on success. False otherwise
     */
    public boolean startEngine() {
        try {
            engineProcess = Runtime.getRuntime().exec(PATH);
            processReader = new BufferedReader(new InputStreamReader(
                    engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(
                    engineProcess.getOutputStream());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Takes in any valid UCI command and executes it
     *
     * @param command
     */
    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is generally called right after 'sendCommand' for getting the raw
     * output from Stockfish
     *
     * @param waitTime
     *            Time in milliseconds for which the function waits before
     *            reading the output. Useful when a long running command is
     *            executed
     * @return Raw output from Stockfish
     */
    public String getOutput(int waitTime) {
        StringBuffer buffer = new StringBuffer();
        try {
            Thread.sleep(waitTime);
            sendCommand("isready");
            while (true) {
                String text = processReader.readLine();
                if (text.equals("readyok"))
                    break;
                else
                    buffer.append(text + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    /**
     * This function returns the best move for a given position after
     * calculating for 'waitTime' ms
     *
     * @param fen
     *            Position string
     * @param waitTime
     *            in milliseconds
     * @return Best Move in PGN format
     */
    public String getBestMove(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);
        getOutput(waitTime + 200);
        return getOutput(waitTime + 200).split("bestmove ")[1].split(" ")[0];
    }

    /**
     * Stops Stockfish and cleans up before closing it
     */
    public void stopEngine() {
        try {
            sendCommand("quit");
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
        }
    }

    /**
     * Get a list of all legal moves from the given position
     *
     * @param fen
     *            Position string
     * @return String of moves
     */
    public String getLegalMoves(String fen) {
        sendCommand("position fen " + fen);
        sendCommand("d");
        return getOutput(0).split("Legal moves: ")[1];
    }

    /**
     * Draws the current state of the chess board
     *
     * @param fen
     *            Position string
     */
    public void drawBoard(String fen) {
        sendCommand("position fen " + fen);
        sendCommand("d");

        String[] rows = getOutput(0).split("\n");

        for (int i = 1; i < 18; i++) {
            System.out.println(rows[i]);
        }
    }

    /**
     * Get the evaluation score of a given board position
     * @param fen Position string
     * @param waitTime in milliseconds
     * @return evalScore
     */
    public float getEvalScore(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);

        float evalScore = 0.0f;
        String[] dump = getOutput(waitTime + 20).split("\n");
        for (int i = dump.length - 1; i >= 0; i--) {
            if (dump[i].startsWith("info depth ")) {
                try {
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" nodes")[0]);
                } catch(Exception e) {
                    evalScore = Float.parseFloat(dump[i].split("score cp ")[1]
                            .split(" upperbound nodes")[0]);
                }
            }
        }
        return evalScore/100;
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        Chessboard chessboard = Chessboard.getInstance();
        chessboard.debug = true;
        chessboard.withTime = false;
        chessboard.createBoard(8, true, true, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
        String fen = args[0] + " " + args[1] + " " + args[2] + " " + args[3] + " " + args[4] + " " + args[5];
        chessboard.setBoardByFen(fen);
        Thread t = new Thread(new StockfishHelper(chessboard, new Stockfish(), args[6]));
        t.start();

        // Start thread for classic chess 1.e4
        /*Runnable classicChess = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "e2e4.csv");
        Thread thread1 = new Thread(classicChess);

        // Start thread for queen pawn 1.d4
        Runnable queenPawn = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/3P4/8/PPP1PPPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "d2d4.csv");
        Thread thread2 = new Thread(queenPawn);

        // Start thread for kingKnight opening 1.nf3
        Runnable knightKing = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1"), new Stockfish(), "g1f3.csv");
        Thread thread3 = new Thread(knightKing);

        // Start thread for english opening 1.c4
        Runnable englishOpening = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/2P5/8/PP1PPPPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "c2c4.csv");
        Thread thread4 = new Thread(englishOpening);

        // Start thread for Van't Kruijs 1.e3
        Runnable kruijs = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "e2e3.csv");
        Thread thread5 = new Thread(kruijs);

        // Start thread for King's Fianchetto 1.g3
        Runnable fianchetto = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/6P1/PPPPPP1P/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "g2g3.csv");
        Thread thread6 = new Thread(fianchetto);

        // Start thread for Saragossa Opening 1.c3
        Runnable saragossa = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/2P5/PP1PPPPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "c2c3.csv");
        Thread thread7 = new Thread(saragossa);

        // Start thread for Anderssen's Opening 1.a3
        Runnable anderssen = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/P7/1PPPPPPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "a2a3.csv");
        Thread thread8 = new Thread(anderssen);

        // Start thread for queenKnight Opening 1.nc3
        Runnable queenKnight = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR b KQkq - 1 1"), new Stockfish(), "b1c3.csv");
        Thread thread9 = new Thread(queenKnight);

        // Start thread for Larsen's Opening 1.b3
        Runnable larsen = new StockfishHelper(chessboardHelper("rnbqkbnr/pppppppp/8/8/8/1P6/P1PPPPPP/RNBQKBNR b KQkq - 0 1"), new Stockfish(), "b2b3.csv");
        Thread thread10 = new Thread(larsen);

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();
        thread9.start();
        thread10.start();
        */
    }
}
