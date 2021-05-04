package model.ai.openingBookParser;

import model.Chessboard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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

    private static Stockfish client;
    private static Chessboard chessboard;

    public static void main(String[] args) throws InterruptedException {
        client = new Stockfish();
        //String FEN = "8/6pk/8/1R5p/3K3P/8/6r1/8 b - - 0 42";
        //String FEN = "rn3rk1/pbppq1pp/1p2pb2/4N2Q/3PN3/3B4/PPP2PPP/R3K2R w KQ - 7 11";
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

        // initialize and connect to engine
        if (client.startEngine()) {
            System.out.println("Engine has started..");
            client.getOutput(10);
        } else {
            System.out.println("Oops! Something went wrong..");
        }
        chessboard = Chessboard.getInstance();
        chessboard.debug = true;
        chessboard.withTime = false;
        chessboard.createBoard(8, true, true, Integer.MAX_VALUE, Integer.MAX_VALUE, 0,0);
        chessboard.setBoardByFen(FEN);

        recursiveSearch();
    }

    private static void recursiveSearch() throws InterruptedException {
        client.sendCommand("position fen "+chessboard.getBoardAsFen());
        client.sendCommand("go depth 100");
        Thread.sleep(10000);
        client.sendCommand("stop");
        System.out.println(client.getOutput(10).split("bestmove ")[1].split(" ")[0]);
        /*ArrayList<String> threeBestMoves = new ArrayList<>();
        for (int i = 5000; i > 2000; i -= 500) {
            String move = client.getBestMove(chessboard.getBoardAsFen(), i);
            if (!threeBestMoves.contains(move)) {
                threeBestMoves.add(move);
                if (threeBestMoves.size() >= 3)
                    break;
            }
            break;
        }
        System.out.println(threeBestMoves);

         */
    }
}
