package model;

import model.Board;
import model.Cell;
import model.GameState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Board class.
 */
public class BoardTest {
    @Test
    public void testInitialBoardIsEmpty() {
        Board board = new Board();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                assertEquals(Cell.EMPTY, board.getCell(row, col));
            }
        }
    }

    @Test
    public void testMakeMovePlacesCorrectCell() {
        Board board = new Board();
        assertTrue(board.makeMove(2, 3, Cell.X));
        assertEquals(Cell.X, board.getCell(2, 3));
    }

    @Test
    public void testCheckWinnerRowWin() {
        Board board = new Board();
        for (int i = 0; i < 4; i++) board.makeMove(1, i, Cell.X);
        assertEquals(GameState.X_WINS, board.checkWinner());
    }

    @Test
    public void testCheckWinnerColumnWin() {
        Board board = new Board();
        for (int i = 0; i < 4; i++) board.makeMove(i, 2, Cell.O);
        assertEquals(GameState.O_WINS, board.checkWinner());
    }

    @Test
    public void testCheckWinnerDiagonalWin() {
        Board board = new Board();
        for (int i = 0; i < 4; i++) board.makeMove(i, i, Cell.X);
        assertEquals(GameState.X_WINS, board.checkWinner());
    }

    @Test
    public void testCheckWinnerAntiDiagonalWin() {
        Board board = new Board();
        for (int i = 0; i < 4; i++) board.makeMove(i, 4 - i, Cell.O);
        assertEquals(GameState.O_WINS, board.checkWinner());
    }

    @Test
    public void testCheckWinnerNoWinnerYet() {
        Board board = new Board();
        board.makeMove(0, 0, Cell.X);
        board.makeMove(1, 0, Cell.O);
        board.makeMove(0, 1, Cell.X);
        board.makeMove(1, 1, Cell.O);
        board.makeMove(0, 2, Cell.X);
        assertEquals(GameState.ONGOING, board.checkWinner());
    }

    @Test
    public void testCheckWinnerOngoing() {
        Board board = new Board();
        board.makeMove(0, 0, Cell.X);
        assertEquals(GameState.ONGOING, board.checkWinner());
    }

    @Test
    public void testGetAvailableMoves() {
        Board board = new Board();
        assertEquals(25, board.getAvailableMoves().size());
        board.makeMove(0, 0, Cell.X);
        assertEquals(24, board.getAvailableMoves().size());
    }

    @Test
    public void testIsFull() {
        Board board = new Board();
        assertFalse(board.isFull());
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                board.makeMove(row, col, Cell.X);
            }
        }
        assertTrue(board.isFull());
    }

    @Test
    public void testCopyIsDeepCopy() {
        Board board = new Board();
        board.makeMove(1, 1, Cell.X);
        Board copy = board.copy();
        copy.makeMove(2, 2, Cell.O);
        assertEquals(Cell.EMPTY, board.getCell(2, 2));
        assertEquals(Cell.O, copy.getCell(2, 2));
    }

    @Test
    public void testUndoMoveRestoresEmpty() {
        Board board = new Board();
        board.makeMove(3, 3, Cell.X);
        board.undoMove(3, 3);
        assertEquals(Cell.EMPTY, board.getCell(3, 3));
    }

    @Test
    public void testResetClearsAllCells() {
        Board board = new Board();
        board.makeMove(0, 0, Cell.X);
        board.reset();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                assertEquals(Cell.EMPTY, board.getCell(row, col));
            }
        }
    }
}
