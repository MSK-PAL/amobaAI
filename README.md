## Generation

A projekt a `PROMPT.md`-ben található prompt segítségével készült,
**GitHub Copilot Chat** használatával (**GPT-4.1**, Agent módban).

A promptot **Claude Sonnet 4.5** segítségével generáltam.

# Amőba AI – 5x5 Tic-Tac-Toe with Minimax

A Java implementation of a 5x5 Tic-Tac-Toe game with an AI opponent using the Minimax algorithm with Alpha-Beta pruning.

## Rules

- 5x5 board, **4 in a row** to win (horizontal, vertical, diagonal, anti-diagonal)
- Human plays as **X** (goes first), AI plays as **O**
- If all 25 cells are filled with no winner, the game ends in a draw

## Features

- Graphical interface built with **Java Swing**
- AI opponent using **Minimax + Alpha-Beta pruning** (depth limit: 4)
- Winning cells highlighted in green
- New Game button to restart at any time

## Project Structure

```
src/
  main/java/
    model/        – Board, Cell, GameState
    ai/           – MiniMaxAI
    gui/          – GameWindow, BoardPanel
    main/         – Main
  test/java/
    model/        – BoardTest
    ai/           – MiniMaxAITest
```

## Requirements

- Java 17+
- Maven 3.x

## How to Run

```bash
git clone https://github.com/MSK-PAL/amobaAI.git
cd amobaAI
mvn compile
mvn exec:java -Dexec.mainClass="main.Main"
```

Or open in **IntelliJ IDEA** and run `Main.java` directly.

## How to Run Tests

```bash
mvn test
```

## Test Coverage

| Class | Method | Line | Branch |
|-------|--------|------|--------|
| Board | 100% | 94% | 76% |
| Cell | 100% | 100% | 100% |
| GameState | 100% | 100% | 100% |
| MiniMaxAI | 100% | 98% | 87% |

## Generation

This project was generated using **GitHub Copilot** (GPT-4.1, Agent mode).  
The prompt used for generation is available in [`PROMPT.md`](PROMPT.md).