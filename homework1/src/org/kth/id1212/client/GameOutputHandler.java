package org.kth.id1212.client;

public class GameOutputHandler {

    static void showCurrentScore(String currentWord, int currentScore, int remainingAttempts) {
        String formattedCurrentWord = currentWord.replace("", " ").trim();
        System.out.println(formattedCurrentWord + " || Current Score: " + currentScore + " || Remaining attempts: " + remainingAttempts);
    }
}
