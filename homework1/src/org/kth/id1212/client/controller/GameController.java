package org.kth.id1212.client.controller;

import org.kth.id1212.client.net.ServerHandler;
import org.kth.id1212.client.net.OutputHandler;
import org.kth.id1212.client.net.InputHandler;
import org.kth.id1212.client.view.InputTerminalView;
import org.kth.id1212.client.view.OutputTerminalView;
import org.kth.id1212.common.Command;
import org.kth.id1212.client.model.GameViewModel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.IOException;

public class GameController extends Thread {
  private ServerHandler server;
  private OutputHandler outputHandler;
  private InputHandler inputHandler;

  private AtomicBoolean waitingForServerResponse = new AtomicBoolean(false);
  private AtomicBoolean gameStarted = new AtomicBoolean(false);

  public GameController(ServerHandler server) {
    this.server = server;
  }

  public void setOutputHandler(OutputHandler output) {
    this.outputHandler = output;
  }

  public void setInputHandler(InputHandler input) {
    this.inputHandler = input;
  }

  public void startGame() throws IOException {
    this.waitingForServerResponse.set(true);
    this.outputHandler.println("Press ANY key to start a new game");
    this.inputHandler.retrieveInput();

    Command command = new Command("start_game");
    server.send(command.toString());
  }


  public void guessCharacter() throws Exception {
    this.waitingForServerResponse.set(true);
    this.outputHandler.println("Type a character and press <ENTER>.");
    String character = this.inputHandler.retrieveInput();

    Command command = new Command("guess_char");
    command.set("char", character);

    server.send(command.toString());
  }

  public void guessWord() throws IOException {
    this.waitingForServerResponse.set(true);
    this.outputHandler.println("Type a word and press <ENTER>.");
    String word = this.inputHandler.retrieveInput();;

    Command command = new Command("guess_word");
    command.set("word", word);

    server.send(command.toString());
  }


  private void receiveServerResponse() throws Exception {
    String response = server.receive();
    Command command = Command.createFromString(response);

    String letters = command.get("letters");
    int points = Integer.parseInt(command.get("score"));
    int attemptsRemaining = Integer.parseInt(command.get("remaining_attempts"));

    GameViewModel game = new GameViewModel(letters, points, attemptsRemaining);

    if (gameStarted.get()) {
      this.outputHandler.println("\n" + game.toString());
    } else {
      this.outputHandler.println(game.toString());
    }

    this.gameStarted.set(game.getGameStarted());
    this.waitingForServerResponse.set(false);

  }

  private void startGameLoop() {
    Thread loop = new Thread(() -> {
      while (true) {
        // Sleep to await responses
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        if (!this.gameStarted.get() && !this.waitingForServerResponse.get()){
          try {
            this.startGame();
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else if (this.gameStarted.get() && !this.waitingForServerResponse.get()){
          try {
            this.outputHandler.println("Press 1 and press <ENTER> to guess a character.");
            this.outputHandler.println("Press 2 and press <ENTER> to guess a word.");
            String choice = this.inputHandler.retrieveInput().trim().split("")[0];

            switch (choice) {
              case "1":   this.guessCharacter();
                    break;
              case "2":   this.guessWord();
                    break;
              default:    break;
            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });

    loop.start();
  }

  @Override
  public void run() {
    this.startGameLoop();

    while(true) {
      try {
        receiveServerResponse();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
