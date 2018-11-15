package org.kth.id1212.server.controller;

import org.kth.id1212.server.model.Game;
import org.kth.id1212.server.model.WordList;
import org.kth.id1212.common.Command;
import java.io.IOException;

public class GameController {

  Game game;
  int score = 0;

  /*
   * Start a new game
   */
  public void startGame(String word) {
    this.game = new Game(word);
  }

  /*
   * Guess a character
   */
  public void guessChar(Character character) throws Exception {

    if (this.game.getHasEnded()) {
      throw new Exception("Game has ended, start a new game.");
    }

    this.game.guessChar(character);

    this.increaseScoreIfWon();
  }

  /*
   * Guess a word
   */
  public void guessWord(String word) throws Exception {

    if (this.game.getHasEnded()) {
      throw new Exception("Game has ended, start a new game.");
    }

    this.game.guessWord(word);

    this.increaseScoreIfWon();
  }

  /*
   * Increase the score if the last game was won
   */
  private void increaseScoreIfWon() throws Exception {

    if (this.game.getHasEnded()) {
      this.score += this.game.getWon() ? 1 : -1;
    }
  }

  /*
   * Build up a state command to return to client
   */
  public Command getState() {

    Command command = new Command("state");
    command.set("letters", this.game.getMaskedWord());
    command.set("remaining_attempts", Integer.toString(this.game.getRemainingAttempts()));
    command.set("score", Integer.toString(this.score));

    return command;
  }
}
