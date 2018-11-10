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

  private final static String CMD_GUESS_CHAR = "1";
  private final static String CMD_GUESS_WORD = "2";
  private final static String CMD_QUIT = "q";

  /**
  * Responsible for the Hangman I/O, spawns to threads (one that handles user input and one that handles server responses)
  */
  public GameController(ServerHandler server) {
    this.server = server;
    this.outputHandler = new OutputTerminalView();
    this.inputHandler = new InputTerminalView();

    // Thread responible for user input
    Thread userInputThread = new Thread(() -> {
      try {
        this.showStartGameInformation();
        this.showQuitGameInformation();
        
        while(true) {
          String userInput = this.inputHandler.retrieveInput();

          // User wants to guess a character
          if (userInput.equals(this.CMD_GUESS_CHAR) && this.gameInitiated.get() && !this.waitingForServerResponse.get()) {
            String character = this.handleChoiceGuessCharacter();
            this.sendCommandToServer("guess_char", "char", character);
          }

          // User wants to guess a word
          else if (userInput.equals(this.CMD_GUESS_WORD) && this.gameInitiated.get() && !this.waitingForServerResponse.get()) {
            String word = this.handleChoiceGuessWord();
            this.sendCommandToServer("guess_word", "word", word);
          }

          // User wants to quit
          else if (userInput.equals(this.CMD_QUIT)) {
            this.showShuttingDownProgressBar();
            System.exit(0);
          }
          
          // User wants to start a new game
          else if (userInput.length() == 0 && !this.gameInitiated.get()) {
            this.sendCommandToServer("start_game", null, null);
          }

          // Game is live, but unknown command
          else if (this.gameInitiated.get() && !this.waitingForServerResponse.get()) {
            this.showGameInitiatedInformation();
            this.showQuitGameInformation();
          }
          
          // Game is live, waiting for server response but unknown command
          else if (this.gameInitiated.get() && this.waitingForServerResponse.get()) {
            this.showQuitGameInformation();
          } else {
            this.showStartGameInformation();
            this.showQuitGameInformation();
          }
        }
      } catch (Exception e) {
        System.out.println("Something went wrong. Shutting down.");
        System.exit(-1);
      }
    });

    // Thread responsible for receiving server commands, interpreting them and prompt the user
    Thread serverOutputThread = new Thread(() -> {
      while (true) {
        try {
          Command serverResponse = this.receiveServerResponse();
          this.handleServerResponse(serverResponse);
        } catch (Exception e) {
          System.out.println("Something went wrong. Shutting down.");
          System.exit(-1);
        }
      }
    });

    userInputThread.start();
    serverOutputThread.start();
  }

  private void showStartGameInformation() {
    this.outputHandler.println("Press <ENTER> to start a new game");
  }

  private void showQuitGameInformation() {
    this.outputHandler.println("Press " + this.CMD_QUIT + " and <ENTER> to quit");
  }

  private void showGameInitiatedInformation() {
    this.outputHandler.println("Press " + this.CMD_GUESS_CHAR + " and <ENTER> to guess a character");
    this.outputHandler.println("Press " + this.CMD_GUESS_WORD + " and <ENTER> to guess a word");
  }
  
  private void showGuessCharacterInformation() {
    this.outputHandler.println("Type a character and press <ENTER>");
  }

  private void showGuessWordInformation() {
    this.outputHandler.println("Type a word and press <ENTER>");
  }

  private void showShuttingDownProgressBar() throws InterruptedException {
    int unnecessaryProgressBarCountdown = 10;
    this.outputHandler.print("Shutting down process.");

    while (unnecessaryProgressBarCountdown > 0) {
      Thread.sleep(100);
      this.outputHandler.print(".");
      unnecessaryProgressBarCountdown--;
    }
  }
  
  /**
   * Sends a command to the server and sets the 'waiting for response' flag to true
   */
  private void sendCommandToServer(String type, String key, String value) throws IOException{
    if (type == null) return;
    
    Command cmd = new Command(type);
    
    if (key != null && value != null) cmd.set(key, value);
    
    this.server.send(cmd.toString());
    this.waitingForServerResponse.set(true);
  }
  
  /**
   * Receives from the server and creates a Command object from the information
   */
  private Command receiveServerResponse() throws IOException, Exception {
    String response = this.server.receive();
    this.waitingForServerResponse.set(false);
    return Command.createFromString(response);
  }

  /**
   * Prompts the user to enter a character, validates it and returns the character
   */
  private String handleChoiceGuessCharacter() throws IOException {
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
  private String handleChoiceGuessWord() throws IOException {
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
  private void handleServerResponse(Command cmd) {
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
