package se.kth.id1212.hangman.controller;


import android.widget.EditText;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import se.kth.id1212.hangman.common.Command;
import se.kth.id1212.hangman.model.GameViewModel;
import se.kth.id1212.hangman.net.ServerHandler;

public class GameController {
    private ServerHandler server;

    private AtomicBoolean gameInitiated = new AtomicBoolean(false);
    private AtomicBoolean waitingForServerResponse = new AtomicBoolean(false);

    /**
     * Responsible for the Hangman I/O, spawns to threads (one that handles user input and one that handles server responses)
     */
    public GameController(ServerHandler server) {
        this.server = server;
    }

    public GameViewModel guess(String text) throws Exception {
        if (text.length() > 1) {
            this.sendCommandToServer("guess_word", "word", text);
        } else {
            this.sendCommandToServer("guess_char", "char", text);
        }

        Command response = this.receiveServerResponse();

        return this.createGameViewModel(response);
    }

    private GameViewModel createGameViewModel(Command response) {
        int score = Integer.parseInt(response.get("score"));
        String letters = response.get("letters");
        int remainingAttempts = Integer.parseInt(response.get("remaining_attempts"));

        return new GameViewModel(score, letters, remainingAttempts);
    }

    /**
     * Sends a command to the server and sets the 'waiting for response' flag to true
     */
    private void sendCommandToServer(String type, String key, String value) throws IOException{
        if (type == null) return;

        Command cmd = new Command(type);

        if (key != null && value != null) cmd.set(key, value);

        this.server.send(cmd.toString());
        this.waitingForServerResponse.set(true);
    }

    /**
     * Receives from the server and creates a Command object from the information
     */
    private Command receiveServerResponse() throws Exception {
        String response = this.server.receive();
        this.waitingForServerResponse.set(false);
        return Command.createFromString(response);
    }

    public GameViewModel startGame() throws Exception {
        this.sendCommandToServer("start_game", null, null);
        Command response = receiveServerResponse();

        return this.createGameViewModel(response);
    }
}
