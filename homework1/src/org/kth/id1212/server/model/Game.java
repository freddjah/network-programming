package org.kth.id1212.server.model;

import java.util.HashMap;
import java.util.HashSet;

public class Game {

  String word;
  HashSet<Character> guessedChars = new HashSet<Character>();
  int remainingAttempts;

  boolean hasEnded = false;
  boolean won = false;

  /*
   * Create game with a specified word
   */
  public Game(String word) {

    System.out.println("Chosen word: " + word);

    this.word = word;
    this.remainingAttempts = word.length();
  }

  /*
   * Guess a character
   */
  public void guessChar(Character character) {

    character = Character.toLowerCase(character);

    // If the character was guessed earlier, just be nice and let the user keep the attempt
    if (this.guessedChars.contains(character)) {
      return;
    }

    // Add the character to guessed chars
    this.guessedChars.add(character);

    // Decrease the remaining attempts if the guessed character was invalid
    if (word.indexOf(character) == -1) {
      this.remainingAttempts--;
    }

    this.checkHasEnded();
  }

  /*
   * Guess a word
   */
  public void guessWord(String word) {

    word = word.toLowerCase();

    // The guessed word was the right one
    if (this.word.equals(word)) {

      this.setHasEnded(true);

      // Add all characters to unmask the word
      for (int i = 0; i < word.length(); i++) {

        Character character = word.charAt(i);
        this.guessedChars.add(character);
      }

      return;
    }

    // Decrease remaining attempt for invalid word
    this.remainingAttempts--;

    this.checkHasEnded();
  }

  private void checkHasEnded() {

    // End game if no remaining attempts
    if (this.remainingAttempts == 0) {

      this.setHasEnded(false);
      return;
    }

    // End game if all characters was guessed
    boolean allCharactersGuessed = true;
    for (int i = 0; i < word.length(); i++) {

      Character wordChar = word.charAt(i);
      if (!this.guessedChars.contains(wordChar)) {
        allCharactersGuessed = false;
        break;
      }
    }

    if (allCharactersGuessed) {
      this.setHasEnded(true);
    }
  }

  /*
   * Get remaining attempts for this game
   */
  public int getRemainingAttempts() {
    return this.remainingAttempts;
  }

  /*
   * Get the word masked with _ as unknown character
   */
  public String getMaskedWord() {

    String maskedWord = "";
    for (int i = 0; i < this.word.length(); i++) {

      Character character = this.word.charAt(i);
      if (this.guessedChars.contains(character)) {
        maskedWord += character;
      } else {
        maskedWord += '_';
      }
    }

    return maskedWord;
  }

  /*
   * Set that the game has ended
   */
  private void setHasEnded(boolean won) {

    this.hasEnded = true;
    this.won = won;
  }

  /*
   * Get if the game has ended
   */
  public boolean getHasEnded() {
    return this.hasEnded;
  }

  /*
   * Get if this ended game was won
   */
  public boolean getWon() throws Exception {

    if (!this.hasEnded) {
      throw new Exception("Could not return result since the game is not finished yet.");
    }

    return this.won;
  }
}
