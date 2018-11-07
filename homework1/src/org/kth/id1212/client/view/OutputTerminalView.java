package org.kth.id1212.client.view;

/**
 * Responsible for handling thread safe terminal outputs.
 */
public class OutputTerminalView {

  public synchronized void print(String message) {
    System.out.print(message);
  }

  public synchronized void println(String message) {
    System.out.println(message);
  }
}
