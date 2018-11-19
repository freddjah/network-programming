package org.kth.id1212.client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

public class ServerHandler extends Thread {
  private String serverUrl;
  private int serverPort;

  private SocketChannel socketChannel;
  private ByteBuffer msgFromServer = ByteBuffer.allocateDirect(1024);
  private boolean serverConnectionEstablished = false;
  private Selector selector;

  private final LinkedBlockingQueue<String> outgoingMessages = new LinkedBlockingQueue<>();
  private final LinkedBlockingQueue<String> incomingMessages = new LinkedBlockingQueue<>();

  /**
   * The ServerHandler is responsible for handling the connection to the server.
   *
   * @param serverUrl
   * @param serverPort
   */
  public ServerHandler(String serverUrl, int serverPort) {
    this.serverUrl = serverUrl;
    this.serverPort = serverPort;
  }

  @Override
  public void run() {
    try {
      initializeConnection();
      initializeSelector();

      while (this.serverConnectionEstablished) {

        if (!this.outgoingMessages.isEmpty()) {
          this.socketChannel.keyFor(this.selector).interestOps(SelectionKey.OP_WRITE);
        }

        selector.select();
        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
        while (keys.hasNext()) {

          SelectionKey key = keys.next();
          keys.remove();

          if (!key.isValid()) {
            continue;
          }

          if (key.isConnectable()) {
            connect(key);
          } else if (key.isReadable()) {
            receiveMessage();
          } else if (key.isWritable()) {
            sendMessage(key);
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Something went wrong with the server connection. Shutting down");
      System.exit(0);
    }
  }

  public void shutDownConnection() throws IOException {
    //this.socketChannel.close();
  }

  /**
   * Fetch message received from server
   * @return String
   */
  public String retrieveMessage() {
    synchronized (this.incomingMessages) {
      try {
        while (this.incomingMessages.isEmpty()) this.incomingMessages.wait();
        return this.incomingMessages.take();
      } catch (InterruptedException e) {
        System.out.println("Something went wrong with the server connection. Shutting down");
        System.exit(0);
        return null;
      }
    }
  }

  private void receiveMessage() {
    synchronized (incomingMessages) {
      try {
        this.msgFromServer.clear();
        int numOfReadBytes = socketChannel.read(msgFromServer);

        if (numOfReadBytes == -1) {
          throw new Exception("Could not read");
        }

        String message = messageFromBuffer(this.msgFromServer);
        incomingMessages.put(message);
        incomingMessages.notify();
      } catch (Exception e) {
        System.out.println("Game server connection was terminated. Shutting down client.");
        System.exit(0);
      }
    }
  }

  /**
   * Add message to outgoing queue
   */
  public void addMessage(String message) {
    synchronized (this.outgoingMessages) {
      try {
        this.outgoingMessages.put(message);
        this.selector.wakeup();
      } catch (InterruptedException e) {
        System.out.println("Something went wrong with the server connection. Shutting down");
        System.exit(0);
      }
    }
  }

  private void sendMessage(SelectionKey key) {
    synchronized (this.outgoingMessages) {
      try {
        ByteBuffer messageBuffer = ByteBuffer.wrap(this.outgoingMessages.take().getBytes());
        this.socketChannel.write(messageBuffer);

        if (messageBuffer.hasRemaining()) messageBuffer.compact();
        else messageBuffer.clear();

        key.interestOps(SelectionKey.OP_READ);
      } catch (Exception e) {
        System.out.println("Something went wrong with the server connection. Shutting down");
        System.exit(0);
      }

    }
  }

  private static String messageFromBuffer(ByteBuffer buffer) {
    buffer.flip();
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    return new String(bytes);
  }

  private void initializeConnection() throws IOException {
    this.socketChannel = SocketChannel.open();
    this.socketChannel.configureBlocking(false);
    this.socketChannel.connect(new InetSocketAddress(serverUrl, serverPort));
    this.serverConnectionEstablished = true;
  }

  private void initializeSelector() throws IOException {
    this.selector = Selector.open();
    this.socketChannel.register(selector, SelectionKey.OP_CONNECT);
  }

  private void connect(SelectionKey key) throws IOException {
    socketChannel.finishConnect();
    key.interestOps(SelectionKey.OP_READ);
  }
}
