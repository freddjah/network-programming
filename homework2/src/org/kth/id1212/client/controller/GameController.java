package org.kth.id1212.client.controller;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.view.InputTerminalView;
import org.kth.id1212.client.view.OutputTerminalView;
import org.kth.id1212.common.Command;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameController {
  private ServerHandler server;
  private OutputTerminalView outputHandler;
  private InputTerminalView inputHandler;
  private AtomicBoolean gameInitiated = new AtomicBoolean(false);
  private AtomicBoolean waitingForServerResponse = new AtomicBoolean(false);

  public final static String CMD_GUESS_CHAR = "1";
  public final static String CMD_GUESS_WORD = "2";
  public final static String CMD_QUIT = "q";

  /**
  * Responsible for the Hangman I/O, spawns to threads (one that handles user input and one that handles server responses)
  */
  public GameController(ServerHandler server) {
    this.server = server;
    this.outputHandler = new OutputTerminalView();
    this.inputHandler = new InputTerminalView();
  }

  public InputTerminalView getInputHandler() {
    return this.inputHandler;
  }

  public boolean isGameInitiated() {
    return this.gameInitiated.get();
  }

  public boolean isWaitingForServerResponse() {
    return this.waitingForServerResponse.get();
  }

  public void showStartGameInformation() {
    this.outputHandler.println("Press <ENTER> to start a new game");
  }

  public void showQuitGameInformation() {
    this.outputHandler.println("Press " + GameController.CMD_QUIT + " and <ENTER> to quit");
  }

  public void showGameInitiatedInformation() {
    this.outputHandler.println("Press " + GameController.CMD_GUESS_CHAR + " and <ENTER> to guess a character");
    this.outputHandler.println("Press " + GameController.CMD_GUESS_WORD + " and <ENTER> to guess a word");
  }
  
  private void showGuessCharacterInformation() {
    this.outputHandler.println("Type a character and press <ENTER>");
  }

  private void showGuessWordInformation() {
    this.outputHandler.println("Type a word and press <ENTER>");
  }

  /**
   * Sends exit command to server and shuts down server connection
   */
  public void shutDownClient() throws InterruptedException, IOException {
    Command exitCommand = new Command("exit");
    this.server.addMessage(exitCommand.toString());
    this.server.shutDownConnection();
    showShuttingDownProgressBar();
  }

  private void showShuttingDownProgressBar() throws InterruptedException {
    int unnecessaryProgressBarCountdown = 10;
    this.outputHandler.print("Shutting down process.");

    while (unnecessaryProgressBarCountdown > 0) {
      Thread.sleep(100);
      this.outputHandler.print(".");
      unnecessaryProgressBarCountdown--;
    }

    System.exit(0);
  }
  
  /**
   * Sends a command to the server and sets the 'waiting for response' flag to true
   */
  public void sendCommandToServer(String type, String key, String value) throws IOException{
    if (type == null) return;
    
    Command cmd = new Command(type);
    
    if (key != null && value != null) cmd.set(key, value);

    this.server.addMessage(cmd.toString());
    this.waitingForServerResponse.set(true);
  }
  
  /**
   * Receives from the server and creates a Command object from the information
   */
  public Command receiveServerResponse() throws Exception {
    String response = this.server.retrieveMessage();
    this.waitingForServerResponse.set(false);
    return Command.createFromString(response);
  }

  /**
   * Prompts the user to enter a character, validates it and returns the character
   */
  public String handleChoiceGuessCharacter() throws IOException {
    this.showGuessCharacterInformation();
    String character = this.inputHandler.retrieveInput();
    
    if (character.length() != 1 || !character.chars().allMatch(Character::isLetter)) {
      return handleChoiceGuessCharacter();
    }
    
    this.outputHandler.println("");
    
    return character;
  }

  /**
   * Prompts the user to enter a word, validates it and returns the word
   */
  public String handleChoiceGuessWord() throws IOException {
    this.showGuessWordInformation();
    String word = this.inputHandler.retrieveInput();
    
    if (word.length() == 0 || !word.chars().allMatch(Character::isLetter)) {
      return handleChoiceGuessWord();
    }
    
    this.outputHandler.println("");

    return word;
  }
  
  /**
   * Handles a server response by extracting the state and printing score to the user. Also displays helpful information about what the next step is for the user.
   * @param cmd The server response as a command
   */
  public void handleServerResponse(Command cmd) {
    String letters = cmd.get("letters");
    int points = Integer.parseInt(cmd.get("score"));
    int attemptsRemaining = Integer.parseInt(cmd.get("remaining_attempts"));
    boolean gameFinished = !letters.contains("_") || attemptsRemaining == 0;

    // Neat formatting for unsolved words with underscores.
    if (!gameFinished || attemptsRemaining == 0) letters = letters.replace("", " ");

    this.gameInitiated.set(!gameFinished);
    this.outputHandler.println("CURRENT WORD: " + letters + " || CURRENT SCORE: " + points + " || ATTEMPTS REMAINING: " + attemptsRemaining + "\n");
    
    // Display information for the user on how to proceed
    if (!gameFinished) this.showGameInitiatedInformation();
    else this.showStartGameInformation();

    // Display information on how to quit
    this.showQuitGameInformation();
  }
}
