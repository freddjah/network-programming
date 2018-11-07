package org.kth.id1212.client.net;

import org.kth.id1212.common.InvalidCommandException;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ServerHandler extends Thread {

  private Socket connection;
  private DataOutputStream requestDataStream;
  private BufferedReader responseDataReader;

  /**
   * The ServerHandler is responsible for handling the connection to the server.
   * @param serverUrl
   * @param serverPort
   */
  public ServerHandler(String serverUrl, int serverPort) throws IOException {
    try {
      this.connection = new Socket(serverUrl, serverPort);
    } catch (SocketException e) {
      System.out.println("Server is not responding. Shutting down.");
      System.exit(-1);
    }
    this.requestDataStream = new DataOutputStream(this.connection.getOutputStream());
    this.responseDataReader = new BufferedReader(new InputStreamReader(this.connection.getInputStream()));
  }

  /**
   * Sends a message
   */
  public void send(String message) throws IOException {
    String responseLength = String.format("%06d", message.length());
    this.requestDataStream.writeBytes(responseLength + message);
  }

  /**
   * Receives a message
   */
  public String receive() throws IOException, InvalidCommandException {
    int contentLength = 0;

    try {
      contentLength = Integer.parseInt(this.read(6));
    } catch (NumberFormatException e) {
      e.printStackTrace();
      throw new InvalidCommandException("Could not parse command.");
    } catch (SocketException e) {
      System.out.println("Server is not responding. Shutting down.");
      System.exit(-1);
    }

    return this.read(contentLength);
  }


  /**
   * Reads chosen amount of bytes from the server.
   */
  private String read(int bytesToRead) throws IOException {

    StringBuilder sb = new StringBuilder();

    while (bytesToRead > 0) {

      int charCode = this.responseDataReader.read();

      if (charCode == -1) {
        System.out.println("Server is not responding. Shutting down.");
        System.exit(-1);
      }

      sb.append((char) charCode);
      bytesToRead--;
    }

    return sb.toString();
  }
}
