package org.kth.id1212.server.controller;

import org.kth.id1212.server.model.Game;
import org.kth.id1212.server.model.WordList;
import org.kth.id1212.common.Command;
import java.io.IOException;

public class GameController {

  Game game;
  int score = 0;

  public void startGame() throws IOException {
    this.game = this.createGame();
  }

  public void guessChar(Character character) throws Exception {

    if (this.game.getHasEnded()) {
      throw new Exception("Game has ended, start a new game.");
    }

    this.game.guessChar(character);

    if (this.game.getHasEnded()) {
      this.score += this.game.getWon() ? 1 : -1;
    }
  }

  public void guessWord(String word) throws Exception {

    if (this.game.getHasEnded()) {
      throw new Exception("Game has ended, start a new game.");
    }

    this.game.guessWord(word);

    if (this.game.getHasEnded()) {
      this.score += this.game.getWon() ? 1 : -1;
    }
  }

  public Command getState() {

    Command command = new Command("state");
    command.set("letters", this.game.getMaskedWord());
    command.set("remaining_attempts", Integer.toString(this.game.getRemainingAttempts()));
    command.set("score", Integer.toString(this.score));

    return command;
  }

  private Game createGame() throws IOException {

    String wordListFilePath = System.getProperty("user.dir") + "/resources/words.txt";
    WordList wordList = new WordList(wordListFilePath);
    return new Game(wordList.getRandomWord());
  }
}