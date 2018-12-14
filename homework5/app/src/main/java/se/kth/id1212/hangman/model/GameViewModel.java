package se.kth.id1212.hangman.model;

public class GameViewModel {
    private int score;
    private String letters;
    private int attemptsLeft;

    public GameViewModel(int score, String letters, int attemptsLeft) {
        this.score = score;
        this.letters = letters;
        this.attemptsLeft = attemptsLeft;
    }

    public int getScore() {
        return this.score;
    }

    public String getLetters() {
        return this.letters;
    }

    public int getAttemptsLeft() {
        return this.attemptsLeft;
    }
}
