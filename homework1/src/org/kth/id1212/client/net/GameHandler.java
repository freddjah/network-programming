package org.kth.id1212.client.net;

import org.kth.id1212.client.controller.GameController;
import org.kth.id1212.client.model.GameViewModel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.IOException;

/**
 * GameHandler
 */
public class GameHandler {

  protected GameController controller;
  protected AtomicBoolean waitingForServerResponse = new AtomicBoolean(false);
  protected AtomicBoolean gameInitiated = new AtomicBoolean(false);

  /**
   * The GameHandles creates two threads, one who's responsible for user interaction and another thread that is responsible for capturing server responses
   * @param controller A GameController that has necessary methods that each thread can use.
   */
  public GameHandler(GameController controller) {
    this.controller = controller;

    // Responsible for handling user interaction
    Thread userInputHandler = new Thread(() -> {
      while (true) {
        // If no game is active
        if (!this.gameInitiated.get() && !this.waitingForServerResponse.get()) {
          try {
            this.waitingForServerResponse.set(true);
            this.controller.startGame();
            this.controller.loading(this.waitingForServerResponse);
          } catch (IOException e) {
            e.printStackTrace();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } 
        // If a game is started and we are not awaiting a server response.
        else if (this.gameInitiated.get() && !this.waitingForServerResponse.get()) {
          try {
            String choice = this.controller.initiateUserGuess();

            if (choice.equals("1")) {
              this.controller.guessCharacter();
              this.waitingForServerResponse.set(true);
              this.controller.loading(this.waitingForServerResponse);
            } else if (choice.equals("2")) {
              this.controller.guessWord();
              this.waitingForServerResponse.set(true);
              this.controller.loading(this.waitingForServerResponse);
            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    });

    // Responsible for capturing server responses and prompting the UI with the received score information.
    Thread serverResponseHandler = new Thread(() -> {
      while(true) {
        try {
          GameViewModel game = controller.receiveServerResponse();
          this.gameInitiated.set(game.getGameStarted());
          this.controller.printScore(game);
          this.waitingForServerResponse.set(false);
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });

    userInputHandler.start();
    serverResponseHandler.start();
  }
}
