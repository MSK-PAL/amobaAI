package ai;

import model.Board;
import model.Cell;
import model.GameState;
import java.util.List;

/**
 * Implements the Minimax algorithm with Alpha-Beta pruning for the AI opponent.
 */
public class MiniMaxAI {
    /** The maximum search depth for the Minimax algorithm. */
    private static final int MAX_DEPTH = 4;

    /**
     * Returns the best move for the AI player using Minimax with Alpha-Beta pruning.
     * @param board the current board state
     * @param aiPlayer the AI player (Cell.O)
     * @return an int array [row, col] representing the best move
     */
    public int[] getBestMove(Board board, Cell aiPlayer) {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = null;
        List<int[]> moves = board.getAvailableMoves();
        for (int[] move : moves) {
            board.makeMove(move[0], move[1], aiPlayer);
            int score = minimax(board, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false, aiPlayer);
            board.undoMove(move[0], move[1]);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    /**
     * Minimax algorithm with Alpha-Beta pruning.
     * @param board the current board state
     * @param depth the current search depth
     * @param alpha the alpha value for pruning
     * @param beta the beta value for pruning
     * @param isMaximizing true if maximizing, false if minimizing
     * @param aiPlayer the AI player (Cell.O)
     * @return the evaluated score
     */
    private int minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing, Cell aiPlayer) {
        GameState state = board.checkWinner();
        if (state != GameState.ONGOING || depth == MAX_DEPTH) {
            return evaluate(board, aiPlayer, state, depth);
        }
        List<int[]> moves = board.getAvailableMoves();
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Cell currentPlayer = isMaximizing ? aiPlayer : (aiPlayer == Cell.X ? Cell.O : Cell.X);
        for (int[] move : moves) {
            board.makeMove(move[0], move[1], currentPlayer);
            int score = minimax(board, depth + 1, alpha, beta, !isMaximizing, aiPlayer);
            board.undoMove(move[0], move[1]);
            if (isMaximizing) {
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
            } else {
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
            }
            if (beta <= alpha) {
                break;
            }
        }
        return bestScore;
    }

    /**
     * Evaluates the board state for the AI player.
     * @param board the board to evaluate
     * @param aiPlayer the AI player
     * @param state the current game state
     * @param depth the current search depth
     * @return the evaluation score
     */
    private int evaluate(Board board, Cell aiPlayer, GameState state, int depth) {
        Cell humanPlayer = aiPlayer == Cell.X ? Cell.O : Cell.X;
        if (state == GameState.X_WINS) {
            return aiPlayer == Cell.X ? 10000 - depth : -10000 + depth;
        } else if (state == GameState.O_WINS) {
            return aiPlayer == Cell.O ? 10000 - depth : -10000 + depth;
        } else if (state == GameState.DRAW) {
            return 0;
        }
        // Heuristic: count open sequences of 3 and 2 for both players
        int aiScore = countOpenSequences(board, aiPlayer, 3) * 1000 + countOpenSequences(board, aiPlayer, 2) * 100;
        int humanScore = countOpenSequences(board, humanPlayer, 3) * 1000 + countOpenSequences(board, humanPlayer, 2) * 100;
        return aiScore - humanScore;
    }

    /**
     * Counts the number of open sequences of a given length for a player.
     * @param board the board
     * @param player the player
     * @param length the sequence length
     * @return the number of open sequences
     */
    private int countOpenSequences(Board board, Cell player, int length) {
        int count = 0;
        int size = Board.SIZE;
        // Horizontal, vertical, diagonal, anti-diagonal
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Horizontal
                if (col <= size - length && isOpenSequence(board, row, col, 0, 1, player, length)) count++;
                // Vertical
                if (row <= size - length && isOpenSequence(board, row, col, 1, 0, player, length)) count++;
                // Diagonal
                if (row <= size - length && col <= size - length && isOpenSequence(board, row, col, 1, 1, player, length)) count++;
                // Anti-diagonal
                if (row <= size - length && col >= length - 1 && isOpenSequence(board, row, col, 1, -1, player, length)) count++;
            }
        }
        return count;
    }

    /**
     * Checks if there is an open sequence of the given length for the player starting at (row, col) in the given direction.
     * @param board the board
     * @param row starting row
     * @param col starting col
     * @param dRow row direction
     * @param dCol col direction
     * @param player the player
     * @param length the sequence length
     * @return true if open sequence, false otherwise
     */
    private boolean isOpenSequence(Board board, int row, int col, int dRow, int dCol, Cell player, int length) {
        int size = Board.SIZE;
        int endRow = row + (length - 1) * dRow;
        int endCol = col + (length - 1) * dCol;
        if (endRow < 0 || endRow >= size || endCol < 0 || endCol >= size) return false;
        for (int i = 0; i < length; i++) {
            if (board.getCell(row + i * dRow, col + i * dCol) != player) return false;
        }
        // Check open ends
        int beforeRow = row - dRow;
        int beforeCol = col - dCol;
        int afterRow = endRow + dRow;
        int afterCol = endCol + dCol;
        boolean beforeOpen = beforeRow >= 0 && beforeRow < size && beforeCol >= 0 && beforeCol < size && board.getCell(beforeRow, beforeCol) == Cell.EMPTY;
        boolean afterOpen = afterRow >= 0 && afterRow < size && afterCol >= 0 && afterCol < size && board.getCell(afterRow, afterCol) == Cell.EMPTY;
        return beforeOpen || afterOpen;
    }
}
