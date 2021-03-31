package model;

public class Main {
    public static void main(String[] args) {
        Chessboard board = Chessboard.getInstance();
        board.createBoard(8);
        board.addFen("rnbqkbnr/pppppppp/8/2Q5/8/8/PPPPP1PP/RNBQKBNR w KQkq - 0 1");
        board.setBoardByFen("rnbqkbnr/pppppppp/8/2Q5/8/8/PPPPP1PP/RNBQKBNR w KQkq - 0 1");
        board.printBoard();
        board.getBoardAsFen();
    }
}
