package main;

import gui.GameWindow;

import javax.swing.SwingUtilities;

/**
 * The main entry point for the Tic-Tac-Toe application.
 */
public class Main {
    /**
     * Launches the application GUI on the Event Dispatch Thread.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GameWindow().setVisible(true);
            }
        });
    }
}
