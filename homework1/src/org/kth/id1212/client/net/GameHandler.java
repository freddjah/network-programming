package org.kth.id1212.client.net;

import org.kth.id1212.common.Command;
import org.kth.id1212.client.controller.ClientController;
import org.kth.id1212.client.model.Game;
import org.kth.id1212.client.view.TerminalView;

import java.util.concurrent.LinkedBlockingQueue;

public class GameHandler extends Thread {
  private ClientController controller;
  private LinkedBlockingQueue<String> serverResponses = new LinkedBlockingQueue<>();
  private TerminalView terminal;
  private Game game = new Game();

  public GameHandler(ClientController controller) {
    this.controller = controller;
    this.terminal = new TerminalView(this);
    this.terminal.start();
    this.terminal.showHelp();
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
      if (Thread.currentThread().isInterrupted()) {
        System.out.println("\nError has occurred: UI shutting down.");
        System.exit(0);
      }

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

        this.terminal.showHelp();

      } catch (InterruptedException e) {
        controller.stopServices();
      } catch (Exception e) {
        controller.stopServices();
      }
    }
  }
}
