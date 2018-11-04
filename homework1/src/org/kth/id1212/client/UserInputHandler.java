package org.kth.id1212.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UserInputHandler extends Thread {
    private GameHandler gameHandler;
    private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    UserInputHandler(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    void showHelp() {
        if (gameHandler.getRemainingAttempts() > 0) {
            System.out.println("Type 1 and press <ENTER> to guess a character.");
            System.out.println("Type 2 and press <ENTER> to guess a word.");
        } else {
            System.out.println("Press ANY key to start a new game");
        }
    }

    void guessCharacter() throws IOException, InterruptedException {
        System.out.println("Type a character and press <ENTER>.");
        String guess = inputReader.readLine().split("")[0];
        gameHandler.guessCharacter(guess);
    }

    void guessWord() throws IOException, InterruptedException {
        System.out.println("Type a word and press <ENTER>.");
        String guess = inputReader.readLine().trim().split(" ")[0];
        gameHandler.guessWord(guess);
    }

    void handleInput() throws IOException, InterruptedException {
        if (gameHandler.getRemainingAttempts() == 0) {
            System.out.println("no attempts left");
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
