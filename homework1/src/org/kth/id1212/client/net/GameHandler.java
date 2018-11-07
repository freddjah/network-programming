package org.kth.id1212.client.net;

import org.kth.id1212.client.controller.GameController;
import org.kth.id1212.client.model.GameViewModel;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.IOException;

/**
 * GameHandler
 */
public class GameHandler extends Thread {

  protected GameController controller;
  protected AtomicBoolean waitingForServerResponse = new AtomicBoolean(false);
  protected AtomicBoolean gameInitiated = new AtomicBoolean(false);

  public GameHandler(GameController controller) {
    this.controller = controller;

    new GameLoop(this).start();
    this.start();
  }

  @Override
  public void run() {
    while(true) {
      try {
        GameViewModel game = controller.receiveServerResponse();
        this.gameInitiated.set(game.getGameStarted());
        this.controller.printScore(game, this.gameInitiated.get());
        this.waitingForServerResponse.set(false);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
}

  /**
   * GameLoop
   */
  public class GameLoop extends Thread {
    private GameHandler handler;

    GameLoop(GameHandler handler) {
      this.handler = handler;
    }

    @Override
    public void run() {
      while (true) {
        if (!this.handler.gameInitiated.get() && !this.handler.waitingForServerResponse.get()){
          try {
            this.handler.waitingForServerResponse.set(true);
            this.handler.controller.startGame();
            this.handler.controller.loading(this.handler.waitingForServerResponse);
          } catch (IOException e) {
            e.printStackTrace();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else if (this.handler.gameInitiated.get() && !this.handler.waitingForServerResponse.get()){
          try {
            String choice = this.handler.controller.initiateUserGuess();

            if (choice.equals("1")) {
              this.handler.controller.guessCharacter();
              this.handler.waitingForServerResponse.set(true);
              this.handler.controller.loading(this.handler.waitingForServerResponse);
            } else if (choice.equals("2")) {
              this.handler.controller.guessWord();
              this.handler.waitingForServerResponse.set(true);
              this.handler.controller.loading(this.handler.waitingForServerResponse);
            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }
  }
}
