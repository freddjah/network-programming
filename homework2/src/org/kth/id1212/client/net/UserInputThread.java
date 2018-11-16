package org.kth.id1212.client.net;

import org.kth.id1212.client.controller.GameController;

public class UserInputThread extends Thread {
  GameController controller;

  public UserInputThread(GameController controller) {
    this.controller = controller;
  }

  @Override
  public void run() {
    try {
      this.controller.showStartGameInformation();
      this.controller.showQuitGameInformation();

      while(true) {
        String userInput = this.controller.getInputHandler().retrieveInput();

        // User wants to guess a character
        if (userInput.equals(GameController.CMD_GUESS_CHAR) && this.controller.isGameInitiated() && !this.controller.isWaitingForServerResponse()) {
            String character = this.controller.handleChoiceGuessCharacter();
            this.controller.sendCommandToServer("guess_char", "char", character);
        }

        // User wants to guess a word
        else if (userInput.equals(GameController.CMD_GUESS_WORD) && this.controller.isGameInitiated() && !this.controller.isWaitingForServerResponse()) {
            String word = this.controller.handleChoiceGuessWord();
            this.controller.sendCommandToServer("guess_word", "word", word);
        }

        // User wants to quit
        else if (userInput.equals(GameController.CMD_QUIT)) {
            this.controller.shutDownClient();
        }

        // User wants to start a new game
        else if (userInput.length() == 0 && !this.controller.isGameInitiated()) {
            this.controller.sendCommandToServer("start_game", null, null);
        }

        // Game is live, but unknown command
        else if (this.controller.isGameInitiated() && !this.controller.isWaitingForServerResponse()) {
            this.controller.showGameInitiatedInformation();
            this.controller.showQuitGameInformation();
        }

        // Game is live, waiting for server response but unknown command
        else if (this.controller.isGameInitiated() && this.controller.isWaitingForServerResponse()) {
            this.controller.showQuitGameInformation();
        } else {
            this.controller.showStartGameInformation();
            this.controller.showQuitGameInformation();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Something went wrong with Terminal. Shutting down.");
      System.exit(-1);
    }
  }
}
