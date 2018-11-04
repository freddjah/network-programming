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

        int contentLength;

        try {
          contentLength = Integer.parseInt(this.read(6));
          System.out.println("Content length is: " + contentLength);
        } catch (NumberFormatException e) {
          throw new InvalidCommandException("Could not parse command.");
        }

        String clientCommand = this.read(contentLength);
        System.out.println("Client command: " + clientCommand);

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

        String responseState = this.gameController.getState().toString() + "\n";
        String responseLength = String.format("%06d", out.length());
        this.clientWriter.writeBytes(responseLength + responseState);

      } catch (InvalidCommandException e) {
        this.handleInvalidCommand(e);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private String read(int bytes) throws IOException {

    char[] cbuf = new char[bytes];
    this.clientReader.read(cbuf, 0, bytes);

    return String.copyValueOf(cbuf);
  }

  private void handleInvalidCommand(InvalidCommandException invalidCommandException) {

    Command command = new Command("error");
    command.set("message", invalidCommandException.getMessage());

    try {
      this.clientWriter.writeBytes(command.toString() + "\n");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}