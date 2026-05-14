package ai;

import model.Board;
import model.Cell;
import model.GameState;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the MiniMaxAI class.
 */
public class MiniMaxAITest {
    @Test
    public void testAIWinsImmediately() {
        Board board = new Board();
        MiniMaxAI ai = new MiniMaxAI();
        for (int i = 0; i < 3; i++) board.makeMove(0, i, Cell.O);
        int[] move = ai.getBestMove(board, Cell.O);
        assertArrayEquals(new int[]{0, 3}, move);
    }

    @Test
    public void testAIBlocksHumanWin() {
        Board board = new Board();
        MiniMaxAI ai = new MiniMaxAI();
        for (int i = 0; i < 3; i++) board.makeMove(1, i, Cell.X);
        int[] move = ai.getBestMove(board, Cell.O);
        assertArrayEquals(new int[]{1, 3}, move);
    }

    @Test
    public void testAIReturnsValidMove() {
        Board board = new Board();
        MiniMaxAI ai = new MiniMaxAI();
        int[] move = ai.getBestMove(board, Cell.O);
        assertNotNull(move);
        assertTrue(move[0] >= 0 && move[0] < Board.SIZE);
        assertTrue(move[1] >= 0 && move[1] < Board.SIZE);
        assertEquals(Cell.EMPTY, board.getCell(move[0], move[1]));
    }

    @Test
    public void testEvaluateWinLossDraw() {
        Board board = new Board();
        MiniMaxAI ai = new MiniMaxAI();
        // AI win
        for (int i = 0; i < 4; i++) board.makeMove(2, i, Cell.O);
        assertEquals(10000, aiTestEvaluate(ai, board, Cell.O));
        // Human win
        board = new Board();
        for (int i = 0; i < 4; i++) board.makeMove(3, i, Cell.X);
        assertEquals(-10000, aiTestEvaluate(ai, board, Cell.O));
        // Draw (empty board has no winner, evaluate should return 0)
        board = new Board();
        assertEquals(0, aiTestEvaluate(ai, board, Cell.O));
    }

    // Helper to access private evaluate method via reflection
    private int aiTestEvaluate(MiniMaxAI ai, Board board, Cell aiPlayer) {
        try {
            java.lang.reflect.Method m = MiniMaxAI.class.getDeclaredMethod("evaluate", Board.class, Cell.class, GameState.class, int.class);
            m.setAccessible(true);
            return (int) m.invoke(ai, board, aiPlayer, board.checkWinner(), 0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
