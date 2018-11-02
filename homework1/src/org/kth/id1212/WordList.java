package org.kth.id1212;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;;

public class WordList {

  String filePath;

  WordList(String filePath) {
    this.filePath = filePath;
  }

  public String getRandomWord() throws IOException {

    ArrayList<String> words = this.readWordsFromFile();
    String word = words.get((new Random()).nextInt(words.size()));
    return word.toLowerCase();
  }

  private ArrayList<String> readWordsFromFile() throws IOException {

    ArrayList<String> words = new ArrayList<String>();

    BufferedReader reader = new BufferedReader(new FileReader(this.filePath));
    String word = reader.readLine();
    while (word != null) {

      words.add(word);
      word = reader.readLine();
    }

    reader.close();

    return words;
  }
}