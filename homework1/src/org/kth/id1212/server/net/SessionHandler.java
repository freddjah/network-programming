package org.kth.id1212.server.net;

import java.io.*;
import java.net.*;
import org.kth.id1212.server.controller.GameController;
import org.kth.id1212.server.model.Game;
import org.kth.id1212.common.Command;
import org.kth.id1212.common.InvalidCommandException;

public class SessionHandler extends Thread {

  Socket client;
  BufferedReader clientReader;
  DataOutputStream clientWriter;
  GameController gameController;

  public SessionHandler(Socket client) throws IOException {

    this.client = client;
    this.clientReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
    this.clientWriter = new DataOutputStream(client.getOutputStream());
    this.gameController = new GameController();
  }

  public void run() {

    System.out.println("Client connected");

    while (true) {

      try {

        String length = this.read(6);
        int contentLength = Integer.parseInt(length);

        String clientCommand = this.read(contentLength);
        System.out.println("Client command: " + clientCommand);

        Command command = Command.createFromString(clientCommand);
        String type = command.get("type");

        if (type.equals("start_game")) {
          this.gameController.startGame();
        } else if (type.equals("guess_char")) {
          this.gameController.guessChar(command.get("char").charAt(0));
        } else if (type.equals("guess_word")) {
          this.gameController.guessWord(command.get("word"));
        }

        this.write(this.gameController.getState().toString());

      } catch (InvalidCommandException e) {
        this.handleInvalidCommand(e);
      } catch (Exception e) {
        e.printStackTrace();
        break;
      }
    }
  }

  private String read(int bytesToRead) throws IOException {

    StringBuilder sb = new StringBuilder();

    while (bytesToRead > 0) {
      sb.append((char) this.clientReader.read());
      bytesToRead--;
    }

    return sb.toString();
  }

  private void write(String message) throws Exception {

    String length = String.format("%06d", message.length());
    this.clientWriter.writeBytes(length + message);
  }

  private void handleInvalidCommand(InvalidCommandException invalidCommandException) {

    Command command = new Command("error");
    command.set("message", invalidCommandException.getMessage());

    try {
      this.write(command.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
