package org.kth.id1212.client.view;

import org.kth.id1212.client.net.GameHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalView extends Thread {
    private GameHandler gameHandler;
    private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    public TerminalView(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void showHelp() {
        if (gameHandler.getRemainingAttempts() > 0) {
            System.out.println("Type 1 and press <ENTER> to guess a character.");
            System.out.println("Type 2 and press <ENTER> to guess a word.");
        } else {
            System.out.println("Press ANY key to start a new game");
        }
    }

    public void guessCharacter() throws IOException, InterruptedException {
        System.out.println("Type a character and press <ENTER>.");
        String guess = inputReader.readLine().split("")[0];
        gameHandler.guessCharacter(guess);
    }

    public void guessWord() throws IOException, InterruptedException {
        System.out.println("Type a word and press <ENTER>.");
        String guess = inputReader.readLine().trim().split(" ")[0];
        gameHandler.guessWord(guess);
    }

    public void handleInput() throws IOException, InterruptedException {
        if (gameHandler.getRemainingAttempts() == 0) {
            String cmd = inputReader.readLine();
            gameHandler.startGame();

            Thread.sleep(500);
        } else {
            String cmd = inputReader.readLine().trim().split("")[0];
            switch (cmd) {
                case "1":   guessCharacter();
                            break;
                case "2":   guessWord();
                            break;
                default:    showHelp();
                            break;
            }
        }
    }

    public static void showCurrentScore(String currentWord, int currentScore, int remainingAttempts) {
        String formattedCurrentWord = currentWord.replace("", " ").trim();
        System.out.println(formattedCurrentWord + " || Current Score: " + currentScore + " || Remaining attempts: " + remainingAttempts);
    }

    @Override
    public void run() {
        showHelp();
        while (true) {
            try {
                handleInput();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
