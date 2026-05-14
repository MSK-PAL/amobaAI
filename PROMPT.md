Create a complete Java Tic-Tac-Toe (5x5 board, 4-in-a-row to win) application with AI opponent using Minimax and Alpha-Beta pruning.
Project structure (Maven or plain Java):
src/
model/
Board.java
Cell.java (enum: EMPTY, X, O)
GameState.java (enum: ONGOING, X_WINS, O_WINS, DRAW)
ai/
MiniMaxAI.java
gui/
GameWindow.java
BoardPanel.java
main/
Main.java
test/
model/
BoardTest.java
ai/
MiniMaxAITest.java
Board.java requirements:

Stores a 5x5 grid of Cell values
Win condition: 4 in a row (horizontal, vertical, diagonal, anti-diagonal)
Methods: makeMove(int row, int col, Cell player), undoMove(int row, int col), checkWinner() returns GameState, getAvailableMoves() returns List<int[]>, isFull(), copy() returns a deep copy, reset()
All methods must have English Javadoc comments

MiniMaxAI.java requirements:

Method getBestMove(Board board, Cell aiPlayer) returns int[] (row, col)
Implements Minimax algorithm with Alpha-Beta pruning
Search depth limited to 4 (5x5 board is too large for full search)
Private minimax(Board board, int depth, int alpha, int beta, boolean isMaximizing, Cell aiPlayer) method
Private evaluate(Board board, Cell aiPlayer) returns:

+10000 for AI win, -10000 for human win
Heuristic scoring for non-terminal states: count open sequences of 3, 2 for both players


Score adjusted by depth: prefer faster wins
All methods must have English Javadoc comments

GameWindow.java requirements:

Extends JFrame
Contains a BoardPanel and a status label showing current game state ("Your turn", "AI is thinking...", "You win!", "AI wins!", "Draw!")
"New Game" button that resets the board
Player is always X (goes first), AI is always O

BoardPanel.java requirements:

Extends JPanel
Draws the 5x5 grid with lines
Draws X as two crossing lines (blue), O as a circle (red)
Handles mouseClicked event: only allow clicks when it is the human's turn and game is ONGOING
After human move, triggers AI move on a SwingWorker thread (so GUI doesn't freeze)
Winning 4 cells are highlighted with a light green background

BoardTest.java — test every method:

Test initial board is all EMPTY
Test makeMove places correct Cell
Test checkWinner detects row win, column win, diagonal win, anti-diagonal win (all with exactly 4 in a row)
Test checkWinner returns DRAW when board is full with no winner
Test checkWinner returns ONGOING when game is not finished
Test getAvailableMoves returns 25 on empty board, decreases after moves
Test isFull returns false on empty, true when all 25 cells filled
Test copy returns independent deep copy (modifying copy does not affect original)
Test undoMove restores EMPTY correctly
Test reset clears all cells

MiniMaxAITest.java — test AI decisions:

Test AI immediately wins when it has three O's in a row with one open end
Test AI blocks human from winning (human has three X's in a row)
Test AI returns a valid move (within bounds, on an EMPTY cell) in any board state
Test evaluate returns +10000 for AI win state, -10000 for human win state, 0 for draw

Code style:

Every class, method and field must have English Javadoc comments
No Hungarian variable names
Use SwingUtilities.invokeLater for GUI initialization in Main.java
Use standard Java (no external libraries except JUnit 5 for tests)