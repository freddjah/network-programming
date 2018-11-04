package org.kth.id1212.client.net;

import org.kth.id1212.common.Command;
import org.kth.id1212.client.controller.ClientController;
import org.kth.id1212.client.model.Game;
import org.kth.id1212.client.view.TerminalView;

import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler extends Thread {
    private ClientController controller;
    private LinkedBlockingQueue<String> serverResponses = new LinkedBlockingQueue<>();
    private Game game = new Game();

    public GameHandler(ClientController controller) {
        this.controller = controller;
        TerminalView terminal = new TerminalView(this);
        terminal.start();
    }

    public synchronized void addServerResponse(String message) {
        this.serverResponses.add(message);
        notify();
    }

    public synchronized String handleServerResponse() throws InterruptedException {
        while(this.serverResponses.isEmpty()) wait();

        return this.serverResponses.poll();
    }

    public void startGame() throws InterruptedException {
        Command command = new Command("start_game");
        controller.addRequest(command.toString());
    }

    public void guessCharacter(String guess) throws InterruptedException {
        Command command = new Command("guess_char");
        command.set("char", guess);
        controller.addRequest(command.toString());
    }

    public void guessWord(String guess) throws InterruptedException {
        Command command = new Command("guess_word");
        command.set("word", guess);
        controller.addRequest(command.toString());
    }

    public String getCurrentWord() {
        return game.getCurrentWord();
    }

    public int getCurrentScore() {
        return game.getCurrentScore();
    }

    public int getRemainingAttempts() {
        return game.getRemainingAttempts();
    }

    @Override
    public void run() {
        while (true) {
            try {
                String response = handleServerResponse();
                Command command = Command.createFromString(response);

                int currentScore = Integer.parseInt(command.get("score"));
                String currentWord = command.get("letters");
                int remainingAttempts = Integer.parseInt(command.get("remaining_attempts"));

                this.game.setCurrentScore(currentScore);
                this.game.setCurrentWord(currentWord);
                this.game.setRemainingAttempts(remainingAttempts);

                TerminalView.showCurrentScore(currentWord, currentScore, remainingAttempts);

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
