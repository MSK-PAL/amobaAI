package gui;

import model.Board;
import model.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom JPanel for drawing the Tic-Tac-Toe board and handling user interaction.
 */
public class BoardPanel extends JPanel {
    /** The board model. */
    private final Board board;
    /** The parent game window. */
    private final GameWindow gameWindow;
    /** List of winning cell coordinates to highlight. */
    private List<int[]> highlightCells = new ArrayList<>();
    /** Cell size in pixels. */
    private static final int CELL_SIZE = 80;
    /** Board padding in pixels. */
    private static final int PADDING = 20;

    /**
     * Constructs the board panel.
     * @param board the board model
     * @param gameWindow the parent game window
     */
    public BoardPanel(Board board, GameWindow gameWindow) {
        this.board = board;
        this.gameWindow = gameWindow;
        setPreferredSize(new Dimension(
                Board.SIZE * CELL_SIZE + 2 * PADDING,
                Board.SIZE * CELL_SIZE + 2 * PADDING));
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameWindow.isHumanTurn() || gameWindow.getGameState() != model.GameState.ONGOING) return;
                int x = e.getX() - PADDING;
                int y = e.getY() - PADDING;
                if (x < 0 || y < 0) return;
                int col = x / CELL_SIZE;
                int row = y / CELL_SIZE;
                if (row >= 0 && row < Board.SIZE && col >= 0 && col < Board.SIZE) {
                    gameWindow.handleHumanMove(row, col);
                }
            }
        });
    }

    /**
     * Paints the board and pieces.
     * @param g the Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        // Draw grid
        for (int i = 0; i <= Board.SIZE; i++) {
            int pos = PADDING + i * CELL_SIZE;
            g2.drawLine(PADDING, pos, PADDING + Board.SIZE * CELL_SIZE, pos);
            g2.drawLine(pos, PADDING, pos, PADDING + Board.SIZE * CELL_SIZE);
        }
        // Highlight winning cells
        g2.setColor(new Color(180, 255, 180));
        for (int[] cell : highlightCells) {
            int x = PADDING + cell[1] * CELL_SIZE;
            int y = PADDING + cell[0] * CELL_SIZE;
            g2.fillRect(x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4);
        }
        // Draw pieces
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Cell cell = board.getCell(row, col);
                int x = PADDING + col * CELL_SIZE;
                int y = PADDING + row * CELL_SIZE;
                if (cell == Cell.X) {
                    g2.setColor(Color.BLUE);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine(x + 10, y + 10, x + CELL_SIZE - 10, y + CELL_SIZE - 10);
                    g2.drawLine(x + 10, y + CELL_SIZE - 10, x + CELL_SIZE - 10, y + 10);
                } else if (cell == Cell.O) {
                    g2.setColor(Color.RED);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawOval(x + 10, y + 10, CELL_SIZE - 20, CELL_SIZE - 20);
                }
            }
        }
    }

    /**
     * Highlights the winning cells for the specified player.
     * @param player the player (Cell.X or Cell.O)
     */
    public void highlightWinningCells(Cell player) {
        highlightCells = findWinningCells(player);
        repaint();
    }

    /**
     * Clears any highlighted cells.
     */
    public void clearHighlight() {
        highlightCells.clear();
        repaint();
    }

    /**
     * Finds the coordinates of the winning 4 cells for the specified player.
     * @param player the player
     * @return list of [row, col] coordinates
     */
    private List<int[]> findWinningCells(Cell player) {
        int size = Board.SIZE;
        int winLen = Board.WIN_LENGTH;
        // Horizontal, vertical, diagonal, anti-diagonal
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                // Horizontal
                if (col <= size - winLen && isWinningLine(row, col, 0, 1, player))
                    return getLineCells(row, col, 0, 1);
                // Vertical
                if (row <= size - winLen && isWinningLine(row, col, 1, 0, player))
                    return getLineCells(row, col, 1, 0);
                // Diagonal
                if (row <= size - winLen && col <= size - winLen && isWinningLine(row, col, 1, 1, player))
                    return getLineCells(row, col, 1, 1);
                // Anti-diagonal
                if (row <= size - winLen && col >= winLen - 1 && isWinningLine(row, col, 1, -1, player))
                    return getLineCells(row, col, 1, -1);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Checks if there is a winning line starting at (row, col) in the given direction.
     */
    private boolean isWinningLine(int row, int col, int dRow, int dCol, Cell player) {
        for (int i = 0; i < Board.WIN_LENGTH; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (board.getCell(r, c) != player) return false;
        }
        return true;
    }

    /**
     * Returns the coordinates of a line starting at (row, col) in the given direction.
     */
    private List<int[]> getLineCells(int row, int col, int dRow, int dCol) {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < Board.WIN_LENGTH; i++) {
            cells.add(new int[]{row + i * dRow, col + i * dCol});
        }
        return cells;
    }
}
