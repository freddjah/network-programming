package org.kth.id1212.server.net;

import org.kth.id1212.common.Command;
import org.kth.id1212.server.controller.GameController;
import org.kth.id1212.server.model.WordList;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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

          ServerSocketChannel server = (ServerSocketChannel) key.channel();
          SocketChannel channel = server.accept();
          channel.configureBlocking(false);
          channel.register(this.selector, SelectionKey.OP_READ, new Attachment());

        } else if (key.isReadable()) {

          SocketChannel channel = (SocketChannel) key.channel();
          Attachment attachment = (Attachment) key.attachment();
          channel.read(attachment.getBuffer());

          this.parseCommand(attachment.getBuffer(), attachment.getGameController(), key);

        } else if (key.isWritable()) {

          SocketChannel channel = (SocketChannel) key.channel();
          Attachment attachment = (Attachment) key.attachment();

          ByteBuffer bytesToSend = ByteBuffer.wrap(attachment.gameController.getState().toString().getBytes());
          channel.write(bytesToSend);
          if (bytesToSend.hasRemaining()) {
            bytesToSend.compact();
          } else {
            bytesToSend.clear();
          }

          key.interestOps(SelectionKey.OP_READ);
        }
      }
    }
  }

  void parseCommand(ByteBuffer buffer, GameController controller, SelectionKey key) throws Exception {

    String clientCommand = this.messageFromBuffer(buffer);
    Command command = Command.createFromString(clientCommand.trim());
    String type = command.get("type");

    if (type.equals("start_game")) {

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
        controller.startGame(word);
        key.interestOps(SelectionKey.OP_WRITE);
      });
    } else if (type.equals("guess_char")) {
      controller.guessChar(command.get("char").charAt(0));
      key.interestOps(SelectionKey.OP_WRITE);
    } else if (type.equals("guess_word")) {
      controller.guessWord(command.get("word"));
      key.interestOps(SelectionKey.OP_WRITE);
    }

    buffer.clear();
  }

  private static String messageFromBuffer(ByteBuffer buffer) {
    buffer.flip();
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    return new String(bytes);
  }
}