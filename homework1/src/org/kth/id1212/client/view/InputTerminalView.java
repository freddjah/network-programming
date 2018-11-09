package org.kth.id1212.client.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * InputTerminalView is responsible for reading input
 */
public class InputTerminalView {

  private BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

  public String retrieveInput() throws IOException {
    return inputReader.readLine();
  }
}
