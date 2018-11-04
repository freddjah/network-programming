package org.kth.id1212.client;

public class Game {
    private int remainingAttempts = 0;
    private int currentScore = 0;
    private String currentWord = null;

    public Game() {

    }

    public synchronized void setRemainingAttempts(int remainingAttempts) {
        this.remainingAttempts = remainingAttempts;
    }

    public synchronized int getRemainingAttempts() {
        return remainingAttempts;
    }

    public synchronized int getCurrentScore() {
        return currentScore;
    }

    public synchronized void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public synchronized String getCurrentWord() {
        return currentWord;
    }

    public synchronized void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

}
