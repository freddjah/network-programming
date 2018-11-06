package org.kth.id1212.client.view;

import org.kth.id1212.client.net.OutputHandler;

public class OutputTerminalView implements OutputHandler {

  @Override
  public synchronized void print(String message) {
    System.out.print(message);
  }

  @Override
  public synchronized void println(String message) {
    System.out.println(message);
  }
}
