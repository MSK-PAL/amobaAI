package model;

/**
 * Represents the possible states of the Tic-Tac-Toe game.
 */
public enum GameState {
    /** The game is ongoing. */
    ONGOING,
    /** The X player has won. */
    X_WINS,
    /** The O player has won. */
    O_WINS,
    /** The game ended in a draw. */
    DRAW
}
