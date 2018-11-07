package org.kth.id1212.client.controller;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.net.OutputHandler;
import org.kth.id1212.client.net.InputHandler;
import org.kth.id1212.client.view.InputTerminalView;
import org.kth.id1212.client.view.OutputTerminalView;
import org.kth.id1212.common.Command;
import org.kth.id1212.client.model.GameViewModel;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Responsible for the Hangman I/O
 */
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

  /**
   * Prints a message to the user. Upon user input a 'start game' message is sent to the server.
   */
  public void startGame() throws IOException {
    this.outputHandler.println("Press ANY key to start a new game");
    this.inputHandler.retrieveInput();

    Command command = new Command("start_game");
    server.send(command.toString());
  }
  
  /**
   * Prompts the user to enter a character, waits for input, validates and sends 'guess character' message to server.
   */
  public void guessCharacter() throws Exception {
    this.outputHandler.println("Type a character and press <ENTER>.");
    String character = this.inputHandler.retrieveInput();

    if (character.length() == 0 || character.length() > 1 || !character.chars().allMatch(Character::isLetter)) {
      guessCharacter();
      return;
    }

    Command command = new Command("guess_char");
    command.set("char", character);

    server.send(command.toString());
  }
  
  /**
   * Prompts the user to enter a word, waits for input, validates and send 'guess word' message to server.
   */
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

  /**
   * Prompts the user to enter a number that corresponds to either guessing a word or a character.
   * @return String commandEntered
   */
  public String initiateUserGuess() throws IOException {
    this.outputHandler.println("Press 1 and press <ENTER> to guess a character.");
    this.outputHandler.println("Press 2 and press <ENTER> to guess a word.");
    return this.inputHandler.retrieveInput().trim().split("")[0];
  }

  /**
   * Receives server message and converts it to a GameViewModel that can be used to easily send to the outputhandler and printed into terminal.
   * @return GameViewModel updatedGame
   */
  public GameViewModel receiveServerResponse() throws Exception {
    String response = server.receive();
    Command command = Command.createFromString(response);

    String letters = command.get("letters");
    int points = Integer.parseInt(command.get("score"));
    int attemptsRemaining = Integer.parseInt(command.get("remaining_attempts"));

    return new GameViewModel(letters, points, attemptsRemaining);
  }

  /**
   * Simply prints out a game using the OutputHandler.
   * @param game A representation of a game of type GameViewModel
   */
  public void printScore(GameViewModel game) {
    this.outputHandler.println("\n\n" + game.toString());
  }

  /**
   * Prints a nice loading screen while waiting for a response.
   * @param waitingForServerResponse
   */
  public void loading(AtomicBoolean waitingForServerResponse) throws InterruptedException {
    this.outputHandler.print("Waiting for server");

    while (waitingForServerResponse.get()) {
      this.outputHandler.print(".");
      Thread.sleep(5);
    }
  }
}
