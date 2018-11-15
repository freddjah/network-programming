package org.kth.id1212.server.net;

import org.kth.id1212.server.controller.GameController;

import java.nio.ByteBuffer;

public class Attachment {

  ByteBuffer buffer = ByteBuffer.allocate(1024);
  GameController gameController = new GameController();

  public GameController getGameController() {
    return this.gameController;
  }

  public ByteBuffer getBuffer() {
    return this.buffer;
  }
}
