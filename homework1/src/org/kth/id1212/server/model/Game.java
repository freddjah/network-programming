package org.kth.id1212.server.model;

import java.util.HashMap;
import java.util.HashSet;

public class Game {

  String word;
  HashSet<Character> guessedChars = new HashSet<Character>();
  int remainingAttempts;

  boolean hasEnded = false;
  boolean won = false;

  public Game(String word) {

    System.out.println("Chosen word: " + word);

    this.word = word;
    this.remainingAttempts = word.length();
  }

  public void guessChar(Character character) {

    character = Character.toLowerCase(character);
    if (this.guessedChars.contains(character)) {
      return;
    }

    this.guessedChars.add(character);

    if (word.indexOf(character) == -1) {
      this.remainingAttempts--;
    }

    if (this.remainingAttempts == 0) {
      this.setHasEnded(false);
      return;
    }

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

  public void guessWord(String word) {

    word = word.toLowerCase();

    if (this.word.equals(word)) {

      this.setHasEnded(true);

      // Add all characters
      for (int i = 0; i < word.length(); i++) {

        Character character = word.charAt(i);
        this.guessedChars.add(character);
      }

      return;
    }

    this.remainingAttempts--;
    if (this.remainingAttempts == 0) {
      this.setHasEnded(false);
    }
  }

  public int getRemainingAttempts() {
    return this.remainingAttempts;
  }

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

  private void setHasEnded(boolean won) {

    this.hasEnded = true;
    this.won = won;
  }

  public boolean getHasEnded() {
    return this.hasEnded;
  }

  public boolean getWon() throws Exception {

    if (!this.hasEnded) {
      throw new Exception("Could not return result since the game is not finished yet.");
    }

    return this.won;
  }
}