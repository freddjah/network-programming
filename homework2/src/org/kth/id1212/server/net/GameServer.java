package org.kth.id1212.server.net;

import org.kth.id1212.common.Command;
import org.kth.id1212.common.InvalidCommandException;
import org.kth.id1212.server.model.WordList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class GameServer {

  Selector selector;
  EventLoop eventLoop;

  public GameServer() throws Exception {

    this.selector = Selector.open();
    this.eventLoop = new EventLoop(() -> selector.wakeup());
  }

  public void start(int port) throws Exception {

    ServerSocketChannel serverChannel = ServerSocketChannel.open();
    ServerSocket serverSocket = serverChannel.socket();
    serverSocket.bind(new InetSocketAddress(port));
    serverChannel.configureBlocking(false);
    serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);

    while (true) {

      this.selector.select();
      this.eventLoop.run();

      Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
      while (keys.hasNext()) {

        SelectionKey key = keys.next();
        keys.remove();

        if (key.isAcceptable()) {
          this.handleNewConnection(key);
        } else if (key.isReadable()) {
          this.readFromClient(key);
        } else if (key.isWritable()) {
          this.sendToClient(key);
        }
      }
    }
  }

  void handleNewConnection(SelectionKey key) throws IOException {

    System.out.println("Client connected");

    ServerSocketChannel server = (ServerSocketChannel) key.channel();
    SocketChannel channel = server.accept();
    channel.configureBlocking(false);
    channel.register(this.selector, SelectionKey.OP_READ, new Attachment());
  }

  void readFromClient(SelectionKey key) throws Exception {

    SocketChannel channel = (SocketChannel) key.channel();
    Attachment attachment = (Attachment) key.attachment();

    ByteBuffer buffer = attachment.getBuffer();

    channel.read(buffer);
    String clientCommand = this.messageFromBuffer(buffer);
    buffer.clear();

    try {
      this.parseCommand(clientCommand, key);
    } catch (InvalidCommandException e) {

      System.out.println("Got invalid command: \"" + clientCommand + "\"");
      attachment.setException(e);
      key.interestOps(SelectionKey.OP_WRITE);
    }
  }

  void sendToClient(SelectionKey key) throws IOException {

    SocketChannel channel = (SocketChannel) key.channel();
    Attachment attachment = (Attachment) key.attachment();
    String response;

    if (attachment.hasException()) {

      Command errorCommand = new Command("error");
      errorCommand.set("message", attachment.getException().getMessage());
      response = errorCommand.toString();
      attachment.setException(null);
    } else {
      response = attachment.gameController.getState().toString();
    }

    ByteBuffer bytesToSend = ByteBuffer.wrap(response.getBytes());
    channel.write(bytesToSend);
    if (bytesToSend.hasRemaining()) {
      bytesToSend.compact();
    } else {
      bytesToSend.clear();
    }

    this.setReadMode(key);
  }

  void parseCommand(String clientCommand, SelectionKey key) throws Exception {

    Command command = Command.createFromString(clientCommand.trim());
    String type = command.get("type");

    switch (type) {

      case "start_game":
        this.startGame(key);
        break;

      case "guess_char":
        this.guessChar(command.get("char").charAt(0), key);
        break;

      case "guess_word":
        this.guessWord(command.get("word"), key);
        break;

      case "exit":
        this.disconnect(key);
        break;

      default:
        throw new InvalidCommandException("No command with type " + command.get("type"));
    }
  }

  void startGame(SelectionKey key) {

    Attachment attachment = (Attachment) key.attachment();

    this.eventLoop.dispatch(() -> {

      try {
        WordList wordList = new WordList(System.getProperty("user.dir") + "/resources/words.txt");
        return wordList.getRandomWord();
      } catch (IOException e) {
        e.printStackTrace();
      }

      return null;
    }, (Object result) -> {

      String word = (String) result;
      attachment.getGameController().startGame(word);
      this.setWriteMode(key);
    });
  }

  void guessChar(char character, SelectionKey key) throws Exception {

    Attachment attachment = (Attachment) key.attachment();
    attachment.getGameController().guessChar(character);
    this.setWriteMode(key);
  }

  void guessWord(String word, SelectionKey key) throws Exception {

    Attachment attachment = (Attachment) key.attachment();
    attachment.getGameController().guessWord(word);
    this.setWriteMode(key);
  }

  void disconnect(SelectionKey key) throws IOException {

    System.out.println("Disconnecting client");
    SocketChannel channel = (SocketChannel) key.channel();
    channel.close();
  }

  void setReadMode(SelectionKey key) {
    key.interestOps(SelectionKey.OP_READ);
  }

  void setWriteMode(SelectionKey key) {
    key.interestOps(SelectionKey.OP_WRITE);
  }

  private static String messageFromBuffer(ByteBuffer buffer) {
    buffer.flip();
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    return new String(bytes);
  }
}