package org.kth.id1212.client.model;

public class GameViewModel {
  private String currentWord;
  private int score;
  private int remainingAttempts;

  /**
   * A representation of a game.
   */
  public GameViewModel(String currentWord, int score, int remainingAttempts) {
    this.currentWord = currentWord;
    this.score = score;
    this.remainingAttempts = remainingAttempts;
  }

  public String getCurrentWord() {
    return currentWord;
  }

  public int getScore() {
    return score;
  }

  public int getRemainingAttempts() {
    return remainingAttempts;
  }

  public boolean getGameStarted() {
    return this.currentWord.contains("_");
  }

  @Override
  public String toString() {
    String formattedCurrentWord = currentWord.replace("", " ").trim();
    return "CURRENT SCOREBOARD: " + formattedCurrentWord + " || Current Score: " + score + " || Remaining attempts: " + remainingAttempts + "\n";
  }
}
