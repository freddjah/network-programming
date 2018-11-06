package org.kth.id1212.client.controller;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.net.OutputHandler;
import org.kth.id1212.client.net.InputHandler;
import org.kth.id1212.client.view.InputTerminalView;
import org.kth.id1212.client.view.OutputTerminalView;
import org.kth.id1212.common.Command;
import org.kth.id1212.client.model.GameViewModel;
import java.io.IOException;

public class GameController extends Thread {
  private ServerHandler server;
  private OutputHandler outputHandler;
  private InputHandler inputHandler;

  public GameController(ServerHandler server) {
    this.server = server;
  }

  public void setOutputHandler(OutputHandler output) {
    this.outputHandler = output;
  }

  public void setInputHandler(InputHandler input) {
    this.inputHandler = input;
  }

  public void startGame() throws IOException {
    this.outputHandler.println("Press ANY key to start a new game");
    this.inputHandler.retrieveInput();

    Command command = new Command("start_game");
    server.send(command.toString());
  }

  public void guessCharacter() throws Exception {
    this.outputHandler.println("Type a character and press <ENTER>.");
    String character = this.inputHandler.retrieveInput();

    if (character.length() == 0 || !character.chars().allMatch(Character::isLetter)) {
      guessCharacter();
      return;
    }

    Command command = new Command("guess_char");
    command.set("char", character);

    server.send(command.toString());
  }

  public void guessWord() throws IOException {
    this.outputHandler.println("Type a word and press <ENTER>.");
    String word = this.inputHandler.retrieveInput();

    if (word.length() == 0 || !word.chars().allMatch(Character::isLetter)) {
      guessWord();
      return;
    }

    Command command = new Command("guess_word");
    command.set("word", word);

    server.send(command.toString());
  }

  public String initiateUserGuess() throws IOException {
    this.outputHandler.println("Press 1 and press <ENTER> to guess a character.");
    this.outputHandler.println("Press 2 and press <ENTER> to guess a word.");
    return this.inputHandler.retrieveInput().trim().split("")[0];
  }

  public GameViewModel receiveServerResponse() throws Exception {
    String response = server.receive();
    Command command = Command.createFromString(response);

    String letters = command.get("letters");
    int points = Integer.parseInt(command.get("score"));
    int attemptsRemaining = Integer.parseInt(command.get("remaining_attempts"));

    return new GameViewModel(letters, points, attemptsRemaining);
  }

  public void printScore(GameViewModel game, boolean gameInitiated) {
    if (gameInitiated) {
      this.outputHandler.println("\n" + game.toString());
    } else {
      this.outputHandler.println(game.toString());
    }
  }
}
