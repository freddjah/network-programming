package org.kth.id1212.client;

import org.kth.id1212.Command;

import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler extends Thread {
    private Client controller;
    private LinkedBlockingQueue<String> serverResponses = new LinkedBlockingQueue<>();
    private Game game = new Game();

    GameHandler(Client controller) {
        this.controller = controller;
        UserInputHandler terminal = new UserInputHandler(this);
        terminal.start();
    }

    synchronized void addServerResponse(String message) {
        this.serverResponses.add(message);
        notify();
    }

    synchronized String handleServerResponse() throws InterruptedException {
        while(this.serverResponses.isEmpty()) wait();

        return this.serverResponses.poll();
    }

    void startGame() throws InterruptedException {
        Command command = new Command("start_game");
        controller.addRequest(command.toString());
    }

    void guessCharacter(String guess) throws InterruptedException {
        Command command = new Command("guess_char");
        command.set("char", guess);
        controller.addRequest(command.toString());
    }

    void guessWord(String guess) throws InterruptedException {
        Command command = new Command("guess_word");
        command.set("word", guess);
        controller.addRequest(command.toString());
    }

    String getCurrentWord() {
        return game.getCurrentWord();
    }

    int getCurrentScore() {
        return game.getCurrentScore();
    }

    int getRemainingAttempts() {
        return game.getRemainingAttempts();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String response = handleServerResponse();
                System.out.println(response);
                Command command = Command.createFromString(response);

                int currentScore = Integer.parseInt(command.get("score"));
                String currentWord = command.get("letters");
                int remainingAttempts = Integer.parseInt(command.get("remaining_attempts"));

                this.game.setCurrentScore(currentScore);
                this.game.setCurrentWord(currentWord);
                this.game.setRemainingAttempts(remainingAttempts);

                GameOutputHandler.showCurrentScore(currentWord, currentScore, remainingAttempts);

                if (!this.game.getCurrentWord().contains("_")) {
                    this.game.setRemainingAttempts(0);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
