package gui;

import model.Board;
import model.Cell;
import model.GameState;
import ai.MiniMaxAI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The main game window for the Tic-Tac-Toe application.
 */
public class GameWindow extends JFrame {
    /** The board panel for drawing and interaction. */
    private BoardPanel boardPanel;
    /** The status label for game messages. */
    private JLabel statusLabel;
    /** The new game button. */
    private JButton newGameButton;
    /** The game board model. */
    private Board board;
    /** The AI opponent. */
    private MiniMaxAI ai;
    /** The current game state. */
    private GameState gameState;
    /** True if it is the human's turn. */
    private boolean humanTurn;

    /**
     * Constructs the game window and initializes components.
     */
    public GameWindow() {
        setTitle("Tic-Tac-Toe 5x5 (4-in-a-row)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        board = new Board();
        ai = new MiniMaxAI();
        gameState = GameState.ONGOING;
        humanTurn = true;
        boardPanel = new BoardPanel(board, this);
        statusLabel = new JLabel("Your turn");
        newGameButton = new JButton("New Game");
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statusLabel, BorderLayout.CENTER);
        topPanel.add(newGameButton, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Resets the game to the initial state.
     */
    public void resetGame() {
        board.reset();
        gameState = GameState.ONGOING;
        humanTurn = true;
        boardPanel.clearHighlight();
        boardPanel.repaint();
        statusLabel.setText("Your turn");
    }

    /**
     * Handles a move made by the human player.
     * @param row the row index
     * @param col the column index
     */
    public void handleHumanMove(int row, int col) {
        if (!humanTurn || gameState != GameState.ONGOING) return;
        if (board.makeMove(row, col, Cell.X)) {
            boardPanel.repaint();
            gameState = board.checkWinner();
            if (gameState == GameState.ONGOING) {
                humanTurn = false;
                statusLabel.setText("AI is thinking...");
                makeAIMoveAsync();
            } else {
                endGame();
            }
        }
    }

    /**
     * Triggers the AI move on a background thread.
     */
    private void makeAIMoveAsync() {
        SwingWorker<int[], Void> worker = new SwingWorker<int[], Void>() {
            @Override
            protected int[] doInBackground() {
                return ai.getBestMove(board.copy(), Cell.O);
            }
            @Override
            protected void done() {
                try {
                    int[] move = get();
                    if (move != null) {
                        board.makeMove(move[0], move[1], Cell.O);
                        boardPanel.repaint();
                        gameState = board.checkWinner();
                        if (gameState == GameState.ONGOING) {
                            humanTurn = true;
                            statusLabel.setText("Your turn");
                        } else {
                            endGame();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    /**
     * Handles the end of the game, updating the status label and highlighting winning cells.
     */
    private void endGame() {
        switch (gameState) {
            case X_WINS:
                statusLabel.setText("You win!");
                boardPanel.highlightWinningCells(Cell.X);
                break;
            case O_WINS:
                statusLabel.setText("AI wins!");
                boardPanel.highlightWinningCells(Cell.O);
                break;
            case DRAW:
                statusLabel.setText("Draw!");
                break;
            default:
                break;
        }
    }

    /**
     * Returns true if it is the human's turn.
     * @return true if human's turn
     */
    public boolean isHumanTurn() {
        return humanTurn;
    }

    /**
     * Returns the current game state.
     * @return the game state
     */
    public GameState getGameState() {
        return gameState;
    }
}
