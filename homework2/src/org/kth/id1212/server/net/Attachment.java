package org.kth.id1212.server.net;

import org.kth.id1212.server.controller.GameController;

import java.nio.ByteBuffer;

public class Attachment {

  ByteBuffer buffer = ByteBuffer.allocate(1024);
  GameController gameController = new GameController();
  Exception exception;

  public GameController getGameController() {
    return this.gameController;
  }

  public ByteBuffer getBuffer() {
    return this.buffer;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public Exception getException() {
    return this.exception;
  }

  public boolean hasException() {
    return this.exception != null;
  }
}
