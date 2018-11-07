package org.kth.id1212.client.view;

import org.kth.id1212.client.net.InputHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * InputTerminalView is responsible for reading input
 */
public class InputTerminalView implements InputHandler {

  private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

  @Override
  public String retrieveInput() throws IOException {
    return inputReader.readLine();
  }
}
