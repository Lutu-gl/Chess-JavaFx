package model.ai.openingBookParser;

import model.Chessboard;
import model.Turn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class StockfishHelper implements Runnable{

    private Chessboard chessboard;
    private Stockfish client;
    private File f;

    public StockfishHelper(Chessboard chessboard, Stockfish client, String filename) {
        this.chessboard = chessboard;
        this.client = client;
        f = new File(filename);
    }


    @Override
    public void run() {
        if (client.startEngine()) {
            System.out.println("Engine has started..");
            client.sendCommand("setoption name multipv value 10");
            client.getOutput(10);
        } else {
            System.out.println("Oops! Something went wrong..");
            return;
        }
        recursiveSearch(20);
    }

    private void recursiveSearch(int depth) {
        if (depth == 0)
            return;
        ArrayList<String> bestMoves = new ArrayList<>();
        client.sendCommand("position fen "+chessboard.getBoardAsFen());
        client.sendCommand("go depth 300");
        String[] lines = client.getOutput(200).split("\n");
        client.sendCommand("stop");
        for (int i = lines.length-10; i < lines.length; i++) {
            bestMoves.add(lines[i].substring(lines[i].indexOf(" pv ")+4, lines[i].indexOf(" pv ")+8));
        }
        System.out.println(bestMoves);
        try {
            String fen = chessboard.getBoardAsFen();
            FileWriter writer = new FileWriter(f, true);
            writer.append(fen.substring(0, fen.indexOf(" ")+2)+";"+bestMoves.get(0)+"\n");
            writer.close();
            System.out.println("Wrote move to file!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String currentMove : bestMoves) {
            Turn turn = convertNotation(currentMove);
            chessboard.handleTurn(convertNotation(currentMove));
            recursiveSearch(depth-1);
            chessboard.undoTurn(turn);
        }

    }
    private Turn convertNotation(String s) {
        int column1 = s.charAt(0)-97, line1 = Math.abs(Integer.parseInt(String.valueOf(s.charAt(1)))-8);
        int column2 = s.charAt(2)-97, line2 = Math.abs(Integer.parseInt(String.valueOf(s.charAt(3)))-8);
        return new Turn(chessboard.getFields()[line1][column1], chessboard.getFields()[line2][column2]);
    }

}