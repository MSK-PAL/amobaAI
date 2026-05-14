package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the 5x5 Tic-Tac-Toe board and provides methods for game logic.
 */
public class Board {
    /** The size of the board (5x5). */
    public static final int SIZE = 5;
    /** The number of consecutive cells needed to win. */
    public static final int WIN_LENGTH = 4;
    /** The 2D array representing the board. */
    private final Cell[][] grid;

    /**
     * Constructs a new empty board.
     */
    public Board() {
        grid = new Cell[SIZE][SIZE];
        reset();
    }

    /**
     * Places a move for the specified player at the given position.
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @param player the player (Cell.X or Cell.O)
     * @return true if the move was successful, false if the cell was not empty
     */
    public boolean makeMove(int row, int col, Cell player) {
        if (isValidCell(row, col) && grid[row][col] == Cell.EMPTY) {
            grid[row][col] = player;
            return true;
        }
        return false;
    }

    /**
     * Undoes a move at the specified position, setting it to EMPTY.
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     */
    public void undoMove(int row, int col) {
        if (isValidCell(row, col)) {
            grid[row][col] = Cell.EMPTY;
        }
    }

    /**
     * Checks the current state of the game (win, draw, ongoing).
     * @return the current GameState
     */
    public GameState checkWinner() {
        // Check rows, columns, diagonals, anti-diagonals
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Cell cell = grid[row][col];
                if (cell == Cell.EMPTY) continue;
                // Horizontal
                if (col <= SIZE - WIN_LENGTH && checkLine(row, col, 0, 1, cell)) {
                    return cell == Cell.X ? GameState.X_WINS : GameState.O_WINS;
                }
                // Vertical
                if (row <= SIZE - WIN_LENGTH && checkLine(row, col, 1, 0, cell)) {
                    return cell == Cell.X ? GameState.X_WINS : GameState.O_WINS;
                }
                // Diagonal
                if (row <= SIZE - WIN_LENGTH && col <= SIZE - WIN_LENGTH && checkLine(row, col, 1, 1, cell)) {
                    return cell == Cell.X ? GameState.X_WINS : GameState.O_WINS;
                }
                // Anti-diagonal
                if (row <= SIZE - WIN_LENGTH && col >= WIN_LENGTH - 1 && checkLine(row, col, 1, -1, cell)) {
                    return cell == Cell.X ? GameState.X_WINS : GameState.O_WINS;
                }
            }
        }
        if (isFull()) {
            return GameState.DRAW;
        }
        return GameState.ONGOING;
    }

    /**
     * Returns a list of available moves as int arrays [row, col].
     * @return list of available moves
     */
    public List<int[]> getAvailableMoves() {
        List<int[]> moves = new ArrayList<>();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == Cell.EMPTY) {
                    moves.add(new int[]{row, col});
                }
            }
        }
        return moves;
    }

    /**
     * Checks if the board is full.
     * @return true if all cells are filled, false otherwise
     */
    public boolean isFull() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                if (grid[row][col] == Cell.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a deep copy of the board.
     * @return a new Board object with the same cell values
     */
    public Board copy() {
        Board newBoard = new Board();
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                newBoard.grid[row][col] = this.grid[row][col];
            }
        }
        return newBoard;
    }

    /**
     * Resets the board to all EMPTY cells.
     */
    public void reset() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = Cell.EMPTY;
            }
        }
    }

    /**
     * Returns the cell value at the specified position.
     * @param row the row index (0-based)
     * @param col the column index (0-based)
     * @return the Cell value
     */
    public Cell getCell(int row, int col) {
        if (isValidCell(row, col)) {
            return grid[row][col];
        }
        return null;
    }

    /**
     * Checks if the given cell coordinates are valid.
     * @param row the row index
     * @param col the column index
     * @return true if valid, false otherwise
     */
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < SIZE && col >= 0 && col < SIZE;
    }

    /**
     * Checks if there is a line of WIN_LENGTH for the given cell starting at (row, col) in the given direction.
     * @param row starting row
     * @param col starting col
     * @param dRow row direction
     * @param dCol col direction
     * @param cell the Cell to check
     * @return true if a line is found, false otherwise
     */
    private boolean checkLine(int row, int col, int dRow, int dCol, Cell cell) {
        for (int i = 0; i < WIN_LENGTH; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (!isValidCell(r, c) || grid[r][c] != cell) {
                return false;
            }
        }
        return true;
    }
}
