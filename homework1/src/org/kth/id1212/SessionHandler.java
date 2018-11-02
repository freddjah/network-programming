package org.kth.id1212;

import java.io.*;
import java.net.*;

public class SessionHandler extends Thread {

  Socket client;
  BufferedReader clientReader;
  DataOutputStream clientWriter;
  GameController gameController;

  SessionHandler(Socket client) throws IOException {

    this.client = client;
    this.clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    this.clientWriter = new DataOutputStream(client.getOutputStream());
    this.gameController = new GameController();
  }

  public void run() {

    System.out.println("Client connected");

    while (true) {

      try {

        String clientCommand = this.clientReader.readLine();
        if (clientCommand == null) {
          System.out.println("Client proably disconnected");
          break;
        }

        Command command = Command.createFromString(clientCommand);

        String type = command.get("type");

        if (type.equals("start_game")) {
          this.gameController.startGame();
        } else if (type.equals("guess_char")) {
          this.gameController.guessChar(command.get("char").charAt(0));
        } else if (type.equals("guess_word")) {
          this.gameController.guessWord(command.get("word"));
        }

        this.clientWriter.writeBytes(this.gameController.getState().toString() + "\n");

      } catch (InvalidCommandException e) {
        this.handleInvalidCommand(e);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  void handleInvalidCommand(InvalidCommandException invalidCommandException) {

    Command command = new Command("error");
    command.set("message", invalidCommandException.getMessage());

    try {
      this.clientWriter.writeBytes(command.toString() + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}